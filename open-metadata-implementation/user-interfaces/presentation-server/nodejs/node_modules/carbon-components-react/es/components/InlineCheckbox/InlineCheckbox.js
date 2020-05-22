function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

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
import { settings } from 'carbon-components';
import mergeRefs from '../../tools/mergeRefs';
var prefix = settings.prefix;

var InlineCheckbox =
/*#__PURE__*/
function (_React$Component) {
  _inherits(InlineCheckbox, _React$Component);

  function InlineCheckbox() {
    var _getPrototypeOf2;

    var _this;

    _classCallCheck(this, InlineCheckbox);

    for (var _len = arguments.length, args = new Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }

    _this = _possibleConstructorReturn(this, (_getPrototypeOf2 = _getPrototypeOf(InlineCheckbox)).call.apply(_getPrototypeOf2, [this].concat(args)));

    _defineProperty(_assertThisInitialized(_this), "handleRef", function (el) {
      _this.inputNode = el;
    });

    return _this;
  }

  _createClass(InlineCheckbox, [{
    key: "componentDidMount",
    value: function componentDidMount() {
      this.inputNode.indeterminate = this.props.indeterminate;
    }
  }, {
    key: "componentDidUpdate",
    value: function componentDidUpdate(prevProps) {
      if (prevProps.indeterminate !== this.props.indeterminate) {
        this.inputNode.indeterminate = this.props.indeterminate;
      }
    }
  }, {
    key: "render",
    value: function render() {
      var _this$props = this.props,
          id = _this$props.id,
          indeterminate = _this$props.indeterminate,
          checked = _this$props.checked,
          disabled = _this$props.disabled,
          ariaLabel = _this$props.ariaLabel,
          name = _this$props.name,
          _onChange = _this$props.onChange,
          onClick = _this$props.onClick,
          onKeyDown = _this$props.onKeyDown,
          _this$props$title = _this$props.title,
          title = _this$props$title === void 0 ? undefined : _this$props$title,
          ref = _this$props.innerRef;
      var inputProps = {
        id: id,
        name: name,
        onClick: onClick,
        onChange: function onChange(evt) {
          _onChange(evt.target.checked, id, evt);
        },
        onKeyDown: onKeyDown,
        className: "".concat(prefix, "--checkbox"),
        type: 'checkbox',
        ref: mergeRefs(ref, this.handleRef),
        checked: false,
        disabled: disabled
      };

      if (checked) {
        inputProps.checked = true;
      }

      if (indeterminate) {
        inputProps.checked = false;
        inputProps['aria-checked'] = 'mixed';
      }

      return React.createElement(React.Fragment, null, React.createElement("input", inputProps),
      /* eslint-disable jsx-a11y/label-has-for,jsx-a11y/label-has-associated-control */
      React.createElement("label", {
        htmlFor: id,
        className: "".concat(prefix, "--checkbox-label"),
        "aria-label": ariaLabel,
        title: title
      }));
    }
  }]);

  return InlineCheckbox;
}(React.Component);

_defineProperty(InlineCheckbox, "propTypes", {
  /**
   * Specify the label for the control
   */
  ariaLabel: PropTypes.string.isRequired,

  /**
   * Specify whether the underlying control is checked, or not
   */
  checked: PropTypes.bool.isRequired,

  /**
   * Specify whether the underlying input control should be disabled
   */
  disabled: PropTypes.bool,

  /**
   * Provide an `id` for the underlying input control
   */
  id: PropTypes.string.isRequired,

  /**
   * Specify whether the control is in an indterminate state
   */
  indeterminate: PropTypes.bool,

  /**
   * Provide a `name` for the underlying input control
   */
  name: PropTypes.string.isRequired,

  /**
   * Provide a handler that is invoked when a user clicks on the control
   */
  onClick: PropTypes.func,

  /**
   * Provide a handler that is invoked on the key down event for the control
   */
  onKeyDown: PropTypes.func,

  /**
   * Provide an optional tooltip for the InlineCheckbox
   */
  title: PropTypes.string,

  /**
   * Provide an optional hook that is called each time the input is updated
   */
  onChange: PropTypes.func
});

_defineProperty(InlineCheckbox, "defaultProps", {
  ariaLabel: '',
  checked: false,
  id: 'inline-checkbox',
  name: '',
  onChange: function onChange() {}
});

export default (function () {
  var forwardRef = function forwardRef(props, ref) {
    return React.createElement(InlineCheckbox, _extends({}, props, {
      innerRef: ref
    }));
  };

  forwardRef.displayName = 'InlineCheckbox';
  return React.forwardRef(forwardRef);
})();