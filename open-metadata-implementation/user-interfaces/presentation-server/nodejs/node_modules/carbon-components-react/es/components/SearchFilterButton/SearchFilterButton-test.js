/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import SearchFilterButton from '../SearchFilterButton';
import { mount } from 'enzyme';
import { Filter16 } from '@carbon/icons-react';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
describe('SearchFilterButton', function () {
  var wrapper = mount(React.createElement(SearchFilterButton, {
    labelText: "testlabel"
  }));
  describe('buttons', function () {
    var btn = wrapper.find('button');
    it('should have type="button"', function () {
      var type = btn.instance().getAttribute('type');
      expect(type).toEqual('button');
    });
    it('has expected class', function () {
      expect(btn.hasClass("".concat(prefix, "--search-button"))).toEqual(true);
    });
  });
  describe('icons', function () {
    it('should use "filter" icon', function () {
      var icon = wrapper.find(Filter16);
      expect(icon.length).toBe(1);
    });
  });
});