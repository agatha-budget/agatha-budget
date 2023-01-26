export default class Utils {
  public static getEurosAmount (centsAmount: number): number {
    return (centsAmount / 100)
  }

  public static getCentsAmount (eurosAmount: number): number {
    return Math.round(+eurosAmount * 100)
  }

  public static centsToEurosDisplay (centsAmount: number): string {
    return (centsAmount / 100).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ' ')
  }

  public static eurosToEurosDisplay (eurosAmount: number): string {
    return eurosAmount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ' ')
  }
}
