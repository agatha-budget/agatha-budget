{ pkgs ? import <nixpkgs> {} }:

with pkgs;

mkShell {
buildInputs = [
nodejs-18_x jdk11 git
];
shellHook = ''
        export PATH="$PWD/node_modules/.bin/:$PATH"
    '';

}
