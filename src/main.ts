import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import 'bootstrap'
import { createI18n } from 'vue-i18n'
import localeFR from '@/assets/locale/fr.json'
import localeEN from '@/assets/locale/en.json'
import localeDate from '@/assets/locale/dateformat.json'
import Properties from '../properties'

const messages = {
  fr: localeFR,
  en: localeEN
}
const i18n = createI18n({ locale: 'fr', fallbackLocale: 'en', messages, localeDate })
console.log('version : ' + Properties.commitHash)

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(i18n)

app.mount('#app')
