"use strict";

var _react = _interopRequireDefault(require("react"));

var _react2 = require("@storybook/react");

var _addonKnobs = require("@storybook/addon-knobs");

var _TooltipDefinition = _interopRequireDefault(require("../TooltipDefinition"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
var directions = {
  'Bottom (bottom)': 'bottom',
  'Top (top)': 'top'
};
var alignments = {
  'Start (start)': 'start',
  'Center (center)': 'center',
  'End (end)': 'end'
};

var props = function props() {
  return {
    triggerClassName: (0, _addonKnobs.text)('Trigger element CSS class name (triggerClassName)', ''),
    direction: (0, _addonKnobs.select)('Tooltip direction (direction)', directions, 'bottom'),
    align: (0, _addonKnobs.select)('Tooltip alignment to trigger button (align)', alignments, 'start'),
    tooltipText: (0, _addonKnobs.text)('Tooltip content (tooltipText)', 'Brief description of the dotted, underlined word above.')
  };
};

(0, _react2.storiesOf)('TooltipDefinition', module).addDecorator(_addonKnobs.withKnobs).add('default', function () {
  return _react.default.createElement("div", {
    style: {
      marginTop: '2rem'
    }
  }, _react.default.createElement(_TooltipDefinition.default, props(), "Definition Tooltip"));
}, {
  info: {
    text: "\n          Definition tooltip is for regular use case of tooltip, e.g. giving the user more text information about something, like defining a word.\n          This works better than the interactive tooltip in regular use cases because the info icon used in interactive tooltip can be repetitive when it\u2019s shown several times on a page.\n          Definition tooltip does not use any JavaScript. If there are actions a user can take in the tooltip (e.g. a link or a button), use interactive tooltip.\n        "
  }
});