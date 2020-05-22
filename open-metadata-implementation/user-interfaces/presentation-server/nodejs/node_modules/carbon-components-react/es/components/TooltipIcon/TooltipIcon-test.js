function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import { mount } from 'enzyme';
import TooltipIcon from '../TooltipIcon';
describe('TooltipIcon', function () {
  var mockProps;
  beforeEach(function () {
    mockProps = {
      direction: 'bottom',
      children: React.createElement("svg", null),
      className: 'custom-class',
      tooltipText: 'tooltip text'
    };
  });
  it('should render', function () {
    var wrapper = mount(React.createElement(TooltipIcon, mockProps));
    expect(wrapper).toMatchSnapshot();
  });
  it('should add extra classes via className', function () {
    var wrapper = mount(React.createElement(TooltipIcon, mockProps));
    expect(wrapper.hasClass('custom-class')).toBe(true);
  });
  it('should have an ID on the content container by default', function () {
    var wrapper = mount(React.createElement(TooltipIcon, mockProps));
    expect(wrapper.find('.bx--assistive-text').props().id).toBeTruthy();
  });
  it('should allow the user to specify the direction', function () {
    var wrapper = mount(React.createElement(TooltipIcon, _extends({}, mockProps, {
      direction: "top"
    })));
    expect(wrapper).toMatchSnapshot();
  });
});