export default class Utils {
  public static getEurosAmount (centsAmount: number): string {
    return (centsAmount / 100).toString()
  }

  public static getCentsAmount (eurosAmount: string): number {
    return +eurosAmount * 100
  }
}
