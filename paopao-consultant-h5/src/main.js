import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import '@/styles/index.scss' // global css
import App from './App'
import store from './store'
import router from './router'
import { Picker, Popup, Cell, Field, Button, DatetimePicker } from 'vant'
import { Toast } from 'vant'
import { Checkbox, CheckboxGroup } from 'vant'
import { Col, Row } from 'vant'
import { RadioGroup, Radio } from 'vant'
import { Divider } from 'vant'
import { Notify } from 'vant'
import { Area } from 'vant'
import { Dialog } from 'vant'
import { Overlay } from 'vant'
import Video from 'video.js'
import 'video.js/dist/video-js.css'

Vue.prototype.$video = Video
Vue.use(Overlay)
// 全局注册
Vue.use(Dialog)
Vue.use(Area)
Vue.use(Notify)
Vue.use(Divider)
Vue.use(Radio)
Vue.use(RadioGroup)
Vue.use(Col)
Vue.use(Row)
Vue.use(Checkbox)
Vue.use(CheckboxGroup)
Vue.use(Toast)
Vue.use(Picker)
Vue.use(Popup)
Vue.use(Cell)
Vue.use(Field)
Vue.use(Button)
Vue.use(DatetimePicker)
Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
