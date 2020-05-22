/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import Link from '../Link';
import { shallow } from 'enzyme';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
describe('Link', function () {
  describe('Renders as expected', function () {
    var link = shallow(React.createElement(Link, {
      href: "www.google.com",
      className: "some-class"
    }, "A simple link"));
    it('should use the appropriate link class', function () {
      expect(link.name()).toEqual('a');
      expect(link.hasClass("".concat(prefix, "--link"))).toEqual(true);
    });
    it('should inherit the href property', function () {
      expect(link.props().href).toEqual('www.google.com');
    });
    it('should include child content', function () {
      expect(link.text()).toEqual('A simple link');
    });
    it('should all for custom classes to be applied', function () {
      expect(link.hasClass('some-class')).toEqual(true);
    });
    it('should support disabled link', function () {
      link.setProps({
        disabled: true
      });
      expect(link.name()).toEqual('p');
      expect(link.hasClass("".concat(prefix, "--link--disabled"))).toEqual(true);
    });
    it('should support inline link', function () {
      link.setProps({
        inline: true
      });
      expect(link.hasClass("".concat(prefix, "--link--inline"))).toEqual(true);
    });
  });
});