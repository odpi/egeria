/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */


import { mixinBehaviors } from "../../node_modules/@polymer/polymer/lib/legacy/class.js";
import { AppLocalizeBehavior } from "../../node_modules/@polymer/app-localize-behavior/app-localize-behavior.js";
import { PolymerElement, html } from "../../node_modules/@polymer/polymer/polymer-element.js";
import '../shared-styles.js';
import '../token-ajax.js';

/**
*
* TypeExplorer is the model web component for the type explorer UI component.
*/

class TypeExplorer extends PolymerElement {

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
            selectedEntityType: {
                type    : String,
                value   : undefined
            },

             //
             diagramType: {
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
    texLoad(serverName, serverURLRoot, enterpriseQuery) {
        console.log("texLoad called");

        if (serverName !== undefined && serverName !== null  && serverURLRoot !== undefined && serverURLRoot !== null ) {

            if (serverName.length > 0 && serverURLRoot.length > 0) {

                /*
                 * Requesting new type information so clear wat we already had...
                 */
                this.selectedEntityType = undefined;
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

            alert("TEX object updated!");

            var customEvent = new CustomEvent('tex-updated', {
              bubbles: true,
              composed: true,
              detail: {something: "else"}
            });
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

}

window.customElements.define('type-explorer', TypeExplorer);