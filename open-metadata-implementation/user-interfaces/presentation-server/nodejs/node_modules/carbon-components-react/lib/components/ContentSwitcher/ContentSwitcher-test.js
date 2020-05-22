"use strict";

var _react = _interopRequireDefault(require("react"));

var _ContentSwitcher = _interopRequireDefault(require("../ContentSwitcher"));

var _Switch = _interopRequireDefault(require("../Switch"));

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
describe('ContentSwitcher', function () {
  describe('component initial rendering', function () {
    var wrapper = (0, _enzyme.shallow)(_react.default.createElement(_ContentSwitcher.default, {
      onChange: function onChange() {},
      className: "extra-class"
    }, _react.default.createElement(_Switch.default, {
      kind: "anchor",
      text: "one"
    }), _react.default.createElement(_Switch.default, {
      kind: "anchor",
      text: "two"
    })));
    var children = wrapper.find(_Switch.default);
    it('should have the correct class', function () {
      expect(wrapper.hasClass("".concat(prefix, "--content-switcher"))).toEqual(true);
    });
    it('should render children as expected', function () {
      expect(children.length).toEqual(2);
    });
    it('should default "selected" property to true on first child', function () {
      expect(children.first().props().selected).toEqual(true);
      expect(children.last().props().selected).toEqual(false);
    });
    it('should apply extra classes passed to it', function () {
      expect(wrapper.hasClass('extra-class')).toEqual(true);
    });
  });
  describe('Allow initial state to draw from props', function () {
    var onChange = jest.fn();
    var mockData = {
      index: 0
    };
    var wrapper = (0, _enzyme.mount)(_react.default.createElement(_ContentSwitcher.default, {
      selectedIndex: 1,
      onChange: onChange,
      className: "extra-class"
    }, _react.default.createElement(_Switch.default, {
      kind: "anchor",
      text: "one"
    }), _react.default.createElement(_Switch.default, {
      kind: "anchor",
      text: "two"
    })));
    var children = wrapper.find(_Switch.default);
    it('Should apply the selected property on the selected child', function () {
      expect(children.first().props().selected).toEqual(false);
      expect(children.last().props().selected).toEqual(true);
    });
    it('should avoid change the selected index upon setting props, unless there the value actually changes', function () {
      wrapper.setProps({
        selectedIndex: 1
      }); // Turns `state.selectedIndex` to `0`

      children.first().props().onClick(mockData);
      wrapper.setProps({
        selectedIndex: 1
      }); // No change in `selectedIndex` prop

      var clonedChildren = wrapper.find(_Switch.default);
      expect(clonedChildren.first().props().selected).toEqual(true);
      expect(clonedChildren.last().props().selected).toEqual(false);
    });
    it('should change the selected index upon change in props', function () {
      wrapper.setProps({
        selectedIndex: 0
      });
      children.first().props().onClick(mockData);
      wrapper.setProps({
        selectedIndex: 1
      });
      var clonedChildren = wrapper.find(_Switch.default);
      expect(clonedChildren.first().props().selected).toEqual(false);
      expect(clonedChildren.last().props().selected).toEqual(true);
    });
  });
  describe('when child component onClick is invoked', function () {
    var onChange = jest.fn();
    var mockData = {
      index: 1
    };
    var wrapper = (0, _enzyme.mount)(_react.default.createElement(_ContentSwitcher.default, {
      onChange: onChange
    }, _react.default.createElement(_Switch.default, {
      kind: "anchor",
      text: "one"
    }), _react.default.createElement(_Switch.default, {
      kind: "anchor",
      text: "two"
    })));
    var children = wrapper.find(_Switch.default);
    children.first().props().onClick(mockData);
    it('should invoke onChange', function () {
      expect(onChange).toBeCalledWith(mockData);
    });
    it('should set the correct selectedIndex', function () {
      expect(wrapper.state('selectedIndex')).toEqual(mockData.index);
    });
    it('should set selected to true on the correct child', function () {
      wrapper.update();
      var firstChild = wrapper.find(_Switch.default).first();
      var secondChild = wrapper.find(_Switch.default).last();
      expect(firstChild.props().selected).toEqual(false);
      expect(secondChild.props().selected).toEqual(true);
    });
  });
  describe('when child component onKeyDown is invoked', function () {
    var onChange = jest.fn();
    var mockData = {
      index: 1
    };
    var wrapper = (0, _enzyme.mount)(_react.default.createElement(_ContentSwitcher.default, {
      onChange: onChange
    }, _react.default.createElement(_Switch.default, {
      kind: "anchor",
      text: "one"
    }), _react.default.createElement(_Switch.default, {
      kind: "anchor",
      text: "two"
    })));
    var children = wrapper.find(_Switch.default);
    children.first().props().onKeyDown(mockData);
    it('should invoke onChange', function () {
      expect(onChange).toBeCalledWith(mockData);
    });
    it('should set the correct selectedIndex', function () {
      expect(wrapper.state('selectedIndex')).toEqual(mockData.index);
    });
    it('should set selected to true on the correct child', function () {
      wrapper.update();
      var firstChild = wrapper.find(_Switch.default).first();
      var secondChild = wrapper.find(_Switch.default).last();
      expect(firstChild.props().selected).toEqual(false);
      expect(secondChild.props().selected).toEqual(true);
    });
  });
});