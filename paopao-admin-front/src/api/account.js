import request from '@/utils/request'
export function listPointHistory(data) {
  return request({
    url: '/point-history/list',
    method: 'post',
    data
  })
}
