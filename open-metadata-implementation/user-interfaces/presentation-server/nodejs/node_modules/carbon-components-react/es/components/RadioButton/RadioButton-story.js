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
import RadioButton from '../RadioButton';
import RadioButtonSkeleton from '../RadioButton/RadioButton.Skeleton';
var labelPositions = {
  'Left (left)': 'left',
  'Right (right)': 'right'
};

var radioProps = function radioProps() {
  return {
    className: 'some-class',
    name: text('Form item name (name)', 'test'),
    value: text('Value (value)', 'standard'),
    labelText: text('Label text (labelText)', 'Standard Radio Button'),
    labelPosition: select('Label position (labelPosition)', labelPositions, 'right'),
    disabled: boolean('Disabled (disabled)', false),
    onChange: action('onChange')
  };
};

storiesOf('RadioButton', module).addDecorator(withKnobs).add('Default', function () {
  return React.createElement(RadioButton, _extends({
    id: "radio-1"
  }, radioProps()));
}, {
  info: {
    text: "\n            Radio buttons are used when a list of two or more options are mutually exclusive,\n            meaning the user must select only one option. The example below shows how the Radio Button component\n            can be used as an uncontrolled component that is initially checked by setting the defaultChecked property\n            to true. To use the component in a controlled way, set the checked property instead.\n          "
  }
}).add('skeleton', function () {
  return React.createElement("div", null, React.createElement(RadioButtonSkeleton, null));
}, {
  info: {
    text: "\n            Placeholder skeleton state to use when content is loading.\n          "
  }
});