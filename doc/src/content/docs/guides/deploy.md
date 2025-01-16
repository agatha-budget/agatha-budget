---
title: Self-host
description: A guide to self-host
---

This documentation is intended to deploy Agatha on a Nixos server with a keycloak and both a beta and a default instances (to be honest it's my recipe to be able to reinstall everything in case of trouble)

The documentation will be improved to better adress the needs of an everyday self-hoster with ready to use nixos packages and redirection toward the Agatha Identity provider rather than having to install your own. Make yourself known so I can prioritize it accordingly. 

/!\ This requires an understanding of Nixos as you will need to edit the OS configuration file


## create a NIXOS VPS
see [vps2day](https://portal.vps2day.com/)

## log as root on your new server
```
ssh root@12.345.678.910 // see password in vps dashboard
passwd // change your temporary password
```

## git and non root user
```sh
nix-channel --updatevim
nano /etc/nixos/configuration.nix
```

add this lines to the file: 
```nix title=/etc/nixos/configuration.nix
users.users.erica = {
    isNormalUser = true;
    extraGroups = [ "wheel" ]; # Enable ‘sudo’ for the user.
};
environment.systemPackages = with pkgs; [ vim git ];
```

then update 

```sh
nixos-rebuild switch
passwd erica
su erica
```

## retrieving the server configuration

- clone the agatha deploy repo

```sh
cd /home/erica/deploy/ 
git clone git@github.com:agatha-budget/deploy.git
git config --global user.email "erica@agatha-budget.fr"
git config --global user.name "Erica - Server name"
ssh-keygen -t ed25519 -C "erica@agatha-budget.fr"
cat /home/erica/.ssh/ssh_key_name.pub
```

see [Github documentation](https://docs.github.com/en/authentication/connecting-to-github-with-ssh/adding-a-new-ssh-key-to-your-github-account#adding-a-new-ssh-key-to-your-account) to authorize a new ssh key


- Add the server configuration to your repo to version it 

```sh
cd /home/erica/deploy/


## retrieve the server configuration file to version them
cp configuration.nix old_configuration.nix
cp /etc/nixos/configuration.nix .
cp /etc/nixos/hardware-configuration.nix .

## keep the original conf aside just in case
sudo mv /etc/nixos/configuration.nix /etc/nixos/initial_configuration.nix
sudo mv /etc/nixos/hardware-configuration.nix /etc/nixos/initial_hardware-configuration.nix

## add symlinks so that any versionned changed is applied to the server
sudo ln -s /home/erica/deploy/nixos/configuration.nix /etc/nixos/configuration.nix
sudo ln -s /home/erica/deploy/nixos/hardware-configuration.nix /etc/nixos/hardware-configuration.nix

git add *
git commit -m "init new server conf"
git push
```

- note : hardware-configuration wont be changed but it is needed in the nixos folder so that the relative links in configuration.nix stay valid

- troubleshooting : if you receive the error "git@github.com: Permission denied (publickey)." run 

```sh
eval `ssh-agent -s`
ssh-add ~/.ssh/private-ssh-key
```

- troubleshooting : error: insufficient permission for adding an object to repository database .git/objects
edit the file manually with the erica user to change ownership somehow

- Update the /var/deploy/nixos/configuration.nix file to include the required element (you can take inspiration from the old_configuration.nix file)

## Updating the configuration

- [important] change the base version  
```sh
[erica@server:~/deploy]$ nixos-version
24.11pre687768.27e30d177e57 (Vicuna)
```

```nix file=configuration.nix
	system.stateVersion = "24.11"; # Did you read the comment?
```

Nix needs to know what your start point was in order to handle updates correctly

## Adding local config 

```sh
cd /home/erica
mkdir logs
cp /home/erica/config-template config
```

go trough the files to change the value depending on your configuration

note for the Agatha-team, see the relevant repo to find our own configuration




## setting the symbolic links 

[optionnal]
 
 it should not be needed if you used the same structure as this doc
 - /home/erica
    - config
    - deploy

otherwise update the following script
```sh
## add symlinks so that the backend use the local configuration
ln -s /home/erica/config/release_back/beta/flyway.conf /home/erica/deploy/release_back/beta/flyway.conf
ln -s /home/erica/config/release_back/beta/gradle.properties /home/erica/deploy/release_back/beta/gradle.properties
ln -s /home/erica/config/release_back/beta/logback.xml /home/erica/deploy/release_back/beta/logback.xml

ln -s /home/erica/config/release_back/default/flyway.conf /home/erica/deploy/release_back/default/flyway.conf
ln -s /home/erica/config/release_back/default/gradle.properties /home/erica/deploy/release_back/default/gradle.properties
ln -s /home/erica/config/release_back/default/logback.xml /home/erica/deploy/release_back/default/logback.xml
```

## prepare folder for frontend
```sh
sudo mkdir /var/www/
sudo mkdir /var/www/front
sudo mkdir /var/www/beta
```

## let's go

```sh
task update
```



# Let's save the day 

## import backup

- create a new database from scratch
- import the backup

```sh
pg_restore -h database.host.net -p 1234 -U name-of-the-user -d name-of-the-database -c database_backup
```
- then, see "change the database" section

/!\ Warning ! Do not import the data in the current database. This is likely to create a migration conflict between the current structure and the former data

## change the database 

- keycloak
    - update the configuration.nix file
    - update the db_password file in the server config

- agatha 
    - update the gradle.properties int the server config
    - update the flyway.conf in the server config

## change the url 

if you want to change the url you will have to update  :

- the gradle.properties files the server config
- the configuration.nix file
- when building the front : 
    - app/front/scripts/set_beta_properties.sh
    - app/front/scripts/set_prod_properties.sh

## Further reading

- Read [Creating docker image with nix](/reference/nix-docker)


