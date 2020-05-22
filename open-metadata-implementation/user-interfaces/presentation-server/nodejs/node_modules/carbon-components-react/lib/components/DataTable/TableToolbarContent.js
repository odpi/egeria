"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _wrapComponent = _interopRequireDefault(require("../../tools/wrapComponent"));

var _carbonComponents = require("carbon-components");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
var prefix = _carbonComponents.settings.prefix;
var TableToolbarContent = (0, _wrapComponent.default)({
  name: 'TableToolbarContent',
  type: 'div',
  className: "".concat(prefix, "--toolbar-content")
});
var _default = TableToolbarContent;
exports.default = _default;