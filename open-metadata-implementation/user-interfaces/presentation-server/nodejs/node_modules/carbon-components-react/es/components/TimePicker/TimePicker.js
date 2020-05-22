function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

function _objectWithoutProperties(source, excluded) { if (source == null) return {}; var target = _objectWithoutPropertiesLoose(source, excluded); var key, i; if (Object.getOwnPropertySymbols) { var sourceSymbolKeys = Object.getOwnPropertySymbols(source); for (i = 0; i < sourceSymbolKeys.length; i++) { key = sourceSymbolKeys[i]; if (excluded.indexOf(key) >= 0) continue; if (!Object.prototype.propertyIsEnumerable.call(source, key)) continue; target[key] = source[key]; } } return target; }

function _objectWithoutPropertiesLoose(source, excluded) { if (source == null) return {}; var target = {}; var sourceKeys = Object.keys(source); var key, i; for (i = 0; i < sourceKeys.length; i++) { key = sourceKeys[i]; if (excluded.indexOf(key) >= 0) continue; target[key] = source[key]; } return target; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } return _assertThisInitialized(self); }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import PropTypes from 'prop-types';
import React, { Component } from 'react';
import classNames from 'classnames';
import { settings } from 'carbon-components';
var prefix = settings.prefix;

var TimePicker =
/*#__PURE__*/
function (_Component) {
  _inherits(TimePicker, _Component);

  function TimePicker() {
    var _getPrototypeOf2;

    var _this;

    _classCallCheck(this, TimePicker);

    for (var _len = arguments.length, args = new Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }

    _this = _possibleConstructorReturn(this, (_getPrototypeOf2 = _getPrototypeOf(TimePicker)).call.apply(_getPrototypeOf2, [this].concat(args)));

    _defineProperty(_assertThisInitialized(_this), "state", {});

    return _this;
  }

  _createClass(TimePicker, [{
    key: "render",
    value: function render() {
      var _classNames,
          _this2 = this,
          _classNames2,
          _classNames3;

      var _this$props = this.props,
          children = _this$props.children,
          className = _this$props.className,
          id = _this$props.id,
          labelText = _this$props.labelText,
          type = _this$props.type,
          pattern = _this$props.pattern,
          _onChange = _this$props.onChange,
          _onClick = _this$props.onClick,
          _onBlur = _this$props.onBlur,
          placeholder = _this$props.placeholder,
          maxLength = _this$props.maxLength,
          invalidText = _this$props.invalidText,
          invalid = _this$props.invalid,
          hideLabel = _this$props.hideLabel,
          light = _this$props.light,
          other = _objectWithoutProperties(_this$props, ["children", "className", "id", "labelText", "type", "pattern", "onChange", "onClick", "onBlur", "placeholder", "maxLength", "invalidText", "invalid", "hideLabel", "light"]);

      var timePickerInputProps = {
        className: classNames("".concat(prefix, "--time-picker__input-field"), "".concat(prefix, "--text-input"), className, (_classNames = {}, _defineProperty(_classNames, "".concat(prefix, "--text-input--light"), light), _defineProperty(_classNames, "".concat(prefix, "--text-input--invalid"), invalid), _classNames)),
        onChange: function onChange(evt) {
          if (!other.disabled) {
            _this2.setState({
              value: evt.target.value
            });

            _onChange(evt);
          }
        },
        onClick: function onClick(evt) {
          if (!other.disabled) {
            _this2.setState({
              value: evt.target.value
            });

            _onClick(evt);
          }
        },
        onBlur: function onBlur(evt) {
          if (!other.disabled) {
            _this2.setState({
              value: evt.target.value
            });

            _onBlur(evt);
          }
        },
        pattern: pattern,
        placeholder: placeholder,
        maxLength: maxLength,
        id: id,
        type: type,
        value: this.state.value
      };
      var timePickerClasses = classNames((_classNames2 = {}, _defineProperty(_classNames2, "".concat(prefix, "--time-picker"), true), _defineProperty(_classNames2, "".concat(prefix, "--time-picker--light"), light), _defineProperty(_classNames2, className, className), _classNames2));
      var labelClasses = classNames("".concat(prefix, "--label"), (_classNames3 = {}, _defineProperty(_classNames3, "".concat(prefix, "--visually-hidden"), hideLabel), _defineProperty(_classNames3, "".concat(prefix, "--label--disabled"), other.disabled), _classNames3));
      var label = labelText ? React.createElement("label", {
        htmlFor: id,
        className: labelClasses
      }, labelText) : null;
      var error = invalid ? React.createElement("div", {
        className: "".concat(prefix, "--form-requirement")
      }, invalidText) : null;
      return React.createElement("div", {
        className: "".concat(prefix, "--form-item")
      }, React.createElement("div", {
        className: timePickerClasses
      }, React.createElement("div", {
        className: "".concat(prefix, "--time-picker__input")
      }, label, React.createElement("input", _extends({}, other, timePickerInputProps, {
        "data-invalid": invalid ? invalid : undefined
      })), error), children));
    }
  }], [{
    key: "getDerivedStateFromProps",
    value: function getDerivedStateFromProps(_ref, state) {
      var value = _ref.value;
      var prevValue = state.prevValue;
      return prevValue === value ? null : {
        value: value,
        prevValue: value
      };
    }
  }]);

  return TimePicker;
}(Component);

_defineProperty(TimePicker, "propTypes", {
  /**
   * Pass in the children that will be rendered next to the form control
   */
  children: PropTypes.node,

  /**
   * Specify an optional className to be applied to the container node
   */
  className: PropTypes.string,

  /**
   * Specify a custom `id` for the <input>
   */
  id: PropTypes.string.isRequired,

  /**
   * Provide the text that will be read by a screen reader when visiting this
   * control
   */
  labelText: PropTypes.node,

  /**
   * Optionally provide an `onClick` handler that is called whenever the
   * <input> is clicked
   */
  onClick: PropTypes.func,

  /**
   * Optionally provide an `onChange` handler that is called whenever <input>
   * is updated
   */
  onChange: PropTypes.func,

  /**
   * Optionally provide an `onBlur` handler that is called whenever the
   * <input> loses focus
   */
  onBlur: PropTypes.func,

  /**
   * Specify the type of the <input>
   */
  type: PropTypes.string,

  /**
   * Specify the regular expression working as the pattern of the time string in <input>
   */
  pattern: PropTypes.string,

  /**
   * Specify the placeholder attribute for the <input>
   */
  placeholder: PropTypes.string,

  /**
   * Specify the maximum length of the time string in <input>
   */
  maxLength: PropTypes.number,

  /**
   * Specify whether the control is currently invalid
   */
  invalid: PropTypes.bool,

  /**
   * Provide the text that is displayed when the control is in an invalid state
   */
  invalidText: PropTypes.string,

  /**
   * Specify whether you want the underlying label to be visually hidden
   */
  hideLabel: PropTypes.bool,

  /**
   * Specify whether the <input> should be disabled
   */
  disabled: PropTypes.bool,

  /**
   * Specify the value of the <input>
   */
  value: PropTypes.string,

  /**
   * `true` to use the light version.
   */
  light: PropTypes.bool
});

_defineProperty(TimePicker, "defaultProps", {
  type: 'text',
  pattern: '(1[012]|[1-9]):[0-5][0-9](\\s)?',
  placeholder: 'hh:mm',
  maxLength: 5,
  invalidText: 'Invalid time format.',
  invalid: false,
  disabled: false,
  onChange: function onChange() {},
  onClick: function onClick() {},
  onBlur: function onBlur() {},
  light: false
});

export { TimePicker as default };