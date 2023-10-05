import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import HomeView from '../views/Home.vue'
import LoginView from '../views/Login.vue'
import ProfileView from '../views/Profile.vue'
import BanksView from '../views/Banks.vue'
import SignupView from '../views/Signup.vue'
import RedirectToAccountPage from '../views/RedirectToAccountPage.vue'
import AccountView from '../views/Account.vue'
import SubscriptionView from '../views/Subscription.vue'
import ChartsView from '../views/Charts.vue'
import { StoreState } from '@/store/index'
import { Store } from 'vuex'
import { AxiosError } from 'axios'

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
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: RouterPages.chartPage,
    component: ChartsView
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export function redirectToLoginPageIfNotLogged (store: Store<StoreState>) {
  if (!store.state.logged) {
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
