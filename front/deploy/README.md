https://joshkingsley.me/blog/publishing-static-site-nix.html


## Create a nix package for the front end

Using the easier solution, I just created a package that copied the generated dist files

## Create a container for it

I would have like to use the nixos nginx module but it isn't available in dockers tool

http://nginx.org/en/docs/beginners_guide.html  

IN THE COMMAND YOU NEED TO WRITE A LIST WITH THE COMMAND AND ITS OPTION

ex  ["ls" "-l"] not ["ls -l"] DAMNIT !! 