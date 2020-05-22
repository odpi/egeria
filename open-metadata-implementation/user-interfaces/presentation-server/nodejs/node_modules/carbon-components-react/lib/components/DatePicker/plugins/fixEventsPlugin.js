"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _keyboard = require("../../../internal/keyboard");

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

/**
 * @param {object} config Plugin configuration.
 * @returns {Plugin} A Flatpickr plugin to fix Flatpickr's behavior of certain events.
 */
var _default = function _default(config) {
  return function (fp) {
    /**
     * Handles `keydown` event.
     */
    var handleKeydown = function handleKeydown(event) {
      var inputFrom = config.inputFrom,
          inputTo = config.inputTo;
      var target = event.target;

      if (inputFrom === target || inputTo === target) {
        if ((0, _keyboard.match)(event, _keyboard.keys.Enter)) {
          // Makes sure the hitting enter key picks up pending values of both `<input>`
          // Workaround for: https://github.com/flatpickr/flatpickr/issues/1942
          fp.setDate([inputFrom.value, inputTo && inputTo.value], true, fp.config.dateFormat);
          event.stopPropagation();
        } else if ((0, _keyboard.match)(event, _keyboard.keys.ArrowLeft) || (0, _keyboard.match)(event, _keyboard.keys.ArrowRight)) {
          // Prevents Flatpickr code from canceling the event if left/right arrow keys are hit on `<input>`,
          // so user can move the keyboard cursor for editing dates
          // Workaround for: https://github.com/flatpickr/flatpickr/issues/1943
          event.stopPropagation();
        } else if ((0, _keyboard.match)(event, _keyboard.keys.ArrowDown)) {
          event.preventDefault();
          fp.open();
        }
      }
    };
    /**
     * Releases event listeners used in this Flatpickr plugin.
     */


    var release = function release() {
      var inputFrom = config.inputFrom,
          inputTo = config.inputTo;

      if (inputTo) {
        inputTo.removeEventListener('keydown', handleKeydown, true);
      }

      inputFrom.removeEventListener('keydown', handleKeydown, true);
    };
    /**
     * Sets up event listeners used for this Flatpickr plugin.
     */


    var init = function init() {
      release();
      var inputFrom = config.inputFrom,
          inputTo = config.inputTo;
      inputFrom.addEventListener('keydown', handleKeydown, true);

      if (inputTo) {
        inputTo.addEventListener('keydown', handleKeydown, true);
      }
    };
    /**
     * Registers this Flatpickr plugin.
     */


    var register = function register() {
      fp.loadedPlugins.push('carbonFlatpickrFixEventsPlugin');
    };

    return {
      onReady: [register, init],
      onDestroy: [release]
    };
  };
};

exports.default = _default;