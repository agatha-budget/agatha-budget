export default class EventUtils {
    public static getInputValue (e: Event): string {
      return (e.target as HTMLInputElement).value
    }
  
    public static centsToEurosDisplay (centsAmount: number): string {
      return (centsAmount / 100).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ' ')
    }
  }
  