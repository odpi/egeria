"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _carbonComponents = require("carbon-components");

var _wrapComponent = _interopRequireDefault(require("../../tools/wrapComponent"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
var prefix = _carbonComponents.settings.prefix;
var TableActionList = (0, _wrapComponent.default)({
  name: 'TableActionList',
  type: 'div',
  className: "".concat(prefix, "--action-list")
});
var _default = TableActionList;
exports.default = _default;