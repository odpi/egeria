"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _MultiSelect = _interopRequireDefault(require("./MultiSelect"));

var _FilterableMultiSelect = _interopRequireDefault(require("./FilterableMultiSelect"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
_FilterableMultiSelect.default.displayName = 'MultiSelect.Filterable';
_MultiSelect.default.Filterable = _FilterableMultiSelect.default;
var _default = _MultiSelect.default;
exports.default = _default;