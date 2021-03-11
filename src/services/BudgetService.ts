interface CategoryItem {
  name: string;
  allocated: number;
  spent: number;
  available: number;
}

export interface MasterCategoryItem extends CategoryItem {
  categories: CategoryItem[];
}

class BudgetService {
  public async getBudget (): Promise<MasterCategoryItem[]> {
    return [
      {
        name: 'Frais fixes',
        allocated: 467,
        spent: 467,
        available: 0,
        categories: [
          {
            name: 'Loyer',
            allocated: 452,
            spent: 452,
            available: 0
          },
          {
            name: 'Internet',
            allocated: 15,
            spent: 15,
            available: 0
          }
        ]
      },
      {
        name: 'Frais variables',
        allocated: 25,
        spent: 27,
        available: 30,
        categories: [
          {
            name: 'Course',
            allocated: 100,
            spent: 50,
            available: 50
          },
          {
            name: 'Resto',
            allocated: 40,
            spent: 10,
            available: 30
          }
        ]
      }
    ]
  }
}

export const budgetService = new BudgetService()
