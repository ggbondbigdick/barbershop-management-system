import server from '@/tool/http'


export default {
  GetReservePaginationList: (data: any) => server.post('/reserve/page', data),

  GetReserveInfo: (data: string) => server.get(`/reserve/info?id=${data}`),

  AddReserve: (data: any) => server.post('/reserve/add', data),

  ModifyReserve: (data: any) => server.post('/reserve/modify', data),

  ReserveCheck: (data: any) => server.post('/reserve/check', data),

  ReservePay: (data: string) => server.get(`/reserve/pay?id=${data}`),

  CancelReserve: (data: string) => server.get(`/reserve/cancel?id=${data}`),

  DeleteReserve: (data: string) => server.get(`/reserve/del?id=${data}`)
}
