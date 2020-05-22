(function webpackUniversalModuleDefinition(root, factory) {
	if(typeof exports === 'object' && typeof module === 'object')
		module.exports = factory(require("react"), require("react-dom"));
	else if(typeof define === 'function' && define.amd)
		define(["react", "react-dom"], factory);
	else if(typeof exports === 'object')
		exports["ReactBootstrap"] = factory(require("react"), require("react-dom"));
	else
		root["ReactBootstrap"] = factory(root["React"], root["ReactDOM"]);
})(window, function(__WEBPACK_EXTERNAL_MODULE__1__, __WEBPACK_EXTERNAL_MODULE__3__) {
return /******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
/******/ 		}
/******/ 	};
/******/
/******/ 	// define __esModule on exports
/******/ 	__webpack_require__.r = function(exports) {
/******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 		}
/******/ 		Object.defineProperty(exports, '__esModule', { value: true });
/******/ 	};
/******/
/******/ 	// create a fake namespace object
/******/ 	// mode & 1: value is a module id, require it
/******/ 	// mode & 2: merge all properties of value into the ns
/******/ 	// mode & 4: return value when already ns object
/******/ 	// mode & 8|1: behave like require
/******/ 	__webpack_require__.t = function(value, mode) {
/******/ 		if(mode & 1) value = __webpack_require__(value);
/******/ 		if(mode & 8) return value;
/******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
/******/ 		var ns = Object.create(null);
/******/ 		__webpack_require__.r(ns);
/******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
/******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
/******/ 		return ns;
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = 21);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ (function(module, exports, __webpack_require__) {

/**
 * Copyright (c) 2013-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
if (false) { var throwOnDirectAccess, ReactIs; } else {
  // By explicitly using `prop-types` you are opting into new production behavior.
  // http://fb.me/prop-types-in-prod
  module.exports = __webpack_require__(12)();
}

/***/ }),
/* 1 */
/***/ (function(module, exports) {

module.exports = __WEBPACK_EXTERNAL_MODULE__1__;

/***/ }),
/* 2 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;/*!
  Copyright (c) 2017 Jed Watson.
  Licensed under the MIT License (MIT), see
  http://jedwatson.github.io/classnames
*/

/* global define */
(function () {
  'use strict';

  var hasOwn = {}.hasOwnProperty;

  function classNames() {
    var classes = [];

    for (var i = 0; i < arguments.length; i++) {
      var arg = arguments[i];
      if (!arg) continue;
      var argType = typeof arg;

      if (argType === 'string' || argType === 'number') {
        classes.push(arg);
      } else if (Array.isArray(arg) && arg.length) {
        var inner = classNames.apply(null, arg);

        if (inner) {
          classes.push(inner);
        }
      } else if (argType === 'object') {
        for (var key in arg) {
          if (hasOwn.call(arg, key) && arg[key]) {
            classes.push(key);
          }
        }
      }
    }

    return classes.join(' ');
  }

  if ( true && module.exports) {
    classNames.default = classNames;
    module.exports = classNames;
  } else if (true) {
    // register as 'classnames', consistent with npm package name
    !(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_RESULT__ = (function () {
      return classNames;
    }).apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
  } else {}
})();

/***/ }),
/* 3 */
/***/ (function(module, exports) {

module.exports = __WEBPACK_EXTERNAL_MODULE__3__;

/***/ }),
/* 4 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = isRequiredForA11y;

function isRequiredForA11y(validator) {
  return function validate(props, propName, componentName, location, propFullName) {
    var componentNameSafe = componentName || '<<anonymous>>';
    var propFullNameSafe = propFullName || propName;

    if (props[propName] == null) {
      return new Error('The ' + location + ' `' + propFullNameSafe + '` is required to make ' + ('`' + componentNameSafe + '` accessible for users of assistive ') + 'technologies such as screen readers.');
    }

    for (var _len = arguments.length, args = Array(_len > 5 ? _len - 5 : 0), _key = 5; _key < _len; _key++) {
      args[_key - 5] = arguments[_key];
    }

    return validator.apply(undefined, [props, propName, componentName, location, propFullName].concat(args));
  };
}

module.exports = exports['default'];

/***/ }),
/* 5 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (c) 2013-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

/**
 * Use invariant() to assert state which your program assumes to be true.
 *
 * Provide sprintf-style format (only %s is supported) and arguments
 * to provide information about what broke and what you were
 * expecting.
 *
 * The invariant message will be stripped in production, but the invariant
 * will remain to ensure logic does not differ in production.
 */

var invariant = function (condition, format, a, b, c, d, e, f) {
  if (false) {}

  if (!condition) {
    var error;

    if (format === undefined) {
      error = new Error('Minified exception occurred; use the non-minified dev environment ' + 'for the full error message and additional helpful warnings.');
    } else {
      var args = [a, b, c, d, e, f];
      var argIndex = 0;
      error = new Error(format.replace(/%s/g, function () {
        return args[argIndex++];
      }));
      error.name = 'Invariant Violation';
    }

    error.framesToPop = 1; // we don't care about invariant's own frame

    throw error;
  }
};

module.exports = invariant;

/***/ }),
/* 6 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (c) 2014-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

/**
 * Similar to invariant but only logs a warning if the condition is not met.
 * This can be used to log issues in development environments in critical
 * paths. Removing the logging code for production environments will keep the
 * same logic and follow the same code paths.
 */

var __DEV__ = "production" !== 'production';

var warning = function () {};

if (__DEV__) {
  var printWarning = function printWarning(format, args) {
    var len = arguments.length;
    args = new Array(len > 1 ? len - 1 : 0);

    for (var key = 1; key < len; key++) {
      args[key - 1] = arguments[key];
    }

    var argIndex = 0;
    var message = 'Warning: ' + format.replace(/%s/g, function () {
      return args[argIndex++];
    });

    if (typeof console !== 'undefined') {
      console.error(message);
    }

    try {
      // --- Welcome to debugging React ---
      // This error was thrown as a convenience so that you can use this stack
      // to find the callsite that caused this warning to fire.
      throw new Error(message);
    } catch (x) {}
  };

  warning = function (condition, format, args) {
    var len = arguments.length;
    args = new Array(len > 2 ? len - 2 : 0);

    for (var key = 2; key < len; key++) {
      args[key - 2] = arguments[key];
    }

    if (format === undefined) {
      throw new Error('`warning(condition, format, ...args)` requires a warning ' + 'message argument');
    }

    if (!condition) {
      printWarning.apply(null, [format].concat(args));
    }
  };
}

module.exports = warning;

/***/ }),
/* 7 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.isRequiredForA11y = exports.elementType = exports.deprecated = exports.componentOrElement = exports.all = undefined;

var _all = __webpack_require__(8);

var _all2 = _interopRequireDefault(_all);

var _componentOrElement = __webpack_require__(14);

var _componentOrElement2 = _interopRequireDefault(_componentOrElement);

var _deprecated = __webpack_require__(15);

var _deprecated2 = _interopRequireDefault(_deprecated);

var _elementType = __webpack_require__(17);

var _elementType2 = _interopRequireDefault(_elementType);

var _isRequiredForA11y = __webpack_require__(4);

var _isRequiredForA11y2 = _interopRequireDefault(_isRequiredForA11y);

function _interopRequireDefault(obj) {
  return obj && obj.__esModule ? obj : {
    default: obj
  };
}

exports.all = _all2.default;
exports.componentOrElement = _componentOrElement2.default;
exports.deprecated = _deprecated2.default;
exports.elementType = _elementType2.default;
exports.isRequiredForA11y = _isRequiredForA11y2.default;

/***/ }),
/* 8 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = all;

var _createChainableTypeChecker = __webpack_require__(10);

var _createChainableTypeChecker2 = _interopRequireDefault(_createChainableTypeChecker);

function _interopRequireDefault(obj) {
  return obj && obj.__esModule ? obj : {
    default: obj
  };
}

function all() {
  for (var _len = arguments.length, validators = Array(_len), _key = 0; _key < _len; _key++) {
    validators[_key] = arguments[_key];
  }

  function allPropTypes() {
    for (var _len2 = arguments.length, args = Array(_len2), _key2 = 0; _key2 < _len2; _key2++) {
      args[_key2] = arguments[_key2];
    }

    var error = null;
    validators.forEach(function (validator) {
      if (error != null) {
        return;
      }

      var result = validator.apply(undefined, args);

      if (result != null) {
        error = result;
      }
    });
    return error;
  }

  return (0, _createChainableTypeChecker2.default)(allPropTypes);
}

module.exports = exports['default'];

/***/ }),
/* 9 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* WEBPACK VAR INJECTION */(function(global) {/**!
 * @fileOverview Kickass library to create and place poppers near their reference elements.
 * @version 1.16.0
 * @license
 * Copyright (c) 2016 Federico Zivolo and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
var isBrowser = typeof window !== 'undefined' && typeof document !== 'undefined' && typeof navigator !== 'undefined';

var timeoutDuration = function () {
  var longerTimeoutBrowsers = ['Edge', 'Trident', 'Firefox'];

  for (var i = 0; i < longerTimeoutBrowsers.length; i += 1) {
    if (isBrowser && navigator.userAgent.indexOf(longerTimeoutBrowsers[i]) >= 0) {
      return 1;
    }
  }

  return 0;
}();

function microtaskDebounce(fn) {
  var called = false;
  return function () {
    if (called) {
      return;
    }

    called = true;
    window.Promise.resolve().then(function () {
      called = false;
      fn();
    });
  };
}

function taskDebounce(fn) {
  var scheduled = false;
  return function () {
    if (!scheduled) {
      scheduled = true;
      setTimeout(function () {
        scheduled = false;
        fn();
      }, timeoutDuration);
    }
  };
}

var supportsMicroTasks = isBrowser && window.Promise;
/**
* Create a debounced version of a method, that's asynchronously deferred
* but called in the minimum time possible.
*
* @method
* @memberof Popper.Utils
* @argument {Function} fn
* @returns {Function}
*/

var debounce = supportsMicroTasks ? microtaskDebounce : taskDebounce;
/**
 * Check if the given variable is a function
 * @method
 * @memberof Popper.Utils
 * @argument {Any} functionToCheck - variable to check
 * @returns {Boolean} answer to: is a function?
 */

function isFunction(functionToCheck) {
  var getType = {};
  return functionToCheck && getType.toString.call(functionToCheck) === '[object Function]';
}
/**
 * Get CSS computed property of the given element
 * @method
 * @memberof Popper.Utils
 * @argument {Eement} element
 * @argument {String} property
 */


function getStyleComputedProperty(element, property) {
  if (element.nodeType !== 1) {
    return [];
  } // NOTE: 1 DOM access here


  var window = element.ownerDocument.defaultView;
  var css = window.getComputedStyle(element, null);
  return property ? css[property] : css;
}
/**
 * Returns the parentNode or the host of the element
 * @method
 * @memberof Popper.Utils
 * @argument {Element} element
 * @returns {Element} parent
 */


function getParentNode(element) {
  if (element.nodeName === 'HTML') {
    return element;
  }

  return element.parentNode || element.host;
}
/**
 * Returns the scrolling parent of the given element
 * @method
 * @memberof Popper.Utils
 * @argument {Element} element
 * @returns {Element} scroll parent
 */


function getScrollParent(element) {
  // Return body, `getScroll` will take care to get the correct `scrollTop` from it
  if (!element) {
    return document.body;
  }

  switch (element.nodeName) {
    case 'HTML':
    case 'BODY':
      return element.ownerDocument.body;

    case '#document':
      return element.body;
  } // Firefox want us to check `-x` and `-y` variations as well


  var _getStyleComputedProp = getStyleComputedProperty(element),
      overflow = _getStyleComputedProp.overflow,
      overflowX = _getStyleComputedProp.overflowX,
      overflowY = _getStyleComputedProp.overflowY;

  if (/(auto|scroll|overlay)/.test(overflow + overflowY + overflowX)) {
    return element;
  }

  return getScrollParent(getParentNode(element));
}
/**
 * Returns the reference node of the reference object, or the reference object itself.
 * @method
 * @memberof Popper.Utils
 * @param {Element|Object} reference - the reference element (the popper will be relative to this)
 * @returns {Element} parent
 */


function getReferenceNode(reference) {
  return reference && reference.referenceNode ? reference.referenceNode : reference;
}

var isIE11 = isBrowser && !!(window.MSInputMethodContext && document.documentMode);
var isIE10 = isBrowser && /MSIE 10/.test(navigator.userAgent);
/**
 * Determines if the browser is Internet Explorer
 * @method
 * @memberof Popper.Utils
 * @param {Number} version to check
 * @returns {Boolean} isIE
 */

function isIE(version) {
  if (version === 11) {
    return isIE11;
  }

  if (version === 10) {
    return isIE10;
  }

  return isIE11 || isIE10;
}
/**
 * Returns the offset parent of the given element
 * @method
 * @memberof Popper.Utils
 * @argument {Element} element
 * @returns {Element} offset parent
 */


function getOffsetParent(element) {
  if (!element) {
    return document.documentElement;
  }

  var noOffsetParent = isIE(10) ? document.body : null; // NOTE: 1 DOM access here

  var offsetParent = element.offsetParent || null; // Skip hidden elements which don't have an offsetParent

  while (offsetParent === noOffsetParent && element.nextElementSibling) {
    offsetParent = (element = element.nextElementSibling).offsetParent;
  }

  var nodeName = offsetParent && offsetParent.nodeName;

  if (!nodeName || nodeName === 'BODY' || nodeName === 'HTML') {
    return element ? element.ownerDocument.documentElement : document.documentElement;
  } // .offsetParent will return the closest TH, TD or TABLE in case
  // no offsetParent is present, I hate this job...


  if (['TH', 'TD', 'TABLE'].indexOf(offsetParent.nodeName) !== -1 && getStyleComputedProperty(offsetParent, 'position') === 'static') {
    return getOffsetParent(offsetParent);
  }

  return offsetParent;
}

function isOffsetContainer(element) {
  var nodeName = element.nodeName;

  if (nodeName === 'BODY') {
    return false;
  }

  return nodeName === 'HTML' || getOffsetParent(element.firstElementChild) === element;
}
/**
 * Finds the root node (document, shadowDOM root) of the given element
 * @method
 * @memberof Popper.Utils
 * @argument {Element} node
 * @returns {Element} root node
 */


function getRoot(node) {
  if (node.parentNode !== null) {
    return getRoot(node.parentNode);
  }

  return node;
}
/**
 * Finds the offset parent common to the two provided nodes
 * @method
 * @memberof Popper.Utils
 * @argument {Element} element1
 * @argument {Element} element2
 * @returns {Element} common offset parent
 */


function findCommonOffsetParent(element1, element2) {
  // This check is needed to avoid errors in case one of the elements isn't defined for any reason
  if (!element1 || !element1.nodeType || !element2 || !element2.nodeType) {
    return document.documentElement;
  } // Here we make sure to give as "start" the element that comes first in the DOM


  var order = element1.compareDocumentPosition(element2) & Node.DOCUMENT_POSITION_FOLLOWING;
  var start = order ? element1 : element2;
  var end = order ? element2 : element1; // Get common ancestor container

  var range = document.createRange();
  range.setStart(start, 0);
  range.setEnd(end, 0);
  var commonAncestorContainer = range.commonAncestorContainer; // Both nodes are inside #document

  if (element1 !== commonAncestorContainer && element2 !== commonAncestorContainer || start.contains(end)) {
    if (isOffsetContainer(commonAncestorContainer)) {
      return commonAncestorContainer;
    }

    return getOffsetParent(commonAncestorContainer);
  } // one of the nodes is inside shadowDOM, find which one


  var element1root = getRoot(element1);

  if (element1root.host) {
    return findCommonOffsetParent(element1root.host, element2);
  } else {
    return findCommonOffsetParent(element1, getRoot(element2).host);
  }
}
/**
 * Gets the scroll value of the given element in the given side (top and left)
 * @method
 * @memberof Popper.Utils
 * @argument {Element} element
 * @argument {String} side `top` or `left`
 * @returns {number} amount of scrolled pixels
 */


function getScroll(element) {
  var side = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : 'top';
  var upperSide = side === 'top' ? 'scrollTop' : 'scrollLeft';
  var nodeName = element.nodeName;

  if (nodeName === 'BODY' || nodeName === 'HTML') {
    var html = element.ownerDocument.documentElement;
    var scrollingElement = element.ownerDocument.scrollingElement || html;
    return scrollingElement[upperSide];
  }

  return element[upperSide];
}
/*
 * Sum or subtract the element scroll values (left and top) from a given rect object
 * @method
 * @memberof Popper.Utils
 * @param {Object} rect - Rect object you want to change
 * @param {HTMLElement} element - The element from the function reads the scroll values
 * @param {Boolean} subtract - set to true if you want to subtract the scroll values
 * @return {Object} rect - The modifier rect object
 */


function includeScroll(rect, element) {
  var subtract = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : false;
  var scrollTop = getScroll(element, 'top');
  var scrollLeft = getScroll(element, 'left');
  var modifier = subtract ? -1 : 1;
  rect.top += scrollTop * modifier;
  rect.bottom += scrollTop * modifier;
  rect.left += scrollLeft * modifier;
  rect.right += scrollLeft * modifier;
  return rect;
}
/*
 * Helper to detect borders of a given element
 * @method
 * @memberof Popper.Utils
 * @param {CSSStyleDeclaration} styles
 * Result of `getStyleComputedProperty` on the given element
 * @param {String} axis - `x` or `y`
 * @return {number} borders - The borders size of the given axis
 */


function getBordersSize(styles, axis) {
  var sideA = axis === 'x' ? 'Left' : 'Top';
  var sideB = sideA === 'Left' ? 'Right' : 'Bottom';
  return parseFloat(styles['border' + sideA + 'Width'], 10) + parseFloat(styles['border' + sideB + 'Width'], 10);
}

function getSize(axis, body, html, computedStyle) {
  return Math.max(body['offset' + axis], body['scroll' + axis], html['client' + axis], html['offset' + axis], html['scroll' + axis], isIE(10) ? parseInt(html['offset' + axis]) + parseInt(computedStyle['margin' + (axis === 'Height' ? 'Top' : 'Left')]) + parseInt(computedStyle['margin' + (axis === 'Height' ? 'Bottom' : 'Right')]) : 0);
}

function getWindowSizes(document) {
  var body = document.body;
  var html = document.documentElement;
  var computedStyle = isIE(10) && getComputedStyle(html);
  return {
    height: getSize('Height', body, html, computedStyle),
    width: getSize('Width', body, html, computedStyle)
  };
}

var classCallCheck = function (instance, Constructor) {
  if (!(instance instanceof Constructor)) {
    throw new TypeError("Cannot call a class as a function");
  }
};

var createClass = function () {
  function defineProperties(target, props) {
    for (var i = 0; i < props.length; i++) {
      var descriptor = props[i];
      descriptor.enumerable = descriptor.enumerable || false;
      descriptor.configurable = true;
      if ("value" in descriptor) descriptor.writable = true;
      Object.defineProperty(target, descriptor.key, descriptor);
    }
  }

  return function (Constructor, protoProps, staticProps) {
    if (protoProps) defineProperties(Constructor.prototype, protoProps);
    if (staticProps) defineProperties(Constructor, staticProps);
    return Constructor;
  };
}();

var defineProperty = function (obj, key, value) {
  if (key in obj) {
    Object.defineProperty(obj, key, {
      value: value,
      enumerable: true,
      configurable: true,
      writable: true
    });
  } else {
    obj[key] = value;
  }

  return obj;
};

var _extends = Object.assign || function (target) {
  for (var i = 1; i < arguments.length; i++) {
    var source = arguments[i];

    for (var key in source) {
      if (Object.prototype.hasOwnProperty.call(source, key)) {
        target[key] = source[key];
      }
    }
  }

  return target;
};
/**
 * Given element offsets, generate an output similar to getBoundingClientRect
 * @method
 * @memberof Popper.Utils
 * @argument {Object} offsets
 * @returns {Object} ClientRect like output
 */


function getClientRect(offsets) {
  return _extends({}, offsets, {
    right: offsets.left + offsets.width,
    bottom: offsets.top + offsets.height
  });
}
/**
 * Get bounding client rect of given element
 * @method
 * @memberof Popper.Utils
 * @param {HTMLElement} element
 * @return {Object} client rect
 */


function getBoundingClientRect(element) {
  var rect = {}; // IE10 10 FIX: Please, don't ask, the element isn't
  // considered in DOM in some circumstances...
  // This isn't reproducible in IE10 compatibility mode of IE11

  try {
    if (isIE(10)) {
      rect = element.getBoundingClientRect();
      var scrollTop = getScroll(element, 'top');
      var scrollLeft = getScroll(element, 'left');
      rect.top += scrollTop;
      rect.left += scrollLeft;
      rect.bottom += scrollTop;
      rect.right += scrollLeft;
    } else {
      rect = element.getBoundingClientRect();
    }
  } catch (e) {}

  var result = {
    left: rect.left,
    top: rect.top,
    width: rect.right - rect.left,
    height: rect.bottom - rect.top
  }; // subtract scrollbar size from sizes

  var sizes = element.nodeName === 'HTML' ? getWindowSizes(element.ownerDocument) : {};
  var width = sizes.width || element.clientWidth || result.width;
  var height = sizes.height || element.clientHeight || result.height;
  var horizScrollbar = element.offsetWidth - width;
  var vertScrollbar = element.offsetHeight - height; // if an hypothetical scrollbar is detected, we must be sure it's not a `border`
  // we make this check conditional for performance reasons

  if (horizScrollbar || vertScrollbar) {
    var styles = getStyleComputedProperty(element);
    horizScrollbar -= getBordersSize(styles, 'x');
    vertScrollbar -= getBordersSize(styles, 'y');
    result.width -= horizScrollbar;
    result.height -= vertScrollbar;
  }

  return getClientRect(result);
}

function getOffsetRectRelativeToArbitraryNode(children, parent) {
  var fixedPosition = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : false;
  var isIE10 = isIE(10);
  var isHTML = parent.nodeName === 'HTML';
  var childrenRect = getBoundingClientRect(children);
  var parentRect = getBoundingClientRect(parent);
  var scrollParent = getScrollParent(children);
  var styles = getStyleComputedProperty(parent);
  var borderTopWidth = parseFloat(styles.borderTopWidth, 10);
  var borderLeftWidth = parseFloat(styles.borderLeftWidth, 10); // In cases where the parent is fixed, we must ignore negative scroll in offset calc

  if (fixedPosition && isHTML) {
    parentRect.top = Math.max(parentRect.top, 0);
    parentRect.left = Math.max(parentRect.left, 0);
  }

  var offsets = getClientRect({
    top: childrenRect.top - parentRect.top - borderTopWidth,
    left: childrenRect.left - parentRect.left - borderLeftWidth,
    width: childrenRect.width,
    height: childrenRect.height
  });
  offsets.marginTop = 0;
  offsets.marginLeft = 0; // Subtract margins of documentElement in case it's being used as parent
  // we do this only on HTML because it's the only element that behaves
  // differently when margins are applied to it. The margins are included in
  // the box of the documentElement, in the other cases not.

  if (!isIE10 && isHTML) {
    var marginTop = parseFloat(styles.marginTop, 10);
    var marginLeft = parseFloat(styles.marginLeft, 10);
    offsets.top -= borderTopWidth - marginTop;
    offsets.bottom -= borderTopWidth - marginTop;
    offsets.left -= borderLeftWidth - marginLeft;
    offsets.right -= borderLeftWidth - marginLeft; // Attach marginTop and marginLeft because in some circumstances we may need them

    offsets.marginTop = marginTop;
    offsets.marginLeft = marginLeft;
  }

  if (isIE10 && !fixedPosition ? parent.contains(scrollParent) : parent === scrollParent && scrollParent.nodeName !== 'BODY') {
    offsets = includeScroll(offsets, parent);
  }

  return offsets;
}

function getViewportOffsetRectRelativeToArtbitraryNode(element) {
  var excludeScroll = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;
  var html = element.ownerDocument.documentElement;
  var relativeOffset = getOffsetRectRelativeToArbitraryNode(element, html);
  var width = Math.max(html.clientWidth, window.innerWidth || 0);
  var height = Math.max(html.clientHeight, window.innerHeight || 0);
  var scrollTop = !excludeScroll ? getScroll(html) : 0;
  var scrollLeft = !excludeScroll ? getScroll(html, 'left') : 0;
  var offset = {
    top: scrollTop - relativeOffset.top + relativeOffset.marginTop,
    left: scrollLeft - relativeOffset.left + relativeOffset.marginLeft,
    width: width,
    height: height
  };
  return getClientRect(offset);
}
/**
 * Check if the given element is fixed or is inside a fixed parent
 * @method
 * @memberof Popper.Utils
 * @argument {Element} element
 * @argument {Element} customContainer
 * @returns {Boolean} answer to "isFixed?"
 */


function isFixed(element) {
  var nodeName = element.nodeName;

  if (nodeName === 'BODY' || nodeName === 'HTML') {
    return false;
  }

  if (getStyleComputedProperty(element, 'position') === 'fixed') {
    return true;
  }

  var parentNode = getParentNode(element);

  if (!parentNode) {
    return false;
  }

  return isFixed(parentNode);
}
/**
 * Finds the first parent of an element that has a transformed property defined
 * @method
 * @memberof Popper.Utils
 * @argument {Element} element
 * @returns {Element} first transformed parent or documentElement
 */


function getFixedPositionOffsetParent(element) {
  // This check is needed to avoid errors in case one of the elements isn't defined for any reason
  if (!element || !element.parentElement || isIE()) {
    return document.documentElement;
  }

  var el = element.parentElement;

  while (el && getStyleComputedProperty(el, 'transform') === 'none') {
    el = el.parentElement;
  }

  return el || document.documentElement;
}
/**
 * Computed the boundaries limits and return them
 * @method
 * @memberof Popper.Utils
 * @param {HTMLElement} popper
 * @param {HTMLElement} reference
 * @param {number} padding
 * @param {HTMLElement} boundariesElement - Element used to define the boundaries
 * @param {Boolean} fixedPosition - Is in fixed position mode
 * @returns {Object} Coordinates of the boundaries
 */


function getBoundaries(popper, reference, padding, boundariesElement) {
  var fixedPosition = arguments.length > 4 && arguments[4] !== undefined ? arguments[4] : false; // NOTE: 1 DOM access here

  var boundaries = {
    top: 0,
    left: 0
  };
  var offsetParent = fixedPosition ? getFixedPositionOffsetParent(popper) : findCommonOffsetParent(popper, getReferenceNode(reference)); // Handle viewport case

  if (boundariesElement === 'viewport') {
    boundaries = getViewportOffsetRectRelativeToArtbitraryNode(offsetParent, fixedPosition);
  } else {
    // Handle other cases based on DOM element used as boundaries
    var boundariesNode = void 0;

    if (boundariesElement === 'scrollParent') {
      boundariesNode = getScrollParent(getParentNode(reference));

      if (boundariesNode.nodeName === 'BODY') {
        boundariesNode = popper.ownerDocument.documentElement;
      }
    } else if (boundariesElement === 'window') {
      boundariesNode = popper.ownerDocument.documentElement;
    } else {
      boundariesNode = boundariesElement;
    }

    var offsets = getOffsetRectRelativeToArbitraryNode(boundariesNode, offsetParent, fixedPosition); // In case of HTML, we need a different computation

    if (boundariesNode.nodeName === 'HTML' && !isFixed(offsetParent)) {
      var _getWindowSizes = getWindowSizes(popper.ownerDocument),
          height = _getWindowSizes.height,
          width = _getWindowSizes.width;

      boundaries.top += offsets.top - offsets.marginTop;
      boundaries.bottom = height + offsets.top;
      boundaries.left += offsets.left - offsets.marginLeft;
      boundaries.right = width + offsets.left;
    } else {
      // for all the other DOM elements, this one is good
      boundaries = offsets;
    }
  } // Add paddings


  padding = padding || 0;
  var isPaddingNumber = typeof padding === 'number';
  boundaries.left += isPaddingNumber ? padding : padding.left || 0;
  boundaries.top += isPaddingNumber ? padding : padding.top || 0;
  boundaries.right -= isPaddingNumber ? padding : padding.right || 0;
  boundaries.bottom -= isPaddingNumber ? padding : padding.bottom || 0;
  return boundaries;
}

function getArea(_ref) {
  var width = _ref.width,
      height = _ref.height;
  return width * height;
}
/**
 * Utility used to transform the `auto` placement to the placement with more
 * available space.
 * @method
 * @memberof Popper.Utils
 * @argument {Object} data - The data object generated by update method
 * @argument {Object} options - Modifiers configuration and options
 * @returns {Object} The data object, properly modified
 */


function computeAutoPlacement(placement, refRect, popper, reference, boundariesElement) {
  var padding = arguments.length > 5 && arguments[5] !== undefined ? arguments[5] : 0;

  if (placement.indexOf('auto') === -1) {
    return placement;
  }

  var boundaries = getBoundaries(popper, reference, padding, boundariesElement);
  var rects = {
    top: {
      width: boundaries.width,
      height: refRect.top - boundaries.top
    },
    right: {
      width: boundaries.right - refRect.right,
      height: boundaries.height
    },
    bottom: {
      width: boundaries.width,
      height: boundaries.bottom - refRect.bottom
    },
    left: {
      width: refRect.left - boundaries.left,
      height: boundaries.height
    }
  };
  var sortedAreas = Object.keys(rects).map(function (key) {
    return _extends({
      key: key
    }, rects[key], {
      area: getArea(rects[key])
    });
  }).sort(function (a, b) {
    return b.area - a.area;
  });
  var filteredAreas = sortedAreas.filter(function (_ref2) {
    var width = _ref2.width,
        height = _ref2.height;
    return width >= popper.clientWidth && height >= popper.clientHeight;
  });
  var computedPlacement = filteredAreas.length > 0 ? filteredAreas[0].key : sortedAreas[0].key;
  var variation = placement.split('-')[1];
  return computedPlacement + (variation ? '-' + variation : '');
}
/**
 * Get offsets to the reference element
 * @method
 * @memberof Popper.Utils
 * @param {Object} state
 * @param {Element} popper - the popper element
 * @param {Element} reference - the reference element (the popper will be relative to this)
 * @param {Element} fixedPosition - is in fixed position mode
 * @returns {Object} An object containing the offsets which will be applied to the popper
 */


function getReferenceOffsets(state, popper, reference) {
  var fixedPosition = arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : null;
  var commonOffsetParent = fixedPosition ? getFixedPositionOffsetParent(popper) : findCommonOffsetParent(popper, getReferenceNode(reference));
  return getOffsetRectRelativeToArbitraryNode(reference, commonOffsetParent, fixedPosition);
}
/**
 * Get the outer sizes of the given element (offset size + margins)
 * @method
 * @memberof Popper.Utils
 * @argument {Element} element
 * @returns {Object} object containing width and height properties
 */


function getOuterSizes(element) {
  var window = element.ownerDocument.defaultView;
  var styles = window.getComputedStyle(element);
  var x = parseFloat(styles.marginTop || 0) + parseFloat(styles.marginBottom || 0);
  var y = parseFloat(styles.marginLeft || 0) + parseFloat(styles.marginRight || 0);
  var result = {
    width: element.offsetWidth + y,
    height: element.offsetHeight + x
  };
  return result;
}
/**
 * Get the opposite placement of the given one
 * @method
 * @memberof Popper.Utils
 * @argument {String} placement
 * @returns {String} flipped placement
 */


function getOppositePlacement(placement) {
  var hash = {
    left: 'right',
    right: 'left',
    bottom: 'top',
    top: 'bottom'
  };
  return placement.replace(/left|right|bottom|top/g, function (matched) {
    return hash[matched];
  });
}
/**
 * Get offsets to the popper
 * @method
 * @memberof Popper.Utils
 * @param {Object} position - CSS position the Popper will get applied
 * @param {HTMLElement} popper - the popper element
 * @param {Object} referenceOffsets - the reference offsets (the popper will be relative to this)
 * @param {String} placement - one of the valid placement options
 * @returns {Object} popperOffsets - An object containing the offsets which will be applied to the popper
 */


function getPopperOffsets(popper, referenceOffsets, placement) {
  placement = placement.split('-')[0]; // Get popper node sizes

  var popperRect = getOuterSizes(popper); // Add position, width and height to our offsets object

  var popperOffsets = {
    width: popperRect.width,
    height: popperRect.height
  }; // depending by the popper placement we have to compute its offsets slightly differently

  var isHoriz = ['right', 'left'].indexOf(placement) !== -1;
  var mainSide = isHoriz ? 'top' : 'left';
  var secondarySide = isHoriz ? 'left' : 'top';
  var measurement = isHoriz ? 'height' : 'width';
  var secondaryMeasurement = !isHoriz ? 'height' : 'width';
  popperOffsets[mainSide] = referenceOffsets[mainSide] + referenceOffsets[measurement] / 2 - popperRect[measurement] / 2;

  if (placement === secondarySide) {
    popperOffsets[secondarySide] = referenceOffsets[secondarySide] - popperRect[secondaryMeasurement];
  } else {
    popperOffsets[secondarySide] = referenceOffsets[getOppositePlacement(secondarySide)];
  }

  return popperOffsets;
}
/**
 * Mimics the `find` method of Array
 * @method
 * @memberof Popper.Utils
 * @argument {Array} arr
 * @argument prop
 * @argument value
 * @returns index or -1
 */


function find(arr, check) {
  // use native find if supported
  if (Array.prototype.find) {
    return arr.find(check);
  } // use `filter` to obtain the same behavior of `find`


  return arr.filter(check)[0];
}
/**
 * Return the index of the matching object
 * @method
 * @memberof Popper.Utils
 * @argument {Array} arr
 * @argument prop
 * @argument value
 * @returns index or -1
 */


function findIndex(arr, prop, value) {
  // use native findIndex if supported
  if (Array.prototype.findIndex) {
    return arr.findIndex(function (cur) {
      return cur[prop] === value;
    });
  } // use `find` + `indexOf` if `findIndex` isn't supported


  var match = find(arr, function (obj) {
    return obj[prop] === value;
  });
  return arr.indexOf(match);
}
/**
 * Loop trough the list of modifiers and run them in order,
 * each of them will then edit the data object.
 * @method
 * @memberof Popper.Utils
 * @param {dataObject} data
 * @param {Array} modifiers
 * @param {String} ends - Optional modifier name used as stopper
 * @returns {dataObject}
 */


function runModifiers(modifiers, data, ends) {
  var modifiersToRun = ends === undefined ? modifiers : modifiers.slice(0, findIndex(modifiers, 'name', ends));
  modifiersToRun.forEach(function (modifier) {
    if (modifier['function']) {
      // eslint-disable-line dot-notation
      console.warn('`modifier.function` is deprecated, use `modifier.fn`!');
    }

    var fn = modifier['function'] || modifier.fn; // eslint-disable-line dot-notation

    if (modifier.enabled && isFunction(fn)) {
      // Add properties to offsets to make them a complete clientRect object
      // we do this before each modifier to make sure the previous one doesn't
      // mess with these values
      data.offsets.popper = getClientRect(data.offsets.popper);
      data.offsets.reference = getClientRect(data.offsets.reference);
      data = fn(data, modifier);
    }
  });
  return data;
}
/**
 * Updates the position of the popper, computing the new offsets and applying
 * the new style.<br />
 * Prefer `scheduleUpdate` over `update` because of performance reasons.
 * @method
 * @memberof Popper
 */


function update() {
  // if popper is destroyed, don't perform any further update
  if (this.state.isDestroyed) {
    return;
  }

  var data = {
    instance: this,
    styles: {},
    arrowStyles: {},
    attributes: {},
    flipped: false,
    offsets: {}
  }; // compute reference element offsets

  data.offsets.reference = getReferenceOffsets(this.state, this.popper, this.reference, this.options.positionFixed); // compute auto placement, store placement inside the data object,
  // modifiers will be able to edit `placement` if needed
  // and refer to originalPlacement to know the original value

  data.placement = computeAutoPlacement(this.options.placement, data.offsets.reference, this.popper, this.reference, this.options.modifiers.flip.boundariesElement, this.options.modifiers.flip.padding); // store the computed placement inside `originalPlacement`

  data.originalPlacement = data.placement;
  data.positionFixed = this.options.positionFixed; // compute the popper offsets

  data.offsets.popper = getPopperOffsets(this.popper, data.offsets.reference, data.placement);
  data.offsets.popper.position = this.options.positionFixed ? 'fixed' : 'absolute'; // run the modifiers

  data = runModifiers(this.modifiers, data); // the first `update` will call `onCreate` callback
  // the other ones will call `onUpdate` callback

  if (!this.state.isCreated) {
    this.state.isCreated = true;
    this.options.onCreate(data);
  } else {
    this.options.onUpdate(data);
  }
}
/**
 * Helper used to know if the given modifier is enabled.
 * @method
 * @memberof Popper.Utils
 * @returns {Boolean}
 */


function isModifierEnabled(modifiers, modifierName) {
  return modifiers.some(function (_ref) {
    var name = _ref.name,
        enabled = _ref.enabled;
    return enabled && name === modifierName;
  });
}
/**
 * Get the prefixed supported property name
 * @method
 * @memberof Popper.Utils
 * @argument {String} property (camelCase)
 * @returns {String} prefixed property (camelCase or PascalCase, depending on the vendor prefix)
 */


function getSupportedPropertyName(property) {
  var prefixes = [false, 'ms', 'Webkit', 'Moz', 'O'];
  var upperProp = property.charAt(0).toUpperCase() + property.slice(1);

  for (var i = 0; i < prefixes.length; i++) {
    var prefix = prefixes[i];
    var toCheck = prefix ? '' + prefix + upperProp : property;

    if (typeof document.body.style[toCheck] !== 'undefined') {
      return toCheck;
    }
  }

  return null;
}
/**
 * Destroys the popper.
 * @method
 * @memberof Popper
 */


function destroy() {
  this.state.isDestroyed = true; // touch DOM only if `applyStyle` modifier is enabled

  if (isModifierEnabled(this.modifiers, 'applyStyle')) {
    this.popper.removeAttribute('x-placement');
    this.popper.style.position = '';
    this.popper.style.top = '';
    this.popper.style.left = '';
    this.popper.style.right = '';
    this.popper.style.bottom = '';
    this.popper.style.willChange = '';
    this.popper.style[getSupportedPropertyName('transform')] = '';
  }

  this.disableEventListeners(); // remove the popper if user explicitly asked for the deletion on destroy
  // do not use `remove` because IE11 doesn't support it

  if (this.options.removeOnDestroy) {
    this.popper.parentNode.removeChild(this.popper);
  }

  return this;
}
/**
 * Get the window associated with the element
 * @argument {Element} element
 * @returns {Window}
 */


function getWindow(element) {
  var ownerDocument = element.ownerDocument;
  return ownerDocument ? ownerDocument.defaultView : window;
}

function attachToScrollParents(scrollParent, event, callback, scrollParents) {
  var isBody = scrollParent.nodeName === 'BODY';
  var target = isBody ? scrollParent.ownerDocument.defaultView : scrollParent;
  target.addEventListener(event, callback, {
    passive: true
  });

  if (!isBody) {
    attachToScrollParents(getScrollParent(target.parentNode), event, callback, scrollParents);
  }

  scrollParents.push(target);
}
/**
 * Setup needed event listeners used to update the popper position
 * @method
 * @memberof Popper.Utils
 * @private
 */


function setupEventListeners(reference, options, state, updateBound) {
  // Resize event listener on window
  state.updateBound = updateBound;
  getWindow(reference).addEventListener('resize', state.updateBound, {
    passive: true
  }); // Scroll event listener on scroll parents

  var scrollElement = getScrollParent(reference);
  attachToScrollParents(scrollElement, 'scroll', state.updateBound, state.scrollParents);
  state.scrollElement = scrollElement;
  state.eventsEnabled = true;
  return state;
}
/**
 * It will add resize/scroll events and start recalculating
 * position of the popper element when they are triggered.
 * @method
 * @memberof Popper
 */


function enableEventListeners() {
  if (!this.state.eventsEnabled) {
    this.state = setupEventListeners(this.reference, this.options, this.state, this.scheduleUpdate);
  }
}
/**
 * Remove event listeners used to update the popper position
 * @method
 * @memberof Popper.Utils
 * @private
 */


function removeEventListeners(reference, state) {
  // Remove resize event listener on window
  getWindow(reference).removeEventListener('resize', state.updateBound); // Remove scroll event listener on scroll parents

  state.scrollParents.forEach(function (target) {
    target.removeEventListener('scroll', state.updateBound);
  }); // Reset state

  state.updateBound = null;
  state.scrollParents = [];
  state.scrollElement = null;
  state.eventsEnabled = false;
  return state;
}
/**
 * It will remove resize/scroll events and won't recalculate popper position
 * when they are triggered. It also won't trigger `onUpdate` callback anymore,
 * unless you call `update` method manually.
 * @method
 * @memberof Popper
 */


function disableEventListeners() {
  if (this.state.eventsEnabled) {
    cancelAnimationFrame(this.scheduleUpdate);
    this.state = removeEventListeners(this.reference, this.state);
  }
}
/**
 * Tells if a given input is a number
 * @method
 * @memberof Popper.Utils
 * @param {*} input to check
 * @return {Boolean}
 */


function isNumeric(n) {
  return n !== '' && !isNaN(parseFloat(n)) && isFinite(n);
}
/**
 * Set the style to the given popper
 * @method
 * @memberof Popper.Utils
 * @argument {Element} element - Element to apply the style to
 * @argument {Object} styles
 * Object with a list of properties and values which will be applied to the element
 */


function setStyles(element, styles) {
  Object.keys(styles).forEach(function (prop) {
    var unit = ''; // add unit if the value is numeric and is one of the following

    if (['width', 'height', 'top', 'right', 'bottom', 'left'].indexOf(prop) !== -1 && isNumeric(styles[prop])) {
      unit = 'px';
    }

    element.style[prop] = styles[prop] + unit;
  });
}
/**
 * Set the attributes to the given popper
 * @method
 * @memberof Popper.Utils
 * @argument {Element} element - Element to apply the attributes to
 * @argument {Object} styles
 * Object with a list of properties and values which will be applied to the element
 */


function setAttributes(element, attributes) {
  Object.keys(attributes).forEach(function (prop) {
    var value = attributes[prop];

    if (value !== false) {
      element.setAttribute(prop, attributes[prop]);
    } else {
      element.removeAttribute(prop);
    }
  });
}
/**
 * @function
 * @memberof Modifiers
 * @argument {Object} data - The data object generated by `update` method
 * @argument {Object} data.styles - List of style properties - values to apply to popper element
 * @argument {Object} data.attributes - List of attribute properties - values to apply to popper element
 * @argument {Object} options - Modifiers configuration and options
 * @returns {Object} The same data object
 */


function applyStyle(data) {
  // any property present in `data.styles` will be applied to the popper,
  // in this way we can make the 3rd party modifiers add custom styles to it
  // Be aware, modifiers could override the properties defined in the previous
  // lines of this modifier!
  setStyles(data.instance.popper, data.styles); // any property present in `data.attributes` will be applied to the popper,
  // they will be set as HTML attributes of the element

  setAttributes(data.instance.popper, data.attributes); // if arrowElement is defined and arrowStyles has some properties

  if (data.arrowElement && Object.keys(data.arrowStyles).length) {
    setStyles(data.arrowElement, data.arrowStyles);
  }

  return data;
}
/**
 * Set the x-placement attribute before everything else because it could be used
 * to add margins to the popper margins needs to be calculated to get the
 * correct popper offsets.
 * @method
 * @memberof Popper.modifiers
 * @param {HTMLElement} reference - The reference element used to position the popper
 * @param {HTMLElement} popper - The HTML element used as popper
 * @param {Object} options - Popper.js options
 */


function applyStyleOnLoad(reference, popper, options, modifierOptions, state) {
  // compute reference element offsets
  var referenceOffsets = getReferenceOffsets(state, popper, reference, options.positionFixed); // compute auto placement, store placement inside the data object,
  // modifiers will be able to edit `placement` if needed
  // and refer to originalPlacement to know the original value

  var placement = computeAutoPlacement(options.placement, referenceOffsets, popper, reference, options.modifiers.flip.boundariesElement, options.modifiers.flip.padding);
  popper.setAttribute('x-placement', placement); // Apply `position` to popper before anything else because
  // without the position applied we can't guarantee correct computations

  setStyles(popper, {
    position: options.positionFixed ? 'fixed' : 'absolute'
  });
  return options;
}
/**
 * @function
 * @memberof Popper.Utils
 * @argument {Object} data - The data object generated by `update` method
 * @argument {Boolean} shouldRound - If the offsets should be rounded at all
 * @returns {Object} The popper's position offsets rounded
 *
 * The tale of pixel-perfect positioning. It's still not 100% perfect, but as
 * good as it can be within reason.
 * Discussion here: https://github.com/FezVrasta/popper.js/pull/715
 *
 * Low DPI screens cause a popper to be blurry if not using full pixels (Safari
 * as well on High DPI screens).
 *
 * Firefox prefers no rounding for positioning and does not have blurriness on
 * high DPI screens.
 *
 * Only horizontal placement and left/right values need to be considered.
 */


function getRoundedOffsets(data, shouldRound) {
  var _data$offsets = data.offsets,
      popper = _data$offsets.popper,
      reference = _data$offsets.reference;
  var round = Math.round,
      floor = Math.floor;

  var noRound = function noRound(v) {
    return v;
  };

  var referenceWidth = round(reference.width);
  var popperWidth = round(popper.width);
  var isVertical = ['left', 'right'].indexOf(data.placement) !== -1;
  var isVariation = data.placement.indexOf('-') !== -1;
  var sameWidthParity = referenceWidth % 2 === popperWidth % 2;
  var bothOddWidth = referenceWidth % 2 === 1 && popperWidth % 2 === 1;
  var horizontalToInteger = !shouldRound ? noRound : isVertical || isVariation || sameWidthParity ? round : floor;
  var verticalToInteger = !shouldRound ? noRound : round;
  return {
    left: horizontalToInteger(bothOddWidth && !isVariation && shouldRound ? popper.left - 1 : popper.left),
    top: verticalToInteger(popper.top),
    bottom: verticalToInteger(popper.bottom),
    right: horizontalToInteger(popper.right)
  };
}

var isFirefox = isBrowser && /Firefox/i.test(navigator.userAgent);
/**
 * @function
 * @memberof Modifiers
 * @argument {Object} data - The data object generated by `update` method
 * @argument {Object} options - Modifiers configuration and options
 * @returns {Object} The data object, properly modified
 */

function computeStyle(data, options) {
  var x = options.x,
      y = options.y;
  var popper = data.offsets.popper; // Remove this legacy support in Popper.js v2

  var legacyGpuAccelerationOption = find(data.instance.modifiers, function (modifier) {
    return modifier.name === 'applyStyle';
  }).gpuAcceleration;

  if (legacyGpuAccelerationOption !== undefined) {
    console.warn('WARNING: `gpuAcceleration` option moved to `computeStyle` modifier and will not be supported in future versions of Popper.js!');
  }

  var gpuAcceleration = legacyGpuAccelerationOption !== undefined ? legacyGpuAccelerationOption : options.gpuAcceleration;
  var offsetParent = getOffsetParent(data.instance.popper);
  var offsetParentRect = getBoundingClientRect(offsetParent); // Styles

  var styles = {
    position: popper.position
  };
  var offsets = getRoundedOffsets(data, window.devicePixelRatio < 2 || !isFirefox);
  var sideA = x === 'bottom' ? 'top' : 'bottom';
  var sideB = y === 'right' ? 'left' : 'right'; // if gpuAcceleration is set to `true` and transform is supported,
  //  we use `translate3d` to apply the position to the popper we
  // automatically use the supported prefixed version if needed

  var prefixedProperty = getSupportedPropertyName('transform'); // now, let's make a step back and look at this code closely (wtf?)
  // If the content of the popper grows once it's been positioned, it
  // may happen that the popper gets misplaced because of the new content
  // overflowing its reference element
  // To avoid this problem, we provide two options (x and y), which allow
  // the consumer to define the offset origin.
  // If we position a popper on top of a reference element, we can set
  // `x` to `top` to make the popper grow towards its top instead of
  // its bottom.

  var left = void 0,
      top = void 0;

  if (sideA === 'bottom') {
    // when offsetParent is <html> the positioning is relative to the bottom of the screen (excluding the scrollbar)
    // and not the bottom of the html element
    if (offsetParent.nodeName === 'HTML') {
      top = -offsetParent.clientHeight + offsets.bottom;
    } else {
      top = -offsetParentRect.height + offsets.bottom;
    }
  } else {
    top = offsets.top;
  }

  if (sideB === 'right') {
    if (offsetParent.nodeName === 'HTML') {
      left = -offsetParent.clientWidth + offsets.right;
    } else {
      left = -offsetParentRect.width + offsets.right;
    }
  } else {
    left = offsets.left;
  }

  if (gpuAcceleration && prefixedProperty) {
    styles[prefixedProperty] = 'translate3d(' + left + 'px, ' + top + 'px, 0)';
    styles[sideA] = 0;
    styles[sideB] = 0;
    styles.willChange = 'transform';
  } else {
    // othwerise, we use the standard `top`, `left`, `bottom` and `right` properties
    var invertTop = sideA === 'bottom' ? -1 : 1;
    var invertLeft = sideB === 'right' ? -1 : 1;
    styles[sideA] = top * invertTop;
    styles[sideB] = left * invertLeft;
    styles.willChange = sideA + ', ' + sideB;
  } // Attributes


  var attributes = {
    'x-placement': data.placement
  }; // Update `data` attributes, styles and arrowStyles

  data.attributes = _extends({}, attributes, data.attributes);
  data.styles = _extends({}, styles, data.styles);
  data.arrowStyles = _extends({}, data.offsets.arrow, data.arrowStyles);
  return data;
}
/**
 * Helper used to know if the given modifier depends from another one.<br />
 * It checks if the needed modifier is listed and enabled.
 * @method
 * @memberof Popper.Utils
 * @param {Array} modifiers - list of modifiers
 * @param {String} requestingName - name of requesting modifier
 * @param {String} requestedName - name of requested modifier
 * @returns {Boolean}
 */


function isModifierRequired(modifiers, requestingName, requestedName) {
  var requesting = find(modifiers, function (_ref) {
    var name = _ref.name;
    return name === requestingName;
  });
  var isRequired = !!requesting && modifiers.some(function (modifier) {
    return modifier.name === requestedName && modifier.enabled && modifier.order < requesting.order;
  });

  if (!isRequired) {
    var _requesting = '`' + requestingName + '`';

    var requested = '`' + requestedName + '`';
    console.warn(requested + ' modifier is required by ' + _requesting + ' modifier in order to work, be sure to include it before ' + _requesting + '!');
  }

  return isRequired;
}
/**
 * @function
 * @memberof Modifiers
 * @argument {Object} data - The data object generated by update method
 * @argument {Object} options - Modifiers configuration and options
 * @returns {Object} The data object, properly modified
 */


function arrow(data, options) {
  var _data$offsets$arrow; // arrow depends on keepTogether in order to work


  if (!isModifierRequired(data.instance.modifiers, 'arrow', 'keepTogether')) {
    return data;
  }

  var arrowElement = options.element; // if arrowElement is a string, suppose it's a CSS selector

  if (typeof arrowElement === 'string') {
    arrowElement = data.instance.popper.querySelector(arrowElement); // if arrowElement is not found, don't run the modifier

    if (!arrowElement) {
      return data;
    }
  } else {
    // if the arrowElement isn't a query selector we must check that the
    // provided DOM node is child of its popper node
    if (!data.instance.popper.contains(arrowElement)) {
      console.warn('WARNING: `arrow.element` must be child of its popper element!');
      return data;
    }
  }

  var placement = data.placement.split('-')[0];
  var _data$offsets = data.offsets,
      popper = _data$offsets.popper,
      reference = _data$offsets.reference;
  var isVertical = ['left', 'right'].indexOf(placement) !== -1;
  var len = isVertical ? 'height' : 'width';
  var sideCapitalized = isVertical ? 'Top' : 'Left';
  var side = sideCapitalized.toLowerCase();
  var altSide = isVertical ? 'left' : 'top';
  var opSide = isVertical ? 'bottom' : 'right';
  var arrowElementSize = getOuterSizes(arrowElement)[len]; //
  // extends keepTogether behavior making sure the popper and its
  // reference have enough pixels in conjunction
  //
  // top/left side

  if (reference[opSide] - arrowElementSize < popper[side]) {
    data.offsets.popper[side] -= popper[side] - (reference[opSide] - arrowElementSize);
  } // bottom/right side


  if (reference[side] + arrowElementSize > popper[opSide]) {
    data.offsets.popper[side] += reference[side] + arrowElementSize - popper[opSide];
  }

  data.offsets.popper = getClientRect(data.offsets.popper); // compute center of the popper

  var center = reference[side] + reference[len] / 2 - arrowElementSize / 2; // Compute the sideValue using the updated popper offsets
  // take popper margin in account because we don't have this info available

  var css = getStyleComputedProperty(data.instance.popper);
  var popperMarginSide = parseFloat(css['margin' + sideCapitalized], 10);
  var popperBorderSide = parseFloat(css['border' + sideCapitalized + 'Width'], 10);
  var sideValue = center - data.offsets.popper[side] - popperMarginSide - popperBorderSide; // prevent arrowElement from being placed not contiguously to its popper

  sideValue = Math.max(Math.min(popper[len] - arrowElementSize, sideValue), 0);
  data.arrowElement = arrowElement;
  data.offsets.arrow = (_data$offsets$arrow = {}, defineProperty(_data$offsets$arrow, side, Math.round(sideValue)), defineProperty(_data$offsets$arrow, altSide, ''), _data$offsets$arrow);
  return data;
}
/**
 * Get the opposite placement variation of the given one
 * @method
 * @memberof Popper.Utils
 * @argument {String} placement variation
 * @returns {String} flipped placement variation
 */


function getOppositeVariation(variation) {
  if (variation === 'end') {
    return 'start';
  } else if (variation === 'start') {
    return 'end';
  }

  return variation;
}
/**
 * List of accepted placements to use as values of the `placement` option.<br />
 * Valid placements are:
 * - `auto`
 * - `top`
 * - `right`
 * - `bottom`
 * - `left`
 *
 * Each placement can have a variation from this list:
 * - `-start`
 * - `-end`
 *
 * Variations are interpreted easily if you think of them as the left to right
 * written languages. Horizontally (`top` and `bottom`), `start` is left and `end`
 * is right.<br />
 * Vertically (`left` and `right`), `start` is top and `end` is bottom.
 *
 * Some valid examples are:
 * - `top-end` (on top of reference, right aligned)
 * - `right-start` (on right of reference, top aligned)
 * - `bottom` (on bottom, centered)
 * - `auto-end` (on the side with more space available, alignment depends by placement)
 *
 * @static
 * @type {Array}
 * @enum {String}
 * @readonly
 * @method placements
 * @memberof Popper
 */


var placements = ['auto-start', 'auto', 'auto-end', 'top-start', 'top', 'top-end', 'right-start', 'right', 'right-end', 'bottom-end', 'bottom', 'bottom-start', 'left-end', 'left', 'left-start']; // Get rid of `auto` `auto-start` and `auto-end`

var validPlacements = placements.slice(3);
/**
 * Given an initial placement, returns all the subsequent placements
 * clockwise (or counter-clockwise).
 *
 * @method
 * @memberof Popper.Utils
 * @argument {String} placement - A valid placement (it accepts variations)
 * @argument {Boolean} counter - Set to true to walk the placements counterclockwise
 * @returns {Array} placements including their variations
 */

function clockwise(placement) {
  var counter = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;
  var index = validPlacements.indexOf(placement);
  var arr = validPlacements.slice(index + 1).concat(validPlacements.slice(0, index));
  return counter ? arr.reverse() : arr;
}

var BEHAVIORS = {
  FLIP: 'flip',
  CLOCKWISE: 'clockwise',
  COUNTERCLOCKWISE: 'counterclockwise'
};
/**
 * @function
 * @memberof Modifiers
 * @argument {Object} data - The data object generated by update method
 * @argument {Object} options - Modifiers configuration and options
 * @returns {Object} The data object, properly modified
 */

function flip(data, options) {
  // if `inner` modifier is enabled, we can't use the `flip` modifier
  if (isModifierEnabled(data.instance.modifiers, 'inner')) {
    return data;
  }

  if (data.flipped && data.placement === data.originalPlacement) {
    // seems like flip is trying to loop, probably there's not enough space on any of the flippable sides
    return data;
  }

  var boundaries = getBoundaries(data.instance.popper, data.instance.reference, options.padding, options.boundariesElement, data.positionFixed);
  var placement = data.placement.split('-')[0];
  var placementOpposite = getOppositePlacement(placement);
  var variation = data.placement.split('-')[1] || '';
  var flipOrder = [];

  switch (options.behavior) {
    case BEHAVIORS.FLIP:
      flipOrder = [placement, placementOpposite];
      break;

    case BEHAVIORS.CLOCKWISE:
      flipOrder = clockwise(placement);
      break;

    case BEHAVIORS.COUNTERCLOCKWISE:
      flipOrder = clockwise(placement, true);
      break;

    default:
      flipOrder = options.behavior;
  }

  flipOrder.forEach(function (step, index) {
    if (placement !== step || flipOrder.length === index + 1) {
      return data;
    }

    placement = data.placement.split('-')[0];
    placementOpposite = getOppositePlacement(placement);
    var popperOffsets = data.offsets.popper;
    var refOffsets = data.offsets.reference; // using floor because the reference offsets may contain decimals we are not going to consider here

    var floor = Math.floor;
    var overlapsRef = placement === 'left' && floor(popperOffsets.right) > floor(refOffsets.left) || placement === 'right' && floor(popperOffsets.left) < floor(refOffsets.right) || placement === 'top' && floor(popperOffsets.bottom) > floor(refOffsets.top) || placement === 'bottom' && floor(popperOffsets.top) < floor(refOffsets.bottom);
    var overflowsLeft = floor(popperOffsets.left) < floor(boundaries.left);
    var overflowsRight = floor(popperOffsets.right) > floor(boundaries.right);
    var overflowsTop = floor(popperOffsets.top) < floor(boundaries.top);
    var overflowsBottom = floor(popperOffsets.bottom) > floor(boundaries.bottom);
    var overflowsBoundaries = placement === 'left' && overflowsLeft || placement === 'right' && overflowsRight || placement === 'top' && overflowsTop || placement === 'bottom' && overflowsBottom; // flip the variation if required

    var isVertical = ['top', 'bottom'].indexOf(placement) !== -1; // flips variation if reference element overflows boundaries

    var flippedVariationByRef = !!options.flipVariations && (isVertical && variation === 'start' && overflowsLeft || isVertical && variation === 'end' && overflowsRight || !isVertical && variation === 'start' && overflowsTop || !isVertical && variation === 'end' && overflowsBottom); // flips variation if popper content overflows boundaries

    var flippedVariationByContent = !!options.flipVariationsByContent && (isVertical && variation === 'start' && overflowsRight || isVertical && variation === 'end' && overflowsLeft || !isVertical && variation === 'start' && overflowsBottom || !isVertical && variation === 'end' && overflowsTop);
    var flippedVariation = flippedVariationByRef || flippedVariationByContent;

    if (overlapsRef || overflowsBoundaries || flippedVariation) {
      // this boolean to detect any flip loop
      data.flipped = true;

      if (overlapsRef || overflowsBoundaries) {
        placement = flipOrder[index + 1];
      }

      if (flippedVariation) {
        variation = getOppositeVariation(variation);
      }

      data.placement = placement + (variation ? '-' + variation : ''); // this object contains `position`, we want to preserve it along with
      // any additional property we may add in the future

      data.offsets.popper = _extends({}, data.offsets.popper, getPopperOffsets(data.instance.popper, data.offsets.reference, data.placement));
      data = runModifiers(data.instance.modifiers, data, 'flip');
    }
  });
  return data;
}
/**
 * @function
 * @memberof Modifiers
 * @argument {Object} data - The data object generated by update method
 * @argument {Object} options - Modifiers configuration and options
 * @returns {Object} The data object, properly modified
 */


function keepTogether(data) {
  var _data$offsets = data.offsets,
      popper = _data$offsets.popper,
      reference = _data$offsets.reference;
  var placement = data.placement.split('-')[0];
  var floor = Math.floor;
  var isVertical = ['top', 'bottom'].indexOf(placement) !== -1;
  var side = isVertical ? 'right' : 'bottom';
  var opSide = isVertical ? 'left' : 'top';
  var measurement = isVertical ? 'width' : 'height';

  if (popper[side] < floor(reference[opSide])) {
    data.offsets.popper[opSide] = floor(reference[opSide]) - popper[measurement];
  }

  if (popper[opSide] > floor(reference[side])) {
    data.offsets.popper[opSide] = floor(reference[side]);
  }

  return data;
}
/**
 * Converts a string containing value + unit into a px value number
 * @function
 * @memberof {modifiers~offset}
 * @private
 * @argument {String} str - Value + unit string
 * @argument {String} measurement - `height` or `width`
 * @argument {Object} popperOffsets
 * @argument {Object} referenceOffsets
 * @returns {Number|String}
 * Value in pixels, or original string if no values were extracted
 */


function toValue(str, measurement, popperOffsets, referenceOffsets) {
  // separate value from unit
  var split = str.match(/((?:\-|\+)?\d*\.?\d*)(.*)/);
  var value = +split[1];
  var unit = split[2]; // If it's not a number it's an operator, I guess

  if (!value) {
    return str;
  }

  if (unit.indexOf('%') === 0) {
    var element = void 0;

    switch (unit) {
      case '%p':
        element = popperOffsets;
        break;

      case '%':
      case '%r':
      default:
        element = referenceOffsets;
    }

    var rect = getClientRect(element);
    return rect[measurement] / 100 * value;
  } else if (unit === 'vh' || unit === 'vw') {
    // if is a vh or vw, we calculate the size based on the viewport
    var size = void 0;

    if (unit === 'vh') {
      size = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);
    } else {
      size = Math.max(document.documentElement.clientWidth, window.innerWidth || 0);
    }

    return size / 100 * value;
  } else {
    // if is an explicit pixel unit, we get rid of the unit and keep the value
    // if is an implicit unit, it's px, and we return just the value
    return value;
  }
}
/**
 * Parse an `offset` string to extrapolate `x` and `y` numeric offsets.
 * @function
 * @memberof {modifiers~offset}
 * @private
 * @argument {String} offset
 * @argument {Object} popperOffsets
 * @argument {Object} referenceOffsets
 * @argument {String} basePlacement
 * @returns {Array} a two cells array with x and y offsets in numbers
 */


function parseOffset(offset, popperOffsets, referenceOffsets, basePlacement) {
  var offsets = [0, 0]; // Use height if placement is left or right and index is 0 otherwise use width
  // in this way the first offset will use an axis and the second one
  // will use the other one

  var useHeight = ['right', 'left'].indexOf(basePlacement) !== -1; // Split the offset string to obtain a list of values and operands
  // The regex addresses values with the plus or minus sign in front (+10, -20, etc)

  var fragments = offset.split(/(\+|\-)/).map(function (frag) {
    return frag.trim();
  }); // Detect if the offset string contains a pair of values or a single one
  // they could be separated by comma or space

  var divider = fragments.indexOf(find(fragments, function (frag) {
    return frag.search(/,|\s/) !== -1;
  }));

  if (fragments[divider] && fragments[divider].indexOf(',') === -1) {
    console.warn('Offsets separated by white space(s) are deprecated, use a comma (,) instead.');
  } // If divider is found, we divide the list of values and operands to divide
  // them by ofset X and Y.


  var splitRegex = /\s*,\s*|\s+/;
  var ops = divider !== -1 ? [fragments.slice(0, divider).concat([fragments[divider].split(splitRegex)[0]]), [fragments[divider].split(splitRegex)[1]].concat(fragments.slice(divider + 1))] : [fragments]; // Convert the values with units to absolute pixels to allow our computations

  ops = ops.map(function (op, index) {
    // Most of the units rely on the orientation of the popper
    var measurement = (index === 1 ? !useHeight : useHeight) ? 'height' : 'width';
    var mergeWithPrevious = false;
    return op // This aggregates any `+` or `-` sign that aren't considered operators
    // e.g.: 10 + +5 => [10, +, +5]
    .reduce(function (a, b) {
      if (a[a.length - 1] === '' && ['+', '-'].indexOf(b) !== -1) {
        a[a.length - 1] = b;
        mergeWithPrevious = true;
        return a;
      } else if (mergeWithPrevious) {
        a[a.length - 1] += b;
        mergeWithPrevious = false;
        return a;
      } else {
        return a.concat(b);
      }
    }, []) // Here we convert the string values into number values (in px)
    .map(function (str) {
      return toValue(str, measurement, popperOffsets, referenceOffsets);
    });
  }); // Loop trough the offsets arrays and execute the operations

  ops.forEach(function (op, index) {
    op.forEach(function (frag, index2) {
      if (isNumeric(frag)) {
        offsets[index] += frag * (op[index2 - 1] === '-' ? -1 : 1);
      }
    });
  });
  return offsets;
}
/**
 * @function
 * @memberof Modifiers
 * @argument {Object} data - The data object generated by update method
 * @argument {Object} options - Modifiers configuration and options
 * @argument {Number|String} options.offset=0
 * The offset value as described in the modifier description
 * @returns {Object} The data object, properly modified
 */


function offset(data, _ref) {
  var offset = _ref.offset;
  var placement = data.placement,
      _data$offsets = data.offsets,
      popper = _data$offsets.popper,
      reference = _data$offsets.reference;
  var basePlacement = placement.split('-')[0];
  var offsets = void 0;

  if (isNumeric(+offset)) {
    offsets = [+offset, 0];
  } else {
    offsets = parseOffset(offset, popper, reference, basePlacement);
  }

  if (basePlacement === 'left') {
    popper.top += offsets[0];
    popper.left -= offsets[1];
  } else if (basePlacement === 'right') {
    popper.top += offsets[0];
    popper.left += offsets[1];
  } else if (basePlacement === 'top') {
    popper.left += offsets[0];
    popper.top -= offsets[1];
  } else if (basePlacement === 'bottom') {
    popper.left += offsets[0];
    popper.top += offsets[1];
  }

  data.popper = popper;
  return data;
}
/**
 * @function
 * @memberof Modifiers
 * @argument {Object} data - The data object generated by `update` method
 * @argument {Object} options - Modifiers configuration and options
 * @returns {Object} The data object, properly modified
 */


function preventOverflow(data, options) {
  var boundariesElement = options.boundariesElement || getOffsetParent(data.instance.popper); // If offsetParent is the reference element, we really want to
  // go one step up and use the next offsetParent as reference to
  // avoid to make this modifier completely useless and look like broken

  if (data.instance.reference === boundariesElement) {
    boundariesElement = getOffsetParent(boundariesElement);
  } // NOTE: DOM access here
  // resets the popper's position so that the document size can be calculated excluding
  // the size of the popper element itself


  var transformProp = getSupportedPropertyName('transform');
  var popperStyles = data.instance.popper.style; // assignment to help minification

  var top = popperStyles.top,
      left = popperStyles.left,
      transform = popperStyles[transformProp];
  popperStyles.top = '';
  popperStyles.left = '';
  popperStyles[transformProp] = '';
  var boundaries = getBoundaries(data.instance.popper, data.instance.reference, options.padding, boundariesElement, data.positionFixed); // NOTE: DOM access here
  // restores the original style properties after the offsets have been computed

  popperStyles.top = top;
  popperStyles.left = left;
  popperStyles[transformProp] = transform;
  options.boundaries = boundaries;
  var order = options.priority;
  var popper = data.offsets.popper;
  var check = {
    primary: function primary(placement) {
      var value = popper[placement];

      if (popper[placement] < boundaries[placement] && !options.escapeWithReference) {
        value = Math.max(popper[placement], boundaries[placement]);
      }

      return defineProperty({}, placement, value);
    },
    secondary: function secondary(placement) {
      var mainSide = placement === 'right' ? 'left' : 'top';
      var value = popper[mainSide];

      if (popper[placement] > boundaries[placement] && !options.escapeWithReference) {
        value = Math.min(popper[mainSide], boundaries[placement] - (placement === 'right' ? popper.width : popper.height));
      }

      return defineProperty({}, mainSide, value);
    }
  };
  order.forEach(function (placement) {
    var side = ['left', 'top'].indexOf(placement) !== -1 ? 'primary' : 'secondary';
    popper = _extends({}, popper, check[side](placement));
  });
  data.offsets.popper = popper;
  return data;
}
/**
 * @function
 * @memberof Modifiers
 * @argument {Object} data - The data object generated by `update` method
 * @argument {Object} options - Modifiers configuration and options
 * @returns {Object} The data object, properly modified
 */


function shift(data) {
  var placement = data.placement;
  var basePlacement = placement.split('-')[0];
  var shiftvariation = placement.split('-')[1]; // if shift shiftvariation is specified, run the modifier

  if (shiftvariation) {
    var _data$offsets = data.offsets,
        reference = _data$offsets.reference,
        popper = _data$offsets.popper;
    var isVertical = ['bottom', 'top'].indexOf(basePlacement) !== -1;
    var side = isVertical ? 'left' : 'top';
    var measurement = isVertical ? 'width' : 'height';
    var shiftOffsets = {
      start: defineProperty({}, side, reference[side]),
      end: defineProperty({}, side, reference[side] + reference[measurement] - popper[measurement])
    };
    data.offsets.popper = _extends({}, popper, shiftOffsets[shiftvariation]);
  }

  return data;
}
/**
 * @function
 * @memberof Modifiers
 * @argument {Object} data - The data object generated by update method
 * @argument {Object} options - Modifiers configuration and options
 * @returns {Object} The data object, properly modified
 */


function hide(data) {
  if (!isModifierRequired(data.instance.modifiers, 'hide', 'preventOverflow')) {
    return data;
  }

  var refRect = data.offsets.reference;
  var bound = find(data.instance.modifiers, function (modifier) {
    return modifier.name === 'preventOverflow';
  }).boundaries;

  if (refRect.bottom < bound.top || refRect.left > bound.right || refRect.top > bound.bottom || refRect.right < bound.left) {
    // Avoid unnecessary DOM access if visibility hasn't changed
    if (data.hide === true) {
      return data;
    }

    data.hide = true;
    data.attributes['x-out-of-boundaries'] = '';
  } else {
    // Avoid unnecessary DOM access if visibility hasn't changed
    if (data.hide === false) {
      return data;
    }

    data.hide = false;
    data.attributes['x-out-of-boundaries'] = false;
  }

  return data;
}
/**
 * @function
 * @memberof Modifiers
 * @argument {Object} data - The data object generated by `update` method
 * @argument {Object} options - Modifiers configuration and options
 * @returns {Object} The data object, properly modified
 */


function inner(data) {
  var placement = data.placement;
  var basePlacement = placement.split('-')[0];
  var _data$offsets = data.offsets,
      popper = _data$offsets.popper,
      reference = _data$offsets.reference;
  var isHoriz = ['left', 'right'].indexOf(basePlacement) !== -1;
  var subtractLength = ['top', 'left'].indexOf(basePlacement) === -1;
  popper[isHoriz ? 'left' : 'top'] = reference[basePlacement] - (subtractLength ? popper[isHoriz ? 'width' : 'height'] : 0);
  data.placement = getOppositePlacement(placement);
  data.offsets.popper = getClientRect(popper);
  return data;
}
/**
 * Modifier function, each modifier can have a function of this type assigned
 * to its `fn` property.<br />
 * These functions will be called on each update, this means that you must
 * make sure they are performant enough to avoid performance bottlenecks.
 *
 * @function ModifierFn
 * @argument {dataObject} data - The data object generated by `update` method
 * @argument {Object} options - Modifiers configuration and options
 * @returns {dataObject} The data object, properly modified
 */

/**
 * Modifiers are plugins used to alter the behavior of your poppers.<br />
 * Popper.js uses a set of 9 modifiers to provide all the basic functionalities
 * needed by the library.
 *
 * Usually you don't want to override the `order`, `fn` and `onLoad` props.
 * All the other properties are configurations that could be tweaked.
 * @namespace modifiers
 */


var modifiers = {
  /**
   * Modifier used to shift the popper on the start or end of its reference
   * element.<br />
   * It will read the variation of the `placement` property.<br />
   * It can be one either `-end` or `-start`.
   * @memberof modifiers
   * @inner
   */
  shift: {
    /** @prop {number} order=100 - Index used to define the order of execution */
    order: 100,

    /** @prop {Boolean} enabled=true - Whether the modifier is enabled or not */
    enabled: true,

    /** @prop {ModifierFn} */
    fn: shift
  },

  /**
   * The `offset` modifier can shift your popper on both its axis.
   *
   * It accepts the following units:
   * - `px` or unit-less, interpreted as pixels
   * - `%` or `%r`, percentage relative to the length of the reference element
   * - `%p`, percentage relative to the length of the popper element
   * - `vw`, CSS viewport width unit
   * - `vh`, CSS viewport height unit
   *
   * For length is intended the main axis relative to the placement of the popper.<br />
   * This means that if the placement is `top` or `bottom`, the length will be the
   * `width`. In case of `left` or `right`, it will be the `height`.
   *
   * You can provide a single value (as `Number` or `String`), or a pair of values
   * as `String` divided by a comma or one (or more) white spaces.<br />
   * The latter is a deprecated method because it leads to confusion and will be
   * removed in v2.<br />
   * Additionally, it accepts additions and subtractions between different units.
   * Note that multiplications and divisions aren't supported.
   *
   * Valid examples are:
   * ```
   * 10
   * '10%'
   * '10, 10'
   * '10%, 10'
   * '10 + 10%'
   * '10 - 5vh + 3%'
   * '-10px + 5vh, 5px - 6%'
   * ```
   * > **NB**: If you desire to apply offsets to your poppers in a way that may make them overlap
   * > with their reference element, unfortunately, you will have to disable the `flip` modifier.
   * > You can read more on this at this [issue](https://github.com/FezVrasta/popper.js/issues/373).
   *
   * @memberof modifiers
   * @inner
   */
  offset: {
    /** @prop {number} order=200 - Index used to define the order of execution */
    order: 200,

    /** @prop {Boolean} enabled=true - Whether the modifier is enabled or not */
    enabled: true,

    /** @prop {ModifierFn} */
    fn: offset,

    /** @prop {Number|String} offset=0
     * The offset value as described in the modifier description
     */
    offset: 0
  },

  /**
   * Modifier used to prevent the popper from being positioned outside the boundary.
   *
   * A scenario exists where the reference itself is not within the boundaries.<br />
   * We can say it has "escaped the boundaries" — or just "escaped".<br />
   * In this case we need to decide whether the popper should either:
   *
   * - detach from the reference and remain "trapped" in the boundaries, or
   * - if it should ignore the boundary and "escape with its reference"
   *
   * When `escapeWithReference` is set to`true` and reference is completely
   * outside its boundaries, the popper will overflow (or completely leave)
   * the boundaries in order to remain attached to the edge of the reference.
   *
   * @memberof modifiers
   * @inner
   */
  preventOverflow: {
    /** @prop {number} order=300 - Index used to define the order of execution */
    order: 300,

    /** @prop {Boolean} enabled=true - Whether the modifier is enabled or not */
    enabled: true,

    /** @prop {ModifierFn} */
    fn: preventOverflow,

    /**
     * @prop {Array} [priority=['left','right','top','bottom']]
     * Popper will try to prevent overflow following these priorities by default,
     * then, it could overflow on the left and on top of the `boundariesElement`
     */
    priority: ['left', 'right', 'top', 'bottom'],

    /**
     * @prop {number} padding=5
     * Amount of pixel used to define a minimum distance between the boundaries
     * and the popper. This makes sure the popper always has a little padding
     * between the edges of its container
     */
    padding: 5,

    /**
     * @prop {String|HTMLElement} boundariesElement='scrollParent'
     * Boundaries used by the modifier. Can be `scrollParent`, `window`,
     * `viewport` or any DOM element.
     */
    boundariesElement: 'scrollParent'
  },

  /**
   * Modifier used to make sure the reference and its popper stay near each other
   * without leaving any gap between the two. Especially useful when the arrow is
   * enabled and you want to ensure that it points to its reference element.
   * It cares only about the first axis. You can still have poppers with margin
   * between the popper and its reference element.
   * @memberof modifiers
   * @inner
   */
  keepTogether: {
    /** @prop {number} order=400 - Index used to define the order of execution */
    order: 400,

    /** @prop {Boolean} enabled=true - Whether the modifier is enabled or not */
    enabled: true,

    /** @prop {ModifierFn} */
    fn: keepTogether
  },

  /**
   * This modifier is used to move the `arrowElement` of the popper to make
   * sure it is positioned between the reference element and its popper element.
   * It will read the outer size of the `arrowElement` node to detect how many
   * pixels of conjunction are needed.
   *
   * It has no effect if no `arrowElement` is provided.
   * @memberof modifiers
   * @inner
   */
  arrow: {
    /** @prop {number} order=500 - Index used to define the order of execution */
    order: 500,

    /** @prop {Boolean} enabled=true - Whether the modifier is enabled or not */
    enabled: true,

    /** @prop {ModifierFn} */
    fn: arrow,

    /** @prop {String|HTMLElement} element='[x-arrow]' - Selector or node used as arrow */
    element: '[x-arrow]'
  },

  /**
   * Modifier used to flip the popper's placement when it starts to overlap its
   * reference element.
   *
   * Requires the `preventOverflow` modifier before it in order to work.
   *
   * **NOTE:** this modifier will interrupt the current update cycle and will
   * restart it if it detects the need to flip the placement.
   * @memberof modifiers
   * @inner
   */
  flip: {
    /** @prop {number} order=600 - Index used to define the order of execution */
    order: 600,

    /** @prop {Boolean} enabled=true - Whether the modifier is enabled or not */
    enabled: true,

    /** @prop {ModifierFn} */
    fn: flip,

    /**
     * @prop {String|Array} behavior='flip'
     * The behavior used to change the popper's placement. It can be one of
     * `flip`, `clockwise`, `counterclockwise` or an array with a list of valid
     * placements (with optional variations)
     */
    behavior: 'flip',

    /**
     * @prop {number} padding=5
     * The popper will flip if it hits the edges of the `boundariesElement`
     */
    padding: 5,

    /**
     * @prop {String|HTMLElement} boundariesElement='viewport'
     * The element which will define the boundaries of the popper position.
     * The popper will never be placed outside of the defined boundaries
     * (except if `keepTogether` is enabled)
     */
    boundariesElement: 'viewport',

    /**
     * @prop {Boolean} flipVariations=false
     * The popper will switch placement variation between `-start` and `-end` when
     * the reference element overlaps its boundaries.
     *
     * The original placement should have a set variation.
     */
    flipVariations: false,

    /**
     * @prop {Boolean} flipVariationsByContent=false
     * The popper will switch placement variation between `-start` and `-end` when
     * the popper element overlaps its reference boundaries.
     *
     * The original placement should have a set variation.
     */
    flipVariationsByContent: false
  },

  /**
   * Modifier used to make the popper flow toward the inner of the reference element.
   * By default, when this modifier is disabled, the popper will be placed outside
   * the reference element.
   * @memberof modifiers
   * @inner
   */
  inner: {
    /** @prop {number} order=700 - Index used to define the order of execution */
    order: 700,

    /** @prop {Boolean} enabled=false - Whether the modifier is enabled or not */
    enabled: false,

    /** @prop {ModifierFn} */
    fn: inner
  },

  /**
   * Modifier used to hide the popper when its reference element is outside of the
   * popper boundaries. It will set a `x-out-of-boundaries` attribute which can
   * be used to hide with a CSS selector the popper when its reference is
   * out of boundaries.
   *
   * Requires the `preventOverflow` modifier before it in order to work.
   * @memberof modifiers
   * @inner
   */
  hide: {
    /** @prop {number} order=800 - Index used to define the order of execution */
    order: 800,

    /** @prop {Boolean} enabled=true - Whether the modifier is enabled or not */
    enabled: true,

    /** @prop {ModifierFn} */
    fn: hide
  },

  /**
   * Computes the style that will be applied to the popper element to gets
   * properly positioned.
   *
   * Note that this modifier will not touch the DOM, it just prepares the styles
   * so that `applyStyle` modifier can apply it. This separation is useful
   * in case you need to replace `applyStyle` with a custom implementation.
   *
   * This modifier has `850` as `order` value to maintain backward compatibility
   * with previous versions of Popper.js. Expect the modifiers ordering method
   * to change in future major versions of the library.
   *
   * @memberof modifiers
   * @inner
   */
  computeStyle: {
    /** @prop {number} order=850 - Index used to define the order of execution */
    order: 850,

    /** @prop {Boolean} enabled=true - Whether the modifier is enabled or not */
    enabled: true,

    /** @prop {ModifierFn} */
    fn: computeStyle,

    /**
     * @prop {Boolean} gpuAcceleration=true
     * If true, it uses the CSS 3D transformation to position the popper.
     * Otherwise, it will use the `top` and `left` properties
     */
    gpuAcceleration: true,

    /**
     * @prop {string} [x='bottom']
     * Where to anchor the X axis (`bottom` or `top`). AKA X offset origin.
     * Change this if your popper should grow in a direction different from `bottom`
     */
    x: 'bottom',

    /**
     * @prop {string} [x='left']
     * Where to anchor the Y axis (`left` or `right`). AKA Y offset origin.
     * Change this if your popper should grow in a direction different from `right`
     */
    y: 'right'
  },

  /**
   * Applies the computed styles to the popper element.
   *
   * All the DOM manipulations are limited to this modifier. This is useful in case
   * you want to integrate Popper.js inside a framework or view library and you
   * want to delegate all the DOM manipulations to it.
   *
   * Note that if you disable this modifier, you must make sure the popper element
   * has its position set to `absolute` before Popper.js can do its work!
   *
   * Just disable this modifier and define your own to achieve the desired effect.
   *
   * @memberof modifiers
   * @inner
   */
  applyStyle: {
    /** @prop {number} order=900 - Index used to define the order of execution */
    order: 900,

    /** @prop {Boolean} enabled=true - Whether the modifier is enabled or not */
    enabled: true,

    /** @prop {ModifierFn} */
    fn: applyStyle,

    /** @prop {Function} */
    onLoad: applyStyleOnLoad,

    /**
     * @deprecated since version 1.10.0, the property moved to `computeStyle` modifier
     * @prop {Boolean} gpuAcceleration=true
     * If true, it uses the CSS 3D transformation to position the popper.
     * Otherwise, it will use the `top` and `left` properties
     */
    gpuAcceleration: undefined
  }
};
/**
 * The `dataObject` is an object containing all the information used by Popper.js.
 * This object is passed to modifiers and to the `onCreate` and `onUpdate` callbacks.
 * @name dataObject
 * @property {Object} data.instance The Popper.js instance
 * @property {String} data.placement Placement applied to popper
 * @property {String} data.originalPlacement Placement originally defined on init
 * @property {Boolean} data.flipped True if popper has been flipped by flip modifier
 * @property {Boolean} data.hide True if the reference element is out of boundaries, useful to know when to hide the popper
 * @property {HTMLElement} data.arrowElement Node used as arrow by arrow modifier
 * @property {Object} data.styles Any CSS property defined here will be applied to the popper. It expects the JavaScript nomenclature (eg. `marginBottom`)
 * @property {Object} data.arrowStyles Any CSS property defined here will be applied to the popper arrow. It expects the JavaScript nomenclature (eg. `marginBottom`)
 * @property {Object} data.boundaries Offsets of the popper boundaries
 * @property {Object} data.offsets The measurements of popper, reference and arrow elements
 * @property {Object} data.offsets.popper `top`, `left`, `width`, `height` values
 * @property {Object} data.offsets.reference `top`, `left`, `width`, `height` values
 * @property {Object} data.offsets.arrow] `top` and `left` offsets, only one of them will be different from 0
 */

/**
 * Default options provided to Popper.js constructor.<br />
 * These can be overridden using the `options` argument of Popper.js.<br />
 * To override an option, simply pass an object with the same
 * structure of the `options` object, as the 3rd argument. For example:
 * ```
 * new Popper(ref, pop, {
 *   modifiers: {
 *     preventOverflow: { enabled: false }
 *   }
 * })
 * ```
 * @type {Object}
 * @static
 * @memberof Popper
 */

var Defaults = {
  /**
   * Popper's placement.
   * @prop {Popper.placements} placement='bottom'
   */
  placement: 'bottom',

  /**
   * Set this to true if you want popper to position it self in 'fixed' mode
   * @prop {Boolean} positionFixed=false
   */
  positionFixed: false,

  /**
   * Whether events (resize, scroll) are initially enabled.
   * @prop {Boolean} eventsEnabled=true
   */
  eventsEnabled: true,

  /**
   * Set to true if you want to automatically remove the popper when
   * you call the `destroy` method.
   * @prop {Boolean} removeOnDestroy=false
   */
  removeOnDestroy: false,

  /**
   * Callback called when the popper is created.<br />
   * By default, it is set to no-op.<br />
   * Access Popper.js instance with `data.instance`.
   * @prop {onCreate}
   */
  onCreate: function onCreate() {},

  /**
   * Callback called when the popper is updated. This callback is not called
   * on the initialization/creation of the popper, but only on subsequent
   * updates.<br />
   * By default, it is set to no-op.<br />
   * Access Popper.js instance with `data.instance`.
   * @prop {onUpdate}
   */
  onUpdate: function onUpdate() {},

  /**
   * List of modifiers used to modify the offsets before they are applied to the popper.
   * They provide most of the functionalities of Popper.js.
   * @prop {modifiers}
   */
  modifiers: modifiers
};
/**
 * @callback onCreate
 * @param {dataObject} data
 */

/**
 * @callback onUpdate
 * @param {dataObject} data
 */
// Utils
// Methods

var Popper = function () {
  /**
   * Creates a new Popper.js instance.
   * @class Popper
   * @param {Element|referenceObject} reference - The reference element used to position the popper
   * @param {Element} popper - The HTML / XML element used as the popper
   * @param {Object} options - Your custom options to override the ones defined in [Defaults](#defaults)
   * @return {Object} instance - The generated Popper.js instance
   */
  function Popper(reference, popper) {
    var _this = this;

    var options = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : {};
    classCallCheck(this, Popper);

    this.scheduleUpdate = function () {
      return requestAnimationFrame(_this.update);
    }; // make update() debounced, so that it only runs at most once-per-tick


    this.update = debounce(this.update.bind(this)); // with {} we create a new object with the options inside it

    this.options = _extends({}, Popper.Defaults, options); // init state

    this.state = {
      isDestroyed: false,
      isCreated: false,
      scrollParents: []
    }; // get reference and popper elements (allow jQuery wrappers)

    this.reference = reference && reference.jquery ? reference[0] : reference;
    this.popper = popper && popper.jquery ? popper[0] : popper; // Deep merge modifiers options

    this.options.modifiers = {};
    Object.keys(_extends({}, Popper.Defaults.modifiers, options.modifiers)).forEach(function (name) {
      _this.options.modifiers[name] = _extends({}, Popper.Defaults.modifiers[name] || {}, options.modifiers ? options.modifiers[name] : {});
    }); // Refactoring modifiers' list (Object => Array)

    this.modifiers = Object.keys(this.options.modifiers).map(function (name) {
      return _extends({
        name: name
      }, _this.options.modifiers[name]);
    }) // sort the modifiers by order
    .sort(function (a, b) {
      return a.order - b.order;
    }); // modifiers have the ability to execute arbitrary code when Popper.js get inited
    // such code is executed in the same order of its modifier
    // they could add new properties to their options configuration
    // BE AWARE: don't add options to `options.modifiers.name` but to `modifierOptions`!

    this.modifiers.forEach(function (modifierOptions) {
      if (modifierOptions.enabled && isFunction(modifierOptions.onLoad)) {
        modifierOptions.onLoad(_this.reference, _this.popper, _this.options, modifierOptions, _this.state);
      }
    }); // fire the first update to position the popper in the right place

    this.update();
    var eventsEnabled = this.options.eventsEnabled;

    if (eventsEnabled) {
      // setup event listeners, they will take care of update the position in specific situations
      this.enableEventListeners();
    }

    this.state.eventsEnabled = eventsEnabled;
  } // We can't use class properties because they don't get listed in the
  // class prototype and break stuff like Sinon stubs


  createClass(Popper, [{
    key: 'update',
    value: function update$$1() {
      return update.call(this);
    }
  }, {
    key: 'destroy',
    value: function destroy$$1() {
      return destroy.call(this);
    }
  }, {
    key: 'enableEventListeners',
    value: function enableEventListeners$$1() {
      return enableEventListeners.call(this);
    }
  }, {
    key: 'disableEventListeners',
    value: function disableEventListeners$$1() {
      return disableEventListeners.call(this);
    }
    /**
     * Schedules an update. It will run on the next UI update available.
     * @method scheduleUpdate
     * @memberof Popper
     */

    /**
     * Collection of utilities useful when writing custom modifiers.
     * Starting from version 1.7, this method is available only if you
     * include `popper-utils.js` before `popper.js`.
     *
     * **DEPRECATION**: This way to access PopperUtils is deprecated
     * and will be removed in v2! Use the PopperUtils module directly instead.
     * Due to the high instability of the methods contained in Utils, we can't
     * guarantee them to follow semver. Use them at your own risk!
     * @static
     * @private
     * @type {Object}
     * @deprecated since version 1.8
     * @member Utils
     * @memberof Popper
     */

  }]);
  return Popper;
}();
/**
 * The `referenceObject` is an object that provides an interface compatible with Popper.js
 * and lets you use it as replacement of a real DOM node.<br />
 * You can use this method to position a popper relatively to a set of coordinates
 * in case you don't have a DOM node to use as reference.
 *
 * ```
 * new Popper(referenceObject, popperNode);
 * ```
 *
 * NB: This feature isn't supported in Internet Explorer 10.
 * @name referenceObject
 * @property {Function} data.getBoundingClientRect
 * A function that returns a set of coordinates compatible with the native `getBoundingClientRect` method.
 * @property {number} data.clientWidth
 * An ES6 getter that will return the width of the virtual reference element.
 * @property {number} data.clientHeight
 * An ES6 getter that will return the height of the virtual reference element.
 */


Popper.Utils = (typeof window !== 'undefined' ? window : global).PopperUtils;
Popper.placements = placements;
Popper.Defaults = Defaults;
/* harmony default export */ __webpack_exports__["a"] = (Popper);
/* WEBPACK VAR INJECTION */}.call(this, __webpack_require__(20)))

/***/ }),
/* 10 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = createChainableTypeChecker;
/**
 * Copyright 2013-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
// Mostly taken from ReactPropTypes.

function createChainableTypeChecker(validate) {
  function checkType(isRequired, props, propName, componentName, location, propFullName) {
    var componentNameSafe = componentName || '<<anonymous>>';
    var propFullNameSafe = propFullName || propName;

    if (props[propName] == null) {
      if (isRequired) {
        return new Error('Required ' + location + ' `' + propFullNameSafe + '` was not specified ' + ('in `' + componentNameSafe + '`.'));
      }

      return null;
    }

    for (var _len = arguments.length, args = Array(_len > 6 ? _len - 6 : 0), _key = 6; _key < _len; _key++) {
      args[_key - 6] = arguments[_key];
    }

    return validate.apply(undefined, [props, propName, componentNameSafe, location, propFullNameSafe].concat(args));
  }

  var chainedCheckType = checkType.bind(null, false);
  chainedCheckType.isRequired = checkType.bind(null, true);
  return chainedCheckType;
}

module.exports = exports['default'];

/***/ }),
/* 11 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


exports.__esModule = true;
exports.default = forwardRef;

var _react = _interopRequireDefault(__webpack_require__(1));

function _interopRequireDefault(obj) {
  return obj && obj.__esModule ? obj : {
    default: obj
  };
}

function forwardRef(renderFn, _temp) {
  var _ref = _temp === void 0 ? {} : _temp,
      propTypes = _ref.propTypes,
      defaultProps = _ref.defaultProps,
      _ref$allowFallback = _ref.allowFallback,
      allowFallback = _ref$allowFallback === void 0 ? false : _ref$allowFallback,
      _ref$displayName = _ref.displayName,
      displayName = _ref$displayName === void 0 ? renderFn.name || renderFn.displayName : _ref$displayName;

  var render = function render(props, ref) {
    return renderFn(props, ref);
  };

  return Object.assign(_react.default.forwardRef || !allowFallback ? _react.default.forwardRef(render) : function (props) {
    return render(props, null);
  }, {
    displayName: displayName,
    propTypes: propTypes,
    defaultProps: defaultProps
  });
}

/***/ }),
/* 12 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (c) 2013-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */


var ReactPropTypesSecret = __webpack_require__(13);

function emptyFunction() {}

function emptyFunctionWithReset() {}

emptyFunctionWithReset.resetWarningCache = emptyFunction;

module.exports = function () {
  function shim(props, propName, componentName, location, propFullName, secret) {
    if (secret === ReactPropTypesSecret) {
      // It is still safe when called from React.
      return;
    }

    var err = new Error('Calling PropTypes validators directly is not supported by the `prop-types` package. ' + 'Use PropTypes.checkPropTypes() to call them. ' + 'Read more at http://fb.me/use-check-prop-types');
    err.name = 'Invariant Violation';
    throw err;
  }

  ;
  shim.isRequired = shim;

  function getShim() {
    return shim;
  }

  ; // Important!
  // Keep this list in sync with production version in `./factoryWithTypeCheckers.js`.

  var ReactPropTypes = {
    array: shim,
    bool: shim,
    func: shim,
    number: shim,
    object: shim,
    string: shim,
    symbol: shim,
    any: shim,
    arrayOf: getShim,
    element: shim,
    elementType: shim,
    instanceOf: getShim,
    node: shim,
    objectOf: getShim,
    oneOf: getShim,
    oneOfType: getShim,
    shape: getShim,
    exact: getShim,
    checkPropTypes: emptyFunctionWithReset,
    resetWarningCache: emptyFunction
  };
  ReactPropTypes.PropTypes = ReactPropTypes;
  return ReactPropTypes;
};

/***/ }),
/* 13 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (c) 2013-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */


var ReactPropTypesSecret = 'SECRET_DO_NOT_PASS_THIS_OR_YOU_WILL_BE_FIRED';
module.exports = ReactPropTypesSecret;

/***/ }),
/* 14 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) {
  return typeof obj;
} : function (obj) {
  return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};

var _react = __webpack_require__(1);

var _react2 = _interopRequireDefault(_react);

var _createChainableTypeChecker = __webpack_require__(10);

var _createChainableTypeChecker2 = _interopRequireDefault(_createChainableTypeChecker);

function _interopRequireDefault(obj) {
  return obj && obj.__esModule ? obj : {
    default: obj
  };
}

function validate(props, propName, componentName, location, propFullName) {
  var propValue = props[propName];
  var propType = typeof propValue === 'undefined' ? 'undefined' : _typeof(propValue);

  if (_react2.default.isValidElement(propValue)) {
    return new Error('Invalid ' + location + ' `' + propFullName + '` of type ReactElement ' + ('supplied to `' + componentName + '`, expected a ReactComponent or a ') + 'DOMElement. You can usually obtain a ReactComponent or DOMElement ' + 'from a ReactElement by attaching a ref to it.');
  }

  if ((propType !== 'object' || typeof propValue.render !== 'function') && propValue.nodeType !== 1) {
    return new Error('Invalid ' + location + ' `' + propFullName + '` of value `' + propValue + '` ' + ('supplied to `' + componentName + '`, expected a ReactComponent or a ') + 'DOMElement.');
  }

  return null;
}

exports.default = (0, _createChainableTypeChecker2.default)(validate);
module.exports = exports['default'];

/***/ }),
/* 15 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = deprecated;

var _warning = __webpack_require__(16);

var _warning2 = _interopRequireDefault(_warning);

function _interopRequireDefault(obj) {
  return obj && obj.__esModule ? obj : {
    default: obj
  };
}

var warned = {};

function deprecated(validator, reason) {
  return function validate(props, propName, componentName, location, propFullName) {
    var componentNameSafe = componentName || '<<anonymous>>';
    var propFullNameSafe = propFullName || propName;

    if (props[propName] != null) {
      var messageKey = componentName + '.' + propName;
      (0, _warning2.default)(warned[messageKey], 'The ' + location + ' `' + propFullNameSafe + '` of ' + ('`' + componentNameSafe + '` is deprecated. ' + reason + '.'));
      warned[messageKey] = true;
    }

    for (var _len = arguments.length, args = Array(_len > 5 ? _len - 5 : 0), _key = 5; _key < _len; _key++) {
      args[_key - 5] = arguments[_key];
    }

    return validator.apply(undefined, [props, propName, componentName, location, propFullName].concat(args));
  };
}
/* eslint-disable no-underscore-dangle */


function _resetWarned() {
  warned = {};
}

deprecated._resetWarned = _resetWarned;
/* eslint-enable no-underscore-dangle */

module.exports = exports['default'];

/***/ }),
/* 16 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright 2014-2015, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

/**
 * Similar to invariant but only logs a warning if the condition is not met.
 * This can be used to log issues in development environments in critical
 * paths. Removing the logging code for production environments will keep the
 * same logic and follow the same code paths.
 */

var warning = function () {};

if (false) {}

module.exports = warning;

/***/ }),
/* 17 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _react = __webpack_require__(1);

var _react2 = _interopRequireDefault(_react);

var _reactIs = __webpack_require__(18);

var _createChainableTypeChecker = __webpack_require__(10);

var _createChainableTypeChecker2 = _interopRequireDefault(_createChainableTypeChecker);

function _interopRequireDefault(obj) {
  return obj && obj.__esModule ? obj : {
    default: obj
  };
}

function elementType(props, propName, componentName, location, propFullName) {
  var propValue = props[propName];

  if (_react2.default.isValidElement(propValue)) {
    return new Error('Invalid ' + location + ' `' + propFullName + '` of type ReactElement ' + ('supplied to `' + componentName + '`,expected an element type (a string ') + ', component class, or function component).');
  }

  if (!(0, _reactIs.isValidElementType)(propValue)) {
    return new Error('Invalid ' + location + ' `' + propFullName + '` of value `' + propValue + '` ' + ('supplied to `' + componentName + '`, expected an element type (a string ') + ', component class, or function component).');
  }

  return null;
}

exports.default = (0, _createChainableTypeChecker2.default)(elementType);
module.exports = exports['default'];

/***/ }),
/* 18 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


if (true) {
  module.exports = __webpack_require__(19);
} else {}

/***/ }),
/* 19 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/** @license React v16.10.2
 * react-is.production.min.js
 *
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */


Object.defineProperty(exports, "__esModule", {
  value: !0
});
var b = "function" === typeof Symbol && Symbol.for,
    c = b ? Symbol.for("react.element") : 60103,
    d = b ? Symbol.for("react.portal") : 60106,
    e = b ? Symbol.for("react.fragment") : 60107,
    f = b ? Symbol.for("react.strict_mode") : 60108,
    g = b ? Symbol.for("react.profiler") : 60114,
    h = b ? Symbol.for("react.provider") : 60109,
    k = b ? Symbol.for("react.context") : 60110,
    l = b ? Symbol.for("react.async_mode") : 60111,
    m = b ? Symbol.for("react.concurrent_mode") : 60111,
    n = b ? Symbol.for("react.forward_ref") : 60112,
    p = b ? Symbol.for("react.suspense") : 60113,
    q = b ? Symbol.for("react.suspense_list") : 60120,
    r = b ? Symbol.for("react.memo") : 60115,
    t = b ? Symbol.for("react.lazy") : 60116,
    v = b ? Symbol.for("react.fundamental") : 60117,
    w = b ? Symbol.for("react.responder") : 60118,
    x = b ? Symbol.for("react.scope") : 60119;

function y(a) {
  if ("object" === typeof a && null !== a) {
    var u = a.$$typeof;

    switch (u) {
      case c:
        switch (a = a.type, a) {
          case l:
          case m:
          case e:
          case g:
          case f:
          case p:
            return a;

          default:
            switch (a = a && a.$$typeof, a) {
              case k:
              case n:
              case h:
                return a;

              default:
                return u;
            }

        }

      case t:
      case r:
      case d:
        return u;
    }
  }
}

function z(a) {
  return y(a) === m;
}

exports.typeOf = y;
exports.AsyncMode = l;
exports.ConcurrentMode = m;
exports.ContextConsumer = k;
exports.ContextProvider = h;
exports.Element = c;
exports.ForwardRef = n;
exports.Fragment = e;
exports.Lazy = t;
exports.Memo = r;
exports.Portal = d;
exports.Profiler = g;
exports.StrictMode = f;
exports.Suspense = p;

exports.isValidElementType = function (a) {
  return "string" === typeof a || "function" === typeof a || a === e || a === m || a === g || a === f || a === p || a === q || "object" === typeof a && null !== a && (a.$$typeof === t || a.$$typeof === r || a.$$typeof === h || a.$$typeof === k || a.$$typeof === n || a.$$typeof === v || a.$$typeof === w || a.$$typeof === x);
};

exports.isAsyncMode = function (a) {
  return z(a) || y(a) === l;
};

exports.isConcurrentMode = z;

exports.isContextConsumer = function (a) {
  return y(a) === k;
};

exports.isContextProvider = function (a) {
  return y(a) === h;
};

exports.isElement = function (a) {
  return "object" === typeof a && null !== a && a.$$typeof === c;
};

exports.isForwardRef = function (a) {
  return y(a) === n;
};

exports.isFragment = function (a) {
  return y(a) === e;
};

exports.isLazy = function (a) {
  return y(a) === t;
};

exports.isMemo = function (a) {
  return y(a) === r;
};

exports.isPortal = function (a) {
  return y(a) === d;
};

exports.isProfiler = function (a) {
  return y(a) === g;
};

exports.isStrictMode = function (a) {
  return y(a) === f;
};

exports.isSuspense = function (a) {
  return y(a) === p;
};

/***/ }),
/* 20 */
/***/ (function(module, exports) {

var g; // This works in non-strict mode

g = function () {
  return this;
}();

try {
  // This works if eval is allowed (see CSP)
  g = g || new Function("return this")();
} catch (e) {
  // This works if the window reference is available
  if (typeof window === "object") g = window;
} // g can still be undefined, but nothing to do about it...
// We return undefined, instead of nothing here, so it's
// easier to handle this case. if(!global) { ...}


module.exports = g;

/***/ }),
/* 21 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);

// CONCATENATED MODULE: ./node_modules/@babel/runtime/helpers/esm/extends.js
function _extends() {
  _extends = Object.assign || function (target) {
    for (var i = 1; i < arguments.length; i++) {
      var source = arguments[i];

      for (var key in source) {
        if (Object.prototype.hasOwnProperty.call(source, key)) {
          target[key] = source[key];
        }
      }
    }

    return target;
  };

  return _extends.apply(this, arguments);
}
// CONCATENATED MODULE: ./node_modules/@babel/runtime/helpers/esm/objectWithoutPropertiesLoose.js
function _objectWithoutPropertiesLoose(source, excluded) {
  if (source == null) return {};
  var target = {};
  var sourceKeys = Object.keys(source);
  var key, i;

  for (i = 0; i < sourceKeys.length; i++) {
    key = sourceKeys[i];
    if (excluded.indexOf(key) >= 0) continue;
    target[key] = source[key];
  }

  return target;
}
// EXTERNAL MODULE: ./node_modules/classnames/index.js
var classnames = __webpack_require__(2);
var classnames_default = /*#__PURE__*/__webpack_require__.n(classnames);

// EXTERNAL MODULE: external {"root":"React","commonjs2":"react","commonjs":"react","amd":"react"}
var external_root_React_commonjs2_react_commonjs_react_amd_react_ = __webpack_require__(1);
var external_root_React_commonjs2_react_commonjs_react_amd_react_default = /*#__PURE__*/__webpack_require__.n(external_root_React_commonjs2_react_commonjs_react_amd_react_);

// EXTERNAL MODULE: ./node_modules/prop-types/index.js
var prop_types = __webpack_require__(0);
var prop_types_default = /*#__PURE__*/__webpack_require__.n(prop_types);

// EXTERNAL MODULE: ./node_modules/invariant/browser.js
var browser = __webpack_require__(5);
var browser_default = /*#__PURE__*/__webpack_require__.n(browser);

// CONCATENATED MODULE: ./node_modules/uncontrollable/esm/utils.js


var noop = function noop() {};

function readOnlyPropType(handler, name) {
  return function (props, propName) {
    if (props[propName] !== undefined) {
      if (!props[handler]) {
        return new Error("You have provided a `" + propName + "` prop to `" + name + "` " + ("without an `" + handler + "` handler prop. This will render a read-only field. ") + ("If the field should be mutable use `" + defaultKey(propName) + "`. ") + ("Otherwise, set `" + handler + "`."));
      }
    }
  };
}

function uncontrolledPropTypes(controlledValues, displayName) {
  var propTypes = {};
  Object.keys(controlledValues).forEach(function (prop) {
    // add default propTypes for folks that use runtime checks
    propTypes[defaultKey(prop)] = noop;

    if (false) { var handler; }
  });
  return propTypes;
}
function utils_isProp(props, prop) {
  return props[prop] !== undefined;
}
function defaultKey(key) {
  return 'default' + key.charAt(0).toUpperCase() + key.substr(1);
}
/**
 * Copyright (c) 2013-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

function utils_canAcceptRef(component) {
  return !!component && (typeof component !== 'function' || component.prototype && component.prototype.isReactComponent);
}
// CONCATENATED MODULE: ./node_modules/uncontrollable/esm/hook.js



function _toPropertyKey(arg) {
  var key = _toPrimitive(arg, "string");

  return typeof key === "symbol" ? key : String(key);
}

function _toPrimitive(input, hint) {
  if (typeof input !== "object" || input === null) return input;
  var prim = input[Symbol.toPrimitive];

  if (prim !== undefined) {
    var res = prim.call(input, hint || "default");
    if (typeof res !== "object") return res;
    throw new TypeError("@@toPrimitive must return a primitive value.");
  }

  return (hint === "string" ? String : Number)(input);
}




function useUncontrolledProp(propValue, defaultValue, handler) {
  var wasPropRef = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useRef"])(propValue !== undefined);

  var _useState = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useState"])(defaultValue),
      stateValue = _useState[0],
      setState = _useState[1];

  var isProp = propValue !== undefined;
  var wasProp = wasPropRef.current;
  wasPropRef.current = isProp;
  /**
   * If a prop switches from controlled to Uncontrolled
   * reset its value to the defaultValue
   */

  if (!isProp && wasProp && stateValue !== defaultValue) {
    setState(defaultValue);
  }

  return [isProp ? propValue : stateValue, Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useCallback"])(function (value) {
    for (var _len = arguments.length, args = new Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
      args[_key - 1] = arguments[_key];
    }

    if (handler) handler.apply(void 0, [value].concat(args));
    setState(value);
  }, [handler])];
}


function useUncontrolled(props, config) {
  return Object.keys(config).reduce(function (result, fieldName) {
    var _extends2;

    var _ref = result,
        defaultValue = _ref[defaultKey(fieldName)],
        propsValue = _ref[fieldName],
        rest = _objectWithoutPropertiesLoose(_ref, [defaultKey(fieldName), fieldName].map(_toPropertyKey));

    var handlerName = config[fieldName];

    var _useUncontrolledProp = useUncontrolledProp(propsValue, defaultValue, props[handlerName]),
        value = _useUncontrolledProp[0],
        handler = _useUncontrolledProp[1];

    return _extends({}, rest, (_extends2 = {}, _extends2[fieldName] = value, _extends2[handlerName] = handler, _extends2));
  }, props);
}
// CONCATENATED MODULE: ./node_modules/@babel/runtime/helpers/esm/inheritsLoose.js
function _inheritsLoose(subClass, superClass) {
  subClass.prototype = Object.create(superClass.prototype);
  subClass.prototype.constructor = subClass;
  subClass.__proto__ = superClass;
}
// CONCATENATED MODULE: ./node_modules/react-lifecycles-compat/react-lifecycles-compat.es.js
/**
 * Copyright (c) 2013-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
function componentWillMount() {
  // Call this.constructor.gDSFP to support sub-classes.
  var state = this.constructor.getDerivedStateFromProps(this.props, this.state);

  if (state !== null && state !== undefined) {
    this.setState(state);
  }
}

function componentWillReceiveProps(nextProps) {
  // Call this.constructor.gDSFP to support sub-classes.
  // Use the setState() updater to ensure state isn't stale in certain edge cases.
  function updater(prevState) {
    var state = this.constructor.getDerivedStateFromProps(nextProps, prevState);
    return state !== null && state !== undefined ? state : null;
  } // Binding "this" is important for shallow renderer support.


  this.setState(updater.bind(this));
}

function componentWillUpdate(nextProps, nextState) {
  try {
    var prevProps = this.props;
    var prevState = this.state;
    this.props = nextProps;
    this.state = nextState;
    this.__reactInternalSnapshotFlag = true;
    this.__reactInternalSnapshot = this.getSnapshotBeforeUpdate(prevProps, prevState);
  } finally {
    this.props = prevProps;
    this.state = prevState;
  }
} // React may warn about cWM/cWRP/cWU methods being deprecated.
// Add a flag to suppress these warnings for this special case.


componentWillMount.__suppressDeprecationWarning = true;
componentWillReceiveProps.__suppressDeprecationWarning = true;
componentWillUpdate.__suppressDeprecationWarning = true;

function polyfill(Component) {
  var prototype = Component.prototype;

  if (!prototype || !prototype.isReactComponent) {
    throw new Error('Can only polyfill class components');
  }

  if (typeof Component.getDerivedStateFromProps !== 'function' && typeof prototype.getSnapshotBeforeUpdate !== 'function') {
    return Component;
  } // If new component APIs are defined, "unsafe" lifecycles won't be called.
  // Error if any of these lifecycles are present,
  // Because they would work differently between older and newer (16.3+) versions of React.


  var foundWillMountName = null;
  var foundWillReceivePropsName = null;
  var foundWillUpdateName = null;

  if (typeof prototype.componentWillMount === 'function') {
    foundWillMountName = 'componentWillMount';
  } else if (typeof prototype.UNSAFE_componentWillMount === 'function') {
    foundWillMountName = 'UNSAFE_componentWillMount';
  }

  if (typeof prototype.componentWillReceiveProps === 'function') {
    foundWillReceivePropsName = 'componentWillReceiveProps';
  } else if (typeof prototype.UNSAFE_componentWillReceiveProps === 'function') {
    foundWillReceivePropsName = 'UNSAFE_componentWillReceiveProps';
  }

  if (typeof prototype.componentWillUpdate === 'function') {
    foundWillUpdateName = 'componentWillUpdate';
  } else if (typeof prototype.UNSAFE_componentWillUpdate === 'function') {
    foundWillUpdateName = 'UNSAFE_componentWillUpdate';
  }

  if (foundWillMountName !== null || foundWillReceivePropsName !== null || foundWillUpdateName !== null) {
    var componentName = Component.displayName || Component.name;
    var newApiName = typeof Component.getDerivedStateFromProps === 'function' ? 'getDerivedStateFromProps()' : 'getSnapshotBeforeUpdate()';
    throw Error('Unsafe legacy lifecycles will not be called for components using new component APIs.\n\n' + componentName + ' uses ' + newApiName + ' but also contains the following legacy lifecycles:' + (foundWillMountName !== null ? '\n  ' + foundWillMountName : '') + (foundWillReceivePropsName !== null ? '\n  ' + foundWillReceivePropsName : '') + (foundWillUpdateName !== null ? '\n  ' + foundWillUpdateName : '') + '\n\nThe above lifecycles should be removed. Learn more about this warning here:\n' + 'https://fb.me/react-async-component-lifecycle-hooks');
  } // React <= 16.2 does not support static getDerivedStateFromProps.
  // As a workaround, use cWM and cWRP to invoke the new static lifecycle.
  // Newer versions of React will ignore these lifecycles if gDSFP exists.


  if (typeof Component.getDerivedStateFromProps === 'function') {
    prototype.componentWillMount = componentWillMount;
    prototype.componentWillReceiveProps = componentWillReceiveProps;
  } // React <= 16.2 does not support getSnapshotBeforeUpdate.
  // As a workaround, use cWU to invoke the new lifecycle.
  // Newer versions of React will ignore that lifecycle if gSBU exists.


  if (typeof prototype.getSnapshotBeforeUpdate === 'function') {
    if (typeof prototype.componentDidUpdate !== 'function') {
      throw new Error('Cannot polyfill getSnapshotBeforeUpdate() for components that do not define componentDidUpdate() on the prototype');
    }

    prototype.componentWillUpdate = componentWillUpdate;
    var componentDidUpdate = prototype.componentDidUpdate;

    prototype.componentDidUpdate = function componentDidUpdatePolyfill(prevProps, prevState, maybeSnapshot) {
      // 16.3+ will not execute our will-update method;
      // It will pass a snapshot value to did-update though.
      // Older versions will require our polyfilled will-update value.
      // We need to handle both cases, but can't just check for the presence of "maybeSnapshot",
      // Because for <= 15.x versions this might be a "prevContext" object.
      // We also can't just check "__reactInternalSnapshot",
      // Because get-snapshot might return a falsy value.
      // So check for the explicit __reactInternalSnapshotFlag flag to determine behavior.
      var snapshot = this.__reactInternalSnapshotFlag ? this.__reactInternalSnapshot : maybeSnapshot;
      componentDidUpdate.call(this, prevProps, prevState, snapshot);
    };
  }

  return Component;
}


// CONCATENATED MODULE: ./node_modules/uncontrollable/esm/uncontrollable.js







function uncontrollable(Component, controlledValues, methods) {
  if (methods === void 0) {
    methods = [];
  }

  var displayName = Component.displayName || Component.name || 'Component';
  var canAcceptRef = utils_canAcceptRef(Component);
  var controlledProps = Object.keys(controlledValues);
  var PROPS_TO_OMIT = controlledProps.map(defaultKey);
  !(canAcceptRef || !methods.length) ?  false ? undefined : browser_default()(false) : void 0;

  var UncontrolledComponent =
  /*#__PURE__*/
  function (_React$Component) {
    _inheritsLoose(UncontrolledComponent, _React$Component);

    function UncontrolledComponent() {
      var _this;

      for (var _len = arguments.length, args = new Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      _this = _React$Component.call.apply(_React$Component, [this].concat(args)) || this;
      _this.handlers = Object.create(null);
      controlledProps.forEach(function (propName) {
        var handlerName = controlledValues[propName];

        var handleChange = function handleChange(value) {
          if (_this.props[handlerName]) {
            var _this$props;

            _this._notifying = true;

            for (var _len2 = arguments.length, args = new Array(_len2 > 1 ? _len2 - 1 : 0), _key2 = 1; _key2 < _len2; _key2++) {
              args[_key2 - 1] = arguments[_key2];
            }

            (_this$props = _this.props)[handlerName].apply(_this$props, [value].concat(args));

            _this._notifying = false;
          }

          if (!_this.unmounted) _this.setState(function (_ref) {
            var _extends2;

            var values = _ref.values;
            return {
              values: _extends(Object.create(null), values, (_extends2 = {}, _extends2[propName] = value, _extends2))
            };
          });
        };

        _this.handlers[handlerName] = handleChange;
      });
      if (methods.length) _this.attachRef = function (ref) {
        _this.inner = ref;
      };
      var values = Object.create(null);
      controlledProps.forEach(function (key) {
        values[key] = _this.props[defaultKey(key)];
      });
      _this.state = {
        values: values,
        prevProps: {}
      };
      return _this;
    }

    var _proto = UncontrolledComponent.prototype;

    _proto.shouldComponentUpdate = function shouldComponentUpdate() {
      //let setState trigger the update
      return !this._notifying;
    };

    UncontrolledComponent.getDerivedStateFromProps = function getDerivedStateFromProps(props, _ref2) {
      var values = _ref2.values,
          prevProps = _ref2.prevProps;
      var nextState = {
        values: _extends(Object.create(null), values),
        prevProps: {}
      };
      controlledProps.forEach(function (key) {
        /**
         * If a prop switches from controlled to Uncontrolled
         * reset its value to the defaultValue
         */
        nextState.prevProps[key] = props[key];

        if (!utils_isProp(props, key) && utils_isProp(prevProps, key)) {
          nextState.values[key] = props[defaultKey(key)];
        }
      });
      return nextState;
    };

    _proto.componentWillUnmount = function componentWillUnmount() {
      this.unmounted = true;
    };

    _proto.render = function render() {
      var _this2 = this;

      var _this$props2 = this.props,
          innerRef = _this$props2.innerRef,
          props = _objectWithoutPropertiesLoose(_this$props2, ["innerRef"]);

      PROPS_TO_OMIT.forEach(function (prop) {
        delete props[prop];
      });
      var newProps = {};
      controlledProps.forEach(function (propName) {
        var propValue = _this2.props[propName];
        newProps[propName] = propValue !== undefined ? propValue : _this2.state.values[propName];
      });
      return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, props, newProps, this.handlers, {
        ref: innerRef || this.attachRef
      }));
    };

    return UncontrolledComponent;
  }(external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Component);

  polyfill(UncontrolledComponent);
  UncontrolledComponent.displayName = "Uncontrolled(" + displayName + ")";
  UncontrolledComponent.propTypes = _extends({
    innerRef: function innerRef() {}
  }, uncontrolledPropTypes(controlledValues, displayName));
  methods.forEach(function (method) {
    UncontrolledComponent.prototype[method] = function $proxiedMethod() {
      var _this$inner;

      return (_this$inner = this.inner)[method].apply(_this$inner, arguments);
    };
  });
  var WrappedComponent = UncontrolledComponent;

  if (external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef) {
    WrappedComponent = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (props, ref) {
      return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(UncontrolledComponent, _extends({}, props, {
        innerRef: ref
      }));
    });
    WrappedComponent.propTypes = UncontrolledComponent.propTypes;
  }

  WrappedComponent.ControlledComponent = Component;
  /**
   * useful when wrapping a Component and you want to control
   * everything
   */

  WrappedComponent.deferControlTo = function (newComponent, additions, nextMethods) {
    if (additions === void 0) {
      additions = {};
    }

    return uncontrollable(newComponent, _extends({}, controlledValues, additions), nextMethods);
  };

  return WrappedComponent;
}
// CONCATENATED MODULE: ./node_modules/uncontrollable/esm/index.js


// EXTERNAL MODULE: ./node_modules/@restart/context/forwardRef.js
var forwardRef = __webpack_require__(11);
var forwardRef_default = /*#__PURE__*/__webpack_require__.n(forwardRef);

// CONCATENATED MODULE: ./src/ThemeProvider.js

var _jsxFileName = "/Users/jquense/src/react-bootstrap/src/ThemeProvider.js";



var ThemeContext = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createContext({});
var Consumer = ThemeContext.Consumer,
    Provider = ThemeContext.Provider;

function ThemeProvider(_ref) {
  var prefixes = _ref.prefixes,
      children = _ref.children;
  var copiedPrefixes = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useMemo"])(function () {
    return _extends({}, prefixes);
  }, [prefixes]);
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Provider, {
    value: copiedPrefixes,
    __source: {
      fileName: _jsxFileName,
      lineNumber: 11
    },
    __self: this
  }, children);
}

ThemeProvider.propTypes = {
  prefixes: prop_types_default.a.object.isRequired
};
function useBootstrapPrefix(prefix, defaultPrefix) {
  var prefixes = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(ThemeContext);
  return prefix || prefixes[defaultPrefix] || defaultPrefix;
}

function createBootstrapComponent(Component, opts) {
  if (typeof opts === 'string') opts = {
    prefix: opts
  };
  var isClassy = Component.prototype && Component.prototype.isReactComponent; // If it's a functional component make sure we don't break it with a ref

  var _opts = opts,
      prefix = _opts.prefix,
      _opts$forwardRefAs = _opts.forwardRefAs,
      forwardRefAs = _opts$forwardRefAs === void 0 ? isClassy ? 'ref' : 'innerRef' : _opts$forwardRefAs;
  return forwardRef_default()(function (_ref2, ref) {
    var props = _extends({}, _ref2);

    props[forwardRefAs] = ref; // eslint-disable-next-line react/prop-types

    var bsPrefix = useBootstrapPrefix(props.bsPrefix, prefix);
    return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, props, {
      bsPrefix: bsPrefix,
      __source: {
        fileName: _jsxFileName,
        lineNumber: 34
      },
      __self: this
    }));
  }, {
    displayName: "Bootstrap(" + (Component.displayName || Component.name) + ")"
  });
}


/* harmony default export */ var src_ThemeProvider = (ThemeProvider);
// CONCATENATED MODULE: ./src/SelectableContext.js

var SelectableContext = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createContext();
var makeEventKey = function makeEventKey(eventKey, href) {
  if (eventKey != null) return String(eventKey);
  return href || null;
};
/* harmony default export */ var src_SelectableContext = (SelectableContext);
// CONCATENATED MODULE: ./src/AccordionContext.js

/* harmony default export */ var AccordionContext = (external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createContext(null));
// CONCATENATED MODULE: ./src/AccordionToggle.js


var AccordionToggle_jsxFileName = "/Users/jquense/src/react-bootstrap/src/AccordionToggle.js";




var propTypes = {
  /** Set a custom element for this component */
  as: prop_types_default.a.elementType,

  /**
   * A key that corresponds to the collapse component that gets triggered
   * when this has been clicked.
   */
  eventKey: prop_types_default.a.string.isRequired,

  /** A callback function for when this component is clicked */
  onClick: prop_types_default.a.func,

  /** Children prop should only contain a single child, and  is enforced as such */
  children: prop_types_default.a.element
};
function useAccordionToggle(eventKey, onClick) {
  var contextEventKey = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(AccordionContext);
  var onSelect = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(src_SelectableContext);
  return function (e) {
    /* 
      Compare the event key in context with the given event key.
      If they are the same, then collapse the component.
    */
    var eventKeyPassed = eventKey === contextEventKey ? null : eventKey;
    onSelect(eventKeyPassed, e);
    if (onClick) onClick(e);
  };
}
var AccordionToggle = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'button' : _ref$as,
      children = _ref.children,
      eventKey = _ref.eventKey,
      onClick = _ref.onClick,
      props = _objectWithoutPropertiesLoose(_ref, ["as", "children", "eventKey", "onClick"]);

  var accordionOnClick = useAccordionToggle(eventKey, onClick);
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({
    ref: ref,
    onClick: accordionOnClick
  }, props, {
    __source: {
      fileName: AccordionToggle_jsxFileName,
      lineNumber: 54
    },
    __self: this
  }), children);
});
AccordionToggle.propTypes = propTypes;
/* harmony default export */ var src_AccordionToggle = (AccordionToggle);
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/ownerDocument.js
function ownerDocument(node) {
  return node && node.ownerDocument || document;
}
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/ownerWindow.js

function ownerWindow(node) {
  var doc = ownerDocument(node);
  return doc && doc.defaultView || window;
}
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/getComputedStyle.js

function getComputedStyle(node, psuedoElement) {
  return ownerWindow(node).getComputedStyle(node, psuedoElement);
}
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/hyphenate.js
var rUpper = /([A-Z])/g;
function hyphenate(string) {
  return string.replace(rUpper, '-$1').toLowerCase();
}
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/hyphenateStyle.js
/**
 * Copyright 2013-2014, Facebook, Inc.
 * All rights reserved.
 * https://github.com/facebook/react/blob/2aeb8a2a6beb00617a4217f7f8284924fa2ad819/src/vendor/core/hyphenateStyleName.js
 */

var msPattern = /^ms-/;
function hyphenateStyleName(string) {
  return hyphenate(string).replace(msPattern, '-ms-');
}
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/isTransform.js
var supportedTransforms = /^((translate|rotate|scale)(X|Y|Z|3d)?|matrix(3d)?|perspective|skew(X|Y)?)$/i;
function isTransform(value) {
  return !!(value && supportedTransforms.test(value));
}
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/css.js




function css_style(node, property) {
  var css = '';
  var transforms = '';

  if (typeof property === 'string') {
    return node.style.getPropertyValue(hyphenateStyleName(property)) || getComputedStyle(node).getPropertyValue(hyphenateStyleName(property));
  }

  Object.keys(property).forEach(function (key) {
    var value = property[key];

    if (!value && value !== 0) {
      node.style.removeProperty(hyphenateStyleName(key));
    } else if (isTransform(key)) {
      transforms += key + "(" + value + ") ";
    } else {
      css += hyphenateStyleName(key) + ": " + value + ";";
    }
  });

  if (transforms) {
    css += "transform: " + transforms + ";";
  }

  node.style.cssText += ";" + css;
}

/* harmony default export */ var esm_css = (css_style);
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/canUseDOM.js
/* harmony default export */ var canUseDOM = (!!(typeof window !== 'undefined' && window.document && window.document.createElement));
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/addEventListener.js
/* eslint-disable no-return-assign */

var optionsSupported = false;
var onceSupported = false;

try {
  var addEventListener_options = {
    get passive() {
      return optionsSupported = true;
    },

    get once() {
      // eslint-disable-next-line no-multi-assign
      return onceSupported = optionsSupported = true;
    }

  };

  if (canUseDOM) {
    window.addEventListener('test', addEventListener_options, addEventListener_options);
    window.removeEventListener('test', addEventListener_options, true);
  }
} catch (e) {}
/* */

/**
 * An `addEventListener` ponyfill, supports the `once` option
 */


function addEventListener(node, eventName, handler, options) {
  if (options && typeof options !== 'boolean' && !onceSupported) {
    var once = options.once,
        capture = options.capture;
    var wrappedHandler = handler;

    if (!onceSupported && once) {
      wrappedHandler = handler.__once || function onceHandler(event) {
        this.removeEventListener(eventName, onceHandler, capture);
        handler.call(this, event);
      };

      handler.__once = wrappedHandler;
    }

    node.addEventListener(eventName, wrappedHandler, optionsSupported ? options : capture);
  }

  node.addEventListener(eventName, handler, options);
}

/* harmony default export */ var esm_addEventListener = (addEventListener);
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/removeEventListener.js
function removeEventListener(node, eventName, handler, options) {
  var capture = options && typeof options !== 'boolean' ? options.capture : options;
  node.removeEventListener(eventName, handler, capture);

  if (handler.__once) {
    node.removeEventListener(eventName, handler.__once, capture);
  }
}

/* harmony default export */ var esm_removeEventListener = (removeEventListener);
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/listen.js



function listen(node, eventName, handler, options) {
  esm_addEventListener(node, eventName, handler, options);
  return function () {
    esm_removeEventListener(node, eventName, handler, options);
  };
}

/* harmony default export */ var esm_listen = (listen);
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/transitionEnd.js



var TRANSITION_SUPPORTED = canUseDOM && 'ontransitionend' in window;
function parseDuration(node) {
  var str = esm_css(node, 'transitionDuration') || '';
  var mult = str.indexOf('ms') === -1 ? 1000 : 1;
  return parseFloat(str) * mult;
}

function triggerTransitionEnd(element) {
  var evt = document.createEvent('HTMLEvents');
  evt.initEvent('transitionend', true, true);
  element.dispatchEvent(evt);
}

function emulateTransitionEnd(element, duration, padding) {
  if (padding === void 0) {
    padding = 5;
  }

  var called = false;
  var handle = setTimeout(function () {
    if (!called) triggerTransitionEnd(element);
  }, duration + padding);
  var remove = esm_listen(element, 'transitionend', function () {
    called = true;
  }, {
    once: true
  });
  return function () {
    clearTimeout(handle);
    remove();
  };
}

function transitionEnd(element, handler, duration) {
  if (!TRANSITION_SUPPORTED) {
    return emulateTransitionEnd(element, 0, 0);
  }

  if (duration == null) duration = parseDuration(element) || 0;
  emulateTransitionEnd(element, duration);
  return esm_listen(element, 'transitionend', handler);
}

/* harmony default export */ var esm_transitionEnd = (transitionEnd);
// EXTERNAL MODULE: external {"root":"ReactDOM","commonjs2":"react-dom","commonjs":"react-dom","amd":"react-dom"}
var external_root_ReactDOM_commonjs2_react_dom_commonjs_react_dom_amd_react_dom_ = __webpack_require__(3);
var external_root_ReactDOM_commonjs2_react_dom_commonjs_react_dom_amd_react_dom_default = /*#__PURE__*/__webpack_require__.n(external_root_ReactDOM_commonjs2_react_dom_commonjs_react_dom_amd_react_dom_);

// CONCATENATED MODULE: ./node_modules/react-transition-group/esm/config.js
/* harmony default export */ var esm_config = ({
  disabled: false
});
// CONCATENATED MODULE: ./node_modules/react-transition-group/esm/TransitionGroupContext.js

/* harmony default export */ var TransitionGroupContext = (external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createContext(null));
// CONCATENATED MODULE: ./node_modules/react-transition-group/esm/Transition.js








var UNMOUNTED = 'unmounted';
var EXITED = 'exited';
var ENTERING = 'entering';
var ENTERED = 'entered';
var EXITING = 'exiting';
/**
 * The Transition component lets you describe a transition from one component
 * state to another _over time_ with a simple declarative API. Most commonly
 * it's used to animate the mounting and unmounting of a component, but can also
 * be used to describe in-place transition states as well.
 *
 * ---
 *
 * **Note**: `Transition` is a platform-agnostic base component. If you're using
 * transitions in CSS, you'll probably want to use
 * [`CSSTransition`](https://reactcommunity.org/react-transition-group/css-transition)
 * instead. It inherits all the features of `Transition`, but contains
 * additional features necessary to play nice with CSS transitions (hence the
 * name of the component).
 *
 * ---
 *
 * By default the `Transition` component does not alter the behavior of the
 * component it renders, it only tracks "enter" and "exit" states for the
 * components. It's up to you to give meaning and effect to those states. For
 * example we can add styles to a component when it enters or exits:
 *
 * ```jsx
 * import { Transition } from 'react-transition-group';
 *
 * const duration = 300;
 *
 * const defaultStyle = {
 *   transition: `opacity ${duration}ms ease-in-out`,
 *   opacity: 0,
 * }
 *
 * const transitionStyles = {
 *   entering: { opacity: 1 },
 *   entered:  { opacity: 1 },
 *   exiting:  { opacity: 0 },
 *   exited:  { opacity: 0 },
 * };
 *
 * const Fade = ({ in: inProp }) => (
 *   <Transition in={inProp} timeout={duration}>
 *     {state => (
 *       <div style={{
 *         ...defaultStyle,
 *         ...transitionStyles[state]
 *       }}>
 *         I'm a fade Transition!
 *       </div>
 *     )}
 *   </Transition>
 * );
 * ```
 *
 * There are 4 main states a Transition can be in:
 *  - `'entering'`
 *  - `'entered'`
 *  - `'exiting'`
 *  - `'exited'`
 *
 * Transition state is toggled via the `in` prop. When `true` the component
 * begins the "Enter" stage. During this stage, the component will shift from
 * its current transition state, to `'entering'` for the duration of the
 * transition and then to the `'entered'` stage once it's complete. Let's take
 * the following example (we'll use the
 * [useState](https://reactjs.org/docs/hooks-reference.html#usestate) hook):
 *
 * ```jsx
 * function App() {
 *   const [inProp, setInProp] = useState(false);
 *   return (
 *     <div>
 *       <Transition in={inProp} timeout={500}>
 *         {state => (
 *           // ...
 *         )}
 *       </Transition>
 *       <button onClick={() => setInProp(true)}>
 *         Click to Enter
 *       </button>
 *     </div>
 *   );
 * }
 * ```
 *
 * When the button is clicked the component will shift to the `'entering'` state
 * and stay there for 500ms (the value of `timeout`) before it finally switches
 * to `'entered'`.
 *
 * When `in` is `false` the same thing happens except the state moves from
 * `'exiting'` to `'exited'`.
 */

var Transition_Transition =
/*#__PURE__*/
function (_React$Component) {
  _inheritsLoose(Transition, _React$Component);

  function Transition(props, context) {
    var _this;

    _this = _React$Component.call(this, props, context) || this;
    var parentGroup = context; // In the context of a TransitionGroup all enters are really appears

    var appear = parentGroup && !parentGroup.isMounting ? props.enter : props.appear;
    var initialStatus;
    _this.appearStatus = null;

    if (props.in) {
      if (appear) {
        initialStatus = EXITED;
        _this.appearStatus = ENTERING;
      } else {
        initialStatus = ENTERED;
      }
    } else {
      if (props.unmountOnExit || props.mountOnEnter) {
        initialStatus = UNMOUNTED;
      } else {
        initialStatus = EXITED;
      }
    }

    _this.state = {
      status: initialStatus
    };
    _this.nextCallback = null;
    return _this;
  }

  Transition.getDerivedStateFromProps = function getDerivedStateFromProps(_ref, prevState) {
    var nextIn = _ref.in;

    if (nextIn && prevState.status === UNMOUNTED) {
      return {
        status: EXITED
      };
    }

    return null;
  }; // getSnapshotBeforeUpdate(prevProps) {
  //   let nextStatus = null
  //   if (prevProps !== this.props) {
  //     const { status } = this.state
  //     if (this.props.in) {
  //       if (status !== ENTERING && status !== ENTERED) {
  //         nextStatus = ENTERING
  //       }
  //     } else {
  //       if (status === ENTERING || status === ENTERED) {
  //         nextStatus = EXITING
  //       }
  //     }
  //   }
  //   return { nextStatus }
  // }


  var _proto = Transition.prototype;

  _proto.componentDidMount = function componentDidMount() {
    this.updateStatus(true, this.appearStatus);
  };

  _proto.componentDidUpdate = function componentDidUpdate(prevProps) {
    var nextStatus = null;

    if (prevProps !== this.props) {
      var status = this.state.status;

      if (this.props.in) {
        if (status !== ENTERING && status !== ENTERED) {
          nextStatus = ENTERING;
        }
      } else {
        if (status === ENTERING || status === ENTERED) {
          nextStatus = EXITING;
        }
      }
    }

    this.updateStatus(false, nextStatus);
  };

  _proto.componentWillUnmount = function componentWillUnmount() {
    this.cancelNextCallback();
  };

  _proto.getTimeouts = function getTimeouts() {
    var timeout = this.props.timeout;
    var exit, enter, appear;
    exit = enter = appear = timeout;

    if (timeout != null && typeof timeout !== 'number') {
      exit = timeout.exit;
      enter = timeout.enter; // TODO: remove fallback for next major

      appear = timeout.appear !== undefined ? timeout.appear : enter;
    }

    return {
      exit: exit,
      enter: enter,
      appear: appear
    };
  };

  _proto.updateStatus = function updateStatus(mounting, nextStatus) {
    if (mounting === void 0) {
      mounting = false;
    }

    if (nextStatus !== null) {
      // nextStatus will always be ENTERING or EXITING.
      this.cancelNextCallback();
      var node = external_root_ReactDOM_commonjs2_react_dom_commonjs_react_dom_amd_react_dom_default.a.findDOMNode(this);

      if (nextStatus === ENTERING) {
        this.performEnter(node, mounting);
      } else {
        this.performExit(node);
      }
    } else if (this.props.unmountOnExit && this.state.status === EXITED) {
      this.setState({
        status: UNMOUNTED
      });
    }
  };

  _proto.performEnter = function performEnter(node, mounting) {
    var _this2 = this;

    var enter = this.props.enter;
    var appearing = this.context ? this.context.isMounting : mounting;
    var timeouts = this.getTimeouts();
    var enterTimeout = appearing ? timeouts.appear : timeouts.enter; // no enter animation skip right to ENTERED
    // if we are mounting and running this it means appear _must_ be set

    if (!mounting && !enter || esm_config.disabled) {
      this.safeSetState({
        status: ENTERED
      }, function () {
        _this2.props.onEntered(node);
      });
      return;
    }

    this.props.onEnter(node, appearing);
    this.safeSetState({
      status: ENTERING
    }, function () {
      _this2.props.onEntering(node, appearing);

      _this2.onTransitionEnd(node, enterTimeout, function () {
        _this2.safeSetState({
          status: ENTERED
        }, function () {
          _this2.props.onEntered(node, appearing);
        });
      });
    });
  };

  _proto.performExit = function performExit(node) {
    var _this3 = this;

    var exit = this.props.exit;
    var timeouts = this.getTimeouts(); // no exit animation skip right to EXITED

    if (!exit || esm_config.disabled) {
      this.safeSetState({
        status: EXITED
      }, function () {
        _this3.props.onExited(node);
      });
      return;
    }

    this.props.onExit(node);
    this.safeSetState({
      status: EXITING
    }, function () {
      _this3.props.onExiting(node);

      _this3.onTransitionEnd(node, timeouts.exit, function () {
        _this3.safeSetState({
          status: EXITED
        }, function () {
          _this3.props.onExited(node);
        });
      });
    });
  };

  _proto.cancelNextCallback = function cancelNextCallback() {
    if (this.nextCallback !== null) {
      this.nextCallback.cancel();
      this.nextCallback = null;
    }
  };

  _proto.safeSetState = function safeSetState(nextState, callback) {
    // This shouldn't be necessary, but there are weird race conditions with
    // setState callbacks and unmounting in testing, so always make sure that
    // we can cancel any pending setState callbacks after we unmount.
    callback = this.setNextCallback(callback);
    this.setState(nextState, callback);
  };

  _proto.setNextCallback = function setNextCallback(callback) {
    var _this4 = this;

    var active = true;

    this.nextCallback = function (event) {
      if (active) {
        active = false;
        _this4.nextCallback = null;
        callback(event);
      }
    };

    this.nextCallback.cancel = function () {
      active = false;
    };

    return this.nextCallback;
  };

  _proto.onTransitionEnd = function onTransitionEnd(node, timeout, handler) {
    this.setNextCallback(handler);
    var doesNotHaveTimeoutOrListener = timeout == null && !this.props.addEndListener;

    if (!node || doesNotHaveTimeoutOrListener) {
      setTimeout(this.nextCallback, 0);
      return;
    }

    if (this.props.addEndListener) {
      this.props.addEndListener(node, this.nextCallback);
    }

    if (timeout != null) {
      setTimeout(this.nextCallback, timeout);
    }
  };

  _proto.render = function render() {
    var status = this.state.status;

    if (status === UNMOUNTED) {
      return null;
    }

    var _this$props = this.props,
        children = _this$props.children,
        childProps = _objectWithoutPropertiesLoose(_this$props, ["children"]); // filter props for Transtition


    delete childProps.in;
    delete childProps.mountOnEnter;
    delete childProps.unmountOnExit;
    delete childProps.appear;
    delete childProps.enter;
    delete childProps.exit;
    delete childProps.timeout;
    delete childProps.addEndListener;
    delete childProps.onEnter;
    delete childProps.onEntering;
    delete childProps.onEntered;
    delete childProps.onExit;
    delete childProps.onExiting;
    delete childProps.onExited;

    if (typeof children === 'function') {
      // allows for nested Transitions
      return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(TransitionGroupContext.Provider, {
        value: null
      }, children(status, childProps));
    }

    var child = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Children.only(children);
    return (// allows for nested Transitions
      external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(TransitionGroupContext.Provider, {
        value: null
      }, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.cloneElement(child, childProps))
    );
  };

  return Transition;
}(external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Component);

Transition_Transition.contextType = TransitionGroupContext;
Transition_Transition.propTypes =  false ? undefined : {};

function Transition_noop() {}

Transition_Transition.defaultProps = {
  in: false,
  mountOnEnter: false,
  unmountOnExit: false,
  appear: false,
  enter: true,
  exit: true,
  onEnter: Transition_noop,
  onEntering: Transition_noop,
  onEntered: Transition_noop,
  onExit: Transition_noop,
  onExiting: Transition_noop,
  onExited: Transition_noop
};
Transition_Transition.UNMOUNTED = 0;
Transition_Transition.EXITED = 1;
Transition_Transition.ENTERING = 2;
Transition_Transition.ENTERED = 3;
Transition_Transition.EXITING = 4;
/* harmony default export */ var esm_Transition = (Transition_Transition);
// CONCATENATED MODULE: ./src/createChainedFunction.js
/**
 * Safe chained function
 *
 * Will only create a new function if needed,
 * otherwise will pass back existing functions or null.
 *
 * @param {function} functions to chain
 * @returns {function|null}
 */
function createChainedFunction() {
  for (var _len = arguments.length, funcs = new Array(_len), _key = 0; _key < _len; _key++) {
    funcs[_key] = arguments[_key];
  }

  return funcs.filter(function (f) {
    return f != null;
  }).reduce(function (acc, f) {
    if (typeof f !== 'function') {
      throw new Error('Invalid Argument Type, must only provide functions, undefined, or null.');
    }

    if (acc === null) return f;
    return function chainedFunction() {
      for (var _len2 = arguments.length, args = new Array(_len2), _key2 = 0; _key2 < _len2; _key2++) {
        args[_key2] = arguments[_key2];
      }

      acc.apply(this, args);
      f.apply(this, args);
    };
  }, null);
}

/* harmony default export */ var src_createChainedFunction = (createChainedFunction);
// CONCATENATED MODULE: ./src/triggerBrowserReflow.js
// reading a dimension prop will cause the browser to recalculate,
// which will let our animations work
function triggerBrowserReflow(node) {
  node.offsetHeight; // eslint-disable-line no-unused-expressions
}
// CONCATENATED MODULE: ./src/Collapse.js




var _collapseStyles,
    Collapse_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Collapse.js";









var MARGINS = {
  height: ['marginTop', 'marginBottom'],
  width: ['marginLeft', 'marginRight']
};

function getDimensionValue(dimension, elem) {
  var offset = "offset" + dimension[0].toUpperCase() + dimension.slice(1);
  var value = elem[offset];
  var margins = MARGINS[dimension];
  return value + parseInt(esm_css(elem, margins[0]), 10) + parseInt(esm_css(elem, margins[1]), 10);
}

var collapseStyles = (_collapseStyles = {}, _collapseStyles[EXITED] = 'collapse', _collapseStyles[EXITING] = 'collapsing', _collapseStyles[ENTERING] = 'collapsing', _collapseStyles[ENTERED] = 'collapse show', _collapseStyles);
var Collapse_propTypes = {
  /**
   * Show the component; triggers the expand or collapse animation
   */
  in: prop_types_default.a.bool,

  /**
   * Wait until the first "enter" transition to mount the component (add it to the DOM)
   */
  mountOnEnter: prop_types_default.a.bool,

  /**
   * Unmount the component (remove it from the DOM) when it is collapsed
   */
  unmountOnExit: prop_types_default.a.bool,

  /**
   * Run the expand animation when the component mounts, if it is initially
   * shown
   */
  appear: prop_types_default.a.bool,

  /**
   * Duration of the collapse animation in milliseconds, to ensure that
   * finishing callbacks are fired even if the original browser transition end
   * events are canceled
   */
  timeout: prop_types_default.a.number,

  /**
   * Callback fired before the component expands
   */
  onEnter: prop_types_default.a.func,

  /**
   * Callback fired after the component starts to expand
   */
  onEntering: prop_types_default.a.func,

  /**
   * Callback fired after the component has expanded
   */
  onEntered: prop_types_default.a.func,

  /**
   * Callback fired before the component collapses
   */
  onExit: prop_types_default.a.func,

  /**
   * Callback fired after the component starts to collapse
   */
  onExiting: prop_types_default.a.func,

  /**
   * Callback fired after the component has collapsed
   */
  onExited: prop_types_default.a.func,

  /**
   * The dimension used when collapsing, or a function that returns the
   * dimension
   *
   * _Note: Bootstrap only partially supports 'width'!
   * You will need to supply your own CSS animation for the `.width` CSS class._
   */
  dimension: prop_types_default.a.oneOfType([prop_types_default.a.oneOf(['height', 'width']), prop_types_default.a.func]),

  /**
   * Function that returns the height or width of the animating DOM node
   *
   * Allows for providing some custom logic for how much the Collapse component
   * should animate in its specified dimension. Called with the current
   * dimension prop value and the DOM node.
   *
   * @default element.offsetWidth | element.offsetHeight
   */
  getDimensionValue: prop_types_default.a.func,

  /**
   * ARIA role of collapsible element
   */
  role: prop_types_default.a.string
};
var Collapse_defaultProps = {
  in: false,
  timeout: 300,
  mountOnEnter: false,
  unmountOnExit: false,
  appear: false,
  dimension: 'height',
  getDimensionValue: getDimensionValue
};

var Collapse_Collapse =
/*#__PURE__*/
function (_React$Component) {
  _inheritsLoose(Collapse, _React$Component);

  function Collapse() {
    var _this;

    for (var _len = arguments.length, args = new Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }

    _this = _React$Component.call.apply(_React$Component, [this].concat(args)) || this;

    _this.handleEnter = function (elem) {
      elem.style[_this.getDimension()] = '0';
    };

    _this.handleEntering = function (elem) {
      var dimension = _this.getDimension();

      elem.style[dimension] = _this._getScrollDimensionValue(elem, dimension);
    };

    _this.handleEntered = function (elem) {
      elem.style[_this.getDimension()] = null;
    };

    _this.handleExit = function (elem) {
      var dimension = _this.getDimension();

      elem.style[dimension] = _this.props.getDimensionValue(dimension, elem) + "px";
      triggerBrowserReflow(elem);
    };

    _this.handleExiting = function (elem) {
      elem.style[_this.getDimension()] = null;
    };

    return _this;
  }

  var _proto = Collapse.prototype;

  _proto.getDimension = function getDimension() {
    return typeof this.props.dimension === 'function' ? this.props.dimension() : this.props.dimension;
  }
  /* -- Expanding -- */
  ;

  // for testing
  _proto._getScrollDimensionValue = function _getScrollDimensionValue(elem, dimension) {
    var scroll = "scroll" + dimension[0].toUpperCase() + dimension.slice(1);
    return elem[scroll] + "px";
  };

  _proto.render = function render() {
    var _this2 = this;

    var _this$props = this.props,
        onEnter = _this$props.onEnter,
        onEntering = _this$props.onEntering,
        onEntered = _this$props.onEntered,
        onExit = _this$props.onExit,
        onExiting = _this$props.onExiting,
        className = _this$props.className,
        children = _this$props.children,
        props = _objectWithoutPropertiesLoose(_this$props, ["onEnter", "onEntering", "onEntered", "onExit", "onExiting", "className", "children"]);

    delete props.dimension;
    delete props.getDimensionValue;
    var handleEnter = src_createChainedFunction(this.handleEnter, onEnter);
    var handleEntering = src_createChainedFunction(this.handleEntering, onEntering);
    var handleEntered = src_createChainedFunction(this.handleEntered, onEntered);
    var handleExit = src_createChainedFunction(this.handleExit, onExit);
    var handleExiting = src_createChainedFunction(this.handleExiting, onExiting);
    return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(esm_Transition, _extends({
      addEndListener: esm_transitionEnd
    }, props, {
      "aria-expanded": props.role ? props.in : null,
      onEnter: handleEnter,
      onEntering: handleEntering,
      onEntered: handleEntered,
      onExit: handleExit,
      onExiting: handleExiting,
      __source: {
        fileName: Collapse_jsxFileName,
        lineNumber: 199
      },
      __self: this
    }), function (state, innerProps) {
      return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.cloneElement(children, _extends({}, innerProps, {
        className: classnames_default()(className, children.props.className, collapseStyles[state], _this2.getDimension() === 'width' && 'width')
      }));
    });
  };

  return Collapse;
}(external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Component);

Collapse_Collapse.propTypes = Collapse_propTypes;
Collapse_Collapse.defaultProps = Collapse_defaultProps;
/* harmony default export */ var src_Collapse = (Collapse_Collapse);
// CONCATENATED MODULE: ./src/AccordionCollapse.js


var AccordionCollapse_jsxFileName = "/Users/jquense/src/react-bootstrap/src/AccordionCollapse.js";




var AccordionCollapse_propTypes = {
  /**
   * A key that corresponds to the toggler that triggers this collapse's expand or collapse.
   */
  eventKey: prop_types_default.a.string.isRequired,
  children: prop_types_default.a.element.isRequired
};
var AccordionCollapse = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var children = _ref.children,
      eventKey = _ref.eventKey,
      props = _objectWithoutPropertiesLoose(_ref, ["children", "eventKey"]);

  var contextEventKey = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(AccordionContext);
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_Collapse, _extends({
    ref: ref,
    in: contextEventKey === eventKey
  }, props, {
    __source: {
      fileName: AccordionCollapse_jsxFileName,
      lineNumber: 21
    },
    __self: this
  }), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", {
    __source: {
      fileName: AccordionCollapse_jsxFileName,
      lineNumber: 22
    },
    __self: this
  }, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Children.only(children)));
});
AccordionCollapse.propTypes = AccordionCollapse_propTypes;
AccordionCollapse.displayName = 'AccordionCollapse';
/* harmony default export */ var src_AccordionCollapse = (AccordionCollapse);
// CONCATENATED MODULE: ./src/Accordion.js


var Accordion_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Accordion.js";









var Accordion_propTypes = {
  /** Set a custom element for this component */
  as: prop_types_default.a.elementType,

  /** @default 'accordion' */
  bsPrefix: prop_types_default.a.string,

  /** The current active key that corresponds to the currently expanded card */
  activeKey: prop_types_default.a.string,

  /** The default active key that is expanded on start */
  defaultActiveKey: prop_types_default.a.string
};
var Accordion = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (props, ref) {
  var _useUncontrolled = useUncontrolled(props, {
    activeKey: 'onSelect'
  }),
      _useUncontrolled$as = _useUncontrolled.as,
      Component = _useUncontrolled$as === void 0 ? 'div' : _useUncontrolled$as,
      activeKey = _useUncontrolled.activeKey,
      bsPrefix = _useUncontrolled.bsPrefix,
      children = _useUncontrolled.children,
      className = _useUncontrolled.className,
      onSelect = _useUncontrolled.onSelect,
      controlledProps = _objectWithoutPropertiesLoose(_useUncontrolled, ["as", "activeKey", "bsPrefix", "children", "className", "onSelect"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'accordion');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(AccordionContext.Provider, {
    value: activeKey,
    __source: {
      fileName: Accordion_jsxFileName,
      lineNumber: 42
    },
    __self: this
  }, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_SelectableContext.Provider, {
    value: onSelect,
    __source: {
      fileName: Accordion_jsxFileName,
      lineNumber: 43
    },
    __self: this
  }, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({
    ref: ref
  }, controlledProps, {
    className: classnames_default()(className, bsPrefix),
    __source: {
      fileName: Accordion_jsxFileName,
      lineNumber: 44
    },
    __self: this
  }), children)));
});
Accordion.propTypes = Accordion_propTypes;
Accordion.Toggle = src_AccordionToggle;
Accordion.Collapse = src_AccordionCollapse;
/* harmony default export */ var src_Accordion = (Accordion);
// EXTERNAL MODULE: ./node_modules/prop-types-extra/lib/index.js
var lib = __webpack_require__(7);

// CONCATENATED MODULE: ./node_modules/@restart/hooks/esm/useCommittedRef.js

/**
 * Creates a `Ref` whose value is updated in an effect, ensuring the most recent
 * value is the one rendered with. Generally only required for Concurrent mode usage
 * where previous work in `render()` may be discarded befor being used.
 *
 * This is safe to access in an event handler.
 *
 * @param value The `Ref` value
 */

function useCommittedRef(value) {
  var ref = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useRef"])(value);
  Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useEffect"])(function () {
    ref.current = value;
  }, [value]);
  return ref;
}

/* harmony default export */ var esm_useCommittedRef = (useCommittedRef);
// CONCATENATED MODULE: ./node_modules/@restart/hooks/esm/useEventCallback.js


function useEventCallback(fn) {
  var ref = esm_useCommittedRef(fn);
  return Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useCallback"])(function () {
    return ref.current && ref.current.apply(ref, arguments);
  }, [ref]);
}
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/camelize.js
var rHyphen = /-(.)/g;
function camelize(string) {
  return string.replace(rHyphen, function (_, chr) {
    return chr.toUpperCase();
  });
}
// CONCATENATED MODULE: ./src/createWithBsPrefix.js


var createWithBsPrefix_jsxFileName = "/Users/jquense/src/react-bootstrap/src/createWithBsPrefix.js";





var createWithBsPrefix_pascalCase = function pascalCase(str) {
  return str[0].toUpperCase() + camelize(str).slice(1);
};

function createWithBsPrefix(prefix, _temp) {
  var _ref = _temp === void 0 ? {} : _temp,
      _ref$displayName = _ref.displayName,
      displayName = _ref$displayName === void 0 ? createWithBsPrefix_pascalCase(prefix) : _ref$displayName,
      _ref$Component = _ref.Component,
      Component = _ref$Component === void 0 ? 'div' : _ref$Component,
      defaultProps = _ref.defaultProps;

  var BsComponent = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef( // eslint-disable-next-line react/prop-types
  function (_ref2, ref) {
    var className = _ref2.className,
        bsPrefix = _ref2.bsPrefix,
        _ref2$as = _ref2.as,
        Tag = _ref2$as === void 0 ? Component : _ref2$as,
        props = _objectWithoutPropertiesLoose(_ref2, ["className", "bsPrefix", "as"]);

    var resolvedPrefix = useBootstrapPrefix(bsPrefix, prefix);
    return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Tag, _extends({
      ref: ref,
      className: classnames_default()(className, resolvedPrefix)
    }, props, {
      __source: {
        fileName: createWithBsPrefix_jsxFileName,
        lineNumber: 17
      },
      __self: this
    }));
  });
  BsComponent.defaultProps = defaultProps;
  BsComponent.displayName = displayName;
  return BsComponent;
}
// CONCATENATED MODULE: ./src/divWithClassName.js

var divWithClassName_jsxFileName = "/Users/jquense/src/react-bootstrap/src/divWithClassName.js";


/* harmony default export */ var divWithClassName = (function (className) {
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (p, ref) {
    return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", _extends({}, p, {
      ref: ref,
      className: classnames_default()(p.className, className),
      __source: {
        fileName: divWithClassName_jsxFileName,
        lineNumber: 6
      },
      __self: this
    }));
  });
});
// CONCATENATED MODULE: ./src/Fade.js



var _fadeStyles,
    Fade_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Fade.js";







var Fade_propTypes = {
  /**
   * Show the component; triggers the fade in or fade out animation
   */
  in: prop_types_default.a.bool,

  /**
   * Wait until the first "enter" transition to mount the component (add it to the DOM)
   */
  mountOnEnter: prop_types_default.a.bool,

  /**
   * Unmount the component (remove it from the DOM) when it is faded out
   */
  unmountOnExit: prop_types_default.a.bool,

  /**
   * Run the fade in animation when the component mounts, if it is initially
   * shown
   */
  appear: prop_types_default.a.bool,

  /**
   * Duration of the fade animation in milliseconds, to ensure that finishing
   * callbacks are fired even if the original browser transition end events are
   * canceled
   */
  timeout: prop_types_default.a.number,

  /**
   * Callback fired before the component fades in
   */
  onEnter: prop_types_default.a.func,

  /**
   * Callback fired after the component starts to fade in
   */
  onEntering: prop_types_default.a.func,

  /**
   * Callback fired after the has component faded in
   */
  onEntered: prop_types_default.a.func,

  /**
   * Callback fired before the component fades out
   */
  onExit: prop_types_default.a.func,

  /**
   * Callback fired after the component starts to fade out
   */
  onExiting: prop_types_default.a.func,

  /**
   * Callback fired after the component has faded out
   */
  onExited: prop_types_default.a.func
};
var Fade_defaultProps = {
  in: false,
  timeout: 300,
  mountOnEnter: false,
  unmountOnExit: false,
  appear: false
};
var fadeStyles = (_fadeStyles = {}, _fadeStyles[ENTERING] = 'show', _fadeStyles[ENTERED] = 'show', _fadeStyles);
var Fade = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var className = _ref.className,
      children = _ref.children,
      props = _objectWithoutPropertiesLoose(_ref, ["className", "children"]);

  var handleEnter = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useCallback"])(function (node) {
    triggerBrowserReflow(node);
    if (props.onEnter) props.onEnter(node);
  }, [props]);
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(esm_Transition, _extends({
    ref: ref,
    addEndListener: esm_transitionEnd
  }, props, {
    onEnter: handleEnter,
    __source: {
      fileName: Fade_jsxFileName,
      lineNumber: 89
    },
    __self: this
  }), function (status, innerProps) {
    return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.cloneElement(children, _extends({}, innerProps, {
      className: classnames_default()('fade', className, children.props.className, fadeStyles[status])
    }));
  });
});
Fade.propTypes = Fade_propTypes;
Fade.defaultProps = Fade_defaultProps;
Fade.displayName = 'Fade';
/* harmony default export */ var src_Fade = (Fade);
// CONCATENATED MODULE: ./src/CloseButton.js


var CloseButton_jsxFileName = "/Users/jquense/src/react-bootstrap/src/CloseButton.js";



var CloseButton_propTypes = {
  label: prop_types_default.a.string.isRequired,
  onClick: prop_types_default.a.func
};
var CloseButton_defaultProps = {
  label: 'Close'
};
var CloseButton = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var label = _ref.label,
      onClick = _ref.onClick,
      className = _ref.className,
      props = _objectWithoutPropertiesLoose(_ref, ["label", "onClick", "className"]);

  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("button", _extends({
    ref: ref,
    type: "button",
    className: classnames_default()('close', className),
    onClick: onClick
  }, props, {
    __source: {
      fileName: CloseButton_jsxFileName,
      lineNumber: 16
    },
    __self: this
  }), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("span", {
    "aria-hidden": "true",
    __source: {
      fileName: CloseButton_jsxFileName,
      lineNumber: 23
    },
    __self: this
  }, "\xD7"), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("span", {
    className: "sr-only",
    __source: {
      fileName: CloseButton_jsxFileName,
      lineNumber: 24
    },
    __self: this
  }, label));
});
CloseButton.displayName = 'CloseButton';
CloseButton.propTypes = CloseButton_propTypes;
CloseButton.defaultProps = CloseButton_defaultProps;
/* harmony default export */ var src_CloseButton = (CloseButton);
// CONCATENATED MODULE: ./src/SafeAnchor.js


var SafeAnchor_jsxFileName = "/Users/jquense/src/react-bootstrap/src/SafeAnchor.js";



var SafeAnchor_propTypes = {
  href: prop_types_default.a.string,
  onClick: prop_types_default.a.func,
  onKeyDown: prop_types_default.a.func,
  disabled: prop_types_default.a.bool,
  role: prop_types_default.a.string,
  tabIndex: prop_types_default.a.oneOfType([prop_types_default.a.number, prop_types_default.a.string]),

  /**
   * this is sort of silly but needed for Button
   */
  as: prop_types_default.a.elementType
};

function isTrivialHref(href) {
  return !href || href.trim() === '#';
}
/**
 * There are situations due to browser quirks or Bootstrap CSS where
 * an anchor tag is needed, when semantically a button tag is the
 * better choice. SafeAnchor ensures that when an anchor is used like a
 * button its accessible. It also emulates input `disabled` behavior for
 * links, which is usually desirable for Buttons, NavItems, DropdownItems, etc.
 */


var SafeAnchor = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'a' : _ref$as,
      disabled = _ref.disabled,
      onKeyDown = _ref.onKeyDown,
      props = _objectWithoutPropertiesLoose(_ref, ["as", "disabled", "onKeyDown"]);

  var handleClick = function handleClick(event) {
    var href = props.href,
        onClick = props.onClick;

    if (disabled || isTrivialHref(href)) {
      event.preventDefault();
    }

    if (disabled) {
      event.stopPropagation();
      return;
    }

    if (onClick) {
      onClick(event);
    }
  };

  var handleKeyDown = function handleKeyDown(event) {
    if (event.key === ' ') {
      event.preventDefault();
      handleClick(event);
    }
  };

  if (isTrivialHref(props.href)) {
    props.role = props.role || 'button'; // we want to make sure there is a href attribute on the node
    // otherwise, the cursor incorrectly styled (except with role='button')

    props.href = props.href || '#';
  }

  if (disabled) {
    props.tabIndex = -1;
    props['aria-disabled'] = true;
  }

  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({
    ref: ref
  }, props, {
    onClick: handleClick,
    onKeyDown: src_createChainedFunction(handleKeyDown, onKeyDown),
    __source: {
      fileName: SafeAnchor_jsxFileName,
      lineNumber: 79
    },
    __self: this
  }));
});
SafeAnchor.propTypes = SafeAnchor_propTypes;
SafeAnchor.displayName = 'SafeAnchor';
/* harmony default export */ var src_SafeAnchor = (SafeAnchor);
// CONCATENATED MODULE: ./src/Alert.js


var Alert_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Alert.js";












var Alert_propTypes = {
  /**
   * @default 'alert'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * The Alert visual variant
   *
   * @type {'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info' | 'dark' | 'light'}
   */
  variant: prop_types_default.a.string,

  /**
   * Renders a properly aligned dismiss button, as well as
   * adding extra horizontal padding to the Alert.
   */
  dismissible: prop_types_default.a.bool,

  /**
   * Controls the visual state of the Alert.
   *
   * @controllable onClose
   */
  show: prop_types_default.a.bool,

  /**
   * Callback fired when alert is closed.
   *
   * @controllable show
   */
  onClose: prop_types_default.a.func,

  /**
   * Sets the text for alert close button.
   */
  closeLabel: prop_types_default.a.string,

  /** A `react-transition-group` Transition component used to animate the Alert on dismissal. */
  transition: lib["elementType"]
};
var Alert_defaultProps = {
  show: true,
  transition: src_Fade,
  closeLabel: 'Close alert'
};
var controllables = {
  show: 'onClose'
};
var Alert = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (uncontrolledProps, ref) {
  var _useUncontrolled = useUncontrolled(uncontrolledProps, controllables),
      bsPrefix = _useUncontrolled.bsPrefix,
      show = _useUncontrolled.show,
      closeLabel = _useUncontrolled.closeLabel,
      className = _useUncontrolled.className,
      children = _useUncontrolled.children,
      variant = _useUncontrolled.variant,
      onClose = _useUncontrolled.onClose,
      dismissible = _useUncontrolled.dismissible,
      Transition = _useUncontrolled.transition,
      props = _objectWithoutPropertiesLoose(_useUncontrolled, ["bsPrefix", "show", "closeLabel", "className", "children", "variant", "onClose", "dismissible", "transition"]);

  var prefix = useBootstrapPrefix(bsPrefix, 'alert');
  var handleClose = useEventCallback(function (e) {
    onClose(false, e);
  });
  var alert = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", _extends({
    role: "alert"
  }, Transition ? props : undefined, {
    className: classnames_default()(className, prefix, variant && prefix + "-" + variant, dismissible && prefix + "-dismissible"),
    __source: {
      fileName: Alert_jsxFileName,
      lineNumber: 87
    },
    __self: this
  }), dismissible && external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_CloseButton, {
    onClick: handleClose,
    label: closeLabel,
    __source: {
      fileName: Alert_jsxFileName,
      lineNumber: 97
    },
    __self: this
  }), children);
  if (!Transition) return show ? alert : null;
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Transition, _extends({
    unmountOnExit: true,
    ref: ref
  }, props, {
    in: show,
    __source: {
      fileName: Alert_jsxFileName,
      lineNumber: 105
    },
    __self: this
  }), alert);
});
var DivStyledAsH4 = divWithClassName('h4');
DivStyledAsH4.displayName = 'DivStyledAsH4';
Alert.displayName = 'Alert';
Alert.propTypes = Alert_propTypes;
Alert.defaultProps = Alert_defaultProps;
Alert.Link = createWithBsPrefix('alert-link', {
  Component: src_SafeAnchor
});
Alert.Heading = createWithBsPrefix('alert-heading', {
  Component: DivStyledAsH4
});
/* harmony default export */ var src_Alert = (Alert);
// CONCATENATED MODULE: ./src/Badge.js


var Badge_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Badge.js";




var Badge_propTypes = {
  /** @default 'badge' */
  bsPrefix: prop_types_default.a.string,

  /**
   * The visual style of the badge
   *
   * @type {('primary'|'secondary'|'success'|'danger'|'warning'|'info'|'light'|'dark')}
   */
  variant: prop_types_default.a.string,

  /**
   * Add the `pill` modifier to make badges more rounded with
   * some additional horizontal padding
   */
  pill: prop_types_default.a.bool.isRequired,

  /** @default span */
  as: prop_types_default.a.elementType
};
var Badge_defaultProps = {
  pill: false
};
var Badge = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      variant = _ref.variant,
      pill = _ref.pill,
      className = _ref.className,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'span' : _ref$as,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "variant", "pill", "className", "as"]);

  var prefix = useBootstrapPrefix(bsPrefix, 'badge');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({
    ref: ref
  }, props, {
    className: classnames_default()(className, prefix, pill && prefix + "-pill", variant && prefix + "-" + variant),
    __source: {
      fileName: Badge_jsxFileName,
      lineNumber: 39
    },
    __self: this
  }));
});
Badge.displayName = 'Badge';
Badge.propTypes = Badge_propTypes;
Badge.defaultProps = Badge_defaultProps;
/* harmony default export */ var src_Badge = (Badge);
// CONCATENATED MODULE: ./src/BreadcrumbItem.js


var BreadcrumbItem_jsxFileName = "/Users/jquense/src/react-bootstrap/src/BreadcrumbItem.js";





var BreadcrumbItem_propTypes = {
  /**
   * @default 'breadcrumb-item'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * Adds a visual "active" state to a Breadcrumb
   * Item and disables the link.
   */
  active: prop_types_default.a.bool,

  /**
   * `href` attribute for the inner `a` element
   */
  href: prop_types_default.a.string,

  /**
   * `title` attribute for the inner `a` element
   */
  title: prop_types_default.a.node,

  /**
   * `target` attribute for the inner `a` element
   */
  target: prop_types_default.a.string,
  as: prop_types_default.a.elementType
};
var BreadcrumbItem_defaultProps = {
  active: false
};
var BreadcrumbItem = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef( // Need to define the default "as" during prop destructuring to be compatible with styled-components github.com/react-bootstrap/react-bootstrap/issues/3595
function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      active = _ref.active,
      className = _ref.className,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'li' : _ref$as,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "active", "className", "as"]);

  var prefix = useBootstrapPrefix(bsPrefix, 'breadcrumb-item');

  var href = props.href,
      title = props.title,
      target = props.target,
      elementProps = _objectWithoutPropertiesLoose(props, ["href", "title", "target"]);

  var linkProps = {
    href: href,
    title: title,
    target: target
  };
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, {
    ref: ref,
    className: classnames_default()(prefix, className, {
      active: active
    }),
    "aria-current": active ? 'page' : undefined,
    __source: {
      fileName: BreadcrumbItem_jsxFileName,
      lineNumber: 47
    },
    __self: this
  }, active ? external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("span", _extends({}, elementProps, {
    className: classnames_default()({
      active: active
    }),
    __source: {
      fileName: BreadcrumbItem_jsxFileName,
      lineNumber: 53
    },
    __self: this
  })) : external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_SafeAnchor, _extends({}, elementProps, linkProps, {
    __source: {
      fileName: BreadcrumbItem_jsxFileName,
      lineNumber: 55
    },
    __self: this
  })));
});
BreadcrumbItem.displayName = 'BreadcrumbItem';
BreadcrumbItem.propTypes = BreadcrumbItem_propTypes;
BreadcrumbItem.defaultProps = BreadcrumbItem_defaultProps;
/* harmony default export */ var src_BreadcrumbItem = (BreadcrumbItem);
// CONCATENATED MODULE: ./src/Breadcrumb.js


var Breadcrumb_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Breadcrumb.js";





var Breadcrumb_propTypes = {
  /**
   * @default 'breadcrumb'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * ARIA label for the nav element
   * https://www.w3.org/TR/wai-aria-practices/#breadcrumb
   */
  label: prop_types_default.a.string,

  /**
   * Additional props passed as-is to the underlying `<ul>` element
   */
  listProps: prop_types_default.a.object,
  as: prop_types_default.a.elementType
};
var Breadcrumb_defaultProps = {
  label: 'breadcrumb',
  listProps: {}
};
var Breadcrumb = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      className = _ref.className,
      listProps = _ref.listProps,
      children = _ref.children,
      label = _ref.label,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'nav' : _ref$as,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "className", "listProps", "children", "label", "as"]);

  var prefix = useBootstrapPrefix(bsPrefix, 'breadcrumb');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({
    "aria-label": label,
    className: className,
    ref: ref
  }, props, {
    __source: {
      fileName: Breadcrumb_jsxFileName,
      lineNumber: 48
    },
    __self: this
  }), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("ol", _extends({}, listProps, {
    className: classnames_default()(prefix, listProps.className),
    __source: {
      fileName: Breadcrumb_jsxFileName,
      lineNumber: 49
    },
    __self: this
  }), children));
});
Breadcrumb.displayName = 'Breadcrumb';
Breadcrumb.propTypes = Breadcrumb_propTypes;
Breadcrumb.defaultProps = Breadcrumb_defaultProps;
Breadcrumb.Item = src_BreadcrumbItem;
/* harmony default export */ var src_Breadcrumb = (Breadcrumb);
// CONCATENATED MODULE: ./src/Button.js


var Button_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Button.js";





var Button_propTypes = {
  /**
   * @default 'btn'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * One or more button variant combinations
   *
   * buttons may be one of a variety of visual variants such as:
   *
   * `'primary', 'secondary', 'success', 'danger', 'warning', 'info', 'dark', 'light', 'link'`
   *
   * as well as "outline" versions (prefixed by 'outline-*')
   *
   * `'outline-primary', 'outline-secondary', 'outline-success', 'outline-danger', 'outline-warning', 'outline-info', 'outline-dark', 'outline-light'`
   */
  variant: prop_types_default.a.string,

  /**
   * Specifies a large or small button.
   *
   * @type ('sm'|'lg')
   */
  size: prop_types_default.a.string,

  /** Spans the full width of the Button parent */
  block: prop_types_default.a.bool,

  /** Manually set the visual state of the button to `:active` */
  active: prop_types_default.a.bool,

  /**
   * Disables the Button, preventing mouse events,
   * even if the underlying component is an `<a>` element
   */
  disabled: prop_types_default.a.bool,

  /** Providing a `href` will render an `<a>` element, _styled_ as a button. */
  href: prop_types_default.a.string,

  /**
   * Defines HTML button type attribute.
   *
   * @default 'button'
   */
  type: prop_types_default.a.oneOf(['button', 'reset', 'submit', null]),
  as: prop_types_default.a.elementType
};
var Button_defaultProps = {
  variant: 'primary',
  active: false,
  disabled: false,
  type: 'button'
};
var Button = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      variant = _ref.variant,
      size = _ref.size,
      active = _ref.active,
      className = _ref.className,
      block = _ref.block,
      type = _ref.type,
      as = _ref.as,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "variant", "size", "active", "className", "block", "type", "as"]);

  var prefix = useBootstrapPrefix(bsPrefix, 'btn');
  var classes = classnames_default()(className, prefix, active && 'active', prefix + "-" + variant, block && prefix + "-block", size && prefix + "-" + size);

  if (props.href) {
    return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_SafeAnchor, _extends({}, props, {
      as: as,
      ref: ref,
      className: classnames_default()(classes, props.disabled && 'disabled'),
      __source: {
        fileName: Button_jsxFileName,
        lineNumber: 84
      },
      __self: this
    }));
  }

  if (ref) {
    props.ref = ref;
  }

  if (!as) {
    props.type = type;
  }

  var Component = as || 'button';
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, props, {
    className: classes,
    __source: {
      fileName: Button_jsxFileName,
      lineNumber: 102
    },
    __self: this
  }));
});
Button.displayName = 'Button';
Button.propTypes = Button_propTypes;
Button.defaultProps = Button_defaultProps;
/* harmony default export */ var src_Button = (Button);
// CONCATENATED MODULE: ./src/ButtonGroup.js


var ButtonGroup_jsxFileName = "/Users/jquense/src/react-bootstrap/src/ButtonGroup.js";




var ButtonGroup_propTypes = {
  /**
   * @default 'btn-group'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * Sets the size for all Buttons in the group.
   *
   * @type ('sm'|'lg')
   */
  size: prop_types_default.a.string,

  /** Make the set of Buttons appear vertically stacked. */
  vertical: prop_types_default.a.bool,

  /**
   * Display as a button toggle group.
   *
   * (Generally it's better to use `ToggleButtonGroup` directly)
   */
  toggle: prop_types_default.a.bool,

  /**
   * An ARIA role describing the button group. Usually the default
   * "group" role is fine. An `aria-label` or `aria-labelledby`
   * prop is also recommended.
   */
  role: prop_types_default.a.string,
  as: prop_types_default.a.elementType
};
var ButtonGroup_defaultProps = {
  vertical: false,
  toggle: false,
  role: 'group'
};
var ButtonGroup = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (props, ref) {
  var bsPrefix = props.bsPrefix,
      size = props.size,
      toggle = props.toggle,
      vertical = props.vertical,
      className = props.className,
      _props$as = props.as,
      Component = _props$as === void 0 ? 'div' : _props$as,
      rest = _objectWithoutPropertiesLoose(props, ["bsPrefix", "size", "toggle", "vertical", "className", "as"]);

  var prefix = useBootstrapPrefix(bsPrefix, 'btn-group');
  var baseClass = prefix;
  if (vertical) baseClass = prefix + "-vertical";
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, rest, {
    ref: ref,
    className: classnames_default()(className, baseClass, size && prefix + "-" + size, toggle && prefix + "-toggle"),
    __source: {
      fileName: ButtonGroup_jsxFileName,
      lineNumber: 64
    },
    __self: this
  }));
});
ButtonGroup.displayName = 'ButtonGroup';
ButtonGroup.propTypes = ButtonGroup_propTypes;
ButtonGroup.defaultProps = ButtonGroup_defaultProps;
/* harmony default export */ var src_ButtonGroup = (ButtonGroup);
// CONCATENATED MODULE: ./src/ButtonToolbar.js


var ButtonToolbar_jsxFileName = "/Users/jquense/src/react-bootstrap/src/ButtonToolbar.js";




var ButtonToolbar_propTypes = {
  /**
   * @default 'btn-toolbar'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * The ARIA role describing the button toolbar. Generally the default
   * "toolbar" role is correct. An `aria-label` or `aria-labelledby`
   * prop is also recommended.
   */
  role: prop_types_default.a.string
};
var ButtonToolbar_defaultProps = {
  role: 'toolbar'
};
var ButtonToolbar = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      className = _ref.className,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "className"]);

  var prefix = useBootstrapPrefix(bsPrefix, 'btn-toolbar');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", _extends({}, props, {
    ref: ref,
    className: classnames_default()(className, prefix),
    __source: {
      fileName: ButtonToolbar_jsxFileName,
      lineNumber: 30
    },
    __self: this
  }));
});
ButtonToolbar.displayName = 'ButtonToolbar';
ButtonToolbar.propTypes = ButtonToolbar_propTypes;
ButtonToolbar.defaultProps = ButtonToolbar_defaultProps;
/* harmony default export */ var src_ButtonToolbar = (ButtonToolbar);
// CONCATENATED MODULE: ./src/CardContext.js

/* harmony default export */ var CardContext = (external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createContext(null));
// CONCATENATED MODULE: ./src/CardImg.js


var CardImg_jsxFileName = "/Users/jquense/src/react-bootstrap/src/CardImg.js";




var CardImg_propTypes = {
  /**
   * @default 'card-img'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * Defines image position inside
   * the card.
   *
   * @type {('top'|'bottom')}
   */
  variant: prop_types_default.a.oneOf(['top', 'bottom', null]),
  as: prop_types_default.a.elementType
};
var CardImg_defaultProps = {
  variant: null
};
var CardImg = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef( // Need to define the default "as" during prop destructuring to be compatible with styled-components github.com/react-bootstrap/react-bootstrap/issues/3595
function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      className = _ref.className,
      variant = _ref.variant,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'img' : _ref$as,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "className", "variant", "as"]);

  var prefix = useBootstrapPrefix(bsPrefix, 'card-img');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({
    ref: ref,
    className: classnames_default()(variant ? prefix + "-" + variant : prefix, className)
  }, props, {
    __source: {
      fileName: CardImg_jsxFileName,
      lineNumber: 34
    },
    __self: this
  }));
});
CardImg.displayName = 'CardImg';
CardImg.propTypes = CardImg_propTypes;
CardImg.defaultProps = CardImg_defaultProps;
/* harmony default export */ var src_CardImg = (CardImg);
// CONCATENATED MODULE: ./src/Card.js


var Card_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Card.js";








var DivStyledAsH5 = divWithClassName('h5');
var DivStyledAsH6 = divWithClassName('h6');
var CardBody = createWithBsPrefix('card-body');
var Card_propTypes = {
  /**
   * @default 'card'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * Sets card background
   *
   * @type {('primary'|'secondary'|'success'|'danger'|'warning'|'info'|'dark'|'light')}
   */
  bg: prop_types_default.a.string,

  /**
   * Sets card text color
   *
   * @type {('primary'|'secondary'|'success'|'danger'|'warning'|'info'|'dark'|'light'|'white'|'muted')}
   */
  text: prop_types_default.a.string,

  /**
   * Sets card border color
   *
   * @type {('primary'|'secondary'|'success'|'danger'|'warning'|'info'|'dark'|'light')}
   */
  border: prop_types_default.a.string,

  /**
   * When this prop is set, it creates a Card with a Card.Body inside
   * passing the children directly to it
   */
  body: prop_types_default.a.bool,
  as: prop_types_default.a.elementType
};
var Card_defaultProps = {
  body: false
};
var Card = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      className = _ref.className,
      bg = _ref.bg,
      text = _ref.text,
      border = _ref.border,
      body = _ref.body,
      children = _ref.children,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'div' : _ref$as,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "className", "bg", "text", "border", "body", "children", "as"]);

  var prefix = useBootstrapPrefix(bsPrefix, 'card');
  var cardContext = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useMemo"])(function () {
    return {
      cardHeaderBsPrefix: prefix + "-header"
    };
  }, [prefix]);
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(CardContext.Provider, {
    value: cardContext,
    __source: {
      fileName: Card_jsxFileName,
      lineNumber: 81
    },
    __self: this
  }, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({
    ref: ref
  }, props, {
    className: classnames_default()(className, prefix, bg && "bg-" + bg, text && "text-" + text, border && "border-" + border),
    __source: {
      fileName: Card_jsxFileName,
      lineNumber: 82
    },
    __self: this
  }), body ? external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(CardBody, {
    __source: {
      fileName: Card_jsxFileName,
      lineNumber: 93
    },
    __self: this
  }, children) : children));
});
Card.displayName = 'Card';
Card.propTypes = Card_propTypes;
Card.defaultProps = Card_defaultProps;
Card.Img = src_CardImg;
Card.Title = createWithBsPrefix('card-title', {
  Component: DivStyledAsH5
});
Card.Subtitle = createWithBsPrefix('card-subtitle', {
  Component: DivStyledAsH6
});
Card.Body = CardBody;
Card.Link = createWithBsPrefix('card-link', {
  Component: 'a'
});
Card.Text = createWithBsPrefix('card-text', {
  Component: 'p'
});
Card.Header = createWithBsPrefix('card-header');
Card.Footer = createWithBsPrefix('card-footer');
Card.ImgOverlay = createWithBsPrefix('card-img-overlay');
/* harmony default export */ var src_Card = (Card);
// CONCATENATED MODULE: ./src/CardColumns.js

/* harmony default export */ var CardColumns = (createWithBsPrefix('card-columns'));
// CONCATENATED MODULE: ./src/CardDeck.js

/* harmony default export */ var CardDeck = (createWithBsPrefix('card-deck'));
// CONCATENATED MODULE: ./src/CardGroup.js

/* harmony default export */ var CardGroup = (createWithBsPrefix('card-group'));
// CONCATENATED MODULE: ./src/CarouselCaption.js

/* harmony default export */ var CarouselCaption = (createWithBsPrefix('carousel-caption', {
  Component: 'div'
}));
// CONCATENATED MODULE: ./src/CarouselItem.js

/* harmony default export */ var CarouselItem = (createWithBsPrefix('carousel-item'));
// CONCATENATED MODULE: ./src/ElementChildren.js

/**
 * Iterates through children that are typically specified as `props.children`,
 * but only maps over children that are "valid elements".
 *
 * The mapFunction provided index will be normalised to the components mapped,
 * so an invalid component would not increase the index.
 *
 */

function map(children, func) {
  var index = 0;
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Children.map(children, function (child) {
    return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.isValidElement(child) ? func(child, index++) : child;
  });
}
/**
 * Iterates through children that are "valid elements".
 *
 * The provided forEachFunc(child, index) will be called for each
 * leaf child with the index reflecting the position relative to "valid components".
 */


function forEach(children, func) {
  var index = 0;
  external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Children.forEach(children, function (child) {
    if (external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.isValidElement(child)) func(child, index++);
  });
}


// CONCATENATED MODULE: ./src/Carousel.js



var Carousel_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Carousel.js";













var Carousel_countChildren = function countChildren(c) {
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Children.toArray(c).filter(external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.isValidElement).length;
};

var SWIPE_THRESHOLD = 40; // TODO: `slide` should be `animate`.

var Carousel_propTypes = {
  /**
   * @default 'carousel'
   */
  bsPrefix: prop_types_default.a.string,
  as: prop_types_default.a.elementType,

  /**
   * Enables animation on the Carousel as it transitions between slides.
   */
  slide: prop_types_default.a.bool,

  /** Cross fade slides instead of the default slide animation */
  fade: prop_types_default.a.bool,

  /** Slides will loop to the start when the last one transitions */
  wrap: prop_types_default.a.bool,

  /**
   * Show a set of slide position indicators
   */
  indicators: prop_types_default.a.bool,

  /**
   * The amount of time to delay between automatically cycling an item.
   * If `null`, carousel will not automatically cycle.
   */
  interval: prop_types_default.a.number,

  /**
   * Show the Carousel previous and next arrows for changing the current slide
   */
  controls: prop_types_default.a.bool,

  /**
   * Temporarily pause the slide interval when the mouse hovers over a slide.
   */
  pauseOnHover: prop_types_default.a.bool,

  /** Enable keyboard navigation via the Arrow keys for changing slides */
  keyboard: prop_types_default.a.bool,

  /**
   * Callback fired when the active item changes.
   *
   * ```js
   * (eventKey: any, direction: 'prev' | 'next', ?event: Object) => any
   * ```
   *
   * @controllable activeIndex
   */
  onSelect: prop_types_default.a.func,

  /** A callback fired after a slide transitions in */
  onSlideEnd: prop_types_default.a.func,

  /**
   * Controls the current visible slide
   *
   * @controllable onSelect
   */
  activeIndex: prop_types_default.a.number,

  /** Override the default button icon for the "previous" control */
  prevIcon: prop_types_default.a.node,

  /**
   * Label shown to screen readers only, can be used to show the previous element
   * in the carousel.
   * Set to null to deactivate.
   */
  prevLabel: prop_types_default.a.string,

  /** Override the default button icon for the "next" control */
  nextIcon: prop_types_default.a.node,

  /**
   * Label shown to screen readers only, can be used to show the next element
   * in the carousel.
   * Set to null to deactivate.
   */
  nextLabel: prop_types_default.a.string,

  /**
   * Whether the carousel should support left/right swipe interactions on touchscreen devices.
   */
  touch: prop_types_default.a.bool
};
var Carousel_defaultProps = {
  slide: true,
  fade: false,
  interval: 5000,
  keyboard: true,
  pauseOnHover: true,
  wrap: true,
  indicators: true,
  controls: true,
  activeIndex: 0,
  prevIcon: external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("span", {
    "aria-hidden": "true",
    className: "carousel-control-prev-icon",
    __source: {
      fileName: Carousel_jsxFileName,
      lineNumber: 121
    },
    __self: undefined
  }),
  prevLabel: 'Previous',
  nextIcon: external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("span", {
    "aria-hidden": "true",
    className: "carousel-control-next-icon",
    __source: {
      fileName: Carousel_jsxFileName,
      lineNumber: 124
    },
    __self: undefined
  }),
  nextLabel: 'Next',
  touch: true
};

var Carousel_Carousel =
/*#__PURE__*/
function (_React$Component) {
  _inheritsLoose(Carousel, _React$Component);

  function Carousel() {
    var _this;

    for (var _len = arguments.length, args = new Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }

    _this = _React$Component.call.apply(_React$Component, [this].concat(args)) || this;
    _this.state = {
      prevClasses: '',
      currentClasses: 'active',
      touchStartX: 0
    };
    _this.isUnmounted = false;
    _this.carousel = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createRef();

    _this.handleTouchStart = function (e) {
      _this.setState({
        touchStartX: e.changedTouches[0].screenX
      });
    };

    _this.handleTouchEnd = function (e) {
      // If the swipe is under the threshold, don't do anything.
      if (Math.abs(e.changedTouches[0].screenX - _this.state.touchStartX) < SWIPE_THRESHOLD) return;

      if (e.changedTouches[0].screenX < _this.state.touchStartX) {
        // Swiping left to navigate to next item.
        _this.handleNext(e);
      } else {
        // Swiping right to navigate to previous item.
        _this.handlePrev(e);
      }
    };

    _this.handleSlideEnd = function () {
      var pendingIndex = _this._pendingIndex;
      _this._isSliding = false;
      _this._pendingIndex = null;
      if (pendingIndex != null) _this.to(pendingIndex);else _this.cycle();
    };

    _this.handleMouseOut = function () {
      _this.cycle();
    };

    _this.handleMouseOver = function () {
      if (_this.props.pauseOnHover) _this.pause();
    };

    _this.handleKeyDown = function (event) {
      if (/input|textarea/i.test(event.target.tagName)) return;

      switch (event.key) {
        case 'ArrowLeft':
          event.preventDefault();

          _this.handlePrev(event);

          break;

        case 'ArrowRight':
          event.preventDefault();

          _this.handleNext(event);

          break;

        default:
          break;
      }
    };

    _this.handleNextWhenVisible = function () {
      if (!_this.isUnmounted && !document.hidden && esm_css(_this.carousel.current, 'visibility') !== 'hidden') {
        _this.handleNext();
      }
    };

    _this.handleNext = function (e) {
      if (_this._isSliding) return;
      var _this$props = _this.props,
          wrap = _this$props.wrap,
          activeIndex = _this$props.activeIndex;
      var index = activeIndex + 1;
      var count = Carousel_countChildren(_this.props.children);

      if (index > count - 1) {
        if (!wrap) return;
        index = 0;
      }

      _this.select(index, e, 'next');
    };

    _this.handlePrev = function (e) {
      if (_this._isSliding) return;
      var _this$props2 = _this.props,
          wrap = _this$props2.wrap,
          activeIndex = _this$props2.activeIndex;
      var index = activeIndex - 1;

      if (index < 0) {
        if (!wrap) return;
        index = Carousel_countChildren(_this.props.children) - 1;
      }

      _this.select(index, e, 'prev');
    };

    return _this;
  }

  var _proto = Carousel.prototype;

  _proto.componentDidMount = function componentDidMount() {
    this.cycle();
  };

  Carousel.getDerivedStateFromProps = function getDerivedStateFromProps(nextProps, _ref) {
    var previousActiveIndex = _ref.activeIndex;

    if (nextProps.activeIndex !== previousActiveIndex) {
      var lastPossibleIndex = Carousel_countChildren(nextProps.children) - 1;
      var nextIndex = Math.max(0, Math.min(nextProps.activeIndex, lastPossibleIndex));
      var direction;

      if (nextIndex === 0 && previousActiveIndex >= lastPossibleIndex || previousActiveIndex <= nextIndex) {
        direction = 'next';
      } else {
        direction = 'prev';
      }

      return {
        direction: direction,
        previousActiveIndex: previousActiveIndex,
        activeIndex: nextIndex
      };
    }

    return null;
  };

  _proto.componentDidUpdate = function componentDidUpdate(_, prevState) {
    var _this2 = this;

    var _this$props3 = this.props,
        bsPrefix = _this$props3.bsPrefix,
        slide = _this$props3.slide,
        onSlideEnd = _this$props3.onSlideEnd;
    if (!slide || this.state.activeIndex === prevState.activeIndex || this._isSliding) return;
    var _this$state = this.state,
        activeIndex = _this$state.activeIndex,
        direction = _this$state.direction;
    var orderClassName, directionalClassName;

    if (direction === 'next') {
      orderClassName = bsPrefix + "-item-next";
      directionalClassName = bsPrefix + "-item-left";
    } else if (direction === 'prev') {
      orderClassName = bsPrefix + "-item-prev";
      directionalClassName = bsPrefix + "-item-right";
    }

    this._isSliding = true;
    this.pause(); // eslint-disable-next-line react/no-did-update-set-state

    this.safeSetState({
      prevClasses: 'active',
      currentClasses: orderClassName
    }, function () {
      var items = _this2.carousel.current.children;
      var nextElement = items[activeIndex];
      triggerBrowserReflow(nextElement);

      _this2.safeSetState({
        prevClasses: classnames_default()('active', directionalClassName),
        currentClasses: classnames_default()(orderClassName, directionalClassName)
      }, function () {
        return esm_transitionEnd(nextElement, function () {
          _this2.safeSetState({
            prevClasses: '',
            currentClasses: 'active'
          }, _this2.handleSlideEnd);

          if (onSlideEnd) {
            onSlideEnd();
          }
        });
      });
    });
  };

  _proto.componentWillUnmount = function componentWillUnmount() {
    clearTimeout(this.timeout);
    this.isUnmounted = true;
  };

  _proto.safeSetState = function safeSetState(state, cb) {
    var _this3 = this;

    if (this.isUnmounted) return;
    this.setState(state, function () {
      return !_this3.isUnmounted && cb();
    });
  } // This might be a public API.
  ;

  _proto.pause = function pause() {
    this._isPaused = true;
    clearInterval(this._interval);
    this._interval = null;
  };

  _proto.cycle = function cycle() {
    this._isPaused = false;
    clearInterval(this._interval);
    this._interval = null;

    if (this.props.interval && !this._isPaused) {
      this._interval = setInterval(document.visibilityState ? this.handleNextWhenVisible : this.handleNext, this.props.interval);
    }
  };

  _proto.to = function to(index, event) {
    var children = this.props.children;

    if (index < 0 || index > Carousel_countChildren(children) - 1) {
      return;
    }

    if (this._isSliding) {
      this._pendingIndex = index;
      return;
    }

    this.select(index, event);
  };

  _proto.select = function select(index, event, direction) {
    var _this4 = this;

    clearTimeout(this.selectThrottle);
    if (event && event.persist) event.persist(); // The timeout throttles fast clicks, in order to give any pending state
    // a chance to update and propagate back through props

    this.selectThrottle = setTimeout(function () {
      clearTimeout(_this4.timeout);
      var _this4$props = _this4.props,
          activeIndex = _this4$props.activeIndex,
          onSelect = _this4$props.onSelect;
      if (index === activeIndex || _this4._isSliding || _this4.isUnmounted) return;
      onSelect(index, direction || (index < activeIndex ? 'prev' : 'next'), event);
    }, 50);
  };

  _proto.renderControls = function renderControls(properties) {
    var bsPrefix = this.props.bsPrefix;
    var wrap = properties.wrap,
        children = properties.children,
        activeIndex = properties.activeIndex,
        prevIcon = properties.prevIcon,
        nextIcon = properties.nextIcon,
        prevLabel = properties.prevLabel,
        nextLabel = properties.nextLabel;
    var count = Carousel_countChildren(children);
    return [(wrap || activeIndex !== 0) && external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_SafeAnchor, {
      key: "prev",
      className: bsPrefix + "-control-prev",
      onClick: this.handlePrev,
      __source: {
        fileName: Carousel_jsxFileName,
        lineNumber: 405
      },
      __self: this
    }, prevIcon, prevLabel && external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("span", {
      className: "sr-only",
      __source: {
        fileName: Carousel_jsxFileName,
        lineNumber: 411
      },
      __self: this
    }, prevLabel)), (wrap || activeIndex !== count - 1) && external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_SafeAnchor, {
      key: "next",
      className: bsPrefix + "-control-next",
      onClick: this.handleNext,
      __source: {
        fileName: Carousel_jsxFileName,
        lineNumber: 416
      },
      __self: this
    }, nextIcon, nextLabel && external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("span", {
      className: "sr-only",
      __source: {
        fileName: Carousel_jsxFileName,
        lineNumber: 422
      },
      __self: this
    }, nextLabel))];
  };

  _proto.renderIndicators = function renderIndicators(children, activeIndex) {
    var _this5 = this;

    var bsPrefix = this.props.bsPrefix;
    var indicators = [];
    forEach(children, function (child, index) {
      indicators.push(external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("li", {
        key: index,
        className: index === activeIndex ? 'active' : null,
        onClick: function onClick(e) {
          return _this5.to(index, e);
        },
        __source: {
          fileName: Carousel_jsxFileName,
          lineNumber: 434
        },
        __self: this
      }), // Force whitespace between indicator elements. Bootstrap requires
      // this for correct spacing of elements.
      ' ');
    });
    return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("ol", {
      className: bsPrefix + "-indicators",
      __source: {
        fileName: Carousel_jsxFileName,
        lineNumber: 446
      },
      __self: this
    }, indicators);
  };

  _proto.render = function render() {
    var _this$props4 = this.props,
        _this$props4$as = _this$props4.as,
        Component = _this$props4$as === void 0 ? 'div' : _this$props4$as,
        bsPrefix = _this$props4.bsPrefix,
        slide = _this$props4.slide,
        fade = _this$props4.fade,
        indicators = _this$props4.indicators,
        controls = _this$props4.controls,
        wrap = _this$props4.wrap,
        touch = _this$props4.touch,
        prevIcon = _this$props4.prevIcon,
        prevLabel = _this$props4.prevLabel,
        nextIcon = _this$props4.nextIcon,
        nextLabel = _this$props4.nextLabel,
        className = _this$props4.className,
        children = _this$props4.children,
        keyboard = _this$props4.keyboard,
        _5 = _this$props4.activeIndex,
        _4 = _this$props4.pauseOnHover,
        _3 = _this$props4.interval,
        _2 = _this$props4.onSelect,
        _1 = _this$props4.onSlideEnd,
        props = _objectWithoutPropertiesLoose(_this$props4, ["as", "bsPrefix", "slide", "fade", "indicators", "controls", "wrap", "touch", "prevIcon", "prevLabel", "nextIcon", "nextLabel", "className", "children", "keyboard", "activeIndex", "pauseOnHover", "interval", "onSelect", "onSlideEnd"]);

    var _this$state2 = this.state,
        activeIndex = _this$state2.activeIndex,
        previousActiveIndex = _this$state2.previousActiveIndex,
        prevClasses = _this$state2.prevClasses,
        currentClasses = _this$state2.currentClasses;
    return (// eslint-disable-next-line jsx-a11y/no-static-element-interactions
      external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({
        onTouchStart: touch ? this.handleTouchStart : undefined,
        onTouchEnd: touch ? this.handleTouchEnd : undefined
      }, props, {
        className: classnames_default()(className, bsPrefix, slide && 'slide', fade && bsPrefix + "-fade"),
        onKeyDown: keyboard ? this.handleKeyDown : undefined,
        onMouseOver: this.handleMouseOver,
        onMouseOut: this.handleMouseOut,
        __source: {
          fileName: Carousel_jsxFileName,
          lineNumber: 484
        },
        __self: this
      }), indicators && this.renderIndicators(children, activeIndex), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", {
        className: bsPrefix + "-inner",
        ref: this.carousel,
        __source: {
          fileName: Carousel_jsxFileName,
          lineNumber: 500
        },
        __self: this
      }, map(children, function (child, index) {
        var current = index === activeIndex;
        var previous = index === previousActiveIndex;
        return Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["cloneElement"])(child, {
          className: classnames_default()(child.props.className, current && currentClasses, previous && prevClasses)
        });
      })), controls && this.renderControls({
        wrap: wrap,
        children: children,
        activeIndex: activeIndex,
        prevIcon: prevIcon,
        prevLabel: prevLabel,
        nextIcon: nextIcon,
        nextLabel: nextLabel
      }))
    );
  };

  return Carousel;
}(external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Component);

Carousel_Carousel.defaultProps = Carousel_defaultProps;
Carousel_Carousel.propTypes = Carousel_propTypes;
var DecoratedCarousel = createBootstrapComponent(uncontrollable(Carousel_Carousel, {
  activeIndex: 'onSelect'
}), 'carousel');
DecoratedCarousel.Caption = CarouselCaption;
DecoratedCarousel.Item = CarouselItem;
/* harmony default export */ var src_Carousel = (DecoratedCarousel);
// CONCATENATED MODULE: ./src/Col.js


var Col_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Col.js";




var DEVICE_SIZES = ['xl', 'lg', 'md', 'sm', 'xs'];
var colSize = prop_types_default.a.oneOfType([prop_types_default.a.bool, prop_types_default.a.number, prop_types_default.a.string, prop_types_default.a.oneOf(['auto'])]);
var stringOrNumber = prop_types_default.a.oneOfType([prop_types_default.a.number, prop_types_default.a.string]);
var Col_column = prop_types_default.a.oneOfType([colSize, prop_types_default.a.shape({
  size: colSize,
  order: stringOrNumber,
  offset: stringOrNumber
})]);
var Col_propTypes = {
  /**
   * @default 'col'
   */
  bsPrefix: prop_types_default.a.string,
  as: prop_types_default.a.elementType,

  /**
   * The number of columns to span on extra small devices (<576px)
   *
   * @type {(true|"auto"|number|{ span: true|"auto"|number, offset: number, order: number })}
   */
  xs: Col_column,

  /**
   * The number of columns to span on small devices (≥576px)
   *
   * @type {(true|"auto"|number|{ span: true|"auto"|number, offset: number, order: number })}
   */
  sm: Col_column,

  /**
   * The number of columns to span on medium devices (≥768px)
   *
   * @type {(true|"auto"|number|{ span: true|"auto"|number, offset: number, order: number })}
   */
  md: Col_column,

  /**
   * The number of columns to span on large devices (≥992px)
   *
   * @type {(true|"auto"|number|{ span: true|"auto"|number, offset: number, order: number })}
   */
  lg: Col_column,

  /**
   * The number of columns to span on extra large devices (≥1200px)
   *
   * @type {(true|"auto"|number|{ span: true|"auto"|number, offset: number, order: number })}
   */
  xl: Col_column
};
var Col = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef( // Need to define the default "as" during prop destructuring to be compatible with styled-components github.com/react-bootstrap/react-bootstrap/issues/3595
function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      className = _ref.className,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'div' : _ref$as,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "className", "as"]);

  var prefix = useBootstrapPrefix(bsPrefix, 'col');
  var spans = [];
  var classes = [];
  DEVICE_SIZES.forEach(function (brkPoint) {
    var propValue = props[brkPoint];
    delete props[brkPoint];
    var span, offset, order;

    if (propValue != null && typeof propValue === 'object') {
      var _propValue$span = propValue.span;
      span = _propValue$span === void 0 ? true : _propValue$span;
      offset = propValue.offset;
      order = propValue.order;
    } else {
      span = propValue;
    }

    var infix = brkPoint !== 'xs' ? "-" + brkPoint : '';
    if (span != null) spans.push(span === true ? "" + prefix + infix : "" + prefix + infix + "-" + span);
    if (order != null) classes.push("order" + infix + "-" + order);
    if (offset != null) classes.push("offset" + infix + "-" + offset);
  });

  if (!spans.length) {
    spans.push(prefix); // plain 'col'
  }

  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, props, {
    ref: ref,
    className: classnames_default.a.apply(void 0, [className].concat(spans, classes)),
    __source: {
      fileName: Col_jsxFileName,
      lineNumber: 107
    },
    __self: this
  }));
});
Col.displayName = 'Col';
Col.propTypes = Col_propTypes;
/* harmony default export */ var src_Col = (Col);
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/matches.js
var matchesImpl;
function matches(node, selector) {
  if (!matchesImpl) {
    var body = document.body;
    var nativeMatch = body.matches || body.matchesSelector || body.webkitMatchesSelector || body.mozMatchesSelector || body.msMatchesSelector;

    matchesImpl = function matchesImpl(n, s) {
      return nativeMatch.call(n, s);
    };
  }

  return matchesImpl(node, selector);
}
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/querySelectorAll.js
var toArray = Function.prototype.bind.call(Function.prototype.call, [].slice);
function qsa(element, selector) {
  return toArray(element.querySelectorAll(selector));
}
// CONCATENATED MODULE: ./node_modules/@restart/hooks/esm/usePrevious.js

/**
 * Store the last of some value. Tracked via a `Ref` only updating it
 * after the component renders.
 *
 * Helpful if you need to compare a prop value to it's previous value during render.
 *
 * ```ts
 * function Component(props) {
 *   const lastProps = usePrevious(props)
 *
 *   if (lastProps.foo !== props.foo)
 *     resetValueFromProps(props.foo)
 * }
 * ```
 *
 * @param value the value to track
 */

function usePrevious(value) {
  var ref = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useRef"])(null);
  Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useEffect"])(function () {
    ref.current = value;
  });
  return ref.current;
}
// CONCATENATED MODULE: ./node_modules/@restart/hooks/esm/useCallbackRef.js

/**
 * A convenience hook around `useState` designed to be paired with
 * the component [callback ref](https://reactjs.org/docs/refs-and-the-dom.html#callback-refs) api.
 * Callback refs are useful over `useRef()` when you need to respond to the ref being set
 * instead of lazily accessing it in an effect.
 *
 * ```ts
 * const [element, attachRef] = useCallbackRef<HTMLDivElement>()
 *
 * useEffect(() => {
 *   if (!element) return
 *
 *   const calendar = new FullCalendar.Calendar(element)
 *
 *   return () => {
 *     calendar.destroy()
 *   }
 * }, [element])
 *
 * return <div ref={attachRef} />
 * ```
 */

function useCallbackRef() {
  return Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useState"])(null);
}
// CONCATENATED MODULE: ./node_modules/@restart/hooks/esm/useForceUpdate.js

/**
 * Returns a function that triggers a component update. the hook equivalent to
 * `this.forceUpdate()` in a class component. In most cases using a state value directly
 * is preferable but may be required in some advanced usages of refs for interop or
 * when direct DOM manipulation is required.
 *
 * ```ts
 * const forceUpdate = useForceUpdate();
 *
 * const updateOnClick = useCallback(() => {
 *  forceUpdate()
 * }, [forceUpdate])
 *
 * return <button type="button" onClick={updateOnClick}>Hi there</button>
 * ```
 */

function useForceUpdate() {
  // The toggling state value is designed to defeat React optimizations for skipping
  var _useReducer = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useReducer"])(function (state) {
    return !state;
  }, false),
      dispatch = _useReducer[1];

  return dispatch;
}
// CONCATENATED MODULE: ./node_modules/react-overlays/esm/DropdownContext.js

var DropdownContext = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createContext({
  menuRef: function menuRef() {},
  toggleRef: function toggleRef() {},
  onToggle: function onToggle() {},
  toggleNode: undefined,
  alignEnd: null,
  show: null,
  drop: null
});
/* harmony default export */ var esm_DropdownContext = (DropdownContext);
// EXTERNAL MODULE: ./node_modules/popper.js/dist/esm/popper.js
var esm_popper = __webpack_require__(9);

// CONCATENATED MODULE: ./node_modules/react-overlays/esm/usePopper.js



var initialPopperStyles = {
  position: 'absolute',
  top: '0',
  left: '0',
  opacity: '0',
  pointerEvents: 'none'
};
var initialArrowStyles = {};
/**
 * Position an element relative some reference element using Popper.js
 *
 * @param {HTMLElement} referenceElement The element
 * @param {HTMLElement} popperElement
 * @param {Object}      options
 * @param {Object}      options.modifiers Popper.js modifiers
 * @param {Boolean}     options.enabled toggle the popper functionality on/off
 * @param {String}      options.placement The popper element placement relative to the reference element
 * @param {Boolean}     options.positionFixed use fixed positioning
 * @param {Boolean}     options.eventsEnabled have Popper listen on window resize events to reposition the element
 */

function usePopper(referenceElement, popperElement, _temp) {
  var _ref = _temp === void 0 ? {} : _temp,
      _ref$enabled = _ref.enabled,
      enabled = _ref$enabled === void 0 ? true : _ref$enabled,
      _ref$placement = _ref.placement,
      placement = _ref$placement === void 0 ? 'bottom' : _ref$placement,
      _ref$positionFixed = _ref.positionFixed,
      positionFixed = _ref$positionFixed === void 0 ? false : _ref$positionFixed,
      _ref$eventsEnabled = _ref.eventsEnabled,
      eventsEnabled = _ref$eventsEnabled === void 0 ? true : _ref$eventsEnabled,
      _ref$modifiers = _ref.modifiers,
      modifiers = _ref$modifiers === void 0 ? {} : _ref$modifiers;

  var popperInstanceRef = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useRef"])();
  var hasArrow = !!(modifiers.arrow && modifiers.arrow.element);
  var scheduleUpdate = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useCallback"])(function () {
    if (popperInstanceRef.current) {
      popperInstanceRef.current.scheduleUpdate();
    }
  }, []);

  var _useState = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useState"])({
    placement: placement,
    scheduleUpdate: scheduleUpdate,
    outOfBoundaries: false,
    styles: initialPopperStyles,
    arrowStyles: initialArrowStyles
  }),
      state = _useState[0],
      setState = _useState[1]; // A placement difference in state means popper determined a new placement
  // apart from the props value. By the time the popper element is rendered with
  // the new position Popper has already measured it, if the place change triggers
  // a size change it will result in a misaligned popper. So we schedule an update to be sure.


  Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useEffect"])(function () {
    scheduleUpdate();
  }, [state.placement, scheduleUpdate]);
  /** Toggle Events */

  Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useEffect"])(function () {
    if (popperInstanceRef.current) {
      // eslint-disable-next-line no-unused-expressions
      eventsEnabled ? popperInstanceRef.current.enableEventListeners() : popperInstanceRef.current.disableEventListeners();
    }
  }, [eventsEnabled]);
  Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useEffect"])(function () {
    if (!enabled || referenceElement == null || popperElement == null) {
      return undefined;
    }

    var arrow = modifiers.arrow && _extends({}, modifiers.arrow, {
      element: modifiers.arrow.element
    });

    popperInstanceRef.current = new esm_popper["a" /* default */](referenceElement, popperElement, {
      placement: placement,
      positionFixed: positionFixed,
      modifiers: _extends({}, modifiers, {
        arrow: arrow,
        applyStyle: {
          enabled: false
        },
        updateStateModifier: {
          enabled: true,
          order: 900,
          fn: function fn(data) {
            setState({
              scheduleUpdate: scheduleUpdate,
              styles: _extends({
                position: data.offsets.popper.position
              }, data.styles),
              arrowStyles: data.arrowStyles,
              outOfBoundaries: data.hide,
              placement: data.placement
            });
          }
        }
      })
    });
    return function () {
      if (popperInstanceRef.current !== null) {
        popperInstanceRef.current.destroy();
        popperInstanceRef.current = null;
      }
    }; // intentionally NOT re-running on new modifiers
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [enabled, placement, positionFixed, referenceElement, popperElement, hasArrow]);
  return state;
}
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/contains.js
/* eslint-disable no-bitwise, no-cond-assign */
// HTML DOM and SVG DOM may have different support levels,
// so we need to check on context instead of a document root element.
function contains(context, node) {
  if (context.contains) return context.contains(node);
  if (context.compareDocumentPosition) return context === node || !!(context.compareDocumentPosition(node) & 16);
}
// EXTERNAL MODULE: ./node_modules/warning/warning.js
var warning = __webpack_require__(6);
var warning_default = /*#__PURE__*/__webpack_require__.n(warning);

// CONCATENATED MODULE: ./node_modules/react-overlays/esm/useRootClose.js





var escapeKeyCode = 27;

var useRootClose_noop = function noop() {};

function isLeftClickEvent(event) {
  return event.button === 0;
}

function isModifiedEvent(event) {
  return !!(event.metaKey || event.altKey || event.ctrlKey || event.shiftKey);
}
/**
 * The `useRootClose` hook registers your callback on the document
 * when rendered. Powers the `<Overlay/>` component. This is used achieve modal
 * style behavior where your callback is triggered when the user tries to
 * interact with the rest of the document or hits the `esc` key.
 *
 * @param {Ref<HTMLElement>|HTMLElement} ref  The element boundary
 * @param {function} onRootClose
 * @param {object}  options
 * @param {boolean} options.disabled
 * @param {string}  options.clickTrigger The DOM event name (click, mousedown, etc) to attach listeners on
 */


function useRootClose(ref, onRootClose, _temp) {
  var _ref = _temp === void 0 ? {} : _temp,
      disabled = _ref.disabled,
      _ref$clickTrigger = _ref.clickTrigger,
      clickTrigger = _ref$clickTrigger === void 0 ? 'click' : _ref$clickTrigger;

  var preventMouseRootCloseRef = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useRef"])(false);
  var onClose = onRootClose || useRootClose_noop;
  var handleMouseCapture = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useCallback"])(function (e) {
    var currentTarget = ref && ('current' in ref ? ref.current : ref);
    warning_default()(!!currentTarget, 'RootClose captured a close event but does not have a ref to compare it to. ' + 'useRootClose(), should be passed a ref that resolves to a DOM node');
    preventMouseRootCloseRef.current = !currentTarget || isModifiedEvent(e) || !isLeftClickEvent(e) || contains(currentTarget, e.target);
  }, [ref]);
  var handleMouse = useEventCallback(function (e) {
    if (!preventMouseRootCloseRef.current) {
      onClose(e);
    }
  });
  var handleKeyUp = useEventCallback(function (e) {
    if (e.keyCode === escapeKeyCode) {
      onClose(e);
    }
  });
  Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useEffect"])(function () {
    if (disabled || ref == null) return undefined; // Use capture for this listener so it fires before React's listener, to
    // avoid false positives in the contains() check below if the target DOM
    // element is removed in the React mouse callback.

    var removeMouseCaptureListener = esm_listen(document, clickTrigger, handleMouseCapture, true);
    var removeMouseListener = esm_listen(document, clickTrigger, handleMouse);
    var removeKeyupListener = esm_listen(document, 'keyup', handleKeyUp);
    var mobileSafariHackListeners = [];

    if ('ontouchstart' in document.documentElement) {
      mobileSafariHackListeners = [].slice.call(document.body.children).map(function (el) {
        return esm_listen(el, 'mousemove', useRootClose_noop);
      });
    }

    return function () {
      removeMouseCaptureListener();
      removeMouseListener();
      removeKeyupListener();
      mobileSafariHackListeners.forEach(function (remove) {
        return remove();
      });
    };
  }, [ref, disabled, clickTrigger, handleMouseCapture, handleMouse, handleKeyUp]);
}

/* harmony default export */ var esm_useRootClose = (useRootClose);
// CONCATENATED MODULE: ./node_modules/react-overlays/esm/DropdownMenu.js








function useDropdownMenu(options) {
  if (options === void 0) {
    options = {};
  }

  var context = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(esm_DropdownContext);

  var _useCallbackRef = useCallbackRef(),
      arrowElement = _useCallbackRef[0],
      attachArrowRef = _useCallbackRef[1];

  var hasShownRef = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useRef"])(false);
  var _options = options,
      flip = _options.flip,
      rootCloseEvent = _options.rootCloseEvent,
      _options$popperConfig = _options.popperConfig,
      popperConfig = _options$popperConfig === void 0 ? {} : _options$popperConfig,
      _options$usePopper = _options.usePopper,
      shouldUsePopper = _options$usePopper === void 0 ? true : _options$usePopper;
  var show = context.show == null ? options.show : context.show;
  var alignEnd = context.alignEnd == null ? options.alignEnd : context.alignEnd;

  if (show && !hasShownRef.current) {
    hasShownRef.current = true;
  }

  var handleClose = function handleClose(e) {
    if (!context.toggle) return;
    context.toggle(false, e);
  };

  var drop = context.drop,
      setMenu = context.setMenu,
      menuElement = context.menuElement,
      toggleElement = context.toggleElement;
  var placement = alignEnd ? 'bottom-end' : 'bottom-start';
  if (drop === 'up') placement = alignEnd ? 'top-end' : 'top-start';else if (drop === 'right') placement = alignEnd ? 'right-end' : 'right-start';else if (drop === 'left') placement = alignEnd ? 'left-end' : 'left-start';
  var popper = usePopper(toggleElement, menuElement, {
    placement: placement,
    enabled: !!(shouldUsePopper && show),
    eventsEnabled: !!show,
    modifiers: _extends({
      flip: {
        enabled: !!flip
      },
      arrow: _extends({}, popperConfig.modifiers && popperConfig.modifiers.arrow, {
        enabled: !!arrowElement,
        element: arrowElement
      })
    }, popperConfig.modifiers)
  });
  var menu = null;
  var menuProps = {
    ref: setMenu,
    'aria-labelledby': toggleElement && toggleElement.id
  };
  var childArgs = {
    show: show,
    alignEnd: alignEnd,
    hasShown: hasShownRef.current,
    close: handleClose
  };

  if (!shouldUsePopper) {
    menu = _extends({}, childArgs, {
      props: menuProps
    });
  } else {
    menu = _extends({}, popper, {}, childArgs, {
      props: _extends({}, menuProps, {
        style: popper.styles
      }),
      arrowProps: {
        ref: attachArrowRef,
        style: popper.arrowStyles
      }
    });
  }

  esm_useRootClose(menuElement, handleClose, {
    clickTrigger: rootCloseEvent,
    disabled: !(menu && show)
  });
  return menu;
}
var DropdownMenu_propTypes = {
  /**
   * A render prop that returns a Menu element. The `props`
   * argument should spread through to **a component that can accept a ref**.
   *
   * @type {Function ({
   *   show: boolean,
   *   alignEnd: boolean,
   *   close: (?SyntheticEvent) => void,
   *   placement: Placement,
   *   outOfBoundaries: ?boolean,
   *   scheduleUpdate: () => void,
   *   props: {
   *     ref: (?HTMLElement) => void,
   *     style: { [string]: string | number },
   *     aria-labelledby: ?string
   *   },
   *   arrowProps: {
   *     ref: (?HTMLElement) => void,
   *     style: { [string]: string | number },
   *   },
   * }) => React.Element}
   */
  children: prop_types_default.a.func.isRequired,

  /**
   * Controls the visible state of the menu, generally this is
   * provided by the parent `Dropdown` component,
   * but may also be specified as a prop directly.
   */
  show: prop_types_default.a.bool,

  /**
   * Aligns the dropdown menu to the 'end' of it's placement position.
   * Generally this is provided by the parent `Dropdown` component,
   * but may also be specified as a prop directly.
   */
  alignEnd: prop_types_default.a.bool,

  /**
   * Enables the Popper.js `flip` modifier, allowing the Dropdown to
   * automatically adjust it's placement in case of overlap with the viewport or toggle.
   * Refer to the [flip docs](https://popper.js.org/popper-documentation.html#modifiers..flip.enabled) for more info
   */
  flip: prop_types_default.a.bool,
  usePopper: prop_types_default.a.oneOf([true, false]),

  /**
   * A set of popper options and props passed directly to react-popper's Popper component.
   */
  popperConfig: prop_types_default.a.object,

  /**
   * Override the default event used by RootCloseWrapper.
   */
  rootCloseEvent: prop_types_default.a.string
};
var DropdownMenu_defaultProps = {
  usePopper: true
};

function DropdownMenu(_ref) {
  var children = _ref.children,
      options = _objectWithoutPropertiesLoose(_ref, ["children"]);

  var args = useDropdownMenu(options);
  return args.hasShown ? children(args) : null;
}

DropdownMenu.displayName = 'ReactOverlaysDropdownMenu';
DropdownMenu.propTypes = DropdownMenu_propTypes;
DropdownMenu.defaultProps = DropdownMenu_defaultProps;
/** @component */

/* harmony default export */ var esm_DropdownMenu = (DropdownMenu);
// CONCATENATED MODULE: ./node_modules/react-overlays/esm/DropdownToggle.js



/**
 * Wires up Dropdown toggle functinality, returning a set a props to attach
 * to the element that functions as the dropdown toggle (generally a button).
 */

function useDropdownToggle() {
  var _useContext = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(esm_DropdownContext),
      show = _useContext.show,
      toggle = _useContext.toggle,
      setToggle = _useContext.setToggle;

  return [{
    ref: setToggle,
    'aria-haspopup': true,
    'aria-expanded': !!show
  }, {
    show: show,
    toggle: toggle
  }];
}
var DropdownToggle_propTypes = {
  /**
   * A render prop that returns a Toggle element. The `props`
   * argument should spread through to **a component that can accept a ref**. Use
   * the `onToggle` argument to toggle the menu open or closed
   *
   * @type {Function ({
   *   show: boolean,
   *   toggle: (show: boolean) => void,
   *   props: {
   *     ref: (?HTMLElement) => void,
   *     aria-haspopup: true
   *     aria-expanded: boolean
   *   },
   * }) => React.Element}
   */
  children: prop_types_default.a.func.isRequired
};

function DropdownToggle(_ref) {
  var children = _ref.children;

  var _useDropdownToggle = useDropdownToggle(),
      props = _useDropdownToggle[0],
      _useDropdownToggle$ = _useDropdownToggle[1],
      show = _useDropdownToggle$.show,
      toggle = _useDropdownToggle$.toggle;

  return children({
    show: show,
    toggle: toggle,
    props: props
  });
}

DropdownToggle.displayName = 'ReactOverlaysDropdownToggle';
DropdownToggle.propTypes = DropdownToggle_propTypes;
/** @component */

/* harmony default export */ var esm_DropdownToggle = (DropdownToggle);
// CONCATENATED MODULE: ./node_modules/react-overlays/esm/Dropdown.js












var Dropdown_propTypes = {
  /**
   * A render prop that returns the root dropdown element. The `props`
   * argument should spread through to an element containing _both_ the
   * menu and toggle in order to handle keyboard events for focus management.
   *
   * @type {Function ({
   *   props: {
   *     onKeyDown: (SyntheticEvent) => void,
   *   },
   * }) => React.Element}
   */
  children: prop_types_default.a.func.isRequired,

  /**
   * Determines the direction and location of the Menu in relation to it's Toggle.
   */
  drop: prop_types_default.a.oneOf(['up', 'left', 'right', 'down']),

  /**
   * Controls the focus behavior for when the Dropdown is opened. Set to
   * `true` to always focus the first menu item, `keyboard` to focus only when
   * navigating via the keyboard, or `false` to disable completely
   *
   * The Default behavior is `false` **unless** the Menu has a `role="menu"`
   * where it will default to `keyboard` to match the recommended [ARIA Authoring practices](https://www.w3.org/TR/wai-aria-practices-1.1/#menubutton).
   */
  focusFirstItemOnShow: prop_types_default.a.oneOf([false, true, 'keyboard']),

  /**
   * A css slector string that will return __focusable__ menu items.
   * Selectors should be relative to the menu component:
   * e.g. ` > li:not('.disabled')`
   */
  itemSelector: prop_types_default.a.string.isRequired,

  /**
   * Align the menu to the 'end' side of the placement side of the Dropdown toggle. The default placement is `top-start` or `bottom-start`.
   */
  alignEnd: prop_types_default.a.bool,

  /**
   * Whether or not the Dropdown is visible.
   *
   * @controllable onToggle
   */
  show: prop_types_default.a.bool,

  /**
   * Sets the initial show position of the Dropdown.
   */
  defaultShow: prop_types_default.a.bool,

  /**
   * A callback fired when the Dropdown wishes to change visibility. Called with the requested
   * `show` value, the DOM event, and the source that fired it: `'click'`,`'keydown'`,`'rootClose'`, or `'select'`.
   *
   * ```js
   * function(
   *   isOpen: boolean,
   *   event: SyntheticEvent,
   * ): void
   * ```
   *
   * @controllable show
   */
  onToggle: prop_types_default.a.func
};
var Dropdown_defaultProps = {
  itemSelector: '* > *'
};
/**
 * `Dropdown` is set of structural components for building, accessible dropdown menus with close-on-click,
 * keyboard navigation, and correct focus handling. As with all the react-overlay's
 * components its BYOS (bring your own styles). Dropdown is primarily
 * built from three base components, you should compose to build your Dropdowns.
 *
 * - `Dropdown`, which wraps the menu and toggle, and handles keyboard navigation
 * - `Dropdown.Toggle` generally a button that triggers the menu opening
 * - `Dropdown.Menu` The overlaid, menu, positioned to the toggle with PopperJs
 */

function Dropdown(_ref) {
  var drop = _ref.drop,
      alignEnd = _ref.alignEnd,
      defaultShow = _ref.defaultShow,
      rawShow = _ref.show,
      rawOnToggle = _ref.onToggle,
      itemSelector = _ref.itemSelector,
      focusFirstItemOnShow = _ref.focusFirstItemOnShow,
      children = _ref.children;
  var forceUpdate = useForceUpdate();

  var _useUncontrolled = useUncontrolled({
    defaultShow: defaultShow,
    show: rawShow,
    onToggle: rawOnToggle
  }, {
    show: 'onToggle'
  }),
      show = _useUncontrolled.show,
      onToggle = _useUncontrolled.onToggle;

  var _useCallbackRef = useCallbackRef(),
      toggleElement = _useCallbackRef[0],
      setToggle = _useCallbackRef[1]; // We use normal refs instead of useCallbackRef in order to populate the
  // the value as quickly as possible, otherwise the effect to focus the element
  // may run before the state value is set


  var menuRef = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useRef"])();
  var menuElement = menuRef.current;
  var setMenu = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useCallback"])(function (ref) {
    menuRef.current = ref; // ensure that a menu set triggers an update for consumers

    forceUpdate();
  }, [forceUpdate]);
  var lastShow = usePrevious(show);
  var lastSourceEvent = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useRef"])(null);
  var focusInDropdown = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useRef"])(false);
  var toggle = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useCallback"])(function (event) {
    onToggle(!show, event);
  }, [onToggle, show]);
  var context = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useMemo"])(function () {
    return {
      toggle: toggle,
      drop: drop,
      show: show,
      alignEnd: alignEnd,
      menuElement: menuElement,
      toggleElement: toggleElement,
      setMenu: setMenu,
      setToggle: setToggle
    };
  }, [toggle, drop, show, alignEnd, menuElement, toggleElement, setMenu, setToggle]);

  if (menuElement && lastShow && !show) {
    focusInDropdown.current = menuElement.contains(document.activeElement);
  }

  var focusToggle = useEventCallback(function () {
    if (toggleElement && toggleElement.focus) {
      toggleElement.focus();
    }
  });
  var maybeFocusFirst = useEventCallback(function () {
    var type = lastSourceEvent.current;
    var focusType = focusFirstItemOnShow;

    if (focusType == null) {
      focusType = menuRef.current && matches(menuRef.current, '[role=menu]') ? 'keyboard' : false;
    }

    if (focusType === false || focusType === 'keyboard' && !/^key.+$/.test(type)) {
      return;
    }

    var first = qsa(menuRef.current, itemSelector)[0];
    if (first && first.focus) first.focus();
  });
  Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useEffect"])(function () {
    if (show) maybeFocusFirst();else if (focusInDropdown.current) {
      focusInDropdown.current = false;
      focusToggle();
    } // only `show` should be changing
  }, [show, focusInDropdown, focusToggle, maybeFocusFirst]);
  Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useEffect"])(function () {
    lastSourceEvent.current = null;
  });

  var getNextFocusedChild = function getNextFocusedChild(current, offset) {
    if (!menuRef.current) return null;
    var items = qsa(menuRef.current, itemSelector);
    var index = items.indexOf(current) + offset;
    index = Math.max(0, Math.min(index, items.length));
    return items[index];
  };

  var handleKeyDown = function handleKeyDown(event) {
    var key = event.key,
        target = event.target; // Second only to https://github.com/twbs/bootstrap/blob/8cfbf6933b8a0146ac3fbc369f19e520bd1ebdac/js/src/dropdown.js#L400
    // in inscrutability

    var isInput = /input|textarea/i.test(target.tagName);

    if (isInput && (key === ' ' || key !== 'Escape' && menuRef.current && menuRef.current.contains(target))) {
      return;
    }

    lastSourceEvent.current = event.type;

    switch (key) {
      case 'ArrowUp':
        {
          var next = getNextFocusedChild(target, -1);
          if (next && next.focus) next.focus();
          event.preventDefault();
          return;
        }

      case 'ArrowDown':
        event.preventDefault();

        if (!show) {
          toggle(event);
        } else {
          var _next = getNextFocusedChild(target, 1);

          if (_next && _next.focus) _next.focus();
        }

        return;

      case 'Escape':
      case 'Tab':
        onToggle(false, event);
        break;

      default:
    }
  };

  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(esm_DropdownContext.Provider, {
    value: context
  }, children({
    props: {
      onKeyDown: handleKeyDown
    }
  }));
}

Dropdown.displayName = 'ReactOverlaysDropdown';
Dropdown.propTypes = Dropdown_propTypes;
Dropdown.defaultProps = Dropdown_defaultProps;
Dropdown.Menu = esm_DropdownMenu;
Dropdown.Toggle = esm_DropdownToggle;
/* harmony default export */ var esm_Dropdown = (Dropdown);
// CONCATENATED MODULE: ./src/NavContext.js

var NavContext = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createContext(null);
/* harmony default export */ var src_NavContext = (NavContext);
// CONCATENATED MODULE: ./src/DropdownItem.js


var DropdownItem_jsxFileName = "/Users/jquense/src/react-bootstrap/src/DropdownItem.js";








var DropdownItem_propTypes = {
  /** @default 'dropdown' */
  bsPrefix: prop_types_default.a.string,

  /**
   * Highlight the menu item as active.
   */
  active: prop_types_default.a.bool,

  /**
   * Disable the menu item, making it unselectable.
   */
  disabled: prop_types_default.a.bool,

  /**
   * Value passed to the `onSelect` handler, useful for identifying the selected menu item.
   */
  eventKey: prop_types_default.a.any,

  /**
   * HTML `href` attribute corresponding to `a.href`.
   */
  href: prop_types_default.a.string,

  /**
   * Callback fired when the menu item is clicked.
   */
  onClick: prop_types_default.a.func,

  /**
   * Callback fired when the menu item is selected.
   *
   * ```js
   * (eventKey: any, event: Object) => any
   * ```
   */
  onSelect: prop_types_default.a.func,
  as: prop_types_default.a.elementType
};
var DropdownItem_defaultProps = {
  as: src_SafeAnchor,
  disabled: false
};
var DropdownItem = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      className = _ref.className,
      children = _ref.children,
      eventKey = _ref.eventKey,
      disabled = _ref.disabled,
      href = _ref.href,
      onClick = _ref.onClick,
      onSelect = _ref.onSelect,
      propActive = _ref.active,
      Component = _ref.as,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "className", "children", "eventKey", "disabled", "href", "onClick", "onSelect", "active", "as"]);

  var prefix = useBootstrapPrefix(bsPrefix, 'dropdown-item');
  var onSelectCtx = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(src_SelectableContext);
  var navContext = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(src_NavContext);

  var _ref2 = navContext || {},
      activeKey = _ref2.activeKey;

  var key = makeEventKey(eventKey, href);
  var active = propActive == null && key != null ? makeEventKey(activeKey) === key : propActive;
  var handleClick = useEventCallback(function (event) {
    // SafeAnchor handles the disabled case, but we handle it here
    // for other components
    if (disabled) return;
    if (onClick) onClick(event);
    if (onSelectCtx) onSelectCtx(key, event);
    if (onSelect) onSelect(key, event);
  });
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, props, {
    ref: ref,
    href: href,
    disabled: disabled,
    className: classnames_default()(className, prefix, active && 'active', disabled && 'disabled'),
    onClick: handleClick,
    __source: {
      fileName: DropdownItem_jsxFileName,
      lineNumber: 96
    },
    __self: this
  }), children);
});
DropdownItem.displayName = 'DropdownItem';
DropdownItem.propTypes = DropdownItem_propTypes;
DropdownItem.defaultProps = DropdownItem_defaultProps;
/* harmony default export */ var src_DropdownItem = (DropdownItem);
// CONCATENATED MODULE: ./node_modules/@restart/hooks/esm/useMergedRefs.js


var toFnRef = function toFnRef(ref) {
  return !ref || typeof ref === 'function' ? ref : function (value) {
    ref.current = value;
  };
};

function mergeRefs(refA, refB) {
  var a = toFnRef(refA);
  var b = toFnRef(refB);
  return function (value) {
    if (a) a(value);
    if (b) b(value);
  };
}
/**
 * Create and returns a single callback ref composed from two other Refs.
 *
 * ```tsx
 * const Button = React.forwardRef((props, ref) => {
 *   const [element, attachRef] = useCallbackRef<HTMLButtonElement>();
 *   const mergedRef = useMergedRefs(ref, attachRef);
 *
 *   return <button ref={mergedRef} {...props}/>
 * })
 * ```
 *
 * @param refA A Callback or mutable Ref
 * @param refB A Callback or mutable Ref
 */

function useMergedRefs(refA, refB) {
  return Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useMemo"])(function () {
    return mergeRefs(refA, refB);
  }, [refA, refB]);
}

/* harmony default export */ var esm_useMergedRefs = (useMergedRefs);
// CONCATENATED MODULE: ./src/NavbarContext.js

/* harmony default export */ var NavbarContext = (external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createContext(null));
// CONCATENATED MODULE: ./src/useWrappedRefWithWarning.js



function useWrappedRefWithWarning(ref, componentName) {
  if (true) return ref; // eslint-disable-next-line react-hooks/rules-of-hooks

  var warningRef = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useCallback"])(function (refValue) {
    !(refValue == null || !refValue.isReactComponent) ?  false ? undefined : browser_default()(false) : void 0;
  }, [componentName]); // eslint-disable-next-line react-hooks/rules-of-hooks

  return esm_useMergedRefs(warningRef, ref);
}
// CONCATENATED MODULE: ./src/DropdownMenu.js


var DropdownMenu_jsxFileName = "/Users/jquense/src/react-bootstrap/src/DropdownMenu.js";








var src_DropdownMenu_propTypes = {
  /**
   * @default 'dropdown-menu'
   */
  bsPrefix: prop_types_default.a.string,

  /** Controls the visibility of the Dropdown menu  */
  show: prop_types_default.a.bool,

  /** Have the dropdown switch to it's opposite placement when necessary to stay on screen. */
  flip: prop_types_default.a.bool,

  /** Aligns the Dropdown menu to the right of it's container. */
  alignRight: prop_types_default.a.bool,
  onSelect: prop_types_default.a.func,

  /**
   * Which event when fired outside the component will cause it to be closed
   *
   * *Note: For custom dropdown components, you will have to pass the
   * `rootCloseEvent` to `<RootCloseWrapper>` in your custom dropdown menu
   * component ([similarly to how it is implemented in `<Dropdown.Menu>`](https://github.com/react-bootstrap/react-bootstrap/blob/v0.31.5/src/DropdownMenu.js#L115-L119)).*
   */
  rootCloseEvent: prop_types_default.a.oneOf(['click', 'mousedown']),

  /**
   * Control the rendering of the DropdownMenu. All non-menu props
   * (listed here) are passed through to the `as` Component.
   *
   * If providing a custom, non DOM, component. the `show`, `close` and `alignRight` props
   * are also injected and should be handled appropriately.
   */
  as: prop_types_default.a.elementType,

  /**
   * A set of popper options and props passed directly to react-popper's Popper component.
   */
  popperConfig: prop_types_default.a.object
};
var src_DropdownMenu_defaultProps = {
  alignRight: false,
  flip: true
};
var DropdownMenu_DropdownMenu = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      className = _ref.className,
      alignRight = _ref.alignRight,
      rootCloseEvent = _ref.rootCloseEvent,
      flip = _ref.flip,
      popperConfig = _ref.popperConfig,
      showProps = _ref.show,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'div' : _ref$as,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "className", "alignRight", "rootCloseEvent", "flip", "popperConfig", "show", "as"]);

  var isNavbar = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(NavbarContext);
  var prefix = useBootstrapPrefix(bsPrefix, 'dropdown-menu');

  var _useDropdownMenu = useDropdownMenu({
    flip: flip,
    popperConfig: popperConfig,
    rootCloseEvent: rootCloseEvent,
    show: showProps,
    alignEnd: alignRight,
    usePopper: !isNavbar
  }),
      hasShown = _useDropdownMenu.hasShown,
      placement = _useDropdownMenu.placement,
      show = _useDropdownMenu.show,
      alignEnd = _useDropdownMenu.alignEnd,
      close = _useDropdownMenu.close,
      menuProps = _useDropdownMenu.props;

  menuProps.ref = esm_useMergedRefs(menuProps.ref, useWrappedRefWithWarning(ref, 'DropdownMenu'));
  if (!hasShown) return null; // For custom components provide additional, non-DOM, props;

  if (typeof Component !== 'string') {
    menuProps.show = show;
    menuProps.close = close;
    menuProps.alignRight = alignEnd;
  }

  var style = props.style;

  if (placement) {
    // we don't need the default popper style,
    // menus are display: none when not shown.
    style = _extends({}, style, {}, menuProps.style);
    props['x-placement'] = placement;
  }

  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, props, menuProps, {
    style: style,
    className: classnames_default()(className, prefix, show && 'show', alignEnd && prefix + "-right"),
    __source: {
      fileName: DropdownMenu_jsxFileName,
      lineNumber: 111
    },
    __self: this
  }));
});
DropdownMenu_DropdownMenu.displayName = 'DropdownMenu';
DropdownMenu_DropdownMenu.propTypes = src_DropdownMenu_propTypes;
DropdownMenu_DropdownMenu.defaultProps = src_DropdownMenu_defaultProps;
/* harmony default export */ var src_DropdownMenu = (DropdownMenu_DropdownMenu);
// EXTERNAL MODULE: ./node_modules/prop-types-extra/lib/isRequiredForA11y.js
var isRequiredForA11y = __webpack_require__(4);
var isRequiredForA11y_default = /*#__PURE__*/__webpack_require__.n(isRequiredForA11y);

// CONCATENATED MODULE: ./src/DropdownToggle.js


var DropdownToggle_jsxFileName = "/Users/jquense/src/react-bootstrap/src/DropdownToggle.js";









var src_DropdownToggle_propTypes = {
  /**
   * @default 'dropdown-toggle'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * An html id attribute, necessary for assistive technologies, such as screen readers.
   * @type {string|number}
   * @required
   */
  id: isRequiredForA11y_default()(prop_types_default.a.any),
  split: prop_types_default.a.bool,
  as: prop_types_default.a.elementType,

  /**
   * to passthrough to the underlying button or whatever from DropdownButton
   * @private
   */
  childBsPrefix: prop_types_default.a.string
};
var DropdownToggle_DropdownToggle = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      split = _ref.split,
      className = _ref.className,
      children = _ref.children,
      childBsPrefix = _ref.childBsPrefix,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? src_Button : _ref$as,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "split", "className", "children", "childBsPrefix", "as"]);

  var prefix = useBootstrapPrefix(bsPrefix, 'dropdown-toggle');

  if (childBsPrefix !== undefined) {
    props.bsPrefix = childBsPrefix;
  }

  var _useDropdownToggle = useDropdownToggle(),
      toggleProps = _useDropdownToggle[0],
      toggle = _useDropdownToggle[1].toggle;

  toggleProps.ref = esm_useMergedRefs(toggleProps.ref, useWrappedRefWithWarning(ref, 'DropdownToggle')); // This intentionally forwards size and variant (if set) to the
  // underlying component, to allow it to render size and style variants.

  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({
    onClick: toggle,
    className: classnames_default()(className, prefix, split && prefix + "-split")
  }, toggleProps, props, {
    __source: {
      fileName: DropdownToggle_jsxFileName,
      lineNumber: 65
    },
    __self: this
  }), children);
});
DropdownToggle_DropdownToggle.displayName = 'DropdownToggle';
DropdownToggle_DropdownToggle.propTypes = src_DropdownToggle_propTypes;
/* harmony default export */ var src_DropdownToggle = (DropdownToggle_DropdownToggle);
// CONCATENATED MODULE: ./src/Dropdown.js


var Dropdown_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Dropdown.js";












var src_Dropdown_propTypes = {
  /** @default 'dropdown' */
  bsPrefix: prop_types_default.a.string,

  /**
   * Determines the direction and location of the Menu in relation to it's Toggle.
   */
  drop: prop_types_default.a.oneOf(['up', 'left', 'right', 'down']),
  as: prop_types_default.a.elementType,

  /**
   * Align the menu to the right side of the Dropdown toggle
   */
  alignRight: prop_types_default.a.bool,

  /**
   * Whether or not the Dropdown is visible.
   *
   * @controllable onToggle
   */
  show: prop_types_default.a.bool,

  /**
   * Allow Dropdown to flip in case of an overlapping on the reference element. For more information refer to
   * Popper.js's flip [docs](https://popper.js.org/popper-documentation.html#modifiers..flip.enabled).
   *
   */
  flip: prop_types_default.a.bool,

  /**
   * A callback fired when the Dropdown wishes to change visibility. Called with the requested
   * `show` value, the DOM event, and the source that fired it: `'click'`,`'keydown'`,`'rootClose'`, or `'select'`.
   *
   * ```js
   * function(
   *   isOpen: boolean,
   *   event: SyntheticEvent,
   *   metadata: {
   *     source: 'select' | 'click' | 'rootClose' | 'keydown'
   *   }
   * ): void
   * ```
   *
   * @controllable show
   */
  onToggle: prop_types_default.a.func,

  /**
   * A callback fired when a menu item is selected.
   *
   * ```js
   * (eventKey: any, event: Object) => any
   * ```
   */
  onSelect: prop_types_default.a.func,

  /**
   * Controls the focus behavior for when the Dropdown is opened. Set to
   * `true` to always focus the first menu item, `keyboard` to focus only when
   * navigating via the keyboard, or `false` to disable completely
   *
   * The Default behavior is `false` **unless** the Menu has a `role="menu"`
   * where it will default to `keyboard` to match the recommended [ARIA Authoring practices](https://www.w3.org/TR/wai-aria-practices-1.1/#menubutton).
   */
  focusFirstItemOnShow: prop_types_default.a.oneOf([false, true, 'keyboard']),

  /** @private */
  navbar: prop_types_default.a.bool
};
var src_Dropdown_defaultProps = {
  navbar: false
};
var Dropdown_Dropdown = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (uncontrolledProps, ref) {
  var _useUncontrolled = useUncontrolled(uncontrolledProps, {
    show: 'onToggle'
  }),
      bsPrefix = _useUncontrolled.bsPrefix,
      drop = _useUncontrolled.drop,
      show = _useUncontrolled.show,
      className = _useUncontrolled.className,
      alignRight = _useUncontrolled.alignRight,
      onSelect = _useUncontrolled.onSelect,
      onToggle = _useUncontrolled.onToggle,
      focusFirstItemOnShow = _useUncontrolled.focusFirstItemOnShow,
      _useUncontrolled$as = _useUncontrolled.as,
      Component = _useUncontrolled$as === void 0 ? 'div' : _useUncontrolled$as,
      _4 = _useUncontrolled.navbar,
      props = _objectWithoutPropertiesLoose(_useUncontrolled, ["bsPrefix", "drop", "show", "className", "alignRight", "onSelect", "onToggle", "focusFirstItemOnShow", "as", "navbar"]);

  var onSelectCtx = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(src_SelectableContext);
  var prefix = useBootstrapPrefix(bsPrefix, 'dropdown');
  var handleToggle = useEventCallback(function (nextShow, event, source) {
    if (source === void 0) {
      source = event.type;
    }

    if (event.currentTarget === document) source = 'rootClose';
    onToggle(nextShow, event, {
      source: source
    });
  });
  var handleSelect = useEventCallback(function (key, event) {
    if (onSelectCtx) onSelectCtx(key, event);
    if (onSelect) onSelect(key, event);
    handleToggle(false, event, 'select');
  });
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_SelectableContext.Provider, {
    value: handleSelect,
    __source: {
      fileName: Dropdown_jsxFileName,
      lineNumber: 121
    },
    __self: this
  }, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(esm_Dropdown, {
    drop: drop,
    show: show,
    alignEnd: alignRight,
    onToggle: handleToggle,
    focusFirstItemOnShow: focusFirstItemOnShow,
    itemSelector: "." + prefix + "-item:not(.disabled):not(:disabled)",
    __source: {
      fileName: Dropdown_jsxFileName,
      lineNumber: 122
    },
    __self: this
  }, function (_ref) {
    var dropdownProps = _ref.props;
    return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, props, dropdownProps, {
      ref: ref,
      className: classnames_default()(className, show && 'show', (!drop || drop === 'down') && prefix, drop === 'up' && 'dropup', drop === 'right' && 'dropright', drop === 'left' && 'dropleft'),
      __source: {
        fileName: Dropdown_jsxFileName,
        lineNumber: 131
      },
      __self: this
    }));
  }));
});
Dropdown_Dropdown.displayName = 'Dropdown';
Dropdown_Dropdown.propTypes = src_Dropdown_propTypes;
Dropdown_Dropdown.defaultProps = src_Dropdown_defaultProps;
Dropdown_Dropdown.Toggle = src_DropdownToggle;
Dropdown_Dropdown.Menu = src_DropdownMenu;
Dropdown_Dropdown.Item = src_DropdownItem;
Dropdown_Dropdown.Header = createWithBsPrefix('dropdown-header', {
  defaultProps: {
    role: 'heading'
  }
});
Dropdown_Dropdown.Divider = createWithBsPrefix('dropdown-divider', {
  defaultProps: {
    role: 'separator'
  }
});
/* harmony default export */ var src_Dropdown = (Dropdown_Dropdown);
// CONCATENATED MODULE: ./src/DropdownButton.js


var DropdownButton_jsxFileName = "/Users/jquense/src/react-bootstrap/src/DropdownButton.js";



var DropdownButton_propTypes = {
  /**
   * An html id attribute for the Toggle button, necessary for assistive technologies, such as screen readers.
   * @type {string|number}
   * @required
   */
  id: prop_types_default.a.any,

  /** An `href` passed to the Toggle component */
  href: prop_types_default.a.string,

  /** An `onClick` handler passed to the Toggle component */
  onClick: prop_types_default.a.func,

  /** The content of the non-toggle Button.  */
  title: prop_types_default.a.node.isRequired,

  /** Disables both Buttons  */
  disabled: prop_types_default.a.bool,

  /** An ARIA accessible role applied to the Menu component. When set to 'menu', The dropdown */
  menuRole: prop_types_default.a.string,

  /**
   *  Which event when fired outside the component will cause it to be closed.
   *
   * _see [DropdownMenu](#menu-props) for more details_
   */
  rootCloseEvent: prop_types_default.a.string,

  /** @ignore */
  bsPrefix: prop_types_default.a.string,

  /** @ignore */
  variant: prop_types_default.a.string,

  /** @ignore */
  size: prop_types_default.a.string
};
/**
 * A convenience component for simple or general use dropdowns. Renders a `Button` toggle and all `children`
 * are passed directly to the default `Dropdown.Menu`.
 *
 * _All unknown props are passed through to the `Dropdown` component._ Only
 * the Button `variant`, `size` and `bsPrefix` props are passed to the toggle,
 * along with menu related props are passed to the `Dropdown.Menu`
 */

var DropdownButton = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var title = _ref.title,
      children = _ref.children,
      bsPrefix = _ref.bsPrefix,
      rootCloseEvent = _ref.rootCloseEvent,
      variant = _ref.variant,
      size = _ref.size,
      menuRole = _ref.menuRole,
      disabled = _ref.disabled,
      href = _ref.href,
      id = _ref.id,
      props = _objectWithoutPropertiesLoose(_ref, ["title", "children", "bsPrefix", "rootCloseEvent", "variant", "size", "menuRole", "disabled", "href", "id"]);

  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_Dropdown, _extends({
    ref: ref
  }, props, {
    __source: {
      fileName: DropdownButton_jsxFileName,
      lineNumber: 69
    },
    __self: this
  }), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_Dropdown.Toggle, {
    id: id,
    href: href,
    size: size,
    variant: variant,
    disabled: disabled,
    childBsPrefix: bsPrefix,
    __source: {
      fileName: DropdownButton_jsxFileName,
      lineNumber: 70
    },
    __self: this
  }, title), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_Dropdown.Menu, {
    role: menuRole,
    rootCloseEvent: rootCloseEvent,
    __source: {
      fileName: DropdownButton_jsxFileName,
      lineNumber: 80
    },
    __self: this
  }, children));
});
DropdownButton.displayName = 'DropdownButton';
DropdownButton.propTypes = DropdownButton_propTypes;
/* harmony default export */ var src_DropdownButton = (DropdownButton);
// EXTERNAL MODULE: ./node_modules/prop-types-extra/lib/all.js
var lib_all = __webpack_require__(8);
var all_default = /*#__PURE__*/__webpack_require__.n(lib_all);

// CONCATENATED MODULE: ./src/Feedback.js


var Feedback_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Feedback.js";



var Feedback_propTypes = {
  /**
   * Specify whether the feedback is for valid or invalid fields
   *
   * @type {('valid'|'invalid')}
   */
  type: prop_types_default.a.string.isRequired,
  as: prop_types_default.a.elementType
};
var Feedback_defaultProps = {
  type: 'valid'
};
var Feedback = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef( // Need to define the default "as" during prop destructuring to be compatible with styled-components github.com/react-bootstrap/react-bootstrap/issues/3595
function (_ref, ref) {
  var _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'div' : _ref$as,
      className = _ref.className,
      type = _ref.type,
      props = _objectWithoutPropertiesLoose(_ref, ["as", "className", "type"]);

  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, props, {
    ref: ref,
    className: classnames_default()(className, type && type + "-feedback"),
    __source: {
      fileName: Feedback_jsxFileName,
      lineNumber: 22
    },
    __self: this
  }));
});
Feedback.displayName = 'Feedback';
Feedback.propTypes = Feedback_propTypes;
Feedback.defaultProps = Feedback_defaultProps;
/* harmony default export */ var src_Feedback = (Feedback);
// CONCATENATED MODULE: ./src/FormContext.js

var FormContext = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createContext({
  controlId: undefined
});
/* harmony default export */ var src_FormContext = (FormContext);
// CONCATENATED MODULE: ./src/FormCheckInput.js


var FormCheckInput_jsxFileName = "/Users/jquense/src/react-bootstrap/src/FormCheckInput.js";





var FormCheckInput_propTypes = {
  /**
   * @default 'form-check-input'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * A seperate bsPrefix used for custom controls
   *
   * @default 'custom-control'
   */
  bsCustomPrefix: prop_types_default.a.string,

  /**
   * The underlying HTML element to use when rendering the FormCheckInput.
   *
   * @type {('input'|elementType)}
   */
  as: prop_types_default.a.elementType,

  /** A HTML id attribute, necessary for proper form accessibility. */
  id: prop_types_default.a.string,

  /** The type of checkable. */
  type: prop_types_default.a.oneOf(['radio', 'checkbox']).isRequired,

  /**
   * A convenience prop shortcut for adding `position-static` to the input, for
   * correct styling when used without an FormCheckLabel
   */
  isStatic: prop_types_default.a.bool,

  /** Manually style the input as valid */
  isValid: prop_types_default.a.bool.isRequired,

  /** Manually style the input as invalid */
  isInvalid: prop_types_default.a.bool.isRequired
};
var FormCheckInput_defaultProps = {
  type: 'checkbox'
};
var FormCheckInput = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var id = _ref.id,
      bsPrefix = _ref.bsPrefix,
      bsCustomPrefix = _ref.bsCustomPrefix,
      className = _ref.className,
      isValid = _ref.isValid,
      isInvalid = _ref.isInvalid,
      isStatic = _ref.isStatic,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'input' : _ref$as,
      props = _objectWithoutPropertiesLoose(_ref, ["id", "bsPrefix", "bsCustomPrefix", "className", "isValid", "isInvalid", "isStatic", "as"]);

  var _useContext = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(src_FormContext),
      controlId = _useContext.controlId,
      custom = _useContext.custom;

  bsPrefix = custom ? useBootstrapPrefix(bsCustomPrefix, 'custom-control-input') : useBootstrapPrefix(bsPrefix, 'form-check-input');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, props, {
    ref: ref,
    id: id || controlId,
    className: classnames_default()(className, bsPrefix, isValid && 'is-valid', isInvalid && 'is-invalid', isStatic && 'position-static'),
    __source: {
      fileName: FormCheckInput_jsxFileName,
      lineNumber: 72
    },
    __self: this
  }));
});
FormCheckInput.displayName = 'FormCheckInput';
FormCheckInput.propTypes = FormCheckInput_propTypes;
FormCheckInput.defaultProps = FormCheckInput_defaultProps;
/* harmony default export */ var src_FormCheckInput = (FormCheckInput);
// CONCATENATED MODULE: ./src/FormCheckLabel.js


var FormCheckLabel_jsxFileName = "/Users/jquense/src/react-bootstrap/src/FormCheckLabel.js";





var FormCheckLabel_propTypes = {
  /**
   * @default 'form-check-input'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * A seperate bsPrefix used for custom controls
   *
   * @default 'custom-control'
   */
  bsCustomPrefix: prop_types_default.a.string,

  /** The HTML for attribute for associating the label with an input */
  htmlFor: prop_types_default.a.string
};
var FormCheckLabel = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      bsCustomPrefix = _ref.bsCustomPrefix,
      className = _ref.className,
      htmlFor = _ref.htmlFor,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "bsCustomPrefix", "className", "htmlFor"]);

  var _useContext = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(src_FormContext),
      controlId = _useContext.controlId,
      custom = _useContext.custom;

  bsPrefix = custom ? useBootstrapPrefix(bsCustomPrefix, 'custom-control-label') : useBootstrapPrefix(bsPrefix, 'form-check-label');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("label", _extends({}, props, {
    ref: ref,
    htmlFor: htmlFor || controlId,
    className: classnames_default()(className, bsPrefix),
    __source: {
      fileName: FormCheckLabel_jsxFileName,
      lineNumber: 32
    },
    __self: this
  }));
});
FormCheckLabel.displayName = 'FormCheckLabel';
FormCheckLabel.propTypes = FormCheckLabel_propTypes;
/* harmony default export */ var src_FormCheckLabel = (FormCheckLabel);
// CONCATENATED MODULE: ./src/FormCheck.js


var FormCheck_jsxFileName = "/Users/jquense/src/react-bootstrap/src/FormCheck.js";









var FormCheck_propTypes = {
  /**
   * @default 'form-check'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * A seperate bsPrefix used for custom controls
   *
   * @default 'custom-control'
   */
  bsCustomPrefix: prop_types_default.a.string,

  /**
   * The FormCheck `ref` will be forwarded to the underlying input element,
   * which means it will be a DOM node, when resolved.
   *
   * @type {ReactRef}
   * @alias ref
   */
  _ref: prop_types_default.a.any,

  /**
   * The underlying HTML element to use when rendering the FormCheck.
   *
   * @type {('input'|elementType)}
   */
  as: prop_types_default.a.elementType,

  /** A HTML id attribute, necessary for proper form accessibility. */
  id: prop_types_default.a.string,

  /**
   * Provide a function child to manually handle the layout of the FormCheck's inner components.
   *
   * ```jsx
   * <FormCheck>
   *   <FormCheck.Input isInvalid type={radio} />
   *   <FormCheck.Label>Allow us to contact you?</FormCheck.Label>
   *   <Feedback type="invalid">Yo this is required</Feedback>
   * </FormCheck>
   * ```
   */
  children: prop_types_default.a.node,
  inline: prop_types_default.a.bool,
  disabled: prop_types_default.a.bool,
  title: prop_types_default.a.string,
  label: prop_types_default.a.node,

  /** Use Bootstrap's custom form elements to replace the browser defaults */
  custom: prop_types_default.a.bool,

  /**
   * The type of checkable.
   * @type {('radio' | 'checkbox' | 'switch')}
   */
  type: all_default()(prop_types_default.a.oneOf(['radio', 'checkbox', 'switch']).isRequired, function (_ref) {
    var type = _ref.type,
        custom = _ref.custom;
    return type === 'switch' && custom === false ? Error('`custom` cannot be set to `false` when the type is `switch`') : null;
  }),

  /** Manually style the input as valid */
  isValid: prop_types_default.a.bool.isRequired,

  /** Manually style the input as invalid */
  isInvalid: prop_types_default.a.bool.isRequired,

  /** A message to display when the input is in a validation state */
  feedback: prop_types_default.a.node
};
var FormCheck_defaultProps = {
  type: 'checkbox',
  inline: false,
  disabled: false,
  isValid: false,
  isInvalid: false,
  title: ''
};
var FormCheck = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref2, ref) {
  var id = _ref2.id,
      bsPrefix = _ref2.bsPrefix,
      bsCustomPrefix = _ref2.bsCustomPrefix,
      inline = _ref2.inline,
      disabled = _ref2.disabled,
      isValid = _ref2.isValid,
      isInvalid = _ref2.isInvalid,
      feedback = _ref2.feedback,
      className = _ref2.className,
      style = _ref2.style,
      title = _ref2.title,
      type = _ref2.type,
      label = _ref2.label,
      children = _ref2.children,
      propCustom = _ref2.custom,
      _ref2$as = _ref2.as,
      as = _ref2$as === void 0 ? 'input' : _ref2$as,
      props = _objectWithoutPropertiesLoose(_ref2, ["id", "bsPrefix", "bsCustomPrefix", "inline", "disabled", "isValid", "isInvalid", "feedback", "className", "style", "title", "type", "label", "children", "custom", "as"]);

  var custom = type === 'switch' ? true : propCustom;
  bsPrefix = custom ? useBootstrapPrefix(bsCustomPrefix, 'custom-control') : useBootstrapPrefix(bsPrefix, 'form-check');

  var _useContext = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(src_FormContext),
      controlId = _useContext.controlId;

  var innerFormContext = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useMemo"])(function () {
    return {
      controlId: id || controlId,
      custom: custom
    };
  }, [controlId, custom, id]);
  var hasLabel = label != null && label !== false && !children;
  var input = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_FormCheckInput, _extends({}, props, {
    type: type === 'switch' ? 'checkbox' : type,
    ref: ref,
    isValid: isValid,
    isInvalid: isInvalid,
    isStatic: !hasLabel,
    disabled: disabled,
    as: as,
    __source: {
      fileName: FormCheck_jsxFileName,
      lineNumber: 137
    },
    __self: this
  }));
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_FormContext.Provider, {
    value: innerFormContext,
    __source: {
      fileName: FormCheck_jsxFileName,
      lineNumber: 150
    },
    __self: this
  }, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", {
    style: style,
    className: classnames_default()(className, bsPrefix, custom && "custom-" + type, inline && bsPrefix + "-inline"),
    __source: {
      fileName: FormCheck_jsxFileName,
      lineNumber: 151
    },
    __self: this
  }, children || external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Fragment, null, input, hasLabel && external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_FormCheckLabel, {
    title: title,
    __source: {
      fileName: FormCheck_jsxFileName,
      lineNumber: 164
    },
    __self: this
  }, label), (isValid || isInvalid) && external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_Feedback, {
    type: isValid ? 'valid' : 'invalid',
    __source: {
      fileName: FormCheck_jsxFileName,
      lineNumber: 167
    },
    __self: this
  }, feedback))));
});
FormCheck.displayName = 'FormCheck';
FormCheck.propTypes = FormCheck_propTypes;
FormCheck.defaultProps = FormCheck_defaultProps;
FormCheck.Input = src_FormCheckInput;
FormCheck.Label = src_FormCheckLabel;
/* harmony default export */ var src_FormCheck = (FormCheck);
// CONCATENATED MODULE: ./src/FormControl.js


var FormControl_jsxFileName = "/Users/jquense/src/react-bootstrap/src/FormControl.js";







var FormControl_propTypes = {
  /**
   * @default {'form-control'}
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * The FormControl `ref` will be forwarded to the underlying input element,
   * which means unless `as` is a composite component,
   * it will be a DOM node, when resolved.
   *
   * @type {ReactRef}
   * @alias ref
   */
  _ref: prop_types_default.a.any,

  /**
   * Input size variants
   *
   * @type {('sm'|'lg')}
   */
  size: prop_types_default.a.string,

  /**
   * The underlying HTML element to use when rendering the FormControl.
   *
   * @type {('input'|'textarea'|'select'|elementType)}
   */
  as: prop_types_default.a.elementType,

  /**
   * Render the input as plain text. Generally used along side `readOnly`.
   */
  plaintext: prop_types_default.a.bool,

  /** Make the control readonly */
  readOnly: prop_types_default.a.bool,

  /** Make the control disabled */
  disabled: prop_types_default.a.bool,

  /**
   * The `value` attribute of underlying input
   *
   * @controllable onChange
   * */
  value: prop_types_default.a.string,

  /** A callback fired when the `value` prop changes */
  onChange: prop_types_default.a.func,

  /**
   * The HTML input `type`, which is only relevant if `as` is `'input'` (the default).
   */
  type: prop_types_default.a.string,

  /**
   * Uses `controlId` from `<FormGroup>` if not explicitly specified.
   */
  id: prop_types_default.a.string,

  /** Add "valid" validation styles to the control */
  isValid: prop_types_default.a.bool,

  /** Add "invalid" validation styles to the control and accompanying label */
  isInvalid: prop_types_default.a.bool
};
var FormControl = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      type = _ref.type,
      size = _ref.size,
      id = _ref.id,
      className = _ref.className,
      isValid = _ref.isValid,
      isInvalid = _ref.isInvalid,
      plaintext = _ref.plaintext,
      readOnly = _ref.readOnly,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'input' : _ref$as,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "type", "size", "id", "className", "isValid", "isInvalid", "plaintext", "readOnly", "as"]);

  var _useContext = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(src_FormContext),
      controlId = _useContext.controlId;

  bsPrefix = useBootstrapPrefix(bsPrefix, 'form-control');
  var classes;

  if (plaintext) {
    var _classes;

    classes = (_classes = {}, _classes[bsPrefix + "-plaintext"] = true, _classes);
  } else if (type === 'file') {
    var _classes2;

    classes = (_classes2 = {}, _classes2[bsPrefix + "-file"] = true, _classes2);
  } else {
    var _classes3;

    classes = (_classes3 = {}, _classes3[bsPrefix] = true, _classes3[bsPrefix + "-" + size] = size, _classes3);
  }

   false ? undefined : void 0;
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, props, {
    type: type,
    ref: ref,
    readOnly: readOnly,
    id: id || controlId,
    className: classnames_default()(className, classes, isValid && "is-valid", isInvalid && "is-invalid"),
    __source: {
      fileName: FormControl_jsxFileName,
      lineNumber: 115
    },
    __self: this
  }));
});
FormControl.displayName = 'FormControl';
FormControl.propTypes = FormControl_propTypes;
FormControl.Feedback = src_Feedback;
/* harmony default export */ var src_FormControl = (FormControl);
// CONCATENATED MODULE: ./src/FormGroup.js


var FormGroup_jsxFileName = "/Users/jquense/src/react-bootstrap/src/FormGroup.js";





var FormGroup_propTypes = {
  /**
   * @default 'form-group'
   */
  bsPrefix: prop_types_default.a.string,
  as: prop_types_default.a.elementType,

  /**
   * Sets `id` on `<FormControl>` and `htmlFor` on `<FormGroup.Label>`.
   */
  controlId: prop_types_default.a.string,

  /**
   * The FormGroup `ref` will be forwarded to the underlying element.
   * Unless the FormGroup is rendered `as` a composite component,
   * it will be a DOM node, when resolved.
   *
   * @type {ReactRef}
   * @alias ref
   */
  _ref: prop_types_default.a.any
};
var FormGroup = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      className = _ref.className,
      children = _ref.children,
      controlId = _ref.controlId,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'div' : _ref$as,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "className", "children", "controlId", "as"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'form-group');
  var context = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useMemo"])(function () {
    return {
      controlId: controlId
    };
  }, [controlId]);
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_FormContext.Provider, {
    value: context,
    __source: {
      fileName: FormGroup_jsxFileName,
      lineNumber: 49
    },
    __self: this
  }, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, props, {
    ref: ref,
    className: classnames_default()(className, bsPrefix),
    __source: {
      fileName: FormGroup_jsxFileName,
      lineNumber: 50
    },
    __self: this
  }), children));
});
FormGroup.displayName = 'FormGroup';
FormGroup.propTypes = FormGroup_propTypes;
/* harmony default export */ var src_FormGroup = (FormGroup);
// CONCATENATED MODULE: ./src/FormLabel.js


var FormLabel_jsxFileName = "/Users/jquense/src/react-bootstrap/src/FormLabel.js";







var FormLabel_propTypes = {
  /**
   * @default 'form-label'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * Uses `controlId` from `<FormGroup>` if not explicitly specified.
   */
  htmlFor: prop_types_default.a.string,

  /**
   * Renders the FormLabel as a `<Col>` component (accepting all the same props),
   * as well as adding additional styling for horizontal forms.
   */
  column: prop_types_default.a.bool,

  /**
   * The FormLabel `ref` will be forwarded to the underlying element.
   * Unless the FormLabel is rendered `as` a composite component,
   * it will be a DOM node, when resolved.
   *
   * @type {ReactRef}
   * @alias ref
   */
  _ref: prop_types_default.a.any,

  /**
   * Hides the label visually while still allowing it to be
   * read by assistive technologies.
   */
  srOnly: prop_types_default.a.bool
};
var FormLabel_defaultProps = {
  column: false,
  srOnly: false
};
var FormLabel = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      column = _ref.column,
      srOnly = _ref.srOnly,
      className = _ref.className,
      htmlFor = _ref.htmlFor,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "column", "srOnly", "className", "htmlFor"]);

  var _useContext = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(src_FormContext),
      controlId = _useContext.controlId;

  bsPrefix = useBootstrapPrefix(bsPrefix, 'form-label');
  var classes = classnames_default()(className, bsPrefix, srOnly && 'sr-only', column && 'col-form-label');
   false ? undefined : void 0;
  htmlFor = htmlFor || controlId;
  if (column) return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_Col, _extends({
    as: "label",
    className: classes,
    htmlFor: htmlFor
  }, props, {
    __source: {
      fileName: FormLabel_jsxFileName,
      lineNumber: 70
    },
    __self: this
  }));
  return (// eslint-disable-next-line jsx-a11y/label-has-for, jsx-a11y/label-has-associated-control
    external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("label", _extends({
      ref: ref,
      className: classes,
      htmlFor: htmlFor
    }, props, {
      __source: {
        fileName: FormLabel_jsxFileName,
        lineNumber: 75
      },
      __self: this
    }))
  );
});
FormLabel.displayName = 'FormLabel';
FormLabel.propTypes = FormLabel_propTypes;
FormLabel.defaultProps = FormLabel_defaultProps;
/* harmony default export */ var src_FormLabel = (FormLabel);
// CONCATENATED MODULE: ./src/FormText.js


var FormText_jsxFileName = "/Users/jquense/src/react-bootstrap/src/FormText.js";




var FormText_propTypes = {
  /** @default 'form-text' */
  bsPrefix: prop_types_default.a.string,

  /**
   * The FormText `ref` will be forwarded to the underlying element.
   * Unless the FormText is rendered `as` a composite component,
   * it will be a DOM node, when resolved.
   *
   * @type {ReactRef}
   * @alias ref
   */
  _ref: prop_types_default.a.any,

  /**
   * A convenience prop for add the `text-muted` class,
   * since it's so commonly used here.
   */
  muted: prop_types_default.a.bool,
  as: prop_types_default.a.elementType
};
var FormText = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef( // Need to define the default "as" during prop destructuring to be compatible with styled-components github.com/react-bootstrap/react-bootstrap/issues/3595
function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      className = _ref.className,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'small' : _ref$as,
      muted = _ref.muted,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "className", "as", "muted"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'form-text');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, props, {
    ref: ref,
    className: classnames_default()(className, bsPrefix, muted && 'text-muted'),
    __source: {
      fileName: FormText_jsxFileName,
      lineNumber: 36
    },
    __self: this
  }));
});
FormText.displayName = 'FormText';
FormText.propTypes = FormText_propTypes;
/* harmony default export */ var src_FormText = (FormText);
// CONCATENATED MODULE: ./src/Switch.js

var Switch_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Switch.js";


var Switch = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (props, ref) {
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_FormCheck, _extends({}, props, {
    ref: ref,
    type: "switch",
    __source: {
      fileName: Switch_jsxFileName,
      lineNumber: 5
    },
    __self: this
  }));
});
Switch.displayName = 'Switch';
Switch.Input = src_FormCheck.Input;
Switch.Label = src_FormCheck.Label;
/* harmony default export */ var src_Switch = (Switch);
// CONCATENATED MODULE: ./src/Form.js


var Form_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Form.js";











var Form_propTypes = {
  /**
   * @default {'form'}
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * The Form `ref` will be forwarded to the underlying element,
   * which means, unless it's rendered `as` a composite component,
   * it will be a DOM node, when resolved.
   *
   * @type {ReactRef}
   * @alias ref
   */
  _ref: prop_types_default.a.any,

  /**
   * Display the series of labels, form controls,
   * and buttons on a single horizontal row
   */
  inline: prop_types_default.a.bool,

  /**
   * Mark a form as having been validated. Setting it to `true` will
   * toggle any validation styles on the forms elements.
   */
  validated: prop_types_default.a.bool,
  as: prop_types_default.a.elementType
};
var Form_defaultProps = {
  inline: false
};
var Form = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      inline = _ref.inline,
      className = _ref.className,
      validated = _ref.validated,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'form' : _ref$as,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "inline", "className", "validated", "as"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'form');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, props, {
    ref: ref,
    className: classnames_default()(className, validated && 'was-validated', inline && bsPrefix + "-inline"),
    __source: {
      fileName: Form_jsxFileName,
      lineNumber: 62
    },
    __self: this
  }));
});
Form.displayName = 'Form';
Form.propTypes = Form_propTypes;
Form.defaultProps = Form_defaultProps;
Form.Row = createWithBsPrefix('form-row');
Form.Group = src_FormGroup;
Form.Control = src_FormControl;
Form.Check = src_FormCheck;
Form.Switch = src_Switch;
Form.Label = src_FormLabel;
Form.Text = src_FormText;
/* harmony default export */ var src_Form = (Form);
// CONCATENATED MODULE: ./src/Container.js


var Container_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Container.js";




var Container_propTypes = {
  /**
   * @default 'container'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * Allow the Container to fill all of its available horizontal space.
   */
  fluid: prop_types_default.a.bool,

  /**
   * You can use a custom element for this component
   */
  as: prop_types_default.a.elementType
};
var Container_defaultProps = {
  fluid: false
};
var Container = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef( // Need to define the default "as" during prop destructuring to be compatible with styled-components github.com/react-bootstrap/react-bootstrap/issues/3595
function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      fluid = _ref.fluid,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'div' : _ref$as,
      className = _ref.className,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "fluid", "as", "className"]);

  var prefix = useBootstrapPrefix(bsPrefix, 'container');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({
    ref: ref
  }, props, {
    className: classnames_default()(className, fluid ? prefix + "-fluid" : prefix),
    __source: {
      fileName: Container_jsxFileName,
      lineNumber: 32
    },
    __self: this
  }));
});
Container.displayName = 'Container';
Container.propTypes = Container_propTypes;
Container.defaultProps = Container_defaultProps;
/* harmony default export */ var src_Container = (Container);
// CONCATENATED MODULE: ./src/Image.js


var Image_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Image.js";




var Image_propTypes = {
  /**
   * @default 'img'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * Sets image as fluid image.
   */
  fluid: prop_types_default.a.bool,

  /**
   * Sets image shape as rounded.
   */
  rounded: prop_types_default.a.bool,

  /**
   * Sets image shape as circle.
   */
  roundedCircle: prop_types_default.a.bool,

  /**
   * Sets image shape as thumbnail.
   */
  thumbnail: prop_types_default.a.bool
};
var Image_defaultProps = {
  fluid: false,
  rounded: false,
  roundedCircle: false,
  thumbnail: false
};
var Image = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      className = _ref.className,
      fluid = _ref.fluid,
      rounded = _ref.rounded,
      roundedCircle = _ref.roundedCircle,
      thumbnail = _ref.thumbnail,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "className", "fluid", "rounded", "roundedCircle", "thumbnail"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'img');
  var classes = classnames_default()(fluid && bsPrefix + "-fluid", rounded && "rounded", roundedCircle && "rounded-circle", thumbnail && bsPrefix + "-thumbnail");
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("img", _extends({
    // eslint-disable-line jsx-a11y/alt-text
    ref: ref
  }, props, {
    className: classnames_default()(className, classes),
    __source: {
      fileName: Image_jsxFileName,
      lineNumber: 55
    },
    __self: this
  }));
});
Image.displayName = 'Image';
Image.propTypes = Image_propTypes;
Image.defaultProps = Image_defaultProps;
/* harmony default export */ var src_Image = (Image);
// CONCATENATED MODULE: ./src/FigureImage.js


var FigureImage_jsxFileName = "/Users/jquense/src/react-bootstrap/src/FigureImage.js";




var FigureImage_propTypes = {
  /**
   * @default 'img'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * Sets image as fluid image.
   */
  fluid: prop_types_default.a.bool,

  /**
   * Sets image shape as rounded.
   */
  rounded: prop_types_default.a.bool,

  /**
   * Sets image shape as circle.
   */
  roundedCircle: prop_types_default.a.bool,

  /**
   * Sets image shape as thumbnail.
   */
  thumbnail: prop_types_default.a.bool
};
var FigureImage_defaultProps = {
  fluid: true
};
var FigureImage = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var className = _ref.className,
      props = _objectWithoutPropertiesLoose(_ref, ["className"]);

  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_Image, _extends({
    ref: ref
  }, props, {
    className: classnames_default()(className, 'figure-img'),
    __source: {
      fileName: FigureImage_jsxFileName,
      lineNumber: 37
    },
    __self: this
  }));
});
FigureImage.displayName = 'FigureImage';
FigureImage.propTypes = FigureImage_propTypes;
FigureImage.defaultProps = FigureImage_defaultProps;
/* harmony default export */ var src_FigureImage = (FigureImage);
// CONCATENATED MODULE: ./src/FigureCaption.js

var FigureCaption = createWithBsPrefix('figure-caption', {
  Component: 'figcaption'
});
/* harmony default export */ var src_FigureCaption = (FigureCaption);
// CONCATENATED MODULE: ./src/Figure.js



var Figure = createWithBsPrefix('figure', {
  Component: 'figure'
});
Figure.Image = src_FigureImage;
Figure.Caption = src_FigureCaption;
/* harmony default export */ var src_Figure = (Figure);
// CONCATENATED MODULE: ./src/InputGroup.js


var InputGroup_jsxFileName = "/Users/jquense/src/react-bootstrap/src/InputGroup.js";





var InputGroup_propTypes = {
  /** @default 'input-group' */
  bsPrefix: prop_types_default.a.string,

  /**
   * Control the size of buttons and form elements from the top-level .
   *
   * @type {('sm'|'lg')}
   */
  size: prop_types_default.a.string,
  as: prop_types_default.a.elementType
};
/**
 *
 * @property {InputGroupAppend} Append
 * @property {InputGroupPrepend} Prepend
 * @property {InputGroupText} Text
 * @property {InputGroupRadio} Radio
 * @property {InputGroupCheckbox} Checkbox
 */

var InputGroup = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      size = _ref.size,
      className = _ref.className,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'div' : _ref$as,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "size", "className", "as"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'input-group');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({
    ref: ref
  }, props, {
    className: classnames_default()(className, bsPrefix, size && bsPrefix + "-" + size),
    __source: {
      fileName: InputGroup_jsxFileName,
      lineNumber: 46
    },
    __self: this
  }));
});
var InputGroupAppend = createWithBsPrefix('input-group-append');
var InputGroupPrepend = createWithBsPrefix('input-group-prepend');
var InputGroupText = createWithBsPrefix('input-group-text', {
  Component: 'span'
});

var InputGroup_InputGroupCheckbox = function InputGroupCheckbox(props) {
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(InputGroupText, {
    __source: {
      fileName: InputGroup_jsxFileName,
      lineNumber: 68
    },
    __self: this
  }, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("input", _extends({
    type: "checkbox"
  }, props, {
    __source: {
      fileName: InputGroup_jsxFileName,
      lineNumber: 69
    },
    __self: this
  })));
};

var InputGroup_InputGroupRadio = function InputGroupRadio(props) {
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(InputGroupText, {
    __source: {
      fileName: InputGroup_jsxFileName,
      lineNumber: 74
    },
    __self: this
  }, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("input", _extends({
    type: "radio"
  }, props, {
    __source: {
      fileName: InputGroup_jsxFileName,
      lineNumber: 75
    },
    __self: this
  })));
};

InputGroup.propTypes = InputGroup_propTypes;
InputGroup.displayName = 'InputGroup';
InputGroup.Text = InputGroupText;
InputGroup.Radio = InputGroup_InputGroupRadio;
InputGroup.Checkbox = InputGroup_InputGroupCheckbox;
InputGroup.Append = InputGroupAppend;
InputGroup.Prepend = InputGroupPrepend;
/* harmony default export */ var src_InputGroup = (InputGroup);
// CONCATENATED MODULE: ./src/Jumbotron.js


var Jumbotron_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Jumbotron.js";




var Jumbotron_propTypes = {
  as: prop_types_default.a.elementType,

  /** Make the jumbotron full width, and without rounded corners */
  fluid: prop_types_default.a.bool,

  /** @default 'jumbotron' */
  bsPrefix: prop_types_default.a.string
};
var Jumbotron_defaultProps = {
  fluid: false
};
var Jumbotron = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var _classes;

  var _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'div' : _ref$as,
      className = _ref.className,
      fluid = _ref.fluid,
      bsPrefix = _ref.bsPrefix,
      props = _objectWithoutPropertiesLoose(_ref, ["as", "className", "fluid", "bsPrefix"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'jumbotron');
  var classes = (_classes = {}, _classes[bsPrefix] = true, _classes[bsPrefix + "-fluid"] = fluid, _classes);
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({
    ref: ref
  }, props, {
    className: classnames_default()(className, classes),
    __source: {
      fileName: Jumbotron_jsxFileName,
      lineNumber: 37
    },
    __self: this
  }));
});
Jumbotron.propTypes = Jumbotron_propTypes;
Jumbotron.defaultProps = Jumbotron_defaultProps;
Jumbotron.displayName = 'Jumbotron';
/* harmony default export */ var src_Jumbotron = (Jumbotron);
// CONCATENATED MODULE: ./src/TabContext.js

var TabContext = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createContext(null);
/* harmony default export */ var src_TabContext = (TabContext);
// CONCATENATED MODULE: ./src/AbstractNav.js


var AbstractNav_jsxFileName = "/Users/jquense/src/react-bootstrap/src/AbstractNav.js";









var AbstractNav_noop = function noop() {};

var AbstractNav_propTypes = {
  onSelect: prop_types_default.a.func.isRequired,
  as: prop_types_default.a.elementType,
  role: prop_types_default.a.string,

  /** @private */
  onKeyDown: prop_types_default.a.func,

  /** @private */
  parentOnSelect: prop_types_default.a.func,

  /** @private */
  getControlledId: prop_types_default.a.func,

  /** @private */
  getControllerId: prop_types_default.a.func,

  /** @private */
  activeKey: prop_types_default.a.any
};
var AbstractNav = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'ul' : _ref$as,
      onSelect = _ref.onSelect,
      activeKey = _ref.activeKey,
      role = _ref.role,
      onKeyDown = _ref.onKeyDown,
      props = _objectWithoutPropertiesLoose(_ref, ["as", "onSelect", "activeKey", "role", "onKeyDown"]);

  // A ref and forceUpdate for refocus, b/c we only want to trigger when needed
  // and don't want to reset the set in the effect
  var forceUpdate = useForceUpdate();
  var needsRefocusRef = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useRef"])(false);
  var parentOnSelect = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(src_SelectableContext);
  var tabContext = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(src_TabContext);
  var getControlledId, getControllerId;

  if (tabContext) {
    role = role || 'tablist';
    activeKey = tabContext.activeKey;
    getControlledId = tabContext.getControlledId;
    getControllerId = tabContext.getControllerId;
  }

  var listNode = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useRef"])(null);

  var getNextActiveChild = function getNextActiveChild(offset) {
    if (!listNode.current) return null;
    var items = qsa(listNode.current, '[data-rb-event-key]:not(.disabled)');
    var activeChild = listNode.current.querySelector('.active');
    var index = items.indexOf(activeChild);
    if (index === -1) return null;
    var nextIndex = index + offset;
    if (nextIndex >= items.length) nextIndex = 0;
    if (nextIndex < 0) nextIndex = items.length - 1;
    return items[nextIndex];
  };

  var handleSelect = function handleSelect(key, event) {
    if (key == null) return;
    if (onSelect) onSelect(key, event);
    if (parentOnSelect) parentOnSelect(key, event);
  };

  var handleKeyDown = function handleKeyDown(event) {
    if (onKeyDown) onKeyDown(event);
    var nextActiveChild;

    switch (event.key) {
      case 'ArrowLeft':
      case 'ArrowUp':
        nextActiveChild = getNextActiveChild(-1);
        break;

      case 'ArrowRight':
      case 'ArrowDown':
        nextActiveChild = getNextActiveChild(1);
        break;

      default:
        return;
    }

    if (!nextActiveChild) return;
    event.preventDefault();
    handleSelect(nextActiveChild.dataset.rbEventKey, event);
    needsRefocusRef.current = true;
    forceUpdate();
  };

  Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useEffect"])(function () {
    if (listNode.current && needsRefocusRef.current) {
      var activeChild = listNode.current.querySelector('[data-rb-event-key].active');
      if (activeChild) activeChild.focus();
    }

    needsRefocusRef.current = false;
  });
  var mergedRef = esm_useMergedRefs(ref, listNode);
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_SelectableContext.Provider, {
    value: handleSelect,
    __source: {
      fileName: AbstractNav_jsxFileName,
      lineNumber: 123
    },
    __self: this
  }, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_NavContext.Provider, {
    value: {
      role: role,
      // used by NavLink to determine it's role
      activeKey: makeEventKey(activeKey),
      getControlledId: getControlledId || AbstractNav_noop,
      getControllerId: getControllerId || AbstractNav_noop
    },
    __source: {
      fileName: AbstractNav_jsxFileName,
      lineNumber: 124
    },
    __self: this
  }, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, props, {
    onKeyDown: handleKeyDown,
    ref: mergedRef,
    role: role,
    __source: {
      fileName: AbstractNav_jsxFileName,
      lineNumber: 132
    },
    __self: this
  }))));
});
AbstractNav.propTypes = AbstractNav_propTypes;
/* harmony default export */ var src_AbstractNav = (AbstractNav);
// CONCATENATED MODULE: ./src/AbstractNavItem.js


var AbstractNavItem_jsxFileName = "/Users/jquense/src/react-bootstrap/src/AbstractNavItem.js";






var AbstractNavItem_propTypes = {
  active: prop_types_default.a.bool,
  role: prop_types_default.a.string,
  href: prop_types_default.a.string,
  tabIndex: prop_types_default.a.string,
  eventKey: prop_types_default.a.any,
  onclick: prop_types_default.a.func,
  as: prop_types_default.a.any,
  onClick: prop_types_default.a.func,
  onSelect: prop_types_default.a.func
};
var AbstractNavItem_defaultProps = {
  disabled: false
};
var AbstractNavItem = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var active = _ref.active,
      className = _ref.className,
      tabIndex = _ref.tabIndex,
      eventKey = _ref.eventKey,
      onSelect = _ref.onSelect,
      onClick = _ref.onClick,
      Component = _ref.as,
      props = _objectWithoutPropertiesLoose(_ref, ["active", "className", "tabIndex", "eventKey", "onSelect", "onClick", "as"]);

  var navKey = makeEventKey(eventKey, props.href);
  var parentOnSelect = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(src_SelectableContext);
  var navContext = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(src_NavContext);
  var isActive = active;

  if (navContext) {
    if (!props.role && navContext.role === 'tablist') props.role = 'tab';
    props['data-rb-event-key'] = navKey;
    props.id = navContext.getControllerId(navKey);
    props['aria-controls'] = navContext.getControlledId(navKey);
    isActive = active == null && navKey != null ? navContext.activeKey === navKey : active;
  }

  if (props.role === 'tab') {
    props.tabIndex = isActive ? tabIndex : -1;
    props['aria-selected'] = isActive;
  }

  var handleOnclick = useEventCallback(function (e) {
    if (onClick) onClick(e);
    if (navKey == null) return;
    if (onSelect) onSelect(navKey, e);
    if (parentOnSelect) parentOnSelect(navKey, e);
  });
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, props, {
    ref: ref,
    onClick: handleOnclick,
    className: classnames_default()(className, isActive && 'active'),
    __source: {
      fileName: AbstractNavItem_jsxFileName,
      lineNumber: 72
    },
    __self: this
  }));
});
AbstractNavItem.propTypes = AbstractNavItem_propTypes;
AbstractNavItem.defaultProps = AbstractNavItem_defaultProps;
/* harmony default export */ var src_AbstractNavItem = (AbstractNavItem);
// CONCATENATED MODULE: ./src/ListGroupItem.js


var ListGroupItem_jsxFileName = "/Users/jquense/src/react-bootstrap/src/ListGroupItem.js";






var ListGroupItem_propTypes = {
  /**
   * @default 'list-group-item'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * Sets contextual classes for list item
   * @type {('primary'|'secondary'|'success'|'danger'|'warning'|'info'|'dark'|'light')}
   */
  variant: prop_types_default.a.string,

  /**
   * Marks a ListGroupItem as actionable, applying additional hover, active and disabled styles
   * for links and buttons.
   */
  action: prop_types_default.a.bool,

  /**
   * Sets list item as active
   */
  active: prop_types_default.a.bool,

  /**
   * Sets list item state as disabled
   */
  disabled: prop_types_default.a.bool,
  eventKey: prop_types_default.a.string,
  onClick: prop_types_default.a.func,
  href: prop_types_default.a.string,

  /**
   * You can use a custom element type for this component. For none `action` items, items render as `li`.
   * For actions the default is an achor or button element depending on whether a `href` is provided.
   *
   * @default {'div' | 'a' | 'button'}
   */
  as: prop_types_default.a.elementType
};
var ListGroupItem_defaultProps = {
  variant: null,
  active: false,
  disabled: false
};
var ListGroupItem = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      active = _ref.active,
      disabled = _ref.disabled,
      className = _ref.className,
      variant = _ref.variant,
      action = _ref.action,
      as = _ref.as,
      eventKey = _ref.eventKey,
      onClick = _ref.onClick,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "active", "disabled", "className", "variant", "action", "as", "eventKey", "onClick"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'list-group-item');
  var handleClick = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useCallback"])(function (event) {
    if (disabled) {
      event.preventDefault();
      event.stopPropagation();
      return;
    }

    if (onClick) onClick(event);
  }, [disabled, onClick]);
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_AbstractNavItem, _extends({
    ref: ref
  }, props, {
    eventKey: makeEventKey(eventKey, props.href) // eslint-disable-next-line
    ,
    as: as || (action ? props.href ? 'a' : 'button' : 'div'),
    onClick: handleClick,
    className: classnames_default()(className, bsPrefix, active && 'active', disabled && 'disabled', variant && bsPrefix + "-" + variant, action && bsPrefix + "-action"),
    __source: {
      fileName: ListGroupItem_jsxFileName,
      lineNumber: 88
    },
    __self: this
  }));
});
ListGroupItem.propTypes = ListGroupItem_propTypes;
ListGroupItem.defaultProps = ListGroupItem_defaultProps;
ListGroupItem.displayName = 'ListGroupItem';
/* harmony default export */ var src_ListGroupItem = (ListGroupItem);
// CONCATENATED MODULE: ./src/ListGroup.js


var ListGroup_jsxFileName = "/Users/jquense/src/react-bootstrap/src/ListGroup.js";








var ListGroup_propTypes = {
  /**
   * @default 'list-group'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * Adds a variant to the list-group
   *
   * @type {('flush')}
   */
  variant: prop_types_default.a.oneOf(['flush', null]),

  /**
   * Changes the flow of the list group items from vertical to horizontal.
   * A value of `null` (the default) sets it to vertical for all breakpoints;
   * Just including the prop sets it for all breakpoints, while `{sm|md|lg|xl}`
   * makes the list group horizontal starting at that breakpoint’s `min-width`.
   * @type {(true|'sm'|'md'|'lg'|'xl')}
   */
  horizontal: prop_types_default.a.oneOf([true, 'sm', 'md', 'lg', 'xl', null]),

  /**
   * You can use a custom element type for this component.
   */
  as: prop_types_default.a.elementType
};
var ListGroup_defaultProps = {
  variant: null,
  horizontal: null
};
var ListGroup = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (props, ref) {
  var _useUncontrolled = useUncontrolled(props, {
    activeKey: 'onSelect'
  }),
      className = _useUncontrolled.className,
      bsPrefix = _useUncontrolled.bsPrefix,
      variant = _useUncontrolled.variant,
      horizontal = _useUncontrolled.horizontal,
      _useUncontrolled$as = _useUncontrolled.as,
      as = _useUncontrolled$as === void 0 ? 'div' : _useUncontrolled$as,
      controlledProps = _objectWithoutPropertiesLoose(_useUncontrolled, ["className", "bsPrefix", "variant", "horizontal", "as"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'list-group');
  var horizontalVariant;

  if (horizontal) {
    horizontalVariant = horizontal === true ? 'horizontal' : "horizontal-" + horizontal;
  } else {
    horizontalVariant = null;
  }

   false ? undefined : void 0;
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_AbstractNav, _extends({
    ref: ref
  }, controlledProps, {
    as: as,
    className: classnames_default()(className, bsPrefix, variant && bsPrefix + "-" + variant, horizontalVariant && bsPrefix + "-" + horizontalVariant),
    __source: {
      fileName: ListGroup_jsxFileName,
      lineNumber: 74
    },
    __self: this
  }));
});
ListGroup.propTypes = ListGroup_propTypes;
ListGroup.defaultProps = ListGroup_defaultProps;
ListGroup.displayName = 'ListGroup';
ListGroup.Item = src_ListGroupItem;
/* harmony default export */ var src_ListGroup = (ListGroup);
// CONCATENATED MODULE: ./src/Media.js


var Media_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Media.js";





var Media_propTypes = {
  /**
   * @default 'media'
   */
  bsPrefix: prop_types_default.a.string,
  as: prop_types_default.a.elementType
};
var Media = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef( // Need to define the default "as" during prop destructuring to be compatible with styled-components github.com/react-bootstrap/react-bootstrap/issues/3595
function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      className = _ref.className,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'div' : _ref$as,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "className", "as"]);

  var prefix = useBootstrapPrefix(bsPrefix, 'media');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, props, {
    ref: ref,
    className: classnames_default()(className, prefix),
    __source: {
      fileName: Media_jsxFileName,
      lineNumber: 23
    },
    __self: this
  }));
});
Media.displayName = 'Media';
Media.propTypes = Media_propTypes;
Media.Body = createWithBsPrefix('media-body');
/* harmony default export */ var src_Media = (Media);
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/scrollbarSize.js

var scrollbarSize_size;
function scrollbarSize(recalc) {
  if (!scrollbarSize_size && scrollbarSize_size !== 0 || recalc) {
    if (canUseDOM) {
      var scrollDiv = document.createElement('div');
      scrollDiv.style.position = 'absolute';
      scrollDiv.style.top = '-9999px';
      scrollDiv.style.width = '50px';
      scrollDiv.style.height = '50px';
      scrollDiv.style.overflow = 'scroll';
      document.body.appendChild(scrollDiv);
      scrollbarSize_size = scrollDiv.offsetWidth - scrollDiv.clientWidth;
      document.body.removeChild(scrollDiv);
    }
  }

  return scrollbarSize_size;
}
// CONCATENATED MODULE: ./node_modules/@babel/runtime/helpers/esm/assertThisInitialized.js
function _assertThisInitialized(self) {
  if (self === void 0) {
    throw new ReferenceError("this hasn't been initialised - super() hasn't been called");
  }

  return self;
}
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/activeElement.js

/**
 * Return the actively focused element safely.
 *
 * @param doc the document to checl
 */

function activeElement(doc) {
  if (doc === void 0) {
    doc = ownerDocument();
  } // Support: IE 9 only
  // IE9 throws an "Unspecified error" accessing document.activeElement from an <iframe>


  try {
    var active = doc.activeElement; // IE11 returns a seemingly empty object in some cases when accessing
    // document.activeElement from an <iframe>

    if (!active || !active.nodeName) return null;
    return active;
  } catch (e) {
    /* ie throws if no active element */
    return doc.body;
  }
}
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/hasClass.js
function hasClass(element, className) {
  if (element.classList) return !!className && element.classList.contains(className);
  return (" " + (element.className.baseVal || element.className) + " ").indexOf(" " + className + " ") !== -1;
}
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/addClass.js

function addClass(element, className) {
  if (element.classList) element.classList.add(className);else if (!hasClass(element, className)) if (typeof element.className === 'string') element.className = element.className + " " + className;else element.setAttribute('class', (element.className && element.className.baseVal || '') + " " + className);
}
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/removeClass.js
function replaceClassName(origClass, classToRemove) {
  return origClass.replace(new RegExp("(^|\\s)" + classToRemove + "(?:\\s|$)", 'g'), '$1').replace(/\s+/g, ' ').replace(/^\s*|\s*$/g, '');
}

function removeClass(element, className) {
  if (element.classList) {
    element.classList.remove(className);
  } else if (typeof element.className === 'string') {
    ;
    element.className = replaceClassName(element.className, className);
  } else {
    element.setAttribute('class', replaceClassName(element.className && element.className.baseVal || '', className));
  }
}
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/isDocument.js
function isDocument(element) {
  return 'nodeType' in element && element.nodeType === document.DOCUMENT_NODE;
}
// CONCATENATED MODULE: ./node_modules/dom-helpers/esm/isWindow.js

function isWindow(node) {
  if ('window' in node && node.window === node) return node;
  if (isDocument(node)) return node.defaultView || false;
  return false;
}
// CONCATENATED MODULE: ./node_modules/react-overlays/esm/utils/isOverflowing.js



function isBody(node) {
  return node && node.tagName.toLowerCase() === 'body';
}

function bodyIsOverflowing(node) {
  var doc = ownerDocument(node);
  var win = isWindow(doc);
  return doc.body.clientWidth < win.innerWidth;
}

function isOverflowing(container) {
  var win = isWindow(container);
  return win || isBody(container) ? bodyIsOverflowing(container) : container.scrollHeight > container.clientHeight;
}
// CONCATENATED MODULE: ./node_modules/react-overlays/esm/utils/manageAriaHidden.js
var BLACKLIST = ['template', 'script', 'style'];

var isHidable = function isHidable(_ref) {
  var nodeType = _ref.nodeType,
      tagName = _ref.tagName;
  return nodeType === 1 && BLACKLIST.indexOf(tagName.toLowerCase()) === -1;
};

var siblings = function siblings(container, exclude, cb) {
  exclude = [].concat(exclude);
  [].forEach.call(container.children, function (node) {
    if (exclude.indexOf(node) === -1 && isHidable(node)) {
      cb(node);
    }
  });
};

function ariaHidden(show, node) {
  if (!node) return;

  if (show) {
    node.setAttribute('aria-hidden', 'true');
  } else {
    node.removeAttribute('aria-hidden');
  }
}
function hideSiblings(container, _ref2) {
  var dialog = _ref2.dialog,
      backdrop = _ref2.backdrop;
  siblings(container, [dialog, backdrop], function (node) {
    return ariaHidden(true, node);
  });
}
function showSiblings(container, _ref3) {
  var dialog = _ref3.dialog,
      backdrop = _ref3.backdrop;
  siblings(container, [dialog, backdrop], function (node) {
    return ariaHidden(false, node);
  });
}
// CONCATENATED MODULE: ./node_modules/react-overlays/esm/ModalManager.js







function findIndexOf(arr, cb) {
  var idx = -1;
  arr.some(function (d, i) {
    if (cb(d, i)) {
      idx = i;
      return true;
    }

    return false;
  });
  return idx;
}
/**
 * Proper state management for containers and the modals in those containers.
 *
 * @internal Used by the Modal to ensure proper styling of containers.
 */


var ModalManager_ModalManager =
/*#__PURE__*/
function () {
  function ModalManager(_temp) {
    var _ref = _temp === void 0 ? {} : _temp,
        _ref$hideSiblingNodes = _ref.hideSiblingNodes,
        hideSiblingNodes = _ref$hideSiblingNodes === void 0 ? true : _ref$hideSiblingNodes,
        _ref$handleContainerO = _ref.handleContainerOverflow,
        handleContainerOverflow = _ref$handleContainerO === void 0 ? true : _ref$handleContainerO;

    this.hideSiblingNodes = hideSiblingNodes;
    this.handleContainerOverflow = handleContainerOverflow;
    this.modals = [];
    this.containers = [];
    this.data = [];
    this.scrollbarSize = scrollbarSize();
  }

  var _proto = ModalManager.prototype;

  _proto.isContainerOverflowing = function isContainerOverflowing(modal) {
    var data = this.data[this.containerIndexFromModal(modal)];
    return data && data.overflowing;
  };

  _proto.containerIndexFromModal = function containerIndexFromModal(modal) {
    return findIndexOf(this.data, function (d) {
      return d.modals.indexOf(modal) !== -1;
    });
  };

  _proto.setContainerStyle = function setContainerStyle(containerState, container) {
    var style = {
      overflow: 'hidden'
    }; // we are only interested in the actual `style` here
    // because we will override it

    containerState.style = {
      overflow: container.style.overflow,
      paddingRight: container.style.paddingRight
    };

    if (containerState.overflowing) {
      // use computed style, here to get the real padding
      // to add our scrollbar width
      style.paddingRight = parseInt(esm_css(container, 'paddingRight') || 0, 10) + this.scrollbarSize + "px";
    }

    esm_css(container, style);
  };

  _proto.removeContainerStyle = function removeContainerStyle(containerState, container) {
    var style = containerState.style;
    Object.keys(style).forEach(function (key) {
      container.style[key] = style[key];
    });
  };

  _proto.add = function add(modal, container, className) {
    var modalIdx = this.modals.indexOf(modal);
    var containerIdx = this.containers.indexOf(container);

    if (modalIdx !== -1) {
      return modalIdx;
    }

    modalIdx = this.modals.length;
    this.modals.push(modal);

    if (this.hideSiblingNodes) {
      hideSiblings(container, modal);
    }

    if (containerIdx !== -1) {
      this.data[containerIdx].modals.push(modal);
      return modalIdx;
    }

    var data = {
      modals: [modal],
      // right now only the first modal of a container will have its classes applied
      classes: className ? className.split(/\s+/) : [],
      overflowing: isOverflowing(container)
    };

    if (this.handleContainerOverflow) {
      this.setContainerStyle(data, container);
    }

    data.classes.forEach(addClass.bind(null, container));
    this.containers.push(container);
    this.data.push(data);
    return modalIdx;
  };

  _proto.remove = function remove(modal) {
    var modalIdx = this.modals.indexOf(modal);

    if (modalIdx === -1) {
      return;
    }

    var containerIdx = this.containerIndexFromModal(modal);
    var data = this.data[containerIdx];
    var container = this.containers[containerIdx];
    data.modals.splice(data.modals.indexOf(modal), 1);
    this.modals.splice(modalIdx, 1); // if that was the last modal in a container,
    // clean up the container

    if (data.modals.length === 0) {
      data.classes.forEach(removeClass.bind(null, container));

      if (this.handleContainerOverflow) {
        this.removeContainerStyle(data, container);
      }

      if (this.hideSiblingNodes) {
        showSiblings(container, modal);
      }

      this.containers.splice(containerIdx, 1);
      this.data.splice(containerIdx, 1);
    } else if (this.hideSiblingNodes) {
      // otherwise make sure the next top modal is visible to a SR
      var _data$modals = data.modals[data.modals.length - 1],
          backdrop = _data$modals.backdrop,
          dialog = _data$modals.dialog;
      ariaHidden(false, dialog);
      ariaHidden(false, backdrop);
    }
  };

  _proto.isTopModal = function isTopModal(modal) {
    return !!this.modals.length && this.modals[this.modals.length - 1] === modal;
  };

  return ModalManager;
}();

/* harmony default export */ var esm_ModalManager = (ModalManager_ModalManager);
// CONCATENATED MODULE: ./node_modules/react-overlays/esm/utils/ownerDocument.js


/* harmony default export */ var utils_ownerDocument = (function (componentOrElement) {
  return ownerDocument(external_root_ReactDOM_commonjs2_react_dom_commonjs_react_dom_amd_react_dom_default.a.findDOMNode(componentOrElement));
});
// CONCATENATED MODULE: ./node_modules/react-overlays/esm/utils/useWaitForDOMRef.js



var useWaitForDOMRef_resolveRef = function resolveRef(ref) {
  if (typeof document === 'undefined') return undefined;
  if (ref == null) return ownerDocument().body;
  if (typeof ref === 'function') ref = ref();
  if (ref && ref.current) ref = ref.current;
  if (ref && ref.nodeType) return ref;
  return null;
};

function useWaitForDOMRef(ref, onResolved) {
  var _useState = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useState"])(function () {
    return useWaitForDOMRef_resolveRef(ref);
  }),
      resolvedRef = _useState[0],
      setRef = _useState[1];

  if (!resolvedRef) {
    var earlyRef = useWaitForDOMRef_resolveRef(ref);
    if (earlyRef) setRef(earlyRef);
  }

  Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useEffect"])(function () {
    if (onResolved && resolvedRef) {
      onResolved(resolvedRef);
    }
  }, [onResolved, resolvedRef]);
  Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useEffect"])(function () {
    var nextRef = useWaitForDOMRef_resolveRef(ref);

    if (nextRef !== resolvedRef) {
      setRef(nextRef);
    }
  }, [ref, resolvedRef]);
  return resolvedRef;
}
// CONCATENATED MODULE: ./node_modules/react-overlays/esm/Modal.js




/* eslint-disable react/prop-types */











var modalManager = new esm_ModalManager();

function omitProps(props, propTypes) {
  var keys = Object.keys(props);
  var newProps = {};
  keys.forEach(function (prop) {
    if (!Object.prototype.hasOwnProperty.call(propTypes, prop)) {
      newProps[prop] = props[prop];
    }
  });
  return newProps;
}
/**
 * Love them or hate them, `<Modal />` provides a solid foundation for creating dialogs, lightboxes, or whatever else.
 * The Modal component renders its `children` node in front of a backdrop component.
 *
 * The Modal offers a few helpful features over using just a `<Portal/>` component and some styles:
 *
 * - Manages dialog stacking when one-at-a-time just isn't enough.
 * - Creates a backdrop, for disabling interaction below the modal.
 * - It properly manages focus; moving to the modal content, and keeping it there until the modal is closed.
 * - It disables scrolling of the page content while open.
 * - Adds the appropriate ARIA roles are automatically.
 * - Easily pluggable animations via a `<Transition/>` component.
 *
 * Note that, in the same way the backdrop element prevents users from clicking or interacting
 * with the page content underneath the Modal, Screen readers also need to be signaled to not to
 * interact with page content while the Modal is open. To do this, we use a common technique of applying
 * the `aria-hidden='true'` attribute to the non-Modal elements in the Modal `container`. This means that for
 * a Modal to be truly modal, it should have a `container` that is _outside_ your app's
 * React hierarchy (such as the default: document.body).
 */


var Modal_Modal =
/*#__PURE__*/
function (_React$Component) {
  _inheritsLoose(Modal, _React$Component);

  function Modal() {
    var _this;

    for (var _len = arguments.length, _args = new Array(_len), _key = 0; _key < _len; _key++) {
      _args[_key] = arguments[_key];
    }

    _this = _React$Component.call.apply(_React$Component, [this].concat(_args)) || this;
    _this.state = {
      exited: !_this.props.show
    };

    _this.onShow = function () {
      var _this$props = _this.props,
          container = _this$props.container,
          containerClassName = _this$props.containerClassName,
          manager = _this$props.manager,
          onShow = _this$props.onShow;
      manager.add(_assertThisInitialized(_this), container, containerClassName);
      _this.removeKeydownListener = esm_listen(document, 'keydown', _this.handleDocumentKeyDown);
      _this.removeFocusListener = esm_listen(document, 'focus', // the timeout is necessary b/c this will run before the new modal is mounted
      // and so steals focus from it
      function () {
        return setTimeout(_this.enforceFocus);
      }, true);

      if (onShow) {
        onShow();
      } // autofocus after onShow, to not trigger a focus event for previous
      // modals before this one is shown.


      _this.autoFocus();
    };

    _this.onHide = function () {
      _this.props.manager.remove(_assertThisInitialized(_this));

      _this.removeKeydownListener();

      _this.removeFocusListener();

      if (_this.props.restoreFocus) {
        _this.restoreLastFocus();
      }
    };

    _this.setDialogRef = function (ref) {
      _this.dialog = ref;
    };

    _this.setBackdropRef = function (ref) {
      _this.backdrop = ref && external_root_ReactDOM_commonjs2_react_dom_commonjs_react_dom_amd_react_dom_default.a.findDOMNode(ref);
    };

    _this.handleHidden = function () {
      _this.setState({
        exited: true
      });

      _this.onHide();

      if (_this.props.onExited) {
        var _this$props2;

        (_this$props2 = _this.props).onExited.apply(_this$props2, arguments);
      }
    };

    _this.handleBackdropClick = function (e) {
      if (e.target !== e.currentTarget) {
        return;
      }

      if (_this.props.onBackdropClick) {
        _this.props.onBackdropClick(e);
      }

      if (_this.props.backdrop === true) {
        _this.props.onHide();
      }
    };

    _this.handleDocumentKeyDown = function (e) {
      if (_this.props.keyboard && e.keyCode === 27 && _this.isTopModal()) {
        if (_this.props.onEscapeKeyDown) {
          _this.props.onEscapeKeyDown(e);
        }

        _this.props.onHide();
      }
    };

    _this.enforceFocus = function () {
      if (!_this.props.enforceFocus || !_this._isMounted || !_this.isTopModal()) {
        return;
      }

      var currentActiveElement = activeElement(utils_ownerDocument(_assertThisInitialized(_this)));

      if (_this.dialog && !contains(_this.dialog, currentActiveElement)) {
        _this.dialog.focus();
      }
    };

    _this.renderBackdrop = function () {
      var _this$props3 = _this.props,
          renderBackdrop = _this$props3.renderBackdrop,
          Transition = _this$props3.backdropTransition;
      var backdrop = renderBackdrop({
        ref: _this.setBackdropRef,
        onClick: _this.handleBackdropClick
      });

      if (Transition) {
        backdrop = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Transition, {
          appear: true,
          "in": _this.props.show
        }, backdrop);
      }

      return backdrop;
    };

    return _this;
  }

  Modal.getDerivedStateFromProps = function getDerivedStateFromProps(nextProps) {
    if (nextProps.show) {
      return {
        exited: false
      };
    }

    if (!nextProps.transition) {
      // Otherwise let handleHidden take care of marking exited.
      return {
        exited: true
      };
    }

    return null;
  };

  var _proto = Modal.prototype;

  _proto.componentDidMount = function componentDidMount() {
    this._isMounted = true;

    if (this.props.show) {
      this.onShow();
    }
  };

  _proto.componentDidUpdate = function componentDidUpdate(prevProps) {
    var transition = this.props.transition;

    if (prevProps.show && !this.props.show && !transition) {
      // Otherwise handleHidden will call this.
      this.onHide();
    } else if (!prevProps.show && this.props.show) {
      this.onShow();
    }
  };

  _proto.componentWillUnmount = function componentWillUnmount() {
    var _this$props4 = this.props,
        show = _this$props4.show,
        transition = _this$props4.transition;
    this._isMounted = false;

    if (show || transition && !this.state.exited) {
      this.onHide();
    }
  };

  _proto.getSnapshotBeforeUpdate = function getSnapshotBeforeUpdate(prevProps) {
    if (canUseDOM && !prevProps.show && this.props.show) {
      this.lastFocus = activeElement();
    }

    return null;
  };

  _proto.restoreLastFocus = function restoreLastFocus() {
    // Support: <=IE11 doesn't support `focus()` on svg elements (RB: #917)
    if (this.lastFocus && this.lastFocus.focus) {
      this.lastFocus.focus(this.props.restoreFocusOptions);
      this.lastFocus = null;
    }
  };

  _proto.autoFocus = function autoFocus() {
    if (!this.props.autoFocus) return;
    var currentActiveElement = activeElement(utils_ownerDocument(this));

    if (this.dialog && !contains(this.dialog, currentActiveElement)) {
      this.lastFocus = currentActiveElement;
      this.dialog.focus();
    }
  };

  _proto.isTopModal = function isTopModal() {
    return this.props.manager.isTopModal(this);
  };

  _proto.render = function render() {
    var _this$props5 = this.props,
        show = _this$props5.show,
        container = _this$props5.container,
        children = _this$props5.children,
        renderDialog = _this$props5.renderDialog,
        _this$props5$role = _this$props5.role,
        role = _this$props5$role === void 0 ? 'dialog' : _this$props5$role,
        Transition = _this$props5.transition,
        backdrop = _this$props5.backdrop,
        className = _this$props5.className,
        style = _this$props5.style,
        onExit = _this$props5.onExit,
        onExiting = _this$props5.onExiting,
        onEnter = _this$props5.onEnter,
        onEntering = _this$props5.onEntering,
        onEntered = _this$props5.onEntered,
        props = _objectWithoutPropertiesLoose(_this$props5, ["show", "container", "children", "renderDialog", "role", "transition", "backdrop", "className", "style", "onExit", "onExiting", "onEnter", "onEntering", "onEntered"]);

    if (!(show || Transition && !this.state.exited)) {
      return null;
    }

    var dialogProps = _extends({
      role: role,
      ref: this.setDialogRef,
      // apparently only works on the dialog role element
      'aria-modal': role === 'dialog' ? true : undefined
    }, omitProps(props, Modal.propTypes), {
      style: style,
      className: className,
      tabIndex: '-1'
    });

    var dialog = renderDialog ? renderDialog(dialogProps) : external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", dialogProps, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.cloneElement(children, {
      role: 'document'
    }));

    if (Transition) {
      dialog = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Transition, {
        appear: true,
        unmountOnExit: true,
        "in": show,
        onExit: onExit,
        onExiting: onExiting,
        onExited: this.handleHidden,
        onEnter: onEnter,
        onEntering: onEntering,
        onEntered: onEntered
      }, dialog);
    }

    return external_root_ReactDOM_commonjs2_react_dom_commonjs_react_dom_amd_react_dom_default.a.createPortal(external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Fragment, null, backdrop && this.renderBackdrop(), dialog), container);
  };

  return Modal;
}(external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Component); // dumb HOC for the sake react-docgen


Modal_Modal.propTypes = {
  /**
   * Set the visibility of the Modal
   */
  show: prop_types_default.a.bool,

  /**
   * A DOM element, a `ref` to an element, or function that returns either. The Modal is appended to it's `container` element.
   *
   * For the sake of assistive technologies, the container should usually be the document body, so that the rest of the
   * page content can be placed behind a virtual backdrop as well as a visual one.
   */
  container: prop_types_default.a.any,

  /**
   * A callback fired when the Modal is opening.
   */
  onShow: prop_types_default.a.func,

  /**
   * A callback fired when either the backdrop is clicked, or the escape key is pressed.
   *
   * The `onHide` callback only signals intent from the Modal,
   * you must actually set the `show` prop to `false` for the Modal to close.
   */
  onHide: prop_types_default.a.func,

  /**
   * Include a backdrop component.
   */
  backdrop: prop_types_default.a.oneOfType([prop_types_default.a.bool, prop_types_default.a.oneOf(['static'])]),

  /**
   * A function that returns the dialog component. Useful for custom
   * rendering. **Note:** the component should make sure to apply the provided ref.
   *
   * ```js
   *  renderDialog={props => <MyDialog {...props} />}
   * ```
   */
  renderDialog: prop_types_default.a.func,

  /**
   * A function that returns a backdrop component. Useful for custom
   * backdrop rendering.
   *
   * ```js
   *  renderBackdrop={props => <MyBackdrop {...props} />}
   * ```
   */
  renderBackdrop: prop_types_default.a.func,

  /**
   * A callback fired when the escape key, if specified in `keyboard`, is pressed.
   */
  onEscapeKeyDown: prop_types_default.a.func,

  /**
   * A callback fired when the backdrop, if specified, is clicked.
   */
  onBackdropClick: prop_types_default.a.func,

  /**
   * A css class or set of classes applied to the modal container when the modal is open,
   * and removed when it is closed.
   */
  containerClassName: prop_types_default.a.string,

  /**
   * Close the modal when escape key is pressed
   */
  keyboard: prop_types_default.a.bool,

  /**
   * A `react-transition-group@2.0.0` `<Transition/>` component used
   * to control animations for the dialog component.
   */
  transition: prop_types_default.a.elementType,

  /**
   * A `react-transition-group@2.0.0` `<Transition/>` component used
   * to control animations for the backdrop components.
   */
  backdropTransition: prop_types_default.a.elementType,

  /**
   * When `true` The modal will automatically shift focus to itself when it opens, and
   * replace it to the last focused element when it closes. This also
   * works correctly with any Modal children that have the `autoFocus` prop.
   *
   * Generally this should never be set to `false` as it makes the Modal less
   * accessible to assistive technologies, like screen readers.
   */
  autoFocus: prop_types_default.a.bool,

  /**
   * When `true` The modal will prevent focus from leaving the Modal while open.
   *
   * Generally this should never be set to `false` as it makes the Modal less
   * accessible to assistive technologies, like screen readers.
   */
  enforceFocus: prop_types_default.a.bool,

  /**
   * When `true` The modal will restore focus to previously focused element once
   * modal is hidden
   */
  restoreFocus: prop_types_default.a.bool,

  /**
   * Options passed to focus function when `restoreFocus` is set to `true`
   *
   * @link  https://developer.mozilla.org/en-US/docs/Web/API/HTMLElement/focus#Parameters
   */
  restoreFocusOptions: prop_types_default.a.shape({
    preventScroll: prop_types_default.a.bool
  }),

  /**
   * Callback fired before the Modal transitions in
   */
  onEnter: prop_types_default.a.func,

  /**
   * Callback fired as the Modal begins to transition in
   */
  onEntering: prop_types_default.a.func,

  /**
   * Callback fired after the Modal finishes transitioning in
   */
  onEntered: prop_types_default.a.func,

  /**
   * Callback fired right before the Modal transitions out
   */
  onExit: prop_types_default.a.func,

  /**
   * Callback fired as the Modal begins to transition out
   */
  onExiting: prop_types_default.a.func,

  /**
   * Callback fired after the Modal finishes transitioning out
   */
  onExited: prop_types_default.a.func,

  /**
   * A ModalManager instance used to track and manage the state of open
   * Modals. Useful when customizing how modals interact within a container
   */
  manager: prop_types_default.a.object.isRequired
};
Modal_Modal.defaultProps = {
  show: false,
  role: 'dialog',
  backdrop: true,
  keyboard: true,
  autoFocus: true,
  enforceFocus: true,
  restoreFocus: true,
  onHide: function onHide() {},
  manager: modalManager,
  renderBackdrop: function renderBackdrop(props) {
    return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", props);
  }
};

function Modal_forwardRef(Component) {
  // eslint-disable-next-line react/display-name
  var ModalWithContainer = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (props, ref) {
    var resolved = useWaitForDOMRef(props.container);
    return resolved ? external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, props, {
      ref: ref,
      container: resolved
    })) : null;
  });
  ModalWithContainer.Manager = esm_ModalManager;
  ModalWithContainer._Inner = Component;
  return ModalWithContainer;
}

var Modal_ModalWithContainer = Modal_forwardRef(Modal_Modal);
Modal_ModalWithContainer.Manager = esm_ModalManager;
/* harmony default export */ var esm_Modal = (Modal_ModalWithContainer);
// CONCATENATED MODULE: ./src/BootstrapModalManager.js





var Selector = {
  FIXED_CONTENT: '.fixed-top, .fixed-bottom, .is-fixed, .sticky-top',
  STICKY_CONTENT: '.sticky-top',
  NAVBAR_TOGGLER: '.navbar-toggler'
};

var BootstrapModalManager_BootstrapModalManager =
/*#__PURE__*/
function (_ModalManager) {
  _inheritsLoose(BootstrapModalManager, _ModalManager);

  function BootstrapModalManager() {
    var _this;

    for (var _len = arguments.length, args = new Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }

    _this = _ModalManager.call.apply(_ModalManager, [this].concat(args)) || this;

    _this.adjustAndStore = function (prop, element, adjust) {
      var _css;

      var actual = element.style[prop];
      element.dataset[prop] = actual;
      esm_css(element, (_css = {}, _css[prop] = parseFloat(esm_css(element, prop)) + adjust + "px", _css));
    };

    _this.restore = function (prop, element) {
      var value = element.dataset[prop];

      if (value !== undefined) {
        var _css2;

        delete element.dataset[prop];
        esm_css(element, (_css2 = {}, _css2[prop] = value, _css2));
      }
    };

    return _this;
  }

  var _proto = BootstrapModalManager.prototype;

  _proto.setContainerStyle = function setContainerStyle(containerState, container) {
    var _this2 = this;

    _ModalManager.prototype.setContainerStyle.call(this, containerState, container);

    if (!containerState.overflowing) return;
    var size = scrollbarSize();
    qsa(container, Selector.FIXED_CONTENT).forEach(function (el) {
      return _this2.adjustAndStore('paddingRight', el, size);
    });
    qsa(container, Selector.STICKY_CONTENT).forEach(function (el) {
      return _this2.adjustAndStore('margingRight', el, -size);
    });
    qsa(container, Selector.NAVBAR_TOGGLER).forEach(function (el) {
      return _this2.adjustAndStore('margingRight', el, size);
    });
  };

  _proto.removeContainerStyle = function removeContainerStyle(containerState, container) {
    var _this3 = this;

    _ModalManager.prototype.removeContainerStyle.call(this, containerState, container);

    qsa(container, Selector.FIXED_CONTENT).forEach(function (el) {
      return _this3.restore('paddingRight', el);
    });
    qsa(container, Selector.STICKY_CONTENT).forEach(function (el) {
      return _this3.restore('margingRight', el);
    });
    qsa(container, Selector.NAVBAR_TOGGLER).forEach(function (el) {
      return _this3.restore('margingRight', el);
    });
  };

  return BootstrapModalManager;
}(esm_ModalManager);


// CONCATENATED MODULE: ./src/ModalBody.js

/* harmony default export */ var ModalBody = (createWithBsPrefix('modal-body'));
// CONCATENATED MODULE: ./src/ModalContext.js

var ModalContext = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createContext({
  onHide: function onHide() {}
});
/* harmony default export */ var src_ModalContext = (ModalContext);
// CONCATENATED MODULE: ./src/ModalDialog.js


var ModalDialog_jsxFileName = "/Users/jquense/src/react-bootstrap/src/ModalDialog.js";




var ModalDialog_propTypes = {
  /** @default 'modal' */
  bsPrefix: prop_types_default.a.string,

  /**
   * Render a large, extra large or small modal.
   *
   * @type ('sm'|'lg','xl')
   */
  size: prop_types_default.a.string,

  /**
   * Specify whether the Component should be vertically centered
   */
  centered: prop_types_default.a.bool,

  /**
   * Allows scrolling the `<Modal.Body>` instead of the entire Modal when overflowing.
   */
  scrollable: prop_types_default.a.bool
};
var ModalDialog = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      className = _ref.className,
      centered = _ref.centered,
      size = _ref.size,
      children = _ref.children,
      scrollable = _ref.scrollable,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "className", "centered", "size", "children", "scrollable"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'modal');
  var dialogClass = bsPrefix + "-dialog";
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", _extends({}, props, {
    ref: ref,
    className: classnames_default()(dialogClass, className, size && bsPrefix + "-" + size, centered && dialogClass + "-centered", scrollable && dialogClass + "-scrollable"),
    __source: {
      fileName: ModalDialog_jsxFileName,
      lineNumber: 38
    },
    __self: this
  }), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", {
    className: bsPrefix + "-content",
    __source: {
      fileName: ModalDialog_jsxFileName,
      lineNumber: 49
    },
    __self: this
  }, children));
});
ModalDialog.displayName = 'ModalDialog';
ModalDialog.propTypes = ModalDialog_propTypes;
/* harmony default export */ var src_ModalDialog = (ModalDialog);
// CONCATENATED MODULE: ./src/ModalFooter.js

/* harmony default export */ var ModalFooter = (createWithBsPrefix('modal-footer'));
// CONCATENATED MODULE: ./src/ModalHeader.js


var ModalHeader_jsxFileName = "/Users/jquense/src/react-bootstrap/src/ModalHeader.js";







var ModalHeader_propTypes = {
  bsPrefix: prop_types_default.a.string,

  /**
   * Provides an accessible label for the close
   * button. It is used for Assistive Technology when the label text is not
   * readable.
   */
  closeLabel: prop_types_default.a.string,

  /**
   * Specify whether the Component should contain a close button
   */
  closeButton: prop_types_default.a.bool,

  /**
   * A Callback fired when the close button is clicked. If used directly inside
   * a Modal component, the onHide will automatically be propagated up to the
   * parent Modal `onHide`.
   */
  onHide: prop_types_default.a.func
};
var ModalHeader_defaultProps = {
  closeLabel: 'Close',
  closeButton: false
};
var ModalHeader = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      closeLabel = _ref.closeLabel,
      closeButton = _ref.closeButton,
      onHide = _ref.onHide,
      className = _ref.className,
      children = _ref.children,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "closeLabel", "closeButton", "onHide", "className", "children"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'modal-header');
  var context = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(src_ModalContext);
  var handleClick = useEventCallback(function () {
    if (context) context.onHide();
    if (onHide) onHide();
  });
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", _extends({
    ref: ref
  }, props, {
    className: classnames_default()(className, bsPrefix),
    __source: {
      fileName: ModalHeader_jsxFileName,
      lineNumber: 61
    },
    __self: this
  }), children, closeButton && external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_CloseButton, {
    label: closeLabel,
    onClick: handleClick,
    __source: {
      fileName: ModalHeader_jsxFileName,
      lineNumber: 65
    },
    __self: this
  }));
});
ModalHeader.displayName = 'ModalHeader';
ModalHeader.propTypes = ModalHeader_propTypes;
ModalHeader.defaultProps = ModalHeader_defaultProps;
/* harmony default export */ var src_ModalHeader = (ModalHeader);
// CONCATENATED MODULE: ./src/ModalTitle.js


var ModalTitle_DivStyledAsH4 = divWithClassName('h4');
/* harmony default export */ var ModalTitle = (createWithBsPrefix('modal-title', {
  Component: ModalTitle_DivStyledAsH4
}));
// CONCATENATED MODULE: ./src/Modal.js



var Modal_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Modal.js";


















var Modal_propTypes = {
  /**
   * @default 'modal'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * Render a large, extra large or small modal.
   *
   * @type ('sm'|'lg','xl')
   */
  size: prop_types_default.a.string,

  /**
   * vertically center the Dialog in the window
   */
  centered: prop_types_default.a.bool,

  /**
   * Include a backdrop component. Specify 'static' for a backdrop that doesn't
   * trigger an "onHide" when clicked.
   */
  backdrop: prop_types_default.a.oneOf(['static', true, false]),

  /**
   * Add an optional extra class name to .modal-backdrop
   * It could end up looking like class="modal-backdrop foo-modal-backdrop in".
   */
  backdropClassName: prop_types_default.a.string,

  /**
   * Close the modal when escape key is pressed
   */
  keyboard: prop_types_default.a.bool,

  /**
   * Allows scrolling the `<Modal.Body>` instead of the entire Modal when overflowing.
   */
  scrollable: prop_types_default.a.bool,

  /**
   * Open and close the Modal with a slide and fade animation.
   */
  animation: prop_types_default.a.bool,

  /**
   * A css class to apply to the Modal dialog DOM node.
   */
  dialogClassName: prop_types_default.a.string,

  /**
   * A Component type that provides the modal content Markup. This is a useful
   * prop when you want to use your own styles and markup to create a custom
   * modal component.
   */
  dialogAs: prop_types_default.a.elementType,

  /**
   * When `true` The modal will automatically shift focus to itself when it
   * opens, and replace it to the last focused element when it closes.
   * Generally this should never be set to false as it makes the Modal less
   * accessible to assistive technologies, like screen-readers.
   */
  autoFocus: prop_types_default.a.bool,

  /**
   * When `true` The modal will prevent focus from leaving the Modal while
   * open. Consider leaving the default value here, as it is necessary to make
   * the Modal work well with assistive technologies, such as screen readers.
   */
  enforceFocus: prop_types_default.a.bool,

  /**
   * When `true` The modal will restore focus to previously focused element once
   * modal is hidden
   */
  restoreFocus: prop_types_default.a.bool,

  /**
   * When `true` The modal will show itself.
   */
  show: prop_types_default.a.bool,

  /**
   * A callback fired when the Modal is opening.
   */
  onShow: prop_types_default.a.func,

  /**
   * A callback fired when the header closeButton or non-static backdrop is
   * clicked. Required if either are specified.
   */
  onHide: prop_types_default.a.func,

  /**
   * A callback fired when the escape key, if specified in `keyboard`, is pressed.
   */
  onEscapeKeyDown: prop_types_default.a.func,

  /**
   * Callback fired before the Modal transitions in
   */
  onEnter: prop_types_default.a.func,

  /**
   * Callback fired as the Modal begins to transition in
   */
  onEntering: prop_types_default.a.func,

  /**
   * Callback fired after the Modal finishes transitioning in
   */
  onEntered: prop_types_default.a.func,

  /**
   * Callback fired right before the Modal transitions out
   */
  onExit: prop_types_default.a.func,

  /**
   * Callback fired as the Modal begins to transition out
   */
  onExiting: prop_types_default.a.func,

  /**
   * Callback fired after the Modal finishes transitioning out
   */
  onExited: prop_types_default.a.func,

  /**
   * A ModalManager instance used to track and manage the state of open
   * Modals. Useful when customizing how modals interact within a container
   */
  manager: prop_types_default.a.object.isRequired,

  /**
   * @private
   */
  container: prop_types_default.a.any
};
var Modal_defaultProps = {
  show: false,
  backdrop: true,
  keyboard: true,
  autoFocus: true,
  enforceFocus: true,
  restoreFocus: true,
  animation: true,
  dialogAs: src_ModalDialog,
  manager: new BootstrapModalManager_BootstrapModalManager()
};
/* eslint-disable no-use-before-define, react/no-multi-comp */

function DialogTransition(props) {
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_Fade, _extends({}, props, {
    __source: {
      fileName: Modal_jsxFileName,
      lineNumber: 175
    },
    __self: this
  }));
}

function BackdropTransition(props) {
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_Fade, _extends({}, props, {
    __source: {
      fileName: Modal_jsxFileName,
      lineNumber: 179
    },
    __self: this
  }));
}
/* eslint-enable no-use-before-define */


var src_Modal_Modal =
/*#__PURE__*/
function (_React$Component) {
  _inheritsLoose(Modal, _React$Component);

  function Modal() {
    var _this;

    for (var _len = arguments.length, _args = new Array(_len), _key = 0; _key < _len; _key++) {
      _args[_key] = arguments[_key];
    }

    _this = _React$Component.call.apply(_React$Component, [this].concat(_args)) || this;
    _this.state = {
      style: {}
    };
    _this.modalContext = {
      onHide: function onHide() {
        return _this.props.onHide();
      }
    };

    _this.setModalRef = function (ref) {
      _this._modal = ref;
    };

    _this.handleDialogMouseDown = function () {
      _this._waitingForMouseUp = true;
    };

    _this.handleMouseUp = function (e) {
      if (_this._waitingForMouseUp && e.target === _this._modal.dialog) {
        _this._ignoreBackdropClick = true;
      }

      _this._waitingForMouseUp = false;
    };

    _this.handleClick = function (e) {
      if (_this._ignoreBackdropClick || e.target !== e.currentTarget) {
        _this._ignoreBackdropClick = false;
        return;
      }

      _this.props.onHide();
    };

    _this.handleEnter = function (node) {
      var _this$props;

      if (node) {
        node.style.display = 'block';

        _this.updateDialogStyle(node);
      }

      for (var _len2 = arguments.length, args = new Array(_len2 > 1 ? _len2 - 1 : 0), _key2 = 1; _key2 < _len2; _key2++) {
        args[_key2 - 1] = arguments[_key2];
      }

      if (_this.props.onEnter) (_this$props = _this.props).onEnter.apply(_this$props, [node].concat(args));
    };

    _this.handleEntering = function (node) {
      var _this$props2;

      for (var _len3 = arguments.length, args = new Array(_len3 > 1 ? _len3 - 1 : 0), _key3 = 1; _key3 < _len3; _key3++) {
        args[_key3 - 1] = arguments[_key3];
      }

      if (_this.props.onEntering) (_this$props2 = _this.props).onEntering.apply(_this$props2, [node].concat(args)); // FIXME: This should work even when animation is disabled.

      esm_addEventListener(window, 'resize', _this.handleWindowResize);
    };

    _this.handleExited = function (node) {
      var _this$props3;

      if (node) node.style.display = ''; // RHL removes it sometimes

      for (var _len4 = arguments.length, args = new Array(_len4 > 1 ? _len4 - 1 : 0), _key4 = 1; _key4 < _len4; _key4++) {
        args[_key4 - 1] = arguments[_key4];
      }

      if (_this.props.onExited) (_this$props3 = _this.props).onExited.apply(_this$props3, args); // FIXME: This should work even when animation is disabled.

      esm_removeEventListener(window, 'resize', _this.handleWindowResize);
    };

    _this.handleWindowResize = function () {
      _this.updateDialogStyle(_this._modal.dialog);
    };

    _this.renderBackdrop = function (props) {
      var _this$props4 = _this.props,
          bsPrefix = _this$props4.bsPrefix,
          backdropClassName = _this$props4.backdropClassName,
          animation = _this$props4.animation;
      return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", _extends({}, props, {
        className: classnames_default()(bsPrefix + "-backdrop", backdropClassName, !animation && 'show'),
        __source: {
          fileName: Modal_jsxFileName,
          lineNumber: 278
        },
        __self: this
      }));
    };

    return _this;
  }

  var _proto = Modal.prototype;

  _proto.componentWillUnmount = function componentWillUnmount() {
    // Clean up the listener if we need to.
    esm_removeEventListener(window, 'resize', this.handleWindowResize);
  };

  _proto.updateDialogStyle = function updateDialogStyle(node) {
    if (!canUseDOM) return;
    var manager = this.props.manager;
    var containerIsOverflowing = manager.isContainerOverflowing(this._modal);
    var modalIsOverflowing = node.scrollHeight > ownerDocument(node).documentElement.clientHeight;
    this.setState({
      style: {
        paddingRight: containerIsOverflowing && !modalIsOverflowing ? scrollbarSize() : undefined,
        paddingLeft: !containerIsOverflowing && modalIsOverflowing ? scrollbarSize() : undefined
      }
    });
  };

  _proto.render = function render() {
    var _this$props5 = this.props,
        bsPrefix = _this$props5.bsPrefix,
        className = _this$props5.className,
        style = _this$props5.style,
        dialogClassName = _this$props5.dialogClassName,
        children = _this$props5.children,
        Dialog = _this$props5.dialogAs,
        show = _this$props5.show,
        animation = _this$props5.animation,
        backdrop = _this$props5.backdrop,
        keyboard = _this$props5.keyboard,
        manager = _this$props5.manager,
        onEscapeKeyDown = _this$props5.onEscapeKeyDown,
        onShow = _this$props5.onShow,
        onHide = _this$props5.onHide,
        container = _this$props5.container,
        autoFocus = _this$props5.autoFocus,
        enforceFocus = _this$props5.enforceFocus,
        restoreFocus = _this$props5.restoreFocus,
        onEntered = _this$props5.onEntered,
        onExit = _this$props5.onExit,
        onExiting = _this$props5.onExiting,
        _ = _this$props5.onExited,
        _1 = _this$props5.onEntering,
        _6 = _this$props5.onEnter,
        _4 = _this$props5.onEntering,
        _2 = _this$props5.backdropClassName,
        props = _objectWithoutPropertiesLoose(_this$props5, ["bsPrefix", "className", "style", "dialogClassName", "children", "dialogAs", "show", "animation", "backdrop", "keyboard", "manager", "onEscapeKeyDown", "onShow", "onHide", "container", "autoFocus", "enforceFocus", "restoreFocus", "onEntered", "onExit", "onExiting", "onExited", "onEntering", "onEnter", "onEntering", "backdropClassName"]);

    var clickHandler = backdrop === true ? this.handleClick : null;

    var baseModalStyle = _extends({}, style, {}, this.state.style); // Sets `display` always block when `animation` is false


    if (!animation) baseModalStyle.display = 'block';
    return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_ModalContext.Provider, {
      value: this.modalContext,
      __source: {
        fileName: Modal_jsxFileName,
        lineNumber: 332
      },
      __self: this
    }, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(esm_Modal, _extends({
      show: show,
      backdrop: backdrop,
      container: container,
      keyboard: keyboard,
      autoFocus: autoFocus,
      enforceFocus: enforceFocus,
      restoreFocus: restoreFocus,
      onEscapeKeyDown: onEscapeKeyDown,
      onShow: onShow,
      onHide: onHide,
      onEntered: onEntered,
      onExit: onExit,
      onExiting: onExiting,
      manager: manager,
      ref: this.setModalRef,
      style: baseModalStyle,
      className: classnames_default()(className, bsPrefix),
      containerClassName: bsPrefix + "-open",
      transition: animation ? DialogTransition : undefined,
      backdropTransition: animation ? BackdropTransition : undefined,
      renderBackdrop: this.renderBackdrop,
      onClick: clickHandler,
      onMouseUp: this.handleMouseUp,
      onEnter: this.handleEnter,
      onEntering: this.handleEntering,
      onExited: this.handleExited
    }, {
      __source: {
        fileName: Modal_jsxFileName,
        lineNumber: 333
      },
      __self: this
    }), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Dialog, _extends({}, props, {
      onMouseDown: this.handleDialogMouseDown,
      className: dialogClassName,
      __source: {
        fileName: Modal_jsxFileName,
        lineNumber: 363
      },
      __self: this
    }), children)));
  };

  return Modal;
}(external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Component);

src_Modal_Modal.propTypes = Modal_propTypes;
src_Modal_Modal.defaultProps = Modal_defaultProps;
var DecoratedModal = createBootstrapComponent(src_Modal_Modal, 'modal');
DecoratedModal.Body = ModalBody;
DecoratedModal.Header = src_ModalHeader;
DecoratedModal.Title = ModalTitle;
DecoratedModal.Footer = ModalFooter;
DecoratedModal.Dialog = src_ModalDialog;
DecoratedModal.TRANSITION_DURATION = 300;
DecoratedModal.BACKDROP_TRANSITION_DURATION = 150;
/* harmony default export */ var src_Modal = (DecoratedModal);
// CONCATENATED MODULE: ./src/NavItem.js


var NavItem_jsxFileName = "/Users/jquense/src/react-bootstrap/src/NavItem.js";




var NavItem_propTypes = {
  /**
   * @default 'nav-item'
   */
  bsPrefix: prop_types_default.a.string,

  /** The ARIA role of the component */
  role: prop_types_default.a.string,
  as: prop_types_default.a.elementType
};
var NavItem = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef( // Need to define the default "as" during prop destructuring to be compatible with styled-components github.com/react-bootstrap/react-bootstrap/issues/3595
function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      className = _ref.className,
      children = _ref.children,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'div' : _ref$as,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "className", "children", "as"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'nav-item');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, props, {
    ref: ref,
    className: classnames_default()(className, bsPrefix),
    __source: {
      fileName: NavItem_jsxFileName,
      lineNumber: 25
    },
    __self: this
  }), children);
});
NavItem.displayName = 'NavItem';
NavItem.propTypes = NavItem_propTypes;
/* harmony default export */ var src_NavItem = (NavItem);
// CONCATENATED MODULE: ./src/NavLink.js


var NavLink_jsxFileName = "/Users/jquense/src/react-bootstrap/src/NavLink.js";






var NavLink_propTypes = {
  /**
   * @default 'nav-link'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * The active state of the NavItem item.
   */
  active: prop_types_default.a.bool,

  /**
   * The disabled state of the NavItem item.
   */
  disabled: prop_types_default.a.bool,

  /**
   * The ARIA role for the `NavLink`, In the context of a 'tablist' parent Nav,
   * the role defaults to 'tab'
   * */
  role: prop_types_default.a.string,

  /** The HTML href attribute for the `NavLink` */
  href: prop_types_default.a.string,

  /** A callback fired when the `NavLink` is selected.
   *
   * ```js
   * function (eventKey: any, event: SyntheticEvent) {}
   * ```
   */
  onSelect: prop_types_default.a.func,

  /**
   * Uniquely idenifies the `NavItem` amongst its siblings,
   * used to determine and control the active state ofthe parent `Nav`
   */
  eventKey: prop_types_default.a.any,

  /** @default 'a' */
  as: prop_types_default.a.elementType
};
var NavLink_defaultProps = {
  disabled: false,
  as: src_SafeAnchor
};
var NavLink = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      disabled = _ref.disabled,
      className = _ref.className,
      href = _ref.href,
      eventKey = _ref.eventKey,
      onSelect = _ref.onSelect,
      as = _ref.as,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "disabled", "className", "href", "eventKey", "onSelect", "as"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'nav-link');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_AbstractNavItem, _extends({}, props, {
    href: href,
    ref: ref,
    eventKey: eventKey,
    as: as,
    disabled: disabled,
    onSelect: onSelect,
    className: classnames_default()(className, bsPrefix, disabled && 'disabled'),
    __source: {
      fileName: NavLink_jsxFileName,
      lineNumber: 65
    },
    __self: this
  }));
});
NavLink.displayName = 'NavLink';
NavLink.propTypes = NavLink_propTypes;
NavLink.defaultProps = NavLink_defaultProps;
/* harmony default export */ var src_NavLink = (NavLink);
// CONCATENATED MODULE: ./src/Nav.js


var Nav_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Nav.js";











var Nav_propTypes = {
  /**
   * @default 'nav'
   */
  bsPrefix: prop_types_default.a.string,

  /** @private */
  navbarBsPrefix: prop_types_default.a.string,

  /** @private */
  cardHeaderBsPrefix: prop_types_default.a.string,

  /**
   * The visual variant of the nav items.
   *
   * @type {('tabs'|'pills')}
   */
  variant: prop_types_default.a.string,

  /**
   * Marks the NavItem with a matching `eventKey` (or `href` if present) as active.
   *
   * @type {string}
   */
  activeKey: prop_types_default.a.any,

  /**
   * Have all `NavItem`s proportionately fill all available width.
   */
  fill: prop_types_default.a.bool,

  /**
   * Have all `NavItem`s evenly fill all available width.
   *
   * @type {boolean}
   */
  justify: all_default()(prop_types_default.a.bool, function (_ref) {
    var justify = _ref.justify,
        navbar = _ref.navbar;
    return justify && navbar ? Error('justify navbar `Nav`s are not supported') : null;
  }),

  /**
   * A callback fired when a NavItem is selected.
   *
   * ```js
   * function (
   *  Any eventKey,
   *  SyntheticEvent event?
   * )
   * ```
   */
  onSelect: prop_types_default.a.func,

  /**
   * ARIA role for the Nav, in the context of a TabContainer, the default will
   * be set to "tablist", but can be overridden by the Nav when set explicitly.
   *
   * When the role is "tablist", NavLink focus is managed according to
   * the ARIA authoring practices for tabs:
   * https://www.w3.org/TR/2013/WD-wai-aria-practices-20130307/#tabpanel
   */
  role: prop_types_default.a.string,

  /**
   * Apply styling an alignment for use in a Navbar. This prop will be set
   * automatically when the Nav is used inside a Navbar.
   */
  navbar: prop_types_default.a.bool,
  as: prop_types_default.a.elementType,

  /** @private */
  onKeyDown: prop_types_default.a.func
};
var Nav_defaultProps = {
  justify: false,
  fill: false
};
var Nav = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (uncontrolledProps, ref) {
  var _classNames;

  var _useUncontrolled = useUncontrolled(uncontrolledProps, {
    activeKey: 'onSelect'
  }),
      _useUncontrolled$as = _useUncontrolled.as,
      as = _useUncontrolled$as === void 0 ? 'div' : _useUncontrolled$as,
      bsPrefix = _useUncontrolled.bsPrefix,
      variant = _useUncontrolled.variant,
      fill = _useUncontrolled.fill,
      justify = _useUncontrolled.justify,
      navbar = _useUncontrolled.navbar,
      className = _useUncontrolled.className,
      children = _useUncontrolled.children,
      activeKey = _useUncontrolled.activeKey,
      props = _objectWithoutPropertiesLoose(_useUncontrolled, ["as", "bsPrefix", "variant", "fill", "justify", "navbar", "className", "children", "activeKey"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'nav');
  var navbarBsPrefix, cardHeaderBsPrefix;
  var navbarContext = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(NavbarContext);
  var cardContext = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(CardContext);

  if (navbarContext) {
    navbarBsPrefix = navbarContext.bsPrefix;
    navbar = navbar == null ? true : navbar;
  } else if (cardContext) {
    cardHeaderBsPrefix = cardContext.cardHeaderBsPrefix;
  }

  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_AbstractNav, _extends({
    as: as,
    ref: ref,
    activeKey: activeKey,
    className: classnames_default()(className, (_classNames = {}, _classNames[bsPrefix] = !navbar, _classNames[navbarBsPrefix + "-nav"] = navbar, _classNames[cardHeaderBsPrefix + "-" + variant] = !!cardHeaderBsPrefix, _classNames[bsPrefix + "-" + variant] = !!variant, _classNames[bsPrefix + "-fill"] = fill, _classNames[bsPrefix + "-justified"] = justify, _classNames))
  }, props, {
    __source: {
      fileName: Nav_jsxFileName,
      lineNumber: 122
    },
    __self: this
  }), children);
});
Nav.displayName = 'Nav';
Nav.propTypes = Nav_propTypes;
Nav.defaultProps = Nav_defaultProps;
Nav.Item = src_NavItem;
Nav.Link = src_NavLink;
/* harmony default export */ var src_Nav = (Nav);
// CONCATENATED MODULE: ./src/NavbarBrand.js


var NavbarBrand_jsxFileName = "/Users/jquense/src/react-bootstrap/src/NavbarBrand.js";




var NavbarBrand_propTypes = {
  /** @default 'navbar' */
  bsPrefix: prop_types_default.a.string,

  /**
   * An href, when provided the Brand will render as an `<a>` element (unless `as` is provided).
   */
  href: prop_types_default.a.string,

  /**
   * Set a custom element for this component.
   */
  as: prop_types_default.a.elementType
};
var NavbarBrand = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      className = _ref.className,
      as = _ref.as,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "className", "as"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'navbar-brand');
  var Component = as || (props.href ? 'a' : 'span');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, props, {
    ref: ref,
    className: classnames_default()(className, bsPrefix),
    __source: {
      fileName: NavbarBrand_jsxFileName,
      lineNumber: 29
    },
    __self: this
  }));
});
NavbarBrand.displayName = 'NavbarBrand';
NavbarBrand.propTypes = NavbarBrand_propTypes;
/* harmony default export */ var src_NavbarBrand = (NavbarBrand);
// CONCATENATED MODULE: ./src/NavbarCollapse.js


var NavbarCollapse_jsxFileName = "/Users/jquense/src/react-bootstrap/src/NavbarCollapse.js";





var NavbarCollapse_propTypes = {
  /** @default 'navbar-collapse' */
  bsPrefix: prop_types_default.a.string
};
var NavbarCollapse = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var children = _ref.children,
      bsPrefix = _ref.bsPrefix,
      props = _objectWithoutPropertiesLoose(_ref, ["children", "bsPrefix"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'navbar-collapse');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(NavbarContext.Consumer, {
    __source: {
      fileName: NavbarCollapse_jsxFileName,
      lineNumber: 17
    },
    __self: this
  }, function (context) {
    return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_Collapse, _extends({
      in: !!(context && context.expanded)
    }, props, {
      __source: {
        fileName: NavbarCollapse_jsxFileName,
        lineNumber: 19
      },
      __self: this
    }), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", {
      ref: ref,
      className: bsPrefix,
      __source: {
        fileName: NavbarCollapse_jsxFileName,
        lineNumber: 20
      },
      __self: this
    }, children));
  });
});
NavbarCollapse.displayName = 'NavbarCollapse';
NavbarCollapse.propTypes = NavbarCollapse_propTypes;
/* harmony default export */ var src_NavbarCollapse = (NavbarCollapse);
// CONCATENATED MODULE: ./src/NavbarToggle.js


var NavbarToggle_jsxFileName = "/Users/jquense/src/react-bootstrap/src/NavbarToggle.js";






var NavbarToggle_propTypes = {
  /** @default 'navbar-toggler' */
  bsPrefix: prop_types_default.a.string,

  /** An accessible ARIA label for the toggler button. */
  label: prop_types_default.a.string,

  /** @private */
  onClick: prop_types_default.a.func,

  /**
   * The toggle content. When empty, the default toggle will be rendered.
   */
  children: prop_types_default.a.node,
  as: prop_types_default.a.elementType
};
var NavbarToggle_defaultProps = {
  label: 'Toggle navigation'
};
var NavbarToggle = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      className = _ref.className,
      children = _ref.children,
      label = _ref.label,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'button' : _ref$as,
      onClick = _ref.onClick,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "className", "children", "label", "as", "onClick"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'navbar-toggler');

  var _ref2 = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(NavbarContext) || {},
      onToggle = _ref2.onToggle,
      expanded = _ref2.expanded;

  var handleClick = useEventCallback(function (e) {
    if (onClick) onClick(e);
    if (onToggle) onToggle();
  });

  if (Component === 'button') {
    props.type = 'button';
  }

  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, props, {
    ref: ref,
    onClick: handleClick,
    "aria-label": label,
    className: classnames_default()(className, bsPrefix, !expanded && 'collapsed'),
    __source: {
      fileName: NavbarToggle_jsxFileName,
      lineNumber: 59
    },
    __self: this
  }), children || external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("span", {
    className: bsPrefix + "-icon",
    __source: {
      fileName: NavbarToggle_jsxFileName,
      lineNumber: 66
    },
    __self: this
  }));
});
NavbarToggle.displayName = 'NavbarToggle';
NavbarToggle.propTypes = NavbarToggle_propTypes;
NavbarToggle.defaultProps = NavbarToggle_defaultProps;
/* harmony default export */ var src_NavbarToggle = (NavbarToggle);
// CONCATENATED MODULE: ./src/Navbar.js


var Navbar_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Navbar.js";











var Navbar_propTypes = {
  /** @default 'navbar' */
  bsPrefix: prop_types_default.a.string,

  /**
   * The general visual variant a the Navbar.
   * Use in combination with the `bg` prop, `background-color` utilities,
   * or your own background styles.
   *
   * @type {('light'|'dark')}
   */
  variant: prop_types_default.a.string,

  /**
   * The breakpoint, below which, the Navbar will collapse.
   * When `true` the Navbar will always be expanded regardless of screen size.
   */
  expand: prop_types_default.a.oneOf([true, 'sm', 'md', 'lg', 'xl']).isRequired,

  /**
   * A convenience prop for adding `bg-*` utility classes since they are so commonly used here.
   * `light` and `dark` are common choices but any `bg-*` class is supported, including any custom ones you might define.
   *
   * Pairs nicely with the `variant` prop.
   */
  bg: prop_types_default.a.string,

  /**
   * Create a fixed navbar along the top or bottom of the screen, that scrolls with the
   * page. A convenience prop for the `fixed-*` positioning classes.
   */
  fixed: prop_types_default.a.oneOf(['top', 'bottom']),

  /**
   * Position the navbar at the top or bottom of the viewport,
   * but only after scrolling past it. . A convenience prop for the `sticky-*` positioning classes.
   *
   *  __Not supported in <= IE11 and other older browsers without a polyfill__
   */
  sticky: prop_types_default.a.oneOf(['top', 'bottom']),

  /**
   * Set a custom element for this component.
   */
  as: prop_types_default.a.elementType,

  /**
   * A callback fired when the `<Navbar>` body collapses or expands. Fired when
   * a `<Navbar.Toggle>` is clicked and called with the new `expanded`
   * boolean value.
   *
   * @controllable expanded
   */
  onToggle: prop_types_default.a.func,

  /**
   * A callback fired when a descendant of a child `<Nav>` is selected. Should
   * be used to execute complex closing or other miscellaneous actions desired
   * after selecting a descendant of `<Nav>`. Does nothing if no `<Nav>` or `<Nav>`
   * descendants exist. The callback is called with an eventKey, which is a
   * prop from the selected `<Nav>` descendant, and an event.
   *
   * ```js
   * function (
   *  eventKey: mixed,
   *  event?: SyntheticEvent
   * )
   * ```
   *
   * For basic closing behavior after all `<Nav>` descendant onSelect events in
   * mobile viewports, try using collapseOnSelect.
   *
   * Note: If you are manually closing the navbar using this `OnSelect` prop,
   * ensure that you are setting `expanded` to false and not *toggling* between
   * true and false.
   */
  onSelect: prop_types_default.a.func,

  /**
   * Toggles `expanded` to `false` after the onSelect event of a descendant of a
   * child `<Nav>` fires. Does nothing if no `<Nav>` or `<Nav>` descendants exist.
   *
   * Manually controlling `expanded` via the onSelect callback is recommended instead,
   * for more complex operations that need to be executed after
   * the `select` event of `<Nav>` descendants.
   */
  collapseOnSelect: prop_types_default.a.bool,

  /**
   * Controls the visiblity of the navbar body
   *
   * @controllable onToggle
   */
  expanded: prop_types_default.a.bool,

  /**
   * The ARIA role for the navbar, will default to 'navigation' for
   * Navbars whose `as` is something other than `<nav>`.
   *
   * @default 'navigation'
   */
  role: prop_types_default.a.string
};
var Navbar_defaultProps = {
  expand: true,
  variant: 'light',
  collapseOnSelect: false
};
var Navbar = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (props, ref) {
  var _useUncontrolled = useUncontrolled(props, {
    expanded: 'onToggle'
  }),
      bsPrefix = _useUncontrolled.bsPrefix,
      expand = _useUncontrolled.expand,
      variant = _useUncontrolled.variant,
      bg = _useUncontrolled.bg,
      fixed = _useUncontrolled.fixed,
      sticky = _useUncontrolled.sticky,
      className = _useUncontrolled.className,
      children = _useUncontrolled.children,
      _useUncontrolled$as = _useUncontrolled.as,
      Component = _useUncontrolled$as === void 0 ? 'nav' : _useUncontrolled$as,
      expanded = _useUncontrolled.expanded,
      _onToggle = _useUncontrolled.onToggle,
      onSelect = _useUncontrolled.onSelect,
      collapseOnSelect = _useUncontrolled.collapseOnSelect,
      controlledProps = _objectWithoutPropertiesLoose(_useUncontrolled, ["bsPrefix", "expand", "variant", "bg", "fixed", "sticky", "className", "children", "as", "expanded", "onToggle", "onSelect", "collapseOnSelect"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'navbar');
  var handleCollapse = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useCallback"])(function () {
    if (onSelect) onSelect.apply(void 0, arguments);

    if (collapseOnSelect && expanded) {
      _onToggle(false);
    }
  }, [onSelect, collapseOnSelect, expanded, _onToggle]); // will result in some false positives but that seems better
  // than false negatives. strict `undefined` check allows explicit
  // "nulling" of the role if the user really doesn't want one

  if (controlledProps.role === undefined && Component !== 'nav') {
    controlledProps.role = 'navigation';
  }

  var expandClass = bsPrefix + "-expand";
  if (typeof expand === 'string') expandClass = expandClass + "-" + expand;
  var navbarContext = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useMemo"])(function () {
    return {
      onToggle: function onToggle() {
        return _onToggle(!expanded);
      },
      bsPrefix: bsPrefix,
      expanded: expanded
    };
  }, [bsPrefix, expanded, _onToggle]);
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(NavbarContext.Provider, {
    value: navbarContext,
    __source: {
      fileName: Navbar_jsxFileName,
      lineNumber: 177
    },
    __self: this
  }, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_SelectableContext.Provider, {
    value: handleCollapse,
    __source: {
      fileName: Navbar_jsxFileName,
      lineNumber: 178
    },
    __self: this
  }, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({
    ref: ref
  }, controlledProps, {
    className: classnames_default()(className, bsPrefix, expand && expandClass, variant && bsPrefix + "-" + variant, bg && "bg-" + bg, sticky && "sticky-" + sticky, fixed && "fixed-" + fixed),
    __source: {
      fileName: Navbar_jsxFileName,
      lineNumber: 179
    },
    __self: this
  }), children)));
});
Navbar.propTypes = Navbar_propTypes;
Navbar.defaultProps = Navbar_defaultProps;
Navbar.displayName = 'Navbar';
Navbar.Brand = src_NavbarBrand;
Navbar.Toggle = src_NavbarToggle;
Navbar.Collapse = src_NavbarCollapse;
Navbar.Text = createWithBsPrefix('navbar-text', {
  Component: 'span'
});
/* harmony default export */ var src_Navbar = (Navbar);
// CONCATENATED MODULE: ./src/NavDropdown.js


var NavDropdown_jsxFileName = "/Users/jquense/src/react-bootstrap/src/NavDropdown.js";





var NavDropdown_propTypes = {
  /**
   * An html id attribute for the Toggle button, necessary for assistive technologies, such as screen readers.
   * @type {string|number}
   * @required
   */
  id: prop_types_default.a.any,

  /** An `onClick` handler passed to the Toggle component */
  onClick: prop_types_default.a.func,

  /** The content of the non-toggle Button.  */
  title: prop_types_default.a.node.isRequired,

  /** Disables the toggle NavLink  */
  disabled: prop_types_default.a.bool,

  /** Style the toggle NavLink as active  */
  active: prop_types_default.a.bool,

  /** An ARIA accessible role applied to the Menu component. When set to 'menu', The dropdown */
  menuRole: prop_types_default.a.string,

  /**
   *  Which event when fired outside the component will cause it to be closed.
   *
   * _see [DropdownMenu](#menu-props) for more details_
   */
  rootCloseEvent: prop_types_default.a.string,

  /** @ignore */
  bsPrefix: prop_types_default.a.string
};
var NavDropdown = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var id = _ref.id,
      title = _ref.title,
      children = _ref.children,
      bsPrefix = _ref.bsPrefix,
      rootCloseEvent = _ref.rootCloseEvent,
      menuRole = _ref.menuRole,
      disabled = _ref.disabled,
      active = _ref.active,
      props = _objectWithoutPropertiesLoose(_ref, ["id", "title", "children", "bsPrefix", "rootCloseEvent", "menuRole", "disabled", "active"]);

  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_Dropdown, _extends({
    ref: ref
  }, props, {
    as: src_NavItem,
    __source: {
      fileName: NavDropdown_jsxFileName,
      lineNumber: 57
    },
    __self: this
  }), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_Dropdown.Toggle, {
    id: id,
    eventKey: null,
    active: active,
    disabled: disabled,
    childBsPrefix: bsPrefix,
    as: src_NavLink,
    __source: {
      fileName: NavDropdown_jsxFileName,
      lineNumber: 58
    },
    __self: this
  }, title), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_Dropdown.Menu, {
    role: menuRole,
    rootCloseEvent: rootCloseEvent,
    __source: {
      fileName: NavDropdown_jsxFileName,
      lineNumber: 69
    },
    __self: this
  }, children));
});
NavDropdown.displayName = 'NavDropdown';
NavDropdown.propTypes = NavDropdown_propTypes;
NavDropdown.Item = src_Dropdown.Item;
NavDropdown.Divider = src_Dropdown.Divider;
NavDropdown.Header = src_Dropdown.Header;
/* harmony default export */ var src_NavDropdown = (NavDropdown);
// CONCATENATED MODULE: ./node_modules/react-overlays/esm/Overlay.js











/**
 * Built on top of `Popper.js`, the overlay component is
 * great for custom tooltip overlays.
 */

var Overlay = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (props, outerRef) {
  var flip = props.flip,
      placement = props.placement,
      containerPadding = props.containerPadding,
      _props$popperConfig = props.popperConfig,
      popperConfig = _props$popperConfig === void 0 ? {} : _props$popperConfig,
      Transition = props.transition;

  var _useCallbackRef = useCallbackRef(),
      rootElement = _useCallbackRef[0],
      attachRef = _useCallbackRef[1];

  var _useCallbackRef2 = useCallbackRef(),
      arrowElement = _useCallbackRef2[0],
      attachArrowRef = _useCallbackRef2[1];

  var mergedRef = esm_useMergedRefs(attachRef, outerRef);
  var container = useWaitForDOMRef(props.container);
  var target = useWaitForDOMRef(props.target);

  var _useState = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useState"])(!props.show),
      exited = _useState[0],
      setExited = _useState[1];

  var _popperConfig$modifie = popperConfig.modifiers,
      modifiers = _popperConfig$modifie === void 0 ? {} : _popperConfig$modifie;

  var _usePopper = usePopper(target, rootElement, _extends({}, popperConfig, {
    placement: placement || 'bottom',
    enableEvents: props.show,
    modifiers: _extends({}, modifiers, {
      preventOverflow: _extends({
        padding: containerPadding || 5
      }, modifiers.preventOverflow),
      arrow: _extends({}, modifiers.arrow, {
        enabled: !!arrowElement,
        element: arrowElement
      }),
      flip: _extends({
        enabled: !!flip
      }, modifiers.preventOverflow)
    })
  })),
      styles = _usePopper.styles,
      arrowStyles = _usePopper.arrowStyles,
      popper = _objectWithoutPropertiesLoose(_usePopper, ["styles", "arrowStyles"]);

  if (props.show) {
    if (exited) setExited(false);
  } else if (!props.transition && !exited) {
    setExited(true);
  }

  var handleHidden = function handleHidden() {
    setExited(true);

    if (props.onExited) {
      props.onExited.apply(props, arguments);
    }
  }; // Don't un-render the overlay while it's transitioning out.


  var mountOverlay = props.show || Transition && !exited;
  esm_useRootClose(rootElement, props.onHide, {
    disabled: !props.rootClose || props.rootCloseDisabled,
    clickTrigger: props.rootCloseEvent
  });

  if (!mountOverlay) {
    // Don't bother showing anything if we don't have to.
    return null;
  }

  var child = props.children(_extends({}, popper, {
    show: props.show,
    props: {
      style: styles,
      ref: mergedRef
    },
    arrowProps: {
      style: arrowStyles,
      ref: attachArrowRef
    }
  }));

  if (Transition) {
    var onExit = props.onExit,
        onExiting = props.onExiting,
        onEnter = props.onEnter,
        onEntering = props.onEntering,
        onEntered = props.onEntered;
    child = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Transition, {
      "in": props.show,
      appear: true,
      onExit: onExit,
      onExiting: onExiting,
      onExited: handleHidden,
      onEnter: onEnter,
      onEntering: onEntering,
      onEntered: onEntered
    }, child);
  }

  return container ? external_root_ReactDOM_commonjs2_react_dom_commonjs_react_dom_amd_react_dom_default.a.createPortal(child, container) : null;
});
Overlay.displayName = 'Overlay';
Overlay.propTypes = {
  /**
   * Set the visibility of the Overlay
   */
  show: prop_types_default.a.bool,

  /** Specify where the overlay element is positioned in relation to the target element */
  placement: prop_types_default.a.oneOf(esm_popper["a" /* default */].placements),

  /**
   * A DOM Element, Ref to an element, or function that returns either. The `target` element is where
   * the overlay is positioned relative to.
   */
  target: prop_types_default.a.any,

  /**
   * A DOM Element, Ref to an element, or function that returns either. The `container` will have the Portal children
   * appended to it.
   */
  container: prop_types_default.a.any,

  /**
   * Enables the Popper.js `flip` modifier, allowing the Overlay to
   * automatically adjust it's placement in case of overlap with the viewport or toggle.
   * Refer to the [flip docs](https://popper.js.org/popper-documentation.html#modifiers..flip.enabled) for more info
   */
  flip: prop_types_default.a.bool,

  /**
   * A render prop that returns an element to overlay and position. See
   * the [react-popper documentation](https://github.com/FezVrasta/react-popper#children) for more info.
   *
   * @type {Function ({
   *   show: boolean,
   *   placement: Placement,
   *   outOfBoundaries: ?boolean,
   *   scheduleUpdate: () => void,
   *   props: {
   *     ref: (?HTMLElement) => void,
   *     style: { [string]: string | number },
   *     aria-labelledby: ?string
   *   },
   *   arrowProps: {
   *     ref: (?HTMLElement) => void,
   *     style: { [string]: string | number },
   *   },
   * }) => React.Element}
   */
  children: prop_types_default.a.func.isRequired,

  /**
   * Control how much space there is between the edge of the boundary element and overlay.
   * A convenience shortcut to setting `popperConfig.modfiers.preventOverflow.padding`
   */
  containerPadding: prop_types_default.a.number,

  /**
   * A set of popper options and props passed directly to react-popper's Popper component.
   */
  popperConfig: prop_types_default.a.object,

  /**
   * Specify whether the overlay should trigger `onHide` when the user clicks outside the overlay
   */
  rootClose: prop_types_default.a.bool,

  /**
   * Specify event for toggling overlay
   */
  rootCloseEvent: prop_types_default.a.oneOf(['click', 'mousedown']),

  /**
   * Specify disabled for disable RootCloseWrapper
   */
  rootCloseDisabled: prop_types_default.a.bool,

  /**
   * A Callback fired by the Overlay when it wishes to be hidden.
   *
   * __required__ when `rootClose` is `true`.
   *
   * @type func
   */
  onHide: function onHide(props) {
    var propType = prop_types_default.a.func;

    if (props.rootClose) {
      propType = propType.isRequired;
    }

    for (var _len = arguments.length, args = new Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
      args[_key - 1] = arguments[_key];
    }

    return propType.apply(void 0, [props].concat(args));
  },

  /**
   * A `react-transition-group@2.0.0` `<Transition/>` component
   * used to animate the overlay as it changes visibility.
   */
  transition: prop_types_default.a.elementType,

  /**
   * Callback fired before the Overlay transitions in
   */
  onEnter: prop_types_default.a.func,

  /**
   * Callback fired as the Overlay begins to transition in
   */
  onEntering: prop_types_default.a.func,

  /**
   * Callback fired after the Overlay finishes transitioning in
   */
  onEntered: prop_types_default.a.func,

  /**
   * Callback fired right before the Overlay transitions out
   */
  onExit: prop_types_default.a.func,

  /**
   * Callback fired as the Overlay begins to transition out
   */
  onExiting: prop_types_default.a.func,

  /**
   * Callback fired after the Overlay finishes transitioning out
   */
  onExited: prop_types_default.a.func
};
Overlay.defaultProps = {
  containerPadding: 5
};
/* harmony default export */ var esm_Overlay = (Overlay);
// CONCATENATED MODULE: ./src/Overlay.js


var Overlay_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Overlay.js";







var Overlay_propTypes = {
  /**
   * A component instance, DOM node, or function that returns either.
   * The `container` element will have the Overlay appended to it via a React portal.
   */
  container: prop_types_default.a.oneOfType([lib["componentOrElement"], prop_types_default.a.func]),

  /**
   * A component instance, DOM node, or function that returns either.
   * The overlay will be positioned in relation to the `target`
   */
  target: prop_types_default.a.oneOfType([lib["componentOrElement"], prop_types_default.a.func]),

  /**
   * Set the visibility of the Overlay
   */
  show: prop_types_default.a.bool,

  /**
   * A set of popper options and props passed directly to react-popper's Popper component.
   */
  popperConfig: prop_types_default.a.object,

  /**
   * Specify whether the overlay should trigger onHide when the user clicks outside the overlay
   */
  rootClose: prop_types_default.a.bool,

  /**
   * Specify event for triggering a "root close" toggle.
   */
  rootCloseEvent: prop_types_default.a.oneOf(['click', 'mousedown']),

  /**
   * A callback invoked by the overlay when it wishes to be hidden. Required if
   * `rootClose` is specified.
   */
  onHide: prop_types_default.a.func,

  /**
   * Animate the entering and exiting of the Ovelay. `true` will use the `<Fade>` transition,
   * or a custom react-transition-group `<Transition>` component can be provided.
   */
  transition: prop_types_default.a.oneOfType([prop_types_default.a.bool, lib["elementType"]]),

  /**
   * Callback fired before the Overlay transitions in
   */
  onEnter: prop_types_default.a.func,

  /**
   * Callback fired as the Overlay begins to transition in
   */
  onEntering: prop_types_default.a.func,

  /**
   * Callback fired after the Overlay finishes transitioning in
   */
  onEntered: prop_types_default.a.func,

  /**
   * Callback fired right before the Overlay transitions out
   */
  onExit: prop_types_default.a.func,

  /**
   * Callback fired as the Overlay begins to transition out
   */
  onExiting: prop_types_default.a.func,

  /**
   * Callback fired after the Overlay finishes transitioning out
   */
  onExited: prop_types_default.a.func,

  /**
   * The placement of the Overlay in relation to it's `target`.
   */
  placement: prop_types_default.a.oneOf(['auto-start', 'auto', 'auto-end', 'top-start', 'top', 'top-end', 'right-start', 'right', 'right-end', 'bottom-end', 'bottom', 'bottom-start', 'left-end', 'left', 'left-start'])
};
var Overlay_defaultProps = {
  transition: src_Fade,
  rootClose: false,
  show: false,
  placement: 'top'
};

function wrapRefs(props, arrowProps) {
  var ref = props.ref;
  var aRef = arrowProps.ref;

  props.ref = ref.__wrapped || (ref.__wrapped = function (r) {
    return ref(Object(external_root_ReactDOM_commonjs2_react_dom_commonjs_react_dom_amd_react_dom_["findDOMNode"])(r));
  });

  arrowProps.ref = aRef.__wrapped || (aRef.__wrapped = function (r) {
    return aRef(Object(external_root_ReactDOM_commonjs2_react_dom_commonjs_react_dom_amd_react_dom_["findDOMNode"])(r));
  });
}

function Overlay_Overlay(_ref) {
  var overlay = _ref.children,
      transition = _ref.transition,
      outerProps = _objectWithoutPropertiesLoose(_ref, ["children", "transition"]);

  transition = transition === true ? src_Fade : transition || null;
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(esm_Overlay, _extends({}, outerProps, {
    transition: transition,
    __source: {
      fileName: Overlay_jsxFileName,
      lineNumber: 127
    },
    __self: this
  }), function (_ref2) {
    var overlayProps = _ref2.props,
        arrowProps = _ref2.arrowProps,
        show = _ref2.show,
        props = _objectWithoutPropertiesLoose(_ref2, ["props", "arrowProps", "show"]);

    wrapRefs(overlayProps, arrowProps);
    if (typeof overlay === 'function') return overlay(_extends({}, props, {}, overlayProps, {
      show: show,
      arrowProps: arrowProps
    }));
    return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.cloneElement(overlay, _extends({}, props, {}, overlayProps, {
      arrowProps: arrowProps,
      className: classnames_default()(overlay.props.className, !transition && show && 'show'),
      style: _extends({}, overlay.props.style, {}, overlayProps.style)
    }));
  });
}

Overlay_Overlay.propTypes = Overlay_propTypes;
Overlay_Overlay.defaultProps = Overlay_defaultProps;
/* harmony default export */ var src_Overlay = (Overlay_Overlay);
// CONCATENATED MODULE: ./src/OverlayTrigger.js



var OverlayTrigger_jsxFileName = "/Users/jquense/src/react-bootstrap/src/OverlayTrigger.js";

/* eslint-disable max-classes-per-file */







var OverlayTrigger_RefHolder =
/*#__PURE__*/
function (_React$Component) {
  _inheritsLoose(RefHolder, _React$Component);

  function RefHolder() {
    return _React$Component.apply(this, arguments) || this;
  }

  var _proto = RefHolder.prototype;

  _proto.render = function render() {
    return this.props.children;
  };

  return RefHolder;
}(external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Component);

var normalizeDelay = function normalizeDelay(delay) {
  return delay && typeof delay === 'object' ? delay : {
    show: delay,
    hide: delay
  };
};

var triggerType = prop_types_default.a.oneOf(['click', 'hover', 'focus']);
var OverlayTrigger_propTypes = {
  children: prop_types_default.a.element.isRequired,

  /**
   * Specify which action or actions trigger Overlay visibility
   *
   * @type {'hover' | 'click' |'focus' | Array<'hover' | 'click' |'focus'>}
   */
  trigger: prop_types_default.a.oneOfType([triggerType, prop_types_default.a.arrayOf(triggerType)]),

  /**
   * A millisecond delay amount to show and hide the Overlay once triggered
   */
  delay: prop_types_default.a.oneOfType([prop_types_default.a.number, prop_types_default.a.shape({
    show: prop_types_default.a.number,
    hide: prop_types_default.a.number
  })]),

  /**
   * The initial visibility state of the Overlay. For more nuanced visibility
   * control, consider using the Overlay component directly.
   */
  defaultShow: prop_types_default.a.bool,

  /**
    The initial flip state of the Overlay.
   */
  flip: prop_types_default.a.bool,

  /**
   * An element or text to overlay next to the target.
   */
  overlay: prop_types_default.a.oneOfType([prop_types_default.a.func, prop_types_default.a.element.isRequired]),

  /**
   * A Popper.js config object passed to the the underlying popper instance.
   */
  popperConfig: prop_types_default.a.object,
  // Overridden props from `<Overlay>`.

  /**
   * @private
   */
  target: prop_types_default.a.oneOf([null]),

  /**
   * @private
   */
  onHide: prop_types_default.a.oneOf([null]),

  /**
   * @private
   */
  show: prop_types_default.a.oneOf([null])
};
var OverlayTrigger_defaultProps = {
  defaultOverlayShown: false,
  trigger: ['hover', 'focus']
}; // eslint-disable-next-line react/no-multi-comp

var OverlayTrigger_OverlayTrigger =
/*#__PURE__*/
function (_React$Component2) {
  _inheritsLoose(OverlayTrigger, _React$Component2);

  function OverlayTrigger(props, context) {
    var _this;

    _this = _React$Component2.call(this, props, context) || this;

    _this.getTarget = function () {
      return external_root_ReactDOM_commonjs2_react_dom_commonjs_react_dom_amd_react_dom_default.a.findDOMNode(_this.trigger.current);
    };

    _this.handleShow = function () {
      clearTimeout(_this._timeout);
      _this._hoverState = 'show';
      var delay = normalizeDelay(_this.props.delay);

      if (!delay.show) {
        _this.show();

        return;
      }

      _this._timeout = setTimeout(function () {
        if (_this._hoverState === 'show') _this.show();
      }, delay.show);
    };

    _this.handleHide = function () {
      clearTimeout(_this._timeout);
      _this._hoverState = 'hide';
      var delay = normalizeDelay(_this.props.delay);

      if (!delay.hide) {
        _this.hide();

        return;
      }

      _this._timeout = setTimeout(function () {
        if (_this._hoverState === 'hide') _this.hide();
      }, delay.hide);
    };

    _this.handleFocus = function (e) {
      var _this$getChildProps = _this.getChildProps(),
          onFocus = _this$getChildProps.onFocus;

      _this.handleShow(e);

      if (onFocus) onFocus(e);
    };

    _this.handleBlur = function (e) {
      var _this$getChildProps2 = _this.getChildProps(),
          onBlur = _this$getChildProps2.onBlur;

      _this.handleHide(e);

      if (onBlur) onBlur(e);
    };

    _this.handleClick = function (e) {
      var _this$getChildProps3 = _this.getChildProps(),
          onClick = _this$getChildProps3.onClick;

      if (_this.state.show) _this.hide();else _this.show();
      if (onClick) onClick(e);
    };

    _this.handleMouseOver = function (e) {
      _this.handleMouseOverOut(_this.handleShow, e, 'fromElement');
    };

    _this.handleMouseOut = function (e) {
      return _this.handleMouseOverOut(_this.handleHide, e, 'toElement');
    };

    _this.trigger = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createRef();
    _this.state = {
      show: !!props.defaultShow
    }; // We add aria-describedby in the case where the overlay is a role="tooltip"
    // for other cases describedby isn't appropriate (e.g. a popover with inputs) so we don't add it.

    _this.ariaModifier = {
      enabled: true,
      order: 900,
      fn: function fn(data) {
        var popper = data.instance.popper;

        var target = _this.getTarget();

        if (!_this.state.show || !target) return data;
        var role = popper.getAttribute('role') || '';

        if (popper.id && role.toLowerCase() === 'tooltip') {
          target.setAttribute('aria-describedby', popper.id);
        }

        return data;
      }
    };
    return _this;
  }

  var _proto2 = OverlayTrigger.prototype;

  _proto2.componentWillUnmount = function componentWillUnmount() {
    clearTimeout(this._timeout);
  };

  _proto2.getChildProps = function getChildProps() {
    return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Children.only(this.props.children).props;
  };

  // Simple implementation of mouseEnter and mouseLeave.
  // React's built version is broken: https://github.com/facebook/react/issues/4251
  // for cases when the trigger is disabled and mouseOut/Over can cause flicker
  // moving from one child element to another.
  _proto2.handleMouseOverOut = function handleMouseOverOut(handler, e, relatedNative) {
    var target = e.currentTarget;
    var related = e.relatedTarget || e.nativeEvent[relatedNative];

    if ((!related || related !== target) && !contains(target, related)) {
      handler(e);
    }
  };

  _proto2.hide = function hide() {
    this.setState({
      show: false
    });
  };

  _proto2.show = function show() {
    this.setState({
      show: true
    });
  };

  _proto2.render = function render() {
    var _this$props = this.props,
        trigger = _this$props.trigger,
        overlay = _this$props.overlay,
        children = _this$props.children,
        _this$props$popperCon = _this$props.popperConfig,
        popperConfig = _this$props$popperCon === void 0 ? {} : _this$props$popperCon,
        props = _objectWithoutPropertiesLoose(_this$props, ["trigger", "overlay", "children", "popperConfig"]);

    delete props.delay;
    delete props.defaultShow;
    var child = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Children.only(children);
    var triggerProps = {};
    var triggers = trigger == null ? [] : [].concat(trigger);

    if (triggers.indexOf('click') !== -1) {
      triggerProps.onClick = this.handleClick;
    }

    if (triggers.indexOf('focus') !== -1) {
      triggerProps.onFocus = this.handleShow;
      triggerProps.onBlur = this.handleHide;
    }

    if (triggers.indexOf('hover') !== -1) {
       false ? undefined : void 0;
      triggerProps.onMouseOver = this.handleMouseOver;
      triggerProps.onMouseOut = this.handleMouseOut;
    }

    return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Fragment, null, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(OverlayTrigger_RefHolder, {
      ref: this.trigger,
      __source: {
        fileName: OverlayTrigger_jsxFileName,
        lineNumber: 245
      },
      __self: this
    }, Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["cloneElement"])(child, triggerProps)), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_Overlay, _extends({}, props, {
      popperConfig: _extends({}, popperConfig, {
        modifiers: _extends({}, popperConfig.modifiers, {
          ariaModifier: this.ariaModifier
        })
      }),
      show: this.state.show,
      onHide: this.handleHide,
      target: this.getTarget,
      __source: {
        fileName: OverlayTrigger_jsxFileName,
        lineNumber: 248
      },
      __self: this
    }), overlay));
  };

  return OverlayTrigger;
}(external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Component);

OverlayTrigger_OverlayTrigger.propTypes = OverlayTrigger_propTypes;
OverlayTrigger_OverlayTrigger.defaultProps = OverlayTrigger_defaultProps;
/* harmony default export */ var src_OverlayTrigger = (OverlayTrigger_OverlayTrigger);
// CONCATENATED MODULE: ./src/PageItem.js



var PageItem_jsxFileName = "/Users/jquense/src/react-bootstrap/src/PageItem.js";

/* eslint-disable react/no-multi-comp */




var PageItem_propTypes = {
  /** Disables the PageItem */
  disabled: prop_types_default.a.bool,

  /** Styles PageItem as active, and renders a `<span>` instead of an `<a>`. */
  active: prop_types_default.a.bool,

  /** An accessible label indicating the active state.. */
  activeLabel: prop_types_default.a.string
};
var PageItem_defaultProps = {
  active: false,
  disabled: false,
  activeLabel: '(current)'
};
var PageItem = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var active = _ref.active,
      disabled = _ref.disabled,
      className = _ref.className,
      style = _ref.style,
      activeLabel = _ref.activeLabel,
      children = _ref.children,
      props = _objectWithoutPropertiesLoose(_ref, ["active", "disabled", "className", "style", "activeLabel", "children"]);

  var Component = active || disabled ? 'span' : src_SafeAnchor;
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("li", {
    ref: ref,
    style: style,
    className: classnames_default()(className, 'page-item', {
      active: active,
      disabled: disabled
    }),
    __source: {
      fileName: PageItem_jsxFileName,
      lineNumber: 32
    },
    __self: this
  }, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({
    className: "page-link",
    disabled: disabled
  }, props, {
    __source: {
      fileName: PageItem_jsxFileName,
      lineNumber: 37
    },
    __self: this
  }), children, active && activeLabel && external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("span", {
    className: "sr-only",
    __source: {
      fileName: PageItem_jsxFileName,
      lineNumber: 40
    },
    __self: this
  }, activeLabel)));
});
PageItem.propTypes = PageItem_propTypes;
PageItem.defaultProps = PageItem_defaultProps;
PageItem.displayName = 'PageItem';
/* harmony default export */ var src_PageItem = (PageItem);

function createButton(name, defaultValue, label) {
  var _class, _temp;

  if (label === void 0) {
    label = name;
  }

  return _temp = _class =
  /*#__PURE__*/
  function (_React$Component) {
    _inheritsLoose(_class, _React$Component);

    function _class() {
      return _React$Component.apply(this, arguments) || this;
    }

    var _proto = _class.prototype;

    _proto.render = function render() {
      var _this$props = this.props,
          children = _this$props.children,
          props = _objectWithoutPropertiesLoose(_this$props, ["children"]);

      delete props.active;
      return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(PageItem, _extends({}, props, {
        __source: {
          fileName: PageItem_jsxFileName,
          lineNumber: 62
        },
        __self: this
      }), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("span", {
        "aria-hidden": "true",
        __source: {
          fileName: PageItem_jsxFileName,
          lineNumber: 63
        },
        __self: this
      }, children || defaultValue), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("span", {
        className: "sr-only",
        __source: {
          fileName: PageItem_jsxFileName,
          lineNumber: 64
        },
        __self: this
      }, label));
    };

    return _class;
  }(external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Component), _class.displayName = name, _temp;
}

var First = createButton('First', '«');
var Prev = createButton('Prev', '‹', 'Previous');
var Ellipsis = createButton('Ellipsis', '…', 'More');
var Next = createButton('Next', '›');
var Last = createButton('Last', '»');
// CONCATENATED MODULE: ./src/Pagination.js


var Pagination_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Pagination.js";





var Pagination_propTypes = {
  /**
   * @default 'pagination'
   * */
  bsPrefix: prop_types_default.a.string,

  /**
   * Set's the size of all PageItems.
   *
   * @type {('sm'|'lg')}
   */
  size: prop_types_default.a.string
};
/**
 * @property {PageItem} Item
 * @property {PageItem} First
 * @property {PageItem} Prev
 * @property {PageItem} Ellipsis
 * @property {PageItem} Next
 * @property {PageItem} Last
 */

var Pagination = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      className = _ref.className,
      children = _ref.children,
      size = _ref.size,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "className", "children", "size"]);

  var decoratedBsPrefix = useBootstrapPrefix(bsPrefix, 'pagination');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("ul", _extends({
    ref: ref
  }, props, {
    className: classnames_default()(className, decoratedBsPrefix, size && decoratedBsPrefix + "-" + size),
    __source: {
      fileName: Pagination_jsxFileName,
      lineNumber: 34
    },
    __self: this
  }), children);
});
Pagination.propTypes = Pagination_propTypes;
Pagination.First = First;
Pagination.Prev = Prev;
Pagination.Ellipsis = Ellipsis;
Pagination.Item = src_PageItem;
Pagination.Next = Next;
Pagination.Last = Last;
/* harmony default export */ var src_Pagination = (Pagination);
// CONCATENATED MODULE: ./src/PopoverTitle.js


var PopoverTitle_jsxFileName = "/Users/jquense/src/react-bootstrap/src/PopoverTitle.js";




var PopoverTitle_propTypes = {
  /** Set a custom element for this component */
  as: prop_types_default.a.elementType,

  /** @default 'popover-header' */
  bsPrefix: prop_types_default.a.string
};
var PopoverTitle = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'div' : _ref$as,
      bsPrefix = _ref.bsPrefix,
      className = _ref.className,
      children = _ref.children,
      props = _objectWithoutPropertiesLoose(_ref, ["as", "bsPrefix", "className", "children"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'popover-header');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({
    ref: ref
  }, props, {
    className: classnames_default()(bsPrefix, className),
    __source: {
      fileName: PopoverTitle_jsxFileName,
      lineNumber: 29
    },
    __self: this
  }), children);
});
PopoverTitle.propTypes = PopoverTitle_propTypes;
/* harmony default export */ var src_PopoverTitle = (PopoverTitle);
// CONCATENATED MODULE: ./src/PopoverContent.js


var PopoverContent_jsxFileName = "/Users/jquense/src/react-bootstrap/src/PopoverContent.js";




var PopoverContent_propTypes = {
  /** Set a custom element for this component */
  as: prop_types_default.a.elementType,

  /** @default 'popover-body' */
  bsPrefix: prop_types_default.a.string
};
var PopoverContent = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'div' : _ref$as,
      bsPrefix = _ref.bsPrefix,
      className = _ref.className,
      children = _ref.children,
      props = _objectWithoutPropertiesLoose(_ref, ["as", "bsPrefix", "className", "children"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'popover-body');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({
    ref: ref
  }, props, {
    className: classnames_default()(className, bsPrefix),
    __source: {
      fileName: PopoverContent_jsxFileName,
      lineNumber: 29
    },
    __self: this
  }), children);
});
PopoverContent.propTypes = PopoverContent_propTypes;
/* harmony default export */ var src_PopoverContent = (PopoverContent);
// CONCATENATED MODULE: ./src/Popover.js


var Popover_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Popover.js";







var Popover_propTypes = {
  /**
   * @default 'popover'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * An html id attribute, necessary for accessibility
   * @type {string|number}
   * @required
   */
  id: isRequiredForA11y_default()(prop_types_default.a.oneOfType([prop_types_default.a.string, prop_types_default.a.number])),

  /**
   * Sets the direction the Popover is positioned towards.
   *
   * > This is generally provided by the `Overlay` component positioning the popover
   */
  placement: prop_types_default.a.oneOf(['auto', 'top', 'bottom', 'left', 'right']),

  /**
   * An Overlay injected set of props for positioning the popover arrow.
   *
   * > This is generally provided by the `Overlay` component positioning the popover
   */
  arrowProps: prop_types_default.a.shape({
    ref: prop_types_default.a.any,
    style: prop_types_default.a.object
  }),

  /**
   * When this prop is set, it creates a Popover with a Popover.Content inside
   * passing the children directly to it
   */
  content: prop_types_default.a.bool,

  /** @private */
  scheduleUpdate: prop_types_default.a.func,

  /** @private */
  outOfBoundaries: prop_types_default.a.bool
};
var Popover_defaultProps = {
  placement: 'right'
};
var Popover = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      placement = _ref.placement,
      className = _ref.className,
      style = _ref.style,
      children = _ref.children,
      content = _ref.content,
      arrowProps = _ref.arrowProps,
      _ = _ref.scheduleUpdate,
      _1 = _ref.outOfBoundaries,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "placement", "className", "style", "children", "content", "arrowProps", "scheduleUpdate", "outOfBoundaries"]);

  var decoratedBsPrefix = useBootstrapPrefix(bsPrefix, 'popover');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", _extends({
    ref: ref,
    role: "tooltip",
    style: style,
    "x-placement": placement,
    className: classnames_default()(className, decoratedBsPrefix, "bs-popover-" + placement)
  }, props, {
    __source: {
      fileName: Popover_jsxFileName,
      lineNumber: 75
    },
    __self: this
  }), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", _extends({
    className: "arrow"
  }, arrowProps, {
    __source: {
      fileName: Popover_jsxFileName,
      lineNumber: 87
    },
    __self: this
  })), content ? external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_PopoverContent, {
    __source: {
      fileName: Popover_jsxFileName,
      lineNumber: 88
    },
    __self: this
  }, children) : children);
});
Popover.propTypes = Popover_propTypes;
Popover.defaultProps = Popover_defaultProps;
Popover.Title = src_PopoverTitle;
Popover.Content = src_PopoverContent;
/* harmony default export */ var src_Popover = (Popover);
// CONCATENATED MODULE: ./src/ProgressBar.js


var ProgressBar_jsxFileName = "/Users/jquense/src/react-bootstrap/src/ProgressBar.js";





var ROUND_PRECISION = 1000;
/**
 * Validate that children, if any, are instances of `<ProgressBar>`.
 */

function onlyProgressBar(props, propName, componentName) {
  var children = props[propName];

  if (!children) {
    return null;
  }

  var error = null;
  external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Children.forEach(children, function (child) {
    if (error) {
      return;
    }
    /**
     * Compare types in a way that works with libraries that patch and proxy
     * components like react-hot-loader.
     *
     * see https://github.com/gaearon/react-hot-loader#checking-element-types
     */


    var element = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(ProgressBar, {
      __source: {
        fileName: ProgressBar_jsxFileName,
        lineNumber: 33
      },
      __self: this
    });
    if (child.type === element.type) return;
    var childIdentifier = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.isValidElement(child) ? child.type.displayName || child.type.name || child.type : child;
    error = new Error("Children of " + componentName + " can contain only ProgressBar " + ("components. Found " + childIdentifier + "."));
  });
  return error;
}

var ProgressBar_propTypes = {
  /**
   * Minimum value progress can begin from
   */
  min: prop_types_default.a.number,

  /**
   * Current value of progress
   */
  now: prop_types_default.a.number,

  /**
   * Maximum value progress can reach
   */
  max: prop_types_default.a.number,

  /**
   * Show label that represents visual percentage.
   * EG. 60%
   */
  label: prop_types_default.a.node,

  /**
   * Hide's the label visually.
   */
  srOnly: prop_types_default.a.bool,

  /**
   * Uses a gradient to create a striped effect.
   */
  striped: prop_types_default.a.bool,

  /**
   * Animate's the stripes from right to left
   */
  animated: prop_types_default.a.bool,

  /**
   * @private
   * @default 'progress-bar'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * Sets the background class of the progress bar.
   *
   * @type ('success'|'danger'|'warning'|'info')
   */
  variant: prop_types_default.a.string,

  /**
   * Child elements (only allows elements of type <ProgressBar />)
   */
  children: onlyProgressBar,

  /**
   * @private
   */
  isChild: prop_types_default.a.bool
};
var ProgressBar_defaultProps = {
  min: 0,
  max: 100,
  animated: false,
  isChild: false,
  srOnly: false,
  striped: false
};

function getPercentage(now, min, max) {
  var percentage = (now - min) / (max - min) * 100;
  return Math.round(percentage * ROUND_PRECISION) / ROUND_PRECISION;
}

function renderProgressBar(_ref, ref) {
  var _classNames;

  var min = _ref.min,
      now = _ref.now,
      max = _ref.max,
      label = _ref.label,
      srOnly = _ref.srOnly,
      striped = _ref.striped,
      animated = _ref.animated,
      className = _ref.className,
      style = _ref.style,
      variant = _ref.variant,
      bsPrefix = _ref.bsPrefix,
      props = _objectWithoutPropertiesLoose(_ref, ["min", "now", "max", "label", "srOnly", "striped", "animated", "className", "style", "variant", "bsPrefix"]);

  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", _extends({
    ref: ref
  }, props, {
    role: "progressbar",
    className: classnames_default()(className, bsPrefix + "-bar", (_classNames = {}, _classNames["bg-" + variant] = variant, _classNames[bsPrefix + "-bar-animated"] = animated, _classNames[bsPrefix + "-bar-striped"] = animated || striped, _classNames)),
    style: _extends({
      width: getPercentage(now, min, max) + "%"
    }, style),
    "aria-valuenow": now,
    "aria-valuemin": min,
    "aria-valuemax": max,
    __source: {
      fileName: ProgressBar_jsxFileName,
      lineNumber: 141
    },
    __self: this
  }), srOnly ? external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("span", {
    className: "sr-only",
    __source: {
      fileName: ProgressBar_jsxFileName,
      lineNumber: 155
    },
    __self: this
  }, label) : label);
}

renderProgressBar.propTypes = ProgressBar_propTypes;
var ProgressBar = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref2, ref) {
  var isChild = _ref2.isChild,
      props = _objectWithoutPropertiesLoose(_ref2, ["isChild"]);

  props.bsPrefix = useBootstrapPrefix(props.bsPrefix, 'progress');

  if (isChild) {
    return renderProgressBar(props, ref);
  }

  var min = props.min,
      now = props.now,
      max = props.max,
      label = props.label,
      srOnly = props.srOnly,
      striped = props.striped,
      animated = props.animated,
      bsPrefix = props.bsPrefix,
      variant = props.variant,
      className = props.className,
      children = props.children,
      wrapperProps = _objectWithoutPropertiesLoose(props, ["min", "now", "max", "label", "srOnly", "striped", "animated", "bsPrefix", "variant", "className", "children"]);

  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", _extends({
    ref: ref
  }, wrapperProps, {
    className: classnames_default()(className, bsPrefix),
    __source: {
      fileName: ProgressBar_jsxFileName,
      lineNumber: 185
    },
    __self: this
  }), children ? map(children, function (child) {
    return Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["cloneElement"])(child, {
      isChild: true
    });
  }) : renderProgressBar({
    min: min,
    now: now,
    max: max,
    label: label,
    srOnly: srOnly,
    striped: striped,
    animated: animated,
    bsPrefix: bsPrefix,
    variant: variant
  }, ref));
});
ProgressBar.displayName = 'ProgressBar';
ProgressBar.propTypes = ProgressBar_propTypes;
ProgressBar.defaultProps = ProgressBar_defaultProps;
/* harmony default export */ var src_ProgressBar = (ProgressBar);
// CONCATENATED MODULE: ./src/ResponsiveEmbed.js


var ResponsiveEmbed_jsxFileName = "/Users/jquense/src/react-bootstrap/src/ResponsiveEmbed.js";




var ResponsiveEmbed_propTypes = {
  /**
   * @default 'embed-responsive'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * This component requires a single child element
   */
  children: prop_types_default.a.element.isRequired,

  /**
   * Set the aspect ration of the embed
   */
  aspectRatio: prop_types_default.a.oneOf(['21by9', '16by9', '4by3', '1by1'])
};
var ResponsiveEmbed_defaultProps = {
  aspectRatio: '1by1'
};
var ResponsiveEmbed = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      className = _ref.className,
      children = _ref.children,
      aspectRatio = _ref.aspectRatio,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "className", "children", "aspectRatio"]);

  var decoratedBsPrefix = useBootstrapPrefix(bsPrefix, 'embed-responsive');
  var child = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Children.only(children);
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", _extends({
    ref: ref
  }, props, {
    className: classnames_default()(decoratedBsPrefix, className, aspectRatio && decoratedBsPrefix + "-" + aspectRatio),
    __source: {
      fileName: ResponsiveEmbed_jsxFileName,
      lineNumber: 33
    },
    __self: this
  }), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.cloneElement(child, {
    className: classnames_default()(child.props.className, decoratedBsPrefix + "-item")
  }));
});
ResponsiveEmbed.propTypes = ResponsiveEmbed_propTypes;
ResponsiveEmbed.defaultProps = ResponsiveEmbed_defaultProps;
/* harmony default export */ var src_ResponsiveEmbed = (ResponsiveEmbed);
// CONCATENATED MODULE: ./src/Row.js


var Row_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Row.js";




var Row_propTypes = {
  /**
   * @default 'row'
   */
  bsPrefix: prop_types_default.a.string,

  /** Removes the gutter spacing between `Col`s as well as any added negative margins. */
  noGutters: prop_types_default.a.bool.isRequired,
  as: prop_types_default.a.elementType
};
var Row_defaultProps = {
  noGutters: false
};
var Row = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (props, ref) {
  var bsPrefix = props.bsPrefix,
      noGutters = props.noGutters,
      _props$as = props.as,
      Component = _props$as === void 0 ? 'div' : _props$as,
      className = props.className,
      otherProps = _objectWithoutPropertiesLoose(props, ["bsPrefix", "noGutters", "as", "className"]);

  var decoratedBsPrefix = useBootstrapPrefix(bsPrefix, 'row');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({
    ref: ref
  }, otherProps, {
    className: classnames_default()(className, decoratedBsPrefix, noGutters && 'no-gutters'),
    __source: {
      fileName: Row_jsxFileName,
      lineNumber: 36
    },
    __self: this
  }));
});
Row.propTypes = Row_propTypes;
Row.defaultProps = Row_defaultProps;
/* harmony default export */ var src_Row = (Row);
// CONCATENATED MODULE: ./src/Spinner.js


var Spinner_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Spinner.js";




var Spinner_propTypes = {
  /**
   * @default 'spinner'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * The visual color style of the spinner
   *
   * @type {('primary'|'secondary'|'success'|'danger'|'warning'|'info'|'light'|'dark')}
   */
  variant: prop_types_default.a.string,

  /**
   * Changes the animation style of the spinner.
   *
   * @type {('border'|'grow')}
   * @default true
   */
  animation: prop_types_default.a.oneOf(['border', 'grow']).isRequired,

  /**
   * Component size variations.
   *
   * @type {('sm')}
   */
  size: prop_types_default.a.string,

  /**
   * This component may be used to wrap child elements or components.
   */
  children: prop_types_default.a.element,

  /**
   * An ARIA accessible role applied to the Menu component. This should generally be set to 'status'
   */
  role: prop_types_default.a.string,

  /**
   * @default div
   */
  as: prop_types_default.a.elementType
};
var Spinner = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      variant = _ref.variant,
      animation = _ref.animation,
      size = _ref.size,
      children = _ref.children,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'div' : _ref$as,
      className = _ref.className,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "variant", "animation", "size", "children", "as", "className"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'spinner');
  var bsSpinnerPrefix = bsPrefix + "-" + animation;
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({
    ref: ref
  }, props, {
    className: classnames_default()(className, bsSpinnerPrefix, size && bsSpinnerPrefix + "-" + size, variant && "text-" + variant),
    __source: {
      fileName: Spinner_jsxFileName,
      lineNumber: 70
    },
    __self: this
  }), children);
});
Spinner.propTypes = Spinner_propTypes;
Spinner.displayName = 'Spinner';
/* harmony default export */ var src_Spinner = (Spinner);
// CONCATENATED MODULE: ./src/SplitButton.js


var SplitButton_jsxFileName = "/Users/jquense/src/react-bootstrap/src/SplitButton.js";





var SplitButton_propTypes = {
  /**
   * An html id attribute for the Toggle button, necessary for assistive technologies, such as screen readers.
   * @type {string|number}
   * @required
   */
  id: prop_types_default.a.any,

  /**
   * Accessible label for the toggle; the value of `title` if not specified.
   */
  toggleLabel: prop_types_default.a.string,

  /** An `href` passed to the non-toggle Button */
  href: prop_types_default.a.string,

  /** An anchor `target` passed to the non-toggle Button */
  target: prop_types_default.a.string,

  /** An `onClick` handler passed to the non-toggle Button */
  onClick: prop_types_default.a.func,

  /** The content of the non-toggle Button.  */
  title: prop_types_default.a.node.isRequired,

  /** Disables both Buttons  */
  disabled: prop_types_default.a.bool,

  /** An ARIA accessible role applied to the Menu component. When set to 'menu', The dropdown */
  menuRole: prop_types_default.a.string,

  /**
   *  Which event when fired outside the component will cause it to be closed.
   *
   * _see [DropdownMenu](#menu-props) for more details_
   */
  rootCloseEvent: prop_types_default.a.string,

  /** @ignore */
  bsPrefix: prop_types_default.a.string,

  /** @ignore */
  variant: prop_types_default.a.string,

  /** @ignore */
  size: prop_types_default.a.string
};
var SplitButton_defaultProps = {
  toggleLabel: 'Toggle dropdown'
};
var SplitButton = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var id = _ref.id,
      bsPrefix = _ref.bsPrefix,
      size = _ref.size,
      variant = _ref.variant,
      title = _ref.title,
      toggleLabel = _ref.toggleLabel,
      children = _ref.children,
      onClick = _ref.onClick,
      href = _ref.href,
      target = _ref.target,
      menuRole = _ref.menuRole,
      rootCloseEvent = _ref.rootCloseEvent,
      props = _objectWithoutPropertiesLoose(_ref, ["id", "bsPrefix", "size", "variant", "title", "toggleLabel", "children", "onClick", "href", "target", "menuRole", "rootCloseEvent"]);

  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_Dropdown, _extends({
    ref: ref
  }, props, {
    as: src_ButtonGroup,
    __source: {
      fileName: SplitButton_jsxFileName,
      lineNumber: 76
    },
    __self: this
  }), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_Button, {
    size: size,
    variant: variant,
    disabled: props.disabled,
    bsPrefix: bsPrefix,
    href: href,
    target: target,
    onClick: onClick,
    __source: {
      fileName: SplitButton_jsxFileName,
      lineNumber: 77
    },
    __self: this
  }, title), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_Dropdown.Toggle, {
    split: true,
    id: id,
    size: size,
    variant: variant,
    disabled: props.disabled,
    childBsPrefix: bsPrefix,
    __source: {
      fileName: SplitButton_jsxFileName,
      lineNumber: 88
    },
    __self: this
  }, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("span", {
    className: "sr-only",
    __source: {
      fileName: SplitButton_jsxFileName,
      lineNumber: 96
    },
    __self: this
  }, toggleLabel)), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_Dropdown.Menu, {
    role: menuRole,
    rootCloseEvent: rootCloseEvent,
    __source: {
      fileName: SplitButton_jsxFileName,
      lineNumber: 99
    },
    __self: this
  }, children));
});
SplitButton.propTypes = SplitButton_propTypes;
SplitButton.defaultProps = SplitButton_defaultProps;
SplitButton.displayName = 'SplitButton';
/* harmony default export */ var src_SplitButton = (SplitButton);
// CONCATENATED MODULE: ./src/TabContainer.js
var TabContainer_jsxFileName = "/Users/jquense/src/react-bootstrap/src/TabContainer.js";





/* eslint-disable react/no-unused-prop-types */

var TabContainer_propTypes = {
  /**
   * HTML id attribute, required if no `generateChildId` prop
   * is specified.
   *
   * @type {string}
   */
  id: function id(props) {
    var error = null;

    if (!props.generateChildId) {
      for (var _len = arguments.length, args = new Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
        args[_key - 1] = arguments[_key];
      }

      error = prop_types_default.a.string.apply(prop_types_default.a, [props].concat(args));

      if (!error && !props.id) {
        error = new Error('In order to properly initialize Tabs in a way that is accessible ' + 'to assistive technologies (such as screen readers) an `id` or a ' + '`generateChildId` prop to TabContainer is required');
      }
    }

    return error;
  },

  /**
   * Sets a default animation strategy for all children `<TabPane>`s. Use
   * `false` to disable, `true` to enable the default `<Fade>` animation or
   * a react-transition-group v2 `<Transition/>` component.
   *
   * @type {{Transition | false}}
   * @default {Fade}
   */
  transition: prop_types_default.a.oneOfType([prop_types_default.a.oneOf([false]), prop_types_default.a.elementType]),

  /**
   * Wait until the first "enter" transition to mount tabs (add them to the DOM)
   */
  mountOnEnter: prop_types_default.a.bool,

  /**
   * Unmount tabs (remove it from the DOM) when they are no longer visible
   */
  unmountOnExit: prop_types_default.a.bool,

  /**
   * A function that takes an `eventKey` and `type` and returns a unique id for
   * child tab `<NavItem>`s and `<TabPane>`s. The function _must_ be a pure
   * function, meaning it should always return the _same_ id for the same set
   * of inputs. The default value requires that an `id` to be set for the
   * `<TabContainer>`.
   *
   * The `type` argument will either be `"tab"` or `"pane"`.
   *
   * @defaultValue (eventKey, type) => `${props.id}-${type}-${eventKey}`
   */
  generateChildId: prop_types_default.a.func,

  /**
   * A callback fired when a tab is selected.
   *
   * @controllable activeKey
   */
  onSelect: prop_types_default.a.func,

  /**
   * The `eventKey` of the currently active tab.
   *
   * @controllable onSelect
   */
  activeKey: prop_types_default.a.any
};

var TabContainer_TabContainer = function TabContainer(props) {
  var _useUncontrolled = useUncontrolled(props, {
    activeKey: 'onSelect'
  }),
      id = _useUncontrolled.id,
      generateCustomChildId = _useUncontrolled.generateChildId,
      onSelect = _useUncontrolled.onSelect,
      activeKey = _useUncontrolled.activeKey,
      transition = _useUncontrolled.transition,
      mountOnEnter = _useUncontrolled.mountOnEnter,
      unmountOnExit = _useUncontrolled.unmountOnExit,
      children = _useUncontrolled.children;

  var generateChildId = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useMemo"])(function () {
    return generateCustomChildId || function (key, type) {
      return id ? id + "-" + type + "-" + key : null;
    };
  }, [id, generateCustomChildId]);
  var tabContext = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useMemo"])(function () {
    return {
      onSelect: onSelect,
      activeKey: activeKey,
      transition: transition,
      mountOnEnter: mountOnEnter,
      unmountOnExit: unmountOnExit,
      getControlledId: function getControlledId(key) {
        return generateChildId(key, 'tabpane');
      },
      getControllerId: function getControllerId(key) {
        return generateChildId(key, 'tab');
      }
    };
  }, [onSelect, activeKey, transition, mountOnEnter, unmountOnExit, generateChildId]);
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_TabContext.Provider, {
    value: tabContext,
    __source: {
      fileName: TabContainer_jsxFileName,
      lineNumber: 124
    },
    __self: this
  }, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_SelectableContext.Provider, {
    value: onSelect,
    __source: {
      fileName: TabContainer_jsxFileName,
      lineNumber: 125
    },
    __self: this
  }, children));
};

TabContainer_TabContainer.propTypes = TabContainer_propTypes;
/* harmony default export */ var src_TabContainer = (TabContainer_TabContainer);
// CONCATENATED MODULE: ./src/TabContent.js


var TabContent_jsxFileName = "/Users/jquense/src/react-bootstrap/src/TabContent.js";




var TabContent_propTypes = {
  /**
   * @default 'tab-content'
   */
  bsPrefix: prop_types_default.a.string,
  as: prop_types_default.a.elementType
};
var TabContent = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      _ref$as = _ref.as,
      Component = _ref$as === void 0 ? 'div' : _ref$as,
      className = _ref.className,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "as", "className"]);

  var decoratedBsPrefix = useBootstrapPrefix(bsPrefix, 'tab-content');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({
    ref: ref
  }, props, {
    className: classnames_default()(className, decoratedBsPrefix),
    __source: {
      fileName: TabContent_jsxFileName,
      lineNumber: 29
    },
    __self: this
  }));
});
TabContent.propTypes = TabContent_propTypes;
/* harmony default export */ var src_TabContent = (TabContent);
// CONCATENATED MODULE: ./src/TabPane.js


var TabPane_jsxFileName = "/Users/jquense/src/react-bootstrap/src/TabPane.js";







var TabPane_propTypes = {
  /**
   * @default 'tab-pane'
   */
  bsPrefix: prop_types_default.a.string,
  as: prop_types_default.a.elementType,

  /**
   * A key that associates the `TabPane` with it's controlling `NavLink`.
   */
  eventKey: prop_types_default.a.any,

  /**
   * Toggles the active state of the TabPane, this is generally controlled by a
   * TabContainer.
   */
  active: prop_types_default.a.bool,

  /**
   * Use animation when showing or hiding `<TabPane>`s. Use `false` to disable,
   * `true` to enable the default `<Fade>` animation or
   * a react-transition-group v2 `<Transition/>` component.
   */
  transition: prop_types_default.a.oneOfType([prop_types_default.a.bool, prop_types_default.a.elementType]),

  /**
   *
   * @default 'tab-pane'
   */
  bsClass: prop_types_default.a.string,

  /**
   * Transition onEnter callback when animation is not `false`
   */
  onEnter: prop_types_default.a.func,

  /**
   * Transition onEntering callback when animation is not `false`
   */
  onEntering: prop_types_default.a.func,

  /**
   * Transition onEntered callback when animation is not `false`
   */
  onEntered: prop_types_default.a.func,

  /**
   * Transition onExit callback when animation is not `false`
   */
  onExit: prop_types_default.a.func,

  /**
   * Transition onExiting callback when animation is not `false`
   */
  onExiting: prop_types_default.a.func,

  /**
   * Transition onExited callback when animation is not `false`
   */
  onExited: prop_types_default.a.func,

  /**
   * Wait until the first "enter" transition to mount the tab (add it to the DOM)
   */
  mountOnEnter: prop_types_default.a.bool,

  /**
   * Unmount the tab (remove it from the DOM) when it is no longer visible
   */
  unmountOnExit: prop_types_default.a.bool,

  /** @ignore * */
  id: prop_types_default.a.string,

  /** @ignore * */
  'aria-labelledby': prop_types_default.a.string
};

function useTabContext(props) {
  var context = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(src_TabContext);
  if (!context) return props;

  var activeKey = context.activeKey,
      getControlledId = context.getControlledId,
      getControllerId = context.getControllerId,
      rest = _objectWithoutPropertiesLoose(context, ["activeKey", "getControlledId", "getControllerId"]);

  var shouldTransition = props.transition !== false && rest.transition !== false;
  var key = makeEventKey(props.eventKey);
  return _extends({}, props, {
    active: props.active == null && key != null ? makeEventKey(activeKey) === key : props.active,
    id: getControlledId(props.eventKey),
    'aria-labelledby': getControllerId(props.eventKey),
    transition: shouldTransition && (props.transition || rest.transition || src_Fade),
    mountOnEnter: props.mountOnEnter != null ? props.mountOnEnter : rest.mountOnEnter,
    unmountOnExit: props.unmountOnExit != null ? props.unmountOnExit : rest.unmountOnExit
  });
}

var TabPane = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (props, ref) {
  var _useTabContext = useTabContext(props),
      bsPrefix = _useTabContext.bsPrefix,
      className = _useTabContext.className,
      active = _useTabContext.active,
      onEnter = _useTabContext.onEnter,
      onEntering = _useTabContext.onEntering,
      onEntered = _useTabContext.onEntered,
      onExit = _useTabContext.onExit,
      onExiting = _useTabContext.onExiting,
      onExited = _useTabContext.onExited,
      mountOnEnter = _useTabContext.mountOnEnter,
      unmountOnExit = _useTabContext.unmountOnExit,
      Transition = _useTabContext.transition,
      _useTabContext$as = _useTabContext.as,
      Component = _useTabContext$as === void 0 ? 'div' : _useTabContext$as,
      _ = _useTabContext.eventKey,
      rest = _objectWithoutPropertiesLoose(_useTabContext, ["bsPrefix", "className", "active", "onEnter", "onEntering", "onEntered", "onExit", "onExiting", "onExited", "mountOnEnter", "unmountOnExit", "transition", "as", "eventKey"]);

  var prefix = useBootstrapPrefix(bsPrefix, 'tab-pane');
  if (!active && unmountOnExit) return null;
  var pane = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Component, _extends({}, rest, {
    ref: ref,
    role: "tabpanel",
    "aria-hidden": !active,
    className: classnames_default()(className, prefix, {
      active: active
    }),
    __source: {
      fileName: TabPane_jsxFileName,
      lineNumber: 142
    },
    __self: this
  }));
  if (Transition) pane = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Transition, {
    in: active,
    onEnter: onEnter,
    onEntering: onEntering,
    onEntered: onEntered,
    onExit: onExit,
    onExiting: onExiting,
    onExited: onExited,
    mountOnEnter: mountOnEnter,
    unmountOnExit: unmountOnExit,
    __source: {
      fileName: TabPane_jsxFileName,
      lineNumber: 153
    },
    __self: this
  }, pane); // We provide an empty the TabContext so `<Nav>`s in `<TabPane>`s don't
  // conflict with the top level one.

  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_TabContext.Provider, {
    value: null,
    __source: {
      fileName: TabPane_jsxFileName,
      lineNumber: 171
    },
    __self: this
  }, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_SelectableContext.Provider, {
    value: null,
    __source: {
      fileName: TabPane_jsxFileName,
      lineNumber: 172
    },
    __self: this
  }, pane));
});
TabPane.displayName = 'TabPane';
TabPane.propTypes = TabPane_propTypes;
/* harmony default export */ var src_TabPane = (TabPane);
// CONCATENATED MODULE: ./src/Tab.js






/* eslint-disable react/require-render-return, react/no-unused-prop-types */

var Tab_Tab =
/*#__PURE__*/
function (_React$Component) {
  _inheritsLoose(Tab, _React$Component);

  function Tab() {
    return _React$Component.apply(this, arguments) || this;
  }

  var _proto = Tab.prototype;

  _proto.render = function render() {
    throw new Error('ReactBootstrap: The `Tab` component is not meant to be rendered! ' + "It's an abstract component that is only valid as a direct Child of the `Tabs` Component. " + 'For custom tabs components use TabPane and TabsContainer directly');
  };

  return Tab;
}(external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.Component);

Tab_Tab.propTypes = {
  title: prop_types_default.a.node.isRequired
};
Tab_Tab.Container = src_TabContainer;
Tab_Tab.Content = src_TabContent;
Tab_Tab.Pane = src_TabPane;
/* harmony default export */ var src_Tab = (Tab_Tab);
// CONCATENATED MODULE: ./src/Table.js


var Table_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Table.js";




var Table_propTypes = {
  /**
   * @default 'table'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * Adds zebra-striping to any table row within the `<tbody>`.
   */
  striped: prop_types_default.a.bool,

  /**
   * Adds borders on all sides of the table and cells.
   */
  bordered: prop_types_default.a.bool,

  /**
   * Removes all borders on the table and cells, including table header.
   */
  borderless: prop_types_default.a.bool,

  /**
   * Enable a hover state on table rows within a `<tbody>`.
   */
  hover: prop_types_default.a.bool,

  /**
   * Make tables more compact by cutting cell padding in half by setting
   * size as `sm`.
   */
  size: prop_types_default.a.string,

  /**
   * Invert the colors of the table — with light text on dark backgrounds
   * by setting variant as `dark`.
   */
  variant: prop_types_default.a.string,

  /**
   * Responsive tables allow tables to be scrolled horizontally with ease.
   * Across every breakpoint, use `responsive` for horizontally
   * scrolling tables. Responsive tables are wrapped automatically in a `div`.
   * Use `responsive="sm"`, `responsive="md"`, `responsive="lg"`, or
   * `responsive="xl"` as needed to create responsive tables up to
   * a particular breakpoint. From that breakpoint and up, the table will
   * behave normally and not scroll horizontally.
   */
  responsive: prop_types_default.a.oneOfType([prop_types_default.a.bool, prop_types_default.a.string])
};
var Table = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      className = _ref.className,
      striped = _ref.striped,
      bordered = _ref.bordered,
      borderless = _ref.borderless,
      hover = _ref.hover,
      size = _ref.size,
      variant = _ref.variant,
      responsive = _ref.responsive,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "className", "striped", "bordered", "borderless", "hover", "size", "variant", "responsive"]);

  var decoratedBsPrefix = useBootstrapPrefix(bsPrefix, 'table');
  var classes = classnames_default()(className, decoratedBsPrefix, variant && decoratedBsPrefix + "-" + variant, size && decoratedBsPrefix + "-" + size, striped && decoratedBsPrefix + "-striped", bordered && decoratedBsPrefix + "-bordered", borderless && decoratedBsPrefix + "-borderless", hover && decoratedBsPrefix + "-hover");
  var table = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("table", _extends({}, props, {
    className: classes,
    ref: ref,
    __source: {
      fileName: Table_jsxFileName,
      lineNumber: 84
    },
    __self: this
  }));

  if (responsive) {
    var responsiveClass = decoratedBsPrefix + "-responsive";

    if (typeof responsive === 'string') {
      responsiveClass = responsiveClass + "-" + responsive;
    }

    return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", {
      className: responsiveClass,
      __source: {
        fileName: Table_jsxFileName,
        lineNumber: 91
      },
      __self: this
    }, table);
  }

  return table;
});
Table.propTypes = Table_propTypes;
/* harmony default export */ var src_Table = (Table);
// CONCATENATED MODULE: ./src/Tabs.js


var Tabs_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Tabs.js";











var Tabs_propTypes = {
  /**
   * Mark the Tab with a matching `eventKey` as active.
   *
   * @controllable onSelect
   */
  activeKey: prop_types_default.a.any,

  /**
   * Navigation style
   *
   * @type {('tabs'| 'pills')}
   */
  variant: prop_types_default.a.string,

  /**
   * Sets a default animation strategy for all children `<TabPane>`s. Use
   * `false` to disable, `true` to enable the default `<Fade>` animation or
   * a react-transition-group v2 `<Transition/>` component.
   *
   * @type {Transition | false}
   * @default {Fade}
   */
  transition: prop_types_default.a.oneOfType([prop_types_default.a.oneOf([false]), prop_types_default.a.elementType]),

  /**
   * HTML id attribute, required if no `generateChildId` prop
   * is specified.
   *
   * @type {string}
   */
  id: isRequiredForA11y_default()(prop_types_default.a.string),

  /**
   * Callback fired when a Tab is selected.
   *
   * ```js
   * function (
   *   Any eventKey,
   *   SyntheticEvent event?
   * )
   * ```
   *
   * @controllable activeKey
   */
  onSelect: prop_types_default.a.func,

  /**
   * Wait until the first "enter" transition to mount tabs (add them to the DOM)
   */
  mountOnEnter: prop_types_default.a.bool,

  /**
   * Unmount tabs (remove it from the DOM) when it is no longer visible
   */
  unmountOnExit: prop_types_default.a.bool
};
var Tabs_defaultProps = {
  variant: 'tabs',
  mountOnEnter: false,
  unmountOnExit: false
};

function getDefaultActiveKey(children) {
  var defaultActiveKey;
  forEach(children, function (child) {
    if (defaultActiveKey == null) {
      defaultActiveKey = child.props.eventKey;
    }
  });
  return defaultActiveKey;
}

function renderTab(child) {
  var _child$props = child.props,
      title = _child$props.title,
      eventKey = _child$props.eventKey,
      disabled = _child$props.disabled,
      tabClassName = _child$props.tabClassName;

  if (title == null) {
    return null;
  }

  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_NavItem, {
    as: src_NavLink,
    eventKey: eventKey,
    disabled: disabled,
    className: tabClassName,
    __source: {
      fileName: Tabs_jsxFileName,
      lineNumber: 101
    },
    __self: this
  }, title);
}

var Tabs = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (props, ref) {
  var _useUncontrolled = useUncontrolled(props, {
    activeKey: 'onSelect'
  }),
      id = _useUncontrolled.id,
      onSelect = _useUncontrolled.onSelect,
      transition = _useUncontrolled.transition,
      mountOnEnter = _useUncontrolled.mountOnEnter,
      unmountOnExit = _useUncontrolled.unmountOnExit,
      children = _useUncontrolled.children,
      _useUncontrolled$acti = _useUncontrolled.activeKey,
      activeKey = _useUncontrolled$acti === void 0 ? getDefaultActiveKey(children) : _useUncontrolled$acti,
      controlledProps = _objectWithoutPropertiesLoose(_useUncontrolled, ["id", "onSelect", "transition", "mountOnEnter", "unmountOnExit", "children", "activeKey"]);

  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_TabContainer, {
    ref: ref,
    id: id,
    activeKey: activeKey,
    onSelect: onSelect,
    transition: transition,
    mountOnEnter: mountOnEnter,
    unmountOnExit: unmountOnExit,
    __source: {
      fileName: Tabs_jsxFileName,
      lineNumber: 127
    },
    __self: this
  }, external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_Nav, _extends({}, controlledProps, {
    role: "tablist",
    as: "nav",
    __source: {
      fileName: Tabs_jsxFileName,
      lineNumber: 136
    },
    __self: this
  }), map(children, renderTab)), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_TabContent, {
    __source: {
      fileName: Tabs_jsxFileName,
      lineNumber: 140
    },
    __self: this
  }, map(children, function (child) {
    var childProps = _extends({}, child.props);

    delete childProps.title;
    delete childProps.disabled;
    delete childProps.tabClassName;
    return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_TabPane, _extends({}, childProps, {
      __source: {
        fileName: Tabs_jsxFileName,
        lineNumber: 148
      },
      __self: this
    }));
  })));
});
Tabs.propTypes = Tabs_propTypes;
Tabs.defaultProps = Tabs_defaultProps;
Tabs.displayName = 'Tabs';
/* harmony default export */ var src_Tabs = (Tabs);
// CONCATENATED MODULE: ./src/ToggleButton.js


var ToggleButton_jsxFileName = "/Users/jquense/src/react-bootstrap/src/ToggleButton.js";





var ToggleButton_noop = function noop() {};

var ToggleButton_propTypes = {
  /**
   * The `<input>` element `type`
   */
  type: prop_types_default.a.oneOf(['checkbox', 'radio']),

  /**
   * The HTML input name, used to group like checkboxes or radio buttons together
   * semantically
   */
  name: prop_types_default.a.string,

  /**
   * The checked state of the input, managed by `<ToggleButtonGroup>` automatically
   */
  checked: prop_types_default.a.bool,

  /**
   * The disabled state of both the label and input
   */
  disabled: prop_types_default.a.bool,

  /**
   * A callback fired when the underlying input element changes. This is passed
   * directly to the `<input>` so shares the same signature as a native `onChange` event.
   */
  onChange: prop_types_default.a.func,

  /**
   * The value of the input, should be unique amongst it's siblings when nested in a
   * `ToggleButtonGroup`.
   */
  value: prop_types_default.a.any.isRequired,

  /**
   * A ref attached to the `<input>` element
   * @type {ReactRef}
   */
  inputRef: prop_types_default.a.any
};
var ToggleButton = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var children = _ref.children,
      name = _ref.name,
      className = _ref.className,
      checked = _ref.checked,
      type = _ref.type,
      onChange = _ref.onChange,
      value = _ref.value,
      disabled = _ref.disabled,
      inputRef = _ref.inputRef,
      props = _objectWithoutPropertiesLoose(_ref, ["children", "name", "className", "checked", "type", "onChange", "value", "disabled", "inputRef"]);

  var _useState = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useState"])(false),
      focused = _useState[0],
      setFocused = _useState[1];

  var handleFocus = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useCallback"])(function (e) {
    if (e.target.tagName === 'INPUT') setFocused(true);
  }, []);
  var handleBlur = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useCallback"])(function (e) {
    if (e.target.tagName === 'INPUT') setFocused(false);
  }, []);
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_Button, _extends({}, props, {
    ref: ref,
    className: classnames_default()(className, focused && 'focus', disabled && 'disabled'),
    type: null,
    active: !!checked,
    as: "label",
    __source: {
      fileName: ToggleButton_jsxFileName,
      lineNumber: 77
    },
    __self: this
  }), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("input", {
    name: name,
    type: type,
    value: value,
    ref: inputRef,
    autoComplete: "off",
    checked: !!checked,
    disabled: !!disabled,
    onFocus: handleFocus,
    onBlur: handleBlur,
    onChange: onChange || ToggleButton_noop,
    __source: {
      fileName: ToggleButton_jsxFileName,
      lineNumber: 89
    },
    __self: this
  }), children);
});
ToggleButton.propTypes = ToggleButton_propTypes;
ToggleButton.displayName = 'ToggleButton';
/* harmony default export */ var src_ToggleButton = (ToggleButton);
// CONCATENATED MODULE: ./src/ToggleButtonGroup.js


var ToggleButtonGroup_jsxFileName = "/Users/jquense/src/react-bootstrap/src/ToggleButtonGroup.js";








var ToggleButtonGroup_propTypes = {
  /**
   * An HTML `<input>` name for each child button.
   *
   * __Required if `type` is set to `'radio'`__
   */
  name: prop_types_default.a.string,

  /**
   * The value, or array of values, of the active (pressed) buttons
   *
   * @controllable onChange
   */
  value: prop_types_default.a.any,

  /**
   * Callback fired when a button is pressed, depending on whether the `type`
   * is `'radio'` or `'checkbox'`, `onChange` will be called with the value or
   * array of active values
   *
   * @controllable values
   */
  onChange: prop_types_default.a.func,

  /**
   * The input `type` of the rendered buttons, determines the toggle behavior
   * of the buttons
   */
  type: prop_types_default.a.oneOf(['checkbox', 'radio']).isRequired
};
var ToggleButtonGroup_defaultProps = {
  type: 'radio'
};
var ToggleButtonGroup = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (props, ref) {
  var _useUncontrolled = useUncontrolled(props, {
    value: 'onChange'
  }),
      children = _useUncontrolled.children,
      type = _useUncontrolled.type,
      name = _useUncontrolled.name,
      value = _useUncontrolled.value,
      onChange = _useUncontrolled.onChange,
      controlledProps = _objectWithoutPropertiesLoose(_useUncontrolled, ["children", "type", "name", "value", "onChange"]);

  var getValues = function getValues() {
    return value == null ? [] : [].concat(value);
  };

  var handleToggle = function handleToggle(inputVal, event) {
    var values = getValues();
    var isActive = values.indexOf(inputVal) !== -1;

    if (type === 'radio') {
      if (!isActive) onChange(inputVal, event);
      return;
    }

    if (isActive) {
      onChange(values.filter(function (n) {
        return n !== inputVal;
      }), event);
    } else {
      onChange([].concat(values, [inputVal]), event);
    }
  };

  !(type !== 'radio' || !!name) ?  false ? undefined : browser_default()(false) : void 0;
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_ButtonGroup, _extends({}, controlledProps, {
    ref: ref,
    toggle: true,
    __source: {
      fileName: ToggleButtonGroup_jsxFileName,
      lineNumber: 86
    },
    __self: this
  }), map(children, function (child) {
    var values = getValues();
    var _child$props = child.props,
        childVal = _child$props.value,
        childOnChange = _child$props.onChange;

    var handler = function handler(e) {
      return handleToggle(childVal, e);
    };

    return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.cloneElement(child, {
      type: type,
      name: child.name || name,
      checked: values.indexOf(childVal) !== -1,
      onChange: src_createChainedFunction(childOnChange, handler)
    });
  }));
});
ToggleButtonGroup.propTypes = ToggleButtonGroup_propTypes;
ToggleButtonGroup.defaultProps = ToggleButtonGroup_defaultProps;
ToggleButtonGroup.Button = src_ToggleButton;
/* harmony default export */ var src_ToggleButtonGroup = (ToggleButtonGroup);
// CONCATENATED MODULE: ./src/Tooltip.js


var Tooltip_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Tooltip.js";





var Tooltip_propTypes = {
  /**
   * @default 'tooltip'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * An html id attribute, necessary for accessibility
   * @type {string|number}
   * @required
   */
  id: isRequiredForA11y_default()(prop_types_default.a.oneOfType([prop_types_default.a.string, prop_types_default.a.number])),

  /**
   * Sets the direction the Tooltip is positioned towards.
   *
   * > This is generally provided by the `Overlay` component positioning the tooltip
   */
  placement: prop_types_default.a.oneOf(['auto-start', 'auto', 'auto-end', 'top-start', 'top', 'top-end', 'right-start', 'right', 'right-end', 'bottom-end', 'bottom', 'bottom-start', 'left-end', 'left', 'left-start']),

  /**
   * An Overlay injected set of props for positioning the tooltip arrow.
   *
   * > This is generally provided by the `Overlay` component positioning the tooltip
   *
   * @type {{ ref: ReactRef, style: Object }}
   */
  arrowProps: prop_types_default.a.shape({
    ref: prop_types_default.a.any,
    style: prop_types_default.a.object
  }),

  /** @private */
  scheduleUpdate: prop_types_default.a.func,

  /** @private */
  outOfBoundaries: prop_types_default.a.any
};
var Tooltip_defaultProps = {
  placement: 'right'
};
var Tooltip = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      placement = _ref.placement,
      className = _ref.className,
      style = _ref.style,
      children = _ref.children,
      arrowProps = _ref.arrowProps,
      _ = _ref.scheduleUpdate,
      _1 = _ref.outOfBoundaries,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "placement", "className", "style", "children", "arrowProps", "scheduleUpdate", "outOfBoundaries"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'tooltip');
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", _extends({
    ref: ref,
    style: style,
    role: "tooltip",
    "x-placement": placement,
    className: classnames_default()(className, bsPrefix, "bs-tooltip-" + placement)
  }, props, {
    __source: {
      fileName: Tooltip_jsxFileName,
      lineNumber: 86
    },
    __self: this
  }), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", _extends({
    className: "arrow"
  }, arrowProps, {
    __source: {
      fileName: Tooltip_jsxFileName,
      lineNumber: 94
    },
    __self: this
  })), external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", {
    className: bsPrefix + "-inner",
    __source: {
      fileName: Tooltip_jsxFileName,
      lineNumber: 95
    },
    __self: this
  }, children));
});
Tooltip.propTypes = Tooltip_propTypes;
Tooltip.defaultProps = Tooltip_defaultProps;
Tooltip.displayName = 'Tooltip';
/* harmony default export */ var src_Tooltip = (Tooltip);
// CONCATENATED MODULE: ./node_modules/@restart/hooks/esm/useUpdatedRef.js

/**
 * Returns a ref that is immediately updated with the new value
 *
 * @param value The Ref value
 */

function useUpdatedRef(value) {
  var valueRef = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useRef"])(value);
  valueRef.current = value;
  return valueRef;
}
// CONCATENATED MODULE: ./node_modules/@restart/hooks/esm/useWillUnmount.js


/**
 * Attach a callback that fires when a component unmounts
 *
 * @param fn Handler to run when the component unmounts
 */

function useWillUnmount(fn) {
  var onUnmount = useUpdatedRef(fn);
  Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useEffect"])(function () {
    return function () {
      return onUnmount.current();
    };
  }, []);
}
// CONCATENATED MODULE: ./node_modules/@restart/hooks/esm/useMounted.js

/**
 * Track whether a component is current mounted. Generally less preferable than
 * properlly canceling effects so they don't run after a component is unmounted,
 * but helpful in cases where that isn't feasible, such as a `Promise` resolution.
 *
 * @returns a function that returns the current isMounted state of the component
 *
 * ```ts
 * const [data, setData] = useState(null)
 * const isMounted = useMounted()
 *
 * useEffect(() => {
 *   fetchdata().then((newData) => {
 *      if (isMounted()) {
 *        setData(newData);
 *      }
 *   })
 * })
 * ```
 */

function useMounted() {
  var mounted = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useRef"])(true);
  var isMounted = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useRef"])(function () {
    return mounted.current;
  });
  Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useEffect"])(function () {
    return function () {
      mounted.current = false;
    };
  }, []);
  return isMounted.current;
}
// CONCATENATED MODULE: ./node_modules/@restart/hooks/esm/useTimeout.js



/**
 * Returns a controller object for setting a timeout that is properly cleaned up
 * once the component unmounts. New timeouts cancel and replace existing ones.
 */

function useTimeout() {
  var isMounted = useMounted();
  var handle = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useRef"])();

  var clear = function clear() {
    return clearTimeout(handle.current);
  };

  useWillUnmount(clear);
  return {
    set: function set(fn, ms) {
      if (!isMounted()) return;
      clear();
      handle.current = setTimeout(fn, ms);
    },
    clear: clear
  };
}
// CONCATENATED MODULE: ./src/ToastContext.js

var ToastContext = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createContext({
  onClose: function onClose() {}
});
/* harmony default export */ var src_ToastContext = (ToastContext);
// CONCATENATED MODULE: ./src/ToastHeader.js


var ToastHeader_jsxFileName = "/Users/jquense/src/react-bootstrap/src/ToastHeader.js";







var ToastHeader_propTypes = {
  bsPrefix: prop_types_default.a.string,

  /**
   * Provides an accessible label for the close
   * button. It is used for Assistive Technology when the label text is not
   * readable.
   */
  closeLabel: prop_types_default.a.string,

  /**
   * Specify whether the Component should contain a close button
   */
  closeButton: prop_types_default.a.bool
};
var ToastHeader_defaultProps = {
  closeLabel: 'Close',
  closeButton: true
};
var ToastHeader = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      closeLabel = _ref.closeLabel,
      closeButton = _ref.closeButton,
      className = _ref.className,
      children = _ref.children,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "closeLabel", "closeButton", "className", "children"]);

  bsPrefix = useBootstrapPrefix(bsPrefix, 'toast-header');
  var context = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useContext"])(src_ToastContext);
  var handleClick = useEventCallback(function () {
    if (context) {
      context.onClose();
    }
  });
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", _extends({
    ref: ref
  }, props, {
    className: classnames_default()(bsPrefix, className),
    __source: {
      fileName: ToastHeader_jsxFileName,
      lineNumber: 47
    },
    __self: this
  }), children, closeButton && external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_CloseButton, {
    label: closeLabel,
    onClick: handleClick,
    className: "ml-2 mb-1",
    "data-dismiss": "toast",
    __source: {
      fileName: ToastHeader_jsxFileName,
      lineNumber: 51
    },
    __self: this
  }));
});
ToastHeader.displayName = 'ToastHeader';
ToastHeader.propTypes = ToastHeader_propTypes;
ToastHeader.defaultProps = ToastHeader_defaultProps;
/* harmony default export */ var src_ToastHeader = (ToastHeader);
// CONCATENATED MODULE: ./src/ToastBody.js

/* harmony default export */ var ToastBody = (createWithBsPrefix('toast-body'));
// CONCATENATED MODULE: ./src/Toast.js


var Toast_jsxFileName = "/Users/jquense/src/react-bootstrap/src/Toast.js";









var Toast_propTypes = {
  /**
   * @default 'toast'
   */
  bsPrefix: prop_types_default.a.string,

  /**
   * Apply a CSS fade transition to the toast
   */
  animation: prop_types_default.a.bool,

  /**
   * Auto hide the toast
   */
  autohide: prop_types_default.a.bool,

  /**
   * Delay hiding the toast (ms)
   */
  delay: prop_types_default.a.number,

  /**
   * A Callback fired when the close button is clicked.
   */
  onClose: prop_types_default.a.func,

  /**
   * When `true` The modal will show itself.
   */
  show: prop_types_default.a.bool,

  /**
   * A `react-transition-group` Transition component used to animate the Toast on dismissal.
   */
  transition: prop_types_default.a.elementType
};
var Toast_defaultProps = {
  animation: true,
  autohide: false,
  delay: 3000,
  show: true,
  transition: src_Fade
};
var Toast = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.forwardRef(function (_ref, ref) {
  var bsPrefix = _ref.bsPrefix,
      className = _ref.className,
      children = _ref.children,
      Transition = _ref.transition,
      show = _ref.show,
      animation = _ref.animation,
      delay = _ref.delay,
      autohide = _ref.autohide,
      onClose = _ref.onClose,
      props = _objectWithoutPropertiesLoose(_ref, ["bsPrefix", "className", "children", "transition", "show", "animation", "delay", "autohide", "onClose"]);

  bsPrefix = useBootstrapPrefix('toast');
  var delayRef = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useRef"])(delay);
  var onCloseRef = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useRef"])(onClose);
  Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useEffect"])(function () {
    // We use refs for these, because we don't want to restart the autohide
    // timer in case these values change.
    delayRef.current = delay;
    onCloseRef.current = onClose;
  }, [delay, onClose]);
  var autohideTimeout = useTimeout();
  var autohideFunc = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useCallback"])(function () {
    if (!(autohide && show)) {
      return;
    }

    onCloseRef.current();
  }, [autohide, show]);
  autohideTimeout.set(autohideFunc, delayRef.current);
  var useAnimation = Object(external_root_React_commonjs2_react_commonjs_react_amd_react_["useMemo"])(function () {
    return Transition && animation;
  }, [Transition, animation]);
  var toast = external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement("div", _extends({}, props, {
    ref: ref,
    className: classnames_default()(bsPrefix, className, !useAnimation && show && 'show'),
    role: "alert",
    "aria-live": "assertive",
    "aria-atomic": "true",
    __source: {
      fileName: Toast_jsxFileName,
      lineNumber: 100
    },
    __self: this
  }), children);
  var toastContext = {
    onClose: onClose
  };
  return external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(src_ToastContext.Provider, {
    value: toastContext,
    __source: {
      fileName: Toast_jsxFileName,
      lineNumber: 121
    },
    __self: this
  }, useAnimation ? external_root_React_commonjs2_react_commonjs_react_amd_react_default.a.createElement(Transition, {
    in: show,
    __source: {
      fileName: Toast_jsxFileName,
      lineNumber: 122
    },
    __self: this
  }, toast) : toast);
});
Toast.propTypes = Toast_propTypes;
Toast.defaultProps = Toast_defaultProps;
Toast.displayName = 'Toast';
Toast.Body = ToastBody;
Toast.Header = src_ToastHeader;
/* harmony default export */ var src_Toast = (Toast);
// CONCATENATED MODULE: ./src/index.js
/* concated harmony reexport Accordion */__webpack_require__.d(__webpack_exports__, "Accordion", function() { return src_Accordion; });
/* concated harmony reexport AccordionToggle */__webpack_require__.d(__webpack_exports__, "AccordionToggle", function() { return src_AccordionToggle; });
/* concated harmony reexport useAccordionToggle */__webpack_require__.d(__webpack_exports__, "useAccordionToggle", function() { return useAccordionToggle; });
/* concated harmony reexport AccordionCollapse */__webpack_require__.d(__webpack_exports__, "AccordionCollapse", function() { return src_AccordionCollapse; });
/* concated harmony reexport Alert */__webpack_require__.d(__webpack_exports__, "Alert", function() { return src_Alert; });
/* concated harmony reexport Badge */__webpack_require__.d(__webpack_exports__, "Badge", function() { return src_Badge; });
/* concated harmony reexport Breadcrumb */__webpack_require__.d(__webpack_exports__, "Breadcrumb", function() { return src_Breadcrumb; });
/* concated harmony reexport BreadcrumbItem */__webpack_require__.d(__webpack_exports__, "BreadcrumbItem", function() { return src_BreadcrumbItem; });
/* concated harmony reexport Button */__webpack_require__.d(__webpack_exports__, "Button", function() { return src_Button; });
/* concated harmony reexport ButtonGroup */__webpack_require__.d(__webpack_exports__, "ButtonGroup", function() { return src_ButtonGroup; });
/* concated harmony reexport ButtonToolbar */__webpack_require__.d(__webpack_exports__, "ButtonToolbar", function() { return src_ButtonToolbar; });
/* concated harmony reexport Card */__webpack_require__.d(__webpack_exports__, "Card", function() { return src_Card; });
/* concated harmony reexport CardColumns */__webpack_require__.d(__webpack_exports__, "CardColumns", function() { return CardColumns; });
/* concated harmony reexport CardDeck */__webpack_require__.d(__webpack_exports__, "CardDeck", function() { return CardDeck; });
/* concated harmony reexport CardImg */__webpack_require__.d(__webpack_exports__, "CardImg", function() { return src_CardImg; });
/* concated harmony reexport CardGroup */__webpack_require__.d(__webpack_exports__, "CardGroup", function() { return CardGroup; });
/* concated harmony reexport Carousel */__webpack_require__.d(__webpack_exports__, "Carousel", function() { return src_Carousel; });
/* concated harmony reexport CarouselItem */__webpack_require__.d(__webpack_exports__, "CarouselItem", function() { return CarouselItem; });
/* concated harmony reexport CloseButton */__webpack_require__.d(__webpack_exports__, "CloseButton", function() { return src_CloseButton; });
/* concated harmony reexport Col */__webpack_require__.d(__webpack_exports__, "Col", function() { return src_Col; });
/* concated harmony reexport Collapse */__webpack_require__.d(__webpack_exports__, "Collapse", function() { return src_Collapse; });
/* concated harmony reexport Dropdown */__webpack_require__.d(__webpack_exports__, "Dropdown", function() { return src_Dropdown; });
/* concated harmony reexport DropdownButton */__webpack_require__.d(__webpack_exports__, "DropdownButton", function() { return src_DropdownButton; });
/* concated harmony reexport DropdownItem */__webpack_require__.d(__webpack_exports__, "DropdownItem", function() { return src_DropdownItem; });
/* concated harmony reexport Fade */__webpack_require__.d(__webpack_exports__, "Fade", function() { return src_Fade; });
/* concated harmony reexport Form */__webpack_require__.d(__webpack_exports__, "Form", function() { return src_Form; });
/* concated harmony reexport FormControl */__webpack_require__.d(__webpack_exports__, "FormControl", function() { return src_FormControl; });
/* concated harmony reexport FormCheck */__webpack_require__.d(__webpack_exports__, "FormCheck", function() { return src_FormCheck; });
/* concated harmony reexport Switch */__webpack_require__.d(__webpack_exports__, "Switch", function() { return src_Switch; });
/* concated harmony reexport FormGroup */__webpack_require__.d(__webpack_exports__, "FormGroup", function() { return src_FormGroup; });
/* concated harmony reexport FormLabel */__webpack_require__.d(__webpack_exports__, "FormLabel", function() { return src_FormLabel; });
/* concated harmony reexport FormText */__webpack_require__.d(__webpack_exports__, "FormText", function() { return src_FormText; });
/* concated harmony reexport Container */__webpack_require__.d(__webpack_exports__, "Container", function() { return src_Container; });
/* concated harmony reexport Image */__webpack_require__.d(__webpack_exports__, "Image", function() { return src_Image; });
/* concated harmony reexport Figure */__webpack_require__.d(__webpack_exports__, "Figure", function() { return src_Figure; });
/* concated harmony reexport InputGroup */__webpack_require__.d(__webpack_exports__, "InputGroup", function() { return src_InputGroup; });
/* concated harmony reexport Jumbotron */__webpack_require__.d(__webpack_exports__, "Jumbotron", function() { return src_Jumbotron; });
/* concated harmony reexport ListGroup */__webpack_require__.d(__webpack_exports__, "ListGroup", function() { return src_ListGroup; });
/* concated harmony reexport ListGroupItem */__webpack_require__.d(__webpack_exports__, "ListGroupItem", function() { return src_ListGroupItem; });
/* concated harmony reexport Media */__webpack_require__.d(__webpack_exports__, "Media", function() { return src_Media; });
/* concated harmony reexport Modal */__webpack_require__.d(__webpack_exports__, "Modal", function() { return src_Modal; });
/* concated harmony reexport ModalBody */__webpack_require__.d(__webpack_exports__, "ModalBody", function() { return ModalBody; });
/* concated harmony reexport ModalDialog */__webpack_require__.d(__webpack_exports__, "ModalDialog", function() { return src_ModalDialog; });
/* concated harmony reexport ModalFooter */__webpack_require__.d(__webpack_exports__, "ModalFooter", function() { return ModalFooter; });
/* concated harmony reexport ModalTitle */__webpack_require__.d(__webpack_exports__, "ModalTitle", function() { return ModalTitle; });
/* concated harmony reexport Nav */__webpack_require__.d(__webpack_exports__, "Nav", function() { return src_Nav; });
/* concated harmony reexport Navbar */__webpack_require__.d(__webpack_exports__, "Navbar", function() { return src_Navbar; });
/* concated harmony reexport NavbarBrand */__webpack_require__.d(__webpack_exports__, "NavbarBrand", function() { return src_NavbarBrand; });
/* concated harmony reexport NavDropdown */__webpack_require__.d(__webpack_exports__, "NavDropdown", function() { return src_NavDropdown; });
/* concated harmony reexport NavItem */__webpack_require__.d(__webpack_exports__, "NavItem", function() { return src_NavItem; });
/* concated harmony reexport Overlay */__webpack_require__.d(__webpack_exports__, "Overlay", function() { return src_Overlay; });
/* concated harmony reexport OverlayTrigger */__webpack_require__.d(__webpack_exports__, "OverlayTrigger", function() { return src_OverlayTrigger; });
/* concated harmony reexport PageItem */__webpack_require__.d(__webpack_exports__, "PageItem", function() { return src_PageItem; });
/* concated harmony reexport Pagination */__webpack_require__.d(__webpack_exports__, "Pagination", function() { return src_Pagination; });
/* concated harmony reexport Popover */__webpack_require__.d(__webpack_exports__, "Popover", function() { return src_Popover; });
/* concated harmony reexport PopoverContent */__webpack_require__.d(__webpack_exports__, "PopoverContent", function() { return src_PopoverContent; });
/* concated harmony reexport PopoverTitle */__webpack_require__.d(__webpack_exports__, "PopoverTitle", function() { return src_PopoverTitle; });
/* concated harmony reexport ProgressBar */__webpack_require__.d(__webpack_exports__, "ProgressBar", function() { return src_ProgressBar; });
/* concated harmony reexport ResponsiveEmbed */__webpack_require__.d(__webpack_exports__, "ResponsiveEmbed", function() { return src_ResponsiveEmbed; });
/* concated harmony reexport Row */__webpack_require__.d(__webpack_exports__, "Row", function() { return src_Row; });
/* concated harmony reexport SafeAnchor */__webpack_require__.d(__webpack_exports__, "SafeAnchor", function() { return src_SafeAnchor; });
/* concated harmony reexport Spinner */__webpack_require__.d(__webpack_exports__, "Spinner", function() { return src_Spinner; });
/* concated harmony reexport SplitButton */__webpack_require__.d(__webpack_exports__, "SplitButton", function() { return src_SplitButton; });
/* concated harmony reexport Tab */__webpack_require__.d(__webpack_exports__, "Tab", function() { return src_Tab; });
/* concated harmony reexport TabContainer */__webpack_require__.d(__webpack_exports__, "TabContainer", function() { return src_TabContainer; });
/* concated harmony reexport TabContent */__webpack_require__.d(__webpack_exports__, "TabContent", function() { return src_TabContent; });
/* concated harmony reexport Table */__webpack_require__.d(__webpack_exports__, "Table", function() { return src_Table; });
/* concated harmony reexport TabPane */__webpack_require__.d(__webpack_exports__, "TabPane", function() { return src_TabPane; });
/* concated harmony reexport Tabs */__webpack_require__.d(__webpack_exports__, "Tabs", function() { return src_Tabs; });
/* concated harmony reexport ThemeProvider */__webpack_require__.d(__webpack_exports__, "ThemeProvider", function() { return src_ThemeProvider; });
/* concated harmony reexport ToggleButton */__webpack_require__.d(__webpack_exports__, "ToggleButton", function() { return src_ToggleButton; });
/* concated harmony reexport ToggleButtonGroup */__webpack_require__.d(__webpack_exports__, "ToggleButtonGroup", function() { return src_ToggleButtonGroup; });
/* concated harmony reexport Tooltip */__webpack_require__.d(__webpack_exports__, "Tooltip", function() { return src_Tooltip; });
/* concated harmony reexport Toast */__webpack_require__.d(__webpack_exports__, "Toast", function() { return src_Toast; });
/* concated harmony reexport ToastBody */__webpack_require__.d(__webpack_exports__, "ToastBody", function() { return ToastBody; });
/* concated harmony reexport ToastHeader */__webpack_require__.d(__webpack_exports__, "ToastHeader", function() { return src_ToastHeader; });
























































































































































/***/ })
/******/ ]);
});