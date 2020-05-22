function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } return _assertThisInitialized(self); }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import TextInput from '../TextInput';
import { mount, shallow } from 'enzyme';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
describe('TextInput', function () {
  describe('renders as expected', function () {
    var wrapper = mount(React.createElement(TextInput.ControlledPasswordInput, {
      id: "test",
      className: "extra-class",
      labelText: "testlabel",
      helperText: "testHelper",
      light: true
    }));

    var textInput = function textInput() {
      return wrapper.find('input');
    };

    describe('input', function () {
      it('renders as expected', function () {
        expect(textInput().length).toBe(1);
      });
      it('should accept refs', function () {
        var MyComponent =
        /*#__PURE__*/
        function (_React$Component) {
          _inherits(MyComponent, _React$Component);

          function MyComponent(props) {
            var _this;

            _classCallCheck(this, MyComponent);

            _this = _possibleConstructorReturn(this, _getPrototypeOf(MyComponent).call(this, props));
            _this.textInput = React.createRef();
            _this.focus = _this.focus.bind(_assertThisInitialized(_this));
            return _this;
          }

          _createClass(MyComponent, [{
            key: "focus",
            value: function focus() {
              this.textInput.current.focus();
            }
          }, {
            key: "render",
            value: function render() {
              return React.createElement(TextInput, {
                id: "test",
                labelText: "testlabel",
                ref: this.textInput
              });
            }
          }]);

          return MyComponent;
        }(React.Component);

        var wrapper = mount(React.createElement(MyComponent, null));
        expect(document.activeElement.type).toBeUndefined();
        wrapper.instance().focus();
        expect(document.activeElement.type).toEqual('text');
      });
      it('has the expected classes', function () {
        expect(textInput().hasClass("".concat(prefix, "--text-input"))).toEqual(true);
      });
      it('should add extra classes that are passed via className', function () {
        expect(textInput().hasClass('extra-class')).toEqual(true);
      });
      it('has the expected classes for light', function () {
        wrapper.setProps({
          light: true
        });
        expect(textInput().hasClass("".concat(prefix, "--text-input--light"))).toEqual(true);
      });
      it('should set type as expected', function () {
        expect(textInput().props().type).toEqual('password');
        wrapper.setProps({
          type: 'text'
        });
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
        expect(textInput().props().placeholder).not.toBeDefined();
        wrapper.setProps({
          placeholder: 'Enter text'
        });
        expect(textInput().props().placeholder).toEqual('Enter text');
      });
    });
    describe('label', function () {
      wrapper.setProps({
        labelText: 'Email Input'
      });
      var renderedLabel = wrapper.find('label');
      it('renders a label', function () {
        expect(renderedLabel.length).toBe(1);
      });
      it('has the expected classes', function () {
        expect(renderedLabel.hasClass("".concat(prefix, "--label"))).toEqual(true);
      });
      it('should set label as expected', function () {
        expect(renderedLabel.text()).toEqual('Email Input');
      });
    });
    describe('helper', function () {
      it('renders a helper', function () {
        var renderedHelper = wrapper.find(".".concat(prefix, "--form__helper-text"));
        expect(renderedHelper.length).toEqual(1);
      });
      it('renders children as expected', function () {
        wrapper.setProps({
          helperText: React.createElement("span", null, "This helper text has ", React.createElement("a", {
            href: "/"
          }, "a link"), ".")
        });
        var renderedHelper = wrapper.find(".".concat(prefix, "--form__helper-text"));
        expect(renderedHelper.props().children).toEqual(React.createElement("span", null, "This helper text has ", React.createElement("a", {
          href: "/"
        }, "a link"), "."));
      });
      it('should set helper text as expected', function () {
        wrapper.setProps({
          helperText: 'Helper text'
        });
        var renderedHelper = wrapper.find(".".concat(prefix, "--form__helper-text"));
        expect(renderedHelper.text()).toEqual('Helper text');
      });
    });
  });
  describe('events', function () {
    describe('disabled textinput', function () {
      var onClick = jest.fn();
      var onChange = jest.fn();
      var wrapper = shallow(React.createElement(TextInput, {
        id: "test",
        labelText: "testlabel",
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
      var wrapper = shallow(React.createElement(TextInput, {
        labelText: "testlabel",
        id: "test",
        onClick: onClick,
        onChange: onChange
      }));
      var input = wrapper.find('input');
      var eventObject = {
        target: {
          defaultValue: 'test'
        }
      };
      it('should invoke onClick when input is clicked', function () {
        input.simulate('click');
        expect(onClick).toBeCalled();
      });
      it('should invoke onChange when input value is changed', function () {
        input.simulate('change', eventObject);
        expect(onChange).toBeCalledWith(eventObject);
      });
    });
  });
});