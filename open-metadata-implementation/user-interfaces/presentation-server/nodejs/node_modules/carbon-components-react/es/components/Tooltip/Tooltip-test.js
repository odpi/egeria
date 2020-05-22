function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } return _assertThisInitialized(self); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React, { Component } from 'react';
import debounce from 'lodash.debounce';
import FloatingMenu from '../../internal/FloatingMenu';
import Tooltip from '../Tooltip';
import { mount } from 'enzyme';
import { Information16 as Information, Add16 as Add, OverflowMenuVertical16 } from '@carbon/icons-react';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
jest.mock('lodash.debounce');
debounce.mockImplementation(function (fn) {
  return fn;
});
describe('Tooltip', function () {
  // An icon component class
  var CustomIcon =
  /*#__PURE__*/
  function (_Component) {
    _inherits(CustomIcon, _Component);

    function CustomIcon() {
      _classCallCheck(this, CustomIcon);

      return _possibleConstructorReturn(this, _getPrototypeOf(CustomIcon).apply(this, arguments));
    }

    _createClass(CustomIcon, [{
      key: "render",
      value: function render() {
        return React.createElement("div", null);
      }
    }]);

    return CustomIcon;
  }(Component);

  describe('Renders as expected with defaults', function () {
    var wrapper = mount(React.createElement(Tooltip, {
      triggerText: "Tooltip"
    }, React.createElement("p", {
      className: "".concat(prefix, "--tooltip__label")
    }, "Tooltip label"), React.createElement("p", null, "Lorem ipsum dolor sit amet")));
    var trigger = wrapper.find(".".concat(prefix, "--tooltip__trigger"));
    describe('tooltip trigger', function () {
      it('renders a tooltip container', function () {
        expect(trigger.length).toEqual(1);
      });
      it('renders the info icon', function () {
        var icon = trigger.find(Information);
        expect(icon.length).toBe(1);
      });
    });
  });
  describe('Renders as expected with specified properties', function () {
    var wrapper = mount(React.createElement(Tooltip, {
      className: "tooltip--class",
      triggerClassName: "tooltip--trigger-class",
      triggerText: "Tooltip",
      direction: "bottom",
      menuOffset: {
        left: 10,
        top: 15
      },
      showIcon: false,
      open: true
    }, React.createElement("p", null, "Tooltip label"), React.createElement("p", null, "Lorem ipsum dolor sit amet")));
    var label = wrapper.find(".".concat(prefix, "--tooltip__label"));
    var floatingMenu = wrapper.find(FloatingMenu);
    describe('tooltip container', function () {
      it("sets the tooltip's position", function () {
        expect(floatingMenu.prop('menuDirection')).toEqual('bottom');
      });
      it("sets the tooltip's offset", function () {
        expect(floatingMenu.prop('menuOffset')).toEqual({
          left: 10,
          top: 15
        });
      });
      it('does not render info icon', function () {
        var icon = label.find(Information);
        expect(icon.exists()).toBe(false);
      });
      it('sets the tooltip class', function () {
        expect(floatingMenu.find('[data-floating-menu-direction]').first().prop('className')).toBe("".concat(prefix, "--tooltip ").concat(prefix, "--tooltip--shown tooltip--class"));
      });
      it('sets the trigger class', function () {
        expect(label.prop('className')).toBe("".concat(prefix, "--tooltip__label tooltip--trigger-class"));
      });
    });
  });
  describe('Renders as expected when an Icon component wrapped with forwardRef is provided', function () {
    var wrapper = mount(React.createElement(Tooltip, {
      renderIcon: Add
    }));
    it('does render Icon', function () {
      var icon = wrapper.find(Add);
      expect(icon.exists()).toBe(true);
    });
  });
  describe('Renders as expected when custom icon component with forwardRef is provided', function () {
    var wrapper = mount(React.createElement(Tooltip, {
      renderIcon: React.forwardRef(function () {
        return React.createElement(CustomIcon, null);
      })
    }));
    it('does render provided custom icon component instance', function () {
      var icon = wrapper.find(CustomIcon);
      expect(icon.exists()).toBe(true);
    });
  });
  describe('Renders as expected when custom icon component with inner forwardRef is provided', function () {
    var wrapper = mount(React.createElement(Tooltip, {
      renderIcon: OverflowMenuVertical16
    }));
    it('does render provided custom icon component instance', function () {
      var icon = wrapper.find(OverflowMenuVertical16);
      expect(icon.exists()).toBe(true);
    });
  });
  describe('events', function () {
    it('A different key press does not change state', function () {
      var wrapper = mount(React.createElement(Tooltip, {
        triggerText: "Tooltip"
      }));
      var icon = wrapper.find(Information);
      icon.simulate('keyDown', {
        which: 'x'
      }); // Enzyme doesn't seem to allow state() in a forwardRef-wrapped class component

      expect(wrapper.find('Tooltip').instance().state.open).toBeFalsy();
    });
    it('A different key press does not change state when custom icon is set', function () {
      var wrapper = mount(React.createElement(Tooltip, {
        renderIcon: React.forwardRef(function (props, ref) {
          return React.createElement("div", {
            className: "custom-icon",
            ref: ref
          });
        }),
        triggerText: "Tooltip"
      }));
      var icon = wrapper.find('.custom-icon');
      icon.simulate('keyDown', {
        which: 'x'
      }); // Enzyme doesn't seem to allow state() in a forwardRef-wrapped class component

      expect(wrapper.find('Tooltip').instance().state.open).toBeFalsy();
    });
    it('should be in a closed state after handleOutsideClick() is invoked', function () {
      var rootWrapper = mount(React.createElement(Tooltip, {
        triggerText: "Tooltip"
      })); // Enzyme doesn't seem to allow state() in a forwardRef-wrapped class component

      expect(rootWrapper.find('Tooltip').instance().state.open).toBeFalsy(); // Enzyme doesn't seem to allow setState() in a forwardRef-wrapped class component

      rootWrapper.find('Tooltip').instance().setState({
        open: true
      });
      rootWrapper.update();
      rootWrapper.find('Tooltip').instance().handleClickOutside(); // Enzyme doesn't seem to allow state() in a forwardRef-wrapped class component

      expect(rootWrapper.find('Tooltip').instance().state.open).toEqual(false);
    });
    it('prop.open change should update open state', function () {
      var rootWrapper = mount(React.createElement(Tooltip, {
        open: false,
        triggerText: "Tooltip"
      })); // Enzyme doesn't seem to allow state() in a forwardRef-wrapped class component

      expect(rootWrapper.find('Tooltip').instance().state.open).toEqual(false);
      rootWrapper.setProps({
        open: true,
        triggerText: 'Tooltip'
      }); // Enzyme doesn't seem to allow state() in a forwardRef-wrapped class component

      expect(rootWrapper.find('Tooltip').instance().state.open).toEqual(true);
    });
    it('should avoid change the open state upon setting props, unless there the value actually changes', function () {
      var rootWrapper = mount(React.createElement(Tooltip, null));
      rootWrapper.setProps({
        open: true
      }); // Enzyme doesn't seem to allow setState() in a forwardRef-wrapped class component

      rootWrapper.find('Tooltip').instance().setState({
        open: false
      });
      rootWrapper.update();
      rootWrapper.setProps({
        open: true
      }); // Enzyme doesn't seem to allow state() in a forwardRef-wrapped class component

      expect(rootWrapper.find('Tooltip').instance().state.open).toEqual(false);
    });
  });
  describe('getTriggerPosition', function () {
    it('sets triggerPosition when triggerEl is set', function () {
      var rootWrapper = mount(React.createElement(Tooltip, {
        triggerText: "Tooltip"
      })); // Enzyme doesn't seem to allow setState() in a forwardRef-wrapped class component

      rootWrapper.find('Tooltip').instance().setState({
        triggerPosition: {
          left: 0,
          top: 0,
          right: 0,
          bottom: 0
        }
      });
      rootWrapper.update();
      rootWrapper.find('Tooltip').instance().getTriggerPosition(); // Enzyme doesn't seem to allow state() in a forwardRef-wrapped class component

      expect(rootWrapper.find('Tooltip').state.triggerPosition).not.toEqual({
        left: 0,
        top: 0,
        right: 0,
        bottom: 0
      });
    });
    it('does not set triggerPosition when triggerEl is not set', function () {
      var rootWrapper = mount(React.createElement(Tooltip, {
        triggerText: "Tooltip"
      })); // Enzyme doesn't seem to allow setState() in a forwardRef-wrapped class component

      rootWrapper.find('Tooltip').instance().setState({
        triggerPosition: {
          left: 0,
          top: 0,
          right: 0,
          bottom: 0
        }
      });
      rootWrapper.update();
      delete rootWrapper.find('Tooltip').instance().triggerEl;
      rootWrapper.find('Tooltip').instance().getTriggerPosition(); // Enzyme doesn't seem to allow state() in a forwardRef-wrapped class component

      expect(rootWrapper.find('Tooltip').instance().state.triggerPosition).toEqual({
        left: 0,
        top: 0,
        right: 0,
        bottom: 0
      });
    });
  });
});