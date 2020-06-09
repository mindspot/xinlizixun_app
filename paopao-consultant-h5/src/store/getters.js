const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  consultantId: state => state.user.consultantId,
  name: state => state.user.name
}
export default getters
