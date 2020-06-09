import request from '@/utils/request'
export function dispatchList(data) {
  return request({
    url: '/web/dispatch/list',
    method: 'post',
    data
  })
}

export function addDispatch(data) {
  return request({
    url: '/web/dispatch/add',
    method: 'post',
    data
  })
}
