/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import { Search16 } from '@carbon/icons-react';
import Button from '../Button';
import Link from '../Link';
import ButtonSkeleton from '../Button/Button.Skeleton';
import { shallow, mount } from 'enzyme';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
describe('Button', function () {
  describe('Renders common props as expected', function () {
    var wrapper = shallow( // eslint-disable-next-line jsx-a11y/tabindex-no-positive
    React.createElement(Button, {
      tabIndex: 2,
      className: "extra-class"
    }, React.createElement("div", {
      className: "child"
    }, "child"), React.createElement("div", {
      className: "child"
    }, "child")));
    var wrapperHref = shallow( // eslint-disable-next-line jsx-a11y/tabindex-no-positive
    React.createElement(Button, {
      tabIndex: 2,
      className: "extra-class",
      href: "/home"
    }, React.createElement("div", {
      className: "child"
    }, "child"), React.createElement("div", {
      className: "child"
    }, "child")));
    it('renders children as expected', function () {
      expect(wrapper.find('.child').length).toBe(2);
      expect(wrapperHref.find('.child').length).toBe(2);
    });
    it('should set tabIndex if one is passed via props', function () {
      expect(wrapper.props().tabIndex).toEqual(2);
      expect(wrapperHref.props().tabIndex).toEqual(2);
    });
    it('should add extra classes via className', function () {
      expect(wrapper.hasClass('extra-class')).toBe(true);
      expect(wrapperHref.hasClass('extra-class')).toBe(true);
    });
  });
  describe('Renders <button> props as expected', function () {
    var wrapper = shallow( // eslint-disable-next-line jsx-a11y/tabindex-no-positive
    React.createElement(Button, {
      tabIndex: 2
    }, React.createElement("div", {
      className: "child"
    }, "child"), React.createElement("div", {
      className: "child"
    }, "child")));
    it('renders as a <button> element without an href', function () {
      expect(wrapper.is('button')).toBe(true);
    });
    it('should set disabled to false by default', function () {
      expect(wrapper.props().disabled).toBe(false);
    });
    it('should set disabled if one is passed via props', function () {
      wrapper.setProps({
        disabled: true
      });
      expect(wrapper.props().disabled).toBe(true);
    });
    it('should set type to button by default', function () {
      expect(wrapper.props().type).toEqual('button');
    });
    it('should only set type to [button, reset or submit] if one is passed via props', function () {
      wrapper.setProps({
        type: 'reset'
      });
      expect(wrapper.props().type).toEqual('reset');
      wrapper.setProps({
        type: 'submit'
      });
      expect(wrapper.props().type).toEqual('submit');
    });
  });
  describe('Renders <a> props as expected', function () {
    var wrapper = shallow( // eslint-disable-next-line jsx-a11y/tabindex-no-positive
    React.createElement(Button, {
      href: "#",
      tabIndex: 2
    }, React.createElement("div", {
      className: "child"
    }, "child"), React.createElement("div", {
      className: "child"
    }, "child")));
    it('renders as an <a> element with an href', function () {
      expect(wrapper.is('a')).toBe(true);
    });
    it('should always render with [role="button"] by default', function () {
      expect(wrapper.props().role).toEqual('button');
    });
  });
  describe('Renders arbitrary component with correct props', function () {
    var wrapper;
    beforeEach(function () {
      wrapper = shallow(React.createElement(Button, {
        as: Link,
        "data-foo": "foo"
      }, React.createElement("div", {
        className: "child"
      }, "child"), React.createElement("div", {
        className: "child"
      }, "child")));
    });
    it('renders as a Link with data attribute', function () {
      expect(wrapper.is(Link)).toBe(true);
      expect(wrapper.is('[data-foo="foo"]')).toBe(true);
    });
  });
  describe('Renders icon buttons', function () {
    var iconButton = mount(React.createElement(Button, {
      renderIcon: Search16,
      iconDescription: "Search"
    }, "Search"));
    var icon = iconButton.find('svg');
    it('should have the appropriate icon', function () {
      expect(icon.hasClass("".concat(prefix, "--btn__icon"))).toBe(true);
    });
    it('should return error if icon given without description', function () {
      var props = {
        renderIcon: Search16
      }; // eslint-disable-next-line quotes

      var error = new Error('renderIcon property specified without also providing an iconDescription property.');
      expect(Button.propTypes.iconDescription(props)).toEqual(error);
    });
  });
  describe('Renders custom icon buttons', function () {
    var iconButton = mount(React.createElement(Button, {
      renderIcon: Search16,
      iconDescription: "Search"
    }, "Search"));
    var originalIcon = mount(React.createElement(Search16, null)).find('svg');
    var icon = iconButton.find('svg');
    it('should have the appropriate icon', function () {
      expect(icon.hasClass("".concat(prefix, "--btn__icon"))).toBe(true);
      expect(icon.find(':not(svg):not(title)').html()).toBe(originalIcon.children().html());
    });
    it('should return error if icon given without description', function () {
      var props = {
        renderIcon: Search16
      }; // eslint-disable-next-line quotes

      var error = new Error('renderIcon property specified without also providing an iconDescription property.');
      expect(Button.propTypes.iconDescription(props)).toEqual(error);
    });
  });
});
describe('Primary Button', function () {
  describe('Renders as expected', function () {
    var wrapper = shallow(React.createElement(Button, {
      className: "extra-class"
    }));
    it('has the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--btn"))).toEqual(true);
    });
    it('should add extra classes that are passed via className', function () {
      expect(wrapper.hasClass('extra-class')).toEqual(true);
    });
  });
});
describe('Secondary Button', function () {
  describe('Renders as expected', function () {
    var wrapper = shallow(React.createElement(Button, {
      kind: "secondary",
      className: "extra-class"
    }));
    it('has the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--btn--secondary"))).toEqual(true);
    });
    it('should add extra classes that are passed via className', function () {
      expect(wrapper.hasClass('extra-class')).toEqual(true);
    });
  });
});
describe('Ghost Button', function () {
  describe('Renders as expected', function () {
    var wrapper = shallow(React.createElement(Button, {
      kind: "ghost",
      className: "extra-class"
    }));
    it('has the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--btn--ghost"))).toEqual(true);
    });
    it('should add extra classes that are passed via className', function () {
      expect(wrapper.hasClass('extra-class')).toEqual(true);
    });
  });
});
describe('Small Button', function () {
  describe('Renders as expected', function () {
    var wrapper = shallow(React.createElement(Button, {
      size: "small",
      className: "extra-class"
    }));
    it('has the expected classes for small', function () {
      expect(wrapper.hasClass("".concat(prefix, "--btn--sm"))).toEqual(true);
    });
    it('should add extra classes that are passed via className', function () {
      expect(wrapper.hasClass('extra-class')).toEqual(true);
    });
  });
  describe('deprecated prop `small`', function () {
    var wrapper = shallow(React.createElement(Button, {
      small: true,
      className: "extra-class"
    }));
    it('has the expected classes for small', function () {
      expect(wrapper.hasClass("".concat(prefix, "--btn--sm"))).toEqual(true);
    });
    it('should add extra classes that are passed via className', function () {
      expect(wrapper.hasClass('extra-class')).toEqual(true);
    });
  });
});
describe('DangerButton', function () {
  describe('Renders as expected', function () {
    var wrapper = shallow(React.createElement(Button, {
      kind: "danger",
      className: "extra-class"
    }));
    it('has the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--btn--danger"))).toEqual(true);
    });
    it('should add extra classes that are passed via className', function () {
      expect(wrapper.hasClass('extra-class')).toEqual(true);
    });
  });
});
describe('danger--primaryButton', function () {
  describe('Renders as exptected', function () {
    var wrapper = shallow(React.createElement(Button, {
      kind: "danger--primary",
      className: "extra-class"
    }));
    it('has the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--btn--danger--primary"))).toEqual(true);
    });
    it('should add extra classes that are passed via className', function () {
      expect(wrapper.hasClass('extra-class')).toEqual(true);
    });
  });
});
describe('TertiaryButton', function () {
  describe('Renders as exptected', function () {
    var wrapper = shallow(React.createElement(Button, {
      kind: "tertiary",
      className: "extra-class"
    }));
    it('has the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--btn--tertiary"))).toEqual(true);
    });
    it('should add extra classes that are passed via className', function () {
      expect(wrapper.hasClass('extra-class')).toEqual(true);
    });
  });
});
describe('Icon-only button', function () {
  describe('Renders as expected', function () {
    var wrapper = mount(React.createElement(Button, {
      hasIconOnly: true
    }));
    it('has the expected classes', function () {
      expect(wrapper.find('button').hasClass("".concat(prefix, "--btn--icon-only"))).toEqual(true);
      expect(wrapper.find('button').hasClass("".concat(prefix, "--tooltip__trigger"))).toEqual(true);
      expect(wrapper.find('button').hasClass("".concat(prefix, "--tooltip--a11y"))).toEqual(true);
    });
    it('should only set tooltip position and alignment if passed via props', function () {
      wrapper.setProps({
        tooltipPosition: 'bottom'
      });
      expect(wrapper.props().tooltipPosition).toEqual('bottom');
      wrapper.setProps({
        tooltipAlignment: 'center'
      });
      expect(wrapper.props().tooltipAlignment).toEqual('center');
    });
    it('should contain assistive text', function () {
      wrapper.setProps({
        tooltipPosition: 'bottom'
      });
      wrapper.setProps({
        tooltipAlignment: 'center'
      });
      expect(wrapper.find(".".concat(prefix, "--assistive-text")).length).toEqual(1);
    });
  });
});
describe('ButtonSkeleton', function () {
  describe('Renders as expected', function () {
    var wrapper = shallow(React.createElement(ButtonSkeleton, null));
    it('has the expected classes', function () {
      expect(wrapper.hasClass("".concat(prefix, "--skeleton"))).toEqual(true);
      expect(wrapper.hasClass("".concat(prefix, "--btn"))).toEqual(true);
    });
  });
  describe('Renders <a> props as expected', function () {
    var wrapper = shallow( // eslint-disable-next-line jsx-a11y/tabindex-no-positive
    React.createElement(ButtonSkeleton, {
      href: "#"
    }));
    it('renders as an <a> element with an href', function () {
      expect(wrapper.is('a')).toBe(true);
    });
    it('should always render with [role="button"] by default', function () {
      expect(wrapper.props().role).toEqual('button');
    });
  });
});
describe('Small ButtonSkeleton', function () {
  describe('Renders as expected', function () {
    var wrapper = shallow(React.createElement(ButtonSkeleton, {
      small: true
    }));
    it('has the expected classes for small', function () {
      expect(wrapper.hasClass("".concat(prefix, "--btn--sm"))).toEqual(true);
      expect(wrapper.hasClass("".concat(prefix, "--skeleton"))).toEqual(true);
    });
  });
});