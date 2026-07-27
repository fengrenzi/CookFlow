import axios from 'axios'
import { ElMessage } from 'element-plus'

const service = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API,
  timeout: 15000
});

// 请求拦截器
service.interceptors.request.use(
  config => {
    // const token = localStorage.getItem('token')
    // if (token) {
    //   config.headers['Authorization'] = 'Bearer ' + token
    // }
    return config
  },
  error => Promise.reject(error)
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    if (response.data) {
      const res = response.data
      if (res.code === undefined) {
        return res
      }
      if (res.code === 200) {
        if (res.total) return res
        if (res.data) return res.data
        if (res.rows) return res.rows
        else return res
      } else if (res.code == 0) {
        return res
      } else {
        ElMessage.error(res.msg || '请求失败')
        return Promise.reject(new Error(res.msg || 'Error'))
      }
    } else {
      return response
    }
  },
  error => {
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default service