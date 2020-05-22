"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _react = _interopRequireDefault(require("react"));

var _propTypes = _interopRequireDefault(require("prop-types"));

var _lodash = _interopRequireDefault(require("lodash.isequal"));

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

var Selection =
/*#__PURE__*/
function (_React$Component) {
  _inherits(Selection, _React$Component);

  function Selection(props) {
    var _this;

    _classCallCheck(this, Selection);

    _this = _possibleConstructorReturn(this, _getPrototypeOf(Selection).call(this, props));

    _defineProperty(_assertThisInitialized(_this), "internalSetState", function (stateToSet, callback) {
      return _this.setState(stateToSet, function () {
        if (callback) {
          callback();
        }

        if (_this.props.onChange) {
          _this.props.onChange(_this.state);
        }
      });
    });

    _defineProperty(_assertThisInitialized(_this), "handleClearSelection", function () {
      if (_this.props.disabled) {
        return;
      }

      _this.internalSetState({
        selectedItems: []
      });
    });

    _defineProperty(_assertThisInitialized(_this), "handleSelectItem", function (item) {
      _this.internalSetState(function (state) {
        return {
          selectedItems: state.selectedItems.concat(item)
        };
      });
    });

    _defineProperty(_assertThisInitialized(_this), "handleRemoveItem", function (index) {
      _this.internalSetState(function (state) {
        return {
          selectedItems: removeAtIndex(state.selectedItems, index)
        };
      });
    });

    _defineProperty(_assertThisInitialized(_this), "handleOnItemChange", function (item) {
      if (_this.props.disabled) {
        return;
      }

      var selectedItems = _this.state.selectedItems;
      var selectedIndex;
      selectedItems.forEach(function (selectedItem, index) {
        if ((0, _lodash.default)(selectedItem, item)) {
          selectedIndex = index;
        }
      });

      if (selectedIndex === undefined) {
        _this.handleSelectItem(item);

        return;
      }

      _this.handleRemoveItem(selectedIndex);
    });

    _this.state = {
      selectedItems: props.initialSelectedItems
    };
    return _this;
  }

  _createClass(Selection, [{
    key: "render",
    value: function render() {
      var _this$props = this.props,
          children = _this$props.children,
          render = _this$props.render;
      var selectedItems = this.state.selectedItems;
      var renderProps = {
        selectedItems: selectedItems,
        onItemChange: this.handleOnItemChange,
        clearSelection: this.handleClearSelection
      };

      if (render !== undefined) {
        return render(renderProps);
      }

      if (children !== undefined) {
        return children(renderProps);
      }

      return null;
    }
  }]);

  return Selection;
}(_react.default.Component); // Generic utility for safely removing an element at a given index from an
// array.


exports.default = Selection;

_defineProperty(Selection, "propTypes", {
  initialSelectedItems: _propTypes.default.array.isRequired
});

_defineProperty(Selection, "defaultProps", {
  initialSelectedItems: []
});

var removeAtIndex = function removeAtIndex(array, index) {
  var result = array.slice();
  result.splice(index, 1);
  return result;
};