import { Operation, Account, Budget, Category, CategoryData as ICategoryData, MasterCategory } from '@/services/api/openApi/api'

interface BudgetData {
    [monthComparable: number]: CategoryDataList;
}

interface CategoryList {
    [categoryId: string]: Category;
}

interface MasterCategoryList {
    [masterCategoryId: string]: MasterCategory;
}

interface CategoryDataList {
    [categoryId: string]: CategoryData;
}

interface CategoryByMasterCategoryList {
    [masterCategoryId: string]: string[];
}

interface MasterCategoriesData {
    [masterCategoryId: string]: CategoryData;
}

class CategoryData implements ICategoryData {
    allocated = 0;
    spent = 0;
    available = 0;
}

const incomeCategoryId = 'universal_income_category'

export {
  Operation, CategoryData, MasterCategoriesData, BudgetData, Account,
  CategoryList, Budget, Category, MasterCategory, CategoryDataList, MasterCategoryList,
  CategoryByMasterCategoryList, incomeCategoryId
}
