/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import Toolbar, { ToolbarItem, ToolbarTitle, ToolbarOption, ToolbarDivider } from '../Toolbar';
import OverflowMenu from '../OverflowMenu';
import ToolbarSearch from '../ToolbarSearch';
import { shallow, mount } from 'enzyme';
import { settings } from 'carbon-components';
var prefix = settings.prefix;
describe('Toolbar', function () {
  describe('renders as expected', function () {
    var toolbar = mount(React.createElement(Toolbar, {
      className: "extra-class"
    }));
    describe('toolbar container', function () {
      it('should render the expected classes', function () {
        expect(toolbar.children().hasClass("".concat(prefix, "--toolbar"))).toEqual(true);
        expect(toolbar.children().hasClass('extra-class')).toEqual(true);
      });
    });
  });
  describe('Toolbar Search Item', function () {
    var toolbar = mount(React.createElement(Toolbar, {
      className: "extra-class"
    }, React.createElement(ToolbarItem, {
      type: "search"
    })));
    var toolbarSearch = mount(React.createElement(ToolbarSearch, {
      placeHolderText: "Test placeholder"
    }));
    var expandBtn = toolbarSearch.find('button');
    it('should render the toolbar search item inside the toolbar', function () {
      expect(toolbar.find(ToolbarItem).length).toEqual(1);
    });
    it('should have the expected placeholder text', function () {
      expect(toolbarSearch.props().placeHolderText).toEqual('Test placeholder');
    });
    it('should expand the search item when the search icon is clicked', function () {
      expect(toolbarSearch.state().expanded).toEqual(false);
      expandBtn.simulate('click');
      expect(toolbarSearch.state().expanded).toEqual(true);
    });
    it('should minimize the search item when the search icon is clicked when the state is expanded', function () {
      expect(toolbarSearch.state().expanded).toEqual(true);
      expandBtn.simulate('click');
      expect(toolbarSearch.state().expanded).toEqual(false);
    });
    it('should minimize the search when clicking outside of the search item', function () {
      var rootWrapper = shallow(React.createElement(ToolbarSearch, null));
      expect(rootWrapper.state().expanded).toEqual(false);
      rootWrapper.setState({
        expanded: true
      });
      rootWrapper.props().onClickOutside();
      expect(rootWrapper.state().expanded).toEqual(false);
    });
  });
  describe('ToolbarItem with an overflow menu', function () {
    var toolbarItem = mount(React.createElement(ToolbarItem, null, React.createElement(OverflowMenu, null)));
    it('should render an overflow menu inside a toolbar item', function () {
      expect(toolbarItem.find(OverflowMenu).length).toEqual(1);
    });
    describe('with ToolbarTitle ', function () {
      var withToolbarTitle = mount(React.createElement(ToolbarItem, null, React.createElement(OverflowMenu, {
        open: true
      }, React.createElement(ToolbarTitle, {
        title: "Test title"
      }))));
      var toolbarTitle = withToolbarTitle.find(ToolbarTitle);
      it('should render a toolbar title with the expected className', function () {
        expect(toolbarTitle.children().hasClass("".concat(prefix, "--toolbar-menu__title"))).toEqual(true);
      });
      it('should render a toolbar title with the expected title', function () {
        expect(toolbarTitle.props().title).toEqual('Test title');
      });
    });
    describe('with ToolbarOption ', function () {
      var withToolbarOption = mount(React.createElement(ToolbarItem, null, React.createElement(OverflowMenu, {
        open: true
      }, React.createElement(ToolbarOption, null, React.createElement("div", null, "Test child")))));
      var toolbarOption = withToolbarOption.find(ToolbarOption);
      it('should render a toolbar option with the expected className', function () {
        expect(toolbarOption.children().hasClass("".concat(prefix, "--toolbar-menu__option"))).toEqual(true);
      });
      it('should render with the expected children', function () {
        expect(toolbarOption.find('div').length).toEqual(1);
      });
    });
    describe('with ToolbarDivider ', function () {
      var withToolbarDivider = mount(React.createElement(ToolbarItem, null, React.createElement(OverflowMenu, {
        open: true
      }, React.createElement(ToolbarDivider, null))));
      var toolbarDivider = withToolbarDivider.find(ToolbarDivider);
      it('should render a toolbar divider with the expected className', function () {
        expect(toolbarDivider.children().hasClass("".concat(prefix, "--toolbar-menu__divider"))).toEqual(true);
      });
    });
  });
});