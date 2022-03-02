sed -i "s/commitHash = '.*'/commitHash = '$1'/g" properties.ts
sed -i "s/agatha-alternance\.herokuapp\.com/api.agatha-budget.fr/g" properties.ts
sed -i "s/tresorier-back\.herokuapp\.com/api.agatha-budget.fr/g" properties.ts