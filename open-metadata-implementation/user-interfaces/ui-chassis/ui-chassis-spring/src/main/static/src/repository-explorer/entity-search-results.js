/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */


import { mixinBehaviors } from "../../node_modules/@polymer/polymer/lib/legacy/class.js";
import { AppLocalizeBehavior } from "../../node_modules/@polymer/app-localize-behavior/app-localize-behavior.js";
import { PolymerElement, html } from "../../node_modules/@polymer/polymer/polymer-element.js";
import { MutableData } from "../../node_modules/@polymer/polymer/lib/mixins/mutable-data.js";

import '../shared-styles.js';
import '../token-ajax.js';


class EntitySearchResults extends MutableData(PolymerElement) {

    static get template() {
        return html`

            <style include="rex-styles">

                 .column {
                   float : left;
                   width : 100%;
                 }

                 /* Clear floats after the columns */
                 .row:after {
                   content : "";
                   display : table;
                   clear   : both;
                 }

                 .multiselect-button {
                   width      : 90px;
                   height     : 25px;
                   padding    : 10px;
                   font-size  : 10px;
                   background : 'white';
                   color      : 'black';
                 }

                 paper-checkbox {
                   --paper-checkbox-checked-color: var(--egeria-primary-color);
                 }

            </style>


            <div style="height:200px; width:700px; overflow:auto;">
                <div class="row">
                    <div class="column">
                        <b>Entities</b>
                        <dom-repeat items="{{instances}}">
                            <template>
                                <div>
                                    <label style="width:50px">
                                        <paper-checkbox id={{item.entityGUID}} value="{{item.checked}}" on-change="instanceCheckboxChanged"
                                            checked="{{item.checked}}">
                                        {{item.label}} ({{item.entityGUID}}) homed in repository {{item.metadataCollectionName}}
                                    </label>
                                 </div>
                            </template>
                        </dom-repeat>
                    </div>
                </div>
            </div>

            <hr>
            <div align=center>
                <paper-button class="multiselect-button"  id = "allButton"  raised  on-click="checkAll"   > Select All  </paper-button>
                <paper-button class="multiselect-button"  id = "noneButton" raised  on-click="uncheckAll" > Clear All  </paper-button>
            </div>
            <hr>

        `; }



    static get properties() {

        return {

            // This is an array of entityDigests
            instances : {
                type : Array,
                value : undefined
            },

            selectedInstances : {
                 type : Object,
                 value : undefined
            }

        };
    }


    /*
     * Element is ready
     */
    ready() {

        // Ensure you call super.ready() first to initialise node hash...
        super.ready();

    }


    instanceCheckboxChanged(e) {

        var instance = this.instances.filter(obj => { return obj.entityGUID === e.target.id })[0];
        instance.checked = e.target.checked;

    }

    getSelectedInstances() {
        // Update selectedEntityTypes and return it to the caller
        this.selectedInstances = this.instances.filter(function(instance) {
              return instance['checked'];
            }.bind(this));
        return this.selectedInstances;
    }





    checkAll() {
        this.changeAll(true);
    }

    uncheckAll() {
        this.changeAll(false);
    }

    changeAll(checked) {

        /* To get Polymer to realise that the checked values have changed it is NOT sufficient to update
         * the members of the array - it requires an array mutation by using the Polymer set() mutation.
         * The replacement array element must bne a new object, not a modification of the existing element.
         */

        for (var inst in this.instances) {
            var newInstance = {}
            Object.assign(newInstance,this.instances[inst]);
            newInstance.checked = checked;
            this.set('instances.'+inst, newInstance);
        }



    }

}

window.customElements.define('entity-search-results', EntitySearchResults);