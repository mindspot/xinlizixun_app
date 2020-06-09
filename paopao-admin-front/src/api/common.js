import request from '@/utils/request'
export function commonList(data) {
  return request({
    url: '/web/common/list',
    method: 'post',
    data
  })
}
export function commonContentList(data) {
  return request({
    url: '/web/common/content/list',
    method: 'post',
    data
  })
}
export function addCommon(data) {
  return request({
    url: '/web/common/addCommon',
    method: 'post',
    data
  })
}
export function deleteCommon(id) {
  return request({
    url: '/web/common/delete',
    method: 'post',
    params: {
      id: id
    }
  })
}

export function updateCommon(data) {
  return request({
    url: '/web/common/updateCommon',
    method: 'post',
    data
  })
}
export function platformWorkingTimeList(data) {
  return request({
    url: '/web/platformWorkingTime/list',
    method: 'post',
    data
  })
}

export function updatePlatformWorkingTime(data) {
  return request({
    url: '/web/platformWorkingTime/update',
    method: 'post',
    data
  })
}
