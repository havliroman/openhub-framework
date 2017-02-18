const { runServer } = require('express-mock-server');

const opt_serverConfig = {
  port: 8080
}

const sources = [
  require('./info/info.mock'),
  require('./health/health.mock')
]

runServer(sources, opt_serverConfig)
