"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.getSortedState = exports.getNextSortState = exports.getNextSortDirection = exports.initialSortState = exports.sortStates = void 0;

var _sorting = require("../tools/sorting");

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

/**
 * We currently support the following sorting states for DataTable headers,
 * namely: `NONE` for no sorting being applied, and then `DESC` and `ASC` for
 * the corresponding direction of the sorting order.
 */
var sortStates = {
  NONE: 'NONE',
  DESC: 'DESC',
  ASC: 'ASC'
}; // Our initialSortState should be `NONE`, unless a consumer has specified a
// different initialSortState

exports.sortStates = sortStates;
var initialSortState = sortStates.NONE;
/**
 * Utility used to get the next sort state given the following pieces of
 * information:
 *
 * @param {string} prevHeader the value of the previous header
 * @param {string} header the value of the currently selected header
 * @param {string} prevState the previous sort state of the table
 * @returns {string}
 */

exports.initialSortState = initialSortState;

var getNextSortDirection = function getNextSortDirection(prevHeader, header, prevState) {
  // If the previous header is equivalent to the current header, we know that we
  // have to derive the next sort state from the previous sort state
  if (prevHeader === header) {
    // When transitioning, we know that the sequence of states is as follows:
    // NONE -> ASC -> DESC -> NONE
    if (prevState === 'NONE') {
      return sortStates.ASC;
    }

    if (prevState === 'ASC') {
      return sortStates.DESC;
    }

    return sortStates.NONE;
  } // Otherwise, we have selected a new header and need to start off by sorting
  // in descending order by default


  return sortStates.ASC;
};

exports.getNextSortDirection = getNextSortDirection;

var getNextSortState = function getNextSortState(props, state, _ref) {
  var key = _ref.key;
  var sortDirection = state.sortDirection,
      sortHeaderKey = state.sortHeaderKey;
  var nextSortDirection = getNextSortDirection(key, sortHeaderKey, sortDirection);
  return getSortedState(props, state, key, nextSortDirection);
};
/**
 * Derive the set of sorted state fields from props and state for the given
 * header key and sortDirection
 *
 * @param {object} props
 * @param {string} props.locale The current locale
 * @param {Function} props.sortRows Method to handle sorting a collection of
 * rows
 * @param {object} state
 * @param {Array<string>} state.rowIds Array of row ids
 * @param {object} state.cellsById Lookup object for cells by id
 * @param {Array<string>} state.initialRowOrder Initial row order for the
 * current set of rows
 * @param {string} key The key for the given header we are derving the
 * sorted state for
 * @param {string} sortDirection The sortState that we want to order by
 * @returns {object}
 */


exports.getNextSortState = getNextSortState;

var getSortedState = function getSortedState(props, state, key, sortDirection) {
  var rowIds = state.rowIds,
      cellsById = state.cellsById,
      initialRowOrder = state.initialRowOrder;
  var locale = props.locale,
      sortRow = props.sortRow;
  var nextRowIds = sortDirection !== sortStates.NONE ? (0, _sorting.sortRows)({
    rowIds: rowIds,
    cellsById: cellsById,
    sortDirection: sortDirection,
    key: key,
    locale: locale,
    sortRow: sortRow
  }) : initialRowOrder;
  return {
    sortHeaderKey: key,
    sortDirection: sortDirection,
    rowIds: nextRowIds
  };
};

exports.getSortedState = getSortedState;