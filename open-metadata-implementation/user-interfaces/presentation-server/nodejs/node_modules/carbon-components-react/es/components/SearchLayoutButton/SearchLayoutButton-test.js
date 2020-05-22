/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import { ListBulleted16, Grid16 } from '@carbon/icons-react';
import SearchLayoutButton from '../SearchLayoutButton';
import { shallow, mount } from 'enzyme';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
describe('SearchLayoutButton', function () {
  var wrapper = mount(React.createElement(SearchLayoutButton, {
    labelText: "testlabel"
  }));
  describe('buttons', function () {
    var btn = wrapper.find('button');
    it('should have type="button"', function () {
      var type = btn.instance().getAttribute('type');
      expect(type).toEqual('button');
    });
    it('has expected class for sort button', function () {
      expect(btn.hasClass("".concat(prefix, "--search-button"))).toEqual(true);
    });
  });
  describe('icons', function () {
    it('should use "list" icon for toggle button', function () {
      var icon = wrapper.find(ListBulleted16);
      expect(icon.length).toBe(1);
    });
    it('should use "grid" icon when format state is not "list"', function () {
      wrapper.setState({
        format: 'not-list'
      });
      var icon = wrapper.find(Grid16);
      expect(icon.length).toBe(1);
    });
    it('should support specifying the layout via props', function () {
      var wrapperWithFormatProps = mount(React.createElement(SearchLayoutButton, {
        format: "grid"
      }));
      expect(wrapperWithFormatProps.find(Grid16).length).toBe(1);
      expect(wrapperWithFormatProps.find(ListBulleted16).length).toBe(0);
      wrapperWithFormatProps.setProps({
        format: 'list'
      });
      expect(wrapperWithFormatProps.find(Grid16).length).toBe(0);
      expect(wrapperWithFormatProps.find(ListBulleted16).length).toBe(1);
    });
    it('should avoid change the format upon setting props, unless there the value actually changes', function () {
      var wrapperWithFormatProps = shallow(React.createElement(SearchLayoutButton, null));
      wrapperWithFormatProps.setProps({
        format: 'grid'
      });
      wrapperWithFormatProps.setState({
        format: 'list'
      });
      wrapperWithFormatProps.setProps({
        format: 'grid'
      });
      expect(wrapperWithFormatProps.state().format).toEqual('list');
    });
    it('should support being notified of change in layout', function () {
      var onChangeFormat = jest.fn();
      var wrapperWithFormatProps = mount(React.createElement(SearchLayoutButton, {
        format: "grid",
        onChangeFormat: onChangeFormat
      }));
      wrapperWithFormatProps.find('button').simulate('click');
      wrapperWithFormatProps.find('button').simulate('click');
      expect(onChangeFormat.mock.calls).toEqual([[{
        format: 'list'
      }], [{
        format: 'grid'
      }]]);
    });
  });
});