'use strict';

var Processor = require('./src/processor');

function scssToJson(path, options) {
  var processor = new Processor(path, options);
  return processor.object;
}

module.exports = scssToJson;
