export default class Utils {
  public static getRoundedAmount (number: number): number {
    return Math.round(100 * number) / 100
  }
}
