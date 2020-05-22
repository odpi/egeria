"use strict";

var _react = _interopRequireDefault(require("react"));

var _UnorderedList = _interopRequireDefault(require("../UnorderedList"));

var _ListItem = _interopRequireDefault(require("../ListItem"));

var _enzyme = require("enzyme");

var _carbonComponents = require("carbon-components");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
var prefix = _carbonComponents.settings.prefix;
describe('UnorderedList', function () {
  describe('Renders as expected', function () {
    var list = (0, _enzyme.shallow)(_react.default.createElement(_UnorderedList.default, {
      className: "some-class"
    }, _react.default.createElement(_ListItem.default, null, "Item")));
    it('should be a ul element', function () {
      expect(list.find('ul').length).toEqual(1);
    });
    it('should render with the appropriate classes', function () {
      expect(list.hasClass("".concat(prefix, "--list--unordered"))).toEqual(true);
      expect(list.hasClass('some-class')).toEqual(true);
    });
    it('should render children as expected', function () {
      expect(list.find(_ListItem.default).length).toEqual(1);
    });
    it('should render nested lists', function () {
      list.setProps({
        nested: true
      });
      expect(list.hasClass("".concat(prefix, "--list--nested"))).toEqual(true);
      list.setProps({
        nested: false
      });
      expect(list.hasClass("".concat(prefix, "--list--nested"))).toEqual(false);
    });
  });
});