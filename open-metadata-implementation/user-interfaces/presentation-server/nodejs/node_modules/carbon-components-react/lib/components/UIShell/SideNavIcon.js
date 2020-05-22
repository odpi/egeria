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

var SideNavIcon = function SideNavIcon(_ref) {
  var _cx;

  var children = _ref.children,
      customClassName = _ref.className,
      small = _ref.small;
  var className = (0, _classnames.default)((_cx = {}, _defineProperty(_cx, "".concat(prefix, "--side-nav__icon"), true), _defineProperty(_cx, "".concat(prefix, "--side-nav__icon--small"), small), _defineProperty(_cx, customClassName, !!customClassName), _cx));
  return _react.default.createElement("div", {
    className: className
  }, children);
};

SideNavIcon.propTypes = {
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
   * Specify whether the icon should be placed in a smaller bounding box
   */
  small: _propTypes.default.bool.isRequired
};
SideNavIcon.defaultProps = {
  small: false
};
var _default = SideNavIcon;
exports.default = _default;