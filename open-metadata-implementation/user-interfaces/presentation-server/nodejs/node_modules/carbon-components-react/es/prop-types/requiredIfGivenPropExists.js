/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

/**
 * @param {Function} name The name of the prop that must exist to validate
 * the current prop.
 * @param {Function} propType The original prop type checker.
 * @returns {Function} The new prop type checker for the current prop that
 * becomes required if the prop corresponding to the provided prop name exists.
 */
export default function requiredIfGivenPropExists(name, propType) {
  return function check(props, propName, componentName) {
    if (process.env.NODE_ENV !== "production" && props[name]) {
      return new Error("You must provide a value for `".concat(propName, "` in `").concat(componentName, "` if `").concat(name, "` exists."));
    }

    for (var _len = arguments.length, rest = new Array(_len > 3 ? _len - 3 : 0), _key = 3; _key < _len; _key++) {
      rest[_key - 3] = arguments[_key];
    }

    return propType.apply(void 0, [props, propName, componentName].concat(rest));
  };
}