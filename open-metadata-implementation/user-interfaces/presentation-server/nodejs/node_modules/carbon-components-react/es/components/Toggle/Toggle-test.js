/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import Toggle from '../Toggle';
import ToggleSkeleton from '../Toggle/Toggle.Skeleton';
import { mount, shallow } from 'enzyme';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
describe('Toggle', function () {
  describe('Renders as expected', function () {
    var wrapper = mount(React.createElement(Toggle, {
      id: "toggle-1"
    }));
    var input = wrapper.find('input');
    it('Switch and label Ids should match', function () {
      var toggleLabel = wrapper.find(".".concat(prefix, "--toggle__label"));
      expect(input.id).toEqual(toggleLabel.htmlFor);
    });
    it('should set defaultChecked as expected', function () {
      expect(input.props().defaultChecked).toEqual(false);
      wrapper.setProps({
        defaultToggled: true
      });
      expect(wrapper.find('input').props().defaultChecked).toEqual(true);
    });
    it('Can set defaultToggled state', function () {
      wrapper.setProps({
        defaultToggled: true
      });
      expect(wrapper.find(".".concat(prefix, "--toggle-input")).props().defaultChecked).toEqual(true);
    });
    it('Should add extra classes that are passed via className', function () {
      wrapper.setProps({
        className: 'extra-class'
      });
      expect(wrapper.find('div').hasClass('extra-class')).toEqual(true);
    });
    it('Can be disabled', function () {
      wrapper.setProps({
        disabled: true
      });
      expect(wrapper.find(".".concat(prefix, "--toggle-input")).props().disabled).toEqual(true);
    });
    it('Can have a labelA', function () {
      wrapper.setProps({
        labelA: 'labelA-test'
      });
      expect(wrapper.find(".".concat(prefix, "--toggle__text--off")).text()).toEqual('labelA-test');
    });
    it('Can have a labelB', function () {
      wrapper.setProps({
        labelB: 'labelB-test'
      });
      expect(wrapper.find(".".concat(prefix, "--toggle__text--on")).text()).toEqual('labelB-test');
    });
  });
  it('toggled prop sets checked prop on input', function () {
    var wrapper = mount(React.createElement(Toggle, {
      id: "test",
      toggled: true
    }));

    var input = function input() {
      return wrapper.find('input');
    };

    expect(input().props().checked).toEqual(true);
    wrapper.setProps({
      toggled: false
    });
    expect(input().props().checked).toEqual(false);
  });
  describe('events', function () {
    it('passes along onChange to <input>', function () {
      var onChange = jest.fn();
      var id = 'test-input';
      var wrapper = mount(React.createElement(Toggle, {
        id: id,
        onChange: onChange
      }));
      var input = wrapper.find('input');
      var inputElement = input.instance();
      inputElement.checked = true;
      wrapper.find('input').simulate('change');
      expect(onChange.mock.calls.map(function (call) {
        return call.map(function (arg, i) {
          return i > 0 ? arg : arg.target;
        });
      })).toEqual([[inputElement]]);
    });
    it('should invoke onToggle with expected arguments', function () {
      var onToggle = jest.fn();
      var id = 'test-input';
      var wrapper = mount(React.createElement(Toggle, {
        id: id,
        onToggle: onToggle
      }));
      var input = wrapper.find('input');
      var inputElement = input.instance();
      inputElement.checked = true;
      wrapper.find('input').simulate('change');
      var call = onToggle.mock.calls[0];
      expect(call[0]).toEqual(true);
      expect(call[1]).toEqual(id);
      expect(call[2].target).toBe(inputElement);
    });
  });
});
describe('ToggleSkeleton', function () {
  describe('Renders as expected', function () {
    var wrapper = shallow(React.createElement(ToggleSkeleton, null));
    var input = wrapper.find('input');
    var toggleLabel = wrapper.find(".".concat(prefix, "--toggle__label"));
    it('Has the expected classes', function () {
      expect(input.hasClass("".concat(prefix, "--skeleton"))).toEqual(true);
      expect(input.hasClass("".concat(prefix, "--toggle"))).toEqual(true);
      expect(toggleLabel.hasClass("".concat(prefix, "--skeleton"))).toEqual(true);
    });
  });
});