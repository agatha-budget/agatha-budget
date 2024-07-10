{ 
  pkgs ? import <nixpkgs> { system = builtins.currentSystem; },
  dockerTools ? pkgs.dockerTools,
  agatha-back  ? pkgs.callPackage ../nix-packages/back.nix { },
  agatha-migrations ? pkgs.callPackage ../nix-packages/migrations.nix { },
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
    Cmd = [ "/bin/bash" "-c" "/bin/flyway -configFiles=/share/agatha-migrations/flyway.conf migrate && /bin/agatha-back" ];
    WorkingDir = "/home";
  };
}