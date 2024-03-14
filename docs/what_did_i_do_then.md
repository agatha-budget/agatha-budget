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
or 
ps aux | grep postgres  to find the pid
kill pid_number


*how often* : 5

--------------
## Adress already in use, Is another postmaster already runnin on port 5432 ?

*how long* : 

*what happened* : 
can't start postgres

*why* :
already run somewhere else

*what did i do to fix it* :

```
- nix-shell -p lsof
- sudo lsof -i :5432
- kill -9 XXXX(pid)

```
*how often* : 2

--------------
## Stripe webhook doesn't call the API

*how long* : 1h

*what happened* : 

*why* :

*what did i do to fix it* : restart the local 
''
stripe listen --forward-to localhost:7000/webhook
''

*how often* : 2

--------------
## migration is skipping 

*how long* : 20min

*what happened* : migration doesn't work with >>gradle migration and >>gradle generateJooq

*why* : It didn't find the new flyway's file

*what did i do to fix it* : the file needs two underscores after its number

*how often* : 1

--------------
## ‘compileKotlin’ task (current target is 1.8)

*how long* : 3h

*what happened* : major update of kotlin and a few lib

*why* : 

*what did i do to fix it* : 
https://ittutoria.net/solutions-for-error-compilejava-task-current-target-is-11-and-compilekotlin-task-current-target-is-1-8-jvm-target-compatibility-should-be-set-to-the-same-ja/

*how often* :

--------------
## expected "identifier"; SQL statement:

*how long* : 3h

*what happened* : update of h2, a column name was a reserved word

*why* : h2 keep some keyword for its own use. see here :  http://h2database.com/html/advanced.html#keywords

*what did i do to fix it* : rename the column

*how often* :

--------------
## Option CORS Failed for Authentication

*how long* : days....

*what happened* : 
- wasn't using the right configuration :
  https://stackoverflow.com/questions/77621552/pac4j-cors-allow-origin-not-matching-origin/
- option was redirected instead of server handling it on its side :
  https://stackoverflow.com/questions/42168773/how-to-resolve-preflight-is-invalid-redirect-or-redirect-is-not-allowed-for
- didn't need pac4j, just trust the jwt :
  https://medium.com/devops-dudes/secure-front-end-react-js-and-back-end-node-js-express-rest-api-with-keycloak-daf159f0a94e
  https://arielweinberger.medium.com/json-web-token-jwt-the-only-explanation-youll-ever-need-cf53f0822f50
*why* :
  I had the wrong impression of what should be done (check with keycloak for each request) and it led me to do try and force things the way I thought it should go
  I learned a lot about security/CORS/etc
  Try and find trusted source of how things should works if there is a next time but I did that now so accept that it is part of the process I guess
  Read well done doc even if it's not the right language/tool to understand things on a meta level and be able to apply it
  Ask more quickly on StackOverflow
  Contacting a maintainer and paying him/her for help may be a good deal (still glad I managed before !)
*what did I do to fix it* : 
read read and read the doc and stack overflow

*how often* :hopefully never again

--------------
## TRANSFERT DES UTILISATEURS

*how long* :

*what happened* :

*why* :

*what did i do to fix it* :

INSERT INTO person (id, email, name, password, unlockingdate, loginattemptcount, deleted, billing_id, billing_status, creation_date, style, dyslexia) VALUES ('keycloak_transfert_id', 'ewf', 'keycloack_transfert', 'efwrgf', 0, 0, false, 'feerg', true, 0, 've', false)

BEGIN TRANSACTION;
// redirect new budget/user somewhere
update budget set person_id='keycloak_for_new_budget' where person_id='NEW_ID';
update person set id='NOM_USER' where id='NEW_ID';
// link new id with existing user and budget
update budget set person_id='keycloak_transfert_id' where person_id='OLD_ID';
update person set id='NEW_ID' where id='OLD_ID';
update budget set person_id='NEW_ID' where person_id='keycloak_transfert_id';
COMMIT;

*how often* :

--------------
## Prolongation periode d'essai

*how long* :

*what happened* :

*why* :

*what did i do to fix it* :

select * from person where email='email';
update person set creation_date=1709219196000 where email='email';

*how often* :

--------------