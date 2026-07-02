const routes = [
    {
        path: '/login',
        name: 'login',
        meta: {requiresAuth: false},
        component: () => import('@/view/Login.vue')
    },
    {
        path: '/register',
        name: 'register',
        meta: {requiresAuth: false},
        component: () => import('@/view/Register.vue')
    },
    {
        path: '/',
        name: 'index',
        redirect: '/hairstyle',
        meta: {requiresAuth: false},
        component: () => import('@/view/home/Home.vue'),
        children: [
            {
                path: '/hairstyle',
                name: 'hairstyle',
                meta: {requiresAuth: false},
                component: () => import('@/view/home/Hairstyle.vue')
            },
            {
                path: '/reserve',
                name: 'reserve',
                meta: {requiresAuth: false},
                component: () => import('@/view/home/Reserve.vue')
            },
            {
                path: '/myReserve',
                name: 'myReserve',
                meta: {requiresAuth: true},
                component: () => import('@/view/home/MyAppointment.vue')
            },
            {
                path: '/myInfo',
                name: 'myInfo',
                meta: {requiresAuth: true},
                component: () => import('@/view/home/PersonalInfo.vue')
            }
        ]
    },
    {
        path: '/home',
        name: 'home',
        redirect: '/Content',
        meta: {requiresAuth: true},
        component: () => import('@/view/Home.vue'),
        children: [
            {
                path: '/Content',
                name: 'Content',
                meta: {requiresAuth: true},
                component: () => import('@/view/Content.vue')
            },
            {
                path: '/CustomerManagement',
                name: 'CustomerManagement',
                meta: {requiresAuth: true},
                component: () => import('@/view/user/Customer.vue')
            },
            {
                path: '/StaffManagement',
                name: 'StaffManagement',
                meta: {requiresAuth: true},
                component: () => import('@/view/user/User.vue')
            },
            {
                path: '/AttendanceManagement',
                name: 'AttendanceManagement',
                meta: {requiresAuth: true},
                component: () => import('@/view/attendance/Attendance.vue')
            },
            {
                path: '/HaircuttingProjectManagement',
                name: 'HaircuttingProjectManagement',
                meta: {requiresAuth: true},
                component: () => import('@/view/project/Project.vue')
            },
            {
                path: '/AppointmentInfo',
                name: 'AppointmentInfo',
                meta: {requiresAuth: true},
                component: () => import('@/view/reserve/Reserve.vue')
            },
            {
                path: '/EvaluateManagement',
                name: 'EvaluateManagement',
                meta: {requiresAuth: true},
                component: () => import('@/view/evaluate/Evaluate.vue')
            },
            {
                path: '/PasswordModify',
                name: 'PasswordModify',
                meta: {requiresAuth: true},
                component: () => import('@/view/PassModify.vue')
            }
        ]
    }
]


export default routes
