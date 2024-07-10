{ 
  pkgs ? import <nixpkgs> { system = builtins.currentSystem; },
  dockerTools ? pkgs.dockerTools,
  agatha-back  ? pkgs.callPackage ../nix-packages/back.nix { },
  flyway ? pkgs.flyway,
  bash ? pkgs.bash,
}:
dockerTools.buildLayeredImage {
  name = "agatha-back-image";
  tag = "latest";

  contents = [ agatha-back agatha-migrations flyway bash];

  extraCommands = ''
    mkdir -p home
  '';

  config = { 
    ## this commands needs to mounted file : flyway.conf and gradle.properties. see docker-compose
    Cmd = [ "/bin/bash" "-c" "/bin/flyway -configFiles=/home/flyway.conf migrate && /bin/agatha-back" ];
    WorkingDir = "/home";
  };
}