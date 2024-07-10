---
title: Creating docker images with nix
description: Understanding how Agatha create its docker image
---

## Why 

As a nix user myself, I wanted to create a deployment sytem that could be derived both as a nix package (my end goal for personal use) and as a set of docker images (for production use). 
Being used to working in a reproducible environment with a nix-shell but aware that Docker was the industry standard, I did some research on the complementarity of both tools. 

- Docker insures a consistant runtime environment. Once the image is built, you're good to go. But if you need to rebuild it, you don't have any guarantee you will be able to recreate it.
- Nix on the other hand ensure a reproducible build. No matter when you retrieve the source, if you rebuilt it you will have the exact same result.

Is Docker still relevant then ? (was it worth it for me to learn it ? was my main question at first)
Docker has a formidable deployment ecosystem that ensure you can easily deploy your application anywhere. Moreover, most developer are used to using it and will be glad to keep a familiar setup 

## How ?

### A) Creating a Docker image for my Kotlin backend

#### 1) Creating a nix package

There seems to be multiple approach, one where you start from your raw code source (see Gradle2Nix) and one where you start with the jar. 
I decided to keep it simple for now and went with the jar option.

```nix title="deploy/nix-packages/back.nix"
let
  pkgs = import <nixpkgs> {};
in

{
# Import all the required library and only those. 
stdenv ? pkgs.stdenv,
fetchurl ? pkgs.fetchurl,
makeWrapper ? pkgs.makeWrapper,
jre ? pkgs.temurin-jre-bin-17
}:

# Create a derivation (aka, recipe to create the package)
stdenv.mkDerivation rec {
  name = "agatha-back";
  version = "2.4";

  # the src file/folder will be accessible in your package
  src = ../../build/libs/tresorier-backend-uber.jar; 
  dontUnpack = true;

  nativeBuildInputs = [ makeWrapper ];

  # the cp put the jar /share/java 
  # the makeWrapper define a /bin/agatha-back executable
  # that will run the jar with the given options 
  installPhase = ''
  mkdir -pv $out/share/java $out/bin
  cp ${src} $out/share/java/${name}-${version}.jar

  makeWrapper ${jre}/bin/java $out/bin/agatha-back \
    --add-flags "-Dlogback.configurationFile=logback.xml" \
    --add-flags "-jar $out/share/java/${name}-${version}.jar" \
    --set _JAVA_OPTIONS '-Dawt.useSystemAAFontSettings=on' \
    --set _JAVA_AWT_WM_NONREPARENTING 1
  '';
}
```

I can now create a nix package by running 
```sh
nix-build back.nix
``` 

<details>

<summary>See the console output</summary>

```sh 
[erica@xiangu:~/_Agatha/code/app/back/deploy/nix-packages]$ nix-build back.nix
this derivation will be built:
  /nix/store/r3mm6cwws2w07gn6v56c3nxgac3ihw5i-agatha-back.drv
building '/nix/store/r3mm6cwws2w07gn6v56c3nxgac3ihw5i-agatha-back.drv'...
Running phase: patchPhase
Running phase: updateAutotoolsGnuConfigScriptsPhase
Running phase: configurePhase
no configure script, doing nothing
Running phase: buildPhase
no Makefile or custom buildPhase, doing nothing
Running phase: installPhase
mkdir: created directory '/nix/store/q47aidmp4akf08ipmqr7xxyd5gdc8kdq-agatha-back'
mkdir: created directory '/nix/store/q47aidmp4akf08ipmqr7xxyd5gdc8kdq-agatha-back/share'
mkdir: created directory '/nix/store/q47aidmp4akf08ipmqr7xxyd5gdc8kdq-agatha-back/share/java'
mkdir: created directory '/nix/store/q47aidmp4akf08ipmqr7xxyd5gdc8kdq-agatha-back/bin'
Running phase: fixupPhase
shrinking RPATHs of ELF executables and libraries in /nix/store/q47aidmp4akf08ipmqr7xxyd5gdc8kdq-agatha-back
checking for references to /build/ in /nix/store/q47aidmp4akf08ipmqr7xxyd5gdc8kdq-agatha-back...
patching script interpreter paths in /nix/store/q47aidmp4akf08ipmqr7xxyd5gdc8kdq-agatha-back
stripping (with command strip and flags -S -p) in  /nix/store/q47aidmp4akf08ipmqr7xxyd5gdc8kdq-agatha-back/bin
/nix/store/q47aidmp4akf08ipmqr7xxyd5gdc8kdq-agatha-back
```

</details>


The package is stored by default in my nix store, I can call it using 
```sh
# replace with the correct hash given by the output of the build
/nix/store/q47aidmp4akf08ipmqr7xxyd5gdc8kdq-agatha-back/bin/agatha-back
```

*the hash is the signature of the package, being reproducible it will always be the same given the same input but any changes (to the config or the jar for exemple) will result in a distinct hash so that both version of the package can co-exist peacefully*

The build command will also create a symlink locally in a result folder. I can call it using 
```sh
./result/bin/agatha-back
``` 
and here we are, my package is running !

#### 2) Creating a docker image

Again, there are multiple ways to do it.

You could go with a classic Dockerfile but I wanted to experiment with a full nix option (besides, I had read that [Nix is a better Docker image builder than Docker's image builder, by Xe](https://xeiaso.net/talks/2024/nix-docker-build/))

It isn't well referenced but my main source of information was the official [nixpkgs documentation for DockerTools](https://nixos.org/manual/nixpkgs/stable/#sec-pkgs-dockerTools)

So here we are :

```nix title="deploy/docker-images/back-docker.nix"
# Import all the required library and only those. 
{ 
  pkgs ? import <nixpkgs> { system = builtins.currentSystem; },
  dockerTools ? pkgs.dockerTools,
  agatha-back  ? pkgs.callPackage ../nix-packages/back.nix { }, 
  agatha-migrations ? pkgs.callPackage ../nix-packages/migrations.nix { },
  flyway ? pkgs.flyway,
  bash ? pkgs.bash, # needed to run a double command
}:
dockerTools.buildLayeredImage {
  name = "agatha-back-image";
  tag = "latest";

  contents = [ agatha-back agatha-migrations flyway bash];

  extraCommands = ''
    mkdir -p home
  '';

  config = { 
    ## this commands needs to mounted file : flyway.conf and gradle.properties. see docker-compose
    Cmd = [ "/bin/bash" "-c" "/bin/flyway -configFiles=/home/flyway.conf migrate && /bin/agatha-back" ];
    WorkingDir = "/home";
  };
}
```