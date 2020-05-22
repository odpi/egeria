"use strict";

var _react = _interopRequireDefault(require("react"));

var _enzyme = require("enzyme");

var _FormLabel = _interopRequireDefault(require("../FormLabel"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
describe('FormLabel', function () {
  it('should render', function () {
    var wrapper = (0, _enzyme.shallow)(_react.default.createElement(_FormLabel.default, null));
    expect(wrapper).toMatchSnapshot();
  });
});