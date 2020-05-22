/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

/**
 * Generic utility to compose event handlers so that consumers can supply their
 * own event listeners on table components. The default heuristic here is to
 * iterate through the given functions until `preventDefault` is called on the
 * given event.
 *
 * @param {Array<Function>} fns array of functions to apply to the event
 * @returns {Function}
 */
export var composeEventHandlers = function composeEventHandlers(fns) {
  return function (event) {
    for (var _len = arguments.length, args = new Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
      args[_key - 1] = arguments[_key];
    }

    for (var i = 0; i < fns.length; i++) {
      if (event.defaultPrevented) {
        break;
      }

      if (typeof fns[i] === 'function') {
        fns[i].apply(fns, [event].concat(args));
      }
    }
  };
};