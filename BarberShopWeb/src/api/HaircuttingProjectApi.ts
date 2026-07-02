import server from '@/tool/http'

export default {
    GetHaircuttingProjectPaginationList: (data: any) => server.post('/haircuttingProject/list', data),

    GetHomeHaircuttingProjectList: () => server.get('/haircuttingProject/home'),

    GetHaircuttingProjectInfo: (id: string) => server.get(`/haircuttingProject/info?id=${id}`),

    AddHaircuttingProject: (data: any) => server.post('/haircuttingProject/add', data),

    ModifyHaircuttingProject: (data: any) => server.post('/haircuttingProject/modify', data),

    DeleteHaircuttingProject: (id: string) => server.delete(`/haircuttingProject/del/id=${id}`)
}
