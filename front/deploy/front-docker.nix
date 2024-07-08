{ 
  pkgs ? import <nixpkgs> { system = builtins.currentSystem; },
  dockerTools ? pkgs.dockerTools,
  agatha-front  ? pkgs.callPackage ./front.nix { },
  agatha-back  ? pkgs.callPackage ../../back/deploy/back.nix { },
  nginx ? pkgs.nginx,
  bash ? pkgs.bash,
  vim ? pkgs.vim
}:
dockerTools.buildLayeredImage {
  name = "agatha-front-image";
  tag = "latest";

  contents = [ agatha-front nginx agatha-back bash vim];

  fakeRootCommands = ''
    ${dockerTools.shadowSetup}
    groupadd -r nogroup
    useradd -r -g nogroup nobody
    mkdir -p home
    mkdir -p tmp
    mkdir -p var/log/nginx/
  '';
  enableFakechroot = true;

  config = {
    Cmd = [ "/bin/nginx" "-c" "/home/nginx.conf" ];
  };
}