export function formatIsEnabled(row) {
  if (row.isEnabled === 1) {
    return '是'
  }
  return '否'
}

export function formatNumber(value) {
  if (!value) {
    return '0'
  } else {
    value = Number(value)
    return Number((value / 100).toFixed(2))
  }
}

export function formatAndTransferNumber(value) {
  if (!value) {
    return 0
  } else {
    value = Number(value)
    return value
  }
}

export function formatNumeric(value) {
  try {
    var f = parseFloat(value)
    var fd = f.toFixed(2) + ''
    return Number(fd.replace('.00', ''))
  } catch (e) {
    return ''
  }
}

export function fillZeroWithBank(value) {
  value = Number(value)
  if (value === 0) {
    return ''
  } else {
    return value.toFixed(2)
  }
}
export function formatMoney(num) {
  if (!num) {
    return num
  }
  return (num + '').replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g, '$1,')
}
