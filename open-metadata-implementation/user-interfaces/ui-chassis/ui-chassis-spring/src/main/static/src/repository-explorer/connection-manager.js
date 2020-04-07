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
* ConnectionManager implements a web component for specification of server details
*
* It should present to the user 5 controls for:
*   * server name        - a string
*   * server URL Root    - a string
*   * enterprise option  - a boolean
*   * a Load! button     - a button to initiate a connection using the displayed settings
*   * status             - a string - displays if a load failure occurs
*
* The user can change the details at any time; they will take effect when Load! button is pressed.
* This will initiate a connection attempt by the TypeManager - which will either succeed (causing the
* typesLoaded event to be sent), or fail (e.g. because the server is unavailable or the details are
* incorrect).
*
*
*/

class RexConnectionManager extends PolymerElement {

    static get template() {
        return html`

            <style include="rex-styles">

            </style>

            <body>

                <div id='connectionParameters' style="height:180px; width:340px; padding:0px 20px;">

                    <div style=" position:absolute; height:150px; width:100%; top:0px; left:20px; padding:0px 20px;" >
                    Repository Server:

                        <!-- LHS -->
                        <div style="position:absolute; top:20px; left:0px; width:200px; " >

                            <paper-input
                            class="inline-element"
                            style="width:150px"
                            id = 'serverNameInput'
                            class='user-input'
                            label = "Server Name"
                            value={{serverName}}
                            on-change="serverNameChanged">
                            </paper-input>

                            <paper-input
                            class="inline-element"
                            style="width:150px;"
                            class='user-input'
                            label = "Server URL Root"
                            value={{serverURLRoot}}
                            on-change="serverURLRootChanged">
                            </paper-input>

                        </div>

                        <!-- RHS -->
                        <div style="position:absolute; top:0px; left:200px; width:120px; height:80px; text-align:center; " >

                            <paper-button
                                style="padding:10px; text-align:center; text-transform:none"
                                id = "loadButton"
                                raised
                                on-click="doConnect">
                                Connect
                            </paper-button>

                            <paper-checkbox
                                style="padding:10px;"
                                id="enterpriseOption"
                                checked="{{enterpriseOption}}"
                                on-change="enterpriseOptionChanged">
                                Enterprise
                            </paper-checkbox>

                        </div>

                    </div>

                    <div id='statusMsg' style="position:absolute; height:50px; width:100%; top:150px; left:20px; padding:0px;">Please enter server details and press 'Connect'</div>

                </div>
            </body>

        `; }


    static get properties() {
        return {

            //  user-specified serverName - using bi-directional databind
            serverName: {
                type               : String,
                value              : "",
                notify             : true,
                reflectToAttribute : true
            },

            //  user-specified serverURLRoot
            serverURLRoot: {
                type               : String,
                value              : "",
                notify             : true,
                reflectToAttribute : true
            },

            //  user-specified enterprise option (true | false)
            enterpriseOption: {
                type               : Boolean,
                value              : false,
                notify             : true,
                reflectToAttribute : true
            },

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

    }


    // Inbound events

    /*
     *  Inbound event: types-loaded
     */
    inEvtTypesLoaded() {
        this.clearStaleConnectionWarning();
    }

    /*
     *  Inbound event: types-loaded
     */
    inEvtTypesNotLoaded() {
        this.displayStaleConnectionWarning();
    }



    serverNameChanged() {
        // No action
    }

    serverURLRootChanged() {
        // No action
    }

    enterpriseOptionChanged() {
        // No action
    }

    // The user has pressed the Connect! button - issue a REST call to the repository server
    // to initially retrieve its supported types. This is a request-response exchange - it is
    // not a real connection - but the ConnectionManager will keep the server details (name
    // and URL) for further exploration requests, so the interface does appear to be conducting
    // a long-running session or connection with the repository server.
    //
    doConnect() {
        var typeManager = this.typeManager;
        typeManager.loadTypes(this.serverName, this.serverURLRoot, this.enterpriseOption);
    }


    displayStaleConnectionWarning() {
      var statusMsg = this.$.statusMsg;
      statusMsg.innerHTML = "Server could not be accessed; check details and retry";
    }

    clearStaleConnectionWarning() {
       var statusMsg = this.$.statusMsg;
       statusMsg.innerHTML = "Server OK";
    }

    getServerDetails() {
        var serverDetails = {};
        serverDetails.serverName       = this.serverName;
        serverDetails.serverURLRoot    = this.serverURLRoot;
        serverDetails.enterpriseOption = this.enterpriseOption;
        return serverDetails;
    }


}

window.customElements.define('rex-connection-manager', RexConnectionManager);