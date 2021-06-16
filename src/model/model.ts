import { Operation, Account, Budget, Category, CategoryData as ICategoryData, MasterCategory } from '@/services/api/openApi/api'

interface BudgetData {
    [monthComparable: number]: CategoryDataList;
}

interface CategoryDataList {
    [categoryId: string]: CategoryData;
}

class CategoryData implements ICategoryData {
    allocated = 0;
    spent = 0;
    available = 0;
}

const incomeCategoryId = 'universal_income_category'

export {
  Operation, CategoryData, BudgetData, Account, Budget, Category, MasterCategory, CategoryDataList, incomeCategoryId
}
