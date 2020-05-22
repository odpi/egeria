"use strict";

var _react = _interopRequireDefault(require("react"));

var _react2 = require("@storybook/react");

var _OrderedList = _interopRequireDefault(require("../OrderedList"));

var _ListItem = _interopRequireDefault(require("../ListItem"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

(0, _react2.storiesOf)('OrderedList', module).add('default', function () {
  return _react.default.createElement(_OrderedList.default, null, _react.default.createElement(_ListItem.default, null, "Ordered List level 1"), _react.default.createElement(_ListItem.default, null, "Ordered List level 1"), _react.default.createElement(_ListItem.default, null, "Ordered List level 1"));
}, {
  info: {
    text: "Lists consist of related content grouped together and organized vertically. Ordered lists are used to present content in a numbered list."
  }
}).add('nested', function () {
  return _react.default.createElement(_OrderedList.default, null, _react.default.createElement(_ListItem.default, null, "Ordered List level 1", _react.default.createElement(_OrderedList.default, {
    nested: true
  }, _react.default.createElement(_ListItem.default, null, "Ordered List level 2"), _react.default.createElement(_ListItem.default, null, "Ordered List level 2", _react.default.createElement(_OrderedList.default, {
    nested: true
  }, _react.default.createElement(_ListItem.default, null, "Ordered List level 2"), _react.default.createElement(_ListItem.default, null, "Ordered List level 2"))))), _react.default.createElement(_ListItem.default, null, "Ordered List level 1"), _react.default.createElement(_ListItem.default, null, "Ordered List level 1"));
}, {
  info: {
    text: "Lists consist of related content grouped together and organized vertically. Ordered lists are used to present content in a numbered list."
  }
});