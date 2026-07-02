import {createRouter, createWebHistory} from 'vue-router'
import Routes from '@/router/routes'
import {getUserMenus} from '@/tool/menuTool'
import {getToken, getUserInfo, checkTokenExpire} from '@/tool/loginTool'

const router = createRouter({
    history: createWebHistory('/haircut'),
    routes: Routes
})


// 路由前置守卫
router.beforeEach((to, from, next) => {
    // 检测token是否过期
    checkTokenExpire()
    if (to.path === '/login' || !to.meta?.requiresAuth) {
        next()
    } else if (getToken()) {
        next()
    } else {
        next('/login')
    }
})

// 获取菜单的路由
export const getMenuRoutes = (): string[] => {

    // 获取对象的路由属性组成 list
    let menuList: any[] = getUserMenus(getUserInfo().role)

    // 递归获取当前角色的菜单和子菜单路径的集合
    let list: string[] = []
    menuList.forEach(item => {
        list.push(item.path)
        if (item.children) {
            item.children.forEach((child: any) => {
                list.push(child.path)
            })
        }
    })

    if (getUserInfo().role === '1') list.push('/PersonalCenter')

    return list
}

export default router
