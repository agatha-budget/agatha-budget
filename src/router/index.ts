import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import AccountPage from '../views/AccountPage.vue'
import { StoreState } from '@/store/index'
import { Store } from 'vuex'

export enum RouterPages {
  home = '/',
  login = '/login',
  account = '/account',
  about = '/about'
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
    path: RouterPages.account,
    component: AccountPage,
    props: {
      accountId: (route: { query: { accountId: String } })  => (route.query.accountId)  
    }
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

export default router
