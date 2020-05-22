'use strict';

var assert = require('assert');
var sinon = require('sinon');
var proxyquire =  require('proxyquire');

describe('Value', function() {
  var scssString;
  var Value;
  var compile;
  var utilities;

  beforeEach(function() {
    scssString = ' blue !global // with a comment';

    compile = {
      fromString: sinon.spy(function(input) { return input; })
    };

    utilities = {
      removeFlags: sinon.spy(function(input) { return input; }),
      removeInlineComments: sinon.spy(function(input) { return input; })
    };

    Value = proxyquire('../src/value', {
      './compile': compile,
      './utilities': utilities
    });
  });

  describe('Constructor', function() {
    it('should call _parse with the given scssString', function() {
      var parseStub = sinon.stub(Value.prototype, '_parse');

      new Value(scssString);

      assert.ok(parseStub.calledOnce);
      assert.ok(parseStub.calledWith(scssString));
    });
  });

  describe('#_parse', function() {
    it('assigns the value and calls the correct transforms', function() {
      var value = new Value(scssString);

      assert.ok(utilities.removeFlags.calledWith(scssString));
      assert.ok(utilities.removeFlags.calledWith(scssString));
      assert.ok(compile.fromString.calledWith(scssString));
      assert.strictEqual(value.value, scssString.trim());
    });
  });
});
