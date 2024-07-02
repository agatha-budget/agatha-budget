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
  # Import nixpkgs to be able to supply reasonable default values for
  # the anonymous function this file defines.
  pkgs = import <nixpkgs> {};
in
# These arguments define the resources (packages and native Nix tools)
# that will be used by the package (the anonymous function I define).
# I want this file to be buildable directly using the command
# nix-build back.nix, so I have to supply reasonable default values
# to these arguments.  The default values naturally come from the
# corresponding attributes of Nixpkgs, visible here under the binding
# pkgs.
{ stdenv ? pkgs.stdenv
, fetchurl ? pkgs.fetchurl
, makeWrapper ? pkgs.makeWrapper
, jre ? pkgs.temurin-jre-bin
}:

# I'll use the default builder, because I don't need any particular
# features.
stdenv.mkDerivation rec {
  name = "agatha-back";
  version = "2.4";

  # Simply fetch the JAR file of agatha-back.
  src = ../back/build/libs/tresorier-backend-uber.jar;
  # I fetch the JAR file directly, so no archives to unpack.
  dontUnpack = true;

  # I need makeWrapper in my build environment to generate the wrapper
  # shell script.  This shell script will call the Java executable on
  # the JAR file of agatha-back and will set the appropriate environment
  # variables.
  nativeBuildInputs = [ makeWrapper ];

  # The only meaningful phase of this build.  I create the
  # subdirectory share/java/ in the output directory, because this is
  # where JAR files are typically stored.  I also create the
  # subdirectory bin/ to store the executable shell script.  I then
  # copy the downloaded JAR file to $out/share/java/.  Once this is
  # done, I create the wrapper shell script using makeWrapper.  This
  # script wraps the Java executable (${jre}/bin/java) in the output
  # shell script file $out/bin/agatha-back.  The script adds the argument
  # -jar … to the Java executable, thus pointing it to the actual
  # agatha-back JAR file.  On my system (NixOS + XMonad), I need to set
  # some additional environment variables to get Java windows to
  # render properly.
  installPhase = ''
  mkdir -pv $out/share/java $out/bin
  cp ${src} $out/share/java/${name}-${version}.jar

  makeWrapper ${jre}/bin/java $out/bin/agatha-back \
    --add-flags "-jar $out/share/java/${name}-${version}.jar" \
    --set _JAVA_OPTIONS '-Dawt.useSystemAAFontSettings=on' \
    --set _JAVA_AWT_WM_NONREPARENTING 1
  '';
}
