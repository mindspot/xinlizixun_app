import request from '@/utils/request'
export function listPublishBook(consultantId) {
  return request({
    url: '/v1/settled-in/list-publishBook',
    method: 'get',
    params: {
      consultantId: consultantId
    }
  })
}
export function listPublishPaper(consultantId) {
  return request({
    url: '/v1/settled-in/list-publishPaper',
    method: 'get',
    params: {
      consultantId: consultantId
    }
  })
}
export function addPublishMaterial(data) {
  return request({
    url: '/v1/settled-in/insert-publishMaterial',
    method: 'post',
    data
  })
}
export function deletePublishMaterial(publishMaterialId) {
  return request({
    url: '/v1/settled-in/delete-publishMaterial',
    method: 'post',
    params: {
      publishMaterialId: publishMaterialId
    }
  })
}
