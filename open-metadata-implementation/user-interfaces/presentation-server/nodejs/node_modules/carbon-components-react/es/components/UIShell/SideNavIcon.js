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

var SideNavIcon = function SideNavIcon(_ref) {
  var _cx;

  var children = _ref.children,
      customClassName = _ref.className,
      small = _ref.small;
  var className = cx((_cx = {}, _defineProperty(_cx, "".concat(prefix, "--side-nav__icon"), true), _defineProperty(_cx, "".concat(prefix, "--side-nav__icon--small"), small), _defineProperty(_cx, customClassName, !!customClassName), _cx));
  return React.createElement("div", {
    className: className
  }, children);
};

SideNavIcon.propTypes = {
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
   * Specify whether the icon should be placed in a smaller bounding box
   */
  small: PropTypes.bool.isRequired
};
SideNavIcon.defaultProps = {
  small: false
};
export default SideNavIcon;