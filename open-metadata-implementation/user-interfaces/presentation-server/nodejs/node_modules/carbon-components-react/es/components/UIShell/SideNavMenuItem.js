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
import { settings } from 'carbon-components';
import cx from 'classnames';
import PropTypes from 'prop-types';
import React from 'react';
import SideNavLinkText from './SideNavLinkText';
import Link from './Link';
var prefix = settings.prefix;
var SideNavMenuItem = React.forwardRef(function SideNavMenuItem(props, ref) {
  var _cx;

  var children = props.children,
      customClassName = props.className,
      isActive = props.isActive,
      rest = _objectWithoutProperties(props, ["children", "className", "isActive"]);

  var className = cx("".concat(prefix, "--side-nav__menu-item"), customClassName);
  var linkClassName = cx((_cx = {}, _defineProperty(_cx, "".concat(prefix, "--side-nav__link"), true), _defineProperty(_cx, "".concat(prefix, "--side-nav__link--current"), isActive), _cx));
  return React.createElement("li", {
    className: className,
    role: "none"
  }, React.createElement(Link, _extends({}, rest, {
    className: linkClassName,
    role: "menuitem",
    ref: ref
  }), React.createElement(SideNavLinkText, null, children)));
});
SideNavMenuItem.propTypes = {
  /**
   * Specify the childrento be rendered inside of the `SideNavMenuItem`
   */
  children: PropTypes.node,

  /**
   * Provide an optional class to be applied to the containing node
   */
  className: PropTypes.string,

  /**
   * Optionally specify whether the link is "active". An active link is one that
   * has an href that is the same as the current page. Can also pass in
   * `aria-current="page"`, as well.
   */
  isActive: PropTypes.bool
};
export default SideNavMenuItem;