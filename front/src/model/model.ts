import type {
Account,
Bank,
BankAccount,
Budget,
Category,
CategoryData as ICategoryData,
MasterCategory,
Operation,
OperationWithDaughters,
Person
} from '@/services/api/openApi/api'

export class CategoryData implements ICategoryData {
  allocated = 0
  spent = 0
  available = 0
}

interface CategoryDataList {
  [categoryId: string]: CategoryData
}

interface BudgetData {
  [monthComparable: number]: CategoryDataList
}

interface SelectOption {
  value: string
  label: string
}

interface GroupSelectOption {
  label: string
  options: SelectOption[]
}

interface ChoiceElement {
  label: string
  value: string
  preSelected: boolean
}

export function operationToOperationWithDaughter(operation: Operation) : OperationWithDaughters {
  return {
    id: operation.id,
    day: operation.day,
    accountId:operation.accountId,
    categoryId:operation.categoryId,
    amount:operation.amount,
    memo:operation.memo,
    pending:operation.pending,
    daughters: []
  }
}

export const incomeCategoryId = 'universal_income_category'
export const transfertCategoryId = 'universal_transfert_category'
export const newMasterCategoryName = ' Nouvelle Catégorie'
export const newCategoryName = ' Nouvelle Enveloppe'

// export interface separatly from class and const because of Typescript compiler
export type {
Account,
Bank,
BankAccount,
Budget, BudgetData, Category, CategoryDataList, ChoiceElement, GroupSelectOption, MasterCategory, Operation,
OperationWithDaughters, Person, SelectOption
}
