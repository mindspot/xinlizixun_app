import md5 from 'js-md5'
const salt = '1a2b3c4d'
export function genPassword(password) {
  var str = '' + salt.charAt(0) + salt.charAt(3) + password + salt.charAt(2) + salt.charAt(5)
  return md5(str)
}
export function getBirthdayFromCard(card) {
  var year, month, day
  if (card.length === 15) {
    year = card.substr(6, 2)
    month = card.substr(8, 2)
    day = card.substr(10, 2)
  } else if (card.length === 18) {
    year = card.substr(6, 4)
    month = card.substr(10, 2)
    day = card.substr(12, 2)
  }
  return year + '-' + month + '-' + day
}

export function getGenderFromCard(card) {
  var sex
  if (card.length === 15) {
    sex = card.substr(14, 1)
  } else if (card.length === 18) {
    sex = card.substr(16, 1)
  }
  if (sex % 2 === 0) { return 0 }
  return 1
}

export function markStar(data, fromIndex, length) {
  fromIndex = fromIndex || -2
  length = length || 3
  if (data.length > length) {
    var newData = ''
    var startIndex = 0
    var endIndex = data.length - 1
    if (fromIndex < 0) {
      startIndex = data.length + fromIndex - length + 1
      endIndex = data.length + fromIndex
    } else if (fromIndex >= 0) {
      startIndex = fromIndex - 1
      endIndex = fromIndex + length - 2
    }
    for (var i = 0; i < data.length; i++) {
      if (i >= startIndex && i <= endIndex) {
        newData = newData + '*'
      } else {
        newData = newData + data[i]
      }
    }
    return newData
  }
  return data
}

export function deepClone(obj) {
  if (obj !== null) {
    var newObj = obj.constructor === Array ? [] : {}
    if (typeof obj !== 'object') {
      return
    } else {
      for (var i in obj) {
        if (obj.hasOwnProperty(i)) {
          newObj[i] = typeof obj[i] === 'object' ? deepClone(obj[i]) : obj[i]
        }
      }
    }
    return newObj
  }
}
export function getDateStr(AddDayCount) {
  var dd = new Date()
  dd.setDate(dd.getDate() + AddDayCount)// 获取AddDayCount天后的日期
  var y = dd.getFullYear()
  var m = dd.getMonth() + 1// 获取当前月份的日期
  var d = dd.getDate()
  return y + '-' + (m < 10 ? '0' + m : m) + '-' + d
}

export function getHour() {
  var dd = new Date()
  return dd.getHours()
}
