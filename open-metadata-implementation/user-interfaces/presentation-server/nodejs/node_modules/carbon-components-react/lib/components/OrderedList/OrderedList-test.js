"use strict";

var _react = _interopRequireDefault(require("react"));

var _OrderedList = _interopRequireDefault(require("../OrderedList"));

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
describe('OrderedList', function () {
  describe('Renders as expected', function () {
    var list = (0, _enzyme.shallow)(_react.default.createElement(_OrderedList.default, {
      className: "some-class"
    }, _react.default.createElement(_ListItem.default, null, "Item")));
    it('should be an ol element', function () {
      expect(list.find('ol').length).toEqual(1);
    });
    it('should render with the appropriate classes', function () {
      expect(list.hasClass("".concat(prefix, "--list--ordered"))).toEqual(true);
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