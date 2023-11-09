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
import KeyCloakService from "@/services/security/KeycloakService";
import HttpService from "@/services/api/httpservice";

const messages = {
  fr: localeFR,
  en: localeEN
}
const i18n = createI18n({ locale: 'fr', fallbackLocale: 'en', messages, localeDate })
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