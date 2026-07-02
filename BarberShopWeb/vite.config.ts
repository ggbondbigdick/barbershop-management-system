import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// https://vitejs.dev/config/
export default defineConfig({
    base: '/haircut',
    plugins: [vue()],
    resolve: {
        extensions: ['.js', '.ts', '.vue'],
        // 配置别名
        alias: [
            {find: '@', replacement: path.resolve(__dirname, 'src')}
        ]
    },
    css: {
        preprocessorOptions: {
            less: {
                modifyVars: {
                    hack: `true; @import (reference) "${path.resolve("src/assets/less/reset.less")}";`,
                },
                javascriptEnabled: true,
            }
        }
    },
    server: {
        host: '0.0.0.0',
        port: 9070,
        proxy: {
            '/api': {
                target: 'http://127.0.0.1:8080',
                changeOrigin: true,
                rewrite: path => path.replace(/^\/api/, '')
            }
        }
    }
})
