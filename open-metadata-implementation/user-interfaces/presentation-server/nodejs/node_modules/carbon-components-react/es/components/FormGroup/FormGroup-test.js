/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import FormGroup from '../FormGroup';
import { shallow } from 'enzyme';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
describe('FormGroup', function () {
  describe('Renders as expected', function () {
    var wrapper = shallow(React.createElement(FormGroup, {
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
      var formgroup = shallow(React.createElement(FormGroup, {
        legendText: "legendtest",
        invalid: true
      }));
      expect(formgroup.props()['data-invalid']).toBe('');
    });
    it('should render wrapper as expected', function () {
      var formGroup = shallow(React.createElement(FormGroup, {
        legendText: "legendtest"
      }, React.createElement("div", {
        className: "test-child1"
      }), React.createElement("div", {
        className: "test-child2"
      })));
      expect(formGroup.length).toEqual(1);
    });
    it('should render children as expected', function () {
      var formGroup1 = shallow(React.createElement(FormGroup, {
        legendText: "legendtest"
      }, React.createElement("div", {
        className: "test-child"
      }), React.createElement("div", {
        className: "test-child"
      })));
      expect(formGroup1.find('.test-child').length).toBe(2);
    });
  });
});