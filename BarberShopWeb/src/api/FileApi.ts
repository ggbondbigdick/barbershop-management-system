import http from '@/tool/http'

// upload file
export const uploadFile = (file: any) => http.post('/file/upload', file)

// get file info
export const getFileInfo = (fileId: string) => http.get(`/file/info?id=${fileId}`)

// preview file
export const previewFile = (fileId: string) => http.get(`/file/preview/${fileId}`)

// download file
export const downloadFile = (fileId: string) => http.get(`/file/download/${fileId}`, { responseType: 'blob' })

// delete file
export const deleteFile = (fileId: string) => http.get(`/file/del/${fileId}`)
