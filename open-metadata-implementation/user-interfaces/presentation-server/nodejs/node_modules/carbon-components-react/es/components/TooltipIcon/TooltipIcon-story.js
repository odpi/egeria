/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import { storiesOf } from '@storybook/react';
import { Filter16 } from '@carbon/icons-react';
import { withKnobs, select, text } from '@storybook/addon-knobs';
import TooltipIcon from '../TooltipIcon';
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
    direction: select('Tooltip direction (direction)', directions, 'bottom'),
    align: select('Tooltip alignment (align)', alignments, 'center'),
    tooltipText: text('Tooltip content (tooltipText)', 'Filter')
  };
};

storiesOf('TooltipIcon', module).addDecorator(withKnobs).add('default', function () {
  return React.createElement(TooltipIcon, props(), React.createElement(Filter16, null));
}, {
  info: {
    text: "\n          Icon tooltip is for short single line of text describing an icon.\n          Icon tooltip does not use any JavaScript. No label should be added to this variation.\n          If there are actions a user can take in the tooltip (e.g. a link or a button), use interactive tooltip.\n        "
  }
});