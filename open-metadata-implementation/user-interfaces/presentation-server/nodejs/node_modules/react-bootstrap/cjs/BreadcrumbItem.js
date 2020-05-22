"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");

exports.__esModule = true;
exports.default = void 0;

var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));

var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));

var _classnames = _interopRequireDefault(require("classnames"));

var _react = _interopRequireDefault(require("react"));

var _SafeAnchor = _interopRequireDefault(require("./SafeAnchor"));

var _ThemeProvider = require("./ThemeProvider");

var defaultProps = {
  active: false
};

var BreadcrumbItem = _react.default.forwardRef( // Need to define the default "as" during prop destructuring to be compatible with styled-components github.com/react-bootstrap/react-bootstrap/issues/3595
function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      active = _ref.active,
      className = _ref.className,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'li' : _ref$as,
      props = (0, _objectWithoutPropertiesLoose2.default)(_ref, ["bsPrefix", "active", "className", "as"]);
  var prefix = (0, _ThemeProvider.useBootstrapPrefix)(bsPrefix, 'breadcrumb-item');
  var href = props.href,
      title = props.title,
      target = props.target,
      elementProps = (0, _objectWithoutPropertiesLoose2.default)(props, ["href", "title", "target"]);
  var linkProps = {
    href: href,
    title: title,
    target: target
  };
  return _react.default.createElement(Component, {
    ref: ref,
    className: (0, _classnames.default)(prefix, className, {
      active: active
    }),
    "aria-current": active ? 'page' : undefined
  }, active ? _react.default.createElement("span", (0, _extends2.default)({}, elementProps, {
    className: (0, _classnames.default)({
      active: active
    })
  })) : _react.default.createElement(_SafeAnchor.default, (0, _extends2.default)({}, elementProps, linkProps)));
});

BreadcrumbItem.displayName = 'BreadcrumbItem';
BreadcrumbItem.defaultProps = defaultProps;
var _default = BreadcrumbItem;
exports.default = _default;
module.exports = exports["default"];