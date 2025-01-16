## Bug description
*how long* : 20 min

*what happened* : impossible to use data() to use a value in htlm code

*why* : this value is modified by some methods

*what did i do to fix it* : creation of new computed that return the value thank to 'this.' to resolve the conflict problem

*how often* : once for moment

--------------
## Bug description
*how long* : 10 min

*what happened* : error 500 & error 400 "This connection has been closed" but data base accessible for dbeaver

*why* : ...

*what did i do to fix it* : restart all Dynos on heroku's dashboard

*how often* : one

--------------
## Bug description
*how long* : 

*what happened* : 502 Bad gateway

journalctl -u nginx -e
*5 no live upstreams while connecting to upstream

*why* : after changing the dns to redirect to the new server

*what did i do to fix it* : 
use 127.0.0.1 instead of localhost

*how often* :

--------------

## Bug description
*how long* : 

*what happened* : 502 Bad gateway

journalctl -u nginx -e
failed (111: Connection refused) while connecting to upstream

*why* : after importing a new database in keycloak
in fact keycloak wasn't working because of a migration problem

*what did i do to fix it* : 

When importing a database to keycloak, do not import the data in the existing database because the structure will be the one from the new keycloak but the data the one from the older one. 

Create a new db, import the data then redirect keycloak to it and it will upgrade the database itself

*how often* :

--------------


