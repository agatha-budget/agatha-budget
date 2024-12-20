{ 
  pkgs ? import <nixpkgs> { system = builtins.currentSystem; },
  dockerTools ? pkgs.dockerTools,
}:
dockerTools.pullImage {
  imageName = "postgres";
  # found the digest in the image page just under the title : https://hub.docker.com/layers/library/postgres/13.15/images/sha256-c07edc26368f1c68093cc9247cc8daa38199e7a78a4fcc2879eef533388ef22c
  imageDigest = "sha256:afcb675cf038e3fc006fe515d407a79dc8e1f829f671dd25b176a8d823be1e7c"; 
  finalImageName = "agatha-db-image";
  finalImageTag = "latest";
  # start by setting any value to the sha256 string,
  # after the first build, you will receive an error with the expected signature you can then copy it here 
  sha256 = "sha256-+wzNtWHS+ZwpKCVJZSNE5gVxzErgBOB1LOSesggVTA0=";
}
