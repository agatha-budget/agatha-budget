interface Category {
    name: string;
    allocated: number;
    spent: number;
    available: number;
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

export { Category, MasterCategoriesData, MasterCategoryArray }
