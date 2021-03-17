export interface MasterCategoryArray {
  [masterCategoryId: string]: {
    name: string;
    categories: {
      [categoryId: string]: {
        name: string;
        allocated: number;
        spent: number;
        available: number;
      };
    };
  };
}

class BudgetService {
  public async getBudget (): Promise<MasterCategoryArray> {
    return {
      mc1: {
        name: 'Frais fixes',
        categories: {
          mc1c1: {
            name: 'Loyer',
            allocated: 452,
            spent: 452,
            available: 0
          },
          mc1c2: {
            name: 'Internet',
            allocated: 15,
            spent: 15,
            available: 0
          }
        }
      },
      mc2: {
        name: 'Frais variable',
        categories: {
          mc2c1: {
            name: 'Courses',
            allocated: 100,
            spent: 70,
            available: 25
          },
          mc2c2: {
            name: 'Frigo',
            allocated: 10,
            spent: 0,
            available: 40
          }
        }
      }
    }
  }
}

export const budgetService = new BudgetService()
