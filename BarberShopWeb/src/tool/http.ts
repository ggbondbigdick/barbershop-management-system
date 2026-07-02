import axios, { AxiosResponse, InternalAxiosRequestConfig } from 'axios'
import { ShowLoading, HideLoading } from '@/tool/loadingStatusTool'
import { getToken, clearAll } from '@/tool/loginTool'
import {ElMessage, ElNotification} from 'element-plus'
import router from '@/router'


const http = axios.create({
    baseURL: '/api',
    timeout: 100000,
})


// 请求拦截
http.interceptors.request.use((req: InternalAxiosRequestConfig) => {
    ShowLoading()
    if (getToken()) {
        req.headers['Authorization'] = getToken()
        req.headers['timezone'] = Intl.DateTimeFormat().resolvedOptions().timeZone
    }
    return req
}, (error: any) => {
    HideLoading()
    return Promise.reject(error)
})


// 响应拦截
http.interceptors.response.use((res: AxiosResponse) => {

    HideLoading()
    let code = res.data.code

    if (code === 11012 || code === 11013) {
        let msg = '身份认证失败，请重新登录'
        ElMessage.error(msg)
        router.push('/login')
        clearAll()
    }

    if (code === 11014 || code === 11015) {
        let msg = '该帐户已在其他地方登录已被下线'
        ElMessage.error(msg)
        router.push('/login')
        clearAll()
    }

    if (!res.data.success && res.data.errorDesc) {
        let msg = res.data.errorDesc
        ElMessage.error(msg)
    }
    return res.data
}, (error: any) => {
    HideLoading()
    let msg = '网络连接错误，请稍后再试'
    ElMessage.error(msg)
    return Promise.reject(error)
})


export default http
