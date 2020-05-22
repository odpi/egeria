"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.defaultItemToString = void 0;

var _invariant = _interopRequireDefault(require("invariant"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

var itemToString = function itemToString(item) {
  !(typeof item.label === 'string') ? process.env.NODE_ENV !== "production" ? (0, _invariant.default)(false, '[MultiSelect] the default `itemToString` method expected to receive ' + 'an item with a `label` field of type `string`. Instead received: `%s`', _typeof(item.label)) : invariant(false) : void 0;
  return item.label || '';
};

var defaultItemToString = function defaultItemToString(item) {
  if (Array.isArray(item)) {
    return item.map(itemToString);
  }

  return itemToString(item);
};

exports.defaultItemToString = defaultItemToString;