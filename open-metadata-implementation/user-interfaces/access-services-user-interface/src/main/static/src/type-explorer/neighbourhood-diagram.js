/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */


import { mixinBehaviors } from "../../node_modules/@polymer/polymer/lib/legacy/class.js";
import { AppLocalizeBehavior } from "../../node_modules/@polymer/app-localize-behavior/app-localize-behavior.js";
import { PolymerElement, html } from "../../node_modules/@polymer/polymer/polymer-element.js";
import "../../node_modules/@polymer/paper-checkbox/paper-checkbox.js";
import "../../node_modules/@polymer/paper-button/paper-button.js";
import "../../node_modules/@polymer/paper-input/paper-input.js";
import '../shared-styles.js';
import '../token-ajax.js';

/**
 *
 * NeighbourhoodDiagram implements a web component for drawing an Entity neighbourhood diagram
 *
 * This component renders a radial plot with the focus entity type at the centre and the
 * availabvle relationship types radiating from it, with the neighbour entity types at the
 * outer ends of the relationhsip types. The focus type is highlighted
 * It is possible to click on a relationship or entity type to 'select' it - if the type is
 * an entity type it becomes the new focus; if it is a relationship type it becomes the new
 * view type (and will be displayed in the details panel).
 *
 *
 */

class NeighbourhoodDiagram extends PolymerElement {

    static get template() {
        return html`

            <style include="shared-styles">

                * { font-size: 12px ; font-family: sans-serif; }

            </style>

            <body>

                <div>
                        <p>
                        Hello I am a neighbourhood diagram...
                        </p>




                </div>
            </body>

        `;
    }


    static get properties() {
        return {

            // Reference to TypeManager element which this ConnectionManager depends on.
            // The TypeManager is created in the DOM of the parent and is passed in
            // once we are all initialised. This avoids any direct dependency from ConnectionManager
            // on TypeManager.

            typeManager: Object

        };
    }




    /*
     * Element is ready
     */
    ready() {
        // Ensure you call super.ready() first to initialise node hash...
        super.ready();
        console.log("neighbourhood-diagram ready");

        this.render();
    }


    render() {
        console.log("ready to render neighbourhood diagram");
    }

}


window.customElements.define('neighbourhood-diagram', NeighbourhoodDiagram);