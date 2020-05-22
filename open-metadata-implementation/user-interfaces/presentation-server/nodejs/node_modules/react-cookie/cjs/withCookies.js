"use strict";

function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = withCookies;

var React = _interopRequireWildcard(require("react"));

var _CookiesContext = require("./CookiesContext");

function _getRequireWildcardCache() { if (typeof WeakMap !== "function") return null; var cache = new WeakMap(); _getRequireWildcardCache = function _getRequireWildcardCache() { return cache; }; return cache; }

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } if (obj === null || _typeof(obj) !== "object" && typeof obj !== "function") { return { "default": obj }; } var cache = _getRequireWildcardCache(); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj["default"] = obj; if (cache) { cache.set(obj, newObj); } return newObj; }

var __extends = void 0 && (void 0).__extends || function () {
  var _extendStatics = function extendStatics(d, b) {
    _extendStatics = Object.setPrototypeOf || {
      __proto__: []
    } instanceof Array && function (d, b) {
      d.__proto__ = b;
    } || function (d, b) {
      for (var p in b) {
        if (b.hasOwnProperty(p)) d[p] = b[p];
      }
    };

    return _extendStatics(d, b);
  };

  return function (d, b) {
    _extendStatics(d, b);

    function __() {
      this.constructor = d;
    }

    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
  };
}();

var __assign = void 0 && (void 0).__assign || function () {
  __assign = Object.assign || function (t) {
    for (var s, i = 1, n = arguments.length; i < n; i++) {
      s = arguments[i];

      for (var p in s) {
        if (Object.prototype.hasOwnProperty.call(s, p)) t[p] = s[p];
      }
    }

    return t;
  };

  return __assign.apply(this, arguments);
};

var __rest = void 0 && (void 0).__rest || function (s, e) {
  var t = {};

  for (var p in s) {
    if (Object.prototype.hasOwnProperty.call(s, p) && e.indexOf(p) < 0) t[p] = s[p];
  }

  if (s != null && typeof Object.getOwnPropertySymbols === "function") for (var i = 0, p = Object.getOwnPropertySymbols(s); i < p.length; i++) {
    if (e.indexOf(p[i]) < 0 && Object.prototype.propertyIsEnumerable.call(s, p[i])) t[p[i]] = s[p[i]];
  }
  return t;
};

// Only way to make function modules work with both TypeScript and Rollup
var hoistStatics = require('hoist-non-react-statics');

function withCookies(WrappedComponent) {
  // @ts-ignore
  var name = WrappedComponent.displayName || WrappedComponent.name;

  var CookieWrapper =
  /** @class */
  function (_super) {
    __extends(CookieWrapper, _super);

    function CookieWrapper() {
      var _this = _super !== null && _super.apply(this, arguments) || this;

      _this.onChange = function () {
        // Make sure to update children with new values
        _this.forceUpdate();
      };

      return _this;
    }

    CookieWrapper.prototype.listen = function () {
      this.props.cookies.addChangeListener(this.onChange);
    };

    CookieWrapper.prototype.unlisten = function (cookies) {
      (cookies || this.props.cookies).removeChangeListener(this.onChange);
    };

    CookieWrapper.prototype.componentDidMount = function () {
      this.listen();
    };

    CookieWrapper.prototype.componentDidUpdate = function (prevProps) {
      if (prevProps.cookies !== this.props.cookies) {
        this.unlisten(prevProps.cookies);
        this.listen();
      }
    };

    CookieWrapper.prototype.componentWillUnmount = function () {
      this.unlisten();
    };

    CookieWrapper.prototype.render = function () {
      var _a = this.props,
          forwardedRef = _a.forwardedRef,
          cookies = _a.cookies,
          restProps = __rest(_a, ["forwardedRef", "cookies"]);

      var allCookies = cookies.getAll();
      return React.createElement(WrappedComponent, __assign({}, restProps, {
        ref: forwardedRef,
        cookies: cookies,
        allCookies: allCookies
      }));
    };

    CookieWrapper.displayName = "withCookies(" + name + ")";
    CookieWrapper.WrappedComponent = WrappedComponent;
    return CookieWrapper;
  }(React.Component);

  var ForwardedComponent = React.forwardRef(function (props, ref) {
    return React.createElement(_CookiesContext.Consumer, null, function (cookies) {
      return React.createElement(CookieWrapper, __assign({
        cookies: cookies
      }, props, {
        forwardedRef: ref
      }));
    });
  });
  ForwardedComponent.displayName = CookieWrapper.displayName;
  ForwardedComponent.WrappedComponent = CookieWrapper.WrappedComponent;
  return hoistStatics(ForwardedComponent, WrappedComponent);
}

module.exports = exports.default;