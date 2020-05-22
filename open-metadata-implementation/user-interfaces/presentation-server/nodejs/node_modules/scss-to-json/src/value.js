'use strict';

var compile = require('./compile');
var utilities = require('./utilities');

function transforms(value) {
  return utilities.removeInlineComments(utilities.removeFlags(value));
}

function Value(scssString) {
  this._parse(scssString);
}

Value.prototype = {
  _parse: function(scssString) {
    var transformed = transforms(scssString);
    var compiled = compile.fromString(transformed);
    this.value = compiled.trim();
  }
};

module.exports = Value;
