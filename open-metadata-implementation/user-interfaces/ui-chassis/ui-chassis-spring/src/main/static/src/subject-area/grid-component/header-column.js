/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from "@polymer/polymer/polymer-element.js";
import '../../shared-styles.js';

class HeaderColumn extends PolymerElement {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          display: block;
        }
        .table_cell {
            display:table-cell;
            border-style: solid;
            border-color: LightGrey;
         }
         .small_label {
            min-width: 5em;
            max-width: 15em;
            overflow-x: scroll;
         }
         .big_label {
            min-width: 10em;
            max-width: 20em;
            overflow-x: scroll;
         }

      </style>
      <template is="dom-if" if="[[smallType]]" restamp="true">
            <div class="table_cell small-label">'{{value.label}}'</div>
      </template>
      <template is="dom-if" if="[[bigType]]" restamp="true">
            <div class="table_cell big-label">'{{value.label}}'</div>
      </template>
       <template is="dom-if" if="[[objectType]]" restamp="true">
           <div class="table_cell small-label">sa Global unique identifier (guid)</div>
           <div class="table_cell small-label">sa Status</div>
           <div class="table_cell small-label">sa Created by</div>
           <div class="table_cell small-label">sa Updated by</div>
           <div class="table_cell small-label">sa Create time</div>
           <div class="table_cell small-label">sa Update time</div>
           <div class="table_cell small-label">sa Version</div>
       </template>
       </div>
    `;
    }

    static get properties() {
        return {
            value: {
                type: Object,
                notify: true
            },
            type: {
              type: String,
              notify: true,
              computed: 'computeType(def)'
            },
            // compute the specific types
            bigType: {
              type: String,
              computed: 'computeBigType(type)',
              notify: true
            },
            smallType: {
              type: String,
              computed: 'computeSmallType(type)',
              notify: true
            },
            // object type
            objectType: {
                  type: String,
                  computed: 'computeObjectType(type)',
                  notify: true
            }

        };
    }
    ready(){
        super.ready();
    }
    computeType(def) {
        return def.type;
    }
    computeBigType(type) {
         return this.computeActualType(type,"bigtext");
    }
    computeSmallType(type) {
       var isSmall = true;
       if (!(type =="object" || type == "bigtext" )) {
            isSmall =true;
       }
       return isSmall;
    }
    computeObjectType(type) {
         return this.computeActualType(type,"object");
    }
    computeActualType(type,typeName) {
        var actualType;
        if (typeName == type) {
            actualType = true;
        }
        return actualType;
    }
}

window.customElements.define('header-column', HeaderColumn);