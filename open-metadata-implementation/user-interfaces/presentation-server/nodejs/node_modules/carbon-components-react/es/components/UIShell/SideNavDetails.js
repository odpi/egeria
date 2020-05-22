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

var SideNavDetails = function SideNavDetails(_ref) {
  var children = _ref.children,
      customClassName = _ref.className,
      title = _ref.title;
  var className = cx("".concat(prefix, "--side-nav__details"), customClassName);
  return React.createElement("div", {
    className: className
  }, React.createElement("h2", {
    className: "".concat(prefix, "--side-nav__title"),
    title: title
  }, title), children);
};

SideNavDetails.propTypes = {
  /**
   * Optionally provide a custom class to apply to the underlying <li> node
   */
  className: PropTypes.string,

  /**
   * Provide optional children to render in `SideNavDetails`. Useful for
   * rendering the `SideNavSwitcher` component.
   */
  children: PropTypes.node,

  /**
   * Provide the text that will be rendered as the title in the component
   */
  title: PropTypes.string.isRequired
};
export default SideNavDetails;