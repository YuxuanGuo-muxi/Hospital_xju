import {fileURLToPath, URL} from 'node:url'

import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import legacy from  '@vitejs/plugin-legacy'
// https://vitejs.dev/config/
export default defineConfig({
    base: './',
    build: {
        outDir: 'web',
    },
    plugins: [
        vue({
            reactivityTransform: true
        }),
        legacy({
            targets: ['defaults', 'not IE 11'],
        }),
    ],
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        }
    },
    css: {
        preprocessorOptions: {
            scss: {
                additionalData: `@import "./src/renderer/js/search-popup.scss";`,
            }
        }
    },
    server: {
        proxy: {
            host:'localhost',  // 本地主机
            // 当请求路径以 '/doctor' 开头时，将请求转发到目标服务器 http://localhost:8081
            '/api': {
                // target: 'http://8.130.76.154:8081/',
                target: 'http://localhost:8081/',
                changeOrigin: true,
                secure: false, // 如果是https接口，需要配置这个参数
                pathRewrite: {
                    '^/api':'/'  //这里理解成用‘/api’代替target里面的地址，后面组件中我们掉接口时直接用api代替 比如我要调用'http://40.00.100.100:3002/api/user/add'，直接写‘/api/user/add’即可
                }
            },
        },
    }
})
