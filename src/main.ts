import { createApp } from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import { store, key } from './store'
import 'bootstrap'
import { createI18n } from 'vue-i18n'
import localeFR from '@/assets/locale/fr.json'
import localeEN from '@/assets/locale/en.json'
import localeDate from '@/assets/locale/dateformat.json'
import Properties from '@/../properties'

const messages = {
  fr: localeFR,
  en: localeEN
}

const i18n = createI18n({ locale: 'fr', fallbackLocale: 'en', messages: messages, datetimeFormats: localeDate })
console.log('version : ' + Properties.commitHash)
createApp(App).use(store, key).use(router).use(i18n).mount('#app')
