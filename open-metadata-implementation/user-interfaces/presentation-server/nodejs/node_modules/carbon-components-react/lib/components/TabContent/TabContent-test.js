"use strict";

var _react = _interopRequireDefault(require("react"));

var _TabContent = _interopRequireDefault(require("../TabContent"));

var _enzyme = require("enzyme");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
describe('TabContent', function () {
  describe('renders as expected', function () {
    var wrapper = (0, _enzyme.shallow)(_react.default.createElement(_TabContent.default, null, _react.default.createElement("div", {
      className: "child"
    }, "content"), _react.default.createElement("div", {
      className: "child"
    }, "content")));
    it('renders children as expected', function () {
      expect(wrapper.props().children.length).toEqual(2);
    });
    it('sets selected if passed in via props', function () {
      wrapper.setProps({
        selected: true
      });
      expect(wrapper.props().selected).toEqual(true);
    });
    it('sets selected and hidden props with opposite boolean values', function () {
      wrapper.setProps({
        selected: true
      });
      expect(wrapper.props().hidden).toEqual(false);
    });
  });
});