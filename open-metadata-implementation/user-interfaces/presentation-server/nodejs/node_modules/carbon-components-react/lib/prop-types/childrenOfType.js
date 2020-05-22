"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _react = require("react");

var _createChainableTypeChecker = _interopRequireDefault(require("./tools/createChainableTypeChecker"));

var _getDisplayName = _interopRequireDefault(require("./tools/getDisplayName"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

/**
 * `childrenOfType` is used for asserting that children of a given React
 * component are only of a given type. Currently, this supports React elements,
 * Stateless Functional Components, and Class-based components.
 *
 * This prop validator also supports chaining through `isRequired`
 */
var childrenOfType = function childrenOfType(expectedChildType) {
  var expectedDisplayName = (0, _getDisplayName.default)( // Support both React elements and components by using `type` if it exists
  expectedChildType.type || expectedChildType);

  var validate = function validate(props, propName, componentName) {
    _react.Children.forEach(props[propName], function (child) {
      var childDisplayName = (0, _getDisplayName.default)(child.type);

      if (child.type !== expectedChildType.type && child.type !== expectedChildType) {
        throw new Error("Invalid prop `children` of type `".concat(childDisplayName, "` ") + "supplied to `".concat(componentName, "`, expected each child to be a ") + "`".concat(expectedDisplayName, "` component."));
      }
    });
  };

  return (0, _createChainableTypeChecker.default)(validate);
};

var _default = childrenOfType;
exports.default = _default;