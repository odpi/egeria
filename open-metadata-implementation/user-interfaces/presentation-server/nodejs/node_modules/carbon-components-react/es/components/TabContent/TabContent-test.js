/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import TabContent from '../TabContent';
import { shallow } from 'enzyme';
describe('TabContent', function () {
  describe('renders as expected', function () {
    var wrapper = shallow(React.createElement(TabContent, null, React.createElement("div", {
      className: "child"
    }, "content"), React.createElement("div", {
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