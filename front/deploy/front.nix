let
  pkgs = import <nixpkgs> {};
in

{
# can be overridden with `yourPackage.override { enableSomething = true; }`
  stdenv ? pkgs.stdenv,
  makeWrapper ? pkgs.makeWrapper,
  http-server ? pkgs.http-server
}:

stdenv.mkDerivation rec {
  name = "agatha-front";
  version = "2.4";

  src = ../dist;

  nativeBuildInputs = [ makeWrapper];
  buildInputs = [ ];

  # the makeWrapper define a /bin/agatha-front executable
  # that will serve the /share/agatha-front on port 5273 with a simple http-server 
  installPhase = ''
    mkdir -p $out/share
    cp -a . $out/share/agatha-front
    makeWrapper ${http-server}/bin/http-server $out/bin/agatha-front \
      --add-flags "$out/share/agatha-front" \
      --add-flags "-p" \
      --add-flags "5173"
  '';
}