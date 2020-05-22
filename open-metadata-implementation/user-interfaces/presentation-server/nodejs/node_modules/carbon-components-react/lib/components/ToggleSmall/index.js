"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
var _exportNames = {};
Object.defineProperty(exports, "default", {
  enumerable: true,
  get: function get() {
    return _ToggleSmall2.default;
  }
});

var _ToggleSmall = require("./ToggleSmall.Skeleton");

Object.keys(_ToggleSmall).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (Object.prototype.hasOwnProperty.call(_exportNames, key)) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function get() {
      return _ToggleSmall[key];
    }
  });
});

var _ToggleSmall2 = _interopRequireDefault(require("./ToggleSmall"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }