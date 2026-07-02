// 获取 token
import {ElMessage, ElNotification} from 'element-plus'

export const getToken = (): any => {
    return localStorage.getItem('token')
}

// 获取设备类型
export const getDeviceType = () => {
    return localStorage.getItem('device')
}

// 检查用户是否登录
export const checkLogin = () => {
    if (!getToken()) {
        return false
    } else {
        return true
    }
}

// 获取登录用户信息
export const getUserInfo = (): any => {
    // @ts-ignore
    let userInfo = JSON.parse(localStorage.getItem('userInfo'))

    if (userInfo) {
        return userInfo
    } else {
        return ""
    }
}


// 清除 TOKEN 和 用户信息
export const clearAll = () => {
    localStorage.clear()
}


// 检查 TOKEN 是否过期
export const checkTokenExpire = () => {

    let expire = Number(localStorage.getItem('tokenExpire'))

    if (expire > 0 && expire < Math.floor(Date.now() / 1000)) {
        let msg = '身份认证失败，请重新登录'
        ElMessage.error(msg)
        clearAll()
    }
}


