import { Operation, Account, Budget } from '@/services/api/openApi/api'

interface Category {
    name: string;
    allocated: number;
    spent: number;
    available: number;
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

export { Operation, Category, MasterCategoriesData, BudgetData, Account, AccountList, Budget }
