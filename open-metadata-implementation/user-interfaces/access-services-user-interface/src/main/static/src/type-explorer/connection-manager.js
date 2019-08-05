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
*   * enterprise scope   - a boolean
*   * a Load! button     - a button to initiate a connection using the displayed settings
*   * status             - a string - displays either success/failure or the last used details (TODO - think about how to display this)
*
* The user can change the details at any time; they will take effect when Load! button is pressed.
* This will initiate a connection attempt by the TypeManager - which will either succeed (causing the
* typesLoaded event to be sent), or fail (e.g. because the server is unavailable or the details are
* incorrect).
*
*
*/

class ConnectionManager extends PolymerElement {

    static get template() {
        return html`


               <style include="shared-styles">

                   * { font-size: 12px ; font-family: sans-serif; }

                </style>

                <body>

                    <div>
                        <p>
                        Load type information from metadata repository server:
                        </p>

                        <paper-input
                            style="width:150px"
                            id = 'serverNameInput'
                            class='user-input'
                            label = "Server Name"
                            value={{serverName}}
                            on-change="serverNameChanged">
                        </paper-input>

                        <paper-input
                            style="width:150px"
                            class='user-input'
                            label = "Server URL Root"
                            value={{serverURLRoot}}
                            on-change="serverURLRootChanged">
                        </paper-input>

                        <paper-checkbox
                             id="enterpriseQuery"
                             checked="{{enterpriseQuery}}"
                             on-change="enterpriseQueryChanged">
                             Enterprise Query
                        </paper-checkbox>

                        <p>


                        <paper-button
                            id = "loadButton"
                            raised
                            on-click="doLoad" >
                            Load!
                        </paper-button>

                    </div>
              </body>

        `; }


    static get properties() {
        return {

            //  user-specified serverName - using bi-directional databind
            serverName: {
                type               : String,
                value              : "cocoMDS1",
                notify             : true,
                reflectToAttribute : true
            },

            //  user-specified serverURLRoot
            serverURLRoot: {
                type               : String,
                value              : "http://localhost:8080",
                notify             : true,
                reflectToAttribute : true
            },

            //  user-specified enterprise scope option (true | false)
            enterpriseScope: {
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
        console.log("connection-manager ready");
    }

    serverNameChanged() {
            console.log("Do nothing but log that serverName has changed....: "+ this.serverName);
    }

    serverURLRootChanged() {
            console.log("Do nothing but log that serverURLRoot has changed....: "+ this.serverURLRoot);
    }

    enterpriseScopeChanged() {
            console.log("Do nothing but log that enterpriseScope has changed....: "+ this.enterpriseScope);
    }

    doLoad() {
        console.log("doLoad called");

        console.log("In doLoad, this is "+this);
        console.log("In doLoad, this has id  "+this.id);
        console.log("In doLoad, this has className  "+this.className);

        var typeManager = this.typeManager;

        console.log("In doLoad, typeManager is "+typeManager);
        console.log("In doLoad, typeManager has id  "+typeManager.id);
        console.log("In doLoad, typeManager has className  "+typeManager.className);

        typeManager.loadTypes(this.serverName, this.serverURLRoot, this.enterpriseScope);
    }





}

window.customElements.define('connection-manager', ConnectionManager);