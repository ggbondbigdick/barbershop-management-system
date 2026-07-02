import server from '@/tool/http'

export default {
    GetAttendancePaginationList: (data: any) => server.post('/attendance/list', data),

    GetAttendanceInfo: (data: string) => server.get(`/attendance/info?id=${data}`),

    AddAttendance: (data: any) => server.post('/attendance/add', data),

    ModifyAttendance: (data: any) => server.post('/attendance/modify', data),

    SignIn: () => server.get('/attendance/sign'),

    DeleteAttendance: (data: string) => server.get(`/attendance/del?id=${data}`)
}
