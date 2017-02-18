import { sidebarWidth } from '../../../common/components/Sidebar/sidebar.styles'
import { dev, transition } from '../../../styles/constants'

export default {
  main: {
    position: 'absolute',
    height: '100%',
    width: '100%',
    ...dev
  },
  body: {
    ...transition(),
    ...dev,
    position: 'relative',
    width: '100%',
    height: '100%',
    boxSizing: 'border-box',
    extended: {
      paddingLeft: `${sidebarWidth}px`
    }
  }
}

