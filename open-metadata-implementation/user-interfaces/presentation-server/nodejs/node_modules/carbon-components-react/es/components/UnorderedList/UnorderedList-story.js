/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import { storiesOf } from '@storybook/react';
import ListItem from '../ListItem';
import UnorderedList from '../UnorderedList';
storiesOf('UnorderedList', module).add('default', function () {
  return React.createElement(UnorderedList, null, React.createElement(ListItem, null, "Unordered List level 1"), React.createElement(ListItem, null, "Unordered List level 1"), React.createElement(ListItem, null, "Unordered List level 1"));
}, {
  info: {
    text: 'Lists consist of related content grouped together and organized ' + 'vertically. Unordered lists are used to present content of equal ' + 'status or value.'
  }
}).add('nested', function () {
  return React.createElement(UnorderedList, null, React.createElement(ListItem, null, "Unordered List level 1", React.createElement(UnorderedList, {
    nested: true
  }, React.createElement(ListItem, null, "Unordered List level 2"), React.createElement(ListItem, null, "Unordered List level 2", React.createElement(UnorderedList, {
    nested: true
  }, React.createElement(ListItem, null, "Unordered List level 2"), React.createElement(ListItem, null, "Unordered List level 2"))))), React.createElement(ListItem, null, "Unordered List level 1"), React.createElement(ListItem, null, "Unordered List level 1"));
}, {
  info: {
    text: 'Lists consist of related content grouped together and organized ' + 'vertically. Unordered lists are used to present content of equal ' + 'status or value.'
  }
});