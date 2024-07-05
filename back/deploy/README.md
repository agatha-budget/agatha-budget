---
title: Guide to create the container for a web app with nix
description: A guide in my new Starlight docs site.
---

See : https://nixos.org/manual/nixpkgs/stable/#sec-pkgs-dockerTools


## Backend 

- First create a nix-package for your backend :

 back.nix

```

let
  pkgs = import <nixpkgs> {};
in

{
# can be overridden with `yourPackage.override { enableSomething = true; }`
stdenv ? pkgs.stdenv,
fetchurl ? pkgs.fetchurl,
makeWrapper ? pkgs.makeWrapper,
jre ? pkgs.temurin-jre-bin
}:

stdenv.mkDerivation rec {
  name = "agatha-back";
  version = "2.4";

  src = ../build/libs/tresorier-backend-uber.jar;
  dontUnpack = true;

  nativeBuildInputs = [ makeWrapper ];

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

you can run it on its own with 

```
nix-build back.nix
./result/bin/agatha-back

```

- Then create a docker container to encapsulate it 

```
{ 
  pkgs ? import <nixpkgs> { system = builtins.currentSystem; },
  dockerTools ? pkgs.dockerTools,
  agatha-back  ? pkgs.callPackage ./back.nix { },
}:
dockerTools.buildLayeredImage {
  name = "agatha-back-image";
  tag = "latest";

  contents = [ agatha-back ];

  extraCommands = ''
    mkdir -p home
  '';

  config = { 
    Cmd = [ "/bin/agatha-back" ];
    WorkingDir = "/home";
  };
}
```

you can run it on its own with 

```
nix-build docker.nix
docker load < result
docker run --mount type=bind,source="$(pwd)/gradle.properties",target=/home/gradle.properties --net=host agatha-back-image:latest
```

## Database


- pull the image from dockerhub 

db-docker.nix

```
{ 
  pkgs ? import <nixpkgs> { system = builtins.currentSystem; },
  dockerTools ? pkgs.dockerTools,
}:
dockerTools.pullImage {
  imageName = "postgres";
  imageDigest = "sha256:afcb675cf038e3fc006fe515d407a79dc8e1f829f671dd25b176a8d823be1e7c"; 
  finalImageName = "agatha-db-image";
  finalImageTag = "latest";
  sha256 = "zRwlQs1FiKrvHPaf8vWOR/Tlp1C5eLn1d9pE4BZg3oA=";
}


```

### imageDigest :
must be replaced by the digest that can be found on the page of the image itself : 

![image](/home/erica/Multi/projets/gdr/gdr_sante/doc/src/assets/nix_container_1.png)


See the section Tag in the [dockerHub](https://hub.docker.com/_/postgres) page which will lead you to the [list of all the available images](https://hub.docker.com/_/postgres/tags), each one has multiple digest, click on one of them to get the link itself 


### sha56 :
specify the signature of the final image. set to anything then run ```nix-build db-docker.nix``` will return the following error msg :

```
error: hash mismatch in fixed-output derivation '/nix/store/khm9snx417irsnsrjhg463ws85l0dkdg-docker-image-agatha-db-image-latest.tar.drv':
         specified: sha256-zRwlQs1FiKrvHPaf8vWOR/Tlp1C5eLn1d9pE4BZg3oA=
            got:    sha256-+wzNtWHS+ZwpKCVJZSNE5gVxzErgBOB1LOSesggVTA0=
```

copy the "got" value inside db-docker.nix

## Run Database and Backend together 

https://docs.docker.com/guides/use-case/databases/#connect-to-a-containerized-database-from-another-container

compose.yaml
```
services:

  agatha-db:
    image: agatha-db-image:latest
    environment:
      POSTGRES_PASSWORD: agatha-db-password
      POSTGRES_USER: postgres
      POSTGRES_DB: agatha
    ports:
      - 4321:5432    #HOST:CONTAINER
    volumes:
      - ../db/postgres-data:/var/lib/postgresql/data

  agatha-back:
    image: agatha-back-image:latest
    ports:
      - 8000:8000
    volumes:
      - type: bind
        source: ../gradle.properties
        target: /home/gradle.properties
    depends_on:
      - agatha-db
```

/!\ With this configuration the adress in the gradle.properties must be agatha-db:5432 instead of localhost
