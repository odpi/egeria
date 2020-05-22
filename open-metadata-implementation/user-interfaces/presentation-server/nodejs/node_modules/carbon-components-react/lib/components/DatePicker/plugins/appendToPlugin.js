"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

/**
 * @license
 *
 * Copyright IBM Corp. 2019
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

/**
 * @param {object} config Plugin configuration.
 * @returns {Plugin} A Flatpickr plugin to put adjust the position of calendar dropdown.
 */
var _default = function _default(config) {
  return function (fp) {
    /**
     * Adjusts the floating meun position after Flatpicker sets it.
     */
    var handlePreCalendarPosition = function handlePreCalendarPosition() {
      Promise.resolve().then(function () {
        var calendarContainer = fp.calendarContainer,
            fpConfig = fp.config,
            positionElement = fp._positionElement;
        var appendTo = fpConfig.appendTo;

        var _appendTo$getBounding = appendTo.getBoundingClientRect(),
            containerLeft = _appendTo$getBounding.left,
            containerTop = _appendTo$getBounding.top;

        var _positionElement$getB = positionElement.getBoundingClientRect(),
            refLeft = _positionElement$getB.left,
            refBottom = _positionElement$getB.bottom;

        if ((appendTo !== appendTo.ownerDocument.body || containerLeft !== 0 || containerTop !== 0) && appendTo.ownerDocument.defaultView.getComputedStyle(appendTo).getPropertyValue('position') === 'static') {
          throw new Error('Floating menu container must not have `position:static`.');
        } // `2` for negative mergin on calendar dropdown


        calendarContainer.style.top = "".concat(refBottom - containerTop + 2, "px");
        calendarContainer.style.left = "".concat(refLeft - containerLeft, "px");
      });
    };
    /**
     * Registers this Flatpickr plugin.
     */


    var register = function register() {
      fp.loadedPlugins.push('carbonFlatpickrAppendToPlugin');
    };

    return {
      appendTo: config.appendTo,
      onReady: register,
      onPreCalendarPosition: handlePreCalendarPosition
    };
  };
};

exports.default = _default;