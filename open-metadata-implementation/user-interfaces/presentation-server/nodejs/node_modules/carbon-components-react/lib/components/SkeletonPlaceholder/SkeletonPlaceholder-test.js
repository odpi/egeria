"use strict";

var _react = _interopRequireDefault(require("react"));

var _SkeletonPlaceholder = _interopRequireDefault(require("../SkeletonPlaceholder"));

var _enzyme = require("enzyme");

var _carbonComponents = require("carbon-components");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
var prefix = _carbonComponents.settings.prefix;
describe('SkeletonPlaceholder', function () {
  var wrapper = (0, _enzyme.shallow)(_react.default.createElement(_SkeletonPlaceholder.default, null));
  it('Has the expected classes', function () {
    expect(wrapper.hasClass("".concat(prefix, "--skeleton__placeholder"))).toEqual(true);
  });
});