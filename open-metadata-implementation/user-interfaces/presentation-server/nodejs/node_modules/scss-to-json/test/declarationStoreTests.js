'use strict';

var assert = require('assert');
var DeclarationStore = require('../src/declarationStore');

describe('DeclarationStore', function() {
  var sampleDeclaration;
  var secondSampleDeclaration;
  var thirdSampleDeclaration;
  var declarationStore;

  beforeEach(function() {
    sampleDeclaration = {
      variable: {
        value: '$test'
      },
      value: {
        value: '10px'
      }
    };
    secondSampleDeclaration = {
      variable: {
        value: '$second'
      },
      value: {
        value: 'blue'
      }
    };
    thirdSampleDeclaration = {
      variable: {
        value: '$gray-50'
      },
      value: {
        value: '#f5f5f5'
      }
    };

    declarationStore = new DeclarationStore();
  });

  describe('Constructor', function() {
    it('should set a store to an empty array', function() {
      assert.deepEqual(declarationStore.declarations, []);
    });
  });

  describe('#addDeclaration', function() {
    it('adds an item into the store', function() {
      assert.equal(declarationStore.declarations.length, 0);
      declarationStore.addDeclaration(sampleDeclaration);
      assert.equal(declarationStore.declarations.length, 1);
      assert.deepEqual(declarationStore.declarations[0], sampleDeclaration);
    });
  });

  describe('#replaceVariables', function() {
    beforeEach(function() {
      declarationStore.declarations = [sampleDeclaration, secondSampleDeclaration, thirdSampleDeclaration];
    });

    it('replaces variables in the given string with their value from the store if they exist in the store', function() {
      assert.strictEqual(declarationStore.replaceVariables('$test'), '10px');
      assert.strictEqual(declarationStore.replaceVariables('1px solid $second'), '1px solid blue');
    });

    it('replaces variables in the given string if that string is a function and the variables match from the store', function() {
      assert.strictEqual(declarationStore.replaceVariables('darken($second, 5%)'), 'darken(blue, 5%)');
    });

    it('does not replace variables if there is a subset of that variable in the store with a word character', function() {
      var scssString = '$gray-500';
      assert.strictEqual(declarationStore.replaceVariables(scssString), scssString);
    });

    it('does not replace variables if there is a subset of that variable in the store with an underscore', function() {
      var scssString = '$gray-50-large';
      assert.strictEqual(declarationStore.replaceVariables(scssString), scssString);
    });

    it('does not replace variables if there is a subset of that variable in the store with an underscore', function() {
      var scssString = '$gray-50_large';
      assert.strictEqual(declarationStore.replaceVariables(scssString), scssString);
    });

    it('returns the original string if the variable is not defined in the store', function() {
      var scssString = '1px solid $not-defined';
      assert.strictEqual(declarationStore.replaceVariables(scssString), scssString);
    });

    it('returns the original string if the store is empty', function() {
      declarationStore.declarations = [];
      var scssString = '1px solid $not-defined';
      assert.strictEqual(declarationStore.replaceVariables(scssString), scssString);
    });

    it('returns the original string if there is no variable defined in the given value', function() {
      var scssString = '1px solid red';
      assert.strictEqual(declarationStore.replaceVariables(scssString), scssString);
    });
  });
});
