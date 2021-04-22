import { Operation, Account, Budget, Category, MasterCategory } from '@/services/api/openApi/api'

interface CategoryData {
    name: string;
    allocated: number;
    spent: number;
    available: number;
}

interface AccountList {
    [accountId: string]: Account;
}

interface CategoryList {
    [categoryId: string]: Category;
}

interface BudgetData {
    [masterCategoryId: string]: {
        name: string;
        categories: {
            [categoryId: string]: CategoryData;
        };
    };
}

interface MasterCategoriesData {
    [masterCategoryId: string]: CategoryData;
}

export { Operation, CategoryData, MasterCategoriesData, BudgetData, Account, AccountList, CategoryList, Budget, Category, MasterCategory }
