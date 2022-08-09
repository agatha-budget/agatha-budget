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
    return this.getDayFromDate(this.getCurrentDate())
  }

  public static getCurrentDate (): Date {
    return new Date()
  }

  public static getCurrentDateString (): string {
    return this.formatDate(this.getCurrentDate())
  }

  public static getMonthAsDate (monthAsInt: number): Date {
    const date = new Date()
    const month = monthAsInt % 100
    const year = (monthAsInt - month) / 100
    // needed because on the 31, setting a month with less than 31 day will break havoc (same when setting february on the 29/30/31)
    date.setDate(1)
    date.setMonth(month - 1)
    date.setFullYear(year)
    return date
  }

  public static getDayFromDate (dateTime: Date): number {
    const year = dateTime.getFullYear()
    const month = dateTime.getMonth() + 1 // because the form count month from 0 - 11
    const day = dateTime.getDate()
    return year * 10000 + month * 100 + day
  }

  public static getDayFromDateString (dateString: string): number {
    const date = new Date(dateString)
    return this.getDayFromDate(date)
  }

  public static getDateFromDay (dayAsInt: number): Date {
    const day = dayAsInt % 100
    const monthAsInt = ((dayAsInt - day) / 100)
    const date = this.getMonthAsDate(monthAsInt)
    date.setDate(day)
    return date
  }

  public static getDateStringFromDay (dayAsInt: number): string {
    const date = this.getDateFromDay(dayAsInt)
    return this.formatDate(date)
  }

  public static monthIsThisYear (monthAsInt: number): boolean {
    const year = this.getMonthAsDate(monthAsInt).getFullYear()
    const currentYear = new Date().getFullYear()
    return year === currentYear
  }

  public static getDateStringFromTimestamp (timestamp: number): string {
    const date = new Date(timestamp * 1) // *1 (to ensure that it is treated as a number)
    console.log(timestamp)
    console.log(date.getTime())
    return date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear()
  }

  private static formatDate (date: Date): string {
    return date.toISOString().split('T')[0]
  }
}
