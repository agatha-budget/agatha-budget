## Agatha-Budget

## Run locally 

### TLDR : 

- install task 

```
npm install -g @go-task/cli 
```
or see for alternative installation method https://taskfile.dev/installation/

- install nix
```
task install_nix_linux      // for Linux
task install_nix_windows    // for Windowz
task install_nix_mac        // for Mac
```

- start the backend in a console
```
task serve_back
```
- start the frontend in another one
 ```
task serve_front
```

### longer version : 
- clone the repo
- install task (alternative to make) 
    - optionnal, you can avoid it by reading the content of the Taskfile.yml and running the command manually but it is awfully convenient
- install nix
    - nix will create a isolated space in which it will manage all the dependencies properly, set up the required database etc..
    - you can also avoid it but you will have to manually install the relevant dependencies and create the bases
       