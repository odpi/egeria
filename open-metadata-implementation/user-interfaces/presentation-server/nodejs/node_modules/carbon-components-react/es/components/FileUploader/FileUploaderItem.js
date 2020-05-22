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
import React from 'react';
import PropTypes from 'prop-types';
import { settings } from 'carbon-components';
import classNames from 'classnames';
import { Filename } from './FileUploader';
import { keys, matches } from '../../internal/keyboard';
import uid from '../../tools/uniqueId';
var prefix = settings.prefix;
export default function FileUploaderItem(_ref) {
  var uuid = _ref.uuid,
      name = _ref.name,
      status = _ref.status,
      iconDescription = _ref.iconDescription,
      onDelete = _ref.onDelete,
      invalid = _ref.invalid,
      errorSubject = _ref.errorSubject,
      errorBody = _ref.errorBody,
      other = _objectWithoutProperties(_ref, ["uuid", "name", "status", "iconDescription", "onDelete", "invalid", "errorSubject", "errorBody"]);

  var classes = classNames("".concat(prefix, "--file__selected-file"), _defineProperty({}, "".concat(prefix, "--file__selected-file--invalid"), invalid));
  return React.createElement("span", _extends({
    className: classes
  }, other), React.createElement("p", {
    className: "".concat(prefix, "--file-filename")
  }, name), React.createElement("span", {
    className: "".concat(prefix, "--file__state-container")
  }, React.createElement(Filename, {
    iconDescription: iconDescription,
    status: status,
    invalid: invalid,
    onKeyDown: function onKeyDown(evt) {
      if (matches(evt, [keys.Enter, keys.Space])) {
        if (status === 'edit') {
          onDelete(evt, {
            uuid: uuid
          });
        }
      }
    },
    onClick: function onClick(evt) {
      if (status === 'edit') {
        onDelete(evt, {
          uuid: uuid
        });
      }
    }
  })), invalid && errorSubject && React.createElement("div", {
    className: "".concat(prefix, "--form-requirement")
  }, React.createElement("div", {
    className: "".concat(prefix, "--form-requirement__title")
  }, errorSubject), errorBody && React.createElement("p", {
    className: "".concat(prefix, "--form-requirement__supplement")
  }, errorBody)));
}
FileUploaderItem.propTypes = {
  /**
   * Unique identifier for the file object
   */
  uuid: PropTypes.string.isRequired,

  /**
   * Name of the uploaded file
   */
  name: PropTypes.string,

  /**
   * Status of the file upload
   */
  status: PropTypes.oneOf(['uploading', 'edit', 'complete']),

  /**
   * Description of status icon (displayed in native tooltip)
   */
  iconDescription: PropTypes.string,

  /**
   * Specify if the currently uploaded file is invalid
   */
  invalid: PropTypes.bool,

  /**
   * Event handler that is called after removing a file from the file uploader
   * The event handler signature looks like `onDelete(evt, { uuid })`
   */
  onDelete: PropTypes.func,

  /**
   * Error message subject for an invalid file upload
   */
  errorSubject: PropTypes.string,

  /**
   * Error message body for an invalid file upload
   */
  errorBody: PropTypes.string
};
FileUploaderItem.defaultProps = {
  uuid: uid(),
  status: 'uploading',
  onDelete: function onDelete() {}
};