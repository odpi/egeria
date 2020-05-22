/* eslint-env node, mocha */
'use strict';

var assert = require('assert');
var path = require('path');
var spawn = require('cross-spawn');
var cli = path.join(__dirname, '../bin/scss-to-json');
var PKG_VERSION = require('../package.json').version;

function callAndExpectNoErrors(args, cb) {
  var bin = spawn(cli, args);
  bin.stdout.setEncoding('utf8');
  bin.stderr.setEncoding('utf8');

  bin.stderr.once('data', function(data) {
    throw new Error(data);
  });

  bin.stdout.once('data', cb);
}

describe('cli', function() {
  it('returns help', function(done) {
    callAndExpectNoErrors(['-h'], function(data) {
      assert.equal(data.indexOf('Usage:'), 0);
      done();
    });
  });

  it('returns version number', function(done) {
    callAndExpectNoErrors(['-v'], function(data) {
      assert.equal(data.trim(), PKG_VERSION);
      done();
    });
  });

  it('compiles', function(done) {
    var file = path.resolve(__dirname, 'scss', 'test.scss');
    callAndExpectNoErrors([file], function(data) {
      assert.ok(data.indexOf('$icon-font-size-lg') >= 0);
      done();
    });
  });

  it('compiles with scoping', function(done) {
    var file = path.resolve(__dirname, 'scss', 'scoped.scss');
    callAndExpectNoErrors([file, '--scope', '%scoped'], function(data) {
      assert.ok(data.indexOf('$global-with-function') >= 0);
      done();
    });
  });

  it('compiles with dependencies', function(done) {
    var file = path.resolve(__dirname, 'scss', 'has-dependents.scss');
    var dependency = path.resolve(__dirname, 'scss', 'dependency.scss');
    var expectedLines = [
      '{',
      '  "$first": "#00f",',
      '  "$global-variable": "#f00",',
      '  "$references": "#00f",',
      '  "$scss-function-with-variable": "#e60000"',
      '}'
    ];

    callAndExpectNoErrors([file, '--dependencies', dependency], function(data) {
      var lines = data.trim().replace(/\r\n/g, '\n').split('\n');
      assert.equal(lines.length, expectedLines.length);
      lines.forEach(function(l, i) {
        assert.equal(l, expectedLines[i]);
      });
      done();
    });
  });
});
