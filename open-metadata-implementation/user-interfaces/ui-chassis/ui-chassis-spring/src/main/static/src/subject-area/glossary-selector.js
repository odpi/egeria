/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import '@polymer/paper-input/paper-input.js';
import '@polymer/paper-material/paper-material.js';
import '@polymer/iron-form/iron-form.js';
import '@polymer/iron-a11y-keys/iron-a11y-keys.js';
import '@polymer/paper-button/paper-button.js';
import '@polymer/paper-styles/paper-styles.js';
import '@polymer/paper-input/paper-input-behavior.js';
import '@vaadin/vaadin-grid/vaadin-grid.js';
import '@vaadin/vaadin-grid/vaadin-grid-selection-column.js';
import '@vaadin/vaadin-grid/vaadin-grid-sort-column.js';
import { PolymerElement, html } from "@polymer/polymer/polymer-element.js";
import {mixinBehaviors} from "@polymer/polymer/lib/legacy/class.js";
import {AppLocalizeBehavior} from "@polymer/app-localize-behavior/app-localize-behavior.js";
import '../shared-styles.js';
import '../token-ajax.js';

class GlossarySelector extends mixinBehaviors([AppLocalizeBehavior], PolymerElement) {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          display: inline-block;
          padding: 10px 20px;
        }
         form  { display: table;      }
                p     { display: table-row;  }
                label { display: table-cell; }
                input { display: table-cell; }
                a     { display: table-cell; }
      </style>
       <token-ajax id="addGlossaryAjaxId" last-response="{{lastAddGlossaryResp}}" ></token-ajax>
       <token-ajax id="getGlossariesAjaxId" last-response="{{lastGetGlossariesResp}}" ></token-ajax>
       <paper-dropdown-menu label="Glossaries"
                            id="glossary-selector"
                            selected="[[selectedGlossary]]"
                            attr-for-selected="name"
                           on-iron-select="_itemSelected">
                      <paper-listbox slot="dropdown-content" selected="1">
                             <template is="dom-repeat" items="[[glossaries]]">
                                 <paper-item guid=[[item.systemAttributes.guid]]>[[item.name]]</paper-item>
                             </template>
                      </paper-listbox>
       </paper-dropdown-menu>
       <paper-button on-tap="onGlossaryDialogOpen" id='addNewSelectedGlossary' disabled>+</paper-button>
       <paper-dialog id="createGlossaryDialog">

               <form is="iron-form" id="createGlossaryForm">
                  <p>
                  <label for="glossaryName">Name</label>
                  <input is="paper-input" id="glossaryName" type="text" name="name"> <br>
                  </p>
                   <p>
                  <label for="glossaryQualifiedName">Qualified Name</label>
                  <input is="paper-input" id="glossaryQualifiedName" type="text" name="qname"><br>
                  </p>
                   <p>
                  <label for="glossaryDescription">Description</label>
                  <input is="paper-input" id="glossaryDescription" type="text" name="description"><br>
                  </p>
                  <div class="buttons">
                      <paper-button  dialog-dismiss>Cancel</paper-button>
                      <paper-button on-tap="_onGlossaryDialogCreate">Create</paper-button>
                  </div>
               </form>
        </paper-dialog>
       
    `;
    }

    static get properties() {
        return {
      language: {
        type: String
      },
      //  add glossary response
      lastAddGlossaryResp: {
        type: Object,
         // Observer called  when this property changes
        observer: '_addGlossaryRespChanged'
      },
      //  get glossary response
      lastGetGlossaryResp: {
        type: Object,
         // Observer called  when this property changes
        observer: '_getGlossaryRespChanged'
      },
      //  get glossaries response
      lastGetGlossariesResp: {
        type: Object,
        notify: true
      },
      // all glossaries from the server rest call response
      glossaries: {
        type: Array,
        computed: 'computeGlossaries(lastGetGlossariesResp)',
        notify: true
      },
      glossaryMap: {
              type: Array,
              computed: 'computeGlossaryMap(glossaries)',
              notify: true
      },
      enabled: {
              type: Boolean,
              notify: true,
              observer: '_handleEnabledChange'

      }
      };
    }
    ready(){
        super.ready();
        this.getGlossaries();
    }
    attached() {
            this.loadResources(
                   // The specified file only contains the flattened translations for that language:
                   "locales/subject-area/glossarysel_" + this.language + ".json",  //e.g. for es {"hi": "hola"}
                   this.language,               // unflatten -> {"es": {"hi": "hola"}}
                   true                // merge so existing resources won't be clobbered
            );
    }
    _handleEnabledChange(newValue) {
         if (newValue) {
            this.$.addNewSelectedGlossary.disabled=false;
         }
    }

    onGlossaryDialogOpen() {
        this.$.createGlossaryDialog.open();
    }
  /**
   *  Create glossary dialog form - by issuing the rest call. Note that this dos not submit the form which would close the dialog.
   * It keeps the dialog open so that ut can display any error that occurs
   */
  _onGlossaryDialogCreate() {
     if (this.$.glossaryName.value) {
         this.createGlossaryAJAX();
     } else {
         alert(this.localize("subject-area_glossary_no_name"));
     }
  }
  /**
   * driven when an item is selected. Issue a custom event to pass up the selected item.
   */
  _itemSelected(e) {
      var selectedItem = e.target.selectedItem;
      if (selectedItem) {
        console.log("selected: " + selectedItem.innerText + ",guid is " + selectedItem.guid);
        var selectedGlossary = this.glossaryMap[selectedItem.guid];
        this.dispatchEvent(new CustomEvent('glossarySelectionEvent', {
                         bubbles: true,  // bubble up
                         composed: true, // allow the event to go through shadow dom boundaries
                         detail: selectedGlossary}));


      }
  }

 /**
   * Issue the create rest Ajax call to add a glossary to the server
   */
  createGlossaryAJAX() {
        var glossary = {};
        glossary.nodeType = "Glossary";
        glossary.class = "Glossary";
        glossary.name =  this.$.glossaryName.value;
        if (this.$.glossaryDescription.value) {
            glossary.description= this.$.glossaryDescription.value;
        }
        if (this.$.glossaryQualifiedName) {
            glossary.qualifiedName =this.$.glossaryQualifiedName.value;
        }

        this.$.addGlossaryAjaxId.method ="post";
        this.$.addGlossaryAjaxId.body = glossary;
        this.$.addGlossaryAjaxId.url = "/servers/<<servername>>/open-metadata/view-services/subject-area/glossaries";
        this.$.addGlossaryAjaxId._go();
  }

  /*
   * After an add glossary - get the glossaries again so the drop down will be up tp date.
   */
  _addGlossaryRespChanged(newValue,oldValue) {
      if (newValue.relatedHTTPCode == 200) {
           this.getGlossaries();
           // close the dialog - a glossary was successfully created
           this.$.createGlossaryDialog.close();
      } else {
          if (newValue.exceptionErrorMessage) {
               // this is an error that the omas code generated with message and user action.
               alert('Error occurred: ' +newValue.exceptionErrorMessage + ',user action: ' + newValue.exceptionUserAction);
           } else {
               alert('Good resp :' +  newValue);
          }
      }
  }

 /**
  * Issue get glossaries Ajax rest call to the server
  */
  getGlossaries() {
      this.$.getGlossariesAjaxId.method ="get";
      this.$.getGlossariesAjaxId.url = "/servers/<<servername>>/open-metadata/view-services/subject-area/glossaries";
      this.$.getGlossariesAjaxId._go();
  }
 /**
   * Get the glossaries from the response
   */
  computeGlossaries(resp) {
        if (resp) {
           return resp.glossaries;
        } else {
           return null;
        }
  }
  computeGlossaryMap(glossaries) {
        var map = null;
        if (glossaries) {
             map = {};
             for (var i = 0; i < glossaries.length; i++) {
               map[glossaries[i].systemAttributes.guid] = glossaries[i];
             }
             return map;
        }
        return map;
  }
}

window.customElements.define('glossary-selector', GlossarySelector);