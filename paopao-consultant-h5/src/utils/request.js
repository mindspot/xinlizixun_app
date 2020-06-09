import axios from 'axios'
import store from '../store'
import { getToken } from '@/utils/token'
// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // api的base_url
  timeout: 30000 // 请求超时时间
})

// request拦截器
service.interceptors.request.use(config => {
  // if (!store.getters.token) {
  //   config.headers['authorization'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
  // }
  config.headers['authorization'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
  config.headers['System-Version'] = '1.0.1'
  return config
}, error => {
  // Do something with request error
  console.log(error) // for debug
  Promise.reject(error)
})

// respone拦截器
service.interceptors.response.use(
  response => {
  /**
  * code为非20000是抛错 可结合自己业务进行修改
  */
    const res = response.data
    console.log(res)
    if (res.code === 0 || res.code <= 0) {
      return response.data
    }
    if (res.code === 50013) {
      // MessageBox.alert('系统版本不正确,请刷新系统', '警告信息', {
      //   confirmButtonText: '确定刷新',
      //   callback: action => {
      //     location.reload()// 为了重新实例化vue-router对象 避免bug
      //   }
      // })
      return Promise.reject('error')
    }
    if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
      // 50008:非法的token; 50012:其他客户端登录了;  50014:Token 过期了;
      return Promise.reject('error')
    } else {
      return response.data
    }
  },
  error => {
    console.log('err' + error)// for debug
    // Message({
    //   message: error.message,
    //   type: 'error',
    //   duration: 5 * 1000
    // })
    return Promise.reject(error)
  }
)

export default service
