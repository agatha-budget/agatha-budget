export default class Utils {
  public static getEurosAmount (centsAmount: number): number {
    return (centsAmount / 100)
  }

  public static getCentsAmount (eurosAmount: number): number {
    return Math.round(+eurosAmount * 100)
  }

  public static addSpacesInThousand (number: number): string {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ' ')
  }
}
