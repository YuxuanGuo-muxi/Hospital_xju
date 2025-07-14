import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from '@/renderer/router/index.js'
import store from '@/renderer/store/index.js'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// 引入路由
const app = createApp(App);


app.use(router).use(ElementPlus).use(store).mount('#app');

