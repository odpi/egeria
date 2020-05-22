"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = useCookies;

var _react = require("react");

var _CookiesContext = _interopRequireDefault(require("./CookiesContext"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

function useCookies(dependencies) {
  var cookies = (0, _react.useContext)(_CookiesContext["default"]);

  if (!cookies) {
    throw new Error('Missing <CookiesProvider>');
  }

  var initialCookies = cookies.getAll();

  var _a = (0, _react.useState)(initialCookies),
      allCookies = _a[0],
      setCookies = _a[1];

  var previousCookiesRef = (0, _react.useRef)(allCookies);
  (0, _react.useEffect)(function () {
    function onChange() {
      var newCookies = cookies.getAll();

      if (shouldUpdate(dependencies || null, newCookies, previousCookiesRef.current)) {
        setCookies(newCookies);
      }

      previousCookiesRef.current = newCookies;
    }

    cookies.addChangeListener(onChange);
    return function () {
      cookies.removeChangeListener(onChange);
    };
  }, [cookies]);
  var setCookie = (0, _react.useMemo)(function () {
    return cookies.set.bind(cookies);
  }, [cookies]);
  var removeCookie = (0, _react.useMemo)(function () {
    return cookies.remove.bind(cookies);
  }, [cookies]);
  return [allCookies, setCookie, removeCookie];
}

function shouldUpdate(dependencies, newCookies, oldCookies) {
  if (!dependencies) {
    return true;
  }

  for (var _i = 0, dependencies_1 = dependencies; _i < dependencies_1.length; _i++) {
    var dependency = dependencies_1[_i];

    if (newCookies[dependency] !== oldCookies[dependency]) {
      return true;
    }
  }

  return false;
}

module.exports = exports.default;