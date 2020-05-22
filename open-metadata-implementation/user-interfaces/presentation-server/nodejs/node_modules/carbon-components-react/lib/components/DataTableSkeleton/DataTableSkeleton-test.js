"use strict";

var _react = _interopRequireDefault(require("react"));

var _DataTableSkeleton = _interopRequireDefault(require("../DataTableSkeleton/"));

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
describe('DataTableSkeleton', function () {
  describe('Renders as expected', function () {
    var rowCount = 20;
    var columnCount = 3;
    var headers = ['Name', 'Protocol', 'Port'];
    var wrapper = (0, _enzyme.shallow)(_react.default.createElement(_DataTableSkeleton.default, {
      compact: true,
      rowCount: rowCount,
      columnCount: columnCount,
      headers: headers
    }));
    it('Has the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--skeleton"))).toEqual(true);
      expect(wrapper.hasClass("".concat(prefix, "--data-table"))).toEqual(true);
    });
    it('Has the correct number of rows and columns', function () {
      expect(wrapper.find('thead > tr > th').length).toEqual(columnCount);
      expect(wrapper.find('tbody > tr').length).toEqual(rowCount);
      expect(wrapper.find('tbody > tr > td').length).toEqual(rowCount * columnCount);
    });
    it('Has the correct headers', function () {
      wrapper.find('thead > tr > th').forEach(function (header, index) {
        return expect(header.text()).toBe(headers[index]);
      });
    });
  });
});
describe('DataTableSkeleton Compact', function () {
  describe('Renders as expected', function () {
    var wrapper = (0, _enzyme.shallow)(_react.default.createElement(_DataTableSkeleton.default, {
      compact: true
    }));
    it('Has the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--skeleton"))).toEqual(true);
      expect(wrapper.hasClass("".concat(prefix, "--data-table"))).toEqual(true);
      expect(wrapper.hasClass("".concat(prefix, "--data-table--compact"))).toEqual(true);
    });
  });
});
describe('DataTableSkeleton Zebra', function () {
  describe('Renders as expected', function () {
    var wrapper = (0, _enzyme.shallow)(_react.default.createElement(_DataTableSkeleton.default, {
      zebra: true
    }));
    it('Has the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--skeleton"))).toEqual(true);
      expect(wrapper.hasClass("".concat(prefix, "--data-table"))).toEqual(true);
      expect(wrapper.hasClass("".concat(prefix, "--data-table--zebra"))).toEqual(true);
    });
  });
});