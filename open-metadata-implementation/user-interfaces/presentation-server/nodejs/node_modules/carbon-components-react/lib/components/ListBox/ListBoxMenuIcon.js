"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = exports.translationIds = void 0;

var _classnames = _interopRequireDefault(require("classnames"));

var _react = _interopRequireDefault(require("react"));

var _propTypes = _interopRequireDefault(require("prop-types"));

var _iconsReact = require("@carbon/icons-react");

var _carbonComponents = require("carbon-components");

var _defaultTranslations;

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

var prefix = _carbonComponents.settings.prefix;
var translationIds = {
  'close.menu': 'close.menu',
  'open.menu': 'open.menu'
};
exports.translationIds = translationIds;
var defaultTranslations = (_defaultTranslations = {}, _defineProperty(_defaultTranslations, translationIds['close.menu'], 'Close menu'), _defineProperty(_defaultTranslations, translationIds['open.menu'], 'Open menu'), _defaultTranslations);
/**
 * `ListBoxMenuIcon` is used to orient the icon up or down depending on the
 * state of the menu for a given `ListBox`
 */

var ListBoxMenuIcon = function ListBoxMenuIcon(_ref) {
  var isOpen = _ref.isOpen,
      t = _ref.translateWithId;
  var className = (0, _classnames.default)("".concat(prefix, "--list-box__menu-icon"), _defineProperty({}, "".concat(prefix, "--list-box__menu-icon--open"), isOpen));
  var description = isOpen ? t('close.menu') : t('open.menu');
  return _react.default.createElement("div", {
    className: className
  }, _react.default.createElement(_iconsReact.ChevronDown16, {
    name: "chevron--down",
    "aria-label": description
  }, _react.default.createElement("title", null, description)));
};

ListBoxMenuIcon.propTypes = {
  /**
   * Specify whether the menu is currently open, which will influence the
   * direction of the menu icon
   */
  isOpen: _propTypes.default.bool.isRequired,

  /**
   * i18n hook used to provide the appropriate description for the given menu
   * icon. This function takes in an id defined in `translationIds` and should
   * return a string message for that given message id.
   */
  translateWithId: _propTypes.default.func.isRequired
};
ListBoxMenuIcon.defaultProps = {
  translateWithId: function translateWithId(id) {
    return defaultTranslations[id];
  }
};
var _default = ListBoxMenuIcon;
exports.default = _default;