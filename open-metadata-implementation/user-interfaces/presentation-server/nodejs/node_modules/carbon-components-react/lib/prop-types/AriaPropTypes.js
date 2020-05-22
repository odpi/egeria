"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.AriaLabelPropType = void 0;

var _propTypes = _interopRequireDefault(require("prop-types"));

var _isRequiredOneOf = _interopRequireDefault(require("./isRequiredOneOf"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
var AriaLabelPropType = (0, _isRequiredOneOf.default)({
  'aria-label': _propTypes.default.string,
  'aria-labelledby': _propTypes.default.string
});
exports.AriaLabelPropType = AriaLabelPropType;