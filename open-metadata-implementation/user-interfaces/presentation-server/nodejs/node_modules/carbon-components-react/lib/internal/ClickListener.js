"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _propTypes = _interopRequireDefault(require("prop-types"));

var _react = _interopRequireDefault(require("react"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

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
 * Generic component used for reacting to a click event happening outside of a
 * given `children` element.
 */
var ClickListener =
/*#__PURE__*/
function (_React$Component) {
  _inherits(ClickListener, _React$Component);

  function ClickListener(props) {
    var _this;

    _classCallCheck(this, ClickListener);

    _this = _possibleConstructorReturn(this, _getPrototypeOf(ClickListener).call(this, props)); // We manually bind handlers in this Component, versus using class
    // properties, so that we can properly test the `handleRef` handler with
    // enzyme.

    _this.handleRef = _this.handleRef.bind(_assertThisInitialized(_this));
    _this.handleDocumentClick = _this.handleDocumentClick.bind(_assertThisInitialized(_this));
    return _this;
  }

  _createClass(ClickListener, [{
    key: "componentDidMount",
    value: function componentDidMount() {
      document.addEventListener('click', this.handleDocumentClick);
    }
  }, {
    key: "componentWillUnmount",
    value: function componentWillUnmount() {
      document.removeEventListener('click', this.handleDocumentClick);
    }
  }, {
    key: "handleDocumentClick",
    value: function handleDocumentClick(evt) {
      if (this.element) {
        if (this.element.contains && !this.element.contains(evt.target)) {
          this.props.onClickOutside(evt);
        }
      }
    }
  }, {
    key: "handleRef",
    value: function handleRef(el) {
      var children = this.props.children;
      this.element = el;
      /**
       * One important note, `children.ref` corresponds to a `ref` prop passed in
       * directly to the child, not necessarily a `ref` defined in the component.
       * This means that here we target the following `ref` location:
       *
       * <ClickListener onClickOutside={() => {}}>
       *   <Child ref={targetedRefHere} />
       * </ClickListener>
       */

      if (children.ref && typeof children.ref === 'function') {
        children.ref(el);
      }
    }
  }, {
    key: "render",
    value: function render() {
      return _react.default.cloneElement(this.props.children, {
        ref: this.handleRef
      });
    }
  }]);

  return ClickListener;
}(_react.default.Component);

exports.default = ClickListener;

_defineProperty(ClickListener, "propTypes", {
  children: _propTypes.default.element.isRequired,
  onClickOutside: _propTypes.default.func.isRequired
});