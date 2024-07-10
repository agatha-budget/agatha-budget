{ 
  pkgs ? import <nixpkgs> { system = builtins.currentSystem; },
  dockerTools ? pkgs.dockerTools,
  agatha-front  ? pkgs.callPackage ./front.nix { },
  http-server ? pkgs.http-server
}:
dockerTools.buildLayeredImage {
  name = "agatha-front-image";
  tag = "latest";

  contents = [ agatha-front http-server];

  config = {
    Cmd = [ "/bin/http-server" "/share/agatha-front" "-p" "5173" ];
  };
}