"use strict";

var _react = _interopRequireDefault(require("react"));

var _react2 = require("@storybook/react");

var _addonActions = require("@storybook/addon-actions");

var _addonKnobs = require("@storybook/addon-knobs");

var _RadioButtonGroup = _interopRequireDefault(require("../RadioButtonGroup"));

var _RadioButton = _interopRequireDefault(require("../RadioButton"));

var _FormGroup = _interopRequireDefault(require("../FormGroup"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

var values = {
  standard: 'standard',
  'default-selected': 'default-selected',
  disabled: 'disabled'
};
var orientations = {
  'Horizontal (horizontal)': 'horizontal',
  'Vertical (vertical)': 'vertical'
};
var labelPositions = {
  'Left (left)': 'left',
  'Right (right)': 'right'
};
var props = {
  group: function group() {
    return {
      name: (0, _addonKnobs.text)('The form control name (name in <RadioButtonGroup>)', 'radio-button-group'),
      valueSelected: (0, _addonKnobs.select)('Value of the selected button (valueSelected in <RadioButtonGroup>)', values, 'default-selected'),
      orientation: (0, _addonKnobs.select)('Radio button orientation (orientation)', orientations, 'horizontal'),
      labelPosition: (0, _addonKnobs.select)('Label position (labelPosition)', labelPositions, 'right'),
      onChange: (0, _addonActions.action)('onChange')
    };
  },
  radio: function radio() {
    return {
      className: 'some-class',
      disabled: (0, _addonKnobs.boolean)('Disabled (disabled in <RadioButton>)', false),
      labelText: (0, _addonKnobs.text)('Label text (labelText in <RadioButton>)', 'Radio button label')
    };
  }
};
(0, _react2.storiesOf)('RadioButtonGroup', module).addDecorator(_addonKnobs.withKnobs).add('Default', function () {
  var radioProps = props.radio();
  return _react.default.createElement(_FormGroup.default, {
    legendText: "Radio Button heading"
  }, _react.default.createElement(_RadioButtonGroup.default, _extends({
    defaultSelected: "default-selected",
    legend: "Group Legend"
  }, props.group()), _react.default.createElement(_RadioButton.default, _extends({
    value: "standard",
    id: "radio-1"
  }, radioProps)), _react.default.createElement(_RadioButton.default, _extends({
    value: "default-selected",
    id: "radio-2"
  }, radioProps)), _react.default.createElement(_RadioButton.default, _extends({
    value: "disabled",
    id: "radio-3"
  }, radioProps))));
}, {
  info: {
    text: "\n            The example below shows a Radio Button Group component with a default selected Radio Button.\n            Although you can set the checked prop on the Radio Button, when using the Radio Button component\n            as a child of the Radio Button Group, either set the defaultSelected or valueSelected which will\n            automatically set the selected prop on the corresponding Radio Button component.\n    \n            Use defaultSelected when you want a radio button to be selected initially, but don't need to set it\n            at a later time. If you do need to set it dynamically at a later time, then use the valueSelected property instead.\n          "
  }
});