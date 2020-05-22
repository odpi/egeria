"use strict";

var _react = _interopRequireDefault(require("react"));

var _react2 = require("@storybook/react");

var _addonActions = require("@storybook/addon-actions");

var _addonKnobs = require("@storybook/addon-knobs");

var _CopyButton = _interopRequireDefault(require("../CopyButton"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
var props = function props() {
  return {
    feedback: (0, _addonKnobs.text)('The text shown upon clicking (feedback)', 'Copied!'),
    feedbackTimeout: (0, _addonKnobs.number)('How long the text is shown upon clicking (feedbackTimeout)', 3000),
    iconDescription: (0, _addonKnobs.text)('Feedback icon description (iconDescription)', 'Copy to clipboard'),
    onClick: (0, _addonActions.action)('onClick')
  };
};

(0, _react2.storiesOf)('CopyButton', module).addDecorator(_addonKnobs.withKnobs).add('Default', function () {
  return _react.default.createElement(_CopyButton.default, props());
}, {
  info: {
    text: 'The copy button can be used when the user needs to copy information, such as a code snippet, to their clipboard.'
  }
});