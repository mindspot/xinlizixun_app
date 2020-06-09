import request from '@/utils/request'
export function listActivities(data) {
  return request({
    url: '/promotional-activities/list',
    method: 'post',
    data
  })
}

export function addActivities(data) {
  return request({
    url: '/promotional-activities/add',
    method: 'post',
    data
  })
}

export function updateActivities(data) {
  return request({
    url: '/promotional-activities/update',
    method: 'post',
    data
  })
}
