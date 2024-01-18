import { createPinia } from 'pinia'
import { createApp } from 'vue'

import App from '@/App.vue'
import localeDate from '@/assets/locale/dateformat.json'
import localeEN from '@/assets/locale/en.json'
import localeFR from '@/assets/locale/fr.json'
import HttpService from "@/services/api/httpservice"
import KeyCloakService from "@/services/security/KeycloakService"
import 'bootstrap'
import { createI18n } from 'vue-i18n'
import Properties from '../properties'
import router from './router'

const messages = {
  fr: localeFR,
  en: localeEN
}

const i18n = createI18n({ 
  locale: 'fr', 
  fallbackLocale: 'en', 
  messages, 
  datetimeFormats : localeDate,
})

console.log('version : ' + Properties.commitHash)

const renderApp = () => {
  createApp(App)
  .use(createPinia())
  .use(router)
  .use(i18n)
  .mount("#app");
};

KeyCloakService.CallLogin(renderApp);
HttpService.configureAxiosKeycloak();