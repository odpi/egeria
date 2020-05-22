/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
import React from 'react';
import { iconSearch } from 'carbon-icons';
import Icon, { findIcon, svgShapes, getSvgData, isPrefixed } from '../Icon';
import { mount } from 'enzyme';
describe('Icon', function () {
  describe('Renders as expected', function () {
    var props = {
      className: 'extra-class',
      icon: iconSearch,
      width: '20',
      height: '20',
      description: 'close the thing',
      iconTitle: 'title',
      style: {
        transition: '2s'
      }
    };
    var wrapper = mount(React.createElement(Icon, props));
    it('Renders `description` as expected', function () {
      expect(wrapper.props().description).toEqual('close the thing');
    });
    it('Renders `title` as expected', function () {
      expect(wrapper.props().iconTitle).toEqual('title');
    });
    it('should have a default role prop', function () {
      expect(wrapper.props().role).toEqual('img');
    });
    it('should have expected viewBox on <svg>', function () {
      expect(wrapper.find('svg').props().viewBox).not.toEqual('');
    });
    it('should add extra classes that are passed via className', function () {
      expect(wrapper.props().className).toEqual('extra-class');
    });
    it('should recieve width props', function () {
      expect(wrapper.props().width).toEqual('20');
    });
    it('should recieve height props', function () {
      expect(wrapper.props().height).toEqual('20');
    });
    it('should recieve style props', function () {
      expect(wrapper.props().style).toEqual({
        transition: '2s'
      });
    });
  });
  describe('findIcon', function () {
    it('should return a defined object', function () {
      var test = findIcon('search');
      expect(test).toBeDefined();
    });
    it('returns a single JSON object', function () {
      var test = [findIcon('search')];
      expect(test.length).toEqual(1);
    });
    it('returns false when given wrong name param', function () {
      var test = findIcon('wrong-name');
      expect(test).toBe(false);
    });
    it('throws if multiple icons are found from one name param', function () {
      var json = [{
        name: 'bob'
      }, {
        name: 'bob'
      }];
      expect(function () {
        findIcon('bob', json);
      }).toThrow();
    });
  });
  describe('getSvgData', function () {
    it('returns false when given an undefined icon name', function () {
      var badData = getSvgData('wrongIconName');
      expect(badData).toBe(false);
    });
  });
  describe('svgShapes', function () {
    it('returns with SVG XML when given a valid icon name', function () {
      var data = getSvgData('icon--search');
      var content = svgShapes(data);
      expect(content.length).toBeGreaterThan(0);
    });
    it('returns empty when given an icon with no valid svgProp', function () {
      var svgData = {
        invalidProp: [{
          invalidAttribute: 43
        }]
      };
      var content = svgShapes(svgData);
      expect(content.length).toBeGreaterThan(0);
      expect(content).toEqual(['']);
    });
    it('takes care of polygons', function () {
      var svgData = {
        polygons: [{
          points: 'POINT'
        }]
      };
      expect(svgShapes(svgData).map(function (item) {
        return item.map(function (_ref) {
          var type = _ref.type,
              key = _ref.key,
              props = _ref.props;
          return {
            type: type,
            key: key,
            props: props
          };
        });
      })).toEqual([[{
        type: 'polygon',
        key: 'key0',
        props: {
          points: 'POINT'
        }
      }]]);
    });
  });
  describe('isPrefixed', function () {
    it('returns true when given a name with icon-- prefix', function () {
      var prefixed = isPrefixed('icon--search');
      expect(prefixed).toBe(true);
    });
    it('returns false when given a name without icon-- prefix', function () {
      var prefixed = isPrefixed('search');
      expect(prefixed).toBe(false);
    });
  });
});