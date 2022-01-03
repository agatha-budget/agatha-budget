import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Profile from '../views/Profile.vue'
import Signup from '../views/Signup.vue'
import RedirectToAccountPage from '../views/RedirectToAccountPage.vue'
import AccountPage from '../views/AccountPage.vue'
import { StoreState } from '@/store/index'
import { Store } from 'vuex'
import { AxiosError, AxiosResponse } from 'axios'

export enum RouterPages {
  home = '/',
  login = '/login',
  signup = '/signup',
  account = '/account',
  about = '/about',
  profile = '/profile',
  redirectToAccountPage = '/redirectToAccountPage'
}

const routes: Array<RouteRecordRaw> = [
  {
    path: RouterPages.home,
    component: Home
  },
  {
    path: RouterPages.login,
    component: Login
  },
  {
    path: RouterPages.signup,
    component: Signup
  },
  {
    path: RouterPages.profile,
    component: Profile
  },
  {
    path: RouterPages.redirectToAccountPage,
    component: RedirectToAccountPage
  },
  {
    path: RouterPages.account,
    component: AccountPage,
    props: route => ({ accountId: route.query.accountId })
  },
  {
    path: RouterPages.about,
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
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
  if (error.response!.status === 402) {
    router.push(RouterPages.profile)
  } else {
    router.push(RouterPages.login)
  }
}

export default router
