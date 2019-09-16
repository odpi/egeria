/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from "@polymer/polymer/polymer-element.js";
import '../../shared-styles.js';

class GridModelInstance extends PolymerElement {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          display: inline-block;
          padding: 10px 20px;
        }
      </style>

      <div></div>
    `;
    }

    static get properties() {
        return {
         modelInstance: {
                type: Object,
                notify: true,
                observer: '_modelInstanceChanged'
              }
     };
    }
    ready(){
        super.ready();
    }
    _modelInstanceChanged(newValue) {
        console.log('_modelInstanceChanged ' + newValue)
    }
}

window.customElements.define('grid-model-instance', GridModelInstance);