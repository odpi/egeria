/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */


import { mixinBehaviors } from "../../node_modules/@polymer/polymer/lib/legacy/class.js";
import { AppLocalizeBehavior } from "../../node_modules/@polymer/app-localize-behavior/app-localize-behavior.js";
import { PolymerElement, html } from "../../node_modules/@polymer/polymer/polymer-element.js";

import '../shared-styles.js';
import '../token-ajax.js';

import './network-diagram.js';


import '@polymer/paper-dropdown-menu/paper-dropdown-menu.js';
import '@polymer/paper-item/paper-item.js';
import '@polymer/paper-listbox/paper-listbox.js';


/**
*
* DiagramManager implements a web component for creation of diagrams and selection of displayed diagram
*
* It should present to the user a selector control for selecting between the available diagrams.
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

class RexDiagramManager extends PolymerElement {

    static get template() {
        return html`


            <style  is="custom-style" include="rex-styles">

                /* diagram-manager uses narrower dropdown than other parts of Rex */
                paper-dropdown-menu {
                    width:125px;
                    height: 50px;
                    display: block;
                    font-size: 12px;
                }

            </style>

            <body>

                <div style="width=300px; height:50px; position:relative; top:0px; left:0px; padding:0px 20px;">


                    <!-- Non Polymer version                                                                                    -->
                    <!-- Diagram type:                                                                                          -->
                    <!-- <select id="diagramSelector"  on-change="diagramSelectorHandler">                                      -->
                    <!--         <option value="dummy" disabled selected>No diagrams possible - please load some data</option>  -->
                    <!--        /* options will be added dynamically */                                                         -->
                    <!-- </select>                                                                                              -->

                    <paper-dropdown-menu class="custom" label="Diagram Type" id="diagramSelector"
                        on-change="diagramSelectorHandler" noink no-animations>
                        <paper-listbox id="diagramSelectorList" slot="dropdown-content" selected="0">
                            <paper-item>none</paper-item>
                        </paper-listbox>
                    </paper-dropdown-menu>

                </div>

                <div id='drawingArea' style="width:1200px; position:absolute; top:60px; left:0px; overflow: scroll; background-color:#FFFFFF; padding:0px;">
                Drawing area below...
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

            typeManager     : Object,

            instanceRetriever     : Object,

            // Consider using polymer dom-repeat for this in a paper-dropdown
            availableDiagramTypes: {
                type: Array,
                value: () => { return [];},
                notify : true
            },


            currentDiagram: {
                type  : Object,
                value : undefined
            },

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

        this.initialiseDiagramArea();
        this.initialiseDiagramArea();
    }


    setInstanceRetriever(instanceRetriever) {
        // You actually don't need to pass the instanceRetriever arg here because the diagram-manager's
        // this.instanceRetriever will bave been set by binding. This is really an event to notify the
        // d-m that the repository-explorer-view is finishing it's ready() and that the instanceRetriever
        // can be set on the dynamically created network-diagram (or other diagram) element - it is not
        // bound using property binding.

        this.currentDiagram.setInstanceRetriever(this.instanceRetriever);
    }


    // Inter-component event handlers


    /*
     *  initialiseDiagramArea - ome time on first data load
     */
    initialiseDiagramArea() {

        // Clear down the selector - removing either preliminary text or diagram types from earlier load
        this.clearDiagramSelector();

        // As soon as types are loaded it is possible to draw the inheritance diagram - it does not need
        // a type to be selected as the focus type.

        // Add it to the selector
        this.addNetworkOption();

        // Clear the area
        var drawingArea = this.$.drawingArea;
        while (drawingArea.firstChild) {
            drawingArea.removeChild(drawingArea.firstChild);
        }

        // ... and automatically select it
        this.selectedDiagramType = "Network";
        this.diagramSelected();


    }

    /*
     *  Inbound event: graph-cleared
     */
    inEvtGraphCleared(e) {
        this.clearSelectedDiagram();
    }



    /*
     *  Inbound event: focus-entity-changed
     */
    inEvtFocusEntityChanged(entityGUID) {

        // Inform the current diagram (if any) that the focus has changed...
        // If the change of focus was not initiated from the diagram then this is when it gets to find out
        var diagram = this.currentDiagram;

        if (diagram !== undefined) {
            diagram.inEvtFocusEntityChanged(entityGUID);
        }
    }


    /*
     *  Inbound event: focus-relationship-changed
     */
    inEvtFocusRelationshipChanged(relationshipGUID) {
        // Inform the current diagram (if any) that the focus has changed...
        // If the change of focus was not initiated from the diagram then this is when it gets to find out
        var diagram = this.currentDiagram;
        if (diagram !== undefined) {
            diagram.inEvtFocusRelationshipChanged(relationshipGUID);
        }
    }

    /*
     *  Inbound event: graph-extended
     */
    inEvtGraphExtended() {
        // Inform the current diagram (if any) that the graph has been extended...
        var diagram = this.currentDiagram;
        if (diagram !== undefined) {
            diagram.inEvtGraphExtended();
        }
    }


    /*
     *  Inbound event: graph-being-reduced
     */
    inEvtGraphBeingReduced() {
        // Inform the current diagram (if any) that the graph has been reduced...
        var diagram = this.currentDiagram;
        if (diagram !== undefined) {
            diagram.inEvtGraphBeingReduced();
        }
    }


    /*
     *  Inbound event: graph-reduced
     */
    inEvtGraphReduced() {
        // Inform the current diagram (if any) that the graph has been reduced...
        var diagram = this.currentDiagram;
        if (diagram !== undefined) {
            diagram.inEvtGraphReduced();
        }
    }



    // Component logic



    // Clear down the selector and replace the 'no diagram...' entry with 'inheritance'...
    clearDiagramSelector() {

        if (this.polymer) {

            this.availableDiagramTypes = [];
            var selectorList = this.$.diagramSelectorList;
            while (selectorList.firstChild) {
                selectorList.removeChild(selectorList.firstChild);
            }

        }
        else {

            this.availableDiagramTypes = [];
            var selector = this.$.diagramSelector;
            while (selector.firstChild) {
                selector.removeChild(selector.firstChild);
            }

        }
    }

    /*
     *  Make inheritance diagram available and selected
     */
    addNetworkOption() {

        // The inheritance diagram will always be the first diagram type made available,
        // so it should be the default value once it is available - i.e. select it automatically.

        if (this.polymer) {

            this.selectedDiagramType = "Network";
            this.availableDiagramTypes.push("Network");
            var selectorList = this.$.diagramSelectorList;
            var opt = document.createElement('paper-item');
            opt.value = "Network";
            opt.innerHTML = "Network Diagram";
            opt.selected = true;
            selectorList.appendChild(opt);

        }
        else {

            this.selectedDiagramType = "Network";
            this.availableDiagramTypes.push("Network");
            var diagSelector = this.$.diagramSelector;
            var opt = document.createElement('option');
            opt.value = "Network";
            opt.innerHTML = "Network Diagram";
            opt.selected = true;
            diagSelector.appendChild(opt);

        }
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

            case "Network" :

                var networkDiagram = document.createElement("network-diagram");
                this.$.drawingArea.appendChild(networkDiagram);
                this.currentDiagram = networkDiagram;
                break;

        }

    }

    updateSelectedDiagram() {

        switch(this.selectedDiagramType) {

            case "Network" :
                var networkDiagram = this.$.drawingArea.firstChild;
                networkDiagram.update();
                break;

        }
    }


    clearSelectedDiagram() {

        switch(this.selectedDiagramType) {

            case "Network" :
                var networkDiagram = this.$.drawingArea.firstChild;
                networkDiagram.clearGraph();
                break;

        }
    }


}

window.customElements.define('rex-diagram-manager', RexDiagramManager);