import server from '@/tool/http'

export default {
    GetEvaluatePaginationList: (data: any) => server.post('/evaluate/page', data),

    GetEvaluateInfo: (data: string) => server.get(`/evaluate/info?id=${data}`),

    AddEvaluate: (data: any) => server.post('/evaluate/add', data),

    ModifyEvaluate: (data: any) => server.post('/evaluate/modify', data),

    DeleteEvaluate: (data: string) => server.get(`/evaluate/del?id=${data}`)
}