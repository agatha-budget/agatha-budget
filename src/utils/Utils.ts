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

  // to calculate a mathematical expression
  public static entireCalcul (calculation: string): number {
    const newCalculation = calculation.replace(/x/g, '*')
    if (calculation.includes(')') && calculation.includes('(')) {
      const numberParenthesis = this.validityParenthesis(newCalculation)
      const listParenthesis = this.separateParenthesis(newCalculation, numberParenthesis)
      const result = this.calculParenthesis(newCalculation, numberParenthesis, listParenthesis)
      return this.basicCalcul(result)
    }
    return this.basicCalcul(newCalculation)
  }

  // resolve a mathematical expression without parenthesis
  public static basicCalcul (calculation: string): number { // 9*((6*7-4)+3)*(5+6-(1+4))/2/3/3/3 returns 41
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
    const position = calculation.lastIndexOf('-')
    const before = calculation.substring(0, position)
    const after = calculation.substring(position + 1)
    return this.basicCalcul(before) - this.basicCalcul(after)
  }

  public static addition (calculation: string): number {
    const position = calculation.lastIndexOf('+')
    const before = calculation.substring(0, position)
    const after = calculation.substring(position + 1)
    return this.basicCalcul(before) + this.basicCalcul(after)
  }

  public static multiplication (calculation: string): number {
    const position = calculation.lastIndexOf('*')
    const before = calculation.substring(0, position)
    const after = calculation.substring(position + 1)
    return this.basicCalcul(before) * this.basicCalcul(after)
  }

  public static division (calculation: string): number {
    const position = calculation.lastIndexOf('/')
    const before = calculation.substring(0, position)
    const after = calculation.substring(position + 1)
    return this.basicCalcul(before) / this.basicCalcul(after)
  }

  // check valid position of parenthesis and returns quantity of pair of these
  public static validityParenthesis (calculation: string): number {
    let counter = 0
    let numberParenthesis = 0
    for (let i = 0; i < calculation.length; i++) {
      if (counter < 0) {
        return -1
      }
      if (calculation.charAt(i) === '(') {
        counter++
        numberParenthesis++
      } else if (calculation.charAt(i) === ')') {
        counter--
      }
    }
    if (counter !== 0) {
      return -1
    }
    return numberParenthesis
  }

  // return list of parenthesis' position filed per pair
  public static separateParenthesis (calculation: string, numberParenthesis: number): Array<Array<number>> {
    const list: Array<Array<number>> = Array(numberParenthesis)
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

  // return priority basic expression due to parenthesis
  public static calculParenthesis (calculation: string, numberParenthesis: number, listParenthesis: Array<Array<number>>): string {
    if (numberParenthesis === 0) {
      return calculation
    }
    let indexParenthesis = 0
    if (numberParenthesis > 1) {
      let ecart = listParenthesis[0][1] - listParenthesis[0][0]
      for (let i = 1; i < numberParenthesis; i++) {
        if (listParenthesis[i][1] - listParenthesis[i][0] < ecart) {
          ecart = listParenthesis[i][1] - listParenthesis[i][0]
          indexParenthesis = i
        }
      }
    }
    const priority = this.basicCalcul(calculation.substring(listParenthesis[indexParenthesis][0] + 1, listParenthesis[indexParenthesis][1]))
    let before = ''
    let after = ''
    if (listParenthesis[indexParenthesis][0] !== 0) {
      before = calculation.substring(0, listParenthesis[indexParenthesis][0])
    }
    if (listParenthesis[indexParenthesis][1] !== calculation.length - 1) {
      after = calculation.substring(listParenthesis[indexParenthesis][1] + 1)
    }
    const newCalculation = before + priority + after
    const newList = this.separateParenthesis(newCalculation, numberParenthesis - 1)
    return this.calculParenthesis(newCalculation, numberParenthesis - 1, newList)
  }
}
