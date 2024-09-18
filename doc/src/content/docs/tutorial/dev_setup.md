---
title: Run Agatha locally
description: A tutorial to get Agatha up and running
---

Welcome to Agatha ! 
Let's first get it running on your machine.

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