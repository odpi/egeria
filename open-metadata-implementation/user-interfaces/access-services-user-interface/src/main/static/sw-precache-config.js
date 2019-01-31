/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

module.exports = {
  staticFileGlobs: [
    'src/**/*',
    'manifest.json'
  ],
  runtimeCaching: [
    {
      urlPattern: /\/@webcomponents\/webcomponentsjs\//,
      handler: 'fastest'
    }
  ]
};
