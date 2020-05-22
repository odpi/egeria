"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _cells = require("./cells");

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

/**
 * Normalize a collection of rows with the given headers.
 *
 * @param {Array<object>} rows
 * @param {Array<object>} headers
 * @returns {object}
 */
var normalize = function normalize(rows, headers) {
  var prevState = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : {};
  var prevRowsByIds = prevState.rowsById;
  var rowIds = new Array(rows.length);
  var rowsById = {};
  var cellsById = {};
  rows.forEach(function (row, i) {
    rowIds[i] = row.id; // Initialize the row info and state values, namely for selection and
    // expansion

    var id = row.id,
        _row$isSelected = row.isSelected,
        isSelected = _row$isSelected === void 0 ? false : _row$isSelected,
        _row$isExpanded = row.isExpanded,
        isExpanded = _row$isExpanded === void 0 ? false : _row$isExpanded,
        _row$disabled = row.disabled,
        disabled = _row$disabled === void 0 ? false : _row$disabled;
    rowsById[id] = {
      id: id,
      isSelected: isSelected,
      isExpanded: isExpanded,
      disabled: disabled,
      cells: new Array(headers.length)
    }; // If we have a previous state, and the row existed in that previous state,
    // then we'll set the state values of the row to the previous state values.

    if (prevRowsByIds && prevRowsByIds[row.id] !== undefined) {
      rowsById[row.id].isSelected = prevRowsByIds[row.id].isSelected;
      rowsById[row.id].isExpanded = prevRowsByIds[row.id].isExpanded;
    }

    headers.forEach(function (_ref, i) {
      var key = _ref.key;
      var id = (0, _cells.getCellId)(row.id, key); // Initialize the cell info and state values, namely for editing

      cellsById[id] = {
        id: id,
        value: row[key],
        isEditable: false,
        isEditing: false,
        isValid: true,
        errors: null,
        info: {
          header: key
        }
      }; // TODO: When working on inline edits, we'll need to derive the state
      // values similarly to rows above.

      rowsById[row.id].cells[i] = id;
    });
  });
  return {
    rowIds: rowIds,
    rowsById: rowsById,
    cellsById: cellsById
  };
};

var _default = normalize;
exports.default = _default;