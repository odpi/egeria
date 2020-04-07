/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */


import { mixinBehaviors } from "../../node_modules/@polymer/polymer/lib/legacy/class.js";
import { AppLocalizeBehavior } from "../../node_modules/@polymer/app-localize-behavior/app-localize-behavior.js";
import { PolymerElement, html } from "../../node_modules/@polymer/polymer/polymer-element.js";
import '../shared-styles.js';
import '../token-ajax.js';

/**
*
* FocusManager is the implementation of a web component for the presentation and selection of focus and view types for the type explorer UI component.
* The view type is always actually a pair (of view type and view category) because it could be either a relationship or classification type.
*
* The FocusManager component API has functions to clear all types and load all types.
* The load all types operation will refer to the TypeManager to access the current types, and add them to the selectors. It also initialises
* the focus and view types.
* The clear all types operation will drop all tyeps from the selectors and reset the focus and view types.
* The clear and load all types operations are invoked on receipt of the typesLoaded event.
*
* The FocusManager component is the only component that csn actually change the focus or view. Other components have controls that the user
* can activate to initiate a focus change or view change - but these merely generate events that ultimately are handled by the FocusManager.
* The changeFocus and changeView functions are the gatekeepers of these requests, and the FocusManager will generate the FocusChanged and
* ViewChanged events on completion.
*
* Because the user can switch focus or view from other components (e.g. by clicking on a node in a diagram) the FocusManager must listen for
* events that are requesting a change of focus or view (change-focus, change-view). WHen handling those events the FocusManager must ensure that
* the selectors are updated to reflect the newly selected type. The listening and event handling is actually confined to the controller so this
* focus-manager component only needs to support an API for externally requested changeFocus and changeView requests.
*
*
*/

class FilterManager extends PolymerElement {

    static get template() {
        return html`

            <style is="custom-style" include="rex-styles">

            </style>

            <body>

                <div style="padding:0px;">

                    <!-- Non-polymer version ...                                                                                 -->
                    <!--                                                                                                         -->
                    <!-- Entity Types:                                                                                           -->
                    <!--                                                                                                         -->
                    <!-- <select id="entityTypeSelector" style="width: 300px; float:left; " on-change="entitySelectorHandler">   -->
                    <!--     <option value="dummy" disabled selected>No types to display</option>                                -->
                    <!--     /*  options will be added dynamically */                                                            -->
                    <!-- </select>                                                                                               -->
                    <!--                                                                                                         -->
                    <!--  <p>                                                                                                    -->
                    <!--  Relationship Types:                                                                                    -->
                    <!--                                                                                                         -->
                    <!-- <select id="relationshipTypeSelector" style="width: 300px; float:right;  left: 150px"                   -->
                    <!--             on-change="relationshipSelectorHandler">                                                    -->
                    <!--      <option value="dummy" disabled selected>No types to display</option>                               -->
                    <!--      /*  options will be added dynamically */                                                           -->
                    <!-- </select>                                                                                               -->
                    <!--                                                                                                         -->
                    <!-- <p>                                                                                                     -->
                    <!-- Classification Types:                                                                                   -->
                    <!--                                                                                                         -->
                    <!-- <select id="classificationTypeSelector" style="width: 300px; float:right;  left: 150px"                 -->
                    <!--             on-change="classificationSelectorHandler">                                                  -->
                    <!--     <option value="dummy" disabled selected>No types to display</option>                                -->
                    <!--     /*  options will be added dynamically */                                                            -->
                    <!-- </select>                                                                                               -->

                    <!-- Polymer version -->
                    <paper-dropdown-menu class="custom" label="Entity Types" id="entityTypeSelector"
                         on-change="entitySelectorChangeHandler" noink no-animations
                         on-iron-select="entitySelectorHandler">
                        <paper-listbox id="entityTypeSelectorList" slot="dropdown-content" attrForSelected="value" selected="0" >
                            <paper-item>No types to display</paper-item>
                        </paper-listbox>
                    </paper-dropdown-menu>

                    <paper-dropdown-menu class="custom" label="Relationship Types" id="relationshipTypeSelector"
                        on-change="relationshipSelectorChangeHandler" noink no-animations
                        on-iron-select="relationshipSelectorHandler">
                        <paper-listbox id="relationshipTypeSelectorList" slot="dropdown-content" attrForSelected="value" selected="0">
                            <paper-item>No types to display</paper-item>
                        </paper-listbox>
                    </paper-dropdown-menu>

                    <paper-dropdown-menu class="custom" label="Classification Types" id="classificationTypeSelector"
                         on-change="classificationSelectorChangeHandler" noink no-animations
                         on-iron-select="classificationSelectorHandler" disabled>
                         <paper-listbox id="classificationTypeSelectorList" slot="dropdown-content" attrForSelected="value" selected="0">
                             <paper-item>No types to display</paper-item>
                         </paper-listbox>
                    </paper-dropdown-menu>

                </div>

             </body>
        `; }



    static get properties() {

        return {

            selectedType: {
                type    : String,
                value   : undefined
            },

             selectedCategory: {
                 type    : String,
                 value   : undefined
             },

             typeManager: Object,

             polymer : {
                 type  : Boolean,
                 value : true
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


    getTypeManager() {
      return this.typeManager;
    }


    // Inter-component event handlers


    /*
     *  Inbound event: types-loaded
     */
    inEvtTypesLoaded() {
        this.populateSelectors();
    }

     /*
      *  Outbound event: type-selected
      */
     outEvtTypeSelected(category, typeName) {
         var customEvent = new CustomEvent('type-selected', { bubbles: true, composed: true, detail: {source: "filter-manager", selectedCategory: category, selectedType: typeName} });
         this.dispatchEvent(customEvent);
     }


     // UI event handlers

    entitySelectorChangeHandler(e) {
        //console.log("Entity selector content changed - do nothing");
    }

    entitySelectorHandler(e) {
        if (polymer) {
            var typeName = e.target.selectedItem.value;
            if (typeName !== "none")
                this.changeTypeSelected("Entity", typeName);
        }
        else {
            var typeName = e.target.value;
            this.changeTypeSelected("Entity", typeName);
        }
    }


    relationshipSelectorHandler(e) {
        //console.log("Relationship selector content changed - do nothing");
    }

    relationshipSelectorHandler(e) {
        if (polymer) {
            var typeName = e.target.selectedItem.value;
            if (typeName !== "none")
                this.changeTypeSelected("Relationship", typeName);
        }
        else {
            var typeName = e.target.value;
            this.changeTypeSelected("Relationship", typeName);
        }
    }

    classificationSelectorChangeHandler(e) {
        //console.log("Classification selector content changed - do nothing");
    }

    classificationSelectorHandler(e) {
        if (polymer) {
            var typeName = e.target.selectedItem.value;
            if (typeName !== "none")
                this.changeTypeSelected("Classification", typeName);
        }
        else {
            var typeName = e.target.value;
            this.changeTypeSelected("Classification", typeName);
        }
    }

    getSelection() {
        var selection = {};
        selection.category = this.selectedCategory;
        selection.typeName = this.selectedType;
        return selection;
    }

    /*
     *  Populate the type selectors using information from the TypeManager.
     */
    populateSelectors() {

        // Clear selections
        this.resetTypeSelectors();

        // Empty the selectors before adding new types...
        this.prepareSelectors();

        // Add entity type names to entity type selectors
        var entities = this.typeManager.getEntityTypes();
        var entityTypesUnsorted = Object.keys(entities);
        var entityTypesSorted = entityTypesUnsorted.sort();

        entityTypesSorted.forEach(entityExpl => {
            var typeName = entities[entityExpl].entityDef.name
            if (this.polymer) {
                this.addTypeToSelector("entityTypeSelectorList", typeName);
            }
            else {
                this.addTypeToSelector("entityTypeSelector", typeName);
            }
        });

        // Add relationship type names to relationship type selector
        var relationships = this.typeManager.getRelationshipTypes();
        var relationshipTypesUnsorted = Object.keys(relationships);
        var relationshipTypesSorted = relationshipTypesUnsorted.sort();

        relationshipTypesSorted.forEach(relationshipExpl => {
            var typeName = relationships[relationshipExpl].relationshipDef.name;
            if (polymer) {
                this.addTypeToSelector("relationshipTypeSelectorList", typeName);
            }
            else {
                this.addTypeToSelector("relationshipTypeSelector", typeName);
            }
        });

        // Add classification type names to classification type selector
        var classifications = this.typeManager.getClassificationTypes();
        var classificationTypesUnsorted = Object.keys(classifications);
        var classificationTypesSorted = classificationTypesUnsorted.sort();

        classificationTypesSorted.forEach(classificationExpl => {
            var typeName = classifications[classificationExpl].classificationDef.name
            if (polymer) {
                this.addTypeToSelector("classificationTypeSelectorList", typeName);
            }
            else {
                this.addTypeToSelector("classificationTypeSelector", typeName);
            }
        });
    }

    /*
     * Helper function to add a type to the specified type selector
     */
    addTypeToSelector(selectorName, typeName) {

        if (this.polymer) {

            var selectorList = this.shadowRoot.getElementById(selectorName);
            var opt = document.createElement('paper-item');
            opt.value = typeName;
            opt.innerHTML = typeName;
            selectorList.appendChild(opt);

        }
        else {

            var select = this.$[selectorName];
            var opt = document.createElement('option');
            opt.value = typeName;
            opt.innerHTML = typeName;
            select.appendChild(opt);

        }
    }

    /*
     *  Clear all the type selectors
     */
    prepareSelectors() {

        this.selectedType     = undefined;
        this.selectedCategory = undefined;

        if (this.polymer) {

            this.prepareSelector("entityTypeSelectorList");
            this.prepareSelector("relationshipTypeSelectorList");
            this.prepareSelector("classificationTypeSelectorList");

        }
        else {

            this.prepareSelector("entityTypeSelector");
            this.prepareSelector("relationshipTypeSelector");
            this.prepareSelector("classificationTypeSelector");

        }
    }

    prepareSelector(selectorName) {

        if (this.polymer) {

             // Clear down the selectors and replace the 'no types' entry with 'select a type'...
             //var selector = this.$[selectorName];
             var selectorList = this.shadowRoot.getElementById(selectorName);
             while (selectorList.firstChild) {
                 selectorList.removeChild(selectorList.firstChild);
             }
             var opt = document.createElement('paper-item');
             opt.value = "none";
             opt.innerHTML = "Restrict search to a selected type...";
             opt.disabled = false;
             opt.selected = true;
             selectorList.appendChild(opt);

        }
        else {

            // Clear down the selectors and replace the 'no types' entry with 'select a type'...
            var selector = this.$[selectorName];
            while (selector.firstChild) {
                selector.removeChild(selector.firstChild);
            }
            var opt = document.createElement('option');
            opt.value = "none";
            opt.innerHTML = "Restrict search to a selected type...";
            opt.disabled = false;
            opt.selected = true;
            selector.appendChild(opt);

        }
    }


    changeTypeSelected(category, typeName) {
        // Clear the type selectors for the other categories
        switch (category) {
            case "Entity":
                this.resetRelTypeSelector();
                this.resetClsTypeSelector();
                break;
            case "Relationship":
                this.resetEntTypeSelector();
                this.resetClsTypeSelector();
                break;
            case "Classification":
                this.resetEntTypeSelector();
                this.resetRelTypeSelector();
                break;
        }

        // Remember the new setting
        this.selectedType     = typeName;
        this.selectedCategory = category;
        // Issue the event
        this.outEvtTypeSelected(category, typeName);
    }


    /*
     *  A reset of a view selector sets the selected value to the 'nothing-selected' value
     *  which is always "none" and has the display text "Please select a type".
     *
     *  A reset does not clear the content of the selector.
     */
    resetTypeSelectors() {
      this.resetEntTypeSelector();
      this.resetRelTypeSelector();
      this.resetClsTypeSelector();
    }

    resetClsTypeSelector() {

        if (this.polymer) {

            // Reset the classification type selector
            var select = this.$.classificationTypeSelectorList;
            select.selected = 0;

        }
        else {

            // Reset the classification type selector
            var select = this.$.classificationTypeSelector;
            select.value = "none";

        }
    }

    resetRelTypeSelector() {

        if (this.polymer) {

            // Reset the relationship type selector
            var select = this.$.relationshipTypeSelectorList;
            select.selected = 0;

        }
        else {

            // Reset the relationship type selector
            var select = this.$.relationshipTypeSelector;
            select.value = "none";

        }
    }

    resetEntTypeSelector() {

        if (this.polymer) {

            // Reset the entity type selector
            var select = this.$.entityTypeSelectorList;
            select.selected = 0;

        }
        else {

            // Reset the entity type selector
            var select = this.$.entityTypeSelector;
            select.value = "none";

        }
    }
}

window.customElements.define('filter-manager', FilterManager);