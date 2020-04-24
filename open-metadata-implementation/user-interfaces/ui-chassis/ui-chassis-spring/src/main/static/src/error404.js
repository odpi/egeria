/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from '@polymer/polymer/polymer-element.js';

class Error404 extends PolymerElement {
  static get template() {
    return html`
      <style>
        :host {
          display: block;
          margin: 10px 24px;
          padding: 5px;
          background-color:  var(--egeria-background-color);
          min-height: calc(100vh - 84px);
        }
      </style>

      Oops, can't find what are you looking for! <br>
      <a href="/#[[rootPath]]">Head back to home.</a>
    `;
  }
}

window.customElements.define('my-view404', Error404);
