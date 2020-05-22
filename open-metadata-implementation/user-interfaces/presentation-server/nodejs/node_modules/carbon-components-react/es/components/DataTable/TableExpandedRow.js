function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

function _objectWithoutProperties(source, excluded) { if (source == null) return {}; var target = _objectWithoutPropertiesLoose(source, excluded); var key, i; if (Object.getOwnPropertySymbols) { var sourceSymbolKeys = Object.getOwnPropertySymbols(source); for (i = 0; i < sourceSymbolKeys.length; i++) { key = sourceSymbolKeys[i]; if (excluded.indexOf(key) >= 0) continue; if (!Object.prototype.propertyIsEnumerable.call(source, key)) continue; target[key] = source[key]; } } return target; }

function _objectWithoutPropertiesLoose(source, excluded) { if (source == null) return {}; var target = {}; var sourceKeys = Object.keys(source); var key, i; for (i = 0; i < sourceKeys.length; i++) { key = sourceKeys[i]; if (excluded.indexOf(key) >= 0) continue; target[key] = source[key]; } return target; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import cx from 'classnames';
import PropTypes from 'prop-types';
import React, { useRef } from 'react';
import { settings } from 'carbon-components';
import TableCell from './TableCell';
var prefix = settings.prefix;

var TableExpandedRow = function TableExpandedRow(_ref) {
  var customClassName = _ref.className,
      children = _ref.children,
      colSpan = _ref.colSpan,
      rest = _objectWithoutProperties(_ref, ["className", "children", "colSpan"]);

  var rowRef = useRef(null);
  var className = cx("".concat(prefix, "--expandable-row"), customClassName);

  var toggleParentHoverClass = function toggleParentHoverClass(eventType) {
    if (rowRef && rowRef.current && rowRef.current.previousElementSibling) {
      var parentNode = rowRef.current.previousElementSibling;

      if (eventType === 'enter') {
        parentNode.classList.add("".concat(prefix, "--expandable-row--hover"));
      } else {
        parentNode.classList.remove("".concat(prefix, "--expandable-row--hover"));
      }
    }
  };

  return React.createElement("tr", _extends({
    ref: rowRef,
    onMouseEnter: function onMouseEnter() {
      return toggleParentHoverClass('enter');
    },
    onMouseLeave: function onMouseLeave() {
      return toggleParentHoverClass('leave');
    }
  }, rest, {
    className: className,
    "data-child-row": true
  }), React.createElement(TableCell, {
    colSpan: colSpan
  }, React.createElement("div", {
    className: "".concat(prefix, "--child-row-inner-container")
  }, children)));
};

TableExpandedRow.propTypes = {
  /**
   * Pass in the contents for your TableExpandedRow
   */
  children: PropTypes.node,

  /**
   * Specify an optional className to be applied to the container node
   */
  className: PropTypes.string,

  /**
   * The width of the expanded row's internal cell
   */
  colSpan: PropTypes.number.isRequired
};
export default TableExpandedRow;