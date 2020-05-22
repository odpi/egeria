"use strict";

var _react = _interopRequireDefault(require("react"));

var _FormGroup = _interopRequireDefault(require("../FormGroup"));

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
describe('FormGroup', function () {
  describe('Renders as expected', function () {
    var wrapper = (0, _enzyme.shallow)(_react.default.createElement(_FormGroup.default, {
      className: "extra-class",
      legendText: "legendtest"
    }));
    it('renders children as expected', function () {
      expect(wrapper.find('.child').length).toBe(0);
    });
    it('renders wrapper as expected', function () {
      expect(wrapper.length).toBe(1);
    });
    it('has the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--fieldset"))).toEqual(true);
    });
    it('renders extra classes passed in via className', function () {
      expect(wrapper.hasClass('extra-class')).toEqual(true);
    });
    it('should not render the data-invalid property by default', function () {
      expect(wrapper.props()['data-invalid']).toBe(undefined);
    });
    it('should render the data-invalid attribute when invalid is set', function () {
      var formgroup = (0, _enzyme.shallow)(_react.default.createElement(_FormGroup.default, {
        legendText: "legendtest",
        invalid: true
      }));
      expect(formgroup.props()['data-invalid']).toBe('');
    });
    it('should render wrapper as expected', function () {
      var formGroup = (0, _enzyme.shallow)(_react.default.createElement(_FormGroup.default, {
        legendText: "legendtest"
      }, _react.default.createElement("div", {
        className: "test-child1"
      }), _react.default.createElement("div", {
        className: "test-child2"
      })));
      expect(formGroup.length).toEqual(1);
    });
    it('should render children as expected', function () {
      var formGroup1 = (0, _enzyme.shallow)(_react.default.createElement(_FormGroup.default, {
        legendText: "legendtest"
      }, _react.default.createElement("div", {
        className: "test-child"
      }), _react.default.createElement("div", {
        className: "test-child"
      })));
      expect(formGroup1.find('.test-child').length).toBe(2);
    });
  });
});