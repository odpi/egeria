/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from "@polymer/polymer/polymer-element.js";
import '@polymer/paper-input/paper-input.js';
import '@polymer/paper-material/paper-material.js';
import '../property-pane/datetime-widget.js';
import './property-row.js';
import '../../shared-styles.js';

class PropertyCell extends PolymerElement {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          display: block;
        }
         .table_cell {
                    display:table-cell;
                    width:15em;
                    border-style: solid;
                    border-color: LightGrey;
        }
      </style>
      <template is="dom-if" if="[[textType]]" restamp="true">
            <paper-input value ="{{value}}" readonly="{{readonly}}">
      </template>
      <template is="dom-if" if="[[bigTextType]]" restamp="true">
            <paper-input value ="{{value}}" readonly="{{readonly}}">
      </template>      
       <template is="dom-if" if="[[dateType]]" restamp="true">
            <datetime-widget type="{{type}}" name ="{{name}}" value ="{{value}}" readonly="{{readonly}}">
            </datetime-widget>

       </template>

       <template is="dom-if" if="[[objectType]]" restamp="true">
         <!--   <property-row row="[[value]]"></property-pane> -->
       </template>
       </div>
    `;
    }

    static get properties() {
        return {
            name: {
                type: String,
                notify: true
            },
            value: {
                type: Object,
                notify: true,
                observer: '_handleValueChanged'
            },
            type: {
              type: String,
              notify: true
            },
            readonly: {
             type: Boolean,
             notify: true
            },
            // compute the specific types

            // primitive types
            textType: {
              type: String,
              computed: 'computeTextType(type)',
              notify: true
            },
            bigTextType: {
              type: String,
              computed: 'computeBigTextType(type)',
              notify: true
            },            
            dateType: {
              type: String,
              computed: 'computeDateType(type)',
              notify: true
            },
            // object type
            objectType: {
                  type: String,
                  computed: 'computeObjectType(type)',
                  notify: true
            }
//            ,
//            booleanType: {
//              type: String,
//              computed: 'computeBooleanType(type)',
//              notify: true
//            },
//            intType: {
//              type: String,
//              computed: 'computeIntType(type)',
//              notify: true
//            },
//            // collection types
//            mapType: {
//              type: String,
//              computed: 'computeIntType(type)',
//              notify: true
//            },
//            setType: {
//              type: String,
//              computed: 'computeSetType(type)',
//              notify: true
//            },
//            listType: {
//               type: String,
//               computed: 'computeListType(type)',
//               notify: true
//            },
            // TODO enums

        };
    }
    ready(){
        super.ready();
    }
    computeTextType(type) {
         return this.computeActualType(type,"text");
    }
    computeBigTextType(type) {
         return this.computeActualType(type,"bigtext");
    }
    computeDateType(type) {
         return this.computeActualType(type,"date");
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
    _handleValueChanged(newValue) {
         console.log('property-cell value changed');
    }
}

window.customElements.define('property-cell', PropertyCell);