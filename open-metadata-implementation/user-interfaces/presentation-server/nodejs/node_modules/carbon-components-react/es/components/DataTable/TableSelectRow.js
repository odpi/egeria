function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

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
import InlineCheckbox from '../InlineCheckbox';
import RadioButton from '../RadioButton';
var prefix = settings.prefix;

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
  var InlineInputComponent = radio ? RadioButton : InlineCheckbox;
  var tableSelectRowClasses = classNames("".concat(prefix, "--table-column-checkbox"), _defineProperty({}, className, className));
  return React.createElement("td", {
    className: tableSelectRowClasses
  }, React.createElement(InlineInputComponent, _extends({}, selectionInputProps, radio && {
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
  ariaLabel: PropTypes.string.isRequired,

  /**
   * Specify whether all items are selected, or not
   */
  checked: PropTypes.bool.isRequired,

  /**
   * Specify whether the control is disabled
   */
  disabled: PropTypes.bool,

  /**
   * Provide an `id` for the underlying input control
   */
  id: PropTypes.string.isRequired,

  /**
   * Provide a `name` for the underlying input control
   */
  name: PropTypes.string.isRequired,

  /**
   * Provide a handler to listen to when a user initiates a selection request
   */
  onSelect: PropTypes.func.isRequired,

  /**
   * Specify whether the control should be a radio button or inline checkbox
   */
  radio: PropTypes.bool,

  /**
   * The CSS class names of the cell that wraps the underlying input control
   */
  className: PropTypes.string
};
export default TableSelectRow;