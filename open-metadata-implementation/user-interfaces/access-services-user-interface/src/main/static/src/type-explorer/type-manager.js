/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */


import { mixinBehaviors } from "../../node_modules/@polymer/polymer/lib/legacy/class.js";
import { AppLocalizeBehavior } from "../../node_modules/@polymer/app-localize-behavior/app-localize-behavior.js";
import { PolymerElement, html } from "../../node_modules/@polymer/polymer/polymer-element.js";
import '../shared-styles.js';
import '../token-ajax.js';

/**
*
* TypeManager is the implementation of a web component for the retrieval and management of type information for the type explorer UI component.
*
*
* The TypeManager component API has a loadTypes() function, which accepts server connection details and attempts to connect to the server
* to retrieve type information.
* On success it updates its internal type store; and fires the typesLoaded event.
* On failure it TODO - NEEDS TO DO SOMETHING ELSE
*
* The TypeManager API also has getter functions for retrieving entity, relationship or classification type names from the loaded type information.
* The TypeManager API also has getter functions for retrieving detailed entity, relationship or classification type information from the loaded type information.
*
*/

class TypeManager extends PolymerElement {

    static get template() {
        return html`

            <token-ajax id="loadTypeExplorerAjaxId" last-response="{{lastLoadTypeExplorerResp}}" ></token-ajax>
        `; }



    static get properties() {

        return {

            //  last response - TODO this might be redundant and you could bind directly to the tex property???
            lastLoadTypeExplorerResp: {
                type: Object,
                observer: '_loadTypeExplorerRespChanged'    // Observer called  when this property changes
            },

            // TEX - this holds all the extended type information
            tex: {
                type  : Object,
                value : undefined
            },

            //
            selectedEntityType: {  // TODO - this should not be part of this component
                type    : String,
                value   : undefined
            },

             //
             diagramType: {  // TODO - this should not be part of this component
                 type    : String,
                 value   : "Inheritance"    // initial diagram is entity inheritance
             }
        };
    }

    /*
     * Element is ready
     */
    ready() {
        // Ensure you call super.ready() first to initialise node hash...
        super.ready();
        console.log("tex-explorer ready function complete");
    }



    /*
     *  Ask the UI Aoplication to retrieve the type information from the named OMAG Server
     */
    loadTypes(serverName, serverURLRoot, enterpriseQuery) {
        console.log("TypeManager loadTypes called");

        if (serverName !== undefined && serverName !== null  && serverURLRoot !== undefined && serverURLRoot !== null ) {

            if (serverName.length > 0 && serverURLRoot.length > 0) {

                /*
                 * Requesting new type information so clear wat we already had...
                 */
                this.selectedEntityType = undefined;   // TODO - this should not be part of this component, but does need to be reset (elsewhere) on a new load
                this.tex                = undefined;

                /*
                 * Format the body for the AJAX query to retrieve the type information from the server
                 */
                var serverDetails              = {};
                serverDetails.serverName       = serverName;
                serverDetails.serverURLRoot    = serverURLRoot;
                serverDetails.enterpriseOption =  enterpriseQuery ? "true" : "false";
                console.log("Server Details: "+serverDetails);


                /*
                 * Issue the AJAX query
                 * The userId under which the back-end REST call will be made is retrieved in the UI Application from the HTTP request's session context
                 */

                console.log("Issue AJAX query");
                this.$.loadTypeExplorerAjaxId.method ="post";
                this.$.loadTypeExplorerAjaxId.body = serverDetails;
                this.$.loadTypeExplorerAjaxId.url = "/api/types/typeExplorer";
                this.$.loadTypeExplorerAjaxId._go();

            }
        }
        else {
            alert("Please check serverName and serverURLRoot fields are set");
        }

    }



    /*
     * Observer to handle receipt of packaged type data response from UI Application
     */
    _loadTypeExplorerRespChanged(newValue,oldValue) {

        // TODO : format and parse response to handle error cases
        //        if (newValue.relatedHTTPCode == 200) {
        //            console.log("_loadTypeExplorerRespChanged response status code 200");
        //
        //            this.getGlossaries();
        //                                // close the dialog - a glossary was successfully created
        //                                            this.$.createGlossaryDialog.close();
        //        } else {
        //            if (newValue.exceptionErrorMessage) {
        //                alert('Error occurred: ' +newValue.exceptionErrorMessage + ',user action: ' + newValue.exceptionUserAction);
        //            } else {
        //                alert('Error occurred resp :' +  newValue);
        //            }
        //        }


        console.log("_loadTypeExplorerRespChanged called, oldValue was "+oldValue+", newValue is "+newValue);

        if (newValue !== undefined && newValue !== null) {
            this.tex = newValue;

            alert("Type information has been updated!");

            var customEvent = new CustomEvent('types-loaded', { bubbles: true, composed: true, detail: {source: "type-manager"}  });
            this.dispatchEvent(customEvent);
        }
    }

    /*
     * Helper function to retrieve entities from TEX
     */
    getEntities() {
        return this.tex.entities;
    }

    /*
     * Helper function to retrieve relationships from TEX
     */
    getRelationships() {
        return this.tex.relationships;
    }

    /*
     * Helper function to retrieve classifications from TEX
     */
    getClassifications() {
        return this.tex.classifications;
    }

    getEntity(typeName) {
        return this.tex.entities[typeName];
    }

    getRelationship(typeName) {
        return this.tex.relationships[typeName];
    }

    getClassification(typeName) {
        return this.tex.classifications[typeName];
    }


    getEnum(typeName) {
        return this.tex.enums[typeName];
    }

}

window.customElements.define('type-manager', TypeManager);