"use strict";

var _react = _interopRequireDefault(require("react"));

var _enzyme = require("enzyme");

var _TooltipDefinition = _interopRequireDefault(require("../TooltipDefinition"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

describe('TooltipDefinition', function () {
  var mockProps;
  beforeEach(function () {
    mockProps = {
      direction: 'bottom',
      children: 'tooltip trigger',
      className: 'custom-class',
      tooltipText: 'tooltip text'
    };
  });
  it('should render', function () {
    var wrapper = (0, _enzyme.mount)(_react.default.createElement(_TooltipDefinition.default, mockProps));
    expect(wrapper).toMatchSnapshot();
  });
  it('should support a custom trigger element class', function () {
    var wrapper = (0, _enzyme.mount)(_react.default.createElement(_TooltipDefinition.default, _extends({}, mockProps, {
      triggerClassName: "custom-trigger-class"
    })));
    expect(wrapper).toMatchSnapshot();
  });
  it('should allow the user to specify the direction', function () {
    var wrapper = (0, _enzyme.mount)(_react.default.createElement(_TooltipDefinition.default, _extends({}, mockProps, {
      direction: "top"
    })));
    expect(wrapper).toMatchSnapshot();
  });
});