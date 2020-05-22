function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import invariant from 'invariant';

var itemToString = function itemToString(item) {
  !(typeof item.label === 'string') ? process.env.NODE_ENV !== "production" ? invariant(false, '[MultiSelect] the default `itemToString` method expected to receive ' + 'an item with a `label` field of type `string`. Instead received: `%s`', _typeof(item.label)) : invariant(false) : void 0;
  return item.label || '';
};

export var defaultItemToString = function defaultItemToString(item) {
  if (Array.isArray(item)) {
    return item.map(itemToString);
  }

  return itemToString(item);
};