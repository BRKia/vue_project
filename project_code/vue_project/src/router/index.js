import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store/store";
import manage from "@/views/Manage";

Vue.use(VueRouter)

const routes = [
  // {
  //   path: '/',
  //   component: () => import('../views/Manage.vue'),
  //   redirect: "/home",
  //   children: [
  //     { path: 'home', name: '首页', component: () => import('../views/Home.vue')},
  //     { path: 'menu', name: '菜单', component: () => import('../views/Menu.vue')},
  //     { path: 'user', name: '用户管理', component: () => import('../views/User.vue')},
  //     { path: 'role', name: '角色管理', component: () => import('../views/Role.vue')},
  //     { path: 'person', name: '个人信息', component: () => import('../views/Person.vue')},
  //     { path: 'file', name: '文件管理', component: () => import('../views/File.vue')},
  //   ]
  // },
  {
    path: '/about',
    name: 'About',
    component: () => import('../views/About.vue')
  },
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('../views/404.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  }
  // {path: '/person', name: 'Person', component: () => import('../views/Person.vue')}
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})
// 提供一个重置路由的方法
export const resetRouter = () => {
  router.matcher = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
  })
}

// 刷新页面后重置路由
export const setRouters = () => {
  const storeMenus = localStorage.getItem("menus");
  if (storeMenus) {
    const currentRouteNames = router.getRoutes().map(v => v.name)
    // 避免反复添加，如果已经有了就不添加。接下来直接再调用一次
    if (!currentRouteNames.includes('Manager')) {
      // 若用户角色有该菜单则拼装动态路由
      const manageRoute = { path: '/',name: 'Manager' ,component: () => import('../views/Manage.vue'), redirect: "/home", children: []}
      manageRoute.children.push({path: '/person', name: 'Person', component: () => import('../views/Person.vue')})
      const menus = JSON.parse(storeMenus)
      // 动态添加到现在的路由对象中
      menus.forEach(item => {
        // 当父级菜单路径不为空时，才设置拼装路由。否则找子菜单
        if (item.path) {
          let itemMenu = { path: item.path.replace('/', ''), name: item.name, component: () => import('../views/'+ item.pagePath +'.vue')}
          manageRoute.children.push(itemMenu)
        } else if (item.children.length) {
          item.children.forEach(item => {
            if (item.path) {
              let itemMenu = {
                path: item.path.replace('/', ''),
                name: item.name,
                component: () => import('../views/' + item.pagePath + '.vue')
              }
              manageRoute.children.push(itemMenu)
            }
          })
        }
      })
      router.addRoute(manageRoute)
    }
  }
}
setRouters()
// 路由守卫
router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称，为了在Header组件中去使用
  store.commit("setPath")  // 触发store的数据更新

  //未找到路由
  if (!to.matched.length) {
    const storeCurrentMenu = localStorage.getItem("menus")
    if (storeCurrentMenu) {
      // 若有菜单则但没有路由则跳404
      next("/404")
    } else {
      // 若没有菜单且没有路由则需要登录
      next("/login")
    }
  } else {
    // 若有路由，则直接放行
    next()  // 放行路由
  }


})

export default router