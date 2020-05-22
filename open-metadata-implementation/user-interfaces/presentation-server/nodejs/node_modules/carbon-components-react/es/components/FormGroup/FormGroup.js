function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

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
import classnames from 'classnames';
import { settings } from 'carbon-components';
var prefix = settings.prefix;

var FormGroup = function FormGroup(_ref) {
  var legendText = _ref.legendText,
      invalid = _ref.invalid,
      children = _ref.children,
      className = _ref.className,
      message = _ref.message,
      messageText = _ref.messageText,
      other = _objectWithoutProperties(_ref, ["legendText", "invalid", "children", "className", "message", "messageText"]);

  var classNamesLegend = classnames("".concat(prefix, "--label"), className);
  var classNamesFieldset = classnames("".concat(prefix, "--fieldset"), className);
  return React.createElement("fieldset", _extends({}, invalid && {
    'data-invalid': ''
  }, {
    className: classNamesFieldset
  }, other), React.createElement("legend", {
    className: classNamesLegend
  }, legendText), children, message ? React.createElement("div", {
    className: "".concat(prefix, "--form__requirements")
  }, messageText) : null);
};

FormGroup.propTypes = {
  /**
   * Provide the children form elements to be rendered inside of the <fieldset>
   */
  children: PropTypes.node,

  /**
   * Provide the text to be rendered inside of the fieldset <legend>
   */
  legendText: PropTypes.string.isRequired,

  /**
   * Provide a custom className to be applied to the containing <fieldset> node
   */
  className: PropTypes.string,

  /**
   * Specify whether the <FormGroup> is invalid
   */
  invalid: PropTypes.bool,

  /**
   * Specify whether the message should be displayed in the <FormGroup>
   */
  message: PropTypes.bool,

  /**
   * Provide the text for the message in the <FormGroup>
   */
  messageText: PropTypes.string
};
FormGroup.defaultProps = {
  invalid: false,
  message: false,
  messageText: ''
};
export default FormGroup;