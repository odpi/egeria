"use strict";

var _react = require("@storybook/react");

var _addonKnobs = require("@storybook/addon-knobs");

var _storybookReadme = require("storybook-readme");

var _README = _interopRequireDefault(require("./README.md"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright IBM Corp. 2016, 2018
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */
var readmeURL = 'https://bit.ly/2Z9PGsC';

var props = function props() {
  return {
    useZebraStyles: (0, _addonKnobs.boolean)('Zebra row styles (useZebraStyles)', false),
    size: (0, _addonKnobs.select)('Row height (size)', {
      compact: 'compact',
      short: 'short',
      tall: 'tall',
      none: null
    }, null),
    stickyHeader: (0, _addonKnobs.boolean)('Sticky header (experimental)', false)
  };
};

(0, _react.storiesOf)('DataTable', module).addDecorator(_addonKnobs.withKnobs).add('default', (0, _storybookReadme.withReadme)(_README.default, function () {
  return require('./stories/default').default(props());
}), {
  info: {
    /* eslint-disable no-useless-escape */
    text: "\n          Data Tables are used to represent a collection of resources, displaying a\n          subset of their fields in columns, or headers. The `DataTable` component\n          that we export from Carbon requires two props to be passed in: `rows`\n          and `headers`.\n          You can find more detailed information surrounding usage of this component\n          at the following url: ".concat(readmeURL, "\n        ")
    /* eslint-enable no-useless-escape */

  }
}).add('with toolbar', (0, _storybookReadme.withReadme)(_README.default, function () {
  return require('./stories/with-toolbar').default(props());
}), {
  info: {
    text: "\n        DataTable with action menu and filtering.\n\n        You can find more detailed information surrounding usage of this component\n        at the following url: ".concat(readmeURL, "\n      ")
  }
}).add('with sorting', (0, _storybookReadme.withReadme)(_README.default, function () {
  return require('./stories/with-sorting').default(props());
}), {
  info: {
    text: "\n        DataTable with sorting\n\n        You can find more detailed information surrounding usage of this component\n        at the following url: ".concat(readmeURL, "\n      ")
  }
}).add('with selection', (0, _storybookReadme.withReadme)(_README.default, function () {
  return require('./stories/with-selection').default(props());
}), {
  info: {
    text: "\n        DataTable with selection\n\n        You can find more detailed information surrounding usage of this component\n        at the following url: ".concat(readmeURL, "\n      ")
  }
}).add('with radio button selection', (0, _storybookReadme.withReadme)(_README.default, function () {
  return require('./stories/with-selection--radio').default(props());
}), {
  info: {
    text: "\n        DataTable with radio button selection\n\n        You can find more detailed information surrounding usage of this component\n        at the following url: ".concat(readmeURL, "\n      ")
  }
}).add('with expansion', (0, _storybookReadme.withReadme)(_README.default, function () {
  return require('./stories/with-expansion').default(props());
}), {
  info: {
    text: "\n          DataTable with expansion\n\n          You can find more detailed information surrounding usage of this component\n          at the following url: ".concat(readmeURL, "\n        ")
  }
}).add('with batch expansion', (0, _storybookReadme.withReadme)(_README.default, function () {
  return require('./stories/with-batch-expansion').default(props());
}), {
  info: {
    text: "\n          DataTable with batch expansion\n\n          You can find more detailed information surrounding usage of this component\n          at the following url: ".concat(readmeURL, "\n        ")
  }
}).add('with batch actions', (0, _storybookReadme.withReadme)(_README.default, function () {
  return require('./stories/with-batch-actions').default(props());
}), {
  info: {
    text: "\n          Uses <TableToolbar> alongside <TableBatchActions> and <TableBatchAction>\n          to create the toolbar and placeholder for where the batch action menu will\n          be displayed.\n\n          You can use the `getBatchActionProps` prop getter on the\n          <TableBatchActions> component to have it wire up the ghost menu for you.\n\n          Individual <TableBatchAction> components take in any kind of event handler\n          prop that you would expect to use, like `onClick`. You can use these\n          alongside the `selectedRows` property in your `render` prop function\n          to pass along this info to your batch action handler.\n\n          You can find more detailed information surrounding usage of this component\n          at the following url: ".concat(readmeURL, "\n        ")
  }
}).add('with dynamic content', (0, _storybookReadme.withReadme)(_README.default, function () {
  return require('./stories/with-dynamic-content').default(props());
}), {
  info: {
    text: "\n        Showcases DataTable behavior when rows are added to the component,\n        and when cell data changes dynamically.\n      "
  }
}).add('with boolean column', (0, _storybookReadme.withReadme)(_README.default, function () {
  return require('./stories/with-boolean-column').default(props());
}), {
  info: {
    text: "\n        DataTable with toolbar and filtering with column has boolean value.\n      "
  }
}).add('with options', (0, _storybookReadme.withReadme)(_README.default, function () {
  return require('./stories/with-options').default(props());
}), {
  info: {
    text: "\n        DataTable with options like disabled, isSelected, isExpanded etc.\n\n        You can find more detailed information surrounding usage of this component\n        at the following url: ".concat(readmeURL, "\n      ")
  }
}).add('with overflow menu', (0, _storybookReadme.withReadme)(_README.default, function () {
  return require('./stories/with-overflow-menu').default(props());
}), {
  info: {
    text: "\n      DataTable with Overflow menus added.\n\n      You can find more detailed information surrounding usage of this component\n      at the following url: ".concat(readmeURL, "\n    ")
  }
});