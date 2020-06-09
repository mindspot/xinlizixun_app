import request from '@/utils/request'

export function listMember(data) {
  return request({
    url: '/member/list',
    method: 'post',
    data
  })
}
export function addMember(data) {
  return request({
    url: '/web/member/add',
    method: 'post',
    data
  })
}

export function deleteMember(ids) {
  return request({
    url: '/member/delete',
    method: 'get',
    params: {
      ids: ids
    }
  })
}

export function updateMember(data) {
  return request({
    url: '/member/update',
    method: 'post',
    data
  })
}
