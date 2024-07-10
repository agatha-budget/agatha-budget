let
  pkgs = import <nixpkgs> {};
in

{
# can be overridden with `yourPackage.override { enableSomething = true; }`
stdenv ? pkgs.stdenv,
}:

stdenv.mkDerivation rec {
  name = "agatha-migrations";
  version = "2.4";

  src = ./../../src/main/resources/db/pg;

  nativeBuildInputs = [ ];
  buildInputs = [ ];

  installPhase = ''
    mkdir -p $out/share
    cp -a . $out/share/agatha-migrations
  '';
}