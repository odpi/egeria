"use strict";

var _react = _interopRequireDefault(require("react"));

var _react2 = require("@storybook/react");

var _addonKnobs = require("@storybook/addon-knobs");

var _SkeletonPlaceholder = _interopRequireDefault(require("../SkeletonPlaceholder"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

/* eslint-disable no-console */
var classNames = {
  'my--skeleton__placeholder--small': 'my--skeleton__placeholder--small',
  'my--skeleton__placeholder--medium': 'my--skeleton__placeholder--medium',
  'my--skeleton__placeholder--large': 'my--skeleton__placeholder--large'
};

var props = function props() {
  return {
    className: (0, _addonKnobs.select)('Classes with different sizes', classNames)
  };
};

(0, _react2.storiesOf)('SkeletonPlaceholder', module).addDecorator(_addonKnobs.withKnobs).add('Default', function () {
  return _react.default.createElement("div", {
    style: {
      height: '250px',
      width: '250px'
    }
  }, _react.default.createElement("style", {
    dangerouslySetInnerHTML: {
      __html: "\n          .my--skeleton__placeholder--small {\n            height: 100px;\n            width: 100px;\n          }\n\n          .my--skeleton__placeholder--medium {\n            height: 150px;\n            width: 150px;\n          }\n\n          .my--skeleton__placeholder--large {\n            height: 250px;\n            width: 250px;\n          }\n        "
    }
  }), _react.default.createElement(_SkeletonPlaceholder.default, props()));
}, {
  info: {
    text: "\n        Skeleton states are used as a progressive loading state while the user waits for content to load.\n\n        By taking a height and/or width property, this component can be used when you know the exact dimensions of the incoming content, such as an image.\n\n        However, for performance reasons, it's recommended to create a class in your stylesheet to set the dimensions.\n      "
  }
});