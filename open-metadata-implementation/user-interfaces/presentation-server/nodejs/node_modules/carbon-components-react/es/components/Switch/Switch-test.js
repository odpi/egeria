/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import Switch from '../Switch';
import { shallow } from 'enzyme';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
describe('Switch', function () {
  describe('component rendering', function () {
    var buttonWrapper = shallow(React.createElement(Switch, {
      kind: "button",
      icon: React.createElement("svg", null),
      text: "test"
    }));
    it('should render a button when kind is button', function () {
      expect(buttonWrapper.is('button')).toEqual(true);
    });
    it('should have the expected text', function () {
      expect(buttonWrapper.text()).toEqual('test');
    });
    it('label should have the expected class', function () {
      var className = "".concat(prefix, "--content-switcher__label");
      expect(buttonWrapper.find('span').hasClass(className)).toEqual(true);
    });
    it('should have the expected class', function () {
      var cls = "".concat(prefix, "--content-switcher-btn");
      expect(buttonWrapper.hasClass(cls)).toEqual(true);
    });
    it('should not have selected class', function () {
      var selectedClass = "".concat(prefix, "--content-switcher--selected");
      expect(buttonWrapper.hasClass(selectedClass)).toEqual(false);
    });
    it('should have a selected class when selected is set to true', function () {
      var selected = true;
      buttonWrapper.setProps({
        selected: selected
      });
      expect(buttonWrapper.hasClass("".concat(prefix, "--content-switcher--selected"))).toEqual(true);
    });
  });
  describe('events', function () {
    var buttonOnClick = jest.fn();
    var linkOnClick = jest.fn();
    var buttonOnKey = jest.fn();
    var linkOnKey = jest.fn();
    var index = 1;
    var name = 'first';
    var text = 'test';
    var buttonWrapper = shallow(React.createElement(Switch, {
      index: index,
      name: name,
      kind: "button",
      onClick: buttonOnClick,
      onKeyDown: buttonOnKey,
      text: text
    }));
    var linkWrapper = shallow(React.createElement(Switch, {
      index: index,
      name: name,
      kind: "anchor",
      onClick: linkOnClick,
      onKeyDown: linkOnKey,
      text: text
    }));
    it('should invoke button onClick handler', function () {
      buttonWrapper.simulate('click', {
        preventDefault: function preventDefault() {}
      });
      expect(buttonOnClick).toBeCalledWith({
        index: index,
        name: name,
        text: text
      });
    });
    it('should invoke link onClick handler', function () {
      linkWrapper.simulate('click', {
        preventDefault: function preventDefault() {}
      });
      expect(buttonOnClick).toBeCalledWith({
        index: index,
        name: name,
        text: text
      });
    });
  });
});