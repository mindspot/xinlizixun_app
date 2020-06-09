import request from '@/utils/request'

export function userCashWithdrawalList(data) {
  return request({
    url: '/web/userCashWithdrawal/list',
    method: 'post',
    data
  })
}
export function cashWithdrawalUpdate(data) {
  return request({
    url: '/web/userCashWithdrawal/updateCashType',
    method: 'post',
    params: {
      id: data.id,
      cashType: data.cashType,
      userId: data.userId
    }
  })
}
