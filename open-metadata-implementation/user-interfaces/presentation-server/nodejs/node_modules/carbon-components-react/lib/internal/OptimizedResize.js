"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _windowOrGlobal = _interopRequireDefault(require("window-or-global"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
// mdn resize function
var OptimizedResize = function optimizedResize() {
  var callbacks = [];
  var running = false; // run the actual callbacks

  function runCallbacks() {
    callbacks.forEach(function (callback) {
      callback();
    });
    running = false;
  } // fired on resize event


  function resize() {
    if (!running) {
      running = true;

      _windowOrGlobal.default.requestAnimationFrame(runCallbacks);
    }
  } // adds callback to loop


  function addCallback(callback) {
    if (callback) {
      var index = callbacks.indexOf(callback);

      if (index < 0) {
        callbacks.push(callback);
      }
    }
  }

  return {
    // public method to add additional callback
    add: function add(callback) {
      if (!callbacks.length) {
        _windowOrGlobal.default.addEventListener('resize', resize);
      }

      addCallback(callback);
      return {
        release: function release() {
          var index = callbacks.indexOf(callback);

          if (index >= 0) {
            callbacks.splice(index, 1);
          }
        }
      };
    }
  };
}();

var _default = OptimizedResize;
exports.default = _default;