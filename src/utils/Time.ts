export default class Time {
  public static getCurrentMonth (): number {
    const dateTime = new Date()
    const year = dateTime.getFullYear()
    const month = dateTime.getMonth() + 1 // by default from 0 to 11
    return year * 100 + month
  }

  public static getNextMonth (monthAsInt: number): number {
    let month = monthAsInt % 100
    let year = (monthAsInt - month) / 100
    if (month < 12) {
      month += 1
    } else {
      year += 1
      month = 1
    }
    return year * 100 + month
  }

  public static getLastMonth (monthAsInt: number): number {
    let month = monthAsInt % 100
    let year = (monthAsInt - month) / 100
    if (month > 1) {
      month -= 1
    } else {
      year -= 1
      month = 12
    }
    return year * 100 + month
  }

  public static getCurrentDay (): number {
    const dateTime = new Date()
    return this.getDayFromDate(dateTime)
  }

  public static getMonthAsDate (monthAsInt: number): Date {
    const date = new Date()
    const month = monthAsInt % 100
    const year = (monthAsInt - month) / 100
    date.setMonth(month - 1)
    date.setFullYear(year)
    return date
  }

  public static getDayFromDate (dateTime: Date): number {
    const year = dateTime.getFullYear()
    const month = dateTime.getMonth() + 1 // by default from 0 to 11
    const day = dateTime.getDate()
    return year * 10000 + month * 100 + day
  }

  public static getDayFromDateString (dateString: string): number {
    const date = new Date(dateString)
    return this.getDayFromDate(date)
  }

  public static getDayAsDate (dayAsInt: number): Date {
    const day = dayAsInt % 100
    const monthAsInt = ((dayAsInt - day) / 100) - 1
    const date = this.getMonthAsDate(monthAsInt)
    date.setDate(day)
    return date
  }

  public static monthIsThisYear (monthAsInt: number): boolean {
    const year = this.getMonthAsDate(monthAsInt).getFullYear()
    const currentYear = new Date().getFullYear()
    return year === currentYear
  }
}
