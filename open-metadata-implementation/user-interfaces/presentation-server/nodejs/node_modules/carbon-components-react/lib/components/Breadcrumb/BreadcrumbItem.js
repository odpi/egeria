"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _propTypes = _interopRequireDefault(require("prop-types"));

var _react = _interopRequireDefault(require("react"));

var _classnames = _interopRequireDefault(require("classnames"));

var _carbonComponents = require("carbon-components");

var _Link = _interopRequireDefault(require("../Link"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

function _objectWithoutProperties(source, excluded) { if (source == null) return {}; var target = _objectWithoutPropertiesLoose(source, excluded); var key, i; if (Object.getOwnPropertySymbols) { var sourceSymbolKeys = Object.getOwnPropertySymbols(source); for (i = 0; i < sourceSymbolKeys.length; i++) { key = sourceSymbolKeys[i]; if (excluded.indexOf(key) >= 0) continue; if (!Object.prototype.propertyIsEnumerable.call(source, key)) continue; target[key] = source[key]; } } return target; }

function _objectWithoutPropertiesLoose(source, excluded) { if (source == null) return {}; var target = {}; var sourceKeys = Object.keys(source); var key, i; for (i = 0; i < sourceKeys.length; i++) { key = sourceKeys[i]; if (excluded.indexOf(key) >= 0) continue; target[key] = source[key]; } return target; }

var prefix = _carbonComponents.settings.prefix;

var BreadcrumbItem = function BreadcrumbItem(_ref) {
  var _cx;

  var ariaCurrent = _ref['aria-current'],
      children = _ref.children,
      customClassName = _ref.className,
      href = _ref.href,
      isCurrentPage = _ref.isCurrentPage,
      rest = _objectWithoutProperties(_ref, ["aria-current", "children", "className", "href", "isCurrentPage"]);

  var className = (0, _classnames.default)((_cx = {}, _defineProperty(_cx, "".concat(prefix, "--breadcrumb-item"), true), _defineProperty(_cx, "".concat(prefix, "--breadcrumb-item--current"), isCurrentPage && ariaCurrent !== 'page'), _defineProperty(_cx, customClassName, !!customClassName), _cx));

  if (typeof children === 'string' && href) {
    return _react.default.createElement("li", _extends({
      className: className
    }, rest), _react.default.createElement(_Link.default, {
      href: href,
      "aria-current": ariaCurrent
    }, children));
  }

  return _react.default.createElement("li", _extends({
    className: className
  }, rest), _react.default.cloneElement(children, {
    'aria-current': ariaCurrent,
    className: "".concat(prefix, "--link")
  }));
};

BreadcrumbItem.propTypes = {
  /**
   * Pass in content that will be inside of the BreadcrumbItem
   */
  children: _propTypes.default.node,

  /**
   * Specify an optional className to be applied to the container node
   */
  className: _propTypes.default.string,

  /**
   * Optional string representing the link location for the BreadcrumbItem
   */
  href: _propTypes.default.string,

  /**
   * Provide if this breadcrumb item represents the current page
   */
  isCurrentPage: _propTypes.default.bool
};
var _default = BreadcrumbItem;
exports.default = _default;