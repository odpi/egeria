'use strict';

var utilities = require('./utilities');

function Variable(scssString) {
  this._parse(scssString);
}

Variable.prototype = {
  _parse: function(scssString) {
    this.value = utilities.stripSpaces(scssString);
  }
};

module.exports = Variable;
