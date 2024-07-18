{ 
  pkgs ? import <nixpkgs> { system = builtins.currentSystem; },
  dockerTools ? pkgs.dockerTools,
  agatha-front  ? pkgs.callPackage ./front.nix { },
}:
dockerTools.buildLayeredImage {
  name = "agatha-front-image";
  tag = "latest";

  contents = [ agatha-front];

  config = {
    Cmd = [ "/bin/agatha-front" ];
  };
}