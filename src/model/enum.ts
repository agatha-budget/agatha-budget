import { Currency } from '@/model/model'

const currencies: {[name: string]: Currency} = {
  EUROS: { name: 'euros', symbol: '€' },
  POUNDS: { name: 'pounds', symbol: '£' }
}

export { currencies }
