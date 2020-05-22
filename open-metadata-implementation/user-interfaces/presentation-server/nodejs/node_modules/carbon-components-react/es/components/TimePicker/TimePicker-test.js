/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import TimePicker from '../TimePicker';
import { mount, shallow } from 'enzyme';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
describe('TimePicker', function () {
  describe('renders as expected', function () {
    describe('input', function () {
      var wrapper;
      var timePicker;
      var textInput;
      beforeEach(function () {
        wrapper = mount(React.createElement(TimePicker, {
          id: "test",
          className: "extra-class"
        }));

        timePicker = function timePicker() {
          return wrapper.find(".".concat(prefix, "--time-picker"));
        };

        textInput = function textInput() {
          return wrapper.find('input');
        };
      });
      it('renders as expected', function () {
        expect(textInput().length).toBe(1);
      });
      it('should add extra classes that are passed via className', function () {
        expect(timePicker().hasClass('extra-class')).toEqual(true);
      });
      it('should set type as expected', function () {
        expect(textInput().props().type).toEqual('text');
      });
      it('should set value as expected', function () {
        expect(textInput().props().defaultValue).toEqual(undefined);
        wrapper.setProps({
          defaultValue: 'test'
        });
        expect(textInput().props().defaultValue).toEqual('test');
      });
      it('should set disabled as expected', function () {
        expect(textInput().props().disabled).toEqual(false);
        wrapper.setProps({
          disabled: true
        });
        expect(textInput().props().disabled).toEqual(true);
      });
      it('should set placeholder as expected', function () {
        wrapper.setProps({
          placeholder: 'ss:mm'
        });
        expect(textInput().props().placeholder).toEqual('ss:mm');
      });
    });
    /*
    describe('Light', () => {
      describe('Renders as expected', () => {
        wrapper = mount(<TimePicker id="test" light className="extra-class" />);
    
        it('Has the expected classes for light', () => {
          expect(wrapper.hasClass(`${prefix}--time-picker--light`)).toEqual(true);
        });
    
        it('Should add extra classes that are passed via className', () => {
          expect(wrapper.hasClass('extra-class')).toEqual(true);
        });
      });
    });
    */

    describe('label', function () {
      var wrapper;
      var label;
      beforeEach(function () {
        wrapper = mount(React.createElement(TimePicker, {
          id: "test",
          className: "extra-class"
        }));

        label = function label() {
          return wrapper.find('label');
        };
      });
      it('does not render a label by default', function () {
        expect(label().length).toBe(0);
      });
      it('renders a label', function () {
        wrapper.setProps({
          labelText: 'Enter a time'
        });
        var renderedlabel = wrapper.find('label');
        expect(renderedlabel.length).toBe(1);
      });
      it('has the expected classes', function () {
        wrapper.setProps({
          labelText: 'Enter a time'
        });
        var renderedlabel = wrapper.find('label');
        expect(renderedlabel.hasClass("".concat(prefix, "--label"))).toEqual(true);
      });
      it('should set label as expected', function () {
        wrapper.setProps({
          labelText: 'Enter a time'
        });
        var renderedlabel = wrapper.find('label');
        expect(renderedlabel.text()).toEqual('Enter a time');
      });
    });
  });
  describe('events', function () {
    describe('disabled time picker', function () {
      var onClick = jest.fn();
      var onChange = jest.fn();
      var wrapper = mount(React.createElement(TimePicker, {
        id: "test",
        onClick: onClick,
        onChange: onChange,
        disabled: true
      }));
      var input = wrapper.find('input');
      it('should not invoke onClick', function () {
        input.simulate('click');
        expect(onClick).not.toBeCalled();
      });
      it('should not invoke onChange', function () {
        input.simulate('change');
        expect(onChange).not.toBeCalled();
      });
    });
    describe('enabled textinput', function () {
      var onClick = jest.fn();
      var onChange = jest.fn();
      var onBlur = jest.fn();
      var wrapper = shallow(React.createElement(TimePicker, {
        id: "test",
        onClick: onClick,
        onChange: onChange,
        onBlur: onBlur
      }));
      var input = wrapper.find('input');
      var eventObject = {
        target: {
          defaultValue: 'test'
        }
      };
      it('should invoke onBlur when input is clicked', function () {
        input.simulate('blur', eventObject);
        expect(onBlur).toBeCalledWith(eventObject);
      });
      it('should invoke onClick when input is clicked', function () {
        input.simulate('click', eventObject);
        expect(onClick).toBeCalledWith(eventObject);
      });
      it('should invoke onChange when input value is changed', function () {
        input.simulate('change', eventObject);
        expect(onChange).toBeCalledWith(eventObject);
      });
    });
  });
  describe('Getting derived state from props', function () {
    var wrapper = shallow(React.createElement(TimePicker, null));
    it('should change the value upon change in props', function () {
      wrapper.setProps({
        value: 'foo'
      });
      wrapper.setState({
        value: 'foo'
      });
      wrapper.setProps({
        value: 'bar'
      });
      expect(wrapper.state().value).toEqual('bar');
    });
    it('should avoid change the value upon setting props, unless there the value actually changes', function () {
      wrapper.setProps({
        value: 'foo'
      });
      wrapper.setState({
        value: 'bar'
      });
      wrapper.setProps({
        value: 'foo'
      });
      expect(wrapper.state().value).toEqual('bar');
    });
  });
});