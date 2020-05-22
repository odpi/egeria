"use strict";

var _react = _interopRequireDefault(require("react"));

var _RadioButton = _interopRequireDefault(require("../RadioButton"));

var _enzyme = require("enzyme");

var _carbonComponents = require("carbon-components");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

var prefix = _carbonComponents.settings.prefix;

var render = function render(props) {
  return (0, _enzyme.mount)(_react.default.createElement(_RadioButton.default, _extends({}, props, {
    className: "extra-class",
    name: "test-name",
    value: "test-value",
    labelText: "testlabel"
  })));
};

describe('RadioButton', function () {
  describe('renders as expected', function () {
    var wrapper = render({
      checked: true
    });
    var input = wrapper.find('input');
    var label = wrapper.find('label');
    var div = wrapper.find('div');
    describe('input', function () {
      it('is of type radio', function () {
        expect(input.props().type).toEqual('radio');
      });
      it('has the expected class', function () {
        expect(input.hasClass("".concat(prefix, "--radio-button"))).toEqual(true);
      });
      it('has a unique id set by default', function () {
        expect(input.props().id).toBeDefined();
      });
      it('should have checked set when checked is passed', function () {
        wrapper.setProps({
          checked: true
        });
        expect(input.props().checked).toEqual(true);
      });
      it('should set the name prop as expected', function () {
        expect(input.props().name).toEqual('test-name');
      });
    });
    describe('label', function () {
      it('should set htmlFor', function () {
        expect(label.props().htmlFor).toEqual(input.props().id);
      });
      it('should set the correct class', function () {
        expect(label.props().className).toEqual("".concat(prefix, "--radio-button__label"));
      });
      it('should render a span with the correct class for radio style', function () {
        var span = label.find('span');
        expect(span.at(0).hasClass("".concat(prefix, "--radio-button__appearance"))).toEqual(true);
      });
      it('should render a span for the label text', function () {
        var span = label.find('span');
        expect(span.at(1).hasClass('')).toEqual(true);
        expect(span.at(1).text()).toEqual('testlabel');
      });
      it('should render label text', function () {
        wrapper.setProps({
          labelText: 'test label text'
        });
        expect(label.text()).toMatch(/test label text/);
      });
      it('should not have the className when no class is passed through props', function () {
        expect(label.hasClass(label.props.className)).toEqual(false);
      });
      it('should have the className passed through props', function () {
        var label = render({
          className: 'extra-class'
        });
        expect(label.hasClass('extra-class')).toEqual(true);
      });
    });
    describe('wrapper', function () {
      it('should have the correct class', function () {
        expect(div.hasClass("".concat(prefix, "--radio-button-wrapper"))).toEqual(true);
      });
      it('should have extra classes applied', function () {
        expect(div.hasClass('extra-class')).toEqual(true);
      });
    });
  });
  it('should set defaultChecked as expected', function () {
    var wrapper = render({
      defaultChecked: true
    });

    var input = function input() {
      return wrapper.find('input');
    };

    expect(input().props().defaultChecked).toEqual(true);
    wrapper.setProps({
      defaultChecked: false
    });
    expect(input().props().defaultChecked).toEqual(false);
  });
  it('should set id if one is passed in', function () {
    var wrapper = render({
      id: 'unique-id'
    });
    var input = wrapper.find('input');
    expect(input.props().id).toEqual('unique-id');
  });
  describe('events', function () {
    it('should invoke onChange with expected arguments', function () {
      var onChange = jest.fn();
      var wrapper = render({
        onChange: onChange
      });
      var input = wrapper.find('input');
      var inputElement = input.instance();
      inputElement.checked = true;
      wrapper.find('input').simulate('change');
      var call = onChange.mock.calls[0];
      expect(call[0]).toEqual('test-value');
      expect(call[1]).toEqual('test-name');
      expect(call[2].target).toBe(inputElement);
    });
  });
});