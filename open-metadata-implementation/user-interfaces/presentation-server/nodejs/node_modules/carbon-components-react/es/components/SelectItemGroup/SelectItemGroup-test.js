/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import SelectItemGroup from '../SelectItemGroup';
import { shallow } from 'enzyme';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
describe('SelectItemGroup', function () {
  describe('Renders as expected', function () {
    var wrapper = shallow(React.createElement(SelectItemGroup, {
      className: "extra-class",
      label: "test"
    }));
    it('should have the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--select-optgroup"))).toEqual(true);
    });
    it('Should add extra classes that are passed via className', function () {
      expect(wrapper.hasClass('extra-class')).toEqual(true);
    });
    it('Should add the label that is passed', function () {
      wrapper.setProps({
        label: 'placeholder-item'
      });
      expect(wrapper.props().label).toEqual('placeholder-item');
    });
    it('Should not be disabled by default', function () {
      expect(wrapper.props().disabled).toEqual(false);
    });
    it('should set disabled as expected', function () {
      expect(wrapper.props().disabled).toEqual(false);
      wrapper.setProps({
        disabled: true
      });
      expect(wrapper.props().disabled).toEqual(true);
    });
  });
});