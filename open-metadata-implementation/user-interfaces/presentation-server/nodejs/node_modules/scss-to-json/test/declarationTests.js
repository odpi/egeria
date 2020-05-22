'use strict';

var assert = require('assert');
var proxyquire = require('proxyquire');
var sinon = require('sinon');

describe('Declaration', function() {
  var Declaration;
  var ValueStub;
  var VariableStub;
  var declarationStoreStub;
  var scssString;
  var globalScssString;

  beforeEach(function() {
    ValueStub = function(value) {
      this.value = value;
      this.isValue = true;
    };

    VariableStub = function(value) {
      this.value = value;
      this.isVariable = true;
    };

    declarationStoreStub = {
      replaceVariables: function(value) { return value; },
      addDeclaration: function() {}
    };

    Declaration = proxyquire('../src/declaration', {
      './value': ValueStub,
      './variable': VariableStub
    });

    scssString = '$test:1px solid blue;';
    globalScssString = '$test:1px solid blue !global;';
  });

  describe('Constructor', function() {
    it('should call parse with the given string', function() {
      var parseStub = sinon.stub(Declaration.prototype, '_parse');
      new Declaration(scssString, declarationStoreStub);

      assert.ok(parseStub.calledWith(scssString, declarationStoreStub));
    });
  });

  describe('#_parse', function() {
    var declaration;

    beforeEach(function() {
      sinon.stub(Declaration.prototype, '_parse');
      declaration = new Declaration(scssString, declarationStoreStub);
      sinon.restore(Declaration.prototype, '_parse');
    });

    it('should assign this.variable with a variable object with the correct value', function() {
      declaration._parse(scssString, declarationStoreStub);

      assert.ok(declaration.variable.isVariable);
      assert.strictEqual(declaration.variable.value, '$test');
    });

    it('should replace variables and assign this.value with a value object with the correct value', function() {
      var replaceVariablesStub = sinon.stub(declarationStoreStub, 'replaceVariables').returns('replaced!');

      declaration._parse(scssString, declarationStoreStub);

      assert.ok(replaceVariablesStub.calledOnce);
      assert.ok(replaceVariablesStub.calledWith('1px solid blue;'));
      assert.ok(declaration.value.isValue);
      assert.strictEqual(declaration.value.value, 'replaced!');
    });

    it('should assign this.global to be true if it has the !global flag', function() {
      declaration._parse(globalScssString, declarationStoreStub);

      assert.strictEqual(declaration.global, true);
    });

    it('should assign this.global to be false if it does not have the !global flag', function() {
      declaration._parse(scssString, declarationStoreStub);

      assert.strictEqual(declaration.global, false);
    });

    it('should add the declaration to the store', function() {
      var addDeclarationStub = sinon.stub(declarationStoreStub, 'addDeclaration');

      declaration._parse(scssString, declarationStoreStub);

      assert.ok(addDeclarationStub.calledOnce);
      assert.ok(addDeclarationStub.calledWith(declaration));
    });
  });
});
