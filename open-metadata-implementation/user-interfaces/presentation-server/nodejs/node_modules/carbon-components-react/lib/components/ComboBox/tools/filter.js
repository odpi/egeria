"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.defaultFilterItems = void 0;

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
var defaultFilterItems = function defaultFilterItems(items, _ref) {
  var itemToString = _ref.itemToString,
      inputValue = _ref.inputValue;
  return items.filter(function (item) {
    if (!inputValue) {
      return true;
    }

    return itemToString(item).toLowerCase().includes(inputValue.toLowerCase());
  });
};

exports.defaultFilterItems = defaultFilterItems;