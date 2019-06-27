/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */
import "@polymer/paper-input/paper-input.js";
import "@polymer/paper-material/paper-material.js";
import "@polymer/iron-form/iron-form.js";
import "@polymer/iron-a11y-keys/iron-a11y-keys.js";
import "@polymer/paper-button/paper-button.js";
import "@polymer/paper-styles/paper-styles.js";
import "@polymer/paper-dropdown-menu/paper-dropdown-menu.js";
import "@polymer/paper-listbox/paper-listbox.js";
import "@polymer/paper-item/paper-item.js";
import "@polymer/paper-menu-button/paper-menu-button.js";
import "@polymer/paper-input/paper-input-behavior.js";
import "@polymer/paper-dialog/paper-dialog.js";
import "@polymer/paper-dialog-behavior/paper-dialog-behavior.js";
import "@polymer/iron-localstorage/iron-localstorage.js";
import "@polymer/iron-ajax/iron-ajax.js";
import {mixinBehaviors} from "@polymer/polymer/lib/legacy/class.js";
import {AppLocalizeBehavior} from "@polymer/app-localize-behavior/app-localize-behavior.js";
import "@vaadin/vaadin-grid/vaadin-grid.js";
import "@vaadin/vaadin-grid/vaadin-grid-selection-column.js";
import "@vaadin/vaadin-grid/vaadin-grid-sort-column.js";
import "@vaadin/vaadin-text-field/vaadin-text-field.js";
import "@vaadin/vaadin-button/vaadin-button.js";
import { PolymerElement, html } from "@polymer/polymer/polymer-element.js";
import '../shared-styles.js';
import '../token-ajax.js';
import '../property-pane.js';
import './glossary-selector.js';
import './project-selector.js';
/**
*
*SubjectAreaView is the top level web component for the subject area expert.
*/

class SubjectAreaView extends mixinBehaviors([AppLocalizeBehavior], PolymerElement) {
  static get template() {
    return html`
      <style include="shared-styles">
        :host {
          display: block;
          padding: 10px 20px;
        }
      </style>
       <div style="margin-bottom: 20px">

         [[localize('subject-area_prototypeLabel')]]
         <p>
         <glossary-selector id='glossarySelector'></glossary-selector>
         <p>
         <project-selector id='projectSelector'></project-selector>
         <p>
         <property-pane id="selected-artifact" name="glossary" artifact="{{selectedGlossary}}" component="subjectarea"></property-pane>
       </div>
  `;
   }

  static get properties() {
    return {
    // language
      language: { value: 'en' },
      //  selected project
      selectedProject: {
        type: Object,
        notify: true
      },
      //  selected glossary
      selectedGlossary: {
        type: Object,
        notify: true
      }
    };
  }
  attached() {
        this.loadResources(
               // The specified file only contains the flattened translations for that language:
               "locales/subjectarea/" + this.language + ".json",  //e.g. for es {"hi": "hola"}
               this.language,               // unflatten -> {"es": {"hi": "hola"}}
               true                // merge so existing resources won't be clobbered
             );
  }
  ready(){
     super.ready();
     this.$.glossarySelector.addEventListener('glossarySelectionEvent', e => {this._handleGlossarySelected(e)});
  }
  _handleGlossarySelected(event) {
     console.log("subject-area-view _handleGlossarySelected... " + event);
     this.selectedGlossary = event.detail;

  }
}

window.customElements.define('subject-area-view', SubjectAreaView);