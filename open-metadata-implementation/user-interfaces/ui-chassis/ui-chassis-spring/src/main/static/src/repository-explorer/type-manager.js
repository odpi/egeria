/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */


import { mixinBehaviors } from "../../node_modules/@polymer/polymer/lib/legacy/class.js";
import { AppLocalizeBehavior } from "../../node_modules/@polymer/app-localize-behavior/app-localize-behavior.js";
import { PolymerElement, html } from "../../node_modules/@polymer/polymer/polymer-element.js";
import '../shared-styles.js';
import '../token-ajax.js';

/**
*
* RexTypeManager is the implementation of a web component for the retrieval and management of type information for the
* repository explorer UI component.
*
* The RexTypeManager component API has a loadTypes() function, which accepts server connection details and attempts to
* connect to the server to retrieve type information.
*
* On success it updates its internal type store; and fires the types-loaded event.
*
* The RexTypeManager API also has getter functions for retrieving entity, relationship or classification type names or
* detailed entity, relationship or classification type information from the loaded type information.
*
*/

class RexTypeManager extends PolymerElement {

    static get template() {
        return html`

            <token-ajax id="loadTypeExplorerAjaxId" last-response="{{lastLoadTypeExplorerResp}}" ></token-ajax>
        `; }



    static get properties() {

        return {

            lastLoadTypeExplorerResp: {
                type: Object,
                observer: '_loadTypeExplorerRespChanged'    // Observer called  when this property changes
            },

            // The tex object - this holds all the extended type information
            tex: {
                type  : Object,
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



    /*
     *  Ask the UI Application to retrieve the type information from the named OMAG Server
     */

    loadTypes(serverName, serverURLRoot, enterpriseOption) {

        if (this.validate(serverName) && this.validate(serverURLRoot)) {

            /*
             * Requesting new type information but do not clear what was previously loaded.
             * It is retained for use during offline mode.
             */

            /*
             * Format the body for the AJAX query to retrieve the type information from the server
             */
            var serverDetails              = {};
            serverDetails.serverName       = serverName;
            serverDetails.serverURLRoot    = serverURLRoot;
            serverDetails.enterpriseOption = enterpriseOption;


            /*
             * Issue the AJAX query
             * The userId under which the back-end REST call will be made is retrieved in the UI Application from the HTTP request's session context
             */

            this.$.loadTypeExplorerAjaxId.method ="post";
            this.$.loadTypeExplorerAjaxId.body = serverDetails;
            this.$.loadTypeExplorerAjaxId.url = "/api/types/rexTypeExplorer";
            this.$.loadTypeExplorerAjaxId._go();

        }
        else {
            alert("Please check serverName and serverURLRoot fields are set - then retry");
        }

    }

    validate(parameter) {
        if (parameter === undefined || parameter === null || parameter === "" || parameter.length <= 0)
            return false;
        return true;
    }



    /*
     * Observer to handle receipt of packaged type data response from UI Application
     */
    _loadTypeExplorerRespChanged(newValue,oldValue) {


        if (newValue !== undefined && newValue !== null) {

            if (newValue.httpStatusCode == 200) {

                // Success

                this.tex = newValue.typeExplorer;

                var customEvent = new CustomEvent('types-loaded', { bubbles: true, composed: true, detail: {source: "type-manager"}  });
                this.dispatchEvent(customEvent);

            }
            else {

                // Failure

                if (newValue.exceptionText) {

                    alert('Error occurred: ' +newValue.exceptionText);

                }
                else {

                    alert('Error occurred: no exception message given');

                }

                // Generate a failure to load event - this will allow the status to be reported
                var customEvent = new CustomEvent('types-not-loaded', { bubbles: true, composed: true, detail: {source: "type-manager"}  });
                this.dispatchEvent(customEvent);
            }
        }
    }


    /*
     * Helper function to retrieve entities from tex
     */
    getEntityTypes() {
        return this.tex.entities;
    }

    /*
     * Helper function to retrieve relationships from tex
     */
    getRelationshipTypes() {
        return this.tex.relationships;
    }

    /*
     * Helper function to retrieve classifications from tex
     */
    getClassificationTypes() {
        return this.tex.classifications;
    }

    /*
     * Helper functions to retrieve specific objects from tex
     */

    getEntityType(typeName) {
        return this.tex.entities[typeName];
    }

    getRelationshipType(typeName) {
        return this.tex.relationships[typeName];
    }

    getClassificationType(typeName) {
        return this.tex.classifications[typeName];
    }

    getEnumType(typeName) {
        return this.tex.enums[typeName];
    }

}

window.customElements.define('rex-type-manager', RexTypeManager);