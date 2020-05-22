function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

function _objectWithoutProperties(source, excluded) { if (source == null) return {}; var target = _objectWithoutPropertiesLoose(source, excluded); var key, i; if (Object.getOwnPropertySymbols) { var sourceSymbolKeys = Object.getOwnPropertySymbols(source); for (i = 0; i < sourceSymbolKeys.length; i++) { key = sourceSymbolKeys[i]; if (excluded.indexOf(key) >= 0) continue; if (!Object.prototype.propertyIsEnumerable.call(source, key)) continue; target[key] = source[key]; } } return target; }

function _objectWithoutPropertiesLoose(source, excluded) { if (source == null) return {}; var target = {}; var sourceKeys = Object.keys(source); var key, i; for (i = 0; i < sourceKeys.length; i++) { key = sourceKeys[i]; if (excluded.indexOf(key) >= 0) continue; target[key] = source[key]; } return target; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import { settings } from 'carbon-components';
import PropTypes from 'prop-types';
import ListBoxMenuItem from './ListBoxMenuItem';
import childrenOfType from '../../prop-types/childrenOfType';
var prefix = settings.prefix;
/**
 * `ListBoxMenu` is a simple container node that isolates the `list-box__menu`
 * class into a single component. It is also being used to validate given
 * `children` components.
 */

var ListBoxMenu = function ListBoxMenu(_ref) {
  var children = _ref.children,
      id = _ref.id,
      rest = _objectWithoutProperties(_ref, ["children", "id"]);

  return React.createElement("div", _extends({
    id: "".concat(id, "__menu"),
    className: "".concat(prefix, "--list-box__menu"),
    role: "listbox"
  }, rest), children);
};

ListBoxMenu.propTypes = {
  /**
   * Provide the contents of your ListBoxMenu
   */
  children: childrenOfType(ListBoxMenuItem),

  /**
   * Specify a custom `id`
   */
  id: PropTypes.string.isRequired
};
export default ListBoxMenu;