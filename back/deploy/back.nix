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
# Define all the required libraries and give a default value for each
stdenv ? pkgs.stdenv,
fetchurl ? pkgs.fetchurl,
makeWrapper ? pkgs.makeWrapper,
jre ? pkgs.temurin-jre-bin-17,
}:

# Create a derivation (aka, recipe to create the package)
stdenv.mkDerivation rec {
  name = "agatha-back";
  version = "2.4";

  # the src files/folders will be accessible in the install Phase of your package
  src = [
    ../build/libs/tresorier-backend-uber.jar
    ../src/main/resources/db/pg
  ];
  dontUnpack = true; 

  nativeBuildInputs = [ makeWrapper ];

  # the cp put the jar and migrations in /share/agatha  
  # the makeWrapper define a /bin/agatha-back executable
  # that will run the jar with the given options 
  installPhase = ''
    mkdir -pv $out/share/agatha $out/bin
    cp $src[0] $out/share/agatha/${name}-${version}.jar
    cp $src[1] $out/share/agatha/migrations 
    makeWrapper ${jre}/bin/java $out/bin/agatha-back \
      --add-flags "-Dlogback.configurationFile=logback.xml" \
      --add-flags "-jar $out/share/agatha/${name}-${version}.jar" \
      --set _JAVA_OPTIONS '-Dawt.useSystemAAFontSettings=on' \
      --set _JAVA_AWT_WM_NONREPARENTING 1
  '';
}
