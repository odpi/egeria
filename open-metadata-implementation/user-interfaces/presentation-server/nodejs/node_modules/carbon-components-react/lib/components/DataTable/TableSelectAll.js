"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _propTypes = _interopRequireDefault(require("prop-types"));

var _react = _interopRequireDefault(require("react"));

var _InlineCheckbox = _interopRequireDefault(require("../InlineCheckbox"));

var _classnames = _interopRequireDefault(require("classnames"));

var _carbonComponents = require("carbon-components");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
var prefix = _carbonComponents.settings.prefix;

var TableSelectAll = function TableSelectAll(_ref) {
  var ariaLabel = _ref.ariaLabel,
      checked = _ref.checked,
      id = _ref.id,
      indeterminate = _ref.indeterminate,
      name = _ref.name,
      onSelect = _ref.onSelect,
      disabled = _ref.disabled,
      className = _ref.className;
  return _react.default.createElement("th", {
    scope: "col",
    className: (0, _classnames.default)("".concat(prefix, "--table-column-checkbox"), className)
  }, _react.default.createElement(_InlineCheckbox.default, {
    ariaLabel: ariaLabel,
    checked: checked,
    id: id,
    indeterminate: indeterminate,
    name: name,
    onClick: onSelect,
    disabled: disabled
  }));
};

TableSelectAll.propTypes = {
  /**
   * Specify the aria label for the underlying input control
   */
  ariaLabel: _propTypes.default.string.isRequired,

  /**
   * Specify whether all items are selected, or not
   */
  checked: _propTypes.default.bool.isRequired,

  /**
   * Provide an `id` for the underlying input control
   */
  id: _propTypes.default.string.isRequired,

  /**
   * Specify whether the selection only has a subset of all items
   */
  indeterminate: _propTypes.default.bool,

  /**
   * Provide a `name` for the underlying input control
   */
  name: _propTypes.default.string.isRequired,

  /**
   * Provide a handler to listen to when a user initiates a selection request
   */
  onSelect: _propTypes.default.func.isRequired,

  /**
   * The CSS class names of the cell that wraps the underlying input control
   */
  className: _propTypes.default.string
};
TableSelectAll.defaultProps = {
  ariaLabel: 'Select all rows in the table'
};
var _default = TableSelectAll;
exports.default = _default;