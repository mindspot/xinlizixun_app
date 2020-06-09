import request from '@/utils/request'
export function addCouponTemplate(data) {
  return request({
    url: '/web/coupon-template/add',
    method: 'post',
    data
  })
}
export function updateCouponTemplate(data) {
  return request({
    url: '/web/coupon-template/update',
    method: 'post',
    data
  })
}
export function listCouponTemplate(data) {
  return request({
    url: '/web/coupon-template/list',
    method: 'post',
    data
  })
}
export function queryCouponTemplate(data) {
  return request({
    url: '/web/coupon-template/query',
    method: 'post',
    data
  })
}
export function generateCoupon(templateId) {
  return request({
    url: '/web/coupon-template/generate',
    method: 'get',
    params: {
      templateId: templateId
    }
  })
}
export function collectCoupon(templateId) {
  return request({
    url: '/web/coupon-template/collect/' + templateId,
    method: 'get'
  })
}
