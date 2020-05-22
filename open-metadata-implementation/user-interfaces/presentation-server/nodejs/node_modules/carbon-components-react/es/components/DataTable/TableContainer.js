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
import cx from 'classnames';
import PropTypes from 'prop-types';
import React from 'react';
import { settings } from 'carbon-components';
var prefix = settings.prefix;

var TableContainer = function TableContainer(_ref) {
  var className = _ref.className,
      children = _ref.children,
      title = _ref.title,
      description = _ref.description,
      stickyHeader = _ref.stickyHeader,
      rest = _objectWithoutProperties(_ref, ["className", "children", "title", "description", "stickyHeader"]);

  var tableContainerClasses = cx(className, "".concat(prefix, "--data-table-container"), _defineProperty({}, "".concat(prefix, "--data-table--max-width"), stickyHeader));
  return React.createElement("div", _extends({}, rest, {
    className: tableContainerClasses
  }), title && React.createElement("div", {
    className: "".concat(prefix, "--data-table-header")
  }, React.createElement("h4", {
    className: "".concat(prefix, "--data-table-header__title")
  }, title), React.createElement("p", {
    className: "".concat(prefix, "--data-table-header__description")
  }, description)), children);
};

TableContainer.propTypes = {
  className: PropTypes.string,
  children: PropTypes.node,

  /**
   * Provide a title for the Table
   */
  title: PropTypes.node,

  /**
   * Optional description text for the Table
   */
  description: PropTypes.node
};
export default TableContainer;