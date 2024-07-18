{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  buildInputs = [ (pkgs.callPackage ./back.nix {}) ];
}