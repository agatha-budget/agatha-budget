{ pkgs ? import <nixpkgs> {} }:

with pkgs;

mkShell {
# seems we should use jdk11_headless for server env
buildInputs = [
nodejs-18_x jdk11 git
];
shellHook = ''
        export PATH="$PWD/node_modules/.bin/:$PATH"
    '';

}
