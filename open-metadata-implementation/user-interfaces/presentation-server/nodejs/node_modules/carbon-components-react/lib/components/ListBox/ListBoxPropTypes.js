"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.ListBoxSize = exports.ListBoxType = void 0;

var _propTypes = _interopRequireDefault(require("prop-types"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
var ListBoxType = _propTypes.default.oneOf(['default', 'inline']);

exports.ListBoxType = ListBoxType;

var ListBoxSize = _propTypes.default.oneOf(['sm', 'xl']);

exports.ListBoxSize = ListBoxSize;