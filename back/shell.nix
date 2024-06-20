{ pkgs ? import <nixpkgs> {} }:

with pkgs;

mkShell {
buildInputs = [
kotlin postgresql_13 gradle temurin-bin-17 python3 go-task
];

shellHook = ''
export LOCALE_ARCHIVE="${pkgs.glibcLocales}/lib/locale/locale-archive"
export PGDATA=$PWD/db/postgres_data
export PGHOST=$PWD/db/postgres
export LOG_PATH=$PWD/db/postgres/LOG
export DATABASE_URL="postgresql:///postgres?host=$PGHOST"
export LC_ALL="en_GB.UTF-8"
export LC_MESSAGES="en_GB.UTF-8"
echo $HOSTNAME

# create directory if need be
if [ ! -d $PGHOST ]; then
mkdir -p $PGHOST
fi
if [ ! -d $PGDATA ]; then
echo 'Initializing postgresql database...'
initdb $PGDATA --auth=trust >/dev/null
fi

pg_ctl restart -l $LOG_PATH -o "-c unix_socket_directories=$PGHOST"
createuser postgres --superuser --createdb

# create user and database if need be
psql -U postgres -tc "SELECT 1 FROM pg_database WHERE datname = 'agatha'" | grep -q 1 || psql -U postgres -c "CREATE DATABASE agatha"
psql -U postgres -tc "SELECT 1 FROM pg_database WHERE datname = 'integration_agatha'" | grep -q 1 || psql -U postgres -c "CREATE DATABASE integration_agatha"
'';
}
