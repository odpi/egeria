import React from 'react';
import { storiesOf } from '@storybook/react';
import OrderedList from '../OrderedList';
import ListItem from '../ListItem';
storiesOf('OrderedList', module).add('default', function () {
  return React.createElement(OrderedList, null, React.createElement(ListItem, null, "Ordered List level 1"), React.createElement(ListItem, null, "Ordered List level 1"), React.createElement(ListItem, null, "Ordered List level 1"));
}, {
  info: {
    text: "Lists consist of related content grouped together and organized vertically. Ordered lists are used to present content in a numbered list."
  }
}).add('nested', function () {
  return React.createElement(OrderedList, null, React.createElement(ListItem, null, "Ordered List level 1", React.createElement(OrderedList, {
    nested: true
  }, React.createElement(ListItem, null, "Ordered List level 2"), React.createElement(ListItem, null, "Ordered List level 2", React.createElement(OrderedList, {
    nested: true
  }, React.createElement(ListItem, null, "Ordered List level 2"), React.createElement(ListItem, null, "Ordered List level 2"))))), React.createElement(ListItem, null, "Ordered List level 1"), React.createElement(ListItem, null, "Ordered List level 1"));
}, {
  info: {
    text: "Lists consist of related content grouped together and organized vertically. Ordered lists are used to present content in a numbered list."
  }
});