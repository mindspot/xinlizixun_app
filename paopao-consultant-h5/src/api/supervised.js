import request from '@/utils/request'
export function listSupervised(consultantId) {
  return request({
    url: '/v1/settled-in/list-supervised',
    method: 'get',
    params: {
      consultantId: consultantId
    }
  })
}
export function addSupervised(data) {
  return request({
    url: '/v1/settled-in/insert-supervised',
    method: 'post',
    data
  })
}
export function deleteSupervised(supervisedExperienceId) {
  return request({
    url: '/v1/settled-in/delete-supervised',
    method: 'post',
    params: {
      educationExperienceId: supervisedExperienceId
    }
  })
}
