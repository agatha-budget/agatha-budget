export default class Time {
  public static getCurrentMonth (): number {
    const dateTime = new Date()
    const year = dateTime.getFullYear()
    const month = dateTime.getMonth() + 1 // by default from 0 to 11
    return year * 100 + month
  }

  public static getCurrentDay (): number {
    const dateTime = new Date()
    const year = dateTime.getFullYear()
    const month = dateTime.getMonth() + 1 // by default from 0 to 11
    const day = dateTime.getDate()
    return year * 10000 + month * 100 + day
  }
}
