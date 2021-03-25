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
    currency: Currency;
}


interface MasterCategoryArray {
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

interface Currency {
    name: string,
    symbol: string
}

export { Category, MasterCategoriesData, MasterCategoryArray, Account, Currency }
