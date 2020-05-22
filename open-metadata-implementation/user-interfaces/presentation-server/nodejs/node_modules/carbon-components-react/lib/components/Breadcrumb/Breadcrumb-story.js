"use strict";

var _react = _interopRequireDefault(require("react"));

var _react2 = require("@storybook/react");

var _addonActions = require("@storybook/addon-actions");

var _addonKnobs = require("@storybook/addon-knobs");

var _Breadcrumb = require("../Breadcrumb");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

var props = function props() {
  return {
    className: 'some-class',
    noTrailingSlash: (0, _addonKnobs.boolean)('No trailing slash (noTrailingSlash)', false),
    onClick: (0, _addonActions.action)('onClick')
  };
};

(0, _react2.storiesOf)('Breadcrumb', module).addDecorator(_addonKnobs.withKnobs).add('default', function () {
  return _react.default.createElement(_Breadcrumb.Breadcrumb, props(), _react.default.createElement(_Breadcrumb.BreadcrumbItem, null, _react.default.createElement("a", {
    href: "/#"
  }, "Breadcrumb 1")), _react.default.createElement(_Breadcrumb.BreadcrumbItem, {
    href: "#"
  }, "Breadcrumb 2"), _react.default.createElement(_Breadcrumb.BreadcrumbItem, {
    href: "#"
  }, "Breadcrumb 3"));
}, {
  info: {
    text: "\n          Breadcrumb enables users to quickly see their location within a path of navigation and move up to a parent level if desired.\n        "
  }
}).add('skeleton', function () {
  return _react.default.createElement(_Breadcrumb.BreadcrumbSkeleton, null);
}, {
  info: {
    text: "\n          Placeholder skeleton state to use when content is loading.\n          "
  }
}).add('current page', function () {
  return _react.default.createElement(_Breadcrumb.Breadcrumb, _extends({}, props(), {
    noTrailingSlash: true
  }), _react.default.createElement(_Breadcrumb.BreadcrumbItem, null, _react.default.createElement("a", {
    href: "/#"
  }, "Breadcrumb 1")), _react.default.createElement(_Breadcrumb.BreadcrumbItem, {
    href: "#"
  }, "Breadcrumb 2"), _react.default.createElement(_Breadcrumb.BreadcrumbItem, {
    href: "#",
    isCurrentPage: true
  }, "Breadcrumb 3"));
}, {
  info: {
    text: 'You can specify a BreadcrumbItem component as the current page with the `isCurrentPage` prop'
  }
}).add('current page with aria-current', function () {
  return _react.default.createElement(_Breadcrumb.Breadcrumb, _extends({}, props(), {
    noTrailingSlash: true
  }), _react.default.createElement(_Breadcrumb.BreadcrumbItem, null, _react.default.createElement("a", {
    href: "/#"
  }, "Breadcrumb 1")), _react.default.createElement(_Breadcrumb.BreadcrumbItem, {
    href: "#"
  }, "Breadcrumb 2"), _react.default.createElement(_Breadcrumb.BreadcrumbItem, {
    href: "#",
    "aria-current": "page"
  }, "Breadcrumb 3"));
}, {
  info: {
    text: 'You can specify a BreadcrumbItem component as the current page with the `aria-current` prop by specifying `aria-current="page"`'
  }
});