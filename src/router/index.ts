import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Profile from '../views/Profile.vue'
import Signup from '../views/Signup.vue'
import AccountPage from '../views/AccountPage.vue'
import { StoreState } from '@/store/index'
import { Store } from 'vuex'
import { AxiosResponse } from 'axios'

export enum RouterPages {
  home = '/',
  login = '/login',
  signup = '/signup',
  account = '/account',
  about = '/about',
  profile = '/profile'
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

export function redirectToLoginPageIfUnauthorizedError (response: AxiosResponse) {
  if (response.status === 401) {
    router.push(RouterPages.login)
  }
}

export default router
