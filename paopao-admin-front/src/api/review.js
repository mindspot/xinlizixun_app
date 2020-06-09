import request from '@/utils/request'

export function addReviewRecord(data) {
  return request({
    url: '/review/add',
    method: 'post',
    data
  })
}

export function getLastReviewRecord(refId, refType) {
  return request({
    url: '/review/last-review-record',
    method: 'get',
    params: {
      refId: refId,
      refType: refType
    }
  })
}
