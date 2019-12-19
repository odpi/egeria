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
* It should present to the user 4 controls for:
*   * server name        - a string
*   * enterprise scope   - a boolean
*   * a Load! button     - a button to initiate a connection using the displayed settings
*   * status             - a string - displays if a load failure occurs
*
* The user can change the details at any time; they will take effect when Load! button is pressed.
* This will initiate a connection attempt by the TypeManager - which will either succeed (causing the
* typesLoaded event to be sent), or fail (e.g. because the server is unavailable or the details are
* incorrect).
*
*/

class ConnectionManager extends PolymerElement {

    static get template() {
        return html`
            <style include="shared-styles">
                * { font-size: 12px ; font-family: sans-serif; }
                :host { display: block;  padding: 0px 10px; }
                .inline-element {
                    display: inline-block;
                }
            </style>

            <div id='connectionParameters' >
                Load types from:
                 <paper-dropdown-menu label="Metadata server name"
                                            id="servernameselector"
                                            selected="[[selectedServerName]]"
                                            attr-for-selected="value"
                                            on-iron-select="_itemSelected">
                                            <paper-listbox slot="dropdown-content" id="serverdropdown">
                                               <template is="dom-repeat" id="serverNamesDomRepeat" items="[[serverNames]]">
                                                 <paper-item>[[item]]</paper-item>
                                               </template>
                                            </paper-listbox>
                       </paper-dropdown-menu>
                <paper-checkbox disabled
                    class="inline-element"
                    style="padding-right:20px; "
                    id="enterpriseQuery"
                    checked="{{enterpriseQuery}}"
                    on-change="enterpriseQueryChanged">
                    Enterprise Query
                </paper-checkbox>
                <paper-button
                    class="inline-element"
                    style="padding-left:10px; padding-right:10px; "
                    id = "loadButton"
                    raised
                    on-click="doLoadTypes" >
                    Load!
                </paper-button>
                <div class="inline-element" id='statusMsg'></div>
            </div>

        `; }

    static get properties() {
        return {

            //  user-specified serverName - using bi-directional databind
            serverName: {
                type               : String,
                notify: true
            },

            serverNames: {
                type: Object,
                notify: true,
                observer: "_serverNamesChanged"
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

            typeManager: {
                type               :Object,
                observer: '_onTypeManagerChanged'
            }

        };
    }
    // wait until the type manager exists befor trying to load the servers using it
    _onTypeManagerChanged(newValue,oldValue) {
         if (newValue) {
             this.doLoadServers();
         }
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
     *  Inbound event: types-not-loaded
     */
    inEvtTypesNotLoaded() {
        this.displayStaleConnectionWarning();
    }

    /*
     *  Inbound event: server-names-loaded
     */
    inEvtServerNamesLoaded(serverNamesFromEvent) {
        this.clearStaleConnectionWarning();
        this.serverNames = serverNamesFromEvent;

    }

    /*
     *  Inbound event: server-names-not-loaded
     */
    inEvtServerNamesNotLoaded() {
        this.displayUnableToGetServersWarning();
    }

    /**
     * driven when an item is selected. Issue a custom event to pass up the selected item.
     */
    _itemSelected(e) {
        var selectedItem = e.target.selectedItem;
        if (selectedItem) {
           console.log("selected: " + selectedItem.innerText);
           this.serverName = selectedItem.innerText;
        }
    }

    _serverNamesChanged(newValue, oldValue) {
      console.log("_serverNamesChanged" + newValue);
    }

    serverNameChanged() {
        // No action
    }


    enterpriseScopeChanged() {
        // No action
    }

    doLoadTypes() {
        this.typeManager.loadTypes(this.serverName, this.enterpriseScope);
    }

    doLoadServers() {
        this.typeManager.loadServers();
    }

    displayStaleConnectionWarning() {
      var statusMsg = this.$.statusMsg;
      statusMsg.innerHTML = "Warning: types did not load; switching to offline mode; previously loaded type information may be stale";
    }

    displayUnableToGetServersWarning() {
      var statusMsg = this.$.statusMsg;
      statusMsg.innerHTML = "Warning: servers did not load from the UI Server. This is an unexpected error; check the UI Server is configured correctly and started.";
    }

    clearStaleConnectionWarning() {
       var statusMsg = this.$.statusMsg;
       statusMsg.innerHTML = "";
    }


}

window.customElements.define('connection-manager', ConnectionManager);