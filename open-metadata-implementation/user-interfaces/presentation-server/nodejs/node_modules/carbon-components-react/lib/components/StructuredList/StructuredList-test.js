"use strict";

var _react = _interopRequireDefault(require("react"));

var _StructuredList = require("../StructuredList");

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
describe('StructuredListWrapper', function () {
  describe('Renders as expected', function () {
    var wrapper = (0, _enzyme.shallow)(_react.default.createElement(_StructuredList.StructuredListWrapper, {
      className: "extra-class"
    }, "hi"));
    it('should have the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--structured-list"))).toEqual(true);
    });
    it('Should add extra classes that are passed via className', function () {
      expect(wrapper.hasClass('extra-class')).toEqual(true);
    });
    it('By default, border prop is false', function () {
      wrapper.setProps({
        border: false
      });
      expect(wrapper.hasClass("".concat(prefix, "--structured-list--border"))).toEqual(false);
    });
    it('By default, selection prop is false', function () {
      wrapper.setProps({
        border: false
      });
      expect(wrapper.hasClass("".concat(prefix, "--structured-list--selection"))).toEqual(false);
    });
    it('Should add the modifier class for border when border prop is true', function () {
      wrapper.setProps({
        border: true
      });
      expect(wrapper.hasClass("".concat(prefix, "--structured-list--border"))).toEqual(true);
    });
    it('Should add the modifier class for selection when selection prop is true', function () {
      wrapper.setProps({
        selection: true
      });
      expect(wrapper.hasClass("".concat(prefix, "--structured-list--selection"))).toEqual(true);
    });
  });
});
describe('StructuredListHead', function () {
  describe('Renders as expected', function () {
    var wrapper = (0, _enzyme.shallow)(_react.default.createElement(_StructuredList.StructuredListHead, {
      className: "extra-class"
    }, "hi"));
    it('should have the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--structured-list-thead"))).toEqual(true);
    });
    it('Should add extra classes that are passed via className', function () {
      expect(wrapper.hasClass('extra-class')).toEqual(true);
    });
    it('Should accept other props from ...other', function () {
      var wrapperProps = (0, _enzyme.shallow)(_react.default.createElement(_StructuredList.StructuredListHead, {
        title: "title"
      }, "hi"));
      expect(wrapperProps.props().title).toEqual('title');
    });
  });
});
describe('StructuredListInput', function () {
  describe('Renders as expected', function () {
    var wrapper = (0, _enzyme.shallow)(_react.default.createElement(_StructuredList.StructuredListInput, {
      className: "extra-class"
    }));
    it('should have the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--structured-list-input"))).toEqual(true);
    });
    it('Should add extra classes that are passed via className', function () {
      expect(wrapper.hasClass('extra-class')).toEqual(true);
    });
    it('Should accept other props from ...other', function () {
      var wrapperProps = (0, _enzyme.shallow)(_react.default.createElement(_StructuredList.StructuredListInput, {
        title: "title"
      }));
      expect(wrapperProps.props().title).toEqual('title');
    });
    it('Should render unique id with multiple inputs when no id prop is given', function () {
      var wrapper1 = (0, _enzyme.mount)(_react.default.createElement(_StructuredList.StructuredListInput, {
        className: "extra-class"
      }));
      var wrapper2 = (0, _enzyme.mount)(_react.default.createElement(_StructuredList.StructuredListInput, {
        className: "extra-class"
      }));
      expect(wrapper1.instance().uid).not.toEqual(wrapper2.instance().uid);
    });
  });
});
describe('StructuredListRow', function () {
  describe('Renders as expected', function () {
    var wrapper = (0, _enzyme.shallow)(_react.default.createElement(_StructuredList.StructuredListRow, {
      className: "extra-class"
    }));
    it('should have the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--structured-list-row"))).toEqual(true);
    });
    it('Should add extra classes that are passed via className', function () {
      expect(wrapper.hasClass('extra-class')).toEqual(true);
    });
    it('should use correct class when head prop is true', function () {
      wrapper.setProps({
        head: true
      });
      expect(wrapper.hasClass("".concat(prefix, "--structured-list-row--header-row"))).toEqual(true);
    });
    it('should use <div> HTML by default (or when label prop is false)', function () {
      var wrapperLabel = (0, _enzyme.shallow)(_react.default.createElement(_StructuredList.StructuredListRow, null));
      expect(wrapperLabel.getElement().type).toEqual('div');
    });
    it('should use <label> HTML when label prop is true', function () {
      var wrapperLabel = (0, _enzyme.shallow)(_react.default.createElement(_StructuredList.StructuredListRow, {
        label: true
      }));
      expect(wrapperLabel.getElement().type).toEqual('label');
    });
    it('Should accept other props from ...other', function () {
      var wrapperProps = (0, _enzyme.shallow)(_react.default.createElement(_StructuredList.StructuredListRow, {
        title: "title"
      }, "hi"));
      expect(wrapperProps.props().title).toEqual('title');
    });
  });
});
describe('StructuredListBody', function () {
  describe('Renders as expected', function () {
    var wrapper = (0, _enzyme.shallow)(_react.default.createElement(_StructuredList.StructuredListBody, {
      className: "extra-class"
    }, "hi"));
    it('should have the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--structured-list-tbody"))).toEqual(true);
    });
    it('Should add extra classes that are passed via className', function () {
      expect(wrapper.hasClass('extra-class')).toEqual(true);
    });
    it('Should accept other props from ...other', function () {
      var wrapperProps = (0, _enzyme.shallow)(_react.default.createElement(_StructuredList.StructuredListBody, {
        title: "title"
      }, "hi"));
      expect(wrapperProps.props().title).toEqual('title');
    });
  });
});
describe('StructuredListCell', function () {
  describe('Renders as expected', function () {
    var wrapper = (0, _enzyme.shallow)(_react.default.createElement(_StructuredList.StructuredListCell, {
      className: "extra-class"
    }, "hi"));
    it('Should add extra classes that are passed via className', function () {
      expect(wrapper.hasClass('extra-class')).toEqual(true);
    });
    it('should have the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--structured-list-td"))).toEqual(true);
    });
    it('should use correct class when head prop is true', function () {
      wrapper.setProps({
        head: true
      });
      expect(wrapper.hasClass("".concat(prefix, "--structured-list-th"))).toEqual(true);
    });
    it('should use correct class when noWrap prop is true', function () {
      wrapper.setProps({
        noWrap: true
      });
      expect(wrapper.hasClass("".concat(prefix, "--structured-list-content--nowrap"))).toEqual(true);
    });
    it('Should accept other props from ...other', function () {
      var wrapperProps = (0, _enzyme.shallow)(_react.default.createElement(_StructuredList.StructuredListCell, {
        title: "title"
      }, "hi"));
      expect(wrapperProps.props().title).toEqual('title');
    });
  });
});