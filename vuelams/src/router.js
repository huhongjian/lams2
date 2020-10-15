import Vue from 'vue'
import Router from 'vue-router'
import Login from './views/Login.vue'
import Home from './views/Home.vue'
import HrInfo from './views/HrInfo.vue'
import SignUp from "@/views/SignUp";

Vue.use(Router)

export default new Router({
    routes: [
        {
            path: '/',
            name: 'Login',
            component: Login,
            hidden: true
        },
        {
            path: '/signUp',
            name: 'SignUp',
            component: SignUp,
            hidden: true
        },
        {
            path: '/home',
            name: 'Home',
            component: Home,
            hidden: true,
            meta: {
                roles: ['admin', 'user']
            },
            children: [
                {
                    path: '/hrinfo',
                    name: '个人中心',
                    component: HrInfo,
                    hidden: true
                }
            ]
        }, {
            path: '*',
            redirect: '/home'
        }
    ]
})