import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import pinia from './store'
import installFontawesome from '@/plugins/fontawesome'
import installElementPlus from '@/plugins/elementpuls'

const app = createApp(App)

installFontawesome(app)
installElementPlus(app)

app.use(router)
app.use(pinia)

app.mount('#app')
