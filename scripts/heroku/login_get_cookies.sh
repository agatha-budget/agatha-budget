curl -X POST "https://tresorier-back.herokuapp.com/login?email=a&password=a" -H  "accept: application/json" -d "" -c /tmp/cookie
cat /tmp/cookie