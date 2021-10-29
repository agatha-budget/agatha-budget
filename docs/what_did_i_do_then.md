## postmaster.pid" does not exist
*how long* : 1h

*what happened* : impossible to access database when starting nix-shell

*why* : postgres is running outside of the shell 

*what did i do to fix it* : first **remember to run pg_ctl start in the shell** to get a explicit error log
I ran : 
''
sudo service postgresql stop 
''
outside of the shell

*how often* : 1

--------------
## Bug description

*how long* :

*what happened* :

*why* :

*what did i do to fix it* : 

*how often* :

--------------