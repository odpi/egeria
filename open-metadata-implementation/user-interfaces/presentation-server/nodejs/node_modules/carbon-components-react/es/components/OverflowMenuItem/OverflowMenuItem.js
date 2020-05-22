function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

function _objectWithoutProperties(source, excluded) { if (source == null) return {}; var target = _objectWithoutPropertiesLoose(source, excluded); var key, i; if (Object.getOwnPropertySymbols) { var sourceSymbolKeys = Object.getOwnPropertySymbols(source); for (i = 0; i < sourceSymbolKeys.length; i++) { key = sourceSymbolKeys[i]; if (excluded.indexOf(key) >= 0) continue; if (!Object.prototype.propertyIsEnumerable.call(source, key)) continue; target[key] = source[key]; } } return target; }

function _objectWithoutPropertiesLoose(source, excluded) { if (source == null) return {}; var target = {}; var sourceKeys = Object.keys(source); var key, i; for (i = 0; i < sourceKeys.length; i++) { key = sourceKeys[i]; if (excluded.indexOf(key) >= 0) continue; target[key] = source[key]; } return target; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } return _assertThisInitialized(self); }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import PropTypes from 'prop-types';
import React from 'react';
import classNames from 'classnames';
import warning from 'warning';
import { settings } from 'carbon-components';
import { match, keys } from '../../internal/keyboard';
var prefix = settings.prefix;

var OverflowMenuItem =
/*#__PURE__*/
function (_React$Component) {
  _inherits(OverflowMenuItem, _React$Component);

  function OverflowMenuItem() {
    var _getPrototypeOf2;

    var _this;

    _classCallCheck(this, OverflowMenuItem);

    for (var _len = arguments.length, args = new Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }

    _this = _possibleConstructorReturn(this, (_getPrototypeOf2 = _getPrototypeOf(OverflowMenuItem)).call.apply(_getPrototypeOf2, [this].concat(args)));

    _defineProperty(_assertThisInitialized(_this), "overflowMenuItem", React.createRef());

    _defineProperty(_assertThisInitialized(_this), "setTabFocus", function (evt) {
      if (match(evt, keys.ArrowDown)) {
        _this.props.handleOverflowMenuItemFocus(_this.props.index + 1);
      }

      if (match(evt, keys.ArrowUp)) {
        _this.props.handleOverflowMenuItemFocus(_this.props.index - 1);
      }
    });

    _defineProperty(_assertThisInitialized(_this), "handleClick", function (evt) {
      var _this$props = _this.props,
          onClick = _this$props.onClick,
          closeMenu = _this$props.closeMenu;
      onClick(evt);

      if (closeMenu) {
        closeMenu();
      }
    });

    return _this;
  }

  _createClass(OverflowMenuItem, [{
    key: "render",
    value: function render() {
      var _classNames,
          _this2 = this;

      var _this$props2 = this.props,
          href = _this$props2.href,
          className = _this$props2.className,
          itemText = _this$props2.itemText,
          hasDivider = _this$props2.hasDivider,
          isDelete = _this$props2.isDelete,
          disabled = _this$props2.disabled,
          closeMenu = _this$props2.closeMenu,
          onClick = _this$props2.onClick,
          handleOverflowMenuItemFocus = _this$props2.handleOverflowMenuItemFocus,
          _onKeyDown = _this$props2.onKeyDown,
          primaryFocus = _this$props2.primaryFocus,
          wrapperClassName = _this$props2.wrapperClassName,
          requireTitle = _this$props2.requireTitle,
          index = _this$props2.index,
          other = _objectWithoutProperties(_this$props2, ["href", "className", "itemText", "hasDivider", "isDelete", "disabled", "closeMenu", "onClick", "handleOverflowMenuItemFocus", "onKeyDown", "primaryFocus", "wrapperClassName", "requireTitle", "index"]);

      if (process.env.NODE_ENV !== "production") {
        process.env.NODE_ENV !== "production" ? warning(closeMenu, '`<OverflowMenuItem>` detected missing `closeMenu` prop. ' + '`closeMenu` is required to let `<OverflowMenu>` close the menu upon actions on `<OverflowMenuItem>`. ' + 'Please make sure `<OverflowMenuItem>` is a direct child of `<OverflowMenu>.') : void 0;
      }

      var overflowMenuBtnClasses = classNames("".concat(prefix, "--overflow-menu-options__btn"), className);
      var overflowMenuItemClasses = classNames("".concat(prefix, "--overflow-menu-options__option"), (_classNames = {}, _defineProperty(_classNames, "".concat(prefix, "--overflow-menu--divider"), hasDivider), _defineProperty(_classNames, "".concat(prefix, "--overflow-menu-options__option--danger"), isDelete), _defineProperty(_classNames, "".concat(prefix, "--overflow-menu-options__option--disabled"), disabled), _classNames), wrapperClassName);
      var primaryFocusProp = primaryFocus ? {
        'data-floating-menu-primary-focus': true
      } : {};
      var TagToUse = href ? 'a' : 'button';

      var OverflowMenuItemContent = function () {
        if (typeof itemText !== 'string') {
          return itemText;
        }

        return React.createElement("div", {
          className: "".concat(prefix, "--overflow-menu-options__option-content")
        }, itemText);
      }();

      return React.createElement("li", {
        className: overflowMenuItemClasses,
        role: "menuitem"
      }, React.createElement(TagToUse, _extends({}, other, primaryFocusProp, {
        href: href,
        className: overflowMenuBtnClasses,
        disabled: disabled,
        onClick: this.handleClick,
        onKeyDown: function onKeyDown(evt) {
          _this2.setTabFocus(evt);

          _onKeyDown(evt);
        },
        ref: this.overflowMenuItem,
        title: requireTitle ? itemText : null,
        tabIndex: "-1",
        index: index
      }), OverflowMenuItemContent));
    }
  }]);

  return OverflowMenuItem;
}(React.Component);

_defineProperty(OverflowMenuItem, "propTypes", {
  /**
   * The CSS class name to be placed on the button element
   */
  className: PropTypes.string,

  /**
   * The CSS class name to be placed on the wrapper list item element
   */
  wrapperClassName: PropTypes.string,

  /**
   * The text in the menu item.
   */
  itemText: PropTypes.node.isRequired,

  /**
   * If given, overflow item will render as a link with the given href
   */
  href: PropTypes.string,

  /**
   * `true` to make this menu item a divider.
   */
  hasDivider: PropTypes.bool,

  /**
   * `true` to make this menu item a "danger button".
   */
  isDelete: PropTypes.bool,

  /**
   * `true` to make this menu item disabled.
   */
  disabled: PropTypes.bool,

  /**
   * event handlers
   */
  onBlur: PropTypes.func,
  onClick: PropTypes.func,
  onFocus: PropTypes.func,
  onKeyDown: PropTypes.func,
  onKeyUp: PropTypes.func,
  onMouseDown: PropTypes.func,
  onMouseEnter: PropTypes.func,
  onMouseLeave: PropTypes.func,
  onMouseUp: PropTypes.func,

  /**
   * A callback to tell the parent menu component that the menu should be closed.
   */
  closeMenu: PropTypes.func,

  /**
   * `true` if this menu item should get focus when the menu gets open.
   */
  primaryFocus: PropTypes.bool,

  /**
   * `true` if this menu item has long text and requires a browser tooltip
   */
  requireTitle: PropTypes.bool
});

_defineProperty(OverflowMenuItem, "defaultProps", {
  hasDivider: false,
  isDelete: false,
  disabled: false,
  itemText: 'Provide itemText',
  onClick: function onClick() {},
  onKeyDown: function onKeyDown() {}
});

export { OverflowMenuItem as default };