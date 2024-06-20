import type { OperationWithDaughters } from "@/model/model";

export default class Utils {
  public static getEurosAmount (centsAmount: number): number {
    return (centsAmount / 100)
  }

  public static centsToEurosDisplay (centsAmount: number): string {
    return (centsAmount / 100).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ' ').replace(' ', "\xa0")
  }

  public static insertInListSortedByDate(operation: OperationWithDaughters,
     operationList: OperationWithDaughters[]) {
     
      var index = this.getSortedIndex(operation, operationList)
      operationList.splice(index, 0, operation);
      return operationList
  }

  private static getSortedIndex(operation: OperationWithDaughters, operationList: OperationWithDaughters[]): number {
    var low = 0,
        high = operationList.length;

    while (low < high) {
        var mid = (low + high) >>> 1; // binary method to divide by two
        if (operationList[mid].day > operation.day) low = mid + 1;
        else high = mid;
    }
    return low;
  }
}
