/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

/* eslint-disable no-console */
import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, select } from '@storybook/addon-knobs';
import SkeletonPlaceholder from '../SkeletonPlaceholder';
var classNames = {
  'my--skeleton__placeholder--small': 'my--skeleton__placeholder--small',
  'my--skeleton__placeholder--medium': 'my--skeleton__placeholder--medium',
  'my--skeleton__placeholder--large': 'my--skeleton__placeholder--large'
};

var props = function props() {
  return {
    className: select('Classes with different sizes', classNames)
  };
};

storiesOf('SkeletonPlaceholder', module).addDecorator(withKnobs).add('Default', function () {
  return React.createElement("div", {
    style: {
      height: '250px',
      width: '250px'
    }
  }, React.createElement("style", {
    dangerouslySetInnerHTML: {
      __html: "\n          .my--skeleton__placeholder--small {\n            height: 100px;\n            width: 100px;\n          }\n\n          .my--skeleton__placeholder--medium {\n            height: 150px;\n            width: 150px;\n          }\n\n          .my--skeleton__placeholder--large {\n            height: 250px;\n            width: 250px;\n          }\n        "
    }
  }), React.createElement(SkeletonPlaceholder, props()));
}, {
  info: {
    text: "\n        Skeleton states are used as a progressive loading state while the user waits for content to load.\n\n        By taking a height and/or width property, this component can be used when you know the exact dimensions of the incoming content, such as an image.\n\n        However, for performance reasons, it's recommended to create a class in your stylesheet to set the dimensions.\n      "
  }
});