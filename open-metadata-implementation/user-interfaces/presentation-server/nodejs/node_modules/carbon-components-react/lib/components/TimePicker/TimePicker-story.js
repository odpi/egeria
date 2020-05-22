"use strict";

var _react = _interopRequireDefault(require("react"));

var _react2 = require("@storybook/react");

var _addonActions = require("@storybook/addon-actions");

var _addonKnobs = require("@storybook/addon-knobs");

var _TimePicker = _interopRequireDefault(require("../TimePicker"));

var _TimePickerSelect = _interopRequireDefault(require("../TimePickerSelect"));

var _SelectItem = _interopRequireDefault(require("../SelectItem"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

var props = {
  timepicker: function timepicker() {
    return {
      pattern: (0, _addonKnobs.text)('Regular expression for the value (pattern in <TimePicker>)', '(1[012]|[1-9]):[0-5][0-9](\\s)?'),
      placeholder: (0, _addonKnobs.text)('Placeholder text (placeholder in <TimePicker>)', 'hh:mm'),
      disabled: (0, _addonKnobs.boolean)('Disabled (disabled in <TimePicker>)', false),
      light: (0, _addonKnobs.boolean)('Light variant (light in <TimePicker>)', false),
      hideLabel: (0, _addonKnobs.boolean)('No label (hideLabel in <TimePicker>)', false),
      labelText: (0, _addonKnobs.text)('Label text (labelText in <TimePicker>)', 'Select a time'),
      invalid: (0, _addonKnobs.boolean)('Show form validation UI (invalid in <TimePicker>)', false),
      invalidText: (0, _addonKnobs.text)('Form validation UI content (invalidText in <TimePicker>)', 'A valid value is required'),
      maxLength: (0, _addonKnobs.number)('Maximum length (maxLength in <TimePicker>)', 5),
      onClick: (0, _addonActions.action)('onClick'),
      onChange: (0, _addonActions.action)('onChange'),
      onBlur: (0, _addonActions.action)('onBlur')
    };
  },
  select: function select() {
    return {
      disabled: (0, _addonKnobs.boolean)('Disabled (disabled in <TimePickerSelect>)', false),
      hideLabel: (0, _addonKnobs.boolean)('No label (hideLabel in <TimePickerSelect>)', true),
      labelText: (0, _addonKnobs.text)('Label text (labelText in <TimePickerSelect>)', 'Please select'),
      iconDescription: (0, _addonKnobs.text)('Trigger icon description (iconDescription in <TimePickerSelect>)', 'open list of options')
    };
  }
};
(0, _react2.storiesOf)('TimePicker', module).addDecorator(_addonKnobs.withKnobs).add('Default', function () {
  var selectProps = props.select();
  return _react.default.createElement(_TimePicker.default, _extends({
    id: "time-picker"
  }, props.timepicker()), _react.default.createElement(_TimePickerSelect.default, _extends({
    id: "time-picker-select-1"
  }, selectProps), _react.default.createElement(_SelectItem.default, {
    value: "AM",
    text: "AM"
  }), _react.default.createElement(_SelectItem.default, {
    value: "PM",
    text: "PM"
  })), _react.default.createElement(_TimePickerSelect.default, _extends({
    id: "time-picker-select-2"
  }, selectProps), _react.default.createElement(_SelectItem.default, {
    value: "Time zone 1",
    text: "Time zone 1"
  }), _react.default.createElement(_SelectItem.default, {
    value: "Time zone 2",
    text: "Time zone 2"
  })));
}, {
  info: {
    text: "\n            The time picker allow users to select a time.\n          "
  }
});