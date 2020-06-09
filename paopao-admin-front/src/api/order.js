import request from '@/utils/request'

export function orderList(data) {
  return request({
    url: '/web/order/orderList',
    method: 'post',
    data
  })
}
export function setMealOrderList(data) {
  return request({
    url: '/web/order/orderList',
    method: 'post',
    data
  })
}

export function consultantSupervisorOrder(data) {
  return request({
    url: '/web/order/supervisorOrderList',
    method: 'post',
    data
  })
}
export function setMealOrderInfo(data) {
  return request({
    url: '/web/order/setMealOrderInfo',
    method: 'post',
    params: {
      id: data.id
    }
  })
}

export function orderInfo(data) {
  return request({
    url: '/web/order/orderInfo',
    method: 'post',
    params: {
      id: data.id
    }
  })
}
export function diagnosis(orderNo) {
  return request({
    url: '/web/order/diagnosis',
    method: 'post',
    params: {
      orderNo: orderNo
    }
  })
}
export function diagnos(orderNo) {
  return request({
    url: '/web/order/diagnos',
    method: 'post',
    params: {
      orderNo: orderNo
    }
  })
}

export function setMelaOrderByOrderNo(orderNo) {
  return request({
    url: '/web/order/setMeal-orderNo',
    method: 'post',
    params: {
      orderNo: orderNo
    }
  })
}
export function setMelaOrderByOrderNoDetails(orderNo) {
  return request({
    url: '/web/order/setMeal-orderNo-details',
    method: 'post',
    params: {
      orderNo: orderNo
    }
  })
}

export function memberCaseByOrderNo(orderNo) {
  return request({
    url: '/web/order/member-cases-orderNo',
    method: 'post',
    params: {
      orderNo: orderNo
    }
  })
}
export function memberRefund(orderNo) {
  return request({
    url: '/web/order/member-refund',
    method: 'get',
    params: {
      orderNo: orderNo
    }
  })
}
export function updateCardNumber(orderNo) {
  return request({
    url: '/web/order/member—update-cardNumber',
    method: 'get',
    params: {
      orderNo: orderNo
    }
  })
}

export function orderCaseClass(orderNo) {
  return request({
    url: '/web/order/case-class',
    method: 'get',
    params: {
      orderNo: orderNo
    }
  })
}