import request from '@/utils/request'

export function proposalList(data) {
  return request({
    url: '/web/proposal/list',
    method: 'post',
    data
  })
}
export function proposalDelete(id) {
  return request({
    url: '/web/proposal/delete',
    method: 'post',
    params: {
      id: id
    }
  })
}
