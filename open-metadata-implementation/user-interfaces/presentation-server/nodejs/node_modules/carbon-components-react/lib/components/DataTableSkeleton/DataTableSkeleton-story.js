"use strict";

var _react = _interopRequireDefault(require("react"));

var _react2 = require("@storybook/react");

var _addonKnobs = require("@storybook/addon-knobs");

var _DataTableSkeleton = _interopRequireDefault(require("../DataTableSkeleton"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

/* eslint-disable no-console */
var props = function props() {
  return {
    headers: (0, _addonKnobs.array)('Optional table headers (headers)', ['Name', 'Protocol', 'Port', 'Rule', 'Attached Groups'], ','),
    zebra: (0, _addonKnobs.boolean)('Use zebra stripe (zebra)', false),
    compact: (0, _addonKnobs.boolean)('Compact variant (compact)', false)
  };
};

(0, _react2.storiesOf)('DataTableSkeleton', module).addDecorator(_addonKnobs.withKnobs).add('default', function () {
  return _react.default.createElement("div", {
    style: {
      width: '800px'
    }
  }, _react.default.createElement(_DataTableSkeleton.default, props()), _react.default.createElement("br", null));
}, {
  info: {
    text: "\n            Skeleton states are used as a progressive loading state while the user waits for content to load.\n    \n            This example shows a skeleton state for a data table.\n          "
  }
});