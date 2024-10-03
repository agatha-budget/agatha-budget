---
title: Run Agatha locally
description: A tutorial to get Agatha up and running
---

Welcome to Agatha ! 
Let's first get it running on your machine.

- clone the project locally

```
git clone https://github.com/agatha-budget/
cd agatha-budget
```

- set the default properties : 

```
cp default.properties gradle.properties
```

You can now change the value of the properties depending on your environment.
For exemple, if you want to use another database.

- install task

Task is an alternative to Make that aims to be simpler and easier to use. 
It will allow you to call all the commands relevant for the project

if you already have node installed on your computer, run :

```
sudo npm install -g @go-task/cli 
```
or see for alternative installation method https://taskfile.dev/installation/

You can now run :
```
task
```

and see the list of all the available command


- install nix

Nix is a package manager, it will be responsible to manage your system dependencies, create the DB etc.

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

