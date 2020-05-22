"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

/**
 * `createChainableTypeChecker` is used inside of our custom prop validators to
 * add in chaining `isRequired` on a given prop validator.
 */
var createChainableTypeChecker = function createChainableTypeChecker(validate) {
  // `checkType` is borrowed heavily from the `prop-types` package
  var checkType = function checkType(isRequired, props, propName, componentName, location) {
    if (props[propName] == null) {
      if (isRequired) {
        if (props[propName] === null) {
          return new Error("The ".concat(location, " `").concat(propName, "` is marked as required in ") + "".concat(componentName, ", but its value is `null`."));
        }

        return new Error("The ".concat(location, " `").concat(propName, "` is marked as required in ") + "".concat(componentName, ", but its value is `undefined`."));
      }

      return null;
    } else {
      return validate(props, propName, componentName, location);
    }
  }; // By default, the validator will have `isRequired` set to false. However, we
  // also define the `isRequired` property on the validtor so that consumers can
  // chain their prop validator and assert that the property is required.


  var chainedCheckType = checkType.bind(null, false);
  chainedCheckType.isRequired = checkType.bind(null, true);
  return chainedCheckType;
};

var _default = createChainableTypeChecker;
exports.default = _default;