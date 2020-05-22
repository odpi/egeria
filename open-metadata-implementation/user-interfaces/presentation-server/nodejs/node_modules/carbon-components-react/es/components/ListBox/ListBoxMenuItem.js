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
import React from 'react';
import PropTypes from 'prop-types';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
/**
 * `ListBoxMenuItem` is a helper component for managing the container class
 * name, alongside any classes for any corresponding states, for a generic list
 * box menu item.
 */

var ListBoxMenuItem = function ListBoxMenuItem(_ref) {
  var _cx;

  var children = _ref.children,
      isActive = _ref.isActive,
      isHighlighted = _ref.isHighlighted,
      rest = _objectWithoutProperties(_ref, ["children", "isActive", "isHighlighted"]);

  var className = cx((_cx = {}, _defineProperty(_cx, "".concat(prefix, "--list-box__menu-item"), true), _defineProperty(_cx, "".concat(prefix, "--list-box__menu-item--active"), isActive), _defineProperty(_cx, "".concat(prefix, "--list-box__menu-item--highlighted"), isHighlighted), _cx));
  return React.createElement("div", _extends({
    className: className
  }, rest), React.createElement("div", {
    className: "".concat(prefix, "--list-box__menu-item__option")
  }, children));
};

ListBoxMenuItem.propTypes = {
  /**
   * Specify any children nodes that hsould be rendered inside of the ListBox
   * Menu Item
   */
  children: PropTypes.node,

  /**
   * Specify whether the current menu item is "active".
   */
  isActive: PropTypes.bool.isRequired,

  /**
   * Specify whether the current menu item is "highlighed".
   */
  isHighlighted: PropTypes.bool.isRequired
};
ListBoxMenuItem.defaultProps = {
  isActive: false,
  isHighlighted: false
};
export default ListBoxMenuItem;