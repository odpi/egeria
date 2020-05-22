"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _carbonComponents = require("carbon-components");

var _classnames = _interopRequireDefault(require("classnames"));

var _propTypes = _interopRequireDefault(require("prop-types"));

var _react = _interopRequireDefault(require("react"));

var _SideNavIcon = _interopRequireDefault(require("./SideNavIcon"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
var prefix = _carbonComponents.settings.prefix;

var SideNavHeader = function SideNavHeader(_ref) {
  var customClassName = _ref.className,
      children = _ref.children,
      IconElement = _ref.renderIcon;
  var className = (0, _classnames.default)("".concat(prefix, "--side-nav__header"), customClassName);
  return _react.default.createElement("header", {
    className: className
  }, _react.default.createElement(_SideNavIcon.default, null, _react.default.createElement(IconElement, null)), children);
};

SideNavHeader.propTypes = {
  /**
   * Provide an optional class to be applied to the containing node
   */
  className: _propTypes.default.string,

  /**
   * Provide an icon to render in the header of the side navigation. Should be
   * a React class.
   */
  renderIcon: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]).isRequired,

  /**
   * Property to indicate if the side nav container is open (or not). Use to
   * keep local state and styling in step with the SideNav expansion state.
   */
  isSideNavExpanded: _propTypes.default.bool
};
var _default = SideNavHeader;
exports.default = _default;