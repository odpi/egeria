"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.ErrorBoundaryContext = void 0;

var _react = require("react");

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
var ErrorBoundaryContext = (0, _react.createContext)({
  log: function log(error, info) {
    console.log(info.componentStack);
  }
});
exports.ErrorBoundaryContext = ErrorBoundaryContext;