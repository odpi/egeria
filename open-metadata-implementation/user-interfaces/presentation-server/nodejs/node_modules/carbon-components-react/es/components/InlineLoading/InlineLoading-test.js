/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import InlineLoading from '../InlineLoading';
import Loading from '../Loading';
import { mount } from 'enzyme';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
describe('Loading', function () {
  describe('Default state renders as expected', function () {
    var wrapper = mount(React.createElement(InlineLoading, {
      className: "extra-class"
    }));
    var container = wrapper.find(".".concat(prefix, "--inline-loading"));
    it('should render with a container', function () {
      expect(container.length).toEqual(1);
    });
    it('shoud render a loader by default', function () {
      expect(wrapper.find(Loading).length).toEqual(1);
    });
    it('container has the expected classes', function () {
      expect(container.hasClass("".concat(prefix, "--inline-loading"))).toEqual(true);
    });
    it('should add extra classes that are passed via className', function () {
      expect(container.hasClass('extra-class')).toEqual(true);
    });
    it('should render an animation container', function () {
      expect(wrapper.find(".".concat(prefix, "--inline-loading__animation")).length).toEqual(1);
    });
    it('should not render any text', function () {
      expect(wrapper.find(".".concat(prefix, "--inline-loading__text")).length).toEqual(0);
    });
    it('should not render the SUCCESS state', function () {
      expect(wrapper.find(".".concat(prefix, "--inline-loading__checkmark-container")).length).toEqual(0);
    });
  });
  describe('Text rendered as expected', function () {
    var wrapper = mount(React.createElement(InlineLoading, {
      className: "extra-class",
      description: "Loading Things..."
    }));
    it('should render the provided description', function () {
      expect(wrapper.find(".".concat(prefix, "--inline-loading__text")).length).toEqual(1);
      expect(wrapper.find(".".concat(prefix, "--inline-loading__text")).text()).toEqual('Loading Things...');
    });
  });
  describe('Success state should render properly', function () {
    var wrapper = mount(React.createElement(InlineLoading, {
      success: true
    }));
    it('should render the success animation', function () {
      expect(wrapper.find("svg.".concat(prefix, "--inline-loading__checkmark-container")).length).toEqual(1);
    });
    it('should not render the loading component', function () {
      expect(wrapper.find(Loading).length).toEqual(0);
    });
    it('should call the onSuccess function after a delay', function (done) {
      mount(React.createElement(InlineLoading, {
        success: true,
        onSuccess: function onSuccess() {
          done();
        }
      }));
    });
  });
});