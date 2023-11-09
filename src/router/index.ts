import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import HomeView from '@/views/Home.vue'
import LoginView from '@/views/Login.vue'
import ProfileView from '@/views/Profile.vue'
import BanksView from '@/views/Banks.vue'
import SignupView from '@/views/Signup.vue'
import RedirectToAccountPage from '@/views/RedirectToAccountPage.vue'
import AccountView from '@/views/Account.vue'
import SubscriptionView from '@/views/Subscription.vue'
import ChartsView from '@/views/Charts.vue'
import { AxiosError } from 'axios'
import { usePersonStore } from '@/stores/personStore'

export enum RouterPages {
  home = '/',
  login = '/login',
  signup = '/signup',
  account = '/account',
  about = '/about',
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
    path: RouterPages.login,
    component: LoginView
  },
  {
    path: RouterPages.signup,
    component: SignupView
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
  },
  {
    path: RouterPages.about,
    component: () => import('@/views/About.vue')
  },
  {
    path: RouterPages.chartPage,
    component: ChartsView
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export function redirectToLoginPageIfNotLogged () {
  const personStore = usePersonStore()
  if (!personStore.logged) {
    router.push(RouterPages.login)
  }
}

export function redirectOnApiError (error: AxiosError) {
  if (error.response && error.response.status === 402) {
    router.push(RouterPages.invalidSubscription)
  } else {
    router.push(RouterPages.login)
  }
}

export default router