import request from '@/utils/request'
export function couponList(data) {
  return request({
    url: '/web/coupon/list',
    method: 'post',
    data
  })
}

export function addCoupon(data) {
  return request({
    url: '/web/coupon/add',
    method: 'post',
    data
  })
}

export function updateCoupon(data) {
  return request({
    url: '/web/coupon/updateCoupon',
    method: 'post',
    data
  })
}
export function redeemCoupon(templateId, redeemCode) {
  return request({
    url: '/web/coupon/redeem/' + templateId,
    method: 'get',
    params: {
      redeemCode: redeemCode
    }
  })
}

