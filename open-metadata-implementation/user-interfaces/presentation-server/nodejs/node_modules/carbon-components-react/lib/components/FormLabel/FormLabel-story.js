"use strict";

var _react = _interopRequireDefault(require("react"));

var _react2 = require("@storybook/react");

var _FormLabel = _interopRequireDefault(require("./FormLabel"));

var _Tooltip = _interopRequireDefault(require("../Tooltip"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
var additionalProps = {
  className: 'some-class'
};
(0, _react2.storiesOf)('FormLabel', module).add('Default', function () {
  return _react.default.createElement(_FormLabel.default, additionalProps, "Label");
}, {
  info: {
    text: 'Form label.'
  }
}).add('With tooltip', function () {
  return _react.default.createElement(_FormLabel.default, additionalProps, _react.default.createElement(_Tooltip.default, {
    triggerText: "Label"
  }, "This is the content of the tooltip."));
}, {
  info: {
    text: 'Form label with tooltip.'
  }
});