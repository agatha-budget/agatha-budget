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

  public static parseComma (amount: string): number {
    if (amount.indexOf(',') !== -1) {
      const parsed = amount.replace(/,/g, '.')
      return Number(parsed)
    }
    return Number(amount)
  }

  public static calcul (calculation: string): number { // 9*((6*7-4)+3)*(5+6-(1+4))/2/3/3/3 returns 41
    let result: number
    if (calculation.includes('+') && calculation.includes('-')) {
      if (calculation.lastIndexOf('+') < calculation.lastIndexOf('-')) {
        result = this.subtraction(calculation)
      } else {
        result = this.addition(calculation)
      }
    } else if (calculation.includes('+')) {
      result = this.addition(calculation)
    } else if (calculation.includes('-')) {
      result = this.subtraction(calculation)
    } else if (calculation.includes('*') && calculation.includes('/')) {
      if (calculation.lastIndexOf('/') < calculation.lastIndexOf('*')) {
        result = this.multiplication(calculation)
      } else {
        result = this.division(calculation)
      }
    } else if (calculation.includes('*')) {
      result = this.multiplication(calculation)
    } else if (calculation.includes('/')) {
      result = this.division(calculation)
    } else {
      result = this.parseComma(calculation)
    }
    return Math.trunc(result * 100) / 100
  }

  public static subtraction (calculation: string): number {
    const pos = calculation.lastIndexOf('-')
    const avant = calculation.substring(0, pos)
    const apres = calculation.substring(pos + 1)
    return this.calcul(avant) - this.calcul(apres)
  }

  public static addition (calculation: string): number {
    const pos = calculation.lastIndexOf('+')
    const avant = calculation.substring(0, pos)
    const apres = calculation.substring(pos + 1)
    return this.calcul(avant) + this.calcul(apres)
  }

  public static multiplication (calculation: string): number {
    const pos = calculation.lastIndexOf('*')
    const avant = calculation.substring(0, pos)
    const apres = calculation.substring(pos + 1)
    return this.calcul(avant) * this.calcul(apres)
  }

  public static division (calculation: string): number {
    const pos = calculation.lastIndexOf('/')
    const avant = calculation.substring(0, pos)
    const apres = calculation.substring(pos + 1)
    return this.calcul(avant) / this.calcul(apres)
  }

  // fonction à appeler pour faire les calculs, peut être faire les tests de présence de parenthèse pour être plus rapide, renommer les variables en anglais et les commentaires
  public static parenthesis (calculation: string): number {
    const newCalculation = calculation.replace(/x/g, '*')
    const nombre = this.validityParenthesis(newCalculation)
    const list = this.separateParenthesis(newCalculation, nombre)
    const result = this.calculParenthesis(newCalculation, nombre, list)
    return this.calcul(result)
  }

  // peut être amélioré, en passant la boucle sur les parenthèses seulement
  public static validityParenthesis (calculation: string): number {
    // test de validité de l'équation (niveau parenthèses)
    let compteur = 0
    let nbParenthesis = 0
    for (let i = 0; i < calculation.length; i++) {
      if (compteur < 0) {
        return -1
      }
      if (calculation.charAt(i) === '(') {
        compteur++
        nbParenthesis++
      } else if (calculation.charAt(i) === ')') {
        compteur--
      }
    }
    if (compteur !== 0) {
      return -1
    }
    return nbParenthesis
  }

  // peut être amélioré, en passant la boucle sur les parenthèses seulement
  public static separateParenthesis (calculation: string, nb: number): Array<Array<number>> {
    // renvoie un tableau avec les indices des parenthèses par couple ouverte/fermée
    const list: Array<Array<number>> = Array(nb)
    let indexList = 0
    for (let i = 0; i < calculation.length; i++) {
      if (calculation.charAt(i) === '(') {
        list[indexList] = [i, -1]
        indexList++
      } else if (calculation.charAt(i) === ')') {
        let indexInvers = indexList - 1
        while (list[indexInvers][1] !== -1) {
          indexInvers--
        }
        list[indexInvers][1] = i
      }
    }
    return list
  }

  public static calculParenthesis (calculation: string, nb: number, tab: Array<Array<number>>): string {
    if (nb === 0) {
      return calculation
    }
    let numParenthesis = 0
    if (nb > 1) {
      let ecart = tab[0][1] - tab[0][0]
      for (let i = 1; i < nb; i++) {
        if (tab[i][1] - tab[i][0] < ecart) {
          ecart = tab[i][1] - tab[i][0]
          numParenthesis = i
        }
      }
    }
    const prio = this.calcul(calculation.substring(tab[numParenthesis][0] + 1, tab[numParenthesis][1]))
    let avant = ''
    let apres = ''
    if (tab[numParenthesis][0] !== 0) {
      avant = calculation.substring(0, tab[numParenthesis][0])
    }
    if (tab[numParenthesis][1] !== calculation.length - 1) {
      apres = calculation.substring(tab[numParenthesis][1] + 1)
    }
    const newCalculation = avant + prio + apres
    const newTab = this.separateParenthesis(newCalculation, nb - 1)
    return this.calculParenthesis(newCalculation, nb - 1, newTab)
  }
}
