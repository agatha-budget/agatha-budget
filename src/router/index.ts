import KeycloakService from "@/services/security/KeycloakService";
import AccountView from '@/views/Account.vue';
import BanksView from '@/views/Banks.vue';
import HomeView from '@/views/Home.vue';
import ProfileView from '@/views/Profile.vue';
import RedirectToAccountPage from '@/views/RedirectToAccountPage.vue';
import SubscriptionView from '@/views/Subscription.vue';
import { AxiosError } from 'axios';
import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';

export enum RouterPages {
  home = '/',
  account = '/account',
  profile = '/profile',
  banks = '/bank',
  subscription = '/subscription',
  invalidSubscription = '/invalidSubscription',
  redirectToAccountPage = '/redirectToAccountPage',
  chartPage = '/chart'
}

const routes: Array<RouteRecordRaw> = [
  {
    path: RouterPages.home,
    component: HomeView
  },
  {
    path: RouterPages.profile,
    component: ProfileView
  },
  {
    path: RouterPages.banks,
    component: BanksView,
    props: route => ({ query: route.query.agreementId })
  },
  {
    path: RouterPages.redirectToAccountPage,
    component: RedirectToAccountPage
  },
  {
    path: RouterPages.subscription,
    component: SubscriptionView,
    props: { validSubscription: true }
  },
  {
    path: RouterPages.invalidSubscription,
    component: SubscriptionView,
    props: { validSubscription: false }
  },
  {
    path: RouterPages.account,
    component: AccountView,
    props: route => ({ accountId: route.query.accountId })
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export function redirectOnApiError (error: AxiosError) {
  if (error.response && error.response.status === 402) {
    router.push(RouterPages.invalidSubscription)
  } else {
    KeycloakService.CallLogOut();
  }
}

export default router