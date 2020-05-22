"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _iconsReact = require("@carbon/icons-react");

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

var SideNavSwitcher = _react.default.forwardRef(function SideNavSwitcher(props, ref) {
  var customClassName = props.className,
      labelText = props.labelText,
      onChange = props.onChange,
      options = props.options;
  var className = (0, _classnames.default)("".concat(prefix, "--side-nav__switcher"), customClassName); // Note for usage around `onBlur`: https://github.com/evcohen/eslint-plugin-jsx-a11y/blob/master/docs/rules/no-onchange.md

  return _react.default.createElement("div", {
    className: className
  }, _react.default.createElement("label", {
    htmlFor: "side-nav-switcher",
    className: "".concat(prefix, "--assistive-text")
  }, labelText), _react.default.createElement("select", {
    id: "carbon-side-nav-switcher",
    className: "".concat(prefix, "--side-nav__select"),
    defaultValue: "",
    onBlur: onChange,
    onChange: onChange,
    ref: ref
  }, _react.default.createElement("option", {
    className: "".concat(prefix, "--side-nav__option"),
    disabled: true,
    hidden: true,
    value: ""
  }, labelText), options.map(function (option) {
    return _react.default.createElement("option", {
      key: option,
      className: "".concat(prefix, "--side-nav__option"),
      value: option
    }, option);
  })), _react.default.createElement("div", {
    className: "".concat(prefix, "--side-nav__switcher-chevron")
  }, _react.default.createElement(_iconsReact.ChevronDown20, null)));
});

SideNavSwitcher.propTypes = {
  /**
   * Provide an optional class to be applied to the containing node
   */
  className: _propTypes.default.string,

  /**
   * Provide the label for the switcher. This will be the firt visible option
   * when someone views this control
   */
  labelText: _propTypes.default.string.isRequired,

  /**
   * Provide a callback function that is called whenever the switcher value is
   * updated
   */
  onChange: _propTypes.default.func,

  /**
   * Provide an array of options to be rendered in the switcher as an
   * `<option>`. The text value will be what is displayed to the user and is set
   * as the `value` prop for each `<option>`.
   */
  options: _propTypes.default.arrayOf(_propTypes.default.string).isRequired
};
var _default = SideNavSwitcher;
exports.default = _default;