{ stdenv }:

stdenv.mkDerivation rec {
  name = "agatha-front";
  version = "2.4";

  src = ../dist;

  nativeBuildInputs = [ ];
  buildInputs = [ ];

  installPhase = ''
    mkdir -p $out
    cp -a . $out
  '';
}
