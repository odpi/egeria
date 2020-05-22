function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

function _objectWithoutProperties(source, excluded) { if (source == null) return {}; var target = _objectWithoutPropertiesLoose(source, excluded); var key, i; if (Object.getOwnPropertySymbols) { var sourceSymbolKeys = Object.getOwnPropertySymbols(source); for (i = 0; i < sourceSymbolKeys.length; i++) { key = sourceSymbolKeys[i]; if (excluded.indexOf(key) >= 0) continue; if (!Object.prototype.propertyIsEnumerable.call(source, key)) continue; target[key] = source[key]; } } return target; }

function _objectWithoutPropertiesLoose(source, excluded) { if (source == null) return {}; var target = {}; var sourceKeys = Object.keys(source); var key, i; for (i = 0; i < sourceKeys.length; i++) { key = sourceKeys[i]; if (excluded.indexOf(key) >= 0) continue; target[key] = source[key]; } return target; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import PropTypes from 'prop-types';
import React from 'react';
import classNames from 'classnames';
import { settings } from 'carbon-components';
var prefix = settings.prefix;

var SelectItem = function SelectItem(_ref) {
  var _classNames;

  var className = _ref.className,
      value = _ref.value,
      disabled = _ref.disabled,
      hidden = _ref.hidden,
      text = _ref.text,
      other = _objectWithoutProperties(_ref, ["className", "value", "disabled", "hidden", "text"]);

  var selectItemClasses = classNames((_classNames = {}, _defineProperty(_classNames, "".concat(prefix, "--select-option"), true), _defineProperty(_classNames, className, className), _classNames));
  return React.createElement("option", _extends({}, other, {
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
  value: PropTypes.any.isRequired,

  /**
   * Specify an optional className to be applied to the node
   */
  className: PropTypes.string,

  /**
   * Specify whether the <SelectItem> should be disabled
   */
  disabled: PropTypes.bool,

  /**
   * Specify whether the <SelectItem> is hidden
   */
  hidden: PropTypes.bool,

  /**
   * Provide the contents of your <SelectItem>
   */
  text: PropTypes.string.isRequired
};
SelectItem.defaultProps = {
  disabled: false,
  hidden: false,
  value: '',
  text: ''
};
export default SelectItem;