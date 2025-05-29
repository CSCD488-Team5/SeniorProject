import { createRouter, createWebHistory } from 'vue-router'; // ✅ changed from createWebHashHistory
import Login from '../components/Login.vue';
import Signup from '../components/Signup.vue';
import PostPage from "@/components/PostPage.vue";
import { isTokenValid } from '../utils/jwt';
import Home from "@/components/Home.vue";
import EventDetails from "@/components/EventDetails.vue";

const routes = [
  { path: "/Login", name: "Login", component: Login },
  { path: "/Signup", name: "Signup", component: Signup },
  {
    path: "/PostPage",
    name: "PostPage",
    component: PostPage,
    beforeEnter: (to, from, next) => {
      if (!isTokenValid()) {
        next("/Login");
      } else {
        next();
      }
    },
  },
  { path: "/", redirect: "/Signup" },
  { path: "/Home", name: "Home", component: Home },
  { path: "/Events/:id", name: "EventDetails", component: EventDetails },
  {
    path: "/Profile/:username",
    name: "Profile",
    component: () => import("@/components/Profile.vue"),
  },
  {
    path: "/oauth-callback",
    name: "OAuthCallback",
    component: () => import("@/components/OAuthCallback.vue"),
  },
  {
    path: "/oauth2callback-google",
    name: "OAuthCallbackGoogle",
    component: () => import("@/components/OAuthCallbackGoogle.vue"),
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
