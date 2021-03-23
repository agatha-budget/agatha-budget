import { createApp } from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import { store, key } from './store'
import 'bootstrap'
import { createI18n } from 'vue-i18n'

const messages = {
  fr: {
    FEBRUARY: 'février'
  },
  en: {
    FEBRUARY: 'february',
    MARCH: 'march'
  }
}

const i18n = createI18n({ locale: 'fr', fallbackLocale: 'en', messages })

createApp(App).use(store, key).use(router).use(i18n).mount('#app')
