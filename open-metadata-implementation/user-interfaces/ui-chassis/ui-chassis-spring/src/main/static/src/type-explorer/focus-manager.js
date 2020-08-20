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
* The clear all types operation will drop all types from the selectors and reset the focus and view types.
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

class FocusManager extends PolymerElement {

    static get template() {
        return html`

             <div style="padding:20px;">

             Focus Entity Type:

             <select id="entityTypeSelector" style="width: 300px; float:right;  left: 150px" on-change="focusSelectorHandler">
                 <option value="dummy" disabled selected>No types to display</option>
                 <!-- options will be added dynamically -->
             </select>

             <p>
             Relationship Types:

             <select id="relationshipTypeSelector" style="width: 300px; float:right;  left: 150px" on-change="viewSelectorHandler">
                 <option value="dummy" disabled selected>No types to display</option>
                 <!-- options will be added dynamically -->
             </select>

             <p>
             Classification Types:

             <select id="classificationTypeSelector" style="width: 300px; float:right;  left: 150px" on-change="viewSelectorHandler">
                 <option value="dummy" disabled selected>No types to display</option>
                 <!-- options will be added dynamically -->
             </select>

             </div>
        `; }



    static get properties() {

        return {


            selectedFocusType: {
                type    : String,
                value   : undefined
            },

            selectedViewType: {
                type    : String,
                value   : undefined
            },

             selectedViewCategory: {
                 type    : String,
                 value   : undefined
             },

             typeManager: Object

        };
    }

    /*
     * Element is ready
     */
    ready() {
        // Ensure you call super.ready() first to initialise node hash...
        super.ready();
    }


    // Inter-component event handlers


    /*
     *  Inbound event: types-loaded
     */
    inEvtTypesLoaded() {
        this.populateSelectors();
    }

     /*
      *  Inbound event: change-focus
      */
     inEvtChangeFocus(focusType) {
         this.changeFocus(focusType);
     }


     /*
      *  Inbound event: change-view
      */
     inEvtChangeView(viewCategory, viewType) {
         this.changeView(viewCategory, viewType);
     }

     /*
      *  Outbound event: focus-changed
      */
     outEvtFocusChanged(typeName) {
         var customEvent = new CustomEvent('focus-changed', { bubbles: true, composed: true, detail: {source: "focus-manager", focusType: typeName} });
         this.dispatchEvent(customEvent);
     }

     /*
      *  Outbound event: view-changed
      */
     outEvtViewChanged(category, typeName) {
         var customEvent = new CustomEvent('view-changed', { bubbles: true, composed: true, detail: {source: "focus-manager", viewCategory: category, viewType: typeName} });
         this.dispatchEvent(customEvent);
     }


     // UI event handlers


    focusSelectorHandler(e) {

        var typeName = e.target.value;
        this.changeFocus(typeName);
    }


    viewSelectorHandler(e) {

        var target = e.target;
        var category = undefined;
        if (e.target.id === "relationshipTypeSelector")
            category = "Relationship";
        else
            category = "Classification";
        var typeName = e.target.value;

        this.changeView(category, typeName);
    }



    // Component logic


    /*
     *  Populate the type selectors using information from the TypeManager.
     */
    populateSelectors() {

       // Empty the selectors before adding new types...
       this.prepareSelectors();


        // Add entity type names to entity type selectors
        var entities = this.typeManager.getEntities();
        var entityTypesUnsorted = Object.keys(entities);
        var entityTypesSorted = entityTypesUnsorted.sort();

        entityTypesSorted.forEach(entityExpl => {
            var typeName = entities[entityExpl].entityDef.name
            this.addTypeToSelector("entityTypeSelector", typeName);
        });


        // Add relationship type names to relationship type selector
        var relationships = this.typeManager.getRelationships();
        var relationshipTypesUnsorted = Object.keys(relationships);
        var relationshipTypesSorted = relationshipTypesUnsorted.sort();

        relationshipTypesSorted.forEach(relationshipExpl => {
            var typeName = relationships[relationshipExpl].relationshipDef.name
            this.addTypeToSelector("relationshipTypeSelector", typeName);
        });


        // Add classification type names to classification type selector
        var classifications = this.typeManager.getClassifications();
        var classificationTypesUnsorted = Object.keys(classifications);
        var classificationTypesSorted = classificationTypesUnsorted.sort();

        classificationTypesSorted.forEach(classificationExpl => {
            var typeName = classifications[classificationExpl].classificationDef.name
            this.addTypeToSelector("classificationTypeSelector", typeName);
        });


    }

    /*
     * Helper function to add a type to the specified type selector
     */
    addTypeToSelector(selectorName, typeName) {

        var select = this.$[selectorName];
        var opt = document.createElement('option');
        opt.value = typeName;
        opt.innerHTML = typeName;
        select.appendChild(opt);
    }

    /*
     *  Clear all the type selectors
     */
    prepareSelectors() {

        this.selectedFocusType    = undefined;
        this.selectedViewType     = undefined;
        this.selectedViewCategory = undefined;

        this.prepareSelector("entityTypeSelector");
        this.prepareSelector("relationshipTypeSelector");
        this.prepareSelector("classificationTypeSelector");


    }

    prepareSelector(selectorName) {
        // Clear down the selectors and replace the 'no types' entry with 'select a type'...
        var selector = this.$[selectorName];
        while (selector.firstChild) {
            selector.removeChild(selector.firstChild);
        }
        var opt = document.createElement('option');
        opt.value = "none";
        opt.innerHTML = "Select a type";
        opt.disabled = true;
        opt.selected = true;
        selector.appendChild(opt);
    }


    changeFocus(typeName) {

        // This function is called:
        //   EITHER - locally by the focus selector event handler in response to a local operation - the user has clicked the focus selector.
        //   OR     -  on receipt of event arising from a request (from another component) to change the current focus.
        // In both cases the FocusManager needs to update its own selectors and issue the focusChanged event.
        //
        // In the case of a local request (i.e. the focus selector is driving the change), the focus selector will already be displaying
        // the requested type, and does not need to be updated. However, the view selectors need to be cleared.
        //
        // In the case of a remotely requested change the focus selector needs updating. To simplify things it is updated in both the
        // local and remote cases.
        //
        // Once the selectors are updated, the FocusManager can issue the appropriate event.

        // Clear the view selectors
        this.resetViewSelectors();

        // Update the focus selector
        var select = this.$.entityTypeSelector;
        select.value = typeName;


        // Issue the event
        this.outEvtFocusChanged(typeName);

    }


    /*
     *  A reset of a view selector sets the selected value to the 'nothing-selected' value
     *  which is always "none" and has the display text "Please select a type".
     *
     *  A reset does not clear the content of the selector.
     */
    resetViewSelectors() {

      this.resetRelTypeSelector();
      this.resetClsTypeSelector();

    }

    resetClsTypeSelector() {

      // Reset the classification view selector
      var select = this.$.classificationTypeSelector;
      select.value = "none";

    }

    resetRelTypeSelector() {

      // Reset the relationship view selector
      var select = this.$.relationshipTypeSelector;
      select.value = "none";

    }





    changeView(category, typeName) {


        // This function is called:
        // EITHER - directly by a view selector event handler in response to a local operation - the user has clicked a view selector.
        // OR     -  on receipt of request (from another component) to change the current view.
        // In both cases the FocusManager needs to update its own selectors and issue the viewChanged event.
        // In the case of a local request (i.e. a view selector is driving the change), that view selector will already be displaying
        // the requested type, and does not need to be updated. However, the other view selector need to be cleared.
        // (The focus selector is not changed).
        // Once the selectors are updated, the FocusManager can issue the appropriate event.

        var relSelector = this.$.relationshipTypeSelector;
        var clsSelector = this.$.classificationTypeSelector;

        if (category === "Relationship") {

            // Reset the classification view selector
            this.resetClsTypeSelector();

            // Update the relationship selector
            relSelector.value = typeName;
        }

        if (category === "Classification" ) {

            // Reset the relationship view selector
            this.resetRelTypeSelector();

           // Update the classification selector
           clsSelector.value = typeName;
        }

         // Issue the event
        this.outEvtViewChanged(category, typeName);


        // It is interesting to note and be aware that the event will be sent and processed (received by the controller and
        // follow-on actions dispatched - before the selectors are actually visually updated. This may not be what you would
        // have expected.

    }







}

window.customElements.define('focus-manager', FocusManager);