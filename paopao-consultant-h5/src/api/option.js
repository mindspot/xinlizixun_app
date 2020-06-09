import request from '@/utils/request'
export function listCertificationType() {
  return request({
    url: '/v1/option/list-certification-type',
    method: 'get'
  })
}

export function listTargetPeople() {
  return request({
    url: '/v1/option/list-target-people',
    method: 'get'
  })
}
export function listExpertiseArea() {
  return request({
    url: '/v1/option/list-expertise-area',
    method: 'get'
  })
}
export function listPlatformWorkingTime() {
  return request({
    url: '/v1/option/list-platform-working-time',
    method: 'get'
  })
}
export function getArticle(id) {
  return request({
    url: '/web/article/get/' + id,
    method: 'get'
  })
}
