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
import cx from 'classnames';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
export var Table = function Table(_ref) {
  var _cx;

  var className = _ref.className,
      children = _ref.children,
      useZebraStyles = _ref.useZebraStyles,
      size = _ref.size,
      isSortable = _ref.isSortable,
      useStaticWidth = _ref.useStaticWidth,
      shouldShowBorder = _ref.shouldShowBorder,
      stickyHeader = _ref.stickyHeader,
      other = _objectWithoutProperties(_ref, ["className", "children", "useZebraStyles", "size", "isSortable", "useStaticWidth", "shouldShowBorder", "stickyHeader"]);

  var componentClass = cx("".concat(prefix, "--data-table"), className, (_cx = {}, _defineProperty(_cx, "".concat(prefix, "--data-table--compact"), size === 'compact'), _defineProperty(_cx, "".concat(prefix, "--data-table--short"), size === 'short'), _defineProperty(_cx, "".concat(prefix, "--data-table--tall"), size === 'tall'), _defineProperty(_cx, "".concat(prefix, "--data-table--sort"), isSortable), _defineProperty(_cx, "".concat(prefix, "--data-table--zebra"), useZebraStyles), _defineProperty(_cx, "".concat(prefix, "--data-table--static"), useStaticWidth), _defineProperty(_cx, "".concat(prefix, "--data-table--no-border"), !shouldShowBorder), _defineProperty(_cx, "".concat(prefix, "--data-table--sticky-header"), stickyHeader), _cx));
  var table = React.createElement("table", _extends({}, other, {
    className: componentClass
  }), children);
  return stickyHeader ? React.createElement("section", {
    className: "".concat(prefix, "--data-table_inner-container")
  }, table) : table;
};
Table.propTypes = {
  className: PropTypes.string,

  /**
   * `true` to add useZebraStyles striping.
   */
  useZebraStyles: PropTypes.bool,

  /**
   * `normal` Change the row height of table
   */
  size: PropTypes.oneOf(['compact', 'short', 'normal', 'tall']),

  /**
   * `false` If true, will use a width of 'auto' instead of 100%
   */
  useStaticWidth: PropTypes.bool,

  /**
   * `false` If true, will remove the table border
   */
  shouldShowBorder: PropTypes.bool,

  /**
   * `false` If true, will apply sorting styles
   */
  isSortable: PropTypes.bool,

  /**
   * `false` If true, will keep the header sticky (only data rows will scroll)
   */
  stickyHeader: PropTypes.bool
};
Table.defaultProps = {
  isSortable: false
};
export default Table;