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
import SideNavIcon from './SideNavIcon';
var prefix = settings.prefix;

var SideNavHeader = function SideNavHeader(_ref) {
  var customClassName = _ref.className,
      children = _ref.children,
      IconElement = _ref.renderIcon;
  var className = cx("".concat(prefix, "--side-nav__header"), customClassName);
  return React.createElement("header", {
    className: className
  }, React.createElement(SideNavIcon, null, React.createElement(IconElement, null)), children);
};

SideNavHeader.propTypes = {
  /**
   * Provide an optional class to be applied to the containing node
   */
  className: PropTypes.string,

  /**
   * Provide an icon to render in the header of the side navigation. Should be
   * a React class.
   */
  renderIcon: PropTypes.oneOfType([PropTypes.func, PropTypes.object]).isRequired,

  /**
   * Property to indicate if the side nav container is open (or not). Use to
   * keep local state and styling in step with the SideNav expansion state.
   */
  isSideNavExpanded: PropTypes.bool
};
export default SideNavHeader;