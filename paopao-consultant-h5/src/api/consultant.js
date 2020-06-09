import request from '@/utils/request'
export function settledInLoginGetCode(phone) {
  return request({
    url: '/settledInLogin/get/code',
    method: 'post',
    params: {
      phone: phone
    }
  })
}
export function settledInLogin(phone, code) {
  return request({
    url: '/settled-in/login',
    method: 'post',
    params: {
      phone: phone,
      code: code
    }
  })
}
export function getConsultantInfo() {
  return request({
    url: '/v1/settled-in/get-info',
    method: 'get'
  })
}
export function settledIninserConsultant(data) {
  return request({
    url: '/v1/settled-in/insert-consultant',
    method: 'post',
    data
  })
}
export function insertConsultantExtraInfo(data) {
  return request({
    url: '/v1/settled-in/insert-consultant-extra-info',
    method: 'post',
    data
  })
}

export function settledIninsertWord(data) {
  return request({
    url: '/v1/settled-in/insert-work',
    method: 'post',
    data
  })
}
export function settledInUpdateWord(data) {
  return request({
    url: '/v1/settled-in/update-work',
    method: 'post',
    data
  })
}
export function listWorkingTime(data) {
  return request({
    url: '/v1/settled-in/list-working-time',
    method: 'get',
    data
  })
}
export function register(phone, code, spreadUrl) {
  return request({
    url: '/user/phoneLogin',
    method: 'post',
    params: {
      phone: phone,
      code: code,
      spreadUrl: spreadUrl
    }
  })
}

export function registerGetCode(data) {
  console.log(data)
  return request({
    url: '/get/code',
    method: 'post',
    params: {
      phone: data
    }
  })
}
