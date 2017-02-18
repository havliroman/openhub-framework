import objectPath from 'object-path'
import axios from 'axios'

// ------------------------------------
// Constants
// ------------------------------------
export const GET_OPENHUB_INFO = 'GET_OPENHUB_INFO'
export const GET_HEALTH_INFO = 'GET_HEALTH_INFO'

// ------------------------------------
// Actions
// ------------------------------------

export const getHealthInfo = () => {
  const payload = axios.get('/web/admin/mgmt/health')
  return {
    type: GET_HEALTH_INFO,
    payload
  }
}

export const getOpenHubInfo = () => {
  const payload = axios.get('/web/admin/mgmt/info')
  return {
    type: GET_OPENHUB_INFO,
    payload
  }
}

export const actions = {
  getHealthInfo
}

// ------------------------------------
// Action Handlers
// ------------------------------------
const ACTION_HANDLERS = {
  [GET_HEALTH_INFO]: (state, { payload }) => ({ ...state, healthInfo: payload.data }),
  [GET_OPENHUB_INFO]: (state, { payload }) => ({ ...state, openHubInfo: objectPath.get(payload, 'data.app') })
}

// ------------------------------------
// Reducer
// ------------------------------------
const initialState = {
  healthInfo: null,
  openHubInfo: null
}

export default function (state = initialState, action) {
  const handler = ACTION_HANDLERS[action.type]
  return handler ? handler(state, action) : state
}
