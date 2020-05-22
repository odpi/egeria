/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import SelectItem from '../SelectItem';
import { shallow } from 'enzyme';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
describe('SelectItem', function () {
  describe('Renders as expected', function () {
    var wrapper = shallow(React.createElement(SelectItem, {
      className: "extra-class",
      value: "test",
      text: "test"
    }));
    it('Has the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--select-option"))).toEqual(true);
    });
    it('Should add extra classes that are passed via className', function () {
      expect(wrapper.hasClass('extra-class')).toEqual(true);
    });
    it('Should add the value that is passed', function () {
      wrapper.setProps({
        value: 'placeholder-item'
      });
      expect(wrapper.props().value).toEqual('placeholder-item');
    });
    it('Should add the select item text that is passed', function () {
      wrapper.setProps({
        text: 'Pick an option'
      });
      expect(wrapper.props().children).toEqual('Pick an option');
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
    it('should set hidden as expected', function () {
      expect(wrapper.props().hidden).toEqual(false);
      wrapper.setProps({
        hidden: true
      });
      expect(wrapper.props().hidden).toEqual(true);
    });
  });
});