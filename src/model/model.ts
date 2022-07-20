import { Person, Operation, Account, Bank, Budget, Category, CategoryData as ICategoryData, MasterCategory } from '@/services/api/openApi/api'

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

interface SelectOption {
    value: string;
    label: string;
}

interface GroupSelectOption {
    label: string;
    options: SelectOption[];
}

const incomeCategoryId = 'universal_income_category'
const transfertCategoryId = 'universal_transfert_category'
const newMasterCategoryName = 'Ω - Nouvelle Catégorie'
const newCategoryName = ' Nouvelle Enveloppe'

export {
  Person, Operation, CategoryData, BudgetData, Account, Bank, Budget, Category,
  MasterCategory, CategoryDataList, incomeCategoryId, transfertCategoryId,
  newMasterCategoryName, newCategoryName, GroupSelectOption, SelectOption
}
