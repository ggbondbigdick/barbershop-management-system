import server from '@/tool/http'


// user register
export const register = (param: any) => server.post('/user/register', param)

// user login
export const login = (param: any) => server.post('/user/login', param)

// user list
export const userPage = (param: any) => server.post('/user/page', param)

// user info
export const userInfo = (param: string) => server.get(`/user/info?id=${param}`)

// get statistics data
export const getStatisticsData = () => server.get('/user/statistics')

// get available staff
export const getAvailableStaff = (projectType: string, reserveDate: string) =>
  server.get(`/user/availableStaff?projectType=${projectType}&reserveDate=${reserveDate}`)

// modify user info
export const modifyUser = (param: any) => server.post('/user/modify', param)

// modify password
export const modifyPassword = (param: any) => server.post('/user/modifyPassword', param)

// reset password
export const resetPassword = (param: any) => server.post('/user/resetPassword', param)

// user recharge
export const userRecharge = (amount: any) => server.get(`/user/recharge?balance=${amount}`)

// freeze or unfreeze user
export const  freezeOrUnfreezeUser = (param: string) => server.get(`/user/freezeOrThaw?id=${param}`)

// get captcha
export const getCaptcha = (email: string, type: string) => server.get(`/user/verifyCode?email=${email}&type=${type}`)

// delete user
export const deleteUser = (param: string) => server.delete(`/user/del/${param}`)

// logout
export const logout = () => server.get('/user/logout')
