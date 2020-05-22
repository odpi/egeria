"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _carbonComponents = require("carbon-components");

var _classnames = _interopRequireDefault(require("classnames"));

var _propTypes = _interopRequireDefault(require("prop-types"));

var _react = _interopRequireDefault(require("react"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
var prefix = _carbonComponents.settings.prefix;

var SideNavDetails = function SideNavDetails(_ref) {
  var children = _ref.children,
      customClassName = _ref.className,
      title = _ref.title;
  var className = (0, _classnames.default)("".concat(prefix, "--side-nav__details"), customClassName);
  return _react.default.createElement("div", {
    className: className
  }, _react.default.createElement("h2", {
    className: "".concat(prefix, "--side-nav__title"),
    title: title
  }, title), children);
};

SideNavDetails.propTypes = {
  /**
   * Optionally provide a custom class to apply to the underlying <li> node
   */
  className: _propTypes.default.string,

  /**
   * Provide optional children to render in `SideNavDetails`. Useful for
   * rendering the `SideNavSwitcher` component.
   */
  children: _propTypes.default.node,

  /**
   * Provide the text that will be rendered as the title in the component
   */
  title: _propTypes.default.string.isRequired
};
var _default = SideNavDetails;
exports.default = _default;