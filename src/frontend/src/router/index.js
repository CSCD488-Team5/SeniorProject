import { createRouter, createWebHashHistory } from 'vue-router';
import Login from '../components/Login.vue';
import Signup from '../components/Signup.vue';

const routes = [
  { path: '/Login', name: 'Login', component: Login },
  { path: '/Signup', name: 'Signup', component: Signup },
  { path: '/', redirect: '/Signup' }, // Redirect root to /Signup
];

const router = createRouter({
  history: createWebHashHistory(), // Ensures the browser doesn't break on refresh
  routes,
});

export default router;
