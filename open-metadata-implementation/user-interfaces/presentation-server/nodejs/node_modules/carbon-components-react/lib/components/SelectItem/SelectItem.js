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

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

function _objectWithoutProperties(source, excluded) { if (source == null) return {}; var target = _objectWithoutPropertiesLoose(source, excluded); var key, i; if (Object.getOwnPropertySymbols) { var sourceSymbolKeys = Object.getOwnPropertySymbols(source); for (i = 0; i < sourceSymbolKeys.length; i++) { key = sourceSymbolKeys[i]; if (excluded.indexOf(key) >= 0) continue; if (!Object.prototype.propertyIsEnumerable.call(source, key)) continue; target[key] = source[key]; } } return target; }

function _objectWithoutPropertiesLoose(source, excluded) { if (source == null) return {}; var target = {}; var sourceKeys = Object.keys(source); var key, i; for (i = 0; i < sourceKeys.length; i++) { key = sourceKeys[i]; if (excluded.indexOf(key) >= 0) continue; target[key] = source[key]; } return target; }

var prefix = _carbonComponents.settings.prefix;

var SelectItem = function SelectItem(_ref) {
  var _classNames;

  var className = _ref.className,
      value = _ref.value,
      disabled = _ref.disabled,
      hidden = _ref.hidden,
      text = _ref.text,
      other = _objectWithoutProperties(_ref, ["className", "value", "disabled", "hidden", "text"]);

  var selectItemClasses = (0, _classnames.default)((_classNames = {}, _defineProperty(_classNames, "".concat(prefix, "--select-option"), true), _defineProperty(_classNames, className, className), _classNames));
  return _react.default.createElement("option", _extends({}, other, {
    className: selectItemClasses,
    value: value,
    disabled: disabled,
    hidden: hidden
  }), text);
};

SelectItem.propTypes = {
  /**
   * Specify the value of the <SelectItem>
   */
  value: _propTypes.default.any.isRequired,

  /**
   * Specify an optional className to be applied to the node
   */
  className: _propTypes.default.string,

  /**
   * Specify whether the <SelectItem> should be disabled
   */
  disabled: _propTypes.default.bool,

  /**
   * Specify whether the <SelectItem> is hidden
   */
  hidden: _propTypes.default.bool,

  /**
   * Provide the contents of your <SelectItem>
   */
  text: _propTypes.default.string.isRequired
};
SelectItem.defaultProps = {
  disabled: false,
  hidden: false,
  value: '',
  text: ''
};
var _default = SelectItem;
exports.default = _default;