let
  # Import nixpkgs to be able to supply reasonable default values for
  # the anonymous function this file defines.
  pkgs = import <nixpkgs> {};
in
{ stdenv ? pkgs.stdenv }:
stdenv.mkDerivation rec {
  name = "agatha-front";
  version = "1.0";

  src = ../front;

  nativeBuildInputs = [ ];
  buildInputs = [ ];

  installPhase = ''
    mkdir -p $out
    cp -a dist $out
  '';
}
