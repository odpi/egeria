"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _classnames = _interopRequireDefault(require("classnames"));

var _react = _interopRequireDefault(require("react"));

var _propTypes = _interopRequireDefault(require("prop-types"));

var _carbonComponents = require("carbon-components");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

function _objectWithoutProperties(source, excluded) { if (source == null) return {}; var target = _objectWithoutPropertiesLoose(source, excluded); var key, i; if (Object.getOwnPropertySymbols) { var sourceSymbolKeys = Object.getOwnPropertySymbols(source); for (i = 0; i < sourceSymbolKeys.length; i++) { key = sourceSymbolKeys[i]; if (excluded.indexOf(key) >= 0) continue; if (!Object.prototype.propertyIsEnumerable.call(source, key)) continue; target[key] = source[key]; } } return target; }

function _objectWithoutPropertiesLoose(source, excluded) { if (source == null) return {}; var target = {}; var sourceKeys = Object.keys(source); var key, i; for (i = 0; i < sourceKeys.length; i++) { key = sourceKeys[i]; if (excluded.indexOf(key) >= 0) continue; target[key] = source[key]; } return target; }

var prefix = _carbonComponents.settings.prefix;
/**
 * `ListBoxMenuItem` is a helper component for managing the container class
 * name, alongside any classes for any corresponding states, for a generic list
 * box menu item.
 */

var ListBoxMenuItem = function ListBoxMenuItem(_ref) {
  var _cx;

  var children = _ref.children,
      isActive = _ref.isActive,
      isHighlighted = _ref.isHighlighted,
      rest = _objectWithoutProperties(_ref, ["children", "isActive", "isHighlighted"]);

  var className = (0, _classnames.default)((_cx = {}, _defineProperty(_cx, "".concat(prefix, "--list-box__menu-item"), true), _defineProperty(_cx, "".concat(prefix, "--list-box__menu-item--active"), isActive), _defineProperty(_cx, "".concat(prefix, "--list-box__menu-item--highlighted"), isHighlighted), _cx));
  return _react.default.createElement("div", _extends({
    className: className
  }, rest), _react.default.createElement("div", {
    className: "".concat(prefix, "--list-box__menu-item__option")
  }, children));
};

ListBoxMenuItem.propTypes = {
  /**
   * Specify any children nodes that hsould be rendered inside of the ListBox
   * Menu Item
   */
  children: _propTypes.default.node,

  /**
   * Specify whether the current menu item is "active".
   */
  isActive: _propTypes.default.bool.isRequired,

  /**
   * Specify whether the current menu item is "highlighed".
   */
  isHighlighted: _propTypes.default.bool.isRequired
};
ListBoxMenuItem.defaultProps = {
  isActive: false,
  isHighlighted: false
};
var _default = ListBoxMenuItem;
exports.default = _default;