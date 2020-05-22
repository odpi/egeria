"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = FileUploaderItem;

var _react = _interopRequireDefault(require("react"));

var _propTypes = _interopRequireDefault(require("prop-types"));

var _carbonComponents = require("carbon-components");

var _classnames = _interopRequireDefault(require("classnames"));

var _FileUploader = require("./FileUploader");

var _keyboard = require("../../internal/keyboard");

var _uniqueId = _interopRequireDefault(require("../../tools/uniqueId"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

function _objectWithoutProperties(source, excluded) { if (source == null) return {}; var target = _objectWithoutPropertiesLoose(source, excluded); var key, i; if (Object.getOwnPropertySymbols) { var sourceSymbolKeys = Object.getOwnPropertySymbols(source); for (i = 0; i < sourceSymbolKeys.length; i++) { key = sourceSymbolKeys[i]; if (excluded.indexOf(key) >= 0) continue; if (!Object.prototype.propertyIsEnumerable.call(source, key)) continue; target[key] = source[key]; } } return target; }

function _objectWithoutPropertiesLoose(source, excluded) { if (source == null) return {}; var target = {}; var sourceKeys = Object.keys(source); var key, i; for (i = 0; i < sourceKeys.length; i++) { key = sourceKeys[i]; if (excluded.indexOf(key) >= 0) continue; target[key] = source[key]; } return target; }

var prefix = _carbonComponents.settings.prefix;

function FileUploaderItem(_ref) {
  var uuid = _ref.uuid,
      name = _ref.name,
      status = _ref.status,
      iconDescription = _ref.iconDescription,
      onDelete = _ref.onDelete,
      invalid = _ref.invalid,
      errorSubject = _ref.errorSubject,
      errorBody = _ref.errorBody,
      other = _objectWithoutProperties(_ref, ["uuid", "name", "status", "iconDescription", "onDelete", "invalid", "errorSubject", "errorBody"]);

  var classes = (0, _classnames.default)("".concat(prefix, "--file__selected-file"), _defineProperty({}, "".concat(prefix, "--file__selected-file--invalid"), invalid));
  return _react.default.createElement("span", _extends({
    className: classes
  }, other), _react.default.createElement("p", {
    className: "".concat(prefix, "--file-filename")
  }, name), _react.default.createElement("span", {
    className: "".concat(prefix, "--file__state-container")
  }, _react.default.createElement(_FileUploader.Filename, {
    iconDescription: iconDescription,
    status: status,
    invalid: invalid,
    onKeyDown: function onKeyDown(evt) {
      if ((0, _keyboard.matches)(evt, [_keyboard.keys.Enter, _keyboard.keys.Space])) {
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
  })), invalid && errorSubject && _react.default.createElement("div", {
    className: "".concat(prefix, "--form-requirement")
  }, _react.default.createElement("div", {
    className: "".concat(prefix, "--form-requirement__title")
  }, errorSubject), errorBody && _react.default.createElement("p", {
    className: "".concat(prefix, "--form-requirement__supplement")
  }, errorBody)));
}

FileUploaderItem.propTypes = {
  /**
   * Unique identifier for the file object
   */
  uuid: _propTypes.default.string.isRequired,

  /**
   * Name of the uploaded file
   */
  name: _propTypes.default.string,

  /**
   * Status of the file upload
   */
  status: _propTypes.default.oneOf(['uploading', 'edit', 'complete']),

  /**
   * Description of status icon (displayed in native tooltip)
   */
  iconDescription: _propTypes.default.string,

  /**
   * Specify if the currently uploaded file is invalid
   */
  invalid: _propTypes.default.bool,

  /**
   * Event handler that is called after removing a file from the file uploader
   * The event handler signature looks like `onDelete(evt, { uuid })`
   */
  onDelete: _propTypes.default.func,

  /**
   * Error message subject for an invalid file upload
   */
  errorSubject: _propTypes.default.string,

  /**
   * Error message body for an invalid file upload
   */
  errorBody: _propTypes.default.string
};
FileUploaderItem.defaultProps = {
  uuid: (0, _uniqueId.default)(),
  status: 'uploading',
  onDelete: function onDelete() {}
};