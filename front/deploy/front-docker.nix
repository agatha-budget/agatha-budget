{ 
  pkgs ? import <nixpkgs> { system = builtins.currentSystem; },
  dockerTools ? pkgs.dockerTools,
  agatha-front  ? pkgs.callPackage ./front.nix { },
  nginx ? pkgs.nginx,
}:
dockerTools.buildLayeredImage {
  name = "agatha-front-image";
  tag = "latest";

  contents = [ agatha-front nginx ];

  extraCommands = ''
    mkdir -p home/conf
    mkdir -p var/log/nginx/
    mkdir -p var/www/front
    ln -s /bin/agatha-front var/www/front
  '';

  config = { 
    Cmd = [ "/bin/nginx" ];
    WorkingDir = "/home";
  };
}


