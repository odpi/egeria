/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import UnorderedList from '../UnorderedList';
import ListItem from '../ListItem';
import { shallow } from 'enzyme';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
describe('UnorderedList', function () {
  describe('Renders as expected', function () {
    var list = shallow(React.createElement(UnorderedList, {
      className: "some-class"
    }, React.createElement(ListItem, null, "Item")));
    it('should be a ul element', function () {
      expect(list.find('ul').length).toEqual(1);
    });
    it('should render with the appropriate classes', function () {
      expect(list.hasClass("".concat(prefix, "--list--unordered"))).toEqual(true);
      expect(list.hasClass('some-class')).toEqual(true);
    });
    it('should render children as expected', function () {
      expect(list.find(ListItem).length).toEqual(1);
    });
    it('should render nested lists', function () {
      list.setProps({
        nested: true
      });
      expect(list.hasClass("".concat(prefix, "--list--nested"))).toEqual(true);
      list.setProps({
        nested: false
      });
      expect(list.hasClass("".concat(prefix, "--list--nested"))).toEqual(false);
    });
  });
});