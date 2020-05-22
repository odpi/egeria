"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
Object.defineProperty(exports, "Breadcrumb", {
  enumerable: true,
  get: function get() {
    return _Breadcrumb.default;
  }
});
Object.defineProperty(exports, "BreadcrumbItem", {
  enumerable: true,
  get: function get() {
    return _BreadcrumbItem.default;
  }
});
Object.defineProperty(exports, "BreadcrumbSkeleton", {
  enumerable: true,
  get: function get() {
    return _Breadcrumb2.default;
  }
});
exports.default = void 0;

var _Breadcrumb = _interopRequireDefault(require("./Breadcrumb"));

var _BreadcrumbItem = _interopRequireDefault(require("./BreadcrumbItem"));

var _Breadcrumb2 = _interopRequireDefault(require("./Breadcrumb.Skeleton"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
// Maintain default export as Breadcrumb for backwards-compatability
var _default = _Breadcrumb.default;
exports.default = _default;