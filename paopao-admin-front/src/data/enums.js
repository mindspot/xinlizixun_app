export const ReviewStatusEnum = [
  {
    value: 2,
    label: '通过'
  }, {
    value: 3,
    label: '驳回'
  }
]
export const OrderStatusEnum = [
  {
    value: -11,
    label: '交易关闭'
  }, {
    value: -10,
    label: '交易失败'
  }, {
    value: -2,
    label: '卖家取消'
  }, {
    value: -1,
    label: '买家取消'
  }, {
    value: 0,
    label: '创建成功'
  }, {
    value: 1,
    label: '已支付'
  }, {
    value: 2,
    label: '已确认'
  }, {
    value: 10,
    label: '交易完成'
  }
]
export const ReviewRefTypeEnum = [{
  type: 0,
  value: '咨询师'
},
{
  type: 1,
  value: '会员'
},
{
  type: 2,
  value: '管理员'
}
]
export function getReviewRefTypeEnum(type) {
  for (const val of ReviewRefTypeEnum) {
    if (val.type === type) {
      return val.value
    }
  }
}
