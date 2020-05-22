function _slicedToArray(arr, i) { return _arrayWithHoles(arr) || _iterableToArrayLimit(arr, i) || _nonIterableRest(); }

function _nonIterableRest() { throw new TypeError("Invalid attempt to destructure non-iterable instance"); }

function _iterableToArrayLimit(arr, i) { if (!(Symbol.iterator in Object(arr) || Object.prototype.toString.call(arr) === "[object Arguments]")) { return; } var _arr = []; var _n = true; var _d = false; var _e = undefined; try { for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"] != null) _i["return"](); } finally { if (_d) throw _e; } } return _arr; }

function _arrayWithHoles(arr) { if (Array.isArray(arr)) return arr; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import PropTypes from 'prop-types';
import React, { useState, useCallback } from 'react';

var HeaderContainer = function HeaderContainer(_ref) {
  var isSideNavExpanded = _ref.isSideNavExpanded,
      Children = _ref.render;

  //state for expandable sidenav
  var _useState = useState(isSideNavExpanded),
      _useState2 = _slicedToArray(_useState, 2),
      isSideNavExpandedState = _useState2[0],
      setIsSideNavExpandedState = _useState2[1];

  var handleHeaderMenuButtonClick = useCallback(function () {
    setIsSideNavExpandedState(!isSideNavExpandedState);
  }, [isSideNavExpandedState, setIsSideNavExpandedState]);
  return React.createElement(Children, {
    isSideNavExpanded: isSideNavExpandedState,
    onClickSideNavExpand: handleHeaderMenuButtonClick
  });
};

HeaderContainer.propTypes = {
  /**
   * Optionally provide a custom class name that is applied to the underlying <header>
   */
  isSideNavExpanded: PropTypes.bool
};
HeaderContainer.defaultProps = {
  isSideNavExpanded: false
};
export default HeaderContainer;