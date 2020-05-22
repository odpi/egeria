"use strict";

var _react = _interopRequireDefault(require("react"));

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
describe('ListItem', function () {
  describe('Renders as expected', function () {
    var item = (0, _enzyme.shallow)(_react.default.createElement(_ListItem.default, {
      className: "some-class"
    }, "An Item"));
    it('should render as an li element', function () {
      expect(item.find('li').length).toEqual(1);
    });
    it('should render with the appropriate classes', function () {
      expect(item.hasClass("".concat(prefix, "--list__item"))).toEqual(true);
      expect(item.hasClass('some-class')).toEqual(true);
    });
    it('should include child content', function () {
      expect(item.text()).toEqual('An Item');
    });
  });
});