function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import { storiesOf } from '@storybook/react';
import { action } from '@storybook/addon-actions';
import { withKnobs, boolean, select, text } from '@storybook/addon-knobs';
import RadioButtonGroup from '../RadioButtonGroup';
import RadioButton from '../RadioButton';
import FormGroup from '../FormGroup';
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
      name: text('The form control name (name in <RadioButtonGroup>)', 'radio-button-group'),
      valueSelected: select('Value of the selected button (valueSelected in <RadioButtonGroup>)', values, 'default-selected'),
      orientation: select('Radio button orientation (orientation)', orientations, 'horizontal'),
      labelPosition: select('Label position (labelPosition)', labelPositions, 'right'),
      onChange: action('onChange')
    };
  },
  radio: function radio() {
    return {
      className: 'some-class',
      disabled: boolean('Disabled (disabled in <RadioButton>)', false),
      labelText: text('Label text (labelText in <RadioButton>)', 'Radio button label')
    };
  }
};
storiesOf('RadioButtonGroup', module).addDecorator(withKnobs).add('Default', function () {
  var radioProps = props.radio();
  return React.createElement(FormGroup, {
    legendText: "Radio Button heading"
  }, React.createElement(RadioButtonGroup, _extends({
    defaultSelected: "default-selected",
    legend: "Group Legend"
  }, props.group()), React.createElement(RadioButton, _extends({
    value: "standard",
    id: "radio-1"
  }, radioProps)), React.createElement(RadioButton, _extends({
    value: "default-selected",
    id: "radio-2"
  }, radioProps)), React.createElement(RadioButton, _extends({
    value: "disabled",
    id: "radio-3"
  }, radioProps))));
}, {
  info: {
    text: "\n            The example below shows a Radio Button Group component with a default selected Radio Button.\n            Although you can set the checked prop on the Radio Button, when using the Radio Button component\n            as a child of the Radio Button Group, either set the defaultSelected or valueSelected which will\n            automatically set the selected prop on the corresponding Radio Button component.\n    \n            Use defaultSelected when you want a radio button to be selected initially, but don't need to set it\n            at a later time. If you do need to set it dynamically at a later time, then use the valueSelected property instead.\n          "
  }
});