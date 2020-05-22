"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _propTypes = _interopRequireDefault(require("prop-types"));

var _react = _interopRequireDefault(require("react"));

var _classnames = _interopRequireDefault(require("classnames"));

var _carbonComponents = require("carbon-components");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

function _objectWithoutProperties(source, excluded) { if (source == null) return {}; var target = _objectWithoutPropertiesLoose(source, excluded); var key, i; if (Object.getOwnPropertySymbols) { var sourceSymbolKeys = Object.getOwnPropertySymbols(source); for (i = 0; i < sourceSymbolKeys.length; i++) { key = sourceSymbolKeys[i]; if (excluded.indexOf(key) >= 0) continue; if (!Object.prototype.propertyIsEnumerable.call(source, key)) continue; target[key] = source[key]; } } return target; }

function _objectWithoutPropertiesLoose(source, excluded) { if (source == null) return {}; var target = {}; var sourceKeys = Object.keys(source); var key, i; for (i = 0; i < sourceKeys.length; i++) { key = sourceKeys[i]; if (excluded.indexOf(key) >= 0) continue; target[key] = source[key]; } return target; }

var prefix = _carbonComponents.settings.prefix;
var randoms = [0.973051493507435, 0.15334737213558558, 0.5671034553053769];

function getRandomInt(min, max, n) {
  return Math.floor(randoms[n % 3] * (max - min + 1)) + min;
}

var SkeletonText = function SkeletonText(_ref) {
  var _classNames;

  var paragraph = _ref.paragraph,
      lineCount = _ref.lineCount,
      width = _ref.width,
      heading = _ref.heading,
      className = _ref.className,
      other = _objectWithoutProperties(_ref, ["paragraph", "lineCount", "width", "heading", "className"]);

  var skeletonTextClasses = (0, _classnames.default)((_classNames = {}, _defineProperty(_classNames, "".concat(prefix, "--skeleton__text"), true), _defineProperty(_classNames, "".concat(prefix, "--skeleton__heading"), heading), _defineProperty(_classNames, className, className), _classNames));
  var widthNum = parseInt(width, 10);
  var widthPx = width.includes('px');
  var widthPercent = width.includes('%');

  if (widthPercent && paragraph) {
    var lines = [];

    for (var i = 0; i < lineCount; i++) {
      var randomWidth = getRandomInt(0, 75, i) + 'px';
      lines.push(_react.default.createElement("p", _extends({
        className: skeletonTextClasses,
        style: {
          width: "calc(".concat(width, " - ").concat(randomWidth, ")")
        },
        key: i
      }, other)));
    }

    return _react.default.createElement("div", null, lines);
  }

  if (widthPx && paragraph) {
    var _lines = [];

    for (var j = 0; j < lineCount; j++) {
      var _randomWidth = getRandomInt(widthNum - 75, widthNum, j) + 'px';

      _lines.push(_react.default.createElement("p", _extends({
        className: skeletonTextClasses,
        style: {
          width: _randomWidth
        },
        key: j
      }, other)));
    }

    return _react.default.createElement("div", null, _lines);
  }

  return _react.default.createElement("p", _extends({
    className: skeletonTextClasses,
    style: {
      width: width
    }
  }, other));
};

SkeletonText.propTypes = {
  /**
   * will generate multiple lines of text
   */
  paragraph: _propTypes.default.bool,

  /**
   * the number of lines in a paragraph
   */
  lineCount: _propTypes.default.number,

  /**
   * width (in px or %) of single line of text or max-width of paragraph lines
   */
  width: _propTypes.default.string,

  /**
   * generates skeleton text at a larger size
   */
  heading: _propTypes.default.bool,

  /**
   * Specify an optional className to be applied to the container node
   */
  className: _propTypes.default.string
};
SkeletonText.defaultProps = {
  paragraph: false,
  width: '100%',
  heading: false,
  lineCount: 3
};
var _default = SkeletonText;
exports.default = _default;