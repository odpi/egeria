"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _propTypes = _interopRequireDefault(require("prop-types"));

var _react = _interopRequireDefault(require("react"));

var _iconsReact = require("@carbon/icons-react");

var _Button = _interopRequireDefault(require("../Button"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
var TableBatchAction = function TableBatchAction(props) {
  return _react.default.createElement(_Button.default, props);
};

TableBatchAction.propTypes = {
  /**
   * Specify if the button is an icon-only button
   */
  hasIconOnly: _propTypes.default.bool,

  /**
   * If specifying the `renderIcon` prop, provide a description for that icon that can
   * be read by screen readers
   */
  iconDescription: function iconDescription(props) {
    if (props.renderIcon && !props.children && !props.iconDescription) {
      return new Error('renderIcon property specified without also providing an iconDescription property.');
    }

    return undefined;
  },

  /**
   * Optional function to render your own icon in the underlying button
   */
  renderIcon: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
};
TableBatchAction.defaultProps = {
  renderIcon: _iconsReact.AddFilled16
};
var _default = TableBatchAction;
exports.default = _default;