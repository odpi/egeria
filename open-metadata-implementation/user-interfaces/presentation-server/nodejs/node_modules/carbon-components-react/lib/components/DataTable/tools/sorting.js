"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.defaultSortRow = exports.sortRows = exports.compareStrings = exports.compare = void 0;

var _cells = require("./cells");

var _sorting = require("../state/sorting");

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

/**
 * Compare two primitives to determine which comes first. Initially, this method
 * will try and figure out if both entries are the same type. If so, it will
 * apply the default sort algorithm for those types. Otherwise, it defaults to a
 * string conversion.
 *
 * @param {number|string} a
 * @param {number|string} b
 * @param {string} locale
 * @returns {number}
 */
var compare = function compare(a, b) {
  var locale = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : 'en';

  if (typeof a === 'number' && typeof b === 'number') {
    return a - b;
  }

  if (typeof a === 'string' && typeof b === 'string') {
    return compareStrings(a, b, locale);
  }

  return compareStrings('' + a, '' + b, locale);
};
/**
 * Use the built-in `localeCompare` function available on strings to compare two
 * srints.
 *
 * @param {string} a
 * @param {string} b
 * @param {string} locale
 * @returns {number}
 */


exports.compare = compare;

var compareStrings = function compareStrings(a, b) {
  var locale = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : 'en';
  return a.localeCompare(b, locale, {
    numeric: true
  });
};
/**
 * Default implementation of how we sort rows internally. The idea behind this
 * implementation is to use the given list of row ids to look up the cells in
 * the row by the given key. We then use the value of these cells and pipe them
 * into our local `compareStrings` method, including the locale where
 * appropriate.
 *
 * @param {object} config
 * @param {Array[string]} config.rowIds array of all the row ids in the table
 * @param {object} config.cellsById object containing a mapping of cell id to
 * cell
 * @param {string} config.direction the sort direction used to determine the
 * order the comparison is called in
 * @param {string} config.key the header key that we use to lookup the cell
 * @param {string} [config.locale] optional locale used in the comparison
 * function
 * @returns {Array[string]} array of sorted rowIds
 */


exports.compareStrings = compareStrings;

var sortRows = function sortRows(_ref) {
  var rowIds = _ref.rowIds,
      cellsById = _ref.cellsById,
      sortDirection = _ref.sortDirection,
      key = _ref.key,
      locale = _ref.locale,
      sortRow = _ref.sortRow;
  return rowIds.slice().sort(function (a, b) {
    var cellA = cellsById[(0, _cells.getCellId)(a, key)];
    var cellB = cellsById[(0, _cells.getCellId)(b, key)];
    return sortRow(cellA.value, cellB.value, {
      key: key,
      sortDirection: sortDirection,
      locale: locale,
      sortStates: _sorting.sortStates,
      compare: compare
    });
  });
};

exports.sortRows = sortRows;

var defaultSortRow = function defaultSortRow(cellA, cellB, _ref2) {
  var sortDirection = _ref2.sortDirection,
      sortStates = _ref2.sortStates,
      locale = _ref2.locale;

  if (sortDirection === sortStates.ASC) {
    return compare(cellA, cellB, locale);
  }

  return compare(cellB, cellA, locale);
};

exports.defaultSortRow = defaultSortRow;