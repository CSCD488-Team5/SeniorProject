import { createApp } from 'vue'
import App from './App.vue'
import router from './router/index.js'
import axios from "axios";
import  { isTokenValid } from "./utils/jwt.js";

// Set Axios base URL from environment variable
axios.defaults.baseURL = import.meta.env.VITE_API_URL

// Axios interceptor
axios.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    // Only add token for non-/api/auth/ requests
    if (token && isTokenValid() && !config.url.includes('/api/auth/login') && !config.url.includes('/api/auth/signup')) {
        config.headers.Authorization = `Bearer ${token}`;
        console.log('Adding token to:', config.url); // Debug
    } else if (token && !isTokenValid()) {
        localStorage.removeItem('token');
        router.push('/login');
        console.log('Token expired, redirecting to login');
    }
    console.log('Request:', config.url, 'Headers:', config.headers); // Debug
    return config;
}, error => {
    console.error('Interceptor error:', error);
    return Promise.reject(error);
});

// Vuetify
import 'vuetify/styles'
import { createVuetify } from "vuetify";
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import DayJsAdapter from '@date-io/dayjs'
import { VTimePicker } from 'vuetify/labs/VTimePicker'



const vuetify = createVuetify({
    date: {
        adapter: DayJsAdapter,
    },
    components: {
        ...components,
        VTimePicker,
    },
    directives,
})

const app = createApp(App)
app.use(router)
app.config.globalProperties.$http = axios;
app.use(vuetify)

app.mount('#app')
