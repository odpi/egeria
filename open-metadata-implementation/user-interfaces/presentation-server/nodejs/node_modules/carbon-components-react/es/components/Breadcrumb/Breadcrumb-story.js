function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

/* eslint-disable no-console */
import React from 'react';
import { storiesOf } from '@storybook/react';
import { action } from '@storybook/addon-actions';
import { withKnobs, boolean } from '@storybook/addon-knobs';
import { Breadcrumb, BreadcrumbItem, BreadcrumbSkeleton } from '../Breadcrumb';

var props = function props() {
  return {
    className: 'some-class',
    noTrailingSlash: boolean('No trailing slash (noTrailingSlash)', false),
    onClick: action('onClick')
  };
};

storiesOf('Breadcrumb', module).addDecorator(withKnobs).add('default', function () {
  return React.createElement(Breadcrumb, props(), React.createElement(BreadcrumbItem, null, React.createElement("a", {
    href: "/#"
  }, "Breadcrumb 1")), React.createElement(BreadcrumbItem, {
    href: "#"
  }, "Breadcrumb 2"), React.createElement(BreadcrumbItem, {
    href: "#"
  }, "Breadcrumb 3"));
}, {
  info: {
    text: "\n          Breadcrumb enables users to quickly see their location within a path of navigation and move up to a parent level if desired.\n        "
  }
}).add('skeleton', function () {
  return React.createElement(BreadcrumbSkeleton, null);
}, {
  info: {
    text: "\n          Placeholder skeleton state to use when content is loading.\n          "
  }
}).add('current page', function () {
  return React.createElement(Breadcrumb, _extends({}, props(), {
    noTrailingSlash: true
  }), React.createElement(BreadcrumbItem, null, React.createElement("a", {
    href: "/#"
  }, "Breadcrumb 1")), React.createElement(BreadcrumbItem, {
    href: "#"
  }, "Breadcrumb 2"), React.createElement(BreadcrumbItem, {
    href: "#",
    isCurrentPage: true
  }, "Breadcrumb 3"));
}, {
  info: {
    text: 'You can specify a BreadcrumbItem component as the current page with the `isCurrentPage` prop'
  }
}).add('current page with aria-current', function () {
  return React.createElement(Breadcrumb, _extends({}, props(), {
    noTrailingSlash: true
  }), React.createElement(BreadcrumbItem, null, React.createElement("a", {
    href: "/#"
  }, "Breadcrumb 1")), React.createElement(BreadcrumbItem, {
    href: "#"
  }, "Breadcrumb 2"), React.createElement(BreadcrumbItem, {
    href: "#",
    "aria-current": "page"
  }, "Breadcrumb 3"));
}, {
  info: {
    text: 'You can specify a BreadcrumbItem component as the current page with the `aria-current` prop by specifying `aria-current="page"`'
  }
});