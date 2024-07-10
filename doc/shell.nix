{ pkgs ? import <nixpkgs> {} }:

with pkgs;

mkShell {
buildInputs = [
nodejs-18_x go-task
];

shellHook = ''
        export PATH="$PWD/node_modules/.bin/:$PATH"
    '';

}
