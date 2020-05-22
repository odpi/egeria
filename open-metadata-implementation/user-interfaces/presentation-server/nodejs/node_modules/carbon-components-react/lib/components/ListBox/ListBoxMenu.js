"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _react = _interopRequireDefault(require("react"));

var _carbonComponents = require("carbon-components");

var _propTypes = _interopRequireDefault(require("prop-types"));

var _ListBoxMenuItem = _interopRequireDefault(require("./ListBoxMenuItem"));

var _childrenOfType = _interopRequireDefault(require("../../prop-types/childrenOfType"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

function _objectWithoutProperties(source, excluded) { if (source == null) return {}; var target = _objectWithoutPropertiesLoose(source, excluded); var key, i; if (Object.getOwnPropertySymbols) { var sourceSymbolKeys = Object.getOwnPropertySymbols(source); for (i = 0; i < sourceSymbolKeys.length; i++) { key = sourceSymbolKeys[i]; if (excluded.indexOf(key) >= 0) continue; if (!Object.prototype.propertyIsEnumerable.call(source, key)) continue; target[key] = source[key]; } } return target; }

function _objectWithoutPropertiesLoose(source, excluded) { if (source == null) return {}; var target = {}; var sourceKeys = Object.keys(source); var key, i; for (i = 0; i < sourceKeys.length; i++) { key = sourceKeys[i]; if (excluded.indexOf(key) >= 0) continue; target[key] = source[key]; } return target; }

var prefix = _carbonComponents.settings.prefix;
/**
 * `ListBoxMenu` is a simple container node that isolates the `list-box__menu`
 * class into a single component. It is also being used to validate given
 * `children` components.
 */

var ListBoxMenu = function ListBoxMenu(_ref) {
  var children = _ref.children,
      id = _ref.id,
      rest = _objectWithoutProperties(_ref, ["children", "id"]);

  return _react.default.createElement("div", _extends({
    id: "".concat(id, "__menu"),
    className: "".concat(prefix, "--list-box__menu"),
    role: "listbox"
  }, rest), children);
};

ListBoxMenu.propTypes = {
  /**
   * Provide the contents of your ListBoxMenu
   */
  children: (0, _childrenOfType.default)(_ListBoxMenuItem.default),

  /**
   * Specify a custom `id`
   */
  id: _propTypes.default.string.isRequired
};
var _default = ListBoxMenu;
exports.default = _default;