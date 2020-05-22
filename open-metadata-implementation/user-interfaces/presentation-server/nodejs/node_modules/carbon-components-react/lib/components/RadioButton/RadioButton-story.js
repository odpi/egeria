"use strict";

var _react = _interopRequireDefault(require("react"));

var _react2 = require("@storybook/react");

var _addonActions = require("@storybook/addon-actions");

var _addonKnobs = require("@storybook/addon-knobs");

var _RadioButton = _interopRequireDefault(require("../RadioButton"));

var _RadioButton2 = _interopRequireDefault(require("../RadioButton/RadioButton.Skeleton"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

var labelPositions = {
  'Left (left)': 'left',
  'Right (right)': 'right'
};

var radioProps = function radioProps() {
  return {
    className: 'some-class',
    name: (0, _addonKnobs.text)('Form item name (name)', 'test'),
    value: (0, _addonKnobs.text)('Value (value)', 'standard'),
    labelText: (0, _addonKnobs.text)('Label text (labelText)', 'Standard Radio Button'),
    labelPosition: (0, _addonKnobs.select)('Label position (labelPosition)', labelPositions, 'right'),
    disabled: (0, _addonKnobs.boolean)('Disabled (disabled)', false),
    onChange: (0, _addonActions.action)('onChange')
  };
};

(0, _react2.storiesOf)('RadioButton', module).addDecorator(_addonKnobs.withKnobs).add('Default', function () {
  return _react.default.createElement(_RadioButton.default, _extends({
    id: "radio-1"
  }, radioProps()));
}, {
  info: {
    text: "\n            Radio buttons are used when a list of two or more options are mutually exclusive,\n            meaning the user must select only one option. The example below shows how the Radio Button component\n            can be used as an uncontrolled component that is initially checked by setting the defaultChecked property\n            to true. To use the component in a controlled way, set the checked property instead.\n          "
  }
}).add('skeleton', function () {
  return _react.default.createElement("div", null, _react.default.createElement(_RadioButton2.default, null));
}, {
  info: {
    text: "\n            Placeholder skeleton state to use when content is loading.\n          "
  }
});