import request from '@/utils/request'
export function listTrainingExperience(consultantId) {
  return request({
    url: '/v1/settled-in/list-training',
    method: 'get',
    params: {
      consultantId: consultantId
    }
  })
}
export function addTrainingExperience(data) {
  return request({
    url: '/v1/settled-in/insert-training',
    method: 'post',
    data
  })
}
export function deleteTrainingExperience(trainingExperienceId) {
  return request({
    url: '/v1/settled-in/delete-training',
    method: 'post',
    params: {
      trainingExperienceId: trainingExperienceId
    }
  })
}
