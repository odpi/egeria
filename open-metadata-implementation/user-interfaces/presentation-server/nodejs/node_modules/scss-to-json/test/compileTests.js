'use strict';

var assert = require('assert');
var proxyquire = require('proxyquire');
var sinon = require('sinon');

describe('Compile', function() {
  var correctlyWrapped;
  var minifiedWrapped;
  var nodeSassStub;
  var cssminStub;
  var Compile;
  var scssString;

  beforeEach(function() {
    scssString = '1px solid blue';
    correctlyWrapped = '.test { content: ' + scssString + ' };';
    minifiedWrapped = '.test{content:' + scssString + '}';
    nodeSassStub = {
      renderSync: sinon.stub().returns({ css: correctlyWrapped })
    };
    cssminStub = sinon.stub().returns(minifiedWrapped);

    Compile = proxyquire('../src/compile', {
      'node-sass': nodeSassStub,
      'cssmin': cssminStub
    });
  });

  describe('#wrapValue', function() {
    it('should return the given CSS value wrapped in a sample declaration', function() {
      var wrapped = Compile.wrapValue(scssString);

      assert.strictEqual(wrapped, correctlyWrapped);
    });
  });

  describe('#unwrapValue', function() {
    it('should return the CSS value unwrapped from its sample declaration', function() {
      var unwrapped = Compile.unwrapValue(minifiedWrapped);

      assert.strictEqual(unwrapped, scssString);
    });
  });

  describe('#fromString', function() {
    it('returns the compiled scss given to it as an argument', function() {
      var compiled = Compile.fromString(scssString);

      assert.ok(nodeSassStub.renderSync.calledWith({ data: correctlyWrapped }));
      assert.ok(cssminStub.calledWith(correctlyWrapped));
      assert.strictEqual(compiled, scssString);
    });
  });
});
