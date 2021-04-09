interface Category {
    name: string;
    allocated: number;
    spent: number;
    available: number;
}

interface Account {
    id: string;
    name: string;
    amount: number;
}

interface Budget {
    id: string;
    name: string;
}

interface AccountList {
    [accountId: string]: Account;
}

interface BudgetData {
    [masterCategoryId: string]: {
        name: string;
        categories: {
            [categoryId: string]: Category;
        };
    };
}

interface MasterCategoriesData {
    [masterCategoryId: string]: Category;
}

export { Category, MasterCategoriesData, BudgetData, Account, AccountList, Budget }
