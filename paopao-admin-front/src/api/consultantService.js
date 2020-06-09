import request from '@/utils/request'
export function consultantServiceList(data) {
  return request({
    url: '/web/consultantService/list',
    method: 'post',
    data
  })
}
export function consultantServiceInfo(data) {
  return request({
    url: '/web/consultantService/info',
    method: 'post',
    params: {
      shopId: data.shopId
    }
  })
}

export function addConsultantService(data) {
  return request({
    url: '/web/consultantService/add',
    method: 'post',
    params: {
      shopId: data.shopId
    }
  })
}

export function deleteConsultantService(data) {
  return request({
    url: '/web/consultantService/delete',
    method: 'post',
    params: {
      shopId: data.shopId
    }
  })
}
