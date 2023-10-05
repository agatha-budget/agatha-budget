import type { Person, Operation, OperationWithDaughters, Account, Bank, BankAccount, Budget, Category, CategoryData as ICategoryData, MasterCategory } from '@/services/api/openApi/api'

export class CategoryData implements ICategoryData {
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

export const incomeCategoryId = 'universal_income_category'
export const transfertCategoryId = 'universal_transfert_category'
export const newMasterCategoryName = ' Nouvelle Catégorie'
export const newCategoryName = ' Nouvelle Enveloppe'

// export interface separatly from class and const because of Typescript compiler
export type {
  Person, Operation, OperationWithDaughters, BudgetData, Account, Bank, BankAccount, Budget, Category,
  MasterCategory, CategoryDataList, GroupSelectOption, SelectOption,
  ChoiceElement
}
