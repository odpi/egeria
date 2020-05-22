'use strict';

var assert = require('assert');
var utilities = require('../src/utilities');

describe('Utilities', function() {
  describe('#stripNewLinesAndSemicolons', function() {
    it('should remove all new lines', function() {
      var input = '\nhello, I have\n some new lines\n and ;semicolons;';
      var output = 'hello, I have some new lines and semicolons';

      assert.strictEqual(utilities.stripNewLinesAndSemicolons(input), output);
    });
  });

  describe('#stripSpaces', function() {
    it('should remove all spaces', function() {
      var input = 'hello, I have some spaces';
      var output = 'hello,Ihavesomespaces';

      assert.strictEqual(utilities.stripSpaces(input), output);
    });
  });

  describe('#removeFlags', function() {
    it('should remove all flags', function() {
      var input = '!global hello, !default I have some spaces !global';
      var output = ' hello,  I have some spaces ';

      assert.strictEqual(utilities.removeFlags(input), output);
    });
  });

  describe('#removeInlineComments', function() {
    it('should remove inline comment if it is the entire line', function() {
      var input = '// this is a comment';
      var output = '';

      assert.strictEqual(utilities.removeInlineComments(input), output);
    });

    it('should remove inline comment if it is at the end of a line separated by a space', function() {
      var input = '1px solid blue // this is a comment';
      var output = '1px solid blue ';

      assert.strictEqual(utilities.removeInlineComments(input), output);
    });

    it('should remove inline comment if it is at the end of a line not separated by a space', function() {
      var input = '1px solid blue// this is a comment';
      var output = '1px solid blue';

      assert.strictEqual(utilities.removeInlineComments(input), output);
    });
  });
});
