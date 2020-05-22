/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import SkeletonText from '../SkeletonText';
import { shallow } from 'enzyme';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
describe('SkeletonText', function () {
  describe('Renders as expected', function () {
    var wrapper = shallow(React.createElement(SkeletonText, null));
    it('Has the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--skeleton__text"))).toEqual(true);
    });
  });
});
describe('SkeletonText Heading', function () {
  describe('Renders as expected', function () {
    var wrapper = shallow(React.createElement(SkeletonText, {
      heading: true
    }));
    it('Has the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--skeleton__heading"))).toEqual(true);
    });
  });
});