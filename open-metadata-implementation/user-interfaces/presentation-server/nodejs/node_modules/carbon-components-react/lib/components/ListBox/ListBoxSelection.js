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

var _keyboard = require("../../internal/keyboard");

var _defaultTranslations;

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

var prefix = _carbonComponents.settings.prefix;
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
  var className = (0, _classnames.default)("".concat(prefix, "--list-box__selection"), (_cx = {}, _defineProperty(_cx, "".concat(prefix, "--tag--filter"), selectionCount), _defineProperty(_cx, "".concat(prefix, "--list-box__selection--multi"), selectionCount), _cx));

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


    if ((0, _keyboard.match)(event, _keyboard.keys.Enter)) {
      clearSelection(event);
    }
  };

  var description = selectionCount ? t('clear.all') : t('clear.selection');
  return _react.default.createElement("div", {
    role: "button",
    className: className,
    tabIndex: disabled ? -1 : 0,
    onClick: handleOnClick,
    onKeyDown: handleOnKeyDown,
    "aria-label": "Clear Selection",
    title: description
  }, selectionCount, _react.default.createElement(_iconsReact.Close16, null));
};

var translationIds = {
  'clear.all': 'clear.all',
  'clear.selection': 'clear.selection'
};
exports.translationIds = translationIds;
var defaultTranslations = (_defaultTranslations = {}, _defineProperty(_defaultTranslations, translationIds['clear.all'], 'Clear all selected items'), _defineProperty(_defaultTranslations, translationIds['clear.selection'], 'Clear selected item'), _defaultTranslations);
ListBoxSelection.propTypes = {
  /**
   * Specify a function to be invoked when a user interacts with the clear
   * selection element.
   */
  clearSelection: _propTypes.default.func.isRequired,

  /**
   * Specify an optional `selectionCount` value that will be used to determine
   * whether the selection should display a badge or a single clear icon.
   */
  selectionCount: _propTypes.default.number,

  /**
   * i18n hook used to provide the appropriate description for the given menu
   * icon. This function takes in an id defined in `translationIds` and should
   * return a string message for that given message id.
   */
  translateWithId: _propTypes.default.func.isRequired
};
ListBoxSelection.defaultProps = {
  translateWithId: function translateWithId(id) {
    return defaultTranslations[id];
  }
};
var _default = ListBoxSelection;
exports.default = _default;