import request from '@/utils/request'
export function listCertification(consultantId) {
  return request({
    url: '/v1/settled-in/list-certification',
    method: 'get',
    params: {
      consultantId: consultantId
    }
  })
}
export function addCertification(data) {
  return request({
    url: '/v1/settled-in/insert-certification',
    method: 'post',
    data
  })
}
export function deleteCertification(certificationId) {
  return request({
    url: '/v1/settled-in/delete-certification',
    method: 'post',
    params: {
      certificationId: certificationId
    }
  })
}
