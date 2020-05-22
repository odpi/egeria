/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import ListItem from '../ListItem';
import { shallow } from 'enzyme';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
describe('ListItem', function () {
  describe('Renders as expected', function () {
    var item = shallow(React.createElement(ListItem, {
      className: "some-class"
    }, "An Item"));
    it('should render as an li element', function () {
      expect(item.find('li').length).toEqual(1);
    });
    it('should render with the appropriate classes', function () {
      expect(item.hasClass("".concat(prefix, "--list__item"))).toEqual(true);
      expect(item.hasClass('some-class')).toEqual(true);
    });
    it('should include child content', function () {
      expect(item.text()).toEqual('An Item');
    });
  });
});