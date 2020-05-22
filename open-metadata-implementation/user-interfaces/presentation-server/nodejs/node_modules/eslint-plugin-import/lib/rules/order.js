'use strict';

var _slicedToArray = function () { function sliceIterator(arr, i) { var _arr = []; var _n = true; var _d = false; var _e = undefined; try { for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"]) _i["return"](); } finally { if (_d) throw _e; } } return _arr; } return function (arr, i) { if (Array.isArray(arr)) { return arr; } else if (Symbol.iterator in Object(arr)) { return sliceIterator(arr, i); } else { throw new TypeError("Invalid attempt to destructure non-iterable instance"); } }; }();

var _minimatch = require('minimatch');

var _minimatch2 = _interopRequireDefault(_minimatch);

var _importType = require('../core/importType');

var _importType2 = _interopRequireDefault(_importType);

var _staticRequire = require('../core/staticRequire');

var _staticRequire2 = _interopRequireDefault(_staticRequire);

var _docsUrl = require('../docsUrl');

var _docsUrl2 = _interopRequireDefault(_docsUrl);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

const defaultGroups = ['builtin', 'external', 'parent', 'sibling', 'index'];

// REPORTING AND FIXING

function reverse(array) {
  return array.map(function (v) {
    return {
      name: v.name,
      rank: -v.rank,
      node: v.node
    };
  }).reverse();
}

function getTokensOrCommentsAfter(sourceCode, node, count) {
  let currentNodeOrToken = node;
  const result = [];
  for (let i = 0; i < count; i++) {
    currentNodeOrToken = sourceCode.getTokenOrCommentAfter(currentNodeOrToken);
    if (currentNodeOrToken == null) {
      break;
    }
    result.push(currentNodeOrToken);
  }
  return result;
}

function getTokensOrCommentsBefore(sourceCode, node, count) {
  let currentNodeOrToken = node;
  const result = [];
  for (let i = 0; i < count; i++) {
    currentNodeOrToken = sourceCode.getTokenOrCommentBefore(currentNodeOrToken);
    if (currentNodeOrToken == null) {
      break;
    }
    result.push(currentNodeOrToken);
  }
  return result.reverse();
}

function takeTokensAfterWhile(sourceCode, node, condition) {
  const tokens = getTokensOrCommentsAfter(sourceCode, node, 100);
  const result = [];
  for (let i = 0; i < tokens.length; i++) {
    if (condition(tokens[i])) {
      result.push(tokens[i]);
    } else {
      break;
    }
  }
  return result;
}

function takeTokensBeforeWhile(sourceCode, node, condition) {
  const tokens = getTokensOrCommentsBefore(sourceCode, node, 100);
  const result = [];
  for (let i = tokens.length - 1; i >= 0; i--) {
    if (condition(tokens[i])) {
      result.push(tokens[i]);
    } else {
      break;
    }
  }
  return result.reverse();
}

function findOutOfOrder(imported) {
  if (imported.length === 0) {
    return [];
  }
  let maxSeenRankNode = imported[0];
  return imported.filter(function (importedModule) {
    const res = importedModule.rank < maxSeenRankNode.rank;
    if (maxSeenRankNode.rank < importedModule.rank) {
      maxSeenRankNode = importedModule;
    }
    return res;
  });
}

function findRootNode(node) {
  let parent = node;
  while (parent.parent != null && parent.parent.body == null) {
    parent = parent.parent;
  }
  return parent;
}

function findEndOfLineWithComments(sourceCode, node) {
  const tokensToEndOfLine = takeTokensAfterWhile(sourceCode, node, commentOnSameLineAs(node));
  let endOfTokens = tokensToEndOfLine.length > 0 ? tokensToEndOfLine[tokensToEndOfLine.length - 1].range[1] : node.range[1];
  let result = endOfTokens;
  for (let i = endOfTokens; i < sourceCode.text.length; i++) {
    if (sourceCode.text[i] === '\n') {
      result = i + 1;
      break;
    }
    if (sourceCode.text[i] !== ' ' && sourceCode.text[i] !== '\t' && sourceCode.text[i] !== '\r') {
      break;
    }
    result = i + 1;
  }
  return result;
}

function commentOnSameLineAs(node) {
  return token => (token.type === 'Block' || token.type === 'Line') && token.loc.start.line === token.loc.end.line && token.loc.end.line === node.loc.end.line;
}

function findStartOfLineWithComments(sourceCode, node) {
  const tokensToEndOfLine = takeTokensBeforeWhile(sourceCode, node, commentOnSameLineAs(node));
  let startOfTokens = tokensToEndOfLine.length > 0 ? tokensToEndOfLine[0].range[0] : node.range[0];
  let result = startOfTokens;
  for (let i = startOfTokens - 1; i > 0; i--) {
    if (sourceCode.text[i] !== ' ' && sourceCode.text[i] !== '\t') {
      break;
    }
    result = i;
  }
  return result;
}

function isPlainRequireModule(node) {
  if (node.type !== 'VariableDeclaration') {
    return false;
  }
  if (node.declarations.length !== 1) {
    return false;
  }
  const decl = node.declarations[0];
  const result = decl.id && (decl.id.type === 'Identifier' || decl.id.type === 'ObjectPattern') && decl.init != null && decl.init.type === 'CallExpression' && decl.init.callee != null && decl.init.callee.name === 'require' && decl.init.arguments != null && decl.init.arguments.length === 1 && decl.init.arguments[0].type === 'Literal';
  return result;
}

function isPlainImportModule(node) {
  return node.type === 'ImportDeclaration' && node.specifiers != null && node.specifiers.length > 0;
}

function canCrossNodeWhileReorder(node) {
  return isPlainRequireModule(node) || isPlainImportModule(node);
}

function canReorderItems(firstNode, secondNode) {
  const parent = firstNode.parent;

  var _sort = [parent.body.indexOf(firstNode), parent.body.indexOf(secondNode)].sort(),
      _sort2 = _slicedToArray(_sort, 2);

  const firstIndex = _sort2[0],
        secondIndex = _sort2[1];

  const nodesBetween = parent.body.slice(firstIndex, secondIndex + 1);
  for (var nodeBetween of nodesBetween) {
    if (!canCrossNodeWhileReorder(nodeBetween)) {
      return false;
    }
  }
  return true;
}

function fixOutOfOrder(context, firstNode, secondNode, order) {
  const sourceCode = context.getSourceCode();

  const firstRoot = findRootNode(firstNode.node);
  const firstRootStart = findStartOfLineWithComments(sourceCode, firstRoot);
  const firstRootEnd = findEndOfLineWithComments(sourceCode, firstRoot);

  const secondRoot = findRootNode(secondNode.node);
  const secondRootStart = findStartOfLineWithComments(sourceCode, secondRoot);
  const secondRootEnd = findEndOfLineWithComments(sourceCode, secondRoot);
  const canFix = canReorderItems(firstRoot, secondRoot);

  let newCode = sourceCode.text.substring(secondRootStart, secondRootEnd);
  if (newCode[newCode.length - 1] !== '\n') {
    newCode = newCode + '\n';
  }

  const message = '`' + secondNode.name + '` import should occur ' + order + ' import of `' + firstNode.name + '`';

  if (order === 'before') {
    context.report({
      node: secondNode.node,
      message: message,
      fix: canFix && (fixer => fixer.replaceTextRange([firstRootStart, secondRootEnd], newCode + sourceCode.text.substring(firstRootStart, secondRootStart)))
    });
  } else if (order === 'after') {
    context.report({
      node: secondNode.node,
      message: message,
      fix: canFix && (fixer => fixer.replaceTextRange([secondRootStart, firstRootEnd], sourceCode.text.substring(secondRootEnd, firstRootEnd) + newCode))
    });
  }
}

function reportOutOfOrder(context, imported, outOfOrder, order) {
  outOfOrder.forEach(function (imp) {
    const found = imported.find(function hasHigherRank(importedItem) {
      return importedItem.rank > imp.rank;
    });
    fixOutOfOrder(context, found, imp, order);
  });
}

function makeOutOfOrderReport(context, imported) {
  const outOfOrder = findOutOfOrder(imported);
  if (!outOfOrder.length) {
    return;
  }
  // There are things to report. Try to minimize the number of reported errors.
  const reversedImported = reverse(imported);
  const reversedOrder = findOutOfOrder(reversedImported);
  if (reversedOrder.length < outOfOrder.length) {
    reportOutOfOrder(context, reversedImported, reversedOrder, 'after');
    return;
  }
  reportOutOfOrder(context, imported, outOfOrder, 'before');
}

function importsSorterAsc(importA, importB) {
  if (importA < importB) {
    return -1;
  }

  if (importA > importB) {
    return 1;
  }

  return 0;
}

function importsSorterDesc(importA, importB) {
  if (importA < importB) {
    return 1;
  }

  if (importA > importB) {
    return -1;
  }

  return 0;
}

function mutateRanksToAlphabetize(imported, alphabetizeOptions) {
  const groupedByRanks = imported.reduce(function (acc, importedItem) {
    if (!Array.isArray(acc[importedItem.rank])) {
      acc[importedItem.rank] = [];
    }
    acc[importedItem.rank].push(importedItem.name);
    return acc;
  }, {});

  const groupRanks = Object.keys(groupedByRanks);

  const sorterFn = alphabetizeOptions.order === 'asc' ? importsSorterAsc : importsSorterDesc;
  const comparator = alphabetizeOptions.caseInsensitive ? (a, b) => sorterFn(String(a).toLowerCase(), String(b).toLowerCase()) : (a, b) => sorterFn(a, b);
  // sort imports locally within their group
  groupRanks.forEach(function (groupRank) {
    groupedByRanks[groupRank].sort(comparator);
  });

  // assign globally unique rank to each import
  let newRank = 0;
  const alphabetizedRanks = groupRanks.sort().reduce(function (acc, groupRank) {
    groupedByRanks[groupRank].forEach(function (importedItemName) {
      acc[importedItemName] = newRank;
      newRank += 1;
    });
    return acc;
  }, {});

  // mutate the original group-rank with alphabetized-rank
  imported.forEach(function (importedItem) {
    importedItem.rank = alphabetizedRanks[importedItem.name];
  });
}

// DETECTING

function computePathRank(ranks, pathGroups, path, maxPosition) {
  for (let i = 0, l = pathGroups.length; i < l; i++) {
    var _pathGroups$i = pathGroups[i];
    const pattern = _pathGroups$i.pattern,
          patternOptions = _pathGroups$i.patternOptions,
          group = _pathGroups$i.group;
    var _pathGroups$i$positio = _pathGroups$i.position;
    const position = _pathGroups$i$positio === undefined ? 1 : _pathGroups$i$positio;

    if ((0, _minimatch2.default)(path, pattern, patternOptions || { nocomment: true })) {
      return ranks[group] + position / maxPosition;
    }
  }
}

function computeRank(context, ranks, name, type, excludedImportTypes) {
  const impType = (0, _importType2.default)(name, context);
  let rank;
  if (!excludedImportTypes.has(impType)) {
    rank = computePathRank(ranks.groups, ranks.pathGroups, name, ranks.maxPosition);
  }
  if (!rank) {
    rank = ranks.groups[impType];
  }
  if (type !== 'import') {
    rank += 100;
  }

  return rank;
}

function registerNode(context, node, name, type, ranks, imported, excludedImportTypes) {
  const rank = computeRank(context, ranks, name, type, excludedImportTypes);
  if (rank !== -1) {
    imported.push({ name, rank, node });
  }
}

function isInVariableDeclarator(node) {
  return node && (node.type === 'VariableDeclarator' || isInVariableDeclarator(node.parent));
}

const types = ['builtin', 'external', 'internal', 'unknown', 'parent', 'sibling', 'index'];

// Creates an object with type-rank pairs.
// Example: { index: 0, sibling: 1, parent: 1, external: 1, builtin: 2, internal: 2 }
// Will throw an error if it contains a type that does not exist, or has a duplicate
function convertGroupsToRanks(groups) {
  const rankObject = groups.reduce(function (res, group, index) {
    if (typeof group === 'string') {
      group = [group];
    }
    group.forEach(function (groupItem) {
      if (types.indexOf(groupItem) === -1) {
        throw new Error('Incorrect configuration of the rule: Unknown type `' + JSON.stringify(groupItem) + '`');
      }
      if (res[groupItem] !== undefined) {
        throw new Error('Incorrect configuration of the rule: `' + groupItem + '` is duplicated');
      }
      res[groupItem] = index;
    });
    return res;
  }, {});

  const omittedTypes = types.filter(function (type) {
    return rankObject[type] === undefined;
  });

  return omittedTypes.reduce(function (res, type) {
    res[type] = groups.length;
    return res;
  }, rankObject);
}

function convertPathGroupsForRanks(pathGroups) {
  const after = {};
  const before = {};

  const transformed = pathGroups.map((pathGroup, index) => {
    const group = pathGroup.group,
          positionString = pathGroup.position;

    let position = 0;
    if (positionString === 'after') {
      if (!after[group]) {
        after[group] = 1;
      }
      position = after[group]++;
    } else if (positionString === 'before') {
      if (!before[group]) {
        before[group] = [];
      }
      before[group].push(index);
    }

    return Object.assign({}, pathGroup, { position });
  });

  let maxPosition = 1;

  Object.keys(before).forEach(group => {
    const groupLength = before[group].length;
    before[group].forEach((groupIndex, index) => {
      transformed[groupIndex].position = -1 * (groupLength - index);
    });
    maxPosition = Math.max(maxPosition, groupLength);
  });

  Object.keys(after).forEach(key => {
    const groupNextPosition = after[key];
    maxPosition = Math.max(maxPosition, groupNextPosition - 1);
  });

  return {
    pathGroups: transformed,
    maxPosition: maxPosition > 10 ? Math.pow(10, Math.ceil(Math.log10(maxPosition))) : 10
  };
}

function fixNewLineAfterImport(context, previousImport) {
  const prevRoot = findRootNode(previousImport.node);
  const tokensToEndOfLine = takeTokensAfterWhile(context.getSourceCode(), prevRoot, commentOnSameLineAs(prevRoot));

  let endOfLine = prevRoot.range[1];
  if (tokensToEndOfLine.length > 0) {
    endOfLine = tokensToEndOfLine[tokensToEndOfLine.length - 1].range[1];
  }
  return fixer => fixer.insertTextAfterRange([prevRoot.range[0], endOfLine], '\n');
}

function removeNewLineAfterImport(context, currentImport, previousImport) {
  const sourceCode = context.getSourceCode();
  const prevRoot = findRootNode(previousImport.node);
  const currRoot = findRootNode(currentImport.node);
  const rangeToRemove = [findEndOfLineWithComments(sourceCode, prevRoot), findStartOfLineWithComments(sourceCode, currRoot)];
  if (/^\s*$/.test(sourceCode.text.substring(rangeToRemove[0], rangeToRemove[1]))) {
    return fixer => fixer.removeRange(rangeToRemove);
  }
  return undefined;
}

function makeNewlinesBetweenReport(context, imported, newlinesBetweenImports) {
  const getNumberOfEmptyLinesBetween = (currentImport, previousImport) => {
    const linesBetweenImports = context.getSourceCode().lines.slice(previousImport.node.loc.end.line, currentImport.node.loc.start.line - 1);

    return linesBetweenImports.filter(line => !line.trim().length).length;
  };
  let previousImport = imported[0];

  imported.slice(1).forEach(function (currentImport) {
    const emptyLinesBetween = getNumberOfEmptyLinesBetween(currentImport, previousImport);

    if (newlinesBetweenImports === 'always' || newlinesBetweenImports === 'always-and-inside-groups') {
      if (currentImport.rank !== previousImport.rank && emptyLinesBetween === 0) {
        context.report({
          node: previousImport.node,
          message: 'There should be at least one empty line between import groups',
          fix: fixNewLineAfterImport(context, previousImport)
        });
      } else if (currentImport.rank === previousImport.rank && emptyLinesBetween > 0 && newlinesBetweenImports !== 'always-and-inside-groups') {
        context.report({
          node: previousImport.node,
          message: 'There should be no empty line within import group',
          fix: removeNewLineAfterImport(context, currentImport, previousImport)
        });
      }
    } else if (emptyLinesBetween > 0) {
      context.report({
        node: previousImport.node,
        message: 'There should be no empty line between import groups',
        fix: removeNewLineAfterImport(context, currentImport, previousImport)
      });
    }

    previousImport = currentImport;
  });
}

function getAlphabetizeConfig(options) {
  const alphabetize = options.alphabetize || {};
  const order = alphabetize.order || 'ignore';
  const caseInsensitive = alphabetize.caseInsensitive || false;

  return { order, caseInsensitive };
}

module.exports = {
  meta: {
    type: 'suggestion',
    docs: {
      url: (0, _docsUrl2.default)('order')
    },

    fixable: 'code',
    schema: [{
      type: 'object',
      properties: {
        groups: {
          type: 'array'
        },
        pathGroupsExcludedImportTypes: {
          type: 'array'
        },
        pathGroups: {
          type: 'array',
          items: {
            type: 'object',
            properties: {
              pattern: {
                type: 'string'
              },
              patternOptions: {
                type: 'object'
              },
              group: {
                type: 'string',
                enum: types
              },
              position: {
                type: 'string',
                enum: ['after', 'before']
              }
            },
            required: ['pattern', 'group']
          }
        },
        'newlines-between': {
          enum: ['ignore', 'always', 'always-and-inside-groups', 'never']
        },
        alphabetize: {
          type: 'object',
          properties: {
            caseInsensitive: {
              type: 'boolean',
              default: false
            },
            order: {
              enum: ['ignore', 'asc', 'desc'],
              default: 'ignore'
            }
          },
          additionalProperties: false
        }
      },
      additionalProperties: false
    }]
  },

  create: function importOrderRule(context) {
    const options = context.options[0] || {};
    const newlinesBetweenImports = options['newlines-between'] || 'ignore';
    const pathGroupsExcludedImportTypes = new Set(options['pathGroupsExcludedImportTypes'] || ['builtin', 'external']);
    const alphabetize = getAlphabetizeConfig(options);
    let ranks;

    try {
      var _convertPathGroupsFor = convertPathGroupsForRanks(options.pathGroups || []);

      const pathGroups = _convertPathGroupsFor.pathGroups,
            maxPosition = _convertPathGroupsFor.maxPosition;

      ranks = {
        groups: convertGroupsToRanks(options.groups || defaultGroups),
        pathGroups,
        maxPosition
      };
    } catch (error) {
      // Malformed configuration
      return {
        Program: function (node) {
          context.report(node, error.message);
        }
      };
    }
    let imported = [];
    let level = 0;

    function incrementLevel() {
      level++;
    }
    function decrementLevel() {
      level--;
    }

    return {
      ImportDeclaration: function handleImports(node) {
        if (node.specifiers.length) {
          // Ignoring unassigned imports
          const name = node.source.value;
          registerNode(context, node, name, 'import', ranks, imported, pathGroupsExcludedImportTypes);
        }
      },
      CallExpression: function handleRequires(node) {
        if (level !== 0 || !(0, _staticRequire2.default)(node) || !isInVariableDeclarator(node.parent)) {
          return;
        }
        const name = node.arguments[0].value;
        registerNode(context, node, name, 'require', ranks, imported, pathGroupsExcludedImportTypes);
      },
      'Program:exit': function reportAndReset() {
        if (newlinesBetweenImports !== 'ignore') {
          makeNewlinesBetweenReport(context, imported, newlinesBetweenImports);
        }

        if (alphabetize.order !== 'ignore') {
          mutateRanksToAlphabetize(imported, alphabetize);
        }

        makeOutOfOrderReport(context, imported);

        imported = [];
      },
      FunctionDeclaration: incrementLevel,
      FunctionExpression: incrementLevel,
      ArrowFunctionExpression: incrementLevel,
      BlockStatement: incrementLevel,
      ObjectExpression: incrementLevel,
      'FunctionDeclaration:exit': decrementLevel,
      'FunctionExpression:exit': decrementLevel,
      'ArrowFunctionExpression:exit': decrementLevel,
      'BlockStatement:exit': decrementLevel,
      'ObjectExpression:exit': decrementLevel
    };
  }
};
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uL3NyYy9ydWxlcy9vcmRlci5qcyJdLCJuYW1lcyI6WyJkZWZhdWx0R3JvdXBzIiwicmV2ZXJzZSIsImFycmF5IiwibWFwIiwidiIsIm5hbWUiLCJyYW5rIiwibm9kZSIsImdldFRva2Vuc09yQ29tbWVudHNBZnRlciIsInNvdXJjZUNvZGUiLCJjb3VudCIsImN1cnJlbnROb2RlT3JUb2tlbiIsInJlc3VsdCIsImkiLCJnZXRUb2tlbk9yQ29tbWVudEFmdGVyIiwicHVzaCIsImdldFRva2Vuc09yQ29tbWVudHNCZWZvcmUiLCJnZXRUb2tlbk9yQ29tbWVudEJlZm9yZSIsInRha2VUb2tlbnNBZnRlcldoaWxlIiwiY29uZGl0aW9uIiwidG9rZW5zIiwibGVuZ3RoIiwidGFrZVRva2Vuc0JlZm9yZVdoaWxlIiwiZmluZE91dE9mT3JkZXIiLCJpbXBvcnRlZCIsIm1heFNlZW5SYW5rTm9kZSIsImZpbHRlciIsImltcG9ydGVkTW9kdWxlIiwicmVzIiwiZmluZFJvb3ROb2RlIiwicGFyZW50IiwiYm9keSIsImZpbmRFbmRPZkxpbmVXaXRoQ29tbWVudHMiLCJ0b2tlbnNUb0VuZE9mTGluZSIsImNvbW1lbnRPblNhbWVMaW5lQXMiLCJlbmRPZlRva2VucyIsInJhbmdlIiwidGV4dCIsInRva2VuIiwidHlwZSIsImxvYyIsInN0YXJ0IiwibGluZSIsImVuZCIsImZpbmRTdGFydE9mTGluZVdpdGhDb21tZW50cyIsInN0YXJ0T2ZUb2tlbnMiLCJpc1BsYWluUmVxdWlyZU1vZHVsZSIsImRlY2xhcmF0aW9ucyIsImRlY2wiLCJpZCIsImluaXQiLCJjYWxsZWUiLCJhcmd1bWVudHMiLCJpc1BsYWluSW1wb3J0TW9kdWxlIiwic3BlY2lmaWVycyIsImNhbkNyb3NzTm9kZVdoaWxlUmVvcmRlciIsImNhblJlb3JkZXJJdGVtcyIsImZpcnN0Tm9kZSIsInNlY29uZE5vZGUiLCJpbmRleE9mIiwic29ydCIsImZpcnN0SW5kZXgiLCJzZWNvbmRJbmRleCIsIm5vZGVzQmV0d2VlbiIsInNsaWNlIiwibm9kZUJldHdlZW4iLCJmaXhPdXRPZk9yZGVyIiwiY29udGV4dCIsIm9yZGVyIiwiZ2V0U291cmNlQ29kZSIsImZpcnN0Um9vdCIsImZpcnN0Um9vdFN0YXJ0IiwiZmlyc3RSb290RW5kIiwic2Vjb25kUm9vdCIsInNlY29uZFJvb3RTdGFydCIsInNlY29uZFJvb3RFbmQiLCJjYW5GaXgiLCJuZXdDb2RlIiwic3Vic3RyaW5nIiwibWVzc2FnZSIsInJlcG9ydCIsImZpeCIsImZpeGVyIiwicmVwbGFjZVRleHRSYW5nZSIsInJlcG9ydE91dE9mT3JkZXIiLCJvdXRPZk9yZGVyIiwiZm9yRWFjaCIsImltcCIsImZvdW5kIiwiZmluZCIsImhhc0hpZ2hlclJhbmsiLCJpbXBvcnRlZEl0ZW0iLCJtYWtlT3V0T2ZPcmRlclJlcG9ydCIsInJldmVyc2VkSW1wb3J0ZWQiLCJyZXZlcnNlZE9yZGVyIiwiaW1wb3J0c1NvcnRlckFzYyIsImltcG9ydEEiLCJpbXBvcnRCIiwiaW1wb3J0c1NvcnRlckRlc2MiLCJtdXRhdGVSYW5rc1RvQWxwaGFiZXRpemUiLCJhbHBoYWJldGl6ZU9wdGlvbnMiLCJncm91cGVkQnlSYW5rcyIsInJlZHVjZSIsImFjYyIsIkFycmF5IiwiaXNBcnJheSIsImdyb3VwUmFua3MiLCJPYmplY3QiLCJrZXlzIiwic29ydGVyRm4iLCJjb21wYXJhdG9yIiwiY2FzZUluc2Vuc2l0aXZlIiwiYSIsImIiLCJTdHJpbmciLCJ0b0xvd2VyQ2FzZSIsImdyb3VwUmFuayIsIm5ld1JhbmsiLCJhbHBoYWJldGl6ZWRSYW5rcyIsImltcG9ydGVkSXRlbU5hbWUiLCJjb21wdXRlUGF0aFJhbmsiLCJyYW5rcyIsInBhdGhHcm91cHMiLCJwYXRoIiwibWF4UG9zaXRpb24iLCJsIiwicGF0dGVybiIsInBhdHRlcm5PcHRpb25zIiwiZ3JvdXAiLCJwb3NpdGlvbiIsIm5vY29tbWVudCIsImNvbXB1dGVSYW5rIiwiZXhjbHVkZWRJbXBvcnRUeXBlcyIsImltcFR5cGUiLCJoYXMiLCJncm91cHMiLCJyZWdpc3Rlck5vZGUiLCJpc0luVmFyaWFibGVEZWNsYXJhdG9yIiwidHlwZXMiLCJjb252ZXJ0R3JvdXBzVG9SYW5rcyIsInJhbmtPYmplY3QiLCJpbmRleCIsImdyb3VwSXRlbSIsIkVycm9yIiwiSlNPTiIsInN0cmluZ2lmeSIsInVuZGVmaW5lZCIsIm9taXR0ZWRUeXBlcyIsImNvbnZlcnRQYXRoR3JvdXBzRm9yUmFua3MiLCJhZnRlciIsImJlZm9yZSIsInRyYW5zZm9ybWVkIiwicGF0aEdyb3VwIiwicG9zaXRpb25TdHJpbmciLCJhc3NpZ24iLCJncm91cExlbmd0aCIsImdyb3VwSW5kZXgiLCJNYXRoIiwibWF4Iiwia2V5IiwiZ3JvdXBOZXh0UG9zaXRpb24iLCJwb3ciLCJjZWlsIiwibG9nMTAiLCJmaXhOZXdMaW5lQWZ0ZXJJbXBvcnQiLCJwcmV2aW91c0ltcG9ydCIsInByZXZSb290IiwiZW5kT2ZMaW5lIiwiaW5zZXJ0VGV4dEFmdGVyUmFuZ2UiLCJyZW1vdmVOZXdMaW5lQWZ0ZXJJbXBvcnQiLCJjdXJyZW50SW1wb3J0IiwiY3VyclJvb3QiLCJyYW5nZVRvUmVtb3ZlIiwidGVzdCIsInJlbW92ZVJhbmdlIiwibWFrZU5ld2xpbmVzQmV0d2VlblJlcG9ydCIsIm5ld2xpbmVzQmV0d2VlbkltcG9ydHMiLCJnZXROdW1iZXJPZkVtcHR5TGluZXNCZXR3ZWVuIiwibGluZXNCZXR3ZWVuSW1wb3J0cyIsImxpbmVzIiwidHJpbSIsImVtcHR5TGluZXNCZXR3ZWVuIiwiZ2V0QWxwaGFiZXRpemVDb25maWciLCJvcHRpb25zIiwiYWxwaGFiZXRpemUiLCJtb2R1bGUiLCJleHBvcnRzIiwibWV0YSIsImRvY3MiLCJ1cmwiLCJmaXhhYmxlIiwic2NoZW1hIiwicHJvcGVydGllcyIsInBhdGhHcm91cHNFeGNsdWRlZEltcG9ydFR5cGVzIiwiaXRlbXMiLCJlbnVtIiwicmVxdWlyZWQiLCJkZWZhdWx0IiwiYWRkaXRpb25hbFByb3BlcnRpZXMiLCJjcmVhdGUiLCJpbXBvcnRPcmRlclJ1bGUiLCJTZXQiLCJlcnJvciIsIlByb2dyYW0iLCJsZXZlbCIsImluY3JlbWVudExldmVsIiwiZGVjcmVtZW50TGV2ZWwiLCJJbXBvcnREZWNsYXJhdGlvbiIsImhhbmRsZUltcG9ydHMiLCJzb3VyY2UiLCJ2YWx1ZSIsIkNhbGxFeHByZXNzaW9uIiwiaGFuZGxlUmVxdWlyZXMiLCJyZXBvcnRBbmRSZXNldCIsIkZ1bmN0aW9uRGVjbGFyYXRpb24iLCJGdW5jdGlvbkV4cHJlc3Npb24iLCJBcnJvd0Z1bmN0aW9uRXhwcmVzc2lvbiIsIkJsb2NrU3RhdGVtZW50IiwiT2JqZWN0RXhwcmVzc2lvbiJdLCJtYXBwaW5ncyI6IkFBQUE7Ozs7QUFFQTs7OztBQUNBOzs7O0FBQ0E7Ozs7QUFDQTs7Ozs7O0FBRUEsTUFBTUEsZ0JBQWdCLENBQUMsU0FBRCxFQUFZLFVBQVosRUFBd0IsUUFBeEIsRUFBa0MsU0FBbEMsRUFBNkMsT0FBN0MsQ0FBdEI7O0FBRUE7O0FBRUEsU0FBU0MsT0FBVCxDQUFpQkMsS0FBakIsRUFBd0I7QUFDdEIsU0FBT0EsTUFBTUMsR0FBTixDQUFVLFVBQVVDLENBQVYsRUFBYTtBQUM1QixXQUFPO0FBQ0xDLFlBQU1ELEVBQUVDLElBREg7QUFFTEMsWUFBTSxDQUFDRixFQUFFRSxJQUZKO0FBR0xDLFlBQU1ILEVBQUVHO0FBSEgsS0FBUDtBQUtELEdBTk0sRUFNSk4sT0FOSSxFQUFQO0FBT0Q7O0FBRUQsU0FBU08sd0JBQVQsQ0FBa0NDLFVBQWxDLEVBQThDRixJQUE5QyxFQUFvREcsS0FBcEQsRUFBMkQ7QUFDekQsTUFBSUMscUJBQXFCSixJQUF6QjtBQUNBLFFBQU1LLFNBQVMsRUFBZjtBQUNBLE9BQUssSUFBSUMsSUFBSSxDQUFiLEVBQWdCQSxJQUFJSCxLQUFwQixFQUEyQkcsR0FBM0IsRUFBZ0M7QUFDOUJGLHlCQUFxQkYsV0FBV0ssc0JBQVgsQ0FBa0NILGtCQUFsQyxDQUFyQjtBQUNBLFFBQUlBLHNCQUFzQixJQUExQixFQUFnQztBQUM5QjtBQUNEO0FBQ0RDLFdBQU9HLElBQVAsQ0FBWUosa0JBQVo7QUFDRDtBQUNELFNBQU9DLE1BQVA7QUFDRDs7QUFFRCxTQUFTSSx5QkFBVCxDQUFtQ1AsVUFBbkMsRUFBK0NGLElBQS9DLEVBQXFERyxLQUFyRCxFQUE0RDtBQUMxRCxNQUFJQyxxQkFBcUJKLElBQXpCO0FBQ0EsUUFBTUssU0FBUyxFQUFmO0FBQ0EsT0FBSyxJQUFJQyxJQUFJLENBQWIsRUFBZ0JBLElBQUlILEtBQXBCLEVBQTJCRyxHQUEzQixFQUFnQztBQUM5QkYseUJBQXFCRixXQUFXUSx1QkFBWCxDQUFtQ04sa0JBQW5DLENBQXJCO0FBQ0EsUUFBSUEsc0JBQXNCLElBQTFCLEVBQWdDO0FBQzlCO0FBQ0Q7QUFDREMsV0FBT0csSUFBUCxDQUFZSixrQkFBWjtBQUNEO0FBQ0QsU0FBT0MsT0FBT1gsT0FBUCxFQUFQO0FBQ0Q7O0FBRUQsU0FBU2lCLG9CQUFULENBQThCVCxVQUE5QixFQUEwQ0YsSUFBMUMsRUFBZ0RZLFNBQWhELEVBQTJEO0FBQ3pELFFBQU1DLFNBQVNaLHlCQUF5QkMsVUFBekIsRUFBcUNGLElBQXJDLEVBQTJDLEdBQTNDLENBQWY7QUFDQSxRQUFNSyxTQUFTLEVBQWY7QUFDQSxPQUFLLElBQUlDLElBQUksQ0FBYixFQUFnQkEsSUFBSU8sT0FBT0MsTUFBM0IsRUFBbUNSLEdBQW5DLEVBQXdDO0FBQ3RDLFFBQUlNLFVBQVVDLE9BQU9QLENBQVAsQ0FBVixDQUFKLEVBQTBCO0FBQ3hCRCxhQUFPRyxJQUFQLENBQVlLLE9BQU9QLENBQVAsQ0FBWjtBQUNELEtBRkQsTUFHSztBQUNIO0FBQ0Q7QUFDRjtBQUNELFNBQU9ELE1BQVA7QUFDRDs7QUFFRCxTQUFTVSxxQkFBVCxDQUErQmIsVUFBL0IsRUFBMkNGLElBQTNDLEVBQWlEWSxTQUFqRCxFQUE0RDtBQUMxRCxRQUFNQyxTQUFTSiwwQkFBMEJQLFVBQTFCLEVBQXNDRixJQUF0QyxFQUE0QyxHQUE1QyxDQUFmO0FBQ0EsUUFBTUssU0FBUyxFQUFmO0FBQ0EsT0FBSyxJQUFJQyxJQUFJTyxPQUFPQyxNQUFQLEdBQWdCLENBQTdCLEVBQWdDUixLQUFLLENBQXJDLEVBQXdDQSxHQUF4QyxFQUE2QztBQUMzQyxRQUFJTSxVQUFVQyxPQUFPUCxDQUFQLENBQVYsQ0FBSixFQUEwQjtBQUN4QkQsYUFBT0csSUFBUCxDQUFZSyxPQUFPUCxDQUFQLENBQVo7QUFDRCxLQUZELE1BR0s7QUFDSDtBQUNEO0FBQ0Y7QUFDRCxTQUFPRCxPQUFPWCxPQUFQLEVBQVA7QUFDRDs7QUFFRCxTQUFTc0IsY0FBVCxDQUF3QkMsUUFBeEIsRUFBa0M7QUFDaEMsTUFBSUEsU0FBU0gsTUFBVCxLQUFvQixDQUF4QixFQUEyQjtBQUN6QixXQUFPLEVBQVA7QUFDRDtBQUNELE1BQUlJLGtCQUFrQkQsU0FBUyxDQUFULENBQXRCO0FBQ0EsU0FBT0EsU0FBU0UsTUFBVCxDQUFnQixVQUFVQyxjQUFWLEVBQTBCO0FBQy9DLFVBQU1DLE1BQU1ELGVBQWVyQixJQUFmLEdBQXNCbUIsZ0JBQWdCbkIsSUFBbEQ7QUFDQSxRQUFJbUIsZ0JBQWdCbkIsSUFBaEIsR0FBdUJxQixlQUFlckIsSUFBMUMsRUFBZ0Q7QUFDOUNtQix3QkFBa0JFLGNBQWxCO0FBQ0Q7QUFDRCxXQUFPQyxHQUFQO0FBQ0QsR0FOTSxDQUFQO0FBT0Q7O0FBRUQsU0FBU0MsWUFBVCxDQUFzQnRCLElBQXRCLEVBQTRCO0FBQzFCLE1BQUl1QixTQUFTdkIsSUFBYjtBQUNBLFNBQU91QixPQUFPQSxNQUFQLElBQWlCLElBQWpCLElBQXlCQSxPQUFPQSxNQUFQLENBQWNDLElBQWQsSUFBc0IsSUFBdEQsRUFBNEQ7QUFDMURELGFBQVNBLE9BQU9BLE1BQWhCO0FBQ0Q7QUFDRCxTQUFPQSxNQUFQO0FBQ0Q7O0FBRUQsU0FBU0UseUJBQVQsQ0FBbUN2QixVQUFuQyxFQUErQ0YsSUFBL0MsRUFBcUQ7QUFDbkQsUUFBTTBCLG9CQUFvQmYscUJBQXFCVCxVQUFyQixFQUFpQ0YsSUFBakMsRUFBdUMyQixvQkFBb0IzQixJQUFwQixDQUF2QyxDQUExQjtBQUNBLE1BQUk0QixjQUFjRixrQkFBa0JaLE1BQWxCLEdBQTJCLENBQTNCLEdBQ2RZLGtCQUFrQkEsa0JBQWtCWixNQUFsQixHQUEyQixDQUE3QyxFQUFnRGUsS0FBaEQsQ0FBc0QsQ0FBdEQsQ0FEYyxHQUVkN0IsS0FBSzZCLEtBQUwsQ0FBVyxDQUFYLENBRko7QUFHQSxNQUFJeEIsU0FBU3VCLFdBQWI7QUFDQSxPQUFLLElBQUl0QixJQUFJc0IsV0FBYixFQUEwQnRCLElBQUlKLFdBQVc0QixJQUFYLENBQWdCaEIsTUFBOUMsRUFBc0RSLEdBQXRELEVBQTJEO0FBQ3pELFFBQUlKLFdBQVc0QixJQUFYLENBQWdCeEIsQ0FBaEIsTUFBdUIsSUFBM0IsRUFBaUM7QUFDL0JELGVBQVNDLElBQUksQ0FBYjtBQUNBO0FBQ0Q7QUFDRCxRQUFJSixXQUFXNEIsSUFBWCxDQUFnQnhCLENBQWhCLE1BQXVCLEdBQXZCLElBQThCSixXQUFXNEIsSUFBWCxDQUFnQnhCLENBQWhCLE1BQXVCLElBQXJELElBQTZESixXQUFXNEIsSUFBWCxDQUFnQnhCLENBQWhCLE1BQXVCLElBQXhGLEVBQThGO0FBQzVGO0FBQ0Q7QUFDREQsYUFBU0MsSUFBSSxDQUFiO0FBQ0Q7QUFDRCxTQUFPRCxNQUFQO0FBQ0Q7O0FBRUQsU0FBU3NCLG1CQUFULENBQTZCM0IsSUFBN0IsRUFBbUM7QUFDakMsU0FBTytCLFNBQVMsQ0FBQ0EsTUFBTUMsSUFBTixLQUFlLE9BQWYsSUFBMkJELE1BQU1DLElBQU4sS0FBZSxNQUEzQyxLQUNaRCxNQUFNRSxHQUFOLENBQVVDLEtBQVYsQ0FBZ0JDLElBQWhCLEtBQXlCSixNQUFNRSxHQUFOLENBQVVHLEdBQVYsQ0FBY0QsSUFEM0IsSUFFWkosTUFBTUUsR0FBTixDQUFVRyxHQUFWLENBQWNELElBQWQsS0FBdUJuQyxLQUFLaUMsR0FBTCxDQUFTRyxHQUFULENBQWFELElBRnhDO0FBR0Q7O0FBRUQsU0FBU0UsMkJBQVQsQ0FBcUNuQyxVQUFyQyxFQUFpREYsSUFBakQsRUFBdUQ7QUFDckQsUUFBTTBCLG9CQUFvQlgsc0JBQXNCYixVQUF0QixFQUFrQ0YsSUFBbEMsRUFBd0MyQixvQkFBb0IzQixJQUFwQixDQUF4QyxDQUExQjtBQUNBLE1BQUlzQyxnQkFBZ0JaLGtCQUFrQlosTUFBbEIsR0FBMkIsQ0FBM0IsR0FBK0JZLGtCQUFrQixDQUFsQixFQUFxQkcsS0FBckIsQ0FBMkIsQ0FBM0IsQ0FBL0IsR0FBK0Q3QixLQUFLNkIsS0FBTCxDQUFXLENBQVgsQ0FBbkY7QUFDQSxNQUFJeEIsU0FBU2lDLGFBQWI7QUFDQSxPQUFLLElBQUloQyxJQUFJZ0MsZ0JBQWdCLENBQTdCLEVBQWdDaEMsSUFBSSxDQUFwQyxFQUF1Q0EsR0FBdkMsRUFBNEM7QUFDMUMsUUFBSUosV0FBVzRCLElBQVgsQ0FBZ0J4QixDQUFoQixNQUF1QixHQUF2QixJQUE4QkosV0FBVzRCLElBQVgsQ0FBZ0J4QixDQUFoQixNQUF1QixJQUF6RCxFQUErRDtBQUM3RDtBQUNEO0FBQ0RELGFBQVNDLENBQVQ7QUFDRDtBQUNELFNBQU9ELE1BQVA7QUFDRDs7QUFFRCxTQUFTa0Msb0JBQVQsQ0FBOEJ2QyxJQUE5QixFQUFvQztBQUNsQyxNQUFJQSxLQUFLZ0MsSUFBTCxLQUFjLHFCQUFsQixFQUF5QztBQUN2QyxXQUFPLEtBQVA7QUFDRDtBQUNELE1BQUloQyxLQUFLd0MsWUFBTCxDQUFrQjFCLE1BQWxCLEtBQTZCLENBQWpDLEVBQW9DO0FBQ2xDLFdBQU8sS0FBUDtBQUNEO0FBQ0QsUUFBTTJCLE9BQU96QyxLQUFLd0MsWUFBTCxDQUFrQixDQUFsQixDQUFiO0FBQ0EsUUFBTW5DLFNBQVNvQyxLQUFLQyxFQUFMLEtBQ1pELEtBQUtDLEVBQUwsQ0FBUVYsSUFBUixLQUFpQixZQUFqQixJQUFpQ1MsS0FBS0MsRUFBTCxDQUFRVixJQUFSLEtBQWlCLGVBRHRDLEtBRWJTLEtBQUtFLElBQUwsSUFBYSxJQUZBLElBR2JGLEtBQUtFLElBQUwsQ0FBVVgsSUFBVixLQUFtQixnQkFITixJQUliUyxLQUFLRSxJQUFMLENBQVVDLE1BQVYsSUFBb0IsSUFKUCxJQUtiSCxLQUFLRSxJQUFMLENBQVVDLE1BQVYsQ0FBaUI5QyxJQUFqQixLQUEwQixTQUxiLElBTWIyQyxLQUFLRSxJQUFMLENBQVVFLFNBQVYsSUFBdUIsSUFOVixJQU9iSixLQUFLRSxJQUFMLENBQVVFLFNBQVYsQ0FBb0IvQixNQUFwQixLQUErQixDQVBsQixJQVFiMkIsS0FBS0UsSUFBTCxDQUFVRSxTQUFWLENBQW9CLENBQXBCLEVBQXVCYixJQUF2QixLQUFnQyxTQVJsQztBQVNBLFNBQU8zQixNQUFQO0FBQ0Q7O0FBRUQsU0FBU3lDLG1CQUFULENBQTZCOUMsSUFBN0IsRUFBbUM7QUFDakMsU0FBT0EsS0FBS2dDLElBQUwsS0FBYyxtQkFBZCxJQUFxQ2hDLEtBQUsrQyxVQUFMLElBQW1CLElBQXhELElBQWdFL0MsS0FBSytDLFVBQUwsQ0FBZ0JqQyxNQUFoQixHQUF5QixDQUFoRztBQUNEOztBQUVELFNBQVNrQyx3QkFBVCxDQUFrQ2hELElBQWxDLEVBQXdDO0FBQ3RDLFNBQU91QyxxQkFBcUJ2QyxJQUFyQixLQUE4QjhDLG9CQUFvQjlDLElBQXBCLENBQXJDO0FBQ0Q7O0FBRUQsU0FBU2lELGVBQVQsQ0FBeUJDLFNBQXpCLEVBQW9DQyxVQUFwQyxFQUFnRDtBQUM5QyxRQUFNNUIsU0FBUzJCLFVBQVUzQixNQUF6Qjs7QUFEOEMsY0FFWixDQUNoQ0EsT0FBT0MsSUFBUCxDQUFZNEIsT0FBWixDQUFvQkYsU0FBcEIsQ0FEZ0MsRUFFaEMzQixPQUFPQyxJQUFQLENBQVk0QixPQUFaLENBQW9CRCxVQUFwQixDQUZnQyxFQUdoQ0UsSUFIZ0MsRUFGWTtBQUFBOztBQUFBLFFBRXZDQyxVQUZ1QztBQUFBLFFBRTNCQyxXQUYyQjs7QUFNOUMsUUFBTUMsZUFBZWpDLE9BQU9DLElBQVAsQ0FBWWlDLEtBQVosQ0FBa0JILFVBQWxCLEVBQThCQyxjQUFjLENBQTVDLENBQXJCO0FBQ0EsT0FBSyxJQUFJRyxXQUFULElBQXdCRixZQUF4QixFQUFzQztBQUNwQyxRQUFJLENBQUNSLHlCQUF5QlUsV0FBekIsQ0FBTCxFQUE0QztBQUMxQyxhQUFPLEtBQVA7QUFDRDtBQUNGO0FBQ0QsU0FBTyxJQUFQO0FBQ0Q7O0FBRUQsU0FBU0MsYUFBVCxDQUF1QkMsT0FBdkIsRUFBZ0NWLFNBQWhDLEVBQTJDQyxVQUEzQyxFQUF1RFUsS0FBdkQsRUFBOEQ7QUFDNUQsUUFBTTNELGFBQWEwRCxRQUFRRSxhQUFSLEVBQW5COztBQUVBLFFBQU1DLFlBQVl6QyxhQUFhNEIsVUFBVWxELElBQXZCLENBQWxCO0FBQ0EsUUFBTWdFLGlCQUFpQjNCLDRCQUE0Qm5DLFVBQTVCLEVBQXdDNkQsU0FBeEMsQ0FBdkI7QUFDQSxRQUFNRSxlQUFleEMsMEJBQTBCdkIsVUFBMUIsRUFBc0M2RCxTQUF0QyxDQUFyQjs7QUFFQSxRQUFNRyxhQUFhNUMsYUFBYTZCLFdBQVduRCxJQUF4QixDQUFuQjtBQUNBLFFBQU1tRSxrQkFBa0I5Qiw0QkFBNEJuQyxVQUE1QixFQUF3Q2dFLFVBQXhDLENBQXhCO0FBQ0EsUUFBTUUsZ0JBQWdCM0MsMEJBQTBCdkIsVUFBMUIsRUFBc0NnRSxVQUF0QyxDQUF0QjtBQUNBLFFBQU1HLFNBQVNwQixnQkFBZ0JjLFNBQWhCLEVBQTJCRyxVQUEzQixDQUFmOztBQUVBLE1BQUlJLFVBQVVwRSxXQUFXNEIsSUFBWCxDQUFnQnlDLFNBQWhCLENBQTBCSixlQUExQixFQUEyQ0MsYUFBM0MsQ0FBZDtBQUNBLE1BQUlFLFFBQVFBLFFBQVF4RCxNQUFSLEdBQWlCLENBQXpCLE1BQWdDLElBQXBDLEVBQTBDO0FBQ3hDd0QsY0FBVUEsVUFBVSxJQUFwQjtBQUNEOztBQUVELFFBQU1FLFVBQVUsTUFBTXJCLFdBQVdyRCxJQUFqQixHQUF3Qix3QkFBeEIsR0FBbUQrRCxLQUFuRCxHQUNaLGNBRFksR0FDS1gsVUFBVXBELElBRGYsR0FDc0IsR0FEdEM7O0FBR0EsTUFBSStELFVBQVUsUUFBZCxFQUF3QjtBQUN0QkQsWUFBUWEsTUFBUixDQUFlO0FBQ2J6RSxZQUFNbUQsV0FBV25ELElBREo7QUFFYndFLGVBQVNBLE9BRkk7QUFHYkUsV0FBS0wsV0FBV00sU0FDZEEsTUFBTUMsZ0JBQU4sQ0FDRSxDQUFDWixjQUFELEVBQWlCSSxhQUFqQixDQURGLEVBRUVFLFVBQVVwRSxXQUFXNEIsSUFBWCxDQUFnQnlDLFNBQWhCLENBQTBCUCxjQUExQixFQUEwQ0csZUFBMUMsQ0FGWixDQURHO0FBSFEsS0FBZjtBQVNELEdBVkQsTUFVTyxJQUFJTixVQUFVLE9BQWQsRUFBdUI7QUFDNUJELFlBQVFhLE1BQVIsQ0FBZTtBQUNiekUsWUFBTW1ELFdBQVduRCxJQURKO0FBRWJ3RSxlQUFTQSxPQUZJO0FBR2JFLFdBQUtMLFdBQVdNLFNBQ2RBLE1BQU1DLGdCQUFOLENBQ0UsQ0FBQ1QsZUFBRCxFQUFrQkYsWUFBbEIsQ0FERixFQUVFL0QsV0FBVzRCLElBQVgsQ0FBZ0J5QyxTQUFoQixDQUEwQkgsYUFBMUIsRUFBeUNILFlBQXpDLElBQXlESyxPQUYzRCxDQURHO0FBSFEsS0FBZjtBQVNEO0FBQ0Y7O0FBRUQsU0FBU08sZ0JBQVQsQ0FBMEJqQixPQUExQixFQUFtQzNDLFFBQW5DLEVBQTZDNkQsVUFBN0MsRUFBeURqQixLQUF6RCxFQUFnRTtBQUM5RGlCLGFBQVdDLE9BQVgsQ0FBbUIsVUFBVUMsR0FBVixFQUFlO0FBQ2hDLFVBQU1DLFFBQVFoRSxTQUFTaUUsSUFBVCxDQUFjLFNBQVNDLGFBQVQsQ0FBdUJDLFlBQXZCLEVBQXFDO0FBQy9ELGFBQU9BLGFBQWFyRixJQUFiLEdBQW9CaUYsSUFBSWpGLElBQS9CO0FBQ0QsS0FGYSxDQUFkO0FBR0E0RCxrQkFBY0MsT0FBZCxFQUF1QnFCLEtBQXZCLEVBQThCRCxHQUE5QixFQUFtQ25CLEtBQW5DO0FBQ0QsR0FMRDtBQU1EOztBQUVELFNBQVN3QixvQkFBVCxDQUE4QnpCLE9BQTlCLEVBQXVDM0MsUUFBdkMsRUFBaUQ7QUFDL0MsUUFBTTZELGFBQWE5RCxlQUFlQyxRQUFmLENBQW5CO0FBQ0EsTUFBSSxDQUFDNkQsV0FBV2hFLE1BQWhCLEVBQXdCO0FBQ3RCO0FBQ0Q7QUFDRDtBQUNBLFFBQU13RSxtQkFBbUI1RixRQUFRdUIsUUFBUixDQUF6QjtBQUNBLFFBQU1zRSxnQkFBZ0J2RSxlQUFlc0UsZ0JBQWYsQ0FBdEI7QUFDQSxNQUFJQyxjQUFjekUsTUFBZCxHQUF1QmdFLFdBQVdoRSxNQUF0QyxFQUE4QztBQUM1QytELHFCQUFpQmpCLE9BQWpCLEVBQTBCMEIsZ0JBQTFCLEVBQTRDQyxhQUE1QyxFQUEyRCxPQUEzRDtBQUNBO0FBQ0Q7QUFDRFYsbUJBQWlCakIsT0FBakIsRUFBMEIzQyxRQUExQixFQUFvQzZELFVBQXBDLEVBQWdELFFBQWhEO0FBQ0Q7O0FBRUQsU0FBU1UsZ0JBQVQsQ0FBMEJDLE9BQTFCLEVBQW1DQyxPQUFuQyxFQUE0QztBQUMxQyxNQUFJRCxVQUFVQyxPQUFkLEVBQXVCO0FBQ3JCLFdBQU8sQ0FBQyxDQUFSO0FBQ0Q7O0FBRUQsTUFBSUQsVUFBVUMsT0FBZCxFQUF1QjtBQUNyQixXQUFPLENBQVA7QUFDRDs7QUFFRCxTQUFPLENBQVA7QUFDRDs7QUFFRCxTQUFTQyxpQkFBVCxDQUEyQkYsT0FBM0IsRUFBb0NDLE9BQXBDLEVBQTZDO0FBQzNDLE1BQUlELFVBQVVDLE9BQWQsRUFBdUI7QUFDckIsV0FBTyxDQUFQO0FBQ0Q7O0FBRUQsTUFBSUQsVUFBVUMsT0FBZCxFQUF1QjtBQUNyQixXQUFPLENBQUMsQ0FBUjtBQUNEOztBQUVELFNBQU8sQ0FBUDtBQUNEOztBQUVELFNBQVNFLHdCQUFULENBQWtDM0UsUUFBbEMsRUFBNEM0RSxrQkFBNUMsRUFBZ0U7QUFDOUQsUUFBTUMsaUJBQWlCN0UsU0FBUzhFLE1BQVQsQ0FBZ0IsVUFBU0MsR0FBVCxFQUFjWixZQUFkLEVBQTRCO0FBQ2pFLFFBQUksQ0FBQ2EsTUFBTUMsT0FBTixDQUFjRixJQUFJWixhQUFhckYsSUFBakIsQ0FBZCxDQUFMLEVBQTRDO0FBQzFDaUcsVUFBSVosYUFBYXJGLElBQWpCLElBQXlCLEVBQXpCO0FBQ0Q7QUFDRGlHLFFBQUlaLGFBQWFyRixJQUFqQixFQUF1QlMsSUFBdkIsQ0FBNEI0RSxhQUFhdEYsSUFBekM7QUFDQSxXQUFPa0csR0FBUDtBQUNELEdBTnNCLEVBTXBCLEVBTm9CLENBQXZCOztBQVFBLFFBQU1HLGFBQWFDLE9BQU9DLElBQVAsQ0FBWVAsY0FBWixDQUFuQjs7QUFFQSxRQUFNUSxXQUFXVCxtQkFBbUJoQyxLQUFuQixLQUE2QixLQUE3QixHQUFxQzJCLGdCQUFyQyxHQUF3REcsaUJBQXpFO0FBQ0EsUUFBTVksYUFBYVYsbUJBQW1CVyxlQUFuQixHQUFxQyxDQUFDQyxDQUFELEVBQUlDLENBQUosS0FBVUosU0FBU0ssT0FBT0YsQ0FBUCxFQUFVRyxXQUFWLEVBQVQsRUFBa0NELE9BQU9ELENBQVAsRUFBVUUsV0FBVixFQUFsQyxDQUEvQyxHQUE0RyxDQUFDSCxDQUFELEVBQUlDLENBQUosS0FBVUosU0FBU0csQ0FBVCxFQUFZQyxDQUFaLENBQXpJO0FBQ0E7QUFDQVAsYUFBV3BCLE9BQVgsQ0FBbUIsVUFBUzhCLFNBQVQsRUFBb0I7QUFDckNmLG1CQUFlZSxTQUFmLEVBQTBCeEQsSUFBMUIsQ0FBK0JrRCxVQUEvQjtBQUNELEdBRkQ7O0FBSUE7QUFDQSxNQUFJTyxVQUFVLENBQWQ7QUFDQSxRQUFNQyxvQkFBb0JaLFdBQVc5QyxJQUFYLEdBQWtCMEMsTUFBbEIsQ0FBeUIsVUFBU0MsR0FBVCxFQUFjYSxTQUFkLEVBQXlCO0FBQzFFZixtQkFBZWUsU0FBZixFQUEwQjlCLE9BQTFCLENBQWtDLFVBQVNpQyxnQkFBVCxFQUEyQjtBQUMzRGhCLFVBQUlnQixnQkFBSixJQUF3QkYsT0FBeEI7QUFDQUEsaUJBQVcsQ0FBWDtBQUNELEtBSEQ7QUFJQSxXQUFPZCxHQUFQO0FBQ0QsR0FOeUIsRUFNdkIsRUFOdUIsQ0FBMUI7O0FBUUE7QUFDQS9FLFdBQVM4RCxPQUFULENBQWlCLFVBQVNLLFlBQVQsRUFBdUI7QUFDdENBLGlCQUFhckYsSUFBYixHQUFvQmdILGtCQUFrQjNCLGFBQWF0RixJQUEvQixDQUFwQjtBQUNELEdBRkQ7QUFHRDs7QUFFRDs7QUFFQSxTQUFTbUgsZUFBVCxDQUF5QkMsS0FBekIsRUFBZ0NDLFVBQWhDLEVBQTRDQyxJQUE1QyxFQUFrREMsV0FBbEQsRUFBK0Q7QUFDN0QsT0FBSyxJQUFJL0csSUFBSSxDQUFSLEVBQVdnSCxJQUFJSCxXQUFXckcsTUFBL0IsRUFBdUNSLElBQUlnSCxDQUEzQyxFQUE4Q2hILEdBQTlDLEVBQW1EO0FBQUEsd0JBQ1E2RyxXQUFXN0csQ0FBWCxDQURSO0FBQUEsVUFDekNpSCxPQUR5QyxpQkFDekNBLE9BRHlDO0FBQUEsVUFDaENDLGNBRGdDLGlCQUNoQ0EsY0FEZ0M7QUFBQSxVQUNoQkMsS0FEZ0IsaUJBQ2hCQSxLQURnQjtBQUFBLDhDQUNUQyxRQURTO0FBQUEsVUFDVEEsUUFEUyx5Q0FDRSxDQURGOztBQUVqRCxRQUFJLHlCQUFVTixJQUFWLEVBQWdCRyxPQUFoQixFQUF5QkMsa0JBQWtCLEVBQUVHLFdBQVcsSUFBYixFQUEzQyxDQUFKLEVBQXFFO0FBQ25FLGFBQU9ULE1BQU1PLEtBQU4sSUFBZ0JDLFdBQVdMLFdBQWxDO0FBQ0Q7QUFDRjtBQUNGOztBQUVELFNBQVNPLFdBQVQsQ0FBcUJoRSxPQUFyQixFQUE4QnNELEtBQTlCLEVBQXFDcEgsSUFBckMsRUFBMkNrQyxJQUEzQyxFQUFpRDZGLG1CQUFqRCxFQUFzRTtBQUNwRSxRQUFNQyxVQUFVLDBCQUFXaEksSUFBWCxFQUFpQjhELE9BQWpCLENBQWhCO0FBQ0EsTUFBSTdELElBQUo7QUFDQSxNQUFJLENBQUM4SCxvQkFBb0JFLEdBQXBCLENBQXdCRCxPQUF4QixDQUFMLEVBQXVDO0FBQ3JDL0gsV0FBT2tILGdCQUFnQkMsTUFBTWMsTUFBdEIsRUFBOEJkLE1BQU1DLFVBQXBDLEVBQWdEckgsSUFBaEQsRUFBc0RvSCxNQUFNRyxXQUE1RCxDQUFQO0FBQ0Q7QUFDRCxNQUFJLENBQUN0SCxJQUFMLEVBQVc7QUFDVEEsV0FBT21ILE1BQU1jLE1BQU4sQ0FBYUYsT0FBYixDQUFQO0FBQ0Q7QUFDRCxNQUFJOUYsU0FBUyxRQUFiLEVBQXVCO0FBQ3JCakMsWUFBUSxHQUFSO0FBQ0Q7O0FBRUQsU0FBT0EsSUFBUDtBQUNEOztBQUVELFNBQVNrSSxZQUFULENBQXNCckUsT0FBdEIsRUFBK0I1RCxJQUEvQixFQUFxQ0YsSUFBckMsRUFBMkNrQyxJQUEzQyxFQUFpRGtGLEtBQWpELEVBQXdEakcsUUFBeEQsRUFBa0U0RyxtQkFBbEUsRUFBdUY7QUFDckYsUUFBTTlILE9BQU82SCxZQUFZaEUsT0FBWixFQUFxQnNELEtBQXJCLEVBQTRCcEgsSUFBNUIsRUFBa0NrQyxJQUFsQyxFQUF3QzZGLG1CQUF4QyxDQUFiO0FBQ0EsTUFBSTlILFNBQVMsQ0FBQyxDQUFkLEVBQWlCO0FBQ2ZrQixhQUFTVCxJQUFULENBQWMsRUFBQ1YsSUFBRCxFQUFPQyxJQUFQLEVBQWFDLElBQWIsRUFBZDtBQUNEO0FBQ0Y7O0FBRUQsU0FBU2tJLHNCQUFULENBQWdDbEksSUFBaEMsRUFBc0M7QUFDcEMsU0FBT0EsU0FDSkEsS0FBS2dDLElBQUwsS0FBYyxvQkFBZCxJQUFzQ2tHLHVCQUF1QmxJLEtBQUt1QixNQUE1QixDQURsQyxDQUFQO0FBRUQ7O0FBRUQsTUFBTTRHLFFBQVEsQ0FBQyxTQUFELEVBQVksVUFBWixFQUF3QixVQUF4QixFQUFvQyxTQUFwQyxFQUErQyxRQUEvQyxFQUF5RCxTQUF6RCxFQUFvRSxPQUFwRSxDQUFkOztBQUVBO0FBQ0E7QUFDQTtBQUNBLFNBQVNDLG9CQUFULENBQThCSixNQUE5QixFQUFzQztBQUNwQyxRQUFNSyxhQUFhTCxPQUFPakMsTUFBUCxDQUFjLFVBQVMxRSxHQUFULEVBQWNvRyxLQUFkLEVBQXFCYSxLQUFyQixFQUE0QjtBQUMzRCxRQUFJLE9BQU9iLEtBQVAsS0FBaUIsUUFBckIsRUFBK0I7QUFDN0JBLGNBQVEsQ0FBQ0EsS0FBRCxDQUFSO0FBQ0Q7QUFDREEsVUFBTTFDLE9BQU4sQ0FBYyxVQUFTd0QsU0FBVCxFQUFvQjtBQUNoQyxVQUFJSixNQUFNL0UsT0FBTixDQUFjbUYsU0FBZCxNQUE2QixDQUFDLENBQWxDLEVBQXFDO0FBQ25DLGNBQU0sSUFBSUMsS0FBSixDQUFVLHdEQUNkQyxLQUFLQyxTQUFMLENBQWVILFNBQWYsQ0FEYyxHQUNjLEdBRHhCLENBQU47QUFFRDtBQUNELFVBQUlsSCxJQUFJa0gsU0FBSixNQUFtQkksU0FBdkIsRUFBa0M7QUFDaEMsY0FBTSxJQUFJSCxLQUFKLENBQVUsMkNBQTJDRCxTQUEzQyxHQUF1RCxpQkFBakUsQ0FBTjtBQUNEO0FBQ0RsSCxVQUFJa0gsU0FBSixJQUFpQkQsS0FBakI7QUFDRCxLQVREO0FBVUEsV0FBT2pILEdBQVA7QUFDRCxHQWZrQixFQWVoQixFQWZnQixDQUFuQjs7QUFpQkEsUUFBTXVILGVBQWVULE1BQU1oSCxNQUFOLENBQWEsVUFBU2EsSUFBVCxFQUFlO0FBQy9DLFdBQU9xRyxXQUFXckcsSUFBWCxNQUFxQjJHLFNBQTVCO0FBQ0QsR0FGb0IsQ0FBckI7O0FBSUEsU0FBT0MsYUFBYTdDLE1BQWIsQ0FBb0IsVUFBUzFFLEdBQVQsRUFBY1csSUFBZCxFQUFvQjtBQUM3Q1gsUUFBSVcsSUFBSixJQUFZZ0csT0FBT2xILE1BQW5CO0FBQ0EsV0FBT08sR0FBUDtBQUNELEdBSE0sRUFHSmdILFVBSEksQ0FBUDtBQUlEOztBQUVELFNBQVNRLHlCQUFULENBQW1DMUIsVUFBbkMsRUFBK0M7QUFDN0MsUUFBTTJCLFFBQVEsRUFBZDtBQUNBLFFBQU1DLFNBQVMsRUFBZjs7QUFFQSxRQUFNQyxjQUFjN0IsV0FBV3ZILEdBQVgsQ0FBZSxDQUFDcUosU0FBRCxFQUFZWCxLQUFaLEtBQXNCO0FBQUEsVUFDL0NiLEtBRCtDLEdBQ1h3QixTQURXLENBQy9DeEIsS0FEK0M7QUFBQSxVQUM5QnlCLGNBRDhCLEdBQ1hELFNBRFcsQ0FDeEN2QixRQUR3Qzs7QUFFdkQsUUFBSUEsV0FBVyxDQUFmO0FBQ0EsUUFBSXdCLG1CQUFtQixPQUF2QixFQUFnQztBQUM5QixVQUFJLENBQUNKLE1BQU1yQixLQUFOLENBQUwsRUFBbUI7QUFDakJxQixjQUFNckIsS0FBTixJQUFlLENBQWY7QUFDRDtBQUNEQyxpQkFBV29CLE1BQU1yQixLQUFOLEdBQVg7QUFDRCxLQUxELE1BS08sSUFBSXlCLG1CQUFtQixRQUF2QixFQUFpQztBQUN0QyxVQUFJLENBQUNILE9BQU90QixLQUFQLENBQUwsRUFBb0I7QUFDbEJzQixlQUFPdEIsS0FBUCxJQUFnQixFQUFoQjtBQUNEO0FBQ0RzQixhQUFPdEIsS0FBUCxFQUFjakgsSUFBZCxDQUFtQjhILEtBQW5CO0FBQ0Q7O0FBRUQsV0FBT2xDLE9BQU8rQyxNQUFQLENBQWMsRUFBZCxFQUFrQkYsU0FBbEIsRUFBNkIsRUFBRXZCLFFBQUYsRUFBN0IsQ0FBUDtBQUNELEdBaEJtQixDQUFwQjs7QUFrQkEsTUFBSUwsY0FBYyxDQUFsQjs7QUFFQWpCLFNBQU9DLElBQVAsQ0FBWTBDLE1BQVosRUFBb0JoRSxPQUFwQixDQUE2QjBDLEtBQUQsSUFBVztBQUNyQyxVQUFNMkIsY0FBY0wsT0FBT3RCLEtBQVAsRUFBYzNHLE1BQWxDO0FBQ0FpSSxXQUFPdEIsS0FBUCxFQUFjMUMsT0FBZCxDQUFzQixDQUFDc0UsVUFBRCxFQUFhZixLQUFiLEtBQXVCO0FBQzNDVSxrQkFBWUssVUFBWixFQUF3QjNCLFFBQXhCLEdBQW1DLENBQUMsQ0FBRCxJQUFNMEIsY0FBY2QsS0FBcEIsQ0FBbkM7QUFDRCxLQUZEO0FBR0FqQixrQkFBY2lDLEtBQUtDLEdBQUwsQ0FBU2xDLFdBQVQsRUFBc0IrQixXQUF0QixDQUFkO0FBQ0QsR0FORDs7QUFRQWhELFNBQU9DLElBQVAsQ0FBWXlDLEtBQVosRUFBbUIvRCxPQUFuQixDQUE0QnlFLEdBQUQsSUFBUztBQUNsQyxVQUFNQyxvQkFBb0JYLE1BQU1VLEdBQU4sQ0FBMUI7QUFDQW5DLGtCQUFjaUMsS0FBS0MsR0FBTCxDQUFTbEMsV0FBVCxFQUFzQm9DLG9CQUFvQixDQUExQyxDQUFkO0FBQ0QsR0FIRDs7QUFLQSxTQUFPO0FBQ0x0QyxnQkFBWTZCLFdBRFA7QUFFTDNCLGlCQUFhQSxjQUFjLEVBQWQsR0FBbUJpQyxLQUFLSSxHQUFMLENBQVMsRUFBVCxFQUFhSixLQUFLSyxJQUFMLENBQVVMLEtBQUtNLEtBQUwsQ0FBV3ZDLFdBQVgsQ0FBVixDQUFiLENBQW5CLEdBQXNFO0FBRjlFLEdBQVA7QUFJRDs7QUFFRCxTQUFTd0MscUJBQVQsQ0FBK0JqRyxPQUEvQixFQUF3Q2tHLGNBQXhDLEVBQXdEO0FBQ3RELFFBQU1DLFdBQVd6SSxhQUFhd0ksZUFBZTlKLElBQTVCLENBQWpCO0FBQ0EsUUFBTTBCLG9CQUFvQmYscUJBQ3hCaUQsUUFBUUUsYUFBUixFQUR3QixFQUNDaUcsUUFERCxFQUNXcEksb0JBQW9Cb0ksUUFBcEIsQ0FEWCxDQUExQjs7QUFHQSxNQUFJQyxZQUFZRCxTQUFTbEksS0FBVCxDQUFlLENBQWYsQ0FBaEI7QUFDQSxNQUFJSCxrQkFBa0JaLE1BQWxCLEdBQTJCLENBQS9CLEVBQWtDO0FBQ2hDa0osZ0JBQVl0SSxrQkFBa0JBLGtCQUFrQlosTUFBbEIsR0FBMkIsQ0FBN0MsRUFBZ0RlLEtBQWhELENBQXNELENBQXRELENBQVo7QUFDRDtBQUNELFNBQVE4QyxLQUFELElBQVdBLE1BQU1zRixvQkFBTixDQUEyQixDQUFDRixTQUFTbEksS0FBVCxDQUFlLENBQWYsQ0FBRCxFQUFvQm1JLFNBQXBCLENBQTNCLEVBQTJELElBQTNELENBQWxCO0FBQ0Q7O0FBRUQsU0FBU0Usd0JBQVQsQ0FBa0N0RyxPQUFsQyxFQUEyQ3VHLGFBQTNDLEVBQTBETCxjQUExRCxFQUEwRTtBQUN4RSxRQUFNNUosYUFBYTBELFFBQVFFLGFBQVIsRUFBbkI7QUFDQSxRQUFNaUcsV0FBV3pJLGFBQWF3SSxlQUFlOUosSUFBNUIsQ0FBakI7QUFDQSxRQUFNb0ssV0FBVzlJLGFBQWE2SSxjQUFjbkssSUFBM0IsQ0FBakI7QUFDQSxRQUFNcUssZ0JBQWdCLENBQ3BCNUksMEJBQTBCdkIsVUFBMUIsRUFBc0M2SixRQUF0QyxDQURvQixFQUVwQjFILDRCQUE0Qm5DLFVBQTVCLEVBQXdDa0ssUUFBeEMsQ0FGb0IsQ0FBdEI7QUFJQSxNQUFJLFFBQVFFLElBQVIsQ0FBYXBLLFdBQVc0QixJQUFYLENBQWdCeUMsU0FBaEIsQ0FBMEI4RixjQUFjLENBQWQsQ0FBMUIsRUFBNENBLGNBQWMsQ0FBZCxDQUE1QyxDQUFiLENBQUosRUFBaUY7QUFDL0UsV0FBUTFGLEtBQUQsSUFBV0EsTUFBTTRGLFdBQU4sQ0FBa0JGLGFBQWxCLENBQWxCO0FBQ0Q7QUFDRCxTQUFPMUIsU0FBUDtBQUNEOztBQUVELFNBQVM2Qix5QkFBVCxDQUFvQzVHLE9BQXBDLEVBQTZDM0MsUUFBN0MsRUFBdUR3SixzQkFBdkQsRUFBK0U7QUFDN0UsUUFBTUMsK0JBQStCLENBQUNQLGFBQUQsRUFBZ0JMLGNBQWhCLEtBQW1DO0FBQ3RFLFVBQU1hLHNCQUFzQi9HLFFBQVFFLGFBQVIsR0FBd0I4RyxLQUF4QixDQUE4Qm5ILEtBQTlCLENBQzFCcUcsZUFBZTlKLElBQWYsQ0FBb0JpQyxHQUFwQixDQUF3QkcsR0FBeEIsQ0FBNEJELElBREYsRUFFMUJnSSxjQUFjbkssSUFBZCxDQUFtQmlDLEdBQW5CLENBQXVCQyxLQUF2QixDQUE2QkMsSUFBN0IsR0FBb0MsQ0FGVixDQUE1Qjs7QUFLQSxXQUFPd0ksb0JBQW9CeEosTUFBcEIsQ0FBNEJnQixJQUFELElBQVUsQ0FBQ0EsS0FBSzBJLElBQUwsR0FBWS9KLE1BQWxELEVBQTBEQSxNQUFqRTtBQUNELEdBUEQ7QUFRQSxNQUFJZ0osaUJBQWlCN0ksU0FBUyxDQUFULENBQXJCOztBQUVBQSxXQUFTd0MsS0FBVCxDQUFlLENBQWYsRUFBa0JzQixPQUFsQixDQUEwQixVQUFTb0YsYUFBVCxFQUF3QjtBQUNoRCxVQUFNVyxvQkFBb0JKLDZCQUE2QlAsYUFBN0IsRUFBNENMLGNBQTVDLENBQTFCOztBQUVBLFFBQUlXLDJCQUEyQixRQUEzQixJQUNHQSwyQkFBMkIsMEJBRGxDLEVBQzhEO0FBQzVELFVBQUlOLGNBQWNwSyxJQUFkLEtBQXVCK0osZUFBZS9KLElBQXRDLElBQThDK0ssc0JBQXNCLENBQXhFLEVBQTJFO0FBQ3pFbEgsZ0JBQVFhLE1BQVIsQ0FBZTtBQUNiekUsZ0JBQU04SixlQUFlOUosSUFEUjtBQUVid0UsbUJBQVMsK0RBRkk7QUFHYkUsZUFBS21GLHNCQUFzQmpHLE9BQXRCLEVBQStCa0csY0FBL0I7QUFIUSxTQUFmO0FBS0QsT0FORCxNQU1PLElBQUlLLGNBQWNwSyxJQUFkLEtBQXVCK0osZUFBZS9KLElBQXRDLElBQ04rSyxvQkFBb0IsQ0FEZCxJQUVOTCwyQkFBMkIsMEJBRnpCLEVBRXFEO0FBQzFEN0csZ0JBQVFhLE1BQVIsQ0FBZTtBQUNiekUsZ0JBQU04SixlQUFlOUosSUFEUjtBQUVid0UsbUJBQVMsbURBRkk7QUFHYkUsZUFBS3dGLHlCQUF5QnRHLE9BQXpCLEVBQWtDdUcsYUFBbEMsRUFBaURMLGNBQWpEO0FBSFEsU0FBZjtBQUtEO0FBQ0YsS0FqQkQsTUFpQk8sSUFBSWdCLG9CQUFvQixDQUF4QixFQUEyQjtBQUNoQ2xILGNBQVFhLE1BQVIsQ0FBZTtBQUNiekUsY0FBTThKLGVBQWU5SixJQURSO0FBRWJ3RSxpQkFBUyxxREFGSTtBQUdiRSxhQUFLd0YseUJBQXlCdEcsT0FBekIsRUFBa0N1RyxhQUFsQyxFQUFpREwsY0FBakQ7QUFIUSxPQUFmO0FBS0Q7O0FBRURBLHFCQUFpQkssYUFBakI7QUFDRCxHQTdCRDtBQThCRDs7QUFFRCxTQUFTWSxvQkFBVCxDQUE4QkMsT0FBOUIsRUFBdUM7QUFDckMsUUFBTUMsY0FBY0QsUUFBUUMsV0FBUixJQUF1QixFQUEzQztBQUNBLFFBQU1wSCxRQUFRb0gsWUFBWXBILEtBQVosSUFBcUIsUUFBbkM7QUFDQSxRQUFNMkMsa0JBQWtCeUUsWUFBWXpFLGVBQVosSUFBK0IsS0FBdkQ7O0FBRUEsU0FBTyxFQUFDM0MsS0FBRCxFQUFRMkMsZUFBUixFQUFQO0FBQ0Q7O0FBRUQwRSxPQUFPQyxPQUFQLEdBQWlCO0FBQ2ZDLFFBQU07QUFDSnBKLFVBQU0sWUFERjtBQUVKcUosVUFBTTtBQUNKQyxXQUFLLHVCQUFRLE9BQVI7QUFERCxLQUZGOztBQU1KQyxhQUFTLE1BTkw7QUFPSkMsWUFBUSxDQUNOO0FBQ0V4SixZQUFNLFFBRFI7QUFFRXlKLGtCQUFZO0FBQ1Z6RCxnQkFBUTtBQUNOaEcsZ0JBQU07QUFEQSxTQURFO0FBSVYwSix1Q0FBK0I7QUFDN0IxSixnQkFBTTtBQUR1QixTQUpyQjtBQU9WbUYsb0JBQVk7QUFDVm5GLGdCQUFNLE9BREk7QUFFVjJKLGlCQUFPO0FBQ0wzSixrQkFBTSxRQUREO0FBRUx5Six3QkFBWTtBQUNWbEUsdUJBQVM7QUFDUHZGLHNCQUFNO0FBREMsZUFEQztBQUlWd0YsOEJBQWdCO0FBQ2R4RixzQkFBTTtBQURRLGVBSk47QUFPVnlGLHFCQUFPO0FBQ0x6RixzQkFBTSxRQUREO0FBRUw0SixzQkFBTXpEO0FBRkQsZUFQRztBQVdWVCx3QkFBVTtBQUNSMUYsc0JBQU0sUUFERTtBQUVSNEosc0JBQU0sQ0FBQyxPQUFELEVBQVUsUUFBVjtBQUZFO0FBWEEsYUFGUDtBQWtCTEMsc0JBQVUsQ0FBQyxTQUFELEVBQVksT0FBWjtBQWxCTDtBQUZHLFNBUEY7QUE4QlYsNEJBQW9CO0FBQ2xCRCxnQkFBTSxDQUNKLFFBREksRUFFSixRQUZJLEVBR0osMEJBSEksRUFJSixPQUpJO0FBRFksU0E5QlY7QUFzQ1ZYLHFCQUFhO0FBQ1hqSixnQkFBTSxRQURLO0FBRVh5SixzQkFBWTtBQUNWakYsNkJBQWlCO0FBQ2Z4RSxvQkFBTSxTQURTO0FBRWY4Six1QkFBUztBQUZNLGFBRFA7QUFLVmpJLG1CQUFPO0FBQ0wrSCxvQkFBTSxDQUFDLFFBQUQsRUFBVyxLQUFYLEVBQWtCLE1BQWxCLENBREQ7QUFFTEUsdUJBQVM7QUFGSjtBQUxHLFdBRkQ7QUFZWEMsZ0NBQXNCO0FBWlg7QUF0Q0gsT0FGZDtBQXVERUEsNEJBQXNCO0FBdkR4QixLQURNO0FBUEosR0FEUzs7QUFxRWZDLFVBQVEsU0FBU0MsZUFBVCxDQUEwQnJJLE9BQTFCLEVBQW1DO0FBQ3pDLFVBQU1vSCxVQUFVcEgsUUFBUW9ILE9BQVIsQ0FBZ0IsQ0FBaEIsS0FBc0IsRUFBdEM7QUFDQSxVQUFNUCx5QkFBeUJPLFFBQVEsa0JBQVIsS0FBK0IsUUFBOUQ7QUFDQSxVQUFNVSxnQ0FBZ0MsSUFBSVEsR0FBSixDQUFRbEIsUUFBUSwrQkFBUixLQUE0QyxDQUFDLFNBQUQsRUFBWSxVQUFaLENBQXBELENBQXRDO0FBQ0EsVUFBTUMsY0FBY0YscUJBQXFCQyxPQUFyQixDQUFwQjtBQUNBLFFBQUk5RCxLQUFKOztBQUVBLFFBQUk7QUFBQSxrQ0FDa0MyQiwwQkFBMEJtQyxRQUFRN0QsVUFBUixJQUFzQixFQUFoRCxDQURsQzs7QUFBQSxZQUNNQSxVQUROLHlCQUNNQSxVQUROO0FBQUEsWUFDa0JFLFdBRGxCLHlCQUNrQkEsV0FEbEI7O0FBRUZILGNBQVE7QUFDTmMsZ0JBQVFJLHFCQUFxQjRDLFFBQVFoRCxNQUFSLElBQWtCdkksYUFBdkMsQ0FERjtBQUVOMEgsa0JBRk07QUFHTkU7QUFITSxPQUFSO0FBS0QsS0FQRCxDQU9FLE9BQU84RSxLQUFQLEVBQWM7QUFDZDtBQUNBLGFBQU87QUFDTEMsaUJBQVMsVUFBU3BNLElBQVQsRUFBZTtBQUN0QjRELGtCQUFRYSxNQUFSLENBQWV6RSxJQUFmLEVBQXFCbU0sTUFBTTNILE9BQTNCO0FBQ0Q7QUFISSxPQUFQO0FBS0Q7QUFDRCxRQUFJdkQsV0FBVyxFQUFmO0FBQ0EsUUFBSW9MLFFBQVEsQ0FBWjs7QUFFQSxhQUFTQyxjQUFULEdBQTBCO0FBQ3hCRDtBQUNEO0FBQ0QsYUFBU0UsY0FBVCxHQUEwQjtBQUN4QkY7QUFDRDs7QUFFRCxXQUFPO0FBQ0xHLHlCQUFtQixTQUFTQyxhQUFULENBQXVCek0sSUFBdkIsRUFBNkI7QUFDOUMsWUFBSUEsS0FBSytDLFVBQUwsQ0FBZ0JqQyxNQUFwQixFQUE0QjtBQUFFO0FBQzVCLGdCQUFNaEIsT0FBT0UsS0FBSzBNLE1BQUwsQ0FBWUMsS0FBekI7QUFDQTFFLHVCQUNFckUsT0FERixFQUVFNUQsSUFGRixFQUdFRixJQUhGLEVBSUUsUUFKRixFQUtFb0gsS0FMRixFQU1FakcsUUFORixFQU9FeUssNkJBUEY7QUFTRDtBQUNGLE9BZEk7QUFlTGtCLHNCQUFnQixTQUFTQyxjQUFULENBQXdCN00sSUFBeEIsRUFBOEI7QUFDNUMsWUFBSXFNLFVBQVUsQ0FBVixJQUFlLENBQUMsNkJBQWdCck0sSUFBaEIsQ0FBaEIsSUFBeUMsQ0FBQ2tJLHVCQUF1QmxJLEtBQUt1QixNQUE1QixDQUE5QyxFQUFtRjtBQUNqRjtBQUNEO0FBQ0QsY0FBTXpCLE9BQU9FLEtBQUs2QyxTQUFMLENBQWUsQ0FBZixFQUFrQjhKLEtBQS9CO0FBQ0ExRSxxQkFDRXJFLE9BREYsRUFFRTVELElBRkYsRUFHRUYsSUFIRixFQUlFLFNBSkYsRUFLRW9ILEtBTEYsRUFNRWpHLFFBTkYsRUFPRXlLLDZCQVBGO0FBU0QsT0E3Qkk7QUE4Qkwsc0JBQWdCLFNBQVNvQixjQUFULEdBQTBCO0FBQ3hDLFlBQUlyQywyQkFBMkIsUUFBL0IsRUFBeUM7QUFDdkNELG9DQUEwQjVHLE9BQTFCLEVBQW1DM0MsUUFBbkMsRUFBNkN3SixzQkFBN0M7QUFDRDs7QUFFRCxZQUFJUSxZQUFZcEgsS0FBWixLQUFzQixRQUExQixFQUFvQztBQUNsQytCLG1DQUF5QjNFLFFBQXpCLEVBQW1DZ0ssV0FBbkM7QUFDRDs7QUFFRDVGLDZCQUFxQnpCLE9BQXJCLEVBQThCM0MsUUFBOUI7O0FBRUFBLG1CQUFXLEVBQVg7QUFDRCxPQTFDSTtBQTJDTDhMLDJCQUFxQlQsY0EzQ2hCO0FBNENMVSwwQkFBb0JWLGNBNUNmO0FBNkNMVywrQkFBeUJYLGNBN0NwQjtBQThDTFksc0JBQWdCWixjQTlDWDtBQStDTGEsd0JBQWtCYixjQS9DYjtBQWdETCxrQ0FBNEJDLGNBaER2QjtBQWlETCxpQ0FBMkJBLGNBakR0QjtBQWtETCxzQ0FBZ0NBLGNBbEQzQjtBQW1ETCw2QkFBdUJBLGNBbkRsQjtBQW9ETCwrQkFBeUJBO0FBcERwQixLQUFQO0FBc0REO0FBM0pjLENBQWpCIiwiZmlsZSI6Im9yZGVyLmpzIiwic291cmNlc0NvbnRlbnQiOlsiJ3VzZSBzdHJpY3QnXG5cbmltcG9ydCBtaW5pbWF0Y2ggZnJvbSAnbWluaW1hdGNoJ1xuaW1wb3J0IGltcG9ydFR5cGUgZnJvbSAnLi4vY29yZS9pbXBvcnRUeXBlJ1xuaW1wb3J0IGlzU3RhdGljUmVxdWlyZSBmcm9tICcuLi9jb3JlL3N0YXRpY1JlcXVpcmUnXG5pbXBvcnQgZG9jc1VybCBmcm9tICcuLi9kb2NzVXJsJ1xuXG5jb25zdCBkZWZhdWx0R3JvdXBzID0gWydidWlsdGluJywgJ2V4dGVybmFsJywgJ3BhcmVudCcsICdzaWJsaW5nJywgJ2luZGV4J11cblxuLy8gUkVQT1JUSU5HIEFORCBGSVhJTkdcblxuZnVuY3Rpb24gcmV2ZXJzZShhcnJheSkge1xuICByZXR1cm4gYXJyYXkubWFwKGZ1bmN0aW9uICh2KSB7XG4gICAgcmV0dXJuIHtcbiAgICAgIG5hbWU6IHYubmFtZSxcbiAgICAgIHJhbms6IC12LnJhbmssXG4gICAgICBub2RlOiB2Lm5vZGUsXG4gICAgfVxuICB9KS5yZXZlcnNlKClcbn1cblxuZnVuY3Rpb24gZ2V0VG9rZW5zT3JDb21tZW50c0FmdGVyKHNvdXJjZUNvZGUsIG5vZGUsIGNvdW50KSB7XG4gIGxldCBjdXJyZW50Tm9kZU9yVG9rZW4gPSBub2RlXG4gIGNvbnN0IHJlc3VsdCA9IFtdXG4gIGZvciAobGV0IGkgPSAwOyBpIDwgY291bnQ7IGkrKykge1xuICAgIGN1cnJlbnROb2RlT3JUb2tlbiA9IHNvdXJjZUNvZGUuZ2V0VG9rZW5PckNvbW1lbnRBZnRlcihjdXJyZW50Tm9kZU9yVG9rZW4pXG4gICAgaWYgKGN1cnJlbnROb2RlT3JUb2tlbiA9PSBudWxsKSB7XG4gICAgICBicmVha1xuICAgIH1cbiAgICByZXN1bHQucHVzaChjdXJyZW50Tm9kZU9yVG9rZW4pXG4gIH1cbiAgcmV0dXJuIHJlc3VsdFxufVxuXG5mdW5jdGlvbiBnZXRUb2tlbnNPckNvbW1lbnRzQmVmb3JlKHNvdXJjZUNvZGUsIG5vZGUsIGNvdW50KSB7XG4gIGxldCBjdXJyZW50Tm9kZU9yVG9rZW4gPSBub2RlXG4gIGNvbnN0IHJlc3VsdCA9IFtdXG4gIGZvciAobGV0IGkgPSAwOyBpIDwgY291bnQ7IGkrKykge1xuICAgIGN1cnJlbnROb2RlT3JUb2tlbiA9IHNvdXJjZUNvZGUuZ2V0VG9rZW5PckNvbW1lbnRCZWZvcmUoY3VycmVudE5vZGVPclRva2VuKVxuICAgIGlmIChjdXJyZW50Tm9kZU9yVG9rZW4gPT0gbnVsbCkge1xuICAgICAgYnJlYWtcbiAgICB9XG4gICAgcmVzdWx0LnB1c2goY3VycmVudE5vZGVPclRva2VuKVxuICB9XG4gIHJldHVybiByZXN1bHQucmV2ZXJzZSgpXG59XG5cbmZ1bmN0aW9uIHRha2VUb2tlbnNBZnRlcldoaWxlKHNvdXJjZUNvZGUsIG5vZGUsIGNvbmRpdGlvbikge1xuICBjb25zdCB0b2tlbnMgPSBnZXRUb2tlbnNPckNvbW1lbnRzQWZ0ZXIoc291cmNlQ29kZSwgbm9kZSwgMTAwKVxuICBjb25zdCByZXN1bHQgPSBbXVxuICBmb3IgKGxldCBpID0gMDsgaSA8IHRva2Vucy5sZW5ndGg7IGkrKykge1xuICAgIGlmIChjb25kaXRpb24odG9rZW5zW2ldKSkge1xuICAgICAgcmVzdWx0LnB1c2godG9rZW5zW2ldKVxuICAgIH1cbiAgICBlbHNlIHtcbiAgICAgIGJyZWFrXG4gICAgfVxuICB9XG4gIHJldHVybiByZXN1bHRcbn1cblxuZnVuY3Rpb24gdGFrZVRva2Vuc0JlZm9yZVdoaWxlKHNvdXJjZUNvZGUsIG5vZGUsIGNvbmRpdGlvbikge1xuICBjb25zdCB0b2tlbnMgPSBnZXRUb2tlbnNPckNvbW1lbnRzQmVmb3JlKHNvdXJjZUNvZGUsIG5vZGUsIDEwMClcbiAgY29uc3QgcmVzdWx0ID0gW11cbiAgZm9yIChsZXQgaSA9IHRva2Vucy5sZW5ndGggLSAxOyBpID49IDA7IGktLSkge1xuICAgIGlmIChjb25kaXRpb24odG9rZW5zW2ldKSkge1xuICAgICAgcmVzdWx0LnB1c2godG9rZW5zW2ldKVxuICAgIH1cbiAgICBlbHNlIHtcbiAgICAgIGJyZWFrXG4gICAgfVxuICB9XG4gIHJldHVybiByZXN1bHQucmV2ZXJzZSgpXG59XG5cbmZ1bmN0aW9uIGZpbmRPdXRPZk9yZGVyKGltcG9ydGVkKSB7XG4gIGlmIChpbXBvcnRlZC5sZW5ndGggPT09IDApIHtcbiAgICByZXR1cm4gW11cbiAgfVxuICBsZXQgbWF4U2VlblJhbmtOb2RlID0gaW1wb3J0ZWRbMF1cbiAgcmV0dXJuIGltcG9ydGVkLmZpbHRlcihmdW5jdGlvbiAoaW1wb3J0ZWRNb2R1bGUpIHtcbiAgICBjb25zdCByZXMgPSBpbXBvcnRlZE1vZHVsZS5yYW5rIDwgbWF4U2VlblJhbmtOb2RlLnJhbmtcbiAgICBpZiAobWF4U2VlblJhbmtOb2RlLnJhbmsgPCBpbXBvcnRlZE1vZHVsZS5yYW5rKSB7XG4gICAgICBtYXhTZWVuUmFua05vZGUgPSBpbXBvcnRlZE1vZHVsZVxuICAgIH1cbiAgICByZXR1cm4gcmVzXG4gIH0pXG59XG5cbmZ1bmN0aW9uIGZpbmRSb290Tm9kZShub2RlKSB7XG4gIGxldCBwYXJlbnQgPSBub2RlXG4gIHdoaWxlIChwYXJlbnQucGFyZW50ICE9IG51bGwgJiYgcGFyZW50LnBhcmVudC5ib2R5ID09IG51bGwpIHtcbiAgICBwYXJlbnQgPSBwYXJlbnQucGFyZW50XG4gIH1cbiAgcmV0dXJuIHBhcmVudFxufVxuXG5mdW5jdGlvbiBmaW5kRW5kT2ZMaW5lV2l0aENvbW1lbnRzKHNvdXJjZUNvZGUsIG5vZGUpIHtcbiAgY29uc3QgdG9rZW5zVG9FbmRPZkxpbmUgPSB0YWtlVG9rZW5zQWZ0ZXJXaGlsZShzb3VyY2VDb2RlLCBub2RlLCBjb21tZW50T25TYW1lTGluZUFzKG5vZGUpKVxuICBsZXQgZW5kT2ZUb2tlbnMgPSB0b2tlbnNUb0VuZE9mTGluZS5sZW5ndGggPiAwXG4gICAgPyB0b2tlbnNUb0VuZE9mTGluZVt0b2tlbnNUb0VuZE9mTGluZS5sZW5ndGggLSAxXS5yYW5nZVsxXVxuICAgIDogbm9kZS5yYW5nZVsxXVxuICBsZXQgcmVzdWx0ID0gZW5kT2ZUb2tlbnNcbiAgZm9yIChsZXQgaSA9IGVuZE9mVG9rZW5zOyBpIDwgc291cmNlQ29kZS50ZXh0Lmxlbmd0aDsgaSsrKSB7XG4gICAgaWYgKHNvdXJjZUNvZGUudGV4dFtpXSA9PT0gJ1xcbicpIHtcbiAgICAgIHJlc3VsdCA9IGkgKyAxXG4gICAgICBicmVha1xuICAgIH1cbiAgICBpZiAoc291cmNlQ29kZS50ZXh0W2ldICE9PSAnICcgJiYgc291cmNlQ29kZS50ZXh0W2ldICE9PSAnXFx0JyAmJiBzb3VyY2VDb2RlLnRleHRbaV0gIT09ICdcXHInKSB7XG4gICAgICBicmVha1xuICAgIH1cbiAgICByZXN1bHQgPSBpICsgMVxuICB9XG4gIHJldHVybiByZXN1bHRcbn1cblxuZnVuY3Rpb24gY29tbWVudE9uU2FtZUxpbmVBcyhub2RlKSB7XG4gIHJldHVybiB0b2tlbiA9PiAodG9rZW4udHlwZSA9PT0gJ0Jsb2NrJyB8fCAgdG9rZW4udHlwZSA9PT0gJ0xpbmUnKSAmJlxuICAgICAgdG9rZW4ubG9jLnN0YXJ0LmxpbmUgPT09IHRva2VuLmxvYy5lbmQubGluZSAmJlxuICAgICAgdG9rZW4ubG9jLmVuZC5saW5lID09PSBub2RlLmxvYy5lbmQubGluZVxufVxuXG5mdW5jdGlvbiBmaW5kU3RhcnRPZkxpbmVXaXRoQ29tbWVudHMoc291cmNlQ29kZSwgbm9kZSkge1xuICBjb25zdCB0b2tlbnNUb0VuZE9mTGluZSA9IHRha2VUb2tlbnNCZWZvcmVXaGlsZShzb3VyY2VDb2RlLCBub2RlLCBjb21tZW50T25TYW1lTGluZUFzKG5vZGUpKVxuICBsZXQgc3RhcnRPZlRva2VucyA9IHRva2Vuc1RvRW5kT2ZMaW5lLmxlbmd0aCA+IDAgPyB0b2tlbnNUb0VuZE9mTGluZVswXS5yYW5nZVswXSA6IG5vZGUucmFuZ2VbMF1cbiAgbGV0IHJlc3VsdCA9IHN0YXJ0T2ZUb2tlbnNcbiAgZm9yIChsZXQgaSA9IHN0YXJ0T2ZUb2tlbnMgLSAxOyBpID4gMDsgaS0tKSB7XG4gICAgaWYgKHNvdXJjZUNvZGUudGV4dFtpXSAhPT0gJyAnICYmIHNvdXJjZUNvZGUudGV4dFtpXSAhPT0gJ1xcdCcpIHtcbiAgICAgIGJyZWFrXG4gICAgfVxuICAgIHJlc3VsdCA9IGlcbiAgfVxuICByZXR1cm4gcmVzdWx0XG59XG5cbmZ1bmN0aW9uIGlzUGxhaW5SZXF1aXJlTW9kdWxlKG5vZGUpIHtcbiAgaWYgKG5vZGUudHlwZSAhPT0gJ1ZhcmlhYmxlRGVjbGFyYXRpb24nKSB7XG4gICAgcmV0dXJuIGZhbHNlXG4gIH1cbiAgaWYgKG5vZGUuZGVjbGFyYXRpb25zLmxlbmd0aCAhPT0gMSkge1xuICAgIHJldHVybiBmYWxzZVxuICB9XG4gIGNvbnN0IGRlY2wgPSBub2RlLmRlY2xhcmF0aW9uc1swXVxuICBjb25zdCByZXN1bHQgPSBkZWNsLmlkICYmXG4gICAgKGRlY2wuaWQudHlwZSA9PT0gJ0lkZW50aWZpZXInIHx8IGRlY2wuaWQudHlwZSA9PT0gJ09iamVjdFBhdHRlcm4nKSAmJlxuICAgIGRlY2wuaW5pdCAhPSBudWxsICYmXG4gICAgZGVjbC5pbml0LnR5cGUgPT09ICdDYWxsRXhwcmVzc2lvbicgJiZcbiAgICBkZWNsLmluaXQuY2FsbGVlICE9IG51bGwgJiZcbiAgICBkZWNsLmluaXQuY2FsbGVlLm5hbWUgPT09ICdyZXF1aXJlJyAmJlxuICAgIGRlY2wuaW5pdC5hcmd1bWVudHMgIT0gbnVsbCAmJlxuICAgIGRlY2wuaW5pdC5hcmd1bWVudHMubGVuZ3RoID09PSAxICYmXG4gICAgZGVjbC5pbml0LmFyZ3VtZW50c1swXS50eXBlID09PSAnTGl0ZXJhbCdcbiAgcmV0dXJuIHJlc3VsdFxufVxuXG5mdW5jdGlvbiBpc1BsYWluSW1wb3J0TW9kdWxlKG5vZGUpIHtcbiAgcmV0dXJuIG5vZGUudHlwZSA9PT0gJ0ltcG9ydERlY2xhcmF0aW9uJyAmJiBub2RlLnNwZWNpZmllcnMgIT0gbnVsbCAmJiBub2RlLnNwZWNpZmllcnMubGVuZ3RoID4gMFxufVxuXG5mdW5jdGlvbiBjYW5Dcm9zc05vZGVXaGlsZVJlb3JkZXIobm9kZSkge1xuICByZXR1cm4gaXNQbGFpblJlcXVpcmVNb2R1bGUobm9kZSkgfHwgaXNQbGFpbkltcG9ydE1vZHVsZShub2RlKVxufVxuXG5mdW5jdGlvbiBjYW5SZW9yZGVySXRlbXMoZmlyc3ROb2RlLCBzZWNvbmROb2RlKSB7XG4gIGNvbnN0IHBhcmVudCA9IGZpcnN0Tm9kZS5wYXJlbnRcbiAgY29uc3QgW2ZpcnN0SW5kZXgsIHNlY29uZEluZGV4XSA9IFtcbiAgICBwYXJlbnQuYm9keS5pbmRleE9mKGZpcnN0Tm9kZSksXG4gICAgcGFyZW50LmJvZHkuaW5kZXhPZihzZWNvbmROb2RlKSxcbiAgXS5zb3J0KClcbiAgY29uc3Qgbm9kZXNCZXR3ZWVuID0gcGFyZW50LmJvZHkuc2xpY2UoZmlyc3RJbmRleCwgc2Vjb25kSW5kZXggKyAxKVxuICBmb3IgKHZhciBub2RlQmV0d2VlbiBvZiBub2Rlc0JldHdlZW4pIHtcbiAgICBpZiAoIWNhbkNyb3NzTm9kZVdoaWxlUmVvcmRlcihub2RlQmV0d2VlbikpIHtcbiAgICAgIHJldHVybiBmYWxzZVxuICAgIH1cbiAgfVxuICByZXR1cm4gdHJ1ZVxufVxuXG5mdW5jdGlvbiBmaXhPdXRPZk9yZGVyKGNvbnRleHQsIGZpcnN0Tm9kZSwgc2Vjb25kTm9kZSwgb3JkZXIpIHtcbiAgY29uc3Qgc291cmNlQ29kZSA9IGNvbnRleHQuZ2V0U291cmNlQ29kZSgpXG5cbiAgY29uc3QgZmlyc3RSb290ID0gZmluZFJvb3ROb2RlKGZpcnN0Tm9kZS5ub2RlKVxuICBjb25zdCBmaXJzdFJvb3RTdGFydCA9IGZpbmRTdGFydE9mTGluZVdpdGhDb21tZW50cyhzb3VyY2VDb2RlLCBmaXJzdFJvb3QpXG4gIGNvbnN0IGZpcnN0Um9vdEVuZCA9IGZpbmRFbmRPZkxpbmVXaXRoQ29tbWVudHMoc291cmNlQ29kZSwgZmlyc3RSb290KVxuXG4gIGNvbnN0IHNlY29uZFJvb3QgPSBmaW5kUm9vdE5vZGUoc2Vjb25kTm9kZS5ub2RlKVxuICBjb25zdCBzZWNvbmRSb290U3RhcnQgPSBmaW5kU3RhcnRPZkxpbmVXaXRoQ29tbWVudHMoc291cmNlQ29kZSwgc2Vjb25kUm9vdClcbiAgY29uc3Qgc2Vjb25kUm9vdEVuZCA9IGZpbmRFbmRPZkxpbmVXaXRoQ29tbWVudHMoc291cmNlQ29kZSwgc2Vjb25kUm9vdClcbiAgY29uc3QgY2FuRml4ID0gY2FuUmVvcmRlckl0ZW1zKGZpcnN0Um9vdCwgc2Vjb25kUm9vdClcblxuICBsZXQgbmV3Q29kZSA9IHNvdXJjZUNvZGUudGV4dC5zdWJzdHJpbmcoc2Vjb25kUm9vdFN0YXJ0LCBzZWNvbmRSb290RW5kKVxuICBpZiAobmV3Q29kZVtuZXdDb2RlLmxlbmd0aCAtIDFdICE9PSAnXFxuJykge1xuICAgIG5ld0NvZGUgPSBuZXdDb2RlICsgJ1xcbidcbiAgfVxuXG4gIGNvbnN0IG1lc3NhZ2UgPSAnYCcgKyBzZWNvbmROb2RlLm5hbWUgKyAnYCBpbXBvcnQgc2hvdWxkIG9jY3VyICcgKyBvcmRlciArXG4gICAgICAnIGltcG9ydCBvZiBgJyArIGZpcnN0Tm9kZS5uYW1lICsgJ2AnXG5cbiAgaWYgKG9yZGVyID09PSAnYmVmb3JlJykge1xuICAgIGNvbnRleHQucmVwb3J0KHtcbiAgICAgIG5vZGU6IHNlY29uZE5vZGUubm9kZSxcbiAgICAgIG1lc3NhZ2U6IG1lc3NhZ2UsXG4gICAgICBmaXg6IGNhbkZpeCAmJiAoZml4ZXIgPT5cbiAgICAgICAgZml4ZXIucmVwbGFjZVRleHRSYW5nZShcbiAgICAgICAgICBbZmlyc3RSb290U3RhcnQsIHNlY29uZFJvb3RFbmRdLFxuICAgICAgICAgIG5ld0NvZGUgKyBzb3VyY2VDb2RlLnRleHQuc3Vic3RyaW5nKGZpcnN0Um9vdFN0YXJ0LCBzZWNvbmRSb290U3RhcnQpXG4gICAgICAgICkpLFxuICAgIH0pXG4gIH0gZWxzZSBpZiAob3JkZXIgPT09ICdhZnRlcicpIHtcbiAgICBjb250ZXh0LnJlcG9ydCh7XG4gICAgICBub2RlOiBzZWNvbmROb2RlLm5vZGUsXG4gICAgICBtZXNzYWdlOiBtZXNzYWdlLFxuICAgICAgZml4OiBjYW5GaXggJiYgKGZpeGVyID0+XG4gICAgICAgIGZpeGVyLnJlcGxhY2VUZXh0UmFuZ2UoXG4gICAgICAgICAgW3NlY29uZFJvb3RTdGFydCwgZmlyc3RSb290RW5kXSxcbiAgICAgICAgICBzb3VyY2VDb2RlLnRleHQuc3Vic3RyaW5nKHNlY29uZFJvb3RFbmQsIGZpcnN0Um9vdEVuZCkgKyBuZXdDb2RlXG4gICAgICAgICkpLFxuICAgIH0pXG4gIH1cbn1cblxuZnVuY3Rpb24gcmVwb3J0T3V0T2ZPcmRlcihjb250ZXh0LCBpbXBvcnRlZCwgb3V0T2ZPcmRlciwgb3JkZXIpIHtcbiAgb3V0T2ZPcmRlci5mb3JFYWNoKGZ1bmN0aW9uIChpbXApIHtcbiAgICBjb25zdCBmb3VuZCA9IGltcG9ydGVkLmZpbmQoZnVuY3Rpb24gaGFzSGlnaGVyUmFuayhpbXBvcnRlZEl0ZW0pIHtcbiAgICAgIHJldHVybiBpbXBvcnRlZEl0ZW0ucmFuayA+IGltcC5yYW5rXG4gICAgfSlcbiAgICBmaXhPdXRPZk9yZGVyKGNvbnRleHQsIGZvdW5kLCBpbXAsIG9yZGVyKVxuICB9KVxufVxuXG5mdW5jdGlvbiBtYWtlT3V0T2ZPcmRlclJlcG9ydChjb250ZXh0LCBpbXBvcnRlZCkge1xuICBjb25zdCBvdXRPZk9yZGVyID0gZmluZE91dE9mT3JkZXIoaW1wb3J0ZWQpXG4gIGlmICghb3V0T2ZPcmRlci5sZW5ndGgpIHtcbiAgICByZXR1cm5cbiAgfVxuICAvLyBUaGVyZSBhcmUgdGhpbmdzIHRvIHJlcG9ydC4gVHJ5IHRvIG1pbmltaXplIHRoZSBudW1iZXIgb2YgcmVwb3J0ZWQgZXJyb3JzLlxuICBjb25zdCByZXZlcnNlZEltcG9ydGVkID0gcmV2ZXJzZShpbXBvcnRlZClcbiAgY29uc3QgcmV2ZXJzZWRPcmRlciA9IGZpbmRPdXRPZk9yZGVyKHJldmVyc2VkSW1wb3J0ZWQpXG4gIGlmIChyZXZlcnNlZE9yZGVyLmxlbmd0aCA8IG91dE9mT3JkZXIubGVuZ3RoKSB7XG4gICAgcmVwb3J0T3V0T2ZPcmRlcihjb250ZXh0LCByZXZlcnNlZEltcG9ydGVkLCByZXZlcnNlZE9yZGVyLCAnYWZ0ZXInKVxuICAgIHJldHVyblxuICB9XG4gIHJlcG9ydE91dE9mT3JkZXIoY29udGV4dCwgaW1wb3J0ZWQsIG91dE9mT3JkZXIsICdiZWZvcmUnKVxufVxuXG5mdW5jdGlvbiBpbXBvcnRzU29ydGVyQXNjKGltcG9ydEEsIGltcG9ydEIpIHtcbiAgaWYgKGltcG9ydEEgPCBpbXBvcnRCKSB7XG4gICAgcmV0dXJuIC0xXG4gIH1cblxuICBpZiAoaW1wb3J0QSA+IGltcG9ydEIpIHtcbiAgICByZXR1cm4gMVxuICB9XG5cbiAgcmV0dXJuIDBcbn1cblxuZnVuY3Rpb24gaW1wb3J0c1NvcnRlckRlc2MoaW1wb3J0QSwgaW1wb3J0Qikge1xuICBpZiAoaW1wb3J0QSA8IGltcG9ydEIpIHtcbiAgICByZXR1cm4gMVxuICB9XG5cbiAgaWYgKGltcG9ydEEgPiBpbXBvcnRCKSB7XG4gICAgcmV0dXJuIC0xXG4gIH1cblxuICByZXR1cm4gMFxufVxuXG5mdW5jdGlvbiBtdXRhdGVSYW5rc1RvQWxwaGFiZXRpemUoaW1wb3J0ZWQsIGFscGhhYmV0aXplT3B0aW9ucykge1xuICBjb25zdCBncm91cGVkQnlSYW5rcyA9IGltcG9ydGVkLnJlZHVjZShmdW5jdGlvbihhY2MsIGltcG9ydGVkSXRlbSkge1xuICAgIGlmICghQXJyYXkuaXNBcnJheShhY2NbaW1wb3J0ZWRJdGVtLnJhbmtdKSkge1xuICAgICAgYWNjW2ltcG9ydGVkSXRlbS5yYW5rXSA9IFtdXG4gICAgfVxuICAgIGFjY1tpbXBvcnRlZEl0ZW0ucmFua10ucHVzaChpbXBvcnRlZEl0ZW0ubmFtZSlcbiAgICByZXR1cm4gYWNjXG4gIH0sIHt9KVxuXG4gIGNvbnN0IGdyb3VwUmFua3MgPSBPYmplY3Qua2V5cyhncm91cGVkQnlSYW5rcylcblxuICBjb25zdCBzb3J0ZXJGbiA9IGFscGhhYmV0aXplT3B0aW9ucy5vcmRlciA9PT0gJ2FzYycgPyBpbXBvcnRzU29ydGVyQXNjIDogaW1wb3J0c1NvcnRlckRlc2NcbiAgY29uc3QgY29tcGFyYXRvciA9IGFscGhhYmV0aXplT3B0aW9ucy5jYXNlSW5zZW5zaXRpdmUgPyAoYSwgYikgPT4gc29ydGVyRm4oU3RyaW5nKGEpLnRvTG93ZXJDYXNlKCksIFN0cmluZyhiKS50b0xvd2VyQ2FzZSgpKSA6IChhLCBiKSA9PiBzb3J0ZXJGbihhLCBiKVxuICAvLyBzb3J0IGltcG9ydHMgbG9jYWxseSB3aXRoaW4gdGhlaXIgZ3JvdXBcbiAgZ3JvdXBSYW5rcy5mb3JFYWNoKGZ1bmN0aW9uKGdyb3VwUmFuaykge1xuICAgIGdyb3VwZWRCeVJhbmtzW2dyb3VwUmFua10uc29ydChjb21wYXJhdG9yKVxuICB9KVxuXG4gIC8vIGFzc2lnbiBnbG9iYWxseSB1bmlxdWUgcmFuayB0byBlYWNoIGltcG9ydFxuICBsZXQgbmV3UmFuayA9IDBcbiAgY29uc3QgYWxwaGFiZXRpemVkUmFua3MgPSBncm91cFJhbmtzLnNvcnQoKS5yZWR1Y2UoZnVuY3Rpb24oYWNjLCBncm91cFJhbmspIHtcbiAgICBncm91cGVkQnlSYW5rc1tncm91cFJhbmtdLmZvckVhY2goZnVuY3Rpb24oaW1wb3J0ZWRJdGVtTmFtZSkge1xuICAgICAgYWNjW2ltcG9ydGVkSXRlbU5hbWVdID0gbmV3UmFua1xuICAgICAgbmV3UmFuayArPSAxXG4gICAgfSlcbiAgICByZXR1cm4gYWNjXG4gIH0sIHt9KVxuXG4gIC8vIG11dGF0ZSB0aGUgb3JpZ2luYWwgZ3JvdXAtcmFuayB3aXRoIGFscGhhYmV0aXplZC1yYW5rXG4gIGltcG9ydGVkLmZvckVhY2goZnVuY3Rpb24oaW1wb3J0ZWRJdGVtKSB7XG4gICAgaW1wb3J0ZWRJdGVtLnJhbmsgPSBhbHBoYWJldGl6ZWRSYW5rc1tpbXBvcnRlZEl0ZW0ubmFtZV1cbiAgfSlcbn1cblxuLy8gREVURUNUSU5HXG5cbmZ1bmN0aW9uIGNvbXB1dGVQYXRoUmFuayhyYW5rcywgcGF0aEdyb3VwcywgcGF0aCwgbWF4UG9zaXRpb24pIHtcbiAgZm9yIChsZXQgaSA9IDAsIGwgPSBwYXRoR3JvdXBzLmxlbmd0aDsgaSA8IGw7IGkrKykge1xuICAgIGNvbnN0IHsgcGF0dGVybiwgcGF0dGVybk9wdGlvbnMsIGdyb3VwLCBwb3NpdGlvbiA9IDEgfSA9IHBhdGhHcm91cHNbaV1cbiAgICBpZiAobWluaW1hdGNoKHBhdGgsIHBhdHRlcm4sIHBhdHRlcm5PcHRpb25zIHx8IHsgbm9jb21tZW50OiB0cnVlIH0pKSB7XG4gICAgICByZXR1cm4gcmFua3NbZ3JvdXBdICsgKHBvc2l0aW9uIC8gbWF4UG9zaXRpb24pXG4gICAgfVxuICB9XG59XG5cbmZ1bmN0aW9uIGNvbXB1dGVSYW5rKGNvbnRleHQsIHJhbmtzLCBuYW1lLCB0eXBlLCBleGNsdWRlZEltcG9ydFR5cGVzKSB7XG4gIGNvbnN0IGltcFR5cGUgPSBpbXBvcnRUeXBlKG5hbWUsIGNvbnRleHQpXG4gIGxldCByYW5rXG4gIGlmICghZXhjbHVkZWRJbXBvcnRUeXBlcy5oYXMoaW1wVHlwZSkpIHtcbiAgICByYW5rID0gY29tcHV0ZVBhdGhSYW5rKHJhbmtzLmdyb3VwcywgcmFua3MucGF0aEdyb3VwcywgbmFtZSwgcmFua3MubWF4UG9zaXRpb24pXG4gIH1cbiAgaWYgKCFyYW5rKSB7XG4gICAgcmFuayA9IHJhbmtzLmdyb3Vwc1tpbXBUeXBlXVxuICB9XG4gIGlmICh0eXBlICE9PSAnaW1wb3J0Jykge1xuICAgIHJhbmsgKz0gMTAwXG4gIH1cblxuICByZXR1cm4gcmFua1xufVxuXG5mdW5jdGlvbiByZWdpc3Rlck5vZGUoY29udGV4dCwgbm9kZSwgbmFtZSwgdHlwZSwgcmFua3MsIGltcG9ydGVkLCBleGNsdWRlZEltcG9ydFR5cGVzKSB7XG4gIGNvbnN0IHJhbmsgPSBjb21wdXRlUmFuayhjb250ZXh0LCByYW5rcywgbmFtZSwgdHlwZSwgZXhjbHVkZWRJbXBvcnRUeXBlcylcbiAgaWYgKHJhbmsgIT09IC0xKSB7XG4gICAgaW1wb3J0ZWQucHVzaCh7bmFtZSwgcmFuaywgbm9kZX0pXG4gIH1cbn1cblxuZnVuY3Rpb24gaXNJblZhcmlhYmxlRGVjbGFyYXRvcihub2RlKSB7XG4gIHJldHVybiBub2RlICYmXG4gICAgKG5vZGUudHlwZSA9PT0gJ1ZhcmlhYmxlRGVjbGFyYXRvcicgfHwgaXNJblZhcmlhYmxlRGVjbGFyYXRvcihub2RlLnBhcmVudCkpXG59XG5cbmNvbnN0IHR5cGVzID0gWydidWlsdGluJywgJ2V4dGVybmFsJywgJ2ludGVybmFsJywgJ3Vua25vd24nLCAncGFyZW50JywgJ3NpYmxpbmcnLCAnaW5kZXgnXVxuXG4vLyBDcmVhdGVzIGFuIG9iamVjdCB3aXRoIHR5cGUtcmFuayBwYWlycy5cbi8vIEV4YW1wbGU6IHsgaW5kZXg6IDAsIHNpYmxpbmc6IDEsIHBhcmVudDogMSwgZXh0ZXJuYWw6IDEsIGJ1aWx0aW46IDIsIGludGVybmFsOiAyIH1cbi8vIFdpbGwgdGhyb3cgYW4gZXJyb3IgaWYgaXQgY29udGFpbnMgYSB0eXBlIHRoYXQgZG9lcyBub3QgZXhpc3QsIG9yIGhhcyBhIGR1cGxpY2F0ZVxuZnVuY3Rpb24gY29udmVydEdyb3Vwc1RvUmFua3MoZ3JvdXBzKSB7XG4gIGNvbnN0IHJhbmtPYmplY3QgPSBncm91cHMucmVkdWNlKGZ1bmN0aW9uKHJlcywgZ3JvdXAsIGluZGV4KSB7XG4gICAgaWYgKHR5cGVvZiBncm91cCA9PT0gJ3N0cmluZycpIHtcbiAgICAgIGdyb3VwID0gW2dyb3VwXVxuICAgIH1cbiAgICBncm91cC5mb3JFYWNoKGZ1bmN0aW9uKGdyb3VwSXRlbSkge1xuICAgICAgaWYgKHR5cGVzLmluZGV4T2YoZ3JvdXBJdGVtKSA9PT0gLTEpIHtcbiAgICAgICAgdGhyb3cgbmV3IEVycm9yKCdJbmNvcnJlY3QgY29uZmlndXJhdGlvbiBvZiB0aGUgcnVsZTogVW5rbm93biB0eXBlIGAnICtcbiAgICAgICAgICBKU09OLnN0cmluZ2lmeShncm91cEl0ZW0pICsgJ2AnKVxuICAgICAgfVxuICAgICAgaWYgKHJlc1tncm91cEl0ZW1dICE9PSB1bmRlZmluZWQpIHtcbiAgICAgICAgdGhyb3cgbmV3IEVycm9yKCdJbmNvcnJlY3QgY29uZmlndXJhdGlvbiBvZiB0aGUgcnVsZTogYCcgKyBncm91cEl0ZW0gKyAnYCBpcyBkdXBsaWNhdGVkJylcbiAgICAgIH1cbiAgICAgIHJlc1tncm91cEl0ZW1dID0gaW5kZXhcbiAgICB9KVxuICAgIHJldHVybiByZXNcbiAgfSwge30pXG5cbiAgY29uc3Qgb21pdHRlZFR5cGVzID0gdHlwZXMuZmlsdGVyKGZ1bmN0aW9uKHR5cGUpIHtcbiAgICByZXR1cm4gcmFua09iamVjdFt0eXBlXSA9PT0gdW5kZWZpbmVkXG4gIH0pXG5cbiAgcmV0dXJuIG9taXR0ZWRUeXBlcy5yZWR1Y2UoZnVuY3Rpb24ocmVzLCB0eXBlKSB7XG4gICAgcmVzW3R5cGVdID0gZ3JvdXBzLmxlbmd0aFxuICAgIHJldHVybiByZXNcbiAgfSwgcmFua09iamVjdClcbn1cblxuZnVuY3Rpb24gY29udmVydFBhdGhHcm91cHNGb3JSYW5rcyhwYXRoR3JvdXBzKSB7XG4gIGNvbnN0IGFmdGVyID0ge31cbiAgY29uc3QgYmVmb3JlID0ge31cblxuICBjb25zdCB0cmFuc2Zvcm1lZCA9IHBhdGhHcm91cHMubWFwKChwYXRoR3JvdXAsIGluZGV4KSA9PiB7XG4gICAgY29uc3QgeyBncm91cCwgcG9zaXRpb246IHBvc2l0aW9uU3RyaW5nIH0gPSBwYXRoR3JvdXBcbiAgICBsZXQgcG9zaXRpb24gPSAwXG4gICAgaWYgKHBvc2l0aW9uU3RyaW5nID09PSAnYWZ0ZXInKSB7XG4gICAgICBpZiAoIWFmdGVyW2dyb3VwXSkge1xuICAgICAgICBhZnRlcltncm91cF0gPSAxXG4gICAgICB9XG4gICAgICBwb3NpdGlvbiA9IGFmdGVyW2dyb3VwXSsrXG4gICAgfSBlbHNlIGlmIChwb3NpdGlvblN0cmluZyA9PT0gJ2JlZm9yZScpIHtcbiAgICAgIGlmICghYmVmb3JlW2dyb3VwXSkge1xuICAgICAgICBiZWZvcmVbZ3JvdXBdID0gW11cbiAgICAgIH1cbiAgICAgIGJlZm9yZVtncm91cF0ucHVzaChpbmRleClcbiAgICB9XG5cbiAgICByZXR1cm4gT2JqZWN0LmFzc2lnbih7fSwgcGF0aEdyb3VwLCB7IHBvc2l0aW9uIH0pXG4gIH0pXG5cbiAgbGV0IG1heFBvc2l0aW9uID0gMVxuXG4gIE9iamVjdC5rZXlzKGJlZm9yZSkuZm9yRWFjaCgoZ3JvdXApID0+IHtcbiAgICBjb25zdCBncm91cExlbmd0aCA9IGJlZm9yZVtncm91cF0ubGVuZ3RoXG4gICAgYmVmb3JlW2dyb3VwXS5mb3JFYWNoKChncm91cEluZGV4LCBpbmRleCkgPT4ge1xuICAgICAgdHJhbnNmb3JtZWRbZ3JvdXBJbmRleF0ucG9zaXRpb24gPSAtMSAqIChncm91cExlbmd0aCAtIGluZGV4KVxuICAgIH0pXG4gICAgbWF4UG9zaXRpb24gPSBNYXRoLm1heChtYXhQb3NpdGlvbiwgZ3JvdXBMZW5ndGgpXG4gIH0pXG5cbiAgT2JqZWN0LmtleXMoYWZ0ZXIpLmZvckVhY2goKGtleSkgPT4ge1xuICAgIGNvbnN0IGdyb3VwTmV4dFBvc2l0aW9uID0gYWZ0ZXJba2V5XVxuICAgIG1heFBvc2l0aW9uID0gTWF0aC5tYXgobWF4UG9zaXRpb24sIGdyb3VwTmV4dFBvc2l0aW9uIC0gMSlcbiAgfSlcblxuICByZXR1cm4ge1xuICAgIHBhdGhHcm91cHM6IHRyYW5zZm9ybWVkLFxuICAgIG1heFBvc2l0aW9uOiBtYXhQb3NpdGlvbiA+IDEwID8gTWF0aC5wb3coMTAsIE1hdGguY2VpbChNYXRoLmxvZzEwKG1heFBvc2l0aW9uKSkpIDogMTAsXG4gIH1cbn1cblxuZnVuY3Rpb24gZml4TmV3TGluZUFmdGVySW1wb3J0KGNvbnRleHQsIHByZXZpb3VzSW1wb3J0KSB7XG4gIGNvbnN0IHByZXZSb290ID0gZmluZFJvb3ROb2RlKHByZXZpb3VzSW1wb3J0Lm5vZGUpXG4gIGNvbnN0IHRva2Vuc1RvRW5kT2ZMaW5lID0gdGFrZVRva2Vuc0FmdGVyV2hpbGUoXG4gICAgY29udGV4dC5nZXRTb3VyY2VDb2RlKCksIHByZXZSb290LCBjb21tZW50T25TYW1lTGluZUFzKHByZXZSb290KSlcblxuICBsZXQgZW5kT2ZMaW5lID0gcHJldlJvb3QucmFuZ2VbMV1cbiAgaWYgKHRva2Vuc1RvRW5kT2ZMaW5lLmxlbmd0aCA+IDApIHtcbiAgICBlbmRPZkxpbmUgPSB0b2tlbnNUb0VuZE9mTGluZVt0b2tlbnNUb0VuZE9mTGluZS5sZW5ndGggLSAxXS5yYW5nZVsxXVxuICB9XG4gIHJldHVybiAoZml4ZXIpID0+IGZpeGVyLmluc2VydFRleHRBZnRlclJhbmdlKFtwcmV2Um9vdC5yYW5nZVswXSwgZW5kT2ZMaW5lXSwgJ1xcbicpXG59XG5cbmZ1bmN0aW9uIHJlbW92ZU5ld0xpbmVBZnRlckltcG9ydChjb250ZXh0LCBjdXJyZW50SW1wb3J0LCBwcmV2aW91c0ltcG9ydCkge1xuICBjb25zdCBzb3VyY2VDb2RlID0gY29udGV4dC5nZXRTb3VyY2VDb2RlKClcbiAgY29uc3QgcHJldlJvb3QgPSBmaW5kUm9vdE5vZGUocHJldmlvdXNJbXBvcnQubm9kZSlcbiAgY29uc3QgY3VyclJvb3QgPSBmaW5kUm9vdE5vZGUoY3VycmVudEltcG9ydC5ub2RlKVxuICBjb25zdCByYW5nZVRvUmVtb3ZlID0gW1xuICAgIGZpbmRFbmRPZkxpbmVXaXRoQ29tbWVudHMoc291cmNlQ29kZSwgcHJldlJvb3QpLFxuICAgIGZpbmRTdGFydE9mTGluZVdpdGhDb21tZW50cyhzb3VyY2VDb2RlLCBjdXJyUm9vdCksXG4gIF1cbiAgaWYgKC9eXFxzKiQvLnRlc3Qoc291cmNlQ29kZS50ZXh0LnN1YnN0cmluZyhyYW5nZVRvUmVtb3ZlWzBdLCByYW5nZVRvUmVtb3ZlWzFdKSkpIHtcbiAgICByZXR1cm4gKGZpeGVyKSA9PiBmaXhlci5yZW1vdmVSYW5nZShyYW5nZVRvUmVtb3ZlKVxuICB9XG4gIHJldHVybiB1bmRlZmluZWRcbn1cblxuZnVuY3Rpb24gbWFrZU5ld2xpbmVzQmV0d2VlblJlcG9ydCAoY29udGV4dCwgaW1wb3J0ZWQsIG5ld2xpbmVzQmV0d2VlbkltcG9ydHMpIHtcbiAgY29uc3QgZ2V0TnVtYmVyT2ZFbXB0eUxpbmVzQmV0d2VlbiA9IChjdXJyZW50SW1wb3J0LCBwcmV2aW91c0ltcG9ydCkgPT4ge1xuICAgIGNvbnN0IGxpbmVzQmV0d2VlbkltcG9ydHMgPSBjb250ZXh0LmdldFNvdXJjZUNvZGUoKS5saW5lcy5zbGljZShcbiAgICAgIHByZXZpb3VzSW1wb3J0Lm5vZGUubG9jLmVuZC5saW5lLFxuICAgICAgY3VycmVudEltcG9ydC5ub2RlLmxvYy5zdGFydC5saW5lIC0gMVxuICAgIClcblxuICAgIHJldHVybiBsaW5lc0JldHdlZW5JbXBvcnRzLmZpbHRlcigobGluZSkgPT4gIWxpbmUudHJpbSgpLmxlbmd0aCkubGVuZ3RoXG4gIH1cbiAgbGV0IHByZXZpb3VzSW1wb3J0ID0gaW1wb3J0ZWRbMF1cblxuICBpbXBvcnRlZC5zbGljZSgxKS5mb3JFYWNoKGZ1bmN0aW9uKGN1cnJlbnRJbXBvcnQpIHtcbiAgICBjb25zdCBlbXB0eUxpbmVzQmV0d2VlbiA9IGdldE51bWJlck9mRW1wdHlMaW5lc0JldHdlZW4oY3VycmVudEltcG9ydCwgcHJldmlvdXNJbXBvcnQpXG5cbiAgICBpZiAobmV3bGluZXNCZXR3ZWVuSW1wb3J0cyA9PT0gJ2Fsd2F5cydcbiAgICAgICAgfHwgbmV3bGluZXNCZXR3ZWVuSW1wb3J0cyA9PT0gJ2Fsd2F5cy1hbmQtaW5zaWRlLWdyb3VwcycpIHtcbiAgICAgIGlmIChjdXJyZW50SW1wb3J0LnJhbmsgIT09IHByZXZpb3VzSW1wb3J0LnJhbmsgJiYgZW1wdHlMaW5lc0JldHdlZW4gPT09IDApIHtcbiAgICAgICAgY29udGV4dC5yZXBvcnQoe1xuICAgICAgICAgIG5vZGU6IHByZXZpb3VzSW1wb3J0Lm5vZGUsXG4gICAgICAgICAgbWVzc2FnZTogJ1RoZXJlIHNob3VsZCBiZSBhdCBsZWFzdCBvbmUgZW1wdHkgbGluZSBiZXR3ZWVuIGltcG9ydCBncm91cHMnLFxuICAgICAgICAgIGZpeDogZml4TmV3TGluZUFmdGVySW1wb3J0KGNvbnRleHQsIHByZXZpb3VzSW1wb3J0KSxcbiAgICAgICAgfSlcbiAgICAgIH0gZWxzZSBpZiAoY3VycmVudEltcG9ydC5yYW5rID09PSBwcmV2aW91c0ltcG9ydC5yYW5rXG4gICAgICAgICYmIGVtcHR5TGluZXNCZXR3ZWVuID4gMFxuICAgICAgICAmJiBuZXdsaW5lc0JldHdlZW5JbXBvcnRzICE9PSAnYWx3YXlzLWFuZC1pbnNpZGUtZ3JvdXBzJykge1xuICAgICAgICBjb250ZXh0LnJlcG9ydCh7XG4gICAgICAgICAgbm9kZTogcHJldmlvdXNJbXBvcnQubm9kZSxcbiAgICAgICAgICBtZXNzYWdlOiAnVGhlcmUgc2hvdWxkIGJlIG5vIGVtcHR5IGxpbmUgd2l0aGluIGltcG9ydCBncm91cCcsXG4gICAgICAgICAgZml4OiByZW1vdmVOZXdMaW5lQWZ0ZXJJbXBvcnQoY29udGV4dCwgY3VycmVudEltcG9ydCwgcHJldmlvdXNJbXBvcnQpLFxuICAgICAgICB9KVxuICAgICAgfVxuICAgIH0gZWxzZSBpZiAoZW1wdHlMaW5lc0JldHdlZW4gPiAwKSB7XG4gICAgICBjb250ZXh0LnJlcG9ydCh7XG4gICAgICAgIG5vZGU6IHByZXZpb3VzSW1wb3J0Lm5vZGUsXG4gICAgICAgIG1lc3NhZ2U6ICdUaGVyZSBzaG91bGQgYmUgbm8gZW1wdHkgbGluZSBiZXR3ZWVuIGltcG9ydCBncm91cHMnLFxuICAgICAgICBmaXg6IHJlbW92ZU5ld0xpbmVBZnRlckltcG9ydChjb250ZXh0LCBjdXJyZW50SW1wb3J0LCBwcmV2aW91c0ltcG9ydCksXG4gICAgICB9KVxuICAgIH1cblxuICAgIHByZXZpb3VzSW1wb3J0ID0gY3VycmVudEltcG9ydFxuICB9KVxufVxuXG5mdW5jdGlvbiBnZXRBbHBoYWJldGl6ZUNvbmZpZyhvcHRpb25zKSB7XG4gIGNvbnN0IGFscGhhYmV0aXplID0gb3B0aW9ucy5hbHBoYWJldGl6ZSB8fCB7fVxuICBjb25zdCBvcmRlciA9IGFscGhhYmV0aXplLm9yZGVyIHx8ICdpZ25vcmUnXG4gIGNvbnN0IGNhc2VJbnNlbnNpdGl2ZSA9IGFscGhhYmV0aXplLmNhc2VJbnNlbnNpdGl2ZSB8fCBmYWxzZVxuXG4gIHJldHVybiB7b3JkZXIsIGNhc2VJbnNlbnNpdGl2ZX1cbn1cblxubW9kdWxlLmV4cG9ydHMgPSB7XG4gIG1ldGE6IHtcbiAgICB0eXBlOiAnc3VnZ2VzdGlvbicsXG4gICAgZG9jczoge1xuICAgICAgdXJsOiBkb2NzVXJsKCdvcmRlcicpLFxuICAgIH0sXG5cbiAgICBmaXhhYmxlOiAnY29kZScsXG4gICAgc2NoZW1hOiBbXG4gICAgICB7XG4gICAgICAgIHR5cGU6ICdvYmplY3QnLFxuICAgICAgICBwcm9wZXJ0aWVzOiB7XG4gICAgICAgICAgZ3JvdXBzOiB7XG4gICAgICAgICAgICB0eXBlOiAnYXJyYXknLFxuICAgICAgICAgIH0sXG4gICAgICAgICAgcGF0aEdyb3Vwc0V4Y2x1ZGVkSW1wb3J0VHlwZXM6IHtcbiAgICAgICAgICAgIHR5cGU6ICdhcnJheScsXG4gICAgICAgICAgfSxcbiAgICAgICAgICBwYXRoR3JvdXBzOiB7XG4gICAgICAgICAgICB0eXBlOiAnYXJyYXknLFxuICAgICAgICAgICAgaXRlbXM6IHtcbiAgICAgICAgICAgICAgdHlwZTogJ29iamVjdCcsXG4gICAgICAgICAgICAgIHByb3BlcnRpZXM6IHtcbiAgICAgICAgICAgICAgICBwYXR0ZXJuOiB7XG4gICAgICAgICAgICAgICAgICB0eXBlOiAnc3RyaW5nJyxcbiAgICAgICAgICAgICAgICB9LFxuICAgICAgICAgICAgICAgIHBhdHRlcm5PcHRpb25zOiB7XG4gICAgICAgICAgICAgICAgICB0eXBlOiAnb2JqZWN0JyxcbiAgICAgICAgICAgICAgICB9LFxuICAgICAgICAgICAgICAgIGdyb3VwOiB7XG4gICAgICAgICAgICAgICAgICB0eXBlOiAnc3RyaW5nJyxcbiAgICAgICAgICAgICAgICAgIGVudW06IHR5cGVzLFxuICAgICAgICAgICAgICAgIH0sXG4gICAgICAgICAgICAgICAgcG9zaXRpb246IHtcbiAgICAgICAgICAgICAgICAgIHR5cGU6ICdzdHJpbmcnLFxuICAgICAgICAgICAgICAgICAgZW51bTogWydhZnRlcicsICdiZWZvcmUnXSxcbiAgICAgICAgICAgICAgICB9LFxuICAgICAgICAgICAgICB9LFxuICAgICAgICAgICAgICByZXF1aXJlZDogWydwYXR0ZXJuJywgJ2dyb3VwJ10sXG4gICAgICAgICAgICB9LFxuICAgICAgICAgIH0sXG4gICAgICAgICAgJ25ld2xpbmVzLWJldHdlZW4nOiB7XG4gICAgICAgICAgICBlbnVtOiBbXG4gICAgICAgICAgICAgICdpZ25vcmUnLFxuICAgICAgICAgICAgICAnYWx3YXlzJyxcbiAgICAgICAgICAgICAgJ2Fsd2F5cy1hbmQtaW5zaWRlLWdyb3VwcycsXG4gICAgICAgICAgICAgICduZXZlcicsXG4gICAgICAgICAgICBdLFxuICAgICAgICAgIH0sXG4gICAgICAgICAgYWxwaGFiZXRpemU6IHtcbiAgICAgICAgICAgIHR5cGU6ICdvYmplY3QnLFxuICAgICAgICAgICAgcHJvcGVydGllczoge1xuICAgICAgICAgICAgICBjYXNlSW5zZW5zaXRpdmU6IHtcbiAgICAgICAgICAgICAgICB0eXBlOiAnYm9vbGVhbicsXG4gICAgICAgICAgICAgICAgZGVmYXVsdDogZmFsc2UsXG4gICAgICAgICAgICAgIH0sXG4gICAgICAgICAgICAgIG9yZGVyOiB7XG4gICAgICAgICAgICAgICAgZW51bTogWydpZ25vcmUnLCAnYXNjJywgJ2Rlc2MnXSxcbiAgICAgICAgICAgICAgICBkZWZhdWx0OiAnaWdub3JlJyxcbiAgICAgICAgICAgICAgfSxcbiAgICAgICAgICAgIH0sXG4gICAgICAgICAgICBhZGRpdGlvbmFsUHJvcGVydGllczogZmFsc2UsXG4gICAgICAgICAgfSxcbiAgICAgICAgfSxcbiAgICAgICAgYWRkaXRpb25hbFByb3BlcnRpZXM6IGZhbHNlLFxuICAgICAgfSxcbiAgICBdLFxuICB9LFxuXG4gIGNyZWF0ZTogZnVuY3Rpb24gaW1wb3J0T3JkZXJSdWxlIChjb250ZXh0KSB7XG4gICAgY29uc3Qgb3B0aW9ucyA9IGNvbnRleHQub3B0aW9uc1swXSB8fCB7fVxuICAgIGNvbnN0IG5ld2xpbmVzQmV0d2VlbkltcG9ydHMgPSBvcHRpb25zWyduZXdsaW5lcy1iZXR3ZWVuJ10gfHwgJ2lnbm9yZSdcbiAgICBjb25zdCBwYXRoR3JvdXBzRXhjbHVkZWRJbXBvcnRUeXBlcyA9IG5ldyBTZXQob3B0aW9uc1sncGF0aEdyb3Vwc0V4Y2x1ZGVkSW1wb3J0VHlwZXMnXSB8fCBbJ2J1aWx0aW4nLCAnZXh0ZXJuYWwnXSlcbiAgICBjb25zdCBhbHBoYWJldGl6ZSA9IGdldEFscGhhYmV0aXplQ29uZmlnKG9wdGlvbnMpXG4gICAgbGV0IHJhbmtzXG5cbiAgICB0cnkge1xuICAgICAgY29uc3QgeyBwYXRoR3JvdXBzLCBtYXhQb3NpdGlvbiB9ID0gY29udmVydFBhdGhHcm91cHNGb3JSYW5rcyhvcHRpb25zLnBhdGhHcm91cHMgfHwgW10pXG4gICAgICByYW5rcyA9IHtcbiAgICAgICAgZ3JvdXBzOiBjb252ZXJ0R3JvdXBzVG9SYW5rcyhvcHRpb25zLmdyb3VwcyB8fCBkZWZhdWx0R3JvdXBzKSxcbiAgICAgICAgcGF0aEdyb3VwcyxcbiAgICAgICAgbWF4UG9zaXRpb24sXG4gICAgICB9XG4gICAgfSBjYXRjaCAoZXJyb3IpIHtcbiAgICAgIC8vIE1hbGZvcm1lZCBjb25maWd1cmF0aW9uXG4gICAgICByZXR1cm4ge1xuICAgICAgICBQcm9ncmFtOiBmdW5jdGlvbihub2RlKSB7XG4gICAgICAgICAgY29udGV4dC5yZXBvcnQobm9kZSwgZXJyb3IubWVzc2FnZSlcbiAgICAgICAgfSxcbiAgICAgIH1cbiAgICB9XG4gICAgbGV0IGltcG9ydGVkID0gW11cbiAgICBsZXQgbGV2ZWwgPSAwXG5cbiAgICBmdW5jdGlvbiBpbmNyZW1lbnRMZXZlbCgpIHtcbiAgICAgIGxldmVsKytcbiAgICB9XG4gICAgZnVuY3Rpb24gZGVjcmVtZW50TGV2ZWwoKSB7XG4gICAgICBsZXZlbC0tXG4gICAgfVxuXG4gICAgcmV0dXJuIHtcbiAgICAgIEltcG9ydERlY2xhcmF0aW9uOiBmdW5jdGlvbiBoYW5kbGVJbXBvcnRzKG5vZGUpIHtcbiAgICAgICAgaWYgKG5vZGUuc3BlY2lmaWVycy5sZW5ndGgpIHsgLy8gSWdub3JpbmcgdW5hc3NpZ25lZCBpbXBvcnRzXG4gICAgICAgICAgY29uc3QgbmFtZSA9IG5vZGUuc291cmNlLnZhbHVlXG4gICAgICAgICAgcmVnaXN0ZXJOb2RlKFxuICAgICAgICAgICAgY29udGV4dCxcbiAgICAgICAgICAgIG5vZGUsXG4gICAgICAgICAgICBuYW1lLFxuICAgICAgICAgICAgJ2ltcG9ydCcsXG4gICAgICAgICAgICByYW5rcyxcbiAgICAgICAgICAgIGltcG9ydGVkLFxuICAgICAgICAgICAgcGF0aEdyb3Vwc0V4Y2x1ZGVkSW1wb3J0VHlwZXNcbiAgICAgICAgICApXG4gICAgICAgIH1cbiAgICAgIH0sXG4gICAgICBDYWxsRXhwcmVzc2lvbjogZnVuY3Rpb24gaGFuZGxlUmVxdWlyZXMobm9kZSkge1xuICAgICAgICBpZiAobGV2ZWwgIT09IDAgfHwgIWlzU3RhdGljUmVxdWlyZShub2RlKSB8fCAhaXNJblZhcmlhYmxlRGVjbGFyYXRvcihub2RlLnBhcmVudCkpIHtcbiAgICAgICAgICByZXR1cm5cbiAgICAgICAgfVxuICAgICAgICBjb25zdCBuYW1lID0gbm9kZS5hcmd1bWVudHNbMF0udmFsdWVcbiAgICAgICAgcmVnaXN0ZXJOb2RlKFxuICAgICAgICAgIGNvbnRleHQsXG4gICAgICAgICAgbm9kZSxcbiAgICAgICAgICBuYW1lLFxuICAgICAgICAgICdyZXF1aXJlJyxcbiAgICAgICAgICByYW5rcyxcbiAgICAgICAgICBpbXBvcnRlZCxcbiAgICAgICAgICBwYXRoR3JvdXBzRXhjbHVkZWRJbXBvcnRUeXBlc1xuICAgICAgICApXG4gICAgICB9LFxuICAgICAgJ1Byb2dyYW06ZXhpdCc6IGZ1bmN0aW9uIHJlcG9ydEFuZFJlc2V0KCkge1xuICAgICAgICBpZiAobmV3bGluZXNCZXR3ZWVuSW1wb3J0cyAhPT0gJ2lnbm9yZScpIHtcbiAgICAgICAgICBtYWtlTmV3bGluZXNCZXR3ZWVuUmVwb3J0KGNvbnRleHQsIGltcG9ydGVkLCBuZXdsaW5lc0JldHdlZW5JbXBvcnRzKVxuICAgICAgICB9XG5cbiAgICAgICAgaWYgKGFscGhhYmV0aXplLm9yZGVyICE9PSAnaWdub3JlJykge1xuICAgICAgICAgIG11dGF0ZVJhbmtzVG9BbHBoYWJldGl6ZShpbXBvcnRlZCwgYWxwaGFiZXRpemUpXG4gICAgICAgIH1cblxuICAgICAgICBtYWtlT3V0T2ZPcmRlclJlcG9ydChjb250ZXh0LCBpbXBvcnRlZClcblxuICAgICAgICBpbXBvcnRlZCA9IFtdXG4gICAgICB9LFxuICAgICAgRnVuY3Rpb25EZWNsYXJhdGlvbjogaW5jcmVtZW50TGV2ZWwsXG4gICAgICBGdW5jdGlvbkV4cHJlc3Npb246IGluY3JlbWVudExldmVsLFxuICAgICAgQXJyb3dGdW5jdGlvbkV4cHJlc3Npb246IGluY3JlbWVudExldmVsLFxuICAgICAgQmxvY2tTdGF0ZW1lbnQ6IGluY3JlbWVudExldmVsLFxuICAgICAgT2JqZWN0RXhwcmVzc2lvbjogaW5jcmVtZW50TGV2ZWwsXG4gICAgICAnRnVuY3Rpb25EZWNsYXJhdGlvbjpleGl0JzogZGVjcmVtZW50TGV2ZWwsXG4gICAgICAnRnVuY3Rpb25FeHByZXNzaW9uOmV4aXQnOiBkZWNyZW1lbnRMZXZlbCxcbiAgICAgICdBcnJvd0Z1bmN0aW9uRXhwcmVzc2lvbjpleGl0JzogZGVjcmVtZW50TGV2ZWwsXG4gICAgICAnQmxvY2tTdGF0ZW1lbnQ6ZXhpdCc6IGRlY3JlbWVudExldmVsLFxuICAgICAgJ09iamVjdEV4cHJlc3Npb246ZXhpdCc6IGRlY3JlbWVudExldmVsLFxuICAgIH1cbiAgfSxcbn1cbiJdfQ==