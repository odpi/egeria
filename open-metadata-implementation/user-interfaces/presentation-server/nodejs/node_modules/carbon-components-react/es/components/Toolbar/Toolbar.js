function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

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
import ToolbarSearch from '../ToolbarSearch';
import classNames from 'classnames';
import { settings } from 'carbon-components';
import warning from 'warning';
var prefix = settings.prefix;
var didWarnAboutDeprecation = false;

var Toolbar = function Toolbar(_ref) {
  var children = _ref.children,
      className = _ref.className,
      other = _objectWithoutProperties(_ref, ["children", "className"]);

  var wrapperClasses = classNames("".concat(prefix, "--toolbar"), className);

  if (process.env.NODE_ENV !== "production") {
    process.env.NODE_ENV !== "production" ? warning(didWarnAboutDeprecation, 'The Toolbar component has been deprecated and will be removed in the next major release of `carbon-components-react`') : void 0;
    didWarnAboutDeprecation = true;
  }

  return React.createElement("div", _extends({
    className: wrapperClasses
  }, other), children);
};

Toolbar.propTypes = {
  /**
   * Specify a collection of ToolbarItem's that should render in the Toolbar
   */
  children: PropTypes.node,

  /**
   * Specify an optional className to be applied to the containing Toolbar node
   */
  className: PropTypes.string
};
export var ToolbarItem = function ToolbarItem(_ref2) {
  var children = _ref2.children,
      type = _ref2.type,
      placeHolderText = _ref2.placeHolderText;
  var toolbarItem = type === 'search' ? React.createElement(ToolbarSearch, {
    placeHolderText: placeHolderText
  }) : children;
  return toolbarItem;
};
ToolbarItem.propTypes = {
  /**
   * Specify the contents of the ToolbarItem
   */
  children: PropTypes.node,

  /**
   * Specify the type of the ToolbarItem. The `search` type will render a
   * `ToolbarSearch` component
   */
  type: PropTypes.string,

  /**
   * Specify the placeholder text for the ToolbarSearch component. Useful if
   * `type` is set to 'search'
   */
  placeHolderText: PropTypes.string
};
ToolbarItem.defaultProps = {
  placeHolderText: 'Provide placeHolderText'
};
export var ToolbarTitle = React.forwardRef(function (_ref3, ref) {
  var title = _ref3.title;
  return React.createElement("li", {
    ref: ref,
    className: "".concat(prefix, "--toolbar-menu__title")
  }, title);
});
ToolbarTitle.propTypes = {
  /**
   * Specify the title of the Toolbar
   */
  title: PropTypes.string
};
export var ToolbarOption = React.forwardRef(function (_ref4, ref) {
  var children = _ref4.children;
  return React.createElement("li", {
    ref: ref,
    className: "".concat(prefix, "--toolbar-menu__option")
  }, children);
});
ToolbarOption.propTypes = {
  /**
   * Specify the contents of the ToolbarOption
   */
  children: PropTypes.node
};
export var ToolbarDivider = React.forwardRef(function (props, ref) {
  return React.createElement("hr", {
    ref: ref,
    className: "".concat(prefix, "--toolbar-menu__divider")
  });
});
export default Toolbar;