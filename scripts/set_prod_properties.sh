backend_url=https://api.agatha-budget.fr
hash=$(git rev-parse HEAD)
# using @ instead of / to insert url smoothly
sed -i "s@commitHash = '.*'@commitHash = '$hash'@g" properties.ts
sed -i "s@server = '.*'@server = '$backend_url'@g" properties.ts