"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");

exports.__esModule = true;
exports.default = void 0;

var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));

var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));

var _classnames = _interopRequireDefault(require("classnames"));

var _react = _interopRequireDefault(require("react"));

var _ThemeProvider = require("./ThemeProvider");

var defaultProps = {
  noGutters: false
};

var Row = _react.default.forwardRef(function (props, ref) {
  var bsPrefix = props.bsPrefix,
      noGutters = props.noGutters,
      _props$as = props.as,
      Component = _props$as === void 0 ? 'div' : _props$as,
      className = props.className,
      otherProps = (0, _objectWithoutPropertiesLoose2.default)(props, ["bsPrefix", "noGutters", "as", "className"]);
  var decoratedBsPrefix = (0, _ThemeProvider.useBootstrapPrefix)(bsPrefix, 'row');
  return _react.default.createElement(Component, (0, _extends2.default)({
    ref: ref
  }, otherProps, {
    className: (0, _classnames.default)(className, decoratedBsPrefix, noGutters && 'no-gutters')
  }));
});

Row.defaultProps = defaultProps;
var _default = Row;
exports.default = _default;
module.exports = exports["default"];