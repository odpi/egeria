"use strict";

var _react = _interopRequireDefault(require("react"));

var _Form = _interopRequireDefault(require("../Form"));

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
describe('Form', function () {
  describe('Renders as expected', function () {
    var wrapper = (0, _enzyme.shallow)(_react.default.createElement(_Form.default, {
      className: "extra-class"
    }));
    it('renders children as expected', function () {
      expect(wrapper.find('.child').length).toBe(0);
    });
    it('renders wrapper as expected', function () {
      expect(wrapper.length).toBe(1);
    });
    it('has the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--form"))).toEqual(true);
    });
    it('renders extra classes passed in via className', function () {
      expect(wrapper.hasClass('extra-class')).toEqual(true);
    });
    it('should render wrapper as expected', function () {
      var form = (0, _enzyme.shallow)(_react.default.createElement(_Form.default, null, _react.default.createElement("div", {
        className: "test-child1"
      }), _react.default.createElement("div", {
        className: "test-child2"
      })));
      expect(form.length).toEqual(1);
    });
    it('should render children as expected', function () {
      var form1 = (0, _enzyme.shallow)(_react.default.createElement(_Form.default, null, _react.default.createElement("div", {
        className: "test-child"
      }), _react.default.createElement("div", {
        className: "test-child"
      })));
      expect(form1.find('.test-child').length).toBe(2);
    });
    it('should handle submit events', function () {
      var onSubmit = jest.fn();
      var form1 = (0, _enzyme.mount)(_react.default.createElement(_Form.default, null, _react.default.createElement("button", {
        className: "button",
        type: "submit",
        onSubmit: onSubmit
      }, "Submit")));
      var btn = form1.find('button');
      btn.simulate('submit');
      expect(onSubmit).toBeCalled();
    });
  });
});