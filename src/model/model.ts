import { Person, Operation, OperationWithDaughters, Account, Bank, BankAccount, Budget, Category, CategoryData as ICategoryData, MasterCategory } from '@/services/api/openApi/api'

class CategoryData implements ICategoryData {
  allocated = 0
  spent = 0
  available = 0
}

interface CategoryDataList {
  [categoryId: string]: CategoryData;
}

interface BudgetData {
  [monthComparable: number]: CategoryDataList;
}

interface SelectOption {
  value: string;
  label: string;
}

interface GroupSelectOption {
  label: string;
  options: SelectOption[];
}

interface ChoiceElement {
  label: string;
  value: string;
  preSelected: boolean;
}

const incomeCategoryId = 'universal_income_category'
const transfertCategoryId = 'universal_transfert_category'
const newMasterCategoryName = ' Nouvelle Catégorie'
const newCategoryName = ' Nouvelle Enveloppe'

export {
  Person, Operation, OperationWithDaughters, CategoryData, BudgetData, Account, Bank, BankAccount, Budget, Category,
  MasterCategory, CategoryDataList, incomeCategoryId, transfertCategoryId,
  newMasterCategoryName, newCategoryName, GroupSelectOption, SelectOption,
  ChoiceElement
}
