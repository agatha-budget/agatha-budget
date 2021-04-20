import { createApp } from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import { store, key } from './store'
import 'bootstrap'
import { createI18n } from 'vue-i18n'
import localeFR from '@/assets/locale/fr.json'
import localeEN from '@/assets/locale/en.json'

const messages = {
  fr: localeFR,
  en: localeEN
}

const i18n = createI18n({ locale: 'fr', fallbackLocale: 'en', messages })
createApp(App).use(store, key).use(router).use(i18n).mount('#app')
