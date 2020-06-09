import request from '@/utils/request'
export function addArticleCategory(data) {
  return request({
    url: '/article/category/add',
    method: 'post',
    data
  })
}

export function listCategory(data) {
  return request({
    url: '/article/category/list',
    method: 'post',
    data
  })
}

export function listAllCategory() {
  return request({
    url: '/web/article/category/all',
    method: 'get'
  })
}

export function updateArticleCategory(data) {
  return request({
    url: '/article/category/update',
    method: 'post',
    data
  })
}

export function deleteArticleCategory(id) {
  return request({
    url: '/article/category/delete',
    method: 'post',
    params: {
      id: id
    }
  })
}
export function addArticle(data) {
  return request({
    url: '/web/article/add',
    method: 'post',
    data
  })
}

export function listArticle(data) {
  return request({
    url: '/web/article/list',
    method: 'post',
    data
  })
}

export function fetchArticle(id) {
  return request({
    url: '/web/article/get/' + id,
    method: 'get'
  })
}

export function deleteArticle(id) {
  return request({
    url: '/web/article/delete',
    method: 'post',
    params: { id }
  })
}
