function ownKeys(object, enumerableOnly) { var keys = Object.keys(object); if (Object.getOwnPropertySymbols) { var symbols = Object.getOwnPropertySymbols(object); if (enumerableOnly) symbols = symbols.filter(function (sym) { return Object.getOwnPropertyDescriptor(object, sym).enumerable; }); keys.push.apply(keys, symbols); } return keys; }

function _objectSpread(target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i] != null ? arguments[i] : {}; if (i % 2) { ownKeys(Object(source), true).forEach(function (key) { _defineProperty(target, key, source[key]); }); } else if (Object.getOwnPropertyDescriptors) { Object.defineProperties(target, Object.getOwnPropertyDescriptors(source)); } else { ownKeys(Object(source)).forEach(function (key) { Object.defineProperty(target, key, Object.getOwnPropertyDescriptor(source, key)); }); } } return target; }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

/**
 * Counterpart to `normalize` for a collection of rows. This method unravels the
 * normalization step that we use to build the given parameters in order to
 * return a natural interface to working with rows for a consumer.
 *
 * The default heuristic here is to map through all the row ids and return the
 * value of the row for the given id, in addition to adding a `cells` key that
 * contains the results of mapping over the rows cells and getting individual
 * cell info.
 *
 * @param {Array<string>} rowIds array of row ids in the table
 * @param {object} rowsById object containing lookups for rows by id
 * @param {object} cellsById object containing lookups for cells by id
 */
var denormalize = function denormalize(rowIds, rowsById, cellsById) {
  return rowIds.map(function (id) {
    return _objectSpread({}, rowsById[id], {
      cells: rowsById[id].cells.map(function (cellId) {
        return cellsById[cellId];
      })
    });
  });
};

export default denormalize;