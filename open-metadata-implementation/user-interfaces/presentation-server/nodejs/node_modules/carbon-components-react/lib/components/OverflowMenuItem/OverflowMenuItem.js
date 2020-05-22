"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _propTypes = _interopRequireDefault(require("prop-types"));

var _react = _interopRequireDefault(require("react"));

var _classnames = _interopRequireDefault(require("classnames"));

var _warning = _interopRequireDefault(require("warning"));

var _carbonComponents = require("carbon-components");

var _keyboard = require("../../internal/keyboard");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

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

var prefix = _carbonComponents.settings.prefix;

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

    _defineProperty(_assertThisInitialized(_this), "overflowMenuItem", _react.default.createRef());

    _defineProperty(_assertThisInitialized(_this), "setTabFocus", function (evt) {
      if ((0, _keyboard.match)(evt, _keyboard.keys.ArrowDown)) {
        _this.props.handleOverflowMenuItemFocus(_this.props.index + 1);
      }

      if ((0, _keyboard.match)(evt, _keyboard.keys.ArrowUp)) {
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
        process.env.NODE_ENV !== "production" ? (0, _warning.default)(closeMenu, '`<OverflowMenuItem>` detected missing `closeMenu` prop. ' + '`closeMenu` is required to let `<OverflowMenu>` close the menu upon actions on `<OverflowMenuItem>`. ' + 'Please make sure `<OverflowMenuItem>` is a direct child of `<OverflowMenu>.') : void 0;
      }

      var overflowMenuBtnClasses = (0, _classnames.default)("".concat(prefix, "--overflow-menu-options__btn"), className);
      var overflowMenuItemClasses = (0, _classnames.default)("".concat(prefix, "--overflow-menu-options__option"), (_classNames = {}, _defineProperty(_classNames, "".concat(prefix, "--overflow-menu--divider"), hasDivider), _defineProperty(_classNames, "".concat(prefix, "--overflow-menu-options__option--danger"), isDelete), _defineProperty(_classNames, "".concat(prefix, "--overflow-menu-options__option--disabled"), disabled), _classNames), wrapperClassName);
      var primaryFocusProp = primaryFocus ? {
        'data-floating-menu-primary-focus': true
      } : {};
      var TagToUse = href ? 'a' : 'button';

      var OverflowMenuItemContent = function () {
        if (typeof itemText !== 'string') {
          return itemText;
        }

        return _react.default.createElement("div", {
          className: "".concat(prefix, "--overflow-menu-options__option-content")
        }, itemText);
      }();

      return _react.default.createElement("li", {
        className: overflowMenuItemClasses,
        role: "menuitem"
      }, _react.default.createElement(TagToUse, _extends({}, other, primaryFocusProp, {
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
}(_react.default.Component);

exports.default = OverflowMenuItem;

_defineProperty(OverflowMenuItem, "propTypes", {
  /**
   * The CSS class name to be placed on the button element
   */
  className: _propTypes.default.string,

  /**
   * The CSS class name to be placed on the wrapper list item element
   */
  wrapperClassName: _propTypes.default.string,

  /**
   * The text in the menu item.
   */
  itemText: _propTypes.default.node.isRequired,

  /**
   * If given, overflow item will render as a link with the given href
   */
  href: _propTypes.default.string,

  /**
   * `true` to make this menu item a divider.
   */
  hasDivider: _propTypes.default.bool,

  /**
   * `true` to make this menu item a "danger button".
   */
  isDelete: _propTypes.default.bool,

  /**
   * `true` to make this menu item disabled.
   */
  disabled: _propTypes.default.bool,

  /**
   * event handlers
   */
  onBlur: _propTypes.default.func,
  onClick: _propTypes.default.func,
  onFocus: _propTypes.default.func,
  onKeyDown: _propTypes.default.func,
  onKeyUp: _propTypes.default.func,
  onMouseDown: _propTypes.default.func,
  onMouseEnter: _propTypes.default.func,
  onMouseLeave: _propTypes.default.func,
  onMouseUp: _propTypes.default.func,

  /**
   * A callback to tell the parent menu component that the menu should be closed.
   */
  closeMenu: _propTypes.default.func,

  /**
   * `true` if this menu item should get focus when the menu gets open.
   */
  primaryFocus: _propTypes.default.bool,

  /**
   * `true` if this menu item has long text and requires a browser tooltip
   */
  requireTitle: _propTypes.default.bool
});

_defineProperty(OverflowMenuItem, "defaultProps", {
  hasDivider: false,
  isDelete: false,
  disabled: false,
  itemText: 'Provide itemText',
  onClick: function onClick() {},
  onKeyDown: function onKeyDown() {}
});