import request from '@/utils/request'
export function listConsultantResult(data) {
  return request({
    url: '/web/consultant/list-result',
    method: 'post',
    data
  })
}
export function listConsultant(data) {
  return request({
    url: '/web/consultant/list',
    method: 'post',
    data
  })
}

export function getConsultantInfo(id) {
  return request({
    url: '/web/consultant/consultantInfo',
    method: 'post',
    params: {
      id: id
    }
  })
}

export function addConsultant(data) {
  return request({
    url: '/consultant/add',
    method: 'post',
    data
  })
}

export function deleteConsultant(ids) {
  return request({
    url: '/consultant/delete',
    method: 'get',
    params: {
      ids: ids
    }
  })
}

export function updateConsultantResult(data) {
  return request({
    url: '/web/consultant/update-result',
    method: 'post',
    data
  })
}
export function updateConsultant(data) {
  return request({
    url: '/web/consultant/update',
    method: 'post',
    data
  })
}
export function updateStatus(id, status) {
  return request({
    url: '/web/consultant/updateStatus',
    method: 'post',
    params: {
      id: id,
      status: status
    }
  })
}
export function updateSupervisor(id, code) {
  return request({
    url: '/web/supervisor/set',
    method: 'post',
    params: {
      id: id,
      invitationCode: code
    }
  })
}

export function updateCancel(id) {
  return request({
    url: '/web/supervisor/cancel',
    method: 'post',
    params: {
      id: id
    }
  })
}
export function supervisorChange(ids, code) {
  return request({
    url: '/web/supervisor/change',
    method: 'post',
    params: {
      consultantIds: ids,
      invitationCode: code
    }
  })
}
export function supervisorCodeList() {
  return request({
    url: '/web/supervisor/code',
    method: 'post'
  })
}
export function updateTitel(data) {
  return request({
    url: '/web/consultant/titel',
    method: 'post',
    data
  })
}
