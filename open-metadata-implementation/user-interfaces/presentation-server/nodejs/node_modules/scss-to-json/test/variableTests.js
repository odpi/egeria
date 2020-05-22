'use strict';

var assert = require('assert');
var sinon = require('sinon');
var proxyquire =  require('proxyquire');

describe('Variable', function() {
  var scssString;
  var Variable;
  var utilities;

  beforeEach(function() {
    scssString = '$test ';

    utilities = {
      stripSpaces: sinon.spy(function(input) { return input; })
    };

    Variable = proxyquire('../src/variable', {
      './utilities': utilities
    });
  });

  describe('Constructor', function() {
    it('should call _parse with the given scssString', function() {
      var parseStub = sinon.stub(Variable.prototype, '_parse');

      new Variable(scssString);

      assert.ok(parseStub.calledOnce);
      assert.ok(parseStub.calledWith(scssString));
    });
  });

  describe('#_parse', function() {
    it('assigns the value and calls the correct transforms', function() {
      var variable = new Variable(scssString);

      assert.ok(utilities.stripSpaces.calledWith(scssString));
      assert.strictEqual(variable.value, scssString);
    });
  });
});
