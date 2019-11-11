/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from "@polymer/polymer/polymer-element.js";
import '../../shared-styles.js';
import './header-column.js';

class HeaderColumns extends PolymerElement {

 static get template() {
        return html`
      <style include="shared-styles">
         :host {
              display: table-row;
         }
      </style>

      <dom-repeat items="{{def}}" mutable-data>
            <template>
              <header-column value='{{item}}' >
              </header-column>
            </template>
      </dom-repeat>
    `;
    }

    static get properties() {
        return {
            def: {
                type: Array,
                notify: true,
                value: function() { return []; },
                observer: '_handleDefChange'
            }
        };
    }
    ready(){
        super.ready();
    }
    _handleDefChange(newValue) {
        if (newValue) {
            console.log("_handleDefChange driven ");
        }

    }

}

window.customElements.define('header-columns', HeaderColumns);