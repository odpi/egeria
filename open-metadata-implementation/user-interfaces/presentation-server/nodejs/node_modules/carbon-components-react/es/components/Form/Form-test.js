/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import Form from '../Form';
import { shallow, mount } from 'enzyme';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
describe('Form', function () {
  describe('Renders as expected', function () {
    var wrapper = shallow(React.createElement(Form, {
      className: "extra-class"
    }));
    it('renders children as expected', function () {
      expect(wrapper.find('.child').length).toBe(0);
    });
    it('renders wrapper as expected', function () {
      expect(wrapper.length).toBe(1);
    });
    it('has the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--form"))).toEqual(true);
    });
    it('renders extra classes passed in via className', function () {
      expect(wrapper.hasClass('extra-class')).toEqual(true);
    });
    it('should render wrapper as expected', function () {
      var form = shallow(React.createElement(Form, null, React.createElement("div", {
        className: "test-child1"
      }), React.createElement("div", {
        className: "test-child2"
      })));
      expect(form.length).toEqual(1);
    });
    it('should render children as expected', function () {
      var form1 = shallow(React.createElement(Form, null, React.createElement("div", {
        className: "test-child"
      }), React.createElement("div", {
        className: "test-child"
      })));
      expect(form1.find('.test-child').length).toBe(2);
    });
    it('should handle submit events', function () {
      var onSubmit = jest.fn();
      var form1 = mount(React.createElement(Form, null, React.createElement("button", {
        className: "button",
        type: "submit",
        onSubmit: onSubmit
      }, "Submit")));
      var btn = form1.find('button');
      btn.simulate('submit');
      expect(onSubmit).toBeCalled();
    });
  });
});