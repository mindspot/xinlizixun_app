/**
 * Created by PanJiaChen on 16/11/18.
 */

/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUsername(str) {
  const valid_map = ['admin', 'editor']
  return valid_map.indexOf(str.trim()) >= 0
}

export function isValidMobile(str) {
  var regex = /^[1]([3-9])[0-9]{9}$/
  return regex.test(str)
}
/**
   * @param {string} email
   * @returns {Boolean}
   */
export function isValidEmail(email) {
  const reg = /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/
  return reg.test(email)
}
export function isValidAge(str) {
  var regex = /^([1-9]\d?|1[01]\d|120)$/
  return regex.test(str)
}
export function isValidUrl(str) {
  var regex = /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/
  return regex.test(str)
}
export function isValidWeiXin(str) {
  var regex = /^[a-zA-Z]{1}[-_a-zA-Z0-9]{5,20}$/
  return regex.test(str)
}
export function isValidUserName(str) {
  var regex = /^[a-zA-Z0-9]+\s?[.·\-()a-zA-Z]*[a-zA-Z]+$/
  return regex.test(str)
}
export function isPureChar(str) {
  var regex = /^[^\u4e00-\u9fa5]{0,}$/
  return regex.test(str)
}
export function isValidNumber(str) {
  var regex = /^([1-9]\d*|1[01]\d)$/
  return regex.test(str)
}
