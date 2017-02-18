import React, { Component, PropTypes } from 'react'
import Radium from 'radium'
import styles from './home.styles'
import Panel from '../../../common/components/Panel/Panel'
import Table from '../../../common/components/Table/Table'
import Status from '../../../common/components/Status/Status'

@Radium
class Home extends Component {

  componentDidMount () {
    const { actions: { getHealthInfo } } = this.props
    getHealthInfo()
  }

  render () {
    const { isAuth, dashboard: { healthInfo, openHubInfo } } = this.props
    const isUp = (val) => val === 'UP'

    const appTableData = openHubInfo && [
      ['Name', openHubInfo.name],
      ['Version', openHubInfo.version]
    ]

    const healthTableData = healthInfo && [
      ['Application', <Status status={isUp(healthInfo.status)} />],
      ['Datasources', <Status status={isUp(healthInfo.db.status)} />],
      ['Database', healthInfo.db.database]
    ]

    const diskTableData = healthInfo && [
      ['Status', <Status status={isUp(healthInfo.diskSpace.status)} />],
      ['Free space', healthInfo.diskSpace.free],
      ['Total space', healthInfo.diskSpace.total],
      ['Threshold', healthInfo.diskSpace.threshold]
    ]

    return (
      <div style={styles.main}>
        <div style={styles.widgets}>
          <Panel title='Application' >
            <Table data={appTableData} />
          </Panel>
          {isAuth &&
            <Panel title='Health'>
              <Table data={healthTableData} />
            </Panel>
          }
          {isAuth &&
            <Panel title='Disk'>
              <Table data={diskTableData} />
            </Panel>
          }
        </div>
      </div>
    )
  }
}

Home.propTypes = {
  isAuth: PropTypes.bool,
  actions: PropTypes.object.isRequired,
  dashboard: PropTypes.object.isRequired
}

export default Home
