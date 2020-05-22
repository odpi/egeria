var _defaultTranslations;

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import cx from 'classnames';
import React from 'react';
import PropTypes from 'prop-types';
import { Close16 } from '@carbon/icons-react';
import { settings } from 'carbon-components';
import { match, keys } from '../../internal/keyboard';
var prefix = settings.prefix;
/**
 * `ListBoxSelection` is used to provide controls for clearing a selection, in
 * addition to conditionally rendering a badge if the control has more than one
 * selection.
 */

var ListBoxSelection = function ListBoxSelection(_ref) {
  var _cx;

  var clearSelection = _ref.clearSelection,
      selectionCount = _ref.selectionCount,
      t = _ref.translateWithId,
      disabled = _ref.disabled;
  var className = cx("".concat(prefix, "--list-box__selection"), (_cx = {}, _defineProperty(_cx, "".concat(prefix, "--tag--filter"), selectionCount), _defineProperty(_cx, "".concat(prefix, "--list-box__selection--multi"), selectionCount), _cx));

  var handleOnClick = function handleOnClick(event) {
    event.stopPropagation();

    if (disabled) {
      return;
    }

    clearSelection(event);
  };

  var handleOnKeyDown = function handleOnKeyDown(event) {
    event.stopPropagation();

    if (disabled) {
      return;
    } // When a user hits ENTER, we'll clear the selection


    if (match(event, keys.Enter)) {
      clearSelection(event);
    }
  };

  var description = selectionCount ? t('clear.all') : t('clear.selection');
  return React.createElement("div", {
    role: "button",
    className: className,
    tabIndex: disabled ? -1 : 0,
    onClick: handleOnClick,
    onKeyDown: handleOnKeyDown,
    "aria-label": "Clear Selection",
    title: description
  }, selectionCount, React.createElement(Close16, null));
};

export var translationIds = {
  'clear.all': 'clear.all',
  'clear.selection': 'clear.selection'
};
var defaultTranslations = (_defaultTranslations = {}, _defineProperty(_defaultTranslations, translationIds['clear.all'], 'Clear all selected items'), _defineProperty(_defaultTranslations, translationIds['clear.selection'], 'Clear selected item'), _defaultTranslations);
ListBoxSelection.propTypes = {
  /**
   * Specify a function to be invoked when a user interacts with the clear
   * selection element.
   */
  clearSelection: PropTypes.func.isRequired,

  /**
   * Specify an optional `selectionCount` value that will be used to determine
   * whether the selection should display a badge or a single clear icon.
   */
  selectionCount: PropTypes.number,

  /**
   * i18n hook used to provide the appropriate description for the given menu
   * icon. This function takes in an id defined in `translationIds` and should
   * return a string message for that given message id.
   */
  translateWithId: PropTypes.func.isRequired
};
ListBoxSelection.defaultProps = {
  translateWithId: function translateWithId(id) {
    return defaultTranslations[id];
  }
};
export default ListBoxSelection;