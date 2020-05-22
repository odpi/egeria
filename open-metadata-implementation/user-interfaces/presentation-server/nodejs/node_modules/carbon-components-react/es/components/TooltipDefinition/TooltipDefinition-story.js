/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, select, text } from '@storybook/addon-knobs';
import TooltipDefinition from '../TooltipDefinition';
var directions = {
  'Bottom (bottom)': 'bottom',
  'Top (top)': 'top'
};
var alignments = {
  'Start (start)': 'start',
  'Center (center)': 'center',
  'End (end)': 'end'
};

var props = function props() {
  return {
    triggerClassName: text('Trigger element CSS class name (triggerClassName)', ''),
    direction: select('Tooltip direction (direction)', directions, 'bottom'),
    align: select('Tooltip alignment to trigger button (align)', alignments, 'start'),
    tooltipText: text('Tooltip content (tooltipText)', 'Brief description of the dotted, underlined word above.')
  };
};

storiesOf('TooltipDefinition', module).addDecorator(withKnobs).add('default', function () {
  return React.createElement("div", {
    style: {
      marginTop: '2rem'
    }
  }, React.createElement(TooltipDefinition, props(), "Definition Tooltip"));
}, {
  info: {
    text: "\n          Definition tooltip is for regular use case of tooltip, e.g. giving the user more text information about something, like defining a word.\n          This works better than the interactive tooltip in regular use cases because the info icon used in interactive tooltip can be repetitive when it\u2019s shown several times on a page.\n          Definition tooltip does not use any JavaScript. If there are actions a user can take in the tooltip (e.g. a link or a button), use interactive tooltip.\n        "
  }
});