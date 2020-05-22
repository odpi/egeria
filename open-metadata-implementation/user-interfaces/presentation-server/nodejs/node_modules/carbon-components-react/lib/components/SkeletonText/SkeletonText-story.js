"use strict";

var _react = _interopRequireDefault(require("react"));

var _react2 = require("@storybook/react");

var _addonKnobs = require("@storybook/addon-knobs");

var _SkeletonText = _interopRequireDefault(require("../SkeletonText"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

/* eslint-disable no-console */
var widths = {
  '100%': '100%',
  '250px': '250px'
};

var props = function props() {
  return {
    heading: (0, _addonKnobs.boolean)('Skeleton text at a larger size (heading)'),
    paragraph: (0, _addonKnobs.boolean)('Use multiple lines of text (paragraph)'),
    lineCount: (0, _addonKnobs.number)('The number of lines in a paragraph (lineCount)', 3),
    width: (0, _addonKnobs.select)('Width (in px or %) of single line of text or max-width of paragraph lines (width)', widths, '100%')
  };
};

(0, _react2.storiesOf)('SkeletonText', module).addDecorator(_addonKnobs.withKnobs).add('Default', function () {
  return _react.default.createElement("div", {
    style: {
      width: '300px'
    }
  }, _react.default.createElement(_SkeletonText.default, props()));
}, {
  info: {
    text: "\n            Skeleton states are used as a progressive loading state while the user waits for content to load.\n          "
  }
});