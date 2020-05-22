function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function ownKeys(object, enumerableOnly) { var keys = Object.keys(object); if (Object.getOwnPropertySymbols) { var symbols = Object.getOwnPropertySymbols(object); if (enumerableOnly) symbols = symbols.filter(function (sym) { return Object.getOwnPropertyDescriptor(object, sym).enumerable; }); keys.push.apply(keys, symbols); } return keys; }

function _objectSpread(target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i] != null ? arguments[i] : {}; if (i % 2) { ownKeys(Object(source), true).forEach(function (key) { _defineProperty(target, key, source[key]); }); } else if (Object.getOwnPropertyDescriptors) { Object.defineProperties(target, Object.getOwnPropertyDescriptors(source)); } else { ownKeys(Object(source)).forEach(function (key) { Object.defineProperty(target, key, Object.getOwnPropertyDescriptor(source, key)); }); } } return target; }

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
import Modal from '../Modal';
import Button from '../Button';
import { ButtonKinds } from '../../prop-types/types';

var ModalWrapper =
/*#__PURE__*/
function (_React$Component) {
  _inherits(ModalWrapper, _React$Component);

  function ModalWrapper() {
    var _getPrototypeOf2;

    var _this;

    _classCallCheck(this, ModalWrapper);

    for (var _len = arguments.length, args = new Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }

    _this = _possibleConstructorReturn(this, (_getPrototypeOf2 = _getPrototypeOf(ModalWrapper)).call.apply(_getPrototypeOf2, [this].concat(args)));

    _defineProperty(_assertThisInitialized(_this), "triggerButton", React.createRef());

    _defineProperty(_assertThisInitialized(_this), "state", {
      isOpen: false
    });

    _defineProperty(_assertThisInitialized(_this), "handleOpen", function () {
      _this.setState({
        isOpen: true
      });
    });

    _defineProperty(_assertThisInitialized(_this), "handleClose", function () {
      _this.setState({
        isOpen: false
      }, function () {
        return _this.triggerButton.current.focus();
      });
    });

    _defineProperty(_assertThisInitialized(_this), "handleOnRequestSubmit", function () {
      var _this$props = _this.props,
          handleSubmit = _this$props.handleSubmit,
          shouldCloseAfterSubmit = _this$props.shouldCloseAfterSubmit;

      if (handleSubmit()) {
        if (shouldCloseAfterSubmit) {
          _this.handleClose();
        }
      }
    });

    return _this;
  }

  _createClass(ModalWrapper, [{
    key: "render",
    value: function render() {
      var _this2 = this;

      var _this$props2 = this.props,
          children = _this$props2.children,
          _onKeyDown = _this$props2.onKeyDown,
          buttonTriggerText = _this$props2.buttonTriggerText,
          buttonTriggerClassName = _this$props2.buttonTriggerClassName,
          renderTriggerButtonIcon = _this$props2.renderTriggerButtonIcon,
          triggerButtonIconDescription = _this$props2.triggerButtonIconDescription,
          triggerButtonKind = _this$props2.triggerButtonKind,
          disabled = _this$props2.disabled,
          handleSubmit = _this$props2.handleSubmit,
          shouldCloseAfterSubmit = _this$props2.shouldCloseAfterSubmit,
          selectorPrimaryFocus = _this$props2.selectorPrimaryFocus,
          other = _objectWithoutProperties(_this$props2, ["children", "onKeyDown", "buttonTriggerText", "buttonTriggerClassName", "renderTriggerButtonIcon", "triggerButtonIconDescription", "triggerButtonKind", "disabled", "handleSubmit", "shouldCloseAfterSubmit", "selectorPrimaryFocus"]);

      var props = _objectSpread({}, other, {
        selectorPrimaryFocus: selectorPrimaryFocus,
        open: this.state.isOpen,
        onRequestClose: this.handleClose,
        onRequestSubmit: this.handleOnRequestSubmit
      });

      return React.createElement("div", {
        role: "presentation",
        onKeyDown: function onKeyDown(evt) {
          if (evt.which === 27) {
            _this2.handleClose();

            _onKeyDown(evt);
          }
        }
      }, React.createElement(Button, {
        className: buttonTriggerClassName,
        disabled: disabled,
        kind: triggerButtonKind,
        renderIcon: renderTriggerButtonIcon,
        iconDescription: triggerButtonIconDescription,
        onClick: this.handleOpen,
        ref: this.triggerButton
      }, buttonTriggerText), React.createElement(Modal, props, children));
    }
  }]);

  return ModalWrapper;
}(React.Component);

_defineProperty(ModalWrapper, "propTypes", {
  status: PropTypes.string,
  handleOpen: PropTypes.func,
  children: PropTypes.node,
  id: PropTypes.string,
  buttonTriggerText: PropTypes.node,
  buttonTriggerClassName: PropTypes.string,
  modalLabel: PropTypes.string,
  modalHeading: PropTypes.string,
  modalText: PropTypes.string,
  passiveModal: PropTypes.bool,
  withHeader: PropTypes.bool,
  modalBeforeContent: PropTypes.bool,
  primaryButtonText: PropTypes.string,
  secondaryButtonText: PropTypes.string,
  handleSubmit: PropTypes.func,
  disabled: PropTypes.bool,
  renderTriggerButtonIcon: PropTypes.oneOfType([PropTypes.func, PropTypes.object]),
  triggerButtonIconDescription: PropTypes.string,
  triggerButtonKind: PropTypes.oneOf(ButtonKinds),
  shouldCloseAfterSubmit: PropTypes.bool
});

_defineProperty(ModalWrapper, "defaultProps", {
  primaryButtonText: 'Save',
  secondaryButtonText: 'Cancel',
  triggerButtonIconDescription: 'Provide icon description if icon is used',
  triggerButtonKind: 'primary',
  disabled: false,
  selectorPrimaryFocus: '[data-modal-primary-focus]',
  onKeyDown: function onKeyDown() {}
});

export { ModalWrapper as default };