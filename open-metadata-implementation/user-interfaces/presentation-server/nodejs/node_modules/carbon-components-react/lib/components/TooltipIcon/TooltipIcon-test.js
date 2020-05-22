"use strict";

var _react = _interopRequireDefault(require("react"));

var _enzyme = require("enzyme");

var _TooltipIcon = _interopRequireDefault(require("../TooltipIcon"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

describe('TooltipIcon', function () {
  var mockProps;
  beforeEach(function () {
    mockProps = {
      direction: 'bottom',
      children: _react.default.createElement("svg", null),
      className: 'custom-class',
      tooltipText: 'tooltip text'
    };
  });
  it('should render', function () {
    var wrapper = (0, _enzyme.mount)(_react.default.createElement(_TooltipIcon.default, mockProps));
    expect(wrapper).toMatchSnapshot();
  });
  it('should add extra classes via className', function () {
    var wrapper = (0, _enzyme.mount)(_react.default.createElement(_TooltipIcon.default, mockProps));
    expect(wrapper.hasClass('custom-class')).toBe(true);
  });
  it('should have an ID on the content container by default', function () {
    var wrapper = (0, _enzyme.mount)(_react.default.createElement(_TooltipIcon.default, mockProps));
    expect(wrapper.find('.bx--assistive-text').props().id).toBeTruthy();
  });
  it('should allow the user to specify the direction', function () {
    var wrapper = (0, _enzyme.mount)(_react.default.createElement(_TooltipIcon.default, _extends({}, mockProps, {
      direction: "top"
    })));
    expect(wrapper).toMatchSnapshot();
  });
});