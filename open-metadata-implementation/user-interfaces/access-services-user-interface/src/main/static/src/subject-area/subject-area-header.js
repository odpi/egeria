/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */
import { PolymerElement, html } from "@polymer/polymer/polymer-element.js";
import {mixinBehaviors} from "@polymer/polymer/lib/legacy/class.js";
import {AppLocalizeBehavior} from "@polymer/app-localize-behavior/app-localize-behavior.js";
import '@polymer/paper-input/paper-input.js';
import '@polymer/paper-material/paper-material.js';
import '@polymer/iron-form/iron-form.js';
import '@polymer/iron-a11y-keys/iron-a11y-keys.js';
import '@polymer/paper-button/paper-button.js';
import '@polymer/paper-styles/paper-styles.js';
import '@polymer/paper-input/paper-input-behavior.js';
import '../shared-styles.js';
/**
*
* Subject Area Header is the header piece that is common across subject area content
*/

class SubjectAreaHeader extends mixinBehaviors([AppLocalizeBehavior], PolymerElement) {
  static get template() {
    return html`
      <style include="shared-styles">
        :host {
          padding: 10px 20px;
          display: inline;
        }
        .headerSelects {
          float: right
        }
      </style>
          <div>
             <paper-dropdown-menu id='viewSelector' label="View"   on-iron-select="_itemSelected">>
                <paper-listbox slot="dropdown-content"  attr-for-selected="name" id="viewlistbox" selected="home">
                           <paper-item name="home">Home</paper-item>
                           <paper-item name="ggrid">Glossary Grid</paper-item>
                           <paper-item name="pgrid">Project Grid</paper-item>
                           <paper-item name="tgrid">Term Grid</paper-item>
                           <paper-item name="ngrid"></paper-item>
                       </paper-listbox>
             </paper-dropdown-menu>
             <div class='headerSelects'>
             <span> [[localize('subject-area_header_project_select')]]</span>
             <span id='selectedProject'><strong>[[selectedProjectLabel]]</strong></span>
             <span> [[localize('subject-area_header_glossary_select')]]</span>
             <span id='selectedGlossary'><strong>[[selectedGlossaryLabel]]</strong></span>
             </div>
          </div>
  `;
   }
   static get properties() {
      return {
        //  selected view
        selectedView: {
                type: Object,
                notify: true
        },
        //  selected project
        selectedProject: {
                type: Object,
                notify: true
        },
        //  selected glossary
        selectedGlossary: {
                 type: Object,
                 notify: true
        },
        //  selected project
        selectedProjectName: {
                type: String,
                notify: true,
                computed: '_computeProjectName(selectedProject)'
        },
        //  selected glossary
        selectedGlossaryName: {
                 type: String,
                 notify: true,
                 computed: '_computeGlossaryName(selectedGlossary)'
        },
        //  selected project label
        selectedProjectLabel: {
                type: String,
                notify: true,
                value: '<No project selected>',
                computed: '_computeProjectLabel(selectedProjectName)'
        },
        //  selected glossary label
        selectedGlossaryLabel: {
                 type: String,
                 notify: true,
                 value: '<No glossary selected>',
                 computed: '_computeGlossaryLabel(selectedGlossaryName)'
        }

      }
   }
  attached() {
        this.loadResources(
               // The specified file only contains the flattened translations for that language:
               "locales/subject-area/header_" + this.language + ".json",  //e.g. for es {"hi": "hola"}
               this.language,               // unflatten -> {"es": {"hi": "hola"}}
               true                // merge so existing resources won't be clobbered
        );
  }
  ready(){
     super.ready();
  }
  _computeProjectName(selected) {
     return this._computeSelectedName(selected);
  }
  _computeGlossaryName(selected) {
     return this._computeSelectedName(selected);
  }
  _computeSelectedName(selected) {
     var selectedName;
     if (selected && selected.name) {
        selectedName = selected.name;
     }
     return selectedName;
  }
  _computeProjectLabel(name) {
    if (name) {
        return name;
    } else {
        return this.localize("subject-area_header_no_selected_project_label");
    }
  }
  _computeGlossaryLabel(name) {
    if (name) {
        return name;
    } else {
        return this.localize("subject-area_header_no_selected_glossary_label");
    }
  }
     /*
      *driven when an item is selected. Issue a custom event to pass up the selected item.
      */
    _itemSelected(e) {
        var selectedItem = e.target.selectedItem;
        var selectedItemName = selectedItem.attributes.name.nodeValue;
        if (selectedItem && selectedItem.attributes && selectedItem.attributes.name ) {
               console.log("selected item is " + selectedItem.attributes.name.nodeValue);

               if (selectedItemName =='ggrid' && !this.selectedProjectName  ) {
                    alert( this.localize("subject-area_header_msg_select_project_for_glossary"));
               } else if (selectedItemName =='ggrid' && !this.selectedProjectName  )
               {
                     alert(this.localize("subject-area_header_msg_choose_home_project_select"));
               } else if (selectedItemName =='pgrid' || selectedItemName =='home') {
                    // home and project do not need anything selected prior to using.
                    this.dispatchEvent(new CustomEvent('viewSelectionEvent', {
                                       bubbles: true,  // bubble up
                                       composed: true, // allow the event to go through shadow dom boundaries
                                       detail: selectedItemName}));
               } else if (!this.selectedProjectName || !this.selectedGlossaryName) {
                    alert(this.localize("subject-area_header_msg_choose_home"));
               } else {
                    // we have selected project and a selected glossary so send the event.
                    this.dispatchEvent(new CustomEvent('viewSelectionEvent', {
                        bubbles: true,  // bubble up
                        composed: true, // allow the event to go through shadow dom boundaries
                        detail: selectedItemName}));
               }
        }
    }
}

window.customElements.define('subject-area-header', SubjectAreaHeader);