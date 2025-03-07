import { createRouter, createWebHistory } from 'vue-router'
import Login from "@/components/Login.vue"
import Signup from "@/components/Signup.vue"

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/Login',
      name: 'Login',
      component: Login,
    },
    {
      path:'/Signup',
      name: 'Signup',
      component: Signup
    },
    {
      path: '/',
      redirect: '/Signup'
    },
  ],
})

export default router
