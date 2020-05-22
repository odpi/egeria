"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _propTypes = _interopRequireDefault(require("prop-types"));

var _react = _interopRequireDefault(require("react"));

var _RadioTile = _interopRequireDefault(require("../RadioTile"));

var _warning = _interopRequireDefault(require("warning"));

var _carbonComponents = require("carbon-components");

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

var TileGroup =
/*#__PURE__*/
function (_React$Component) {
  _inherits(TileGroup, _React$Component);

  function TileGroup() {
    var _getPrototypeOf2;

    var _this;

    _classCallCheck(this, TileGroup);

    for (var _len = arguments.length, args = new Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }

    _this = _possibleConstructorReturn(this, (_getPrototypeOf2 = _getPrototypeOf(TileGroup)).call.apply(_getPrototypeOf2, [this].concat(args)));

    _defineProperty(_assertThisInitialized(_this), "state", {
      selected: _this.props.valueSelected || _this.props.defaultSelected || null,
      prevValueSelected: _this.props.valueSelected
    });

    _defineProperty(_assertThisInitialized(_this), "getRadioTiles", function () {
      var childrenArray = _react.default.Children.toArray(_this.props.children);

      var children = childrenArray.map(function (tileRadio) {
        var _tileRadio$props = tileRadio.props,
            value = _tileRadio$props.value,
            other = _objectWithoutProperties(_tileRadio$props, ["value"]);
        /* istanbul ignore if */


        if (typeof tileRadio.props.checked !== 'undefined') {
          process.env.NODE_ENV !== "production" ? (0, _warning.default)(false, "Instead of using the checked property on the RadioTile, set\n            the defaultSelected property or valueSelected property on the TileGroup.") : void 0;
        }

        return _react.default.createElement(_RadioTile.default, _extends({}, other, {
          name: _this.props.name,
          key: value,
          value: value,
          onChange: _this.handleChange,
          checked: value === _this.state.selected
        }));
      });
      return children;
    });

    _defineProperty(_assertThisInitialized(_this), "handleChange", function (newSelection, value, evt) {
      if (newSelection !== _this.state.selected) {
        _this.setState({
          selected: newSelection
        });

        _this.props.onChange(newSelection, _this.props.name, evt);
      }
    });

    _defineProperty(_assertThisInitialized(_this), "renderLegend", function (legend) {
      if (legend) {
        return _react.default.createElement("legend", null, legend);
      }
    });

    return _this;
  }

  _createClass(TileGroup, [{
    key: "render",
    value: function render() {
      var _this$props = this.props,
          disabled = _this$props.disabled,
          _this$props$className = _this$props.className,
          className = _this$props$className === void 0 ? "".concat(prefix, "--tile-group") : _this$props$className,
          legend = _this$props.legend;
      return _react.default.createElement("fieldset", {
        className: className,
        disabled: disabled
      }, this.renderLegend(legend), _react.default.createElement("div", null, this.getRadioTiles()));
    }
  }], [{
    key: "getDerivedStateFromProps",
    value: function getDerivedStateFromProps(_ref, state) {
      var valueSelected = _ref.valueSelected,
          defaultSelected = _ref.defaultSelected;
      var prevValueSelected = state.prevValueSelected;
      return prevValueSelected === valueSelected ? null : {
        selected: valueSelected || defaultSelected || null,
        prevValueSelected: valueSelected
      };
    }
  }]);

  return TileGroup;
}(_react.default.Component);

exports.default = TileGroup;

_defineProperty(TileGroup, "propTypes", {
  /**
   * Provide a collection of <RadioTile> components to render in the group
   */
  children: _propTypes.default.node,

  /**
   * Provide an optional className to be applied to the container node
   */
  className: _propTypes.default.string,

  /**
   * Specify the the value of <RadioTile> to be selected by default
   */
  defaultSelected: _propTypes.default.oneOfType([_propTypes.default.string, _propTypes.default.number]),

  /**
   * Specify the name of the underlying <input> nodes
   */
  name: _propTypes.default.string.isRequired,

  /**
   * Specify whether the group is disabled
   */
  disabled: _propTypes.default.bool,

  /**
   * Provide an optional `onChange` hook that is called whenever the value of
   * the group changes
   */
  onChange: _propTypes.default.func,

  /**
   * Provide an optional legend for this group
   */
  legend: _propTypes.default.string,

  /**
   * Specify the value that is currently selected in the group
   */
  valueSelected: _propTypes.default.oneOfType([_propTypes.default.string, _propTypes.default.number])
});

_defineProperty(TileGroup, "defaultProps", {
  onChange:
  /* istanbul ignore next */
  function onChange() {}
});