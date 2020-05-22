/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import { shallow, mount } from 'enzyme';
import RadioButtonGroup from '../RadioButtonGroup';
import RadioButton from '../RadioButton';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
describe('RadioButtonGroup', function () {
  describe('renders as expected', function () {
    var wrapper = mount(React.createElement(RadioButtonGroup, {
      defaultSelected: "female",
      name: "gender"
    }, React.createElement(RadioButton, {
      labelText: "Male",
      value: "male"
    }), React.createElement(RadioButton, {
      labelText: "Female",
      value: "female"
    })));
    describe('wrapping div', function () {
      var div = wrapper.first('div');
      it('renders a div', function () {
        expect(div.length).toEqual(1);
      });
      it('sets classes that are passed via className prop', function () {
        wrapper.setProps({
          className: 'extra-class'
        });
        expect(div.hasClass('extra-class'));
      });
      it('sets disabled attribute if disabled prop is set', function () {
        wrapper.setProps({
          disabled: true
        });
        expect(wrapper.first().props().disabled).toEqual(true);
      });
    });
    describe('children', function () {
      var radioButton = function radioButton() {
        return wrapper.find(RadioButton);
      };

      it('renders expected number of children', function () {
        expect(radioButton().length).toEqual(2);
      });
      it('should set checked property based on defaultSelected prop', function () {
        expect(radioButton().last().props().checked).toEqual(true);
      });
      it('should set checked property based on valueSelected prop', function () {
        wrapper.setProps({
          valueSelected: 'male'
        });
        expect(radioButton().first().props().checked).toEqual(true);
        wrapper.setProps({
          valueSelected: 'female'
        });
        expect(radioButton().last().props().checked).toEqual(true);
      });
      it('should set expected props on children', function () {
        var firstChild = radioButton().first();
        expect(firstChild.props().name).toEqual('gender');
        expect(firstChild.props().value).toEqual('male');
      });
    });
  });
  describe('onChange event', function () {
    var onChange = jest.fn();
    var wrapper = mount(React.createElement(RadioButtonGroup, {
      onChange: onChange,
      name: "gender"
    }, React.createElement(RadioButton, {
      labelText: "Male",
      value: "male"
    }), React.createElement(RadioButton, {
      labelText: "Female",
      value: "female"
    })));
    var firstRadio = wrapper.find(RadioButton).first();
    var args = ['male', 'gender', {
      test: 'test event'
    }];
    it('first child should not have checked set initially', function () {
      expect(firstRadio.props().checked).toEqual(false);
    });
    it('invoking onChange sets checked on correct child', function () {
      var _firstRadio$props;

      (_firstRadio$props = firstRadio.props()).onChange.apply(_firstRadio$props, args);

      wrapper.update();
      expect(wrapper.find(RadioButton).first().props().checked).toEqual(true);
    });
    it('should invoke onChange with correct arguments', function () {
      var _expect;

      (_expect = expect(onChange)).toBeCalledWith.apply(_expect, args);
    });
    it('calling onChange with same args should not call onChange prop', function () {
      var _firstRadio$props2;

      onChange.mockClear();

      (_firstRadio$props2 = firstRadio.props()).onChange.apply(_firstRadio$props2, args);

      expect(onChange).not.toHaveBeenCalled();
    });
  });
  describe('Getting derived state from props', function () {
    var wrapper = shallow(React.createElement(RadioButtonGroup, {
      valueSelected: "male",
      defaultSelected: "female",
      name: "gender"
    }, React.createElement(RadioButton, {
      labelText: "Male",
      value: "male"
    }), React.createElement(RadioButton, {
      labelText: "Female",
      value: "female"
    })));
    it('should initialize the current selection from props', function () {
      expect(wrapper.state().selected).toEqual('male');
    });
    it('should change the current selection upon change in props', function () {
      wrapper.setProps({
        valueSelected: 'male'
      });
      wrapper.setState({
        selected: 'male'
      });
      wrapper.setProps({
        valueSelected: undefined
      });
      expect(wrapper.state().selected).toEqual('female');
    });
    it('should avoid change the current selection upon setting props, unless there the value actually changes', function () {
      wrapper.setProps({
        valueSelected: 'female'
      });
      wrapper.setState({
        selected: 'male'
      });
      wrapper.setProps({
        valueSelected: 'female'
      });
      expect(wrapper.state().selected).toEqual('male');
    });
  });
  describe('Custom class name should stay with original class name', function () {
    var wrapper = shallow(React.createElement(RadioButtonGroup, {
      className: "my-radio-group",
      valueSelected: "male",
      defaultSelected: "female",
      name: "gender"
    }, React.createElement(RadioButton, {
      labelText: "Male",
      value: "male"
    }), React.createElement(RadioButton, {
      labelText: "Female",
      value: "female"
    })));
    it('should found the provided class along with the base class', function () {
      expect(wrapper.exists('.my-radio-group')).toBe(true);
      expect(wrapper.exists(".".concat(prefix, "--radio-button-group.my-radio-group"))).toBe(true);
    });
  });
});