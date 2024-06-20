const allocatedColor = '#b2babb' // grey
const spentColor = '#dc7633' // orange
const availableColor = '#45c1b8' // green
const redColor = '#e74c3c'
const blueColor = '#3498db'
const orangeColor = '#fba619'
const purpleColor = '#8e44ad'
const greenColor = '#27ae60'
const yellowColor = '#f1c40f'
const navyColor = '#152c8c'
const pinkColor = '#fb19cb'
const brownColor = '#935116'
const blackColor = '#17202a'
const lightGreyColor = '#bfc9ca'
const darkGreyColor = '#909497'
const lightGreenColor = '#36ec49'
const salmonColor = '#ff8f84'
const lavenderColor = '#f08bf3'
const bordeauxColor = '#b2155d'
const navigationColor = '#003249'

class Color {
  public static shadeColor (color: string, percent: number): string {
    let R: number = parseInt(color.substring(1, 3), 16)
    let G: number = parseInt(color.substring(3, 5), 16)
    let B: number = parseInt(color.substring(5, 7), 16)

    R = Math.round(R * (100 + percent) / 100)
    G = Math.round(G * (100 + percent) / 100)
    B = Math.round(B * (100 + percent) / 100)

    R = (R < 255) ? R : 255
    G = (G < 255) ? G : 255
    B = (B < 255) ? B : 255

    const RR = ((R.toString(16).length === 1) ? '0' + R.toString(16) : R.toString(16))
    const GG = ((G.toString(16).length === 1) ? '0' + G.toString(16) : G.toString(16))
    const BB = ((B.toString(16).length === 1) ? '0' + B.toString(16) : B.toString(16))

    return '#' + RR + GG + BB
  }
}

export {
  Color, allocatedColor, spentColor, availableColor, redColor, blueColor, orangeColor,
  purpleColor, greenColor, yellowColor, navyColor, pinkColor, brownColor,
  blackColor, lightGreyColor, darkGreyColor, lightGreenColor, salmonColor,
  lavenderColor, bordeauxColor, navigationColor
}
