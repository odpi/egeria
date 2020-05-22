"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _carbonComponents = require("carbon-components");

var _classnames = _interopRequireDefault(require("classnames"));

var _propTypes = _interopRequireDefault(require("prop-types"));

var _react = _interopRequireDefault(require("react"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

var prefix = _carbonComponents.settings.prefix;

var SideNavItem = function SideNavItem(_ref) {
  var _cx;

  var customClassName = _ref.className,
      children = _ref.children,
      _ref$large = _ref.large,
      large = _ref$large === void 0 ? false : _ref$large;
  var className = (0, _classnames.default)((_cx = {}, _defineProperty(_cx, "".concat(prefix, "--side-nav__item"), true), _defineProperty(_cx, "".concat(prefix, "--side-nav__item--large"), large), _defineProperty(_cx, customClassName, !!customClassName), _cx));
  return _react.default.createElement("li", {
    className: className
  }, children);
};

SideNavItem.propTypes = {
  /**
   * Provide an optional class to be applied to the containing node
   */
  className: _propTypes.default.string,

  /**
   * Provide a single icon as the child to `SideNavIcon` to render in the
   * container
   */
  children: _propTypes.default.node.isRequired,

  /**
   * Specify if this is a large variation of the SideNavItem
   */
  large: _propTypes.default.bool
};
var _default = SideNavItem;
exports.default = _default;