
var assert = require('assert')

module.exports = function () {
  return function (req, res, next) {
    assert(req.session, 'a req.session is required!')
    if (!Array.isArray(req.session.flash)) req.session.flash = []
    res.locals.flash = req.session.flash
    req.flash = res.flash = push
    next()
  }
}

function push(type, msg) {
  if (!msg) {
    msg = type
    type = 'info'
  }
  msg = {
    message: msg,
    type: type
  }
  var res = this.res || this
  var messages = res.locals.flash
  // do not allow duplicate flash messages
  for (var i = 0; i < messages.length; i++) {
    var message = messages[i]
    if (msg.type === message.type && msg.message === message.message) return this
  }
  messages.push(msg)
  return this
}
