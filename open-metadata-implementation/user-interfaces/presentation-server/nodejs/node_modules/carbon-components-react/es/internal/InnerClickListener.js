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
/**
 * Generic component used for reacting to a click event happening outside of a
 * given child component that used the forwarded `handleRef` function through
 * the `refKey` prop.
 */

var InnerClickListener =
/*#__PURE__*/
function (_React$Component) {
  _inherits(InnerClickListener, _React$Component);

  function InnerClickListener(props) {
    var _this;

    _classCallCheck(this, InnerClickListener);

    _this = _possibleConstructorReturn(this, _getPrototypeOf(InnerClickListener).call(this, props)); // We manually bind handlers in this Component, versus using class
    // properties, so that we can properly test the `handleRef` handler with
    // enzyme.

    _this.handleRef = _this.handleRef.bind(_assertThisInitialized(_this));
    _this.handleDocumentClick = _this.handleDocumentClick.bind(_assertThisInitialized(_this));
    return _this;
  }

  _createClass(InnerClickListener, [{
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
    value: function handleDocumentClick(event) {
      // Ensure that the target exists in the DOM before checking the element
      if (this.element && (this.element.ownerDocument === event.target || this.element.ownerDocument.body.contains(event.target))) {
        if (this.element.contains && !this.element.contains(event.target)) {
          this.props.onClickOutside(event);
        }
      }
    }
  }, {
    key: "handleRef",
    value: function handleRef(el) {
      this.element = el;
    }
  }, {
    key: "render",
    value: function render() {
      var _this$props = this.props,
          refKey = _this$props.refKey,
          children = _this$props.children;
      return React.cloneElement(children, _defineProperty({}, refKey, this.handleRef));
    }
  }]);

  return InnerClickListener;
}(React.Component);

_defineProperty(InnerClickListener, "propTypes", {
  children: PropTypes.node.isRequired,
  refKey: PropTypes.string.isRequired,
  onClickOutside: PropTypes.func.isRequired
});

export { InnerClickListener as default };