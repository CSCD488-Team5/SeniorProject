import { createRouter, createWebHashHistory } from 'vue-router';
import Login from '../components/Login.vue';
import Signup from '../components/Signup.vue';
import PostPage from "@/components/PostPage.vue";

const routes = [
  { path: '/Login', name: 'Login', component: Login },
  { path: '/Signup', name: 'Signup', component: Signup },
  {path: '/PostPage', name: 'PostPage', component: PostPage},
  { path: '/', redirect: '/Signup' }, // Redirect root to /Signup
];

const router = createRouter({
  history: createWebHashHistory(), // Ensures the browser doesn't break on refresh
  routes,
});

export default router;
