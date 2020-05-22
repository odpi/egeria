/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import DangerButton from '../DangerButton';
import { shallow, mount } from 'enzyme';
import { Search16 } from '@carbon/icons-react';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
describe('DangerButton', function () {
  describe('Renders as expected', function () {
    var wrapper = shallow(React.createElement(DangerButton, {
      small: true,
      className: "extra-class"
    }, React.createElement("div", {
      className: "child"
    }, "Test"), React.createElement("div", {
      className: "child"
    }, "Test")));
    it('Renders children as expected', function () {
      expect(wrapper.find('.child').length).toBe(2);
      expect(wrapper.find('svg').length).toBe(0);
    });
    it('Renders wrapper as expected', function () {
      expect(wrapper.length).toBe(1);
    });
    it('Has kind="danger"', function () {
      expect(wrapper.props().kind).toEqual('danger');
    });
    it('Has small property', function () {
      expect(wrapper.props().small).toEqual(true);
    });
    it('Should add extra classes that are passed via className', function () {
      expect(wrapper.hasClass('extra-class')).toEqual(true);
    });
    describe('Renders icon buttons', function () {
      var iconButton = mount(React.createElement(DangerButton, {
        renderIcon: Search16,
        iconDescription: "Search"
      }, "Search"));
      var icon = iconButton.find('svg');
      it('should have the appropriate icon', function () {
        expect(icon.hasClass("".concat(prefix, "--btn__icon"))).toBe(true);
      });
    });
  });
});