import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const now = new Date();

const store = new Vuex.Store({
    state: {
        routes: [],
        currentUser: JSON.parse(window.sessionStorage.getItem("user"))
    },
    mutations: {
        INIT_CURRENTUSER(state, user) {
            state.currentUser = user;
        },
        initRoutes(state, data) {
            state.routes = data;
        }
    }
})

export default store;