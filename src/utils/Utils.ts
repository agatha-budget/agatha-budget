export default class Utils {
  public static getEurosAmount (centsAmount: number): string {
    return (centsAmount / 100).toString()
  }
}
