"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _propTypes = _interopRequireDefault(require("prop-types"));

var _react = _interopRequireDefault(require("react"));

var _classnames = _interopRequireDefault(require("classnames"));

var _carbonComponents = require("carbon-components");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

function _objectWithoutProperties(source, excluded) { if (source == null) return {}; var target = _objectWithoutPropertiesLoose(source, excluded); var key, i; if (Object.getOwnPropertySymbols) { var sourceSymbolKeys = Object.getOwnPropertySymbols(source); for (i = 0; i < sourceSymbolKeys.length; i++) { key = sourceSymbolKeys[i]; if (excluded.indexOf(key) >= 0) continue; if (!Object.prototype.propertyIsEnumerable.call(source, key)) continue; target[key] = source[key]; } } return target; }

function _objectWithoutPropertiesLoose(source, excluded) { if (source == null) return {}; var target = {}; var sourceKeys = Object.keys(source); var key, i; for (i = 0; i < sourceKeys.length; i++) { key = sourceKeys[i]; if (excluded.indexOf(key) >= 0) continue; target[key] = source[key]; } return target; }

var prefix = _carbonComponents.settings.prefix;

var Switch = _react.default.forwardRef(function Switch(props, tabRef) {
  var className = props.className,
      index = props.index,
      name = props.name,
      onClick = props.onClick,
      onKeyDown = props.onKeyDown,
      selected = props.selected,
      text = props.text,
      other = _objectWithoutProperties(props, ["className", "index", "name", "onClick", "onKeyDown", "selected", "text"]);

  var handleClick = function handleClick(e) {
    e.preventDefault();
    onClick({
      index: index,
      name: name,
      text: text
    });
  };

  var handleKeyDown = function handleKeyDown(event) {
    var key = event.key || event.which;
    onKeyDown({
      index: index,
      name: name,
      text: text,
      key: key
    });
  };

  var classes = (0, _classnames.default)(className, "".concat(prefix, "--content-switcher-btn"), _defineProperty({}, "".concat(prefix, "--content-switcher--selected"), selected));
  var commonProps = {
    onClick: handleClick,
    onKeyDown: handleKeyDown,
    className: classes
  };
  return _react.default.createElement("button", _extends({
    ref: tabRef,
    role: "tab",
    tabIndex: selected ? '0' : '-1',
    "aria-selected": selected
  }, other, commonProps), _react.default.createElement("span", {
    className: "".concat(prefix, "--content-switcher__label")
  }, text));
});

Switch.displayName = 'Switch';
Switch.propTypes = {
  /**
   * Specify an optional className to be added to your Switch
   */
  className: _propTypes.default.string,

  /**
   * The index of your Switch in your ContentSwitcher that is used for event handlers.
   * Reserved for usage in ContentSwitcher
   */
  index: _propTypes.default.number,

  /**
   * Provide the name of your Switch that is used for event handlers
   */
  name: _propTypes.default.oneOfType([_propTypes.default.string, _propTypes.default.number]),

  /**
   * A handler that is invoked when a user clicks on the control.
   * Reserved for usage in ContentSwitcher
   */
  onClick: _propTypes.default.func,

  /**
   * A handler that is invoked on the key down event for the control.
   * Reserved for usage in ContentSwitcher
   */
  onKeyDown: _propTypes.default.func,

  /**
   * Whether your Switch is selected. Reserved for usage in ContentSwitcher
   */
  selected: _propTypes.default.bool,

  /**
   * Provide the contents of your Switch
   */
  text: _propTypes.default.string.isRequired
};
Switch.defaultProps = {
  selected: false,
  text: 'Provide text',
  onClick: function onClick() {},
  onKeyDown: function onKeyDown() {}
};
var _default = Switch;
exports.default = _default;