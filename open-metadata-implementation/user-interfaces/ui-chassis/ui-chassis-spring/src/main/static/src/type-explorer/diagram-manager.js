/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */


import { mixinBehaviors } from "../../node_modules/@polymer/polymer/lib/legacy/class.js";
import { AppLocalizeBehavior } from "../../node_modules/@polymer/app-localize-behavior/app-localize-behavior.js";
import { PolymerElement, html } from "../../node_modules/@polymer/polymer/polymer-element.js";

import '../shared-styles.js';
import '../token-ajax.js';

import './inheritance-diagram.js';
import './neighbourhood-diagram.js';

/**
*
* DiagramManager implements a web component for creation of diagrams and selection of displayed diagram
*
* It should present to the user a selector control for selecting between the available diagrams.
* The set of diagrams that is available depends on the state of the interface:
*
*   * initial load       - no diagrams are available (because there is no type information yet):
*   * types loaded       - an inheritance diagram is possible (because this can be displayed with no focus)
*   * focus selected     - either inheritance diagram (with focus) or neighbourhood diagram are available
*
* As the interface progresses through the above states, the diagram selector is populated with the options
* that are available. Once a focus type is chosen, it can be changed but there is no ability in the UI to
* return to the 'no-focus' state - i.e. types-loaded. Once any focus is selected there will therefore always
* be a focus type. Consequently, diagrams do not need to be removed from the diagram selector.
*
*/

class DiagramManager extends PolymerElement {

    static get template() {
        return html`


               <style include="shared-styles">

                   * { font-size: 12px ; font-family: sans-serif; }

                </style>

                <body>

                    <div style="padding:20px;">
                        Diagram type:

                        <select id="diagramSelector"  on-change="diagramSelectorHandler">
                            <option value="dummy" disabled selected>No diagrams possible - please load type information</option>
                            <!-- options will be added dynamically -->
                        </select>
                    </div>


                    <div id='drawingArea' style="overflow: scroll; background-color:#FFFFFF">
                    </div>
              </body>

        `; }


    static get properties() {
        return {

            //  user-specified serverName - using bi-directional databind
            selectedDiagramType: {
                type               : String,
                value              : "",
                notify             : true,
                reflectToAttribute : true
            },


            // Reference to TypeManager element which this DiagramManager depends on.
            // The TypeManager is created in the DOM of the parent and is passed in
            // once we are all initialised. This avoids any direct dependency from DiagramManager
            // on TypeManager.

            typeManager: Object,

            // diagram-manager keeps a note of focus type because the diagrams are dynamic and
            // may be created/rendered after a focus type has been selected, so the diagram-manager
            // caches the current focus so it can inform te diagrams when they are created.

            cachedFocusType: {
                type               : String,
                value              : undefined,
                notify             : true,
                reflectToAttribute : true
            },


            // Consider using polymer dom-repeat for this in a paper-dropdown
            availableDiagramTypes: {
                type: Array,
                value: () => { return [];},
                notify : true
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


    // Inter-component event handlers


    /*
     *  Inbound event: types-loaded
     */
    inEvtTypesLoaded(e) {

        // Clear the cached focusType...it may no longer be a valid type
        this.cachedFocusType = undefined;

        // Clear down the selector - removing either preliminary text or diagram types from earlier load
        this.clearDiagramSelector();

        // As soon as types are loaded it is possible to draw the inheritance diagram - it does not need
        // a type to be selected as the focus type.

        // Add it to the selector
        this.addInheritanceOption();

        // Clear the area
        var drawingArea = this.$.drawingArea;
        while (drawingArea.firstChild) {
            drawingArea.removeChild(drawingArea.firstChild);
        }

        // Create the inheritance diagram ...
        var inheritanceDiagram = document.createElement("inheritance-diagram");
        this.$.drawingArea.appendChild(inheritanceDiagram);

        // ... and automatically select it
        this.selectedDiagramType = "Inheritance";
        this.diagramSelected();


    }

    /*
     *  Inbound event: focus-changed
     */
    inEvtFocusChanged(focusType) {

        // Cache the new value of focus type
        this.cachedFocusType = focusType;

        // If not already available, add the neighbourhood option
        var nhbIdx = this.availableDiagramTypes.indexOf("Neighbourhood");

        if (nhbIdx === -1) {
            this.addNeighbourhoodOption();
        }

        // Inform the current diagram that the focus has changed...
        var diagram = this.$.drawingArea.firstChild;
        diagram.inEvtFocusChanged(focusType);
    }


    // Component logic



    // Clear down the selector and replace the 'no diagram...' entry with 'inheritance'...
    clearDiagramSelector() {

        this.availableDiagramTypes = [];
        var selector = this.$.diagramSelector;
        while (selector.firstChild) {
            selector.removeChild(selector.firstChild);
        }

    }

    /*
     *  Make inheritance diagram available and selected
     */
    addInheritanceOption() {

        // The inheritance diagram will always be the first diagram type made available,
        // so it should be the default value once it is available - i.e. select it automatically.

        this.selectedDiagramType = "Inheritance";

        this.availableDiagramTypes.push("Inheritance");

        var diagSelector = this.$.diagramSelector;
        var opt = document.createElement('option');
        opt.value = "Inheritance";
        opt.innerHTML = "Entity Inheritance";
        opt.selected = true;
        diagSelector.appendChild(opt);

    }


    /*
     *  Make neighbourhood diagram available. It is not automatically selected
     */

    addNeighbourhoodOption() {

        // The neighbourhood diagram will never be the first diagram type made available,
        // so it should be made available but not automatically replace the existing selection
        // which is probably the inheritance diagram

        this.availableDiagramTypes.push("Neighbourhood");

        var diagSelector = this.$.diagramSelector;
        var opt = document.createElement('option');
        opt.value = "Neighbourhood";
        opt.innerHTML = "Entity Neighborhood";
        opt.selected = false;
        diagSelector.appendChild(opt);

    }



    diagramSelectorHandler(e) {

        var diagramType = e.target.value;
        this.selectedDiagramType = diagramType;
        this.diagramSelected();

    }

    diagramSelected() {

        // Clear the area

        var drawingArea = this.$.drawingArea;
        while (drawingArea.firstChild) {
            drawingArea.removeChild(drawingArea.firstChild);
        }

        this.renderSelectedDiagram();
    }




    renderSelectedDiagram() {


        switch(this.selectedDiagramType) {

            case "Inheritance" :

                var inheritanceDiagram = document.createElement("inheritance-diagram");
                this.$.drawingArea.appendChild(inheritanceDiagram);

                // Consider using polymer property push-down for these...
                inheritanceDiagram.setTypeManager(this.typeManager);

                inheritanceDiagram.render(this.cachedFocusType);
                break;

           case "Neighbourhood" :

               var neighbourhoodDiagram = document.createElement("neighbourhood-diagram");
               this.$.drawingArea.appendChild(neighbourhoodDiagram);

               // Consider using polymer property push-down for these...
               neighbourhoodDiagram.setTypeManager(this.typeManager);

               neighbourhoodDiagram.render(this.cachedFocusType);
               break;

        }
    }


}

window.customElements.define('diagram-manager', DiagramManager);