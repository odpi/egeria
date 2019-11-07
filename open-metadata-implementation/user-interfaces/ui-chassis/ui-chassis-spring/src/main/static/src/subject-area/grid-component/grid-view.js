/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import '@polymer/iron-form/iron-form.js';
import '@polymer/iron-a11y-keys/iron-a11y-keys.js';
import './property-row.js';
import './header-columns.js';
import '../../shared-styles.js';

import { PolymerElement, html } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/vaadin-button.js";

class GridView extends PolymerElement {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
              display: inline-block;
               padding: 10px 20px;
          }
           .table_cell {
                display:table-cell;
                width:15em;
                border-style: solid;
                border-color: LightGrey;
           }
           .table {
                display:table;
                 border-style: solid;
                 border-color: dark-grey;
                 max-width: 100%;
                 overflow-x: scroll;
           }
           .table_header {
                display:table-header-group;
                width:15em;
                border-style: solid;
                border-color: DarkGrey;
           }
           .table_body {
                display:table-row-group;
           }

      </style>
           <h2>{{name}} Grid - work in progress</h2>
           <label for="newName">Add a new {{name}} with name:</label>
           <input is="paper-input" id="newName" type="text" name="name">
           <vaadin-button on-click="_newName">+</vaadin-button>
           <br>
           <div class="table">
              <div class="table_header">
                <dom-repeat items="{{defs}}" mutable-data>
                    <template><div class="table_cell">{{item.label}}</div></template>
              <!--     <header-columns def="[[item]]"></header-columns> -->
                </dom-repeat>
              </div>
              <div class='table_body'>
              <dom-repeat items={{rows}} mutable-data>
                <template>
                      <property-row row=[[item]]></property-row>
                </template>
              </dom-repeat>
             <!-- <property-row row=[[emptyRow]]></property-row> -->
               </div>
           </div>
    `;
    }

    static get properties() {
        return {
            defs: {
               type: Array,
               notify: true,
                observer: '_handleDefsChanged'
            },   
            // all model instances
            modelInstances:{
                type: Array,
                notify: true,
                value: function() { return []; },
                observer: '_handleViewModelInstanceChanged'
            },
            rows: {
               type: Array,
               notify: true,
               computed: '_computeRows(defs,modelInstances)'
            },
            component: {
              type: String,
              notify: true
            },
            name: {
              type: String,
              notify: true
            }
      };
    }
    ready(){
        super.ready();
    }
    _newName(e) {
        var nameValue = this.$.newName.value;
        var eventName = 'view-new-' + this.name.toLowerCase() + '-glossary-name';
        this.dispatchEvent(new CustomEvent(eventName, {
                          bubbles: true,
                          composed: true,
                          detail: nameValue }));
    }
     /**
     * Take the definition of the properties ( the names of the fields in a artifact along with there type etc) and
     * fold in the artifact values and guids to form propertyArrays. A row is the array that
     * drives the row processing.
     *
     * @param passedPropertyDefinitions an array of fields, each filed has metadata information like its label, name and type
     * @param modelInstance the artifact
     * @return return the row - arrays of the passed property definitions embellished with values from the matching artifact field.
     */
    _computeRows(passedDefs,passedModelInstances) {
       var computedRows =[];
       if (passedDefs) {
          if (passedModelInstances) {
            var modelInstanceCount;
            for (modelInstanceCount=0;modelInstanceCount<passedModelInstances.length;modelInstanceCount++) {
              // compute the row and push into rows
              let row = this._computeRow(passedDefs,passedModelInstances[modelInstanceCount]);
              // we need to clone the array - using a deep copy to create a new reference.
              let cloneArray = JSON.parse(JSON.stringify(row))
              computedRows.push(cloneArray);
            }
          }
       }
       return computedRows;
    }
        /**
         * Compute a row for the UI from definitions and a modelInstance
         * The definition contains a name and type, this method adds in a guid and value from the
         * modelInstance (the actual data).
         */
        _computeRow(defs,modelInstance) {
            var guid;
            if (modelInstance.guid) {
                // if this is a system Attributes in which case we have the guid as a property
                guid = modelInstance.guid;
            } else if (modelInstance.systemAttributes) {
                // if this is a top level artifact then the guid will be in the system attributes
                guid = modelInstance.systemAttributes.guid;
            }
            var propertyNumber;
            // initialise row in local scope
            let row =[];
            for (propertyNumber=0; propertyNumber<defs.length;propertyNumber++) {
                // copy over existing properties
                let valuedProp = defs[propertyNumber];
                var key = valuedProp.name;

                // Handle system attributes separately
                if ("systemAttributes" == valuedProp.object) {
                      if (modelInstance.systemAttributes[key]) {
                           valuedProp.value =modelInstance.systemAttributes[key];
                      }
                } else {
                    // find the artifact property that matches the property.
                    if (modelInstance[key]) {
                        // add in the value from the artifact if there is one
                        valuedProp.value = modelInstance[key];
                    }
                    // storing the guid in the item when there is not a value allows us to still have the guid when we update on this property
                    if (guid) {
                        valuedProp.guid = guid;
                    }
                }
                row.push(valuedProp);
            }
            return row;
        }
        _handleViewModelInstanceChanged(newValue) {
            console.log('_handleViewModelInstanceChanged');
        }
        _handleDefsChanged(newValue) {
            console.log('grid view _handleDefsChanged');
        }
}

window.customElements.define('grid-view', GridView);