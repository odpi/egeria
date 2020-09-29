/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */
import "../../node_modules/@polymer/paper-input/paper-input.js";
import "../../node_modules/@polymer/paper-material/paper-material.js";
import "../../node_modules/@polymer/iron-form/iron-form.js";
import "../../node_modules/@polymer/iron-a11y-keys/iron-a11y-keys.js";
import "../../node_modules/@polymer/paper-button/paper-button.js";
import "../../node_modules/@polymer/paper-styles/paper-styles.js";
import "../../node_modules/@polymer/paper-dropdown-menu/paper-dropdown-menu.js";
import "../../node_modules/@polymer/paper-listbox/paper-listbox.js";
import "../../node_modules/@polymer/paper-item/paper-item.js";
import "../../node_modules/@polymer/paper-menu-button/paper-menu-button.js";
import "../../node_modules/@polymer/paper-input/paper-input-behavior.js";
import "../../node_modules/@polymer/paper-dialog/paper-dialog.js";
import "../../node_modules/@polymer/paper-dialog-behavior/paper-dialog-behavior.js";
import "../../node_modules/@polymer/iron-localstorage/iron-localstorage.js";
import "../../node_modules/@polymer/iron-ajax/iron-ajax.js";
import { mixinBehaviors } from "../../node_modules/@polymer/polymer/lib/legacy/class.js";
import { AppLocalizeBehavior } from "../../node_modules/@polymer/app-localize-behavior/app-localize-behavior.js";
import "../../node_modules/@vaadin/vaadin-grid/vaadin-grid.js";
import "../../node_modules/@vaadin/vaadin-grid/vaadin-grid-selection-column.js";
import "../../node_modules/@vaadin/vaadin-grid/vaadin-grid-sort-column.js";
import "../../node_modules/@vaadin/vaadin-text-field/vaadin-text-field.js";
import "../../node_modules/@vaadin/vaadin-button/vaadin-button.js";
import { PolymerElement, html } from "../../node_modules/@polymer/polymer/polymer-element.js";
import '../shared-styles.js';
import '../token-ajax.js';
import "../../node_modules/@polymer/paper-checkbox/paper-checkbox.js";
import "../../node_modules/@polymer/paper-radio-button/paper-radio-button.js";
import "../../node_modules/@polymer/paper-radio-group/paper-radio-group.js";

import './type-manager.js';
import './connection-manager.js';
import './focus-manager.js';
import './diagram-manager.js';
import './details-panel.js';

/**
*
* TypeExplorerView is the top level web component for the Type Explorer UI component.
* It implements the controller component of the design.
* It is responsible for creating the ConnectionManager, TypeManager components
* It is responsible for creating the FocusManager, DiagramManager and DetailsPanel components
*/

class TypeExplorerView extends mixinBehaviors([AppLocalizeBehavior], PolymerElement) {
    static get template() {
        return html`

            <style include="shared-styles">

                * { font-size: 12px ; font-family: sans-serif; }

                :host { display: block;  padding: 10px 10px; }

                .user-input {
                    width: 200px;
                    --paper-input-container-input: { font-size: 12px; };
                }

                .vl { border-left: 1px solid black; }



            </style>


            <body>

                <!-- <b style=" font-size:18px; font-family:sans-serif; ">Type Explorer</b>  -->

                <type-manager id="typeManager"></type-manager>



                <div>

                    <connection-manager id="connectionManager" type-manager="[[theTypeManager]]"></connection-manager>

                    <!-- LHS-->
                    <div id="lhs" style="position:absolute;left:0px;top:150px; height:1200px; width:400px; overflow-x: hidden;  overflow: auto; background-color:#CCCCCC">
                       <focus-manager id="focusManager" type-manager="[[theTypeManager]]"></focus-manager>
                       <details-panel id="detailsPanel" type-manager="[[theTypeManager]]"></details-panel>
                    </div>

                    <!-- RHS-->
                    <div id="rhs" style="position:absolute;left:400px;top:150px; height:1200px; width:1200px; overflow-x: hidden;  overflow: auto; background-color:#FFFFFF">
                        <diagram-manager id="diagramManager" style="overflow:auto;" type-manager="[[theTypeManager]]"></diagram-manager>
                    </div>

                </div>

            </body>



        `;
    }

    static get properties() {
        return {

            theTypeManager: Object

        };
    }

    ready() {
        // Call super.ready() first to initialise node hash...
        super.ready();
        this.initialise();
        this.theTypeManager = this.$.typeManager;

    }

    initialise() {

        // This class implements the event listeners that orchestrate via function calls to the child components.
        // The events are as follows:

        this.addEventListener('types-loaded', function (e) {
            this.$.connectionManager.inEvtTypesLoaded();
            this.$.focusManager.inEvtTypesLoaded();
            this.$.diagramManager.inEvtTypesLoaded();
            this.$.detailsPanel.inEvtTypesLoaded();
        });

        this.addEventListener('types-not-loaded', function (e) {
            this.$.connectionManager.inEvtTypesNotLoaded();
        });

        this.addEventListener('focus-changed', function (e) {
             var focusType = e.detail.focusType;
             this.$.diagramManager.inEvtFocusChanged(focusType);
             this.$.detailsPanel.inEvtFocusChanged(focusType);
        });

        this.addEventListener('view-changed', function (e) {
             var viewCategory = e.detail.viewCategory;
             var viewType = e.detail.viewType;
             this.$.detailsPanel.inEvtViewChanged(viewCategory,viewType);
        });

        this.addEventListener('change-focus', function (e) {
             var focusType = e.detail.focusType;
             this.$.focusManager.inEvtChangeFocus(focusType);
        });

        this.addEventListener('change-view', function (e) {
             var viewCategory = e.detail.viewCategory;
             var viewType = e.detail.viewType;
             this.$.focusManager.inEvtChangeView(viewCategory, viewType);
        });


    }


}

window.customElements.define('type-explorer-view', TypeExplorerView);