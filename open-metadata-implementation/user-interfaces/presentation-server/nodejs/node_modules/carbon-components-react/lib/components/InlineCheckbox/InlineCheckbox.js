"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _propTypes = _interopRequireDefault(require("prop-types"));

var _react = _interopRequireDefault(require("react"));

var _carbonComponents = require("carbon-components");

var _mergeRefs = _interopRequireDefault(require("../../tools/mergeRefs"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

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

var prefix = _carbonComponents.settings.prefix;

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
        ref: (0, _mergeRefs.default)(ref, this.handleRef),
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

      return _react.default.createElement(_react.default.Fragment, null, _react.default.createElement("input", inputProps),
      /* eslint-disable jsx-a11y/label-has-for,jsx-a11y/label-has-associated-control */
      _react.default.createElement("label", {
        htmlFor: id,
        className: "".concat(prefix, "--checkbox-label"),
        "aria-label": ariaLabel,
        title: title
      }));
    }
  }]);

  return InlineCheckbox;
}(_react.default.Component);

_defineProperty(InlineCheckbox, "propTypes", {
  /**
   * Specify the label for the control
   */
  ariaLabel: _propTypes.default.string.isRequired,

  /**
   * Specify whether the underlying control is checked, or not
   */
  checked: _propTypes.default.bool.isRequired,

  /**
   * Specify whether the underlying input control should be disabled
   */
  disabled: _propTypes.default.bool,

  /**
   * Provide an `id` for the underlying input control
   */
  id: _propTypes.default.string.isRequired,

  /**
   * Specify whether the control is in an indterminate state
   */
  indeterminate: _propTypes.default.bool,

  /**
   * Provide a `name` for the underlying input control
   */
  name: _propTypes.default.string.isRequired,

  /**
   * Provide a handler that is invoked when a user clicks on the control
   */
  onClick: _propTypes.default.func,

  /**
   * Provide a handler that is invoked on the key down event for the control
   */
  onKeyDown: _propTypes.default.func,

  /**
   * Provide an optional tooltip for the InlineCheckbox
   */
  title: _propTypes.default.string,

  /**
   * Provide an optional hook that is called each time the input is updated
   */
  onChange: _propTypes.default.func
});

_defineProperty(InlineCheckbox, "defaultProps", {
  ariaLabel: '',
  checked: false,
  id: 'inline-checkbox',
  name: '',
  onChange: function onChange() {}
});

var _default = function () {
  var forwardRef = function forwardRef(props, ref) {
    return _react.default.createElement(InlineCheckbox, _extends({}, props, {
      innerRef: ref
    }));
  };

  forwardRef.displayName = 'InlineCheckbox';
  return _react.default.forwardRef(forwardRef);
}();

exports.default = _default;