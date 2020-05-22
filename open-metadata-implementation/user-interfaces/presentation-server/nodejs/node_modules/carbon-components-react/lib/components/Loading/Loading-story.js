"use strict";

var _react = _interopRequireDefault(require("react"));

var _react2 = require("@storybook/react");

var _addonKnobs = require("@storybook/addon-knobs");

var _Loading = _interopRequireDefault(require("../Loading"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

var props = function props() {
  return {
    active: (0, _addonKnobs.boolean)('Active (active)', true),
    withOverlay: (0, _addonKnobs.boolean)('With overlay (withOverlay)', false),
    small: (0, _addonKnobs.boolean)('Small (small)', false),
    description: (0, _addonKnobs.text)('Description (description)', 'Active loading indicator')
  };
};

(0, _react2.storiesOf)('Loading', module).addDecorator(_addonKnobs.withKnobs).add('Default', function () {
  return _react.default.createElement(_Loading.default, _extends({}, props(), {
    className: 'some-class'
  }));
}, {
  info: {
    text: "\n            Loading spinners are used when retrieving data or performing slow computations,\n            and help to notify users that loading is underway. The 'active' property is true by default;\n            set to false to end the animation.\n          "
  }
});