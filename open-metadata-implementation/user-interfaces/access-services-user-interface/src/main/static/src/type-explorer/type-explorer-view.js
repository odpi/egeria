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

import './type-explorer.js';

/**
*
* TypeExplorerView is the top level web component for the type explorer UI component.
*/

class TypeExplorerView extends mixinBehaviors([AppLocalizeBehavior], PolymerElement) {
    static get template() {
        return html`

            <style include="shared-styles">

                * { font-size: 12px ; font-family: sans-serif; }

                :host { display: block;  padding: 10px 20px; }

                .user-input {
                    width: 200px;
                    --paper-input-container-input: { font-size: 12px; };
                }

                .vl { border-left: 1px solid black; }
            </style>


            <body>


                <b style=" font-size:18px; font-family:sans-serif; ">Type Explorer</b>


                <!-- initial language is set in initialise -->
                <paper-radio-group id="radio_language"  on-paper-radio-group-changed="languageChanged">
                    <paper-radio-button name="en">English</paper-radio-button>
                    <paper-radio-button name="fr">French</paper-radio-button>
                </paper-radio-group>



                <!-- left hand side -->

                <div style=" position:absolute; left:10px;top:150px; height:1300px; width:1000px; background-color:#FFFFFF; " >

                    <!-- Server controls -->

                    <div style=" position:absolute;left:10px;top:10px; height:150px; width:250px; background-color:#FFFFFF">
                        <p style="text-align:center;">
                        Load type information from metadata repository server:
                        </p>

                        <paper-input
                            id = 'serverNameInput'
                            class='user-input'
                            label = "Server Name"
                            value={{serverName}}
                            on-change="serverNameChanged">
                        </paper-input>

                        <paper-input
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

                        <!-- TODO Consider moving button into type-explorer.js DOM -->
                        <type-explorer id="typeGetter"></type-explorer>
                        <paper-button
                            id = "loadButton"
                            raised
                            on-click="doLoad" >
                            Load!
                        </paper-button>

                    </div> <!-- End of Server Controls -->

                    <div class="vl" style=" position:absolute; left:260px; top:10px; height:145px; "></div>

                    <div style=" position:absolute;left:270px;top:10px; height:120px; width:300px; background-color:#FFFFFF">
                        <p style="text-align:center;">
                        Jump to Entity Type:
                        <p>
                        <select id="entityTypeSelector" style="width: 300px; float:right;  left: 150px" onchange="typeSelected('Entity',this.value)" >
                            <option value="" disabled selected>None</option>
                            <!-- options will be added dynamically -->
                        </select>
                    </div>

                    <div class="vl" style=" position:absolute; left:580px; top:10px; height:145px; "></div>

                    <div style=" position:absolute;left:600px;top:10px; height:120px; width:250px; background-color:#FFFFFF">
                        <p style="text-align:center;">
                        Switch Diagram Type:
                        <p>
                        <select id="diagramTypeSelector" style="width: 150px; float:right;  left: 50px" onchange="diagramSelected(this.value)">
                            <option>Inheritance</option>
                            <option>Neighbourhood</option>
                            <!-- <option>Connectivity</option> -- NOT YET IMPLEMENTED -->
                        </select>
                    </div>

                    <div class="vl" style=" position:absolute; left:860px; top:15px; height: 145px; "></div>

                </div> <!-- end of LHS -->


                <!-- right hand side -->

                <div style=" position:absolute; left:900px;top:150px; height:1300px; width:470px;  background-color:#FFFFFF; " >

                    <div style=" position:absolute;left:10px;top:10px; height:180px; width:450px; background-color:#FFFFFF">
                        <p style="text-align:center;">
                        Show properties for the following type:
                        <p>
                        Entity Types:
                        <select id="entityTypeSelector2" style="width: 300px; float:right;  left: 150px" onchange="typeSelected('Entity',this.value)" >
                            <option value="" disabled selected>None</option>
                            <!-- options will be added dynamically -->
                        </select>

                        <p>
                        Relationship Types:
                        <select id="relationshipTypeSelector" style="width: 300px; float:right;  left: 150px" onchange="typeSelected('Relationship',this.value)">
                            <option value="" disabled selected>None</option>
                            <!-- options will be added dynamically -->
                        </select>

                        <p>
                        Classification Types:
                        <select id="classificationTypeSelector" style="width: 300px; float:right;  left: 150px" onchange="typeSelected('Classification',this.value)">
                            <option value="" disabled selected>None</option>
                            <!-- options will be added dynamically -->
                        </select>
                    </div>

                    <div id="details" style="position:absolute;left:10px;top:200px;  height:100px; width:450px; overflow-x: hidden;  overflow: auto; background-color:#FFFFFF">
                        <p style="text-align:center;">
                        Details of a type will be displayed here when a type is selected
                        </p>
                    </div>

                </div> <!-- end of RHS -->

            </body>


            <!-- TODO - add localization (NLS) support like the following :

              <div>
               [[localize('type-explorer_prototypeLabel')]]
              </div>

            -->

        `;
    }

    static get properties() {
        return {
            // language - currently uses an explicit on-xxx-changed callback
            language: {
                type              : String,   // TODO string or String??
                value             : 'en'      // default value
            },
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
            //  user-specified enterprise query option (it is true | false)
            enterpriseQuery: {
                type               : Boolean,   // TODO boolean??
                value              : false,
                notify             : true,
                reflectToAttribute : true
            }
        };
    }

    attached() {
        this.loadResources(                                             // The specified file only contains the flattened translations for that language:
            "locales/type-explorer/" + this.language + ".json",         // e.g. for es {"hi": "hola"} unflatten -> {"es": {"hi": "hola"}}
            this.language,
            true                                                        // merge so existing resources won't be clobbered
        );
    }

    ready() {
        // Ensure you call super.ready() first to initialise node hash...
        super.ready();

        console.log("ready called");

        this.initialise();


        this.addEventListener('tex-updated', function (e) {
            // If you get a text-updated event it means you can populate the type selectors
            alert(
                "Event Received!" + "\n" +
                "e.target.id: " + e.target.id + "\n" +
                "e.detail.something: " + e.detail.something + "\n" +
                "e.bubbles: " + e.bubbles + "\n" +
                "e.composed: " + e.composed
            );
            this.tex_updated();
        })

        console.log("tex-explorer-view ready function complete");

    }

    initialise() {
        console.log("initialise called");

        // Language
        var default_language = 'en';
        var prg = this.$.radio_language;
        prg.selected = default_language;

    }

    languageChanged(event) {

        console.log("language changed callback event: "+event);
        console.log("language changed callback event.target: "+event.target);

        console.log("language changed callback this: "+this);
        console.log("language changed callback this.$.radio_language: "+this.$.radio_language);
        console.log("language changed callback this.$.radio_language.selected: "+this.$.radio_language.selected);

        var langName = this.$.radio_language.selected;
        alert('Language changed to '+langName);
        this.language = langName;
        console.log("New value of language property: "+this.language);
    }

    serverNameChanged() {
        console.log("Do nothing but log that serverName has changed....: "+ this.serverName);
    }

    serverURLRootChanged() {
        console.log("Do nothing but log that serverURLRoot has changed....: "+ this.serverURLRoot);
    }

    enterpriseQueryChanged() {
        console.log("Do nothing but log that enterpriseQuery has changed....: "+ this.enterpriseQuery);
    }



    doLoad() {
        console.log("doLoad called");

        console.log("In doLoad, this is "+this);
        console.log("In doLoad, this has id  "+this.id);
        console.log("In doLoad, this has className  "+this.className);

        //var tex = document.createElement('type-explorer');
        var tex = this.$.typeGetter;
        console.log("In doLoad, tex is "+tex);
        console.log("In doLoad, tex has id  "+tex.id);
        console.log("In doLoad, tex has className  "+tex.className);

        tex.texLoad(this.serverName, this.serverURLRoot, this.enterpriseQuery);
    }


    /*
     * Observer to invoke when new type information has been retrieved by the child type-explorer element
     */
    tex_updated() {

        var tex = this.$.typeGetter;


        // Add entity types to entity type selectors
        var entities = tex.getEntities();
        var entityTypesUnsorted = Object.keys(entities);
        var entityTypesSorted = entityTypesUnsorted.sort();

        entityTypesSorted.forEach(entityExpl => {
            var typeName = entities[entityExpl].entityDef.name
            this.addTypeToSelector("entityTypeSelector", typeName);
            this.addTypeToSelector("entityTypeSelector2", typeName);
        });


        // Add relationship types to relationship type selector
        var relationships = tex.getRelationships();
        var relationshipTypesUnsorted = Object.keys(relationships);
        var relationshipTypesSorted = relationshipTypesUnsorted.sort();

        relationshipTypesSorted.forEach(relationshipExpl => {
            var typeName = relationships[relationshipExpl].relationshipDef.name
            this.addTypeToSelector("relationshipTypeSelector", typeName);
        });


        // Add classification types to classification type selector
        var classifications = tex.getClassifications();
        var classificationTypesUnsorted = Object.keys(classifications);
        var classificationTypesSorted = classificationTypesUnsorted.sort();

        classificationTypesSorted.forEach(classificationExpl => {
            var typeName = classifications[classificationExpl].classificationDef.name
            this.addTypeToSelector("classificationTypeSelector", typeName);
        });


        //  console.log("_loadTypeExplorerRespChanged: would call diagram except there isn't one yet....")
        //renderSelectedDiagram();
    }


    /*
     * Helper function to add a type to the specified type selector
     */
    addTypeToSelector(selectorName, typeName) {

        var select = this.$[selectorName];
        var opt = document.createElement('option');
        opt.value = typeName;
        opt.innerHTML = typeName;
        select.appendChild(opt);
    }




}

window.customElements.define('type-explorer-view', TypeExplorerView);