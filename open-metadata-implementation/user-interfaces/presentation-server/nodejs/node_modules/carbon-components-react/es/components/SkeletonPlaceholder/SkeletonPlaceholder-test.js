/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import SkeletonPlaceholder from '../SkeletonPlaceholder';
import { shallow } from 'enzyme';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
describe('SkeletonPlaceholder', function () {
  var wrapper = shallow(React.createElement(SkeletonPlaceholder, null));
  it('Has the expected classes', function () {
    expect(wrapper.hasClass("".concat(prefix, "--skeleton__placeholder"))).toEqual(true);
  });
});