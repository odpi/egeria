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
* It should present to the user a selector control for selecting between the avaiulable diagrans.
* The set of diagrams that is available depends on the state of the interface:
*
*   * initial load       - no diagrams are available (because there is no type information yet):
*   * types loaded       - an inheritance diagram is possible (because this can be displayed with no focus)
*   * focus selected     - either inheritance diagram (with focus) or neighbourhood diagram are avauilable
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

                    <div>

                    <p>
                    Diagram type:
                    <p>
                    <select id="diagramSelector"  on-change="diagramSelectorHandler">
                        <option value="dummy" disabled selected>No diagrams possible - please load type information</option>
                        <!-- options will be added dynamically -->
                    </select>

                    </div>

                    <div id='drawingArea'>
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


            availableDiagramTypes: {     // TODO - you could polymerize this fully by binding it to dom-repeat in paper-dropdown
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
        console.log("diagram-manager ready");   // TODO - remove
    }


    // Inter-component event handlers


    /*
     *  Inbound event: types-loaded
     */
    inEvtTypesLoaded(e) {

        // If not already available, add the inheritance option
        var inhIdx = this.availableDiagramTypes.indexOf("Inheritance");
        console.log("diagram-manager: inhIdx is "+inhIdx);   // TODO - remove
        if (inhIdx === -1) {
            // Clear down the preliminary text
            this.clearDiagramSelector();
            this.addInheritanceOption();
        }

        // As soon as types are loaded it is possible to draw the inheritance diagram - it does not need
        // a type to be selected as the focus type.

        // Create it...
        console.log("ID: types-loaded, auto-create the inheritance diagram");  // TODO - remove

        // Clear the area
        console.log("ID: types-loaded, clear");
        var drawingArea = this.$.drawingArea;
        while (drawingArea.firstChild) {
            drawingArea.removeChild(drawingArea.firstChild);
        }
        // Add the inheritance diagram
        console.log("ID: types-loaded, create");   // TODO - remove
        var inheritanceDiagram = document.createElement("inheritance-diagram");
        this.$.drawingArea.appendChild(inheritanceDiagram);

        // ... then select it
        console.log("ID: types-loaded, select");   // TODO - remove
        this.selectedDiagramType = "Inheritance";
        this.diagramSelected();


    }

    /*
     *  Inbound event: focus-changed
     */
    inEvtFocusChanged(focusType) {

        // If not already available, add the neighbourhood option
        var nhbIdx = this.availableDiagramTypes.indexOf("Neighbourhood");
        console.log("diagram-manager: nhbIdx is "+nhbIdx);   // TODO - remove
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
        opt.innerHTML = "Inheritance";
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
        opt.innerHTML = "Neighbourhood";
        opt.selected = false;
        diagSelector.appendChild(opt);
    }




    diagramSelectorHandler(e) {

        var diagramType = e.target.value;
        console.log("diagramSelectorHandler called, diagramType "+diagramType);   // TODO - remove
        this.selectedDiagramType = diagramType;
        this.diagramSelected();

    }

    diagramSelected() {

        // Clear the area
        console.log("ID: diagram-selected, clear");   // TODO - remove
        var drawingArea = this.$.drawingArea;
        while (drawingArea.firstChild) {
            drawingArea.removeChild(drawingArea.firstChild);
        }

        console.log("ID: diagram-selected, render");   // TODO - remove
        this.renderSelectedDiagram();
    }




    renderSelectedDiagram() {



        switch(this.selectedDiagramType) {

            case "Inheritance" :
                console.log("ID: render inheritance diagram");   // TODO - remove

                console.log("ID: render create");   // TODO - remove
                var inheritanceDiagram = document.createElement("inheritance-diagram");
                this.$.drawingArea.appendChild(inheritanceDiagram);
                console.log("ID: render set type manager");   // TODO - remove
                inheritanceDiagram.setTypeManager(this.typeManager);  // TODO - use Polymer property push down instead
                console.log("ID: render inh-diagr");   // TODO - remove
                inheritanceDiagram.render();
                break;

           case "Neighbourhood" :
               console.log("Draw the neighbourhood diagram");   // TODO - remove
               var neighbourhoodDiagram = document.createElement("neighbourhood-diagram");
               this.$.drawingArea.appendChild(neighbourhoodDiagram);
               neighbourhoodDiagram.setTypeManager(this.typeManager);  // TODO - use Polymer property push down instead
               neighbourhoodDiagram.render();
               break;

           default :
               console.log("ERROR - I should not be here");
        }
    }


}

window.customElements.define('diagram-manager', DiagramManager);