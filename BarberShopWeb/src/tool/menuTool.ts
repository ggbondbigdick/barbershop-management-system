// 根据用户类型获取相应的菜单数据
export const getUserMenus = (val: string): any => {
    if (val === '1') return SuperAdminMenus
    if (val === '3') return StaffMenus
    return ''
}

// Super Admin Menus
const SuperAdminMenus = [
    {
        path: '/Content',
        name: 'Content',
        title: '主页'
    },
    {
        path: '/CustomerManagement',
        name: 'CustomerManagement',
        title: '客户管理'
    },
    {
        path: '/StaffManagement',
        name: 'StaffManagement',
        title: '员工管理'
    },
    {
        path: '/AttendanceManagement',
        name: 'AttendanceManagement',
        title: '考勤管理'
    },
    {
        path: '/HaircuttingProjectManagement',
        name: 'HaircuttingProjectManagement',
        title: '理发项目管理'
    },
    {
        path: '/AppointmentInfo',
        name: 'AppointmentInfo',
        title: '客户预约信息'
    },
    {
        path: '/EvaluateManagement',
        name: 'EvaluateManagement',
        title: '预约评价管理'
    },
    {
        path: '/PasswordModify',
        name: 'PasswordModify',
        title: '账户密码修改'
    }
]



// Customer Menus
const StaffMenus = [
    {
        path: '/Content',
        name: 'Content',
        title: '主页'
    },
    {
        path: '/AppointmentInfo',
        name: 'AppointmentInfo',
        title: '预约信息'
    },
    {
        path: '/AttendanceManagement',
        name: 'AttendanceManagement',
        title: '考勤信息'
    },
    {
        path: '/PasswordModify',
        name: 'PasswordModify',
        title: '账户密码修改'
    }
]
