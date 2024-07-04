{ 
  pkgs ? import <nixpkgs> { system = builtins.currentSystem; },
  dockerTools ? pkgs.dockerTools,
  agatha-back  ? pkgs.callPackage ./back.nix { },
  bash ? pkgs.bash,
}:
dockerTools.buildLayeredImage {
  name = "agatha-back-image";
  tag = "latest";

  contents = [ agatha-back ];

  extraCommands = ''
    mkdir -p home
  '';

  config = { 
    Cmd = [ "ls" ];
    #Cmd = [ "/bin/agatha-back" ];
    WorkingDir = "/home";
  };
}