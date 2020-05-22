function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

function _objectWithoutProperties(source, excluded) { if (source == null) return {}; var target = _objectWithoutPropertiesLoose(source, excluded); var key, i; if (Object.getOwnPropertySymbols) { var sourceSymbolKeys = Object.getOwnPropertySymbols(source); for (i = 0; i < sourceSymbolKeys.length; i++) { key = sourceSymbolKeys[i]; if (excluded.indexOf(key) >= 0) continue; if (!Object.prototype.propertyIsEnumerable.call(source, key)) continue; target[key] = source[key]; } } return target; }

function _objectWithoutPropertiesLoose(source, excluded) { if (source == null) return {}; var target = {}; var sourceKeys = Object.keys(source); var key, i; for (i = 0; i < sourceKeys.length; i++) { key = sourceKeys[i]; if (excluded.indexOf(key) >= 0) continue; target[key] = source[key]; } return target; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import PropTypes from 'prop-types';
import React from 'react';
import classNames from 'classnames';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
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

  var skeletonTextClasses = classNames((_classNames = {}, _defineProperty(_classNames, "".concat(prefix, "--skeleton__text"), true), _defineProperty(_classNames, "".concat(prefix, "--skeleton__heading"), heading), _defineProperty(_classNames, className, className), _classNames));
  var widthNum = parseInt(width, 10);
  var widthPx = width.includes('px');
  var widthPercent = width.includes('%');

  if (widthPercent && paragraph) {
    var lines = [];

    for (var i = 0; i < lineCount; i++) {
      var randomWidth = getRandomInt(0, 75, i) + 'px';
      lines.push(React.createElement("p", _extends({
        className: skeletonTextClasses,
        style: {
          width: "calc(".concat(width, " - ").concat(randomWidth, ")")
        },
        key: i
      }, other)));
    }

    return React.createElement("div", null, lines);
  }

  if (widthPx && paragraph) {
    var _lines = [];

    for (var j = 0; j < lineCount; j++) {
      var _randomWidth = getRandomInt(widthNum - 75, widthNum, j) + 'px';

      _lines.push(React.createElement("p", _extends({
        className: skeletonTextClasses,
        style: {
          width: _randomWidth
        },
        key: j
      }, other)));
    }

    return React.createElement("div", null, _lines);
  }

  return React.createElement("p", _extends({
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
  paragraph: PropTypes.bool,

  /**
   * the number of lines in a paragraph
   */
  lineCount: PropTypes.number,

  /**
   * width (in px or %) of single line of text or max-width of paragraph lines
   */
  width: PropTypes.string,

  /**
   * generates skeleton text at a larger size
   */
  heading: PropTypes.bool,

  /**
   * Specify an optional className to be applied to the container node
   */
  className: PropTypes.string
};
SkeletonText.defaultProps = {
  paragraph: false,
  width: '100%',
  heading: false,
  lineCount: 3
};
export default SkeletonText;