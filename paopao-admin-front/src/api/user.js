import request from '@/utils/request'

export function login(phone, pwd) {
  return request({
    url: '/user/pwdLogin',
    method: 'post',
    params: {
      phone: phone,
      pwd: pwd
    }
  })
}

export function listUser(data) {
  return request({
    url: '/web/user/list',
    method: 'post',
    data
  })
}
export function updateUserStatus(id, status) {
  return request({
    url: '/web/user/update-status',
    method: 'post',
    params: {
      id: id,
      status: status
    }
  })
}
export function getInfo(token) {
  return request({
    url: '/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}
