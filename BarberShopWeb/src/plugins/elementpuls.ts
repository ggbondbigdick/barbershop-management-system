// @ts-ignore
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// @ts-ignore
import locale from 'element-plus/es/locale/lang/zh-cn'
import * as ElIconModules from '@element-plus/icons-vue'
import {App} from 'vue'

export default (app: App<Element>) => {
    app.use(ElementPlus, {locale})
    for (const iconName in ElIconModules) {
        app.component(
            iconName,
            ElIconModules[iconName as keyof typeof ElIconModules]
        )
    }
}