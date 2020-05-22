"use strict";

var _react = _interopRequireDefault(require("react"));

var _react2 = require("@storybook/react");

var _iconsReact = require("@carbon/icons-react");

var _addonKnobs = require("@storybook/addon-knobs");

var _TooltipIcon = _interopRequireDefault(require("../TooltipIcon"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
var directions = {
  'Top (top)': 'top',
  'Right (right)': 'right',
  'Bottom (bottom)': 'bottom',
  'Left (left)': 'left'
};
var alignments = {
  'Start (start)': 'start',
  'Center (center)': 'center',
  'End (end)': 'end'
};

var props = function props() {
  return {
    direction: (0, _addonKnobs.select)('Tooltip direction (direction)', directions, 'bottom'),
    align: (0, _addonKnobs.select)('Tooltip alignment (align)', alignments, 'center'),
    tooltipText: (0, _addonKnobs.text)('Tooltip content (tooltipText)', 'Filter')
  };
};

(0, _react2.storiesOf)('TooltipIcon', module).addDecorator(_addonKnobs.withKnobs).add('default', function () {
  return _react.default.createElement(_TooltipIcon.default, props(), _react.default.createElement(_iconsReact.Filter16, null));
}, {
  info: {
    text: "\n          Icon tooltip is for short single line of text describing an icon.\n          Icon tooltip does not use any JavaScript. No label should be added to this variation.\n          If there are actions a user can take in the tooltip (e.g. a link or a button), use interactive tooltip.\n        "
  }
});