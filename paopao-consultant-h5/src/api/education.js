import request from '@/utils/request'
export function listEducation(consultantId) {
  return request({
    url: '/v1/settled-in/list-education',
    method: 'get',
    params: {
      consultantId: consultantId
    }
  })
}
export function addEducation(data) {
  return request({
    url: '/v1/settled-in/insert-education',
    method: 'post',
    data
  })
}
export function deleteEducation(educationExperienceId) {
  return request({
    url: '/v1/settled-in/delete-education',
    method: 'post',
    params: {
      educationExperienceId: educationExperienceId
    }
  })
}
