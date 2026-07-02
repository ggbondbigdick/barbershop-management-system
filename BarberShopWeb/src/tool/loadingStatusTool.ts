import { ElLoading } from 'element-plus'

let loadingInstance: any = null

export const ShowLoading = () => {
    if (!loadingInstance) {
        loadingInstance = ElLoading.service({
            lock: true,
            text: 'Loading...',
            background: 'rgba(0, 0, 0, 0.7)'
        })
    }
}

export const HideLoading = () => {
    if (loadingInstance) {
        loadingInstance.close()
        loadingInstance = null
    }
}
