/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */


import { mixinBehaviors } from "../../node_modules/@polymer/polymer/lib/legacy/class.js";
import { AppLocalizeBehavior } from "../../node_modules/@polymer/app-localize-behavior/app-localize-behavior.js";
import { PolymerElement, html } from "../../node_modules/@polymer/polymer/polymer-element.js";
import { MutableData } from "../../node_modules/@polymer/polymer/lib/mixins/mutable-data.js";

import '../shared-styles.js';
import '../token-ajax.js';


class TraversalFilters extends MutableData(PolymerElement) {

    static get template() {
        return html`

            <style include="rex-styles">


                 .column {
                   float: left;
                   width: 33.33%;
                 }

                 /* Clear floats after the columns */
                 .row:after {
                   content: "";
                   display: table;
                   clear: both;
                 }

                 .multiselect-button {
                   width:90px;
                   height:25px;
                   padding:10px;
                   font-size:10px;
                   background:'white';
                   color:'black';
                 }

                 paper-checkbox {
                   --paper-checkbox-checked-color: var(--egeria-primary-color);
                 }

            </style>


            <div style="height:150px; width:700px; overflow: auto">

                <div class="row" style="width:700px;">
                    <div class="column">
                        <b>Entity Types</b>
                        <dom-repeat items="{{entityTypes}}">
                            <template>
                                <div>
                                    <label style="width:50px">
                                        <paper-checkbox id={{item.name}}
                                               value="{{item.checked}}"
                                               on-change="entityFilterChanged"
                                               checked="{{item.checked}}">
                                        {{item.name}} ({{item.count}})
                                    </label>
                                 </div>
                            </template>
                        </dom-repeat>
                    </div>

                    <div class="column">
                        <b>Relationship Types</b>
                        <dom-repeat items="{{relationshipTypes}}">
                            <template>
                                <div>
                                    <label style="width:50px">
                                        <paper-checkbox id={{item.name}}
                                               value="{{item.checked}}"
                                               on-change="relationshipFilterChanged"
                                               checked="{{item.checked}}">
                                        {{item.name}} ({{item.count}})
                                    </label>
                                </div>
                            </template>
                        </dom-repeat>
                    </div>

                    <div class="column">
                        <b>Classification Types</b>
                        <dom-repeat items="{{classificationTypes}}">
                            <template>
                                <div>
                                    <label style="width:50px">
                                        <paper-checkbox id={{item.name}}
                                               value="{{item.checked}}"
                                               on-change="classificationFilterChanged"
                                               checked="{{item.checked}}">
                                        {{item.name}} ({{item.count}})
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

            // entityTypes is the map from the preTraversal object in i-r
            entityTypes : {
                type : Object,
                value : undefined
            },

            selectedEntityTypes : {
                 type : Object,
                 value : undefined
            },

            // relationshipTypes is the map from the preTraversal object in i-r
            relationshipTypes : {
                 type : Object,
                 value : undefined
            },

            selectedRelationshipTypes : {
                 type : Object,
                 value : undefined
            },

           // classificationTypes is the map from the preTraversal object in i-r
           classificationTypes : {
                type : Object,
                value : undefined
            },

            selectedClassificationTypes : {
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

    entityFilterChanged(e) {
        var entityType = this.entityTypes.filter(obj => { return obj.name === e.target.id  })[0];
        entityType.checked = e.target.checked;
    }

    getSelectedEntityTypes() {
        // Update selectedEntityTypes and return it to the caller
        this.selectedEntityTypes = this.entityTypes.filter(function(entityType) {
              return entityType['checked'];
            }.bind(this));
        return this.selectedEntityTypes;
    }

    relationshipFilterChanged(e) {
        var relationshipType = this.relationshipTypes.filter(obj => { return obj.name === e.target.id  })[0];
        relationshipType.checked = e.target.checked;
    }

    getSelectedRelationshipTypes() {
        // Update selectedRelationshipTypes and return it to the caller
        this.selectedRelationshipTypes = this.relationshipTypes.filter(function(relationshipType) {
              return relationshipType['checked'];
            }.bind(this));
        return this.selectedRelationshipTypes;
    }

    classificationFilterChanged(e) {
        var classificationType = this.classificationTypes.filter(obj => { return obj.name === e.target.id  })[0];
        classificationType.checked = e.target.checked;
    }

    getSelectedClassificationTypes() {
        // Update selectedClassificationTypes and return it to the caller
        this.selectedClassificationTypes = this.classificationTypes.filter(function(classificationType) {
              return classificationType['checked'];
            }.bind(this));
        return this.selectedClassificationTypes;
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

        for (var etype in this.entityTypes) {
            var newEntity = {}
            Object.assign(newEntity,this.entityTypes[etype]);
            newEntity.checked = checked;
            this.set('entityTypes.'+etype, newEntity);
        }

        for (var rtype in this.relationshipTypes) {
            var newRelationship = {}
            Object.assign(newRelationship,this.relationshipTypes[rtype]);
            newRelationship.checked = checked;
            this.set('relationshipTypes.'+rtype, newRelationship);
        }

        for (var ctype in this.classificationTypes) {
            var newClassification = {}
            Object.assign(newClassification,this.classificationTypes[ctype]);
            newClassification.checked = checked;
            this.set('classificationTypes.'+ctype, newClassification);
        }

    }

}

window.customElements.define('traversal-filters', TraversalFilters);