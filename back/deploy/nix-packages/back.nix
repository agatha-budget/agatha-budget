# agatha-back.nix
#
# Packages agatha-back.
#
# You can build this file with the following command:
#
#   nix-build back.nix
#
# This will create the symlink result in the current directory.  The
# runnable shell script is result/bin/back.
#

let
  pkgs = import <nixpkgs> {};
in

{
# can be overridden with `yourPackage.override { enableSomething = true; }`
stdenv ? pkgs.stdenv,
fetchurl ? pkgs.fetchurl,
makeWrapper ? pkgs.makeWrapper,
jre ? pkgs.temurin-jre-bin-17
}:

stdenv.mkDerivation rec {
  name = "agatha-back";
  version = "2.4";

  src = [
    ../../build/libs/tresorier-backend-uber.jar
    ../../src/main/resources/db/pg
  ];

  unpackPhase = ''
    runHook preUnpack

    mkdir -pv share/agatha $out/bin
    cp $src[0] share/agatha/${name}-${version}.jar
    cp $src[1] share/agatha/migrations

    runHook postUnpack
  '';

  nativeBuildInputs = [ makeWrapper ];

  installPhase = ''
    cp -a share $out/share
    makeWrapper ${jre}/bin/java $out/bin/agatha-back \
      --add-flags "-Dlogback.configurationFile=logback.xml" \
      --add-flags "-jar $out/share/agatha/${name}-${version}.jar" \
      --set _JAVA_OPTIONS '-Dawt.useSystemAAFontSettings=on' \
      --set _JAVA_AWT_WM_NONREPARENTING 1
  '';
}
