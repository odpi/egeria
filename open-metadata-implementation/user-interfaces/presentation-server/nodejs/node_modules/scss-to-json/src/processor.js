'use strict';

var fs = require('fs');
var Declaration = require('./declaration');
var DeclarationStore = require('./declarationStore');
var utilities = require('./utilities');

var LINE_DELIMITER = '\n';
var COMMENT_DELIMETER = '//';
var EMPTY_LINES = ['', '\n', '\s'];

function makeObject(declarations, options) {
  var output = {};

  declarations.forEach(function(declaration) {
    if (hasScope(options)) {
      if (declaration.global) {
        output[declaration.variable.value] = declaration.value.value;
      }
    } else {
      output[declaration.variable.value] = declaration.value.value;
    }
  });

  return output;
}

function filterLines(line) {
  return EMPTY_LINES.every(function(lineValue) {
    return line !== lineValue && line.slice(0, 2) !== COMMENT_DELIMETER;
  });
}

function getScopeIndices(data, scope) {
  var startIndex;
  var endIndex;
  var regex = new RegExp('\\' + scope + '.*\{', 'g');
  var match = data.match(regex);

  if (match) {
    for (var i = data.indexOf(match[0]); i < data.length; i++) {
      if (data[i] === '{') {
        startIndex = i;
      } else if (data[i] === '}') {
        endIndex = i;
        break;
      }
    }
  }

  return {
    start: startIndex,
    end: endIndex
  };
}

function extractScope(data, scope) {
  var extractedScope = data;
  var scopeIndices = getScopeIndices(data, scope);

  if (scopeIndices.start && scopeIndices.end) {
    extractedScope = extractedScope.substring(scopeIndices.start + 1, scopeIndices.end - 1);
  }

  return extractedScope;
}

function hasScope(options) {
  return options && options.scope && typeof options.scope === 'string';
}

function hasDependencies(options) {
  return options && options.dependencies && options.dependencies.length > 0;
}

function normalizeLines(line) {
  var stripped = utilities.stripNewLinesAndSemicolons(line);
  return stripped.trim();
}

function declarationsFromString(path, declarationStore, options) {
  var data = fs.readFileSync(path, 'utf8');

  if (hasScope(options)) {
    data = extractScope(data, options.scope);
  }

  var lines = String(data).split(LINE_DELIMITER).map(normalizeLines).filter(filterLines);
  return lines.map(function(line) {
    return new Declaration(line, declarationStore);
  });
}

function Processor(path, options) {
  var declarations;
  var declarationStore = new DeclarationStore();

  if (hasDependencies(options)) {
    options.dependencies.forEach(function(dependency) {
      declarationsFromString(dependency.path, declarationStore, dependency);
    });
  }

  declarations = declarationsFromString(path, declarationStore, options);

  this.object = makeObject(declarations, options);
}

module.exports = Processor;
