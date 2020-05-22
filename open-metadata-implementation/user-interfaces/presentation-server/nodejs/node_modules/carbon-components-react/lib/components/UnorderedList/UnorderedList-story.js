"use strict";

var _react = _interopRequireDefault(require("react"));

var _react2 = require("@storybook/react");

var _ListItem = _interopRequireDefault(require("../ListItem"));

var _UnorderedList = _interopRequireDefault(require("../UnorderedList"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
(0, _react2.storiesOf)('UnorderedList', module).add('default', function () {
  return _react.default.createElement(_UnorderedList.default, null, _react.default.createElement(_ListItem.default, null, "Unordered List level 1"), _react.default.createElement(_ListItem.default, null, "Unordered List level 1"), _react.default.createElement(_ListItem.default, null, "Unordered List level 1"));
}, {
  info: {
    text: 'Lists consist of related content grouped together and organized ' + 'vertically. Unordered lists are used to present content of equal ' + 'status or value.'
  }
}).add('nested', function () {
  return _react.default.createElement(_UnorderedList.default, null, _react.default.createElement(_ListItem.default, null, "Unordered List level 1", _react.default.createElement(_UnorderedList.default, {
    nested: true
  }, _react.default.createElement(_ListItem.default, null, "Unordered List level 2"), _react.default.createElement(_ListItem.default, null, "Unordered List level 2", _react.default.createElement(_UnorderedList.default, {
    nested: true
  }, _react.default.createElement(_ListItem.default, null, "Unordered List level 2"), _react.default.createElement(_ListItem.default, null, "Unordered List level 2"))))), _react.default.createElement(_ListItem.default, null, "Unordered List level 1"), _react.default.createElement(_ListItem.default, null, "Unordered List level 1"));
}, {
  info: {
    text: 'Lists consist of related content grouped together and organized ' + 'vertically. Unordered lists are used to present content of equal ' + 'status or value.'
  }
});