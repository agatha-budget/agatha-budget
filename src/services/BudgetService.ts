export interface CategoryItem {
  id: string;
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
        id: '4541dez',
        name: 'Frais fixes',
        allocated: 467,
        spent: 467,
        available: 0,
        categories: [
          {
            id: '4541dez',
            name: 'Loyer',
            allocated: 452,
            spent: 452,
            available: 0
          },
          {
            id: '4541dez',
            name: 'Internet',
            allocated: 15,
            spent: 15,
            available: 0
          }
        ]
      },
      {
        id: '4541dez',
        name: 'Frais variables',
        allocated: 25,
        spent: 27,
        available: 30,
        categories: [
          {
            id: '4541dez',
            name: 'Course',
            allocated: 100,
            spent: 50,
            available: 50
          },
          {
            id: '4541dez',
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
