{ pkgs ? import <nixpkgs> {} }:

with pkgs;

mkShell {
buildInputs = [
nodejs_24 jdk11 git go-task

];
shellHook = ''
        export PATH="$PWD/node_modules/.bin/:$PATH"
    '';

}
