"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _sorting = require("./sorting");

var _normalize2 = _interopRequireDefault(require("../tools/normalize"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

/**
 * Helper to derive the next state from the given props and the
 * prevState. Potential future-facing API hook for React v17.
 *
 * Currently, it's being used as a way to normalize the incoming data that we
 * are receiving for rows
 */
var getDerivedStateFromProps = function getDerivedStateFromProps(props, prevState) {
  var _normalize = (0, _normalize2.default)(props.rows, props.headers, prevState),
      rowIds = _normalize.rowIds,
      rowsById = _normalize.rowsById,
      cellsById = _normalize.cellsById;

  var state = {
    rowIds: rowIds,
    rowsById: rowsById,
    cellsById: cellsById,
    sortDirection: prevState.sortDirection || _sorting.initialSortState,
    sortHeaderKey: prevState.sortHeaderKey || null,
    // Copy over rowIds so the reference doesn't mutate the stored
    // `initialRowOrder`
    initialRowOrder: rowIds.slice(),
    filterInputValue: prevState.filterInputValue || null,
    // Optional state field to indicate whether a consumer should show a
    // batch actions menu
    shouldShowBatchActions: prevState.shouldShowBatchActions || false
  };

  if (prevState.sortDirection && prevState.sortHeaderKey) {
    var _getSortedState = (0, _sorting.getSortedState)(props, state, prevState.sortHeaderKey, prevState.sortDirection),
        _rowIds = _getSortedState.rowIds;

    state.rowIds = _rowIds;
  }

  return state;
};

var _default = getDerivedStateFromProps;
exports.default = _default;