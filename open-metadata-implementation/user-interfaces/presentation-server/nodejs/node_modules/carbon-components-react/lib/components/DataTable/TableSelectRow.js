"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _propTypes = _interopRequireDefault(require("prop-types"));

var _react = _interopRequireDefault(require("react"));

var _classnames = _interopRequireDefault(require("classnames"));

var _carbonComponents = require("carbon-components");

var _InlineCheckbox = _interopRequireDefault(require("../InlineCheckbox"));

var _RadioButton = _interopRequireDefault(require("../RadioButton"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

var prefix = _carbonComponents.settings.prefix;

var TableSelectRow = function TableSelectRow(_ref) {
  var ariaLabel = _ref.ariaLabel,
      checked = _ref.checked,
      id = _ref.id,
      name = _ref.name,
      onSelect = _ref.onSelect,
      disabled = _ref.disabled,
      radio = _ref.radio,
      className = _ref.className;
  var selectionInputProps = {
    id: id,
    name: name,
    onClick: onSelect,
    checked: checked,
    disabled: disabled
  };
  var InlineInputComponent = radio ? _RadioButton.default : _InlineCheckbox.default;
  var tableSelectRowClasses = (0, _classnames.default)("".concat(prefix, "--table-column-checkbox"), _defineProperty({}, className, className));
  return _react.default.createElement("td", {
    className: tableSelectRowClasses
  }, _react.default.createElement(InlineInputComponent, _extends({}, selectionInputProps, radio && {
    labelText: ariaLabel,
    hideLabel: true
  }, !radio && {
    ariaLabel: ariaLabel
  })));
};

TableSelectRow.propTypes = {
  /**
   * Specify the aria label for the underlying input control
   */
  ariaLabel: _propTypes.default.string.isRequired,

  /**
   * Specify whether all items are selected, or not
   */
  checked: _propTypes.default.bool.isRequired,

  /**
   * Specify whether the control is disabled
   */
  disabled: _propTypes.default.bool,

  /**
   * Provide an `id` for the underlying input control
   */
  id: _propTypes.default.string.isRequired,

  /**
   * Provide a `name` for the underlying input control
   */
  name: _propTypes.default.string.isRequired,

  /**
   * Provide a handler to listen to when a user initiates a selection request
   */
  onSelect: _propTypes.default.func.isRequired,

  /**
   * Specify whether the control should be a radio button or inline checkbox
   */
  radio: _propTypes.default.bool,

  /**
   * The CSS class names of the cell that wraps the underlying input control
   */
  className: _propTypes.default.string
};
var _default = TableSelectRow;
exports.default = _default;