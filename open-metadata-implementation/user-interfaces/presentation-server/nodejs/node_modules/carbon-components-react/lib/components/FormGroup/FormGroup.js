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

var FormGroup = function FormGroup(_ref) {
  var legendText = _ref.legendText,
      invalid = _ref.invalid,
      children = _ref.children,
      className = _ref.className,
      message = _ref.message,
      messageText = _ref.messageText,
      other = _objectWithoutProperties(_ref, ["legendText", "invalid", "children", "className", "message", "messageText"]);

  var classNamesLegend = (0, _classnames.default)("".concat(prefix, "--label"), className);
  var classNamesFieldset = (0, _classnames.default)("".concat(prefix, "--fieldset"), className);
  return _react.default.createElement("fieldset", _extends({}, invalid && {
    'data-invalid': ''
  }, {
    className: classNamesFieldset
  }, other), _react.default.createElement("legend", {
    className: classNamesLegend
  }, legendText), children, message ? _react.default.createElement("div", {
    className: "".concat(prefix, "--form__requirements")
  }, messageText) : null);
};

FormGroup.propTypes = {
  /**
   * Provide the children form elements to be rendered inside of the <fieldset>
   */
  children: _propTypes.default.node,

  /**
   * Provide the text to be rendered inside of the fieldset <legend>
   */
  legendText: _propTypes.default.string.isRequired,

  /**
   * Provide a custom className to be applied to the containing <fieldset> node
   */
  className: _propTypes.default.string,

  /**
   * Specify whether the <FormGroup> is invalid
   */
  invalid: _propTypes.default.bool,

  /**
   * Specify whether the message should be displayed in the <FormGroup>
   */
  message: _propTypes.default.bool,

  /**
   * Provide the text for the message in the <FormGroup>
   */
  messageText: _propTypes.default.string
};
FormGroup.defaultProps = {
  invalid: false,
  message: false,
  messageText: ''
};
var _default = FormGroup;
exports.default = _default;