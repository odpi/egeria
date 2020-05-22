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
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import { Search16 } from '@carbon/icons-react';
import { settings } from 'carbon-components';
import ClickListener from '../../internal/ClickListener';
import warning from 'warning';
var prefix = settings.prefix;
var didWarnAboutDeprecation = false;

var ToolbarSearch =
/*#__PURE__*/
function (_Component) {
  _inherits(ToolbarSearch, _Component);

  function ToolbarSearch(props) {
    var _this;

    _classCallCheck(this, ToolbarSearch);

    _this = _possibleConstructorReturn(this, _getPrototypeOf(ToolbarSearch).call(this, props));

    _defineProperty(_assertThisInitialized(_this), "state", {
      expanded: false
    });

    _defineProperty(_assertThisInitialized(_this), "expandSearch", function () {
      _this.setState({
        expanded: !_this.state.expanded
      });

      _this.input.focus();
    });

    _defineProperty(_assertThisInitialized(_this), "handleClickOutside", function () {
      _this.setState({
        expanded: false
      });
    });

    if (process.env.NODE_ENV !== "production") {
      process.env.NODE_ENV !== "production" ? warning(didWarnAboutDeprecation, 'The ToolbarSearch component has been deprecated and will be removed in the next major release of `carbon-components-react`') : void 0;
      didWarnAboutDeprecation = true;
    }

    return _this;
  }

  _createClass(ToolbarSearch, [{
    key: "render",
    value: function render() {
      var _classNames,
          _this2 = this;

      var _this$props = this.props,
          className = _this$props.className,
          type = _this$props.type,
          id = _this$props.id,
          placeHolderText = _this$props.placeHolderText,
          labelText = _this$props.labelText,
          role = _this$props.role,
          labelId = _this$props.labelId,
          other = _objectWithoutProperties(_this$props, ["className", "type", "id", "placeHolderText", "labelText", "role", "labelId"]);

      var searchClasses = classNames((_classNames = {}, _defineProperty(_classNames, "".concat(prefix, "--search ").concat(prefix, "--search--sm ").concat(prefix, "--toolbar-search"), true), _defineProperty(_classNames, "".concat(prefix, "--toolbar-search--active"), this.state.expanded), _defineProperty(_classNames, className, className), _classNames));
      return React.createElement(ClickListener, {
        onClickOutside: this.handleClickOutside
      }, React.createElement("div", {
        className: searchClasses,
        role: role
      }, React.createElement("label", {
        htmlFor: id,
        className: "".concat(prefix, "--label"),
        id: labelId
      }, labelText), React.createElement("input", _extends({}, other, {
        type: type,
        className: "".concat(prefix, "--search-input"),
        id: id,
        "aria-labelledby": labelId,
        placeholder: placeHolderText,
        ref: function ref(input) {
          _this2.input = input;
        }
      })), React.createElement("button", {
        className: "".concat(prefix, "--toolbar-search__btn"),
        title: labelText,
        onClick: this.expandSearch
      }, React.createElement(Search16, {
        className: "".concat(prefix, "--search-magnifier"),
        "aria-label": labelText
      }))));
    }
  }]);

  return ToolbarSearch;
}(Component);

_defineProperty(ToolbarSearch, "propTypes", {
  /**
   * The child nodes.
   */
  children: PropTypes.node,

  /**
   * The CSS class names.
   */
  className: PropTypes.string,

  /**
   * The `type` of the `<input>`.
   */
  type: PropTypes.string,

  /**
   * `true` to use the small version of the UI.
   */
  small: PropTypes.bool,

  /**
   * The placeholder text of the `<input>`.
   */
  placeHolderText: PropTypes.string,

  /**
   * The text in the `<label>`.
   */
  labelText: PropTypes.node,

  /**
   * The ID of the `<input>`.
   */
  id: PropTypes.string
});

_defineProperty(ToolbarSearch, "defaultProps", {
  type: 'search',
  id: 'search__input',
  labelText: '',
  placeHolderText: '',
  role: 'search',
  labelId: 'search__label'
});

export { ToolbarSearch as default };