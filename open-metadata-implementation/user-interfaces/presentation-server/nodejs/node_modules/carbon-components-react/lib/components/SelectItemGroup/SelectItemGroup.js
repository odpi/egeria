"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _propTypes = _interopRequireDefault(require("prop-types"));

var _react = _interopRequireDefault(require("react"));

var _classnames = _interopRequireDefault(require("classnames"));

var _carbonComponents = require("carbon-components");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

function _objectWithoutProperties(source, excluded) { if (source == null) return {}; var target = _objectWithoutPropertiesLoose(source, excluded); var key, i; if (Object.getOwnPropertySymbols) { var sourceSymbolKeys = Object.getOwnPropertySymbols(source); for (i = 0; i < sourceSymbolKeys.length; i++) { key = sourceSymbolKeys[i]; if (excluded.indexOf(key) >= 0) continue; if (!Object.prototype.propertyIsEnumerable.call(source, key)) continue; target[key] = source[key]; } } return target; }

function _objectWithoutPropertiesLoose(source, excluded) { if (source == null) return {}; var target = {}; var sourceKeys = Object.keys(source); var key, i; for (i = 0; i < sourceKeys.length; i++) { key = sourceKeys[i]; if (excluded.indexOf(key) >= 0) continue; target[key] = source[key]; } return target; }

var prefix = _carbonComponents.settings.prefix;

var SelectItemGroup = function SelectItemGroup(_ref) {
  var children = _ref.children,
      className = _ref.className,
      disabled = _ref.disabled,
      label = _ref.label,
      other = _objectWithoutProperties(_ref, ["children", "className", "disabled", "label"]);

  var classNames = (0, _classnames.default)("".concat(prefix, "--select-optgroup"), className);
  return _react.default.createElement("optgroup", _extends({
    className: classNames,
    label: label,
    disabled: disabled
  }, other), children);
};

SelectItemGroup.propTypes = {
  /**
   * Provide the contents of your <SelectItemGroup>
   */
  children: _propTypes.default.node,

  /**
   * Specify an optional className to be applied to the node
   */
  className: _propTypes.default.string,

  /**
   * Specify whether the <SelectItemGroup> should be disabled
   */
  disabled: _propTypes.default.bool,

  /**
   * Specify the label to be displayed
   */
  label: _propTypes.default.string.isRequired
};
SelectItemGroup.defaultProps = {
  disabled: false,
  label: 'Provide label'
};
var _default = SelectItemGroup;
exports.default = _default;