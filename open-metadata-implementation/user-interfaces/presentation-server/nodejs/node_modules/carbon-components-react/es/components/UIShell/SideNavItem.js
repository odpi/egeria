function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

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
var prefix = settings.prefix;

var SideNavItem = function SideNavItem(_ref) {
  var _cx;

  var customClassName = _ref.className,
      children = _ref.children,
      _ref$large = _ref.large,
      large = _ref$large === void 0 ? false : _ref$large;
  var className = cx((_cx = {}, _defineProperty(_cx, "".concat(prefix, "--side-nav__item"), true), _defineProperty(_cx, "".concat(prefix, "--side-nav__item--large"), large), _defineProperty(_cx, customClassName, !!customClassName), _cx));
  return React.createElement("li", {
    className: className
  }, children);
};

SideNavItem.propTypes = {
  /**
   * Provide an optional class to be applied to the containing node
   */
  className: PropTypes.string,

  /**
   * Provide a single icon as the child to `SideNavIcon` to render in the
   * container
   */
  children: PropTypes.node.isRequired,

  /**
   * Specify if this is a large variation of the SideNavItem
   */
  large: PropTypes.bool
};
export default SideNavItem;