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

  public static calcul (calculation: string): number {
    let result: number
    if (calculation.indexOf('+') !== -1) {
      const pos = calculation.indexOf('+')
      const avant = calculation.substring(0, pos)
      const apres = calculation.substring(pos + 1)
      console.log(pos, calculation)
      result = this.calcul(avant) + this.calcul(apres)
    } else if (calculation.indexOf('-') !== -1) {
      const pos = calculation.indexOf('-')
      const avant = calculation.substring(0, pos)
      const apres = calculation.substring(pos + 1)
      console.log(pos, calculation)
      result = this.calcul(avant) - this.calcul(apres)
    } else if (calculation.indexOf('*') !== -1) {
      const pos = calculation.indexOf('*')
      const avant = calculation.substring(0, pos)
      const apres = calculation.substring(pos + 1)
      console.log(pos, calculation)
      result = this.calcul(avant) * this.calcul(apres)
    } else if (calculation.indexOf('/') !== -1) {
      const pos = calculation.indexOf('/')
      const avant = calculation.substring(0, pos)
      const apres = calculation.substring(pos + 1)
      console.log(pos, calculation)
      result = this.calcul(avant) / this.calcul(apres)
    } else {
      result = this.parseComma(calculation)
    }
    return Math.trunc(result * 100) / 100
  }

  public static parenthesis (calculation: string): number {
    const nombre = this.ValidityParenthesis(calculation)
    console.log('nombre de parenthèses ' + nombre)
    const list = this.separateParenthesisBis(calculation, nombre)
    console.log('tableau ' + list)
    const result = this.calculParenthesis(calculation, nombre, list)
    console.log('resultat' + result)
    return result
  }

  public static ValidityParenthesis (calculation: string): number {
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

  public static separateParenthesisBis (calculation: string, nb: number): Array<Array<number>> {
    // renvoie un tableau avec les indices des parenthèse par couple ouverte/fermée
    const list: Array<Array<number>> = Array(nb)
    let indexList = 0
    console.log(list)
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

  public static calculParenthesis (calculation: string, nb: number, tab: Array<Array<number>>): number {
    let newCalculation = ''
    let indice = 0
    for (let i = 0; i < nb; i++) {
      if (indice < tab[i][0]) {
        newCalculation = newCalculation + calculation.substring(indice, tab[i][0])
      }
      const prio = calculation.substring(tab[i][0] + 1, tab[i][1])
      newCalculation = newCalculation + this.calcul(prio)
      indice = tab[i][1]
    }
    console.log('indice ' + indice, 'length ' + calculation.length)
    if (indice < calculation.length - 1) {
      newCalculation = newCalculation + calculation.substring(indice + 1, calculation.length)
    }
    console.log(newCalculation, 'nouveau calcul')
    return this.calcul(newCalculation)
  }
}
