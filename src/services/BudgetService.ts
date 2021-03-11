export interface CategoryItem {
  name: string;
  allocated: number;
  spent: number;
  available: number;
}

export interface CategoryArray {
  [categoryId: string]: CategoryItem;
}

export interface MasterCategoryItem extends CategoryItem {
    categories: CategoryArray;
}

export interface MasterCategoryArray {
  [masterCategoryId: string]: MasterCategoryItem;
}

class BudgetService {
  public async getBudget (): Promise<MasterCategoryArray> {
    return {
      '4541dez': {
        name: 'Frais fixes',
        allocated: 467,
        spent: 467,
        available: 0,
        categories: {
          '4541dez': {
            name: 'Loyer',
            allocated: 452,
            spent: 452,
            available: 0
          },
          '4541dez5': {
            name: 'Internet',
            allocated: 15,
            spent: 15,
            available: 0
          }
        }
      },
      '4541dedezz': {
        name: 'Frais variable',
        allocated: 467,
        spent: 467,
        available: 0,
        categories: {
          '4541dreez': {
            name: 'Courses',
            allocated: 452,
            spent: 452,
            available: 0
          },
          '4541dezerz': {
            name: 'Frigo',
            allocated: 15,
            spent: 15,
            available: 0
          }
        }
      }
    }
  }
}

export const budgetService = new BudgetService()
