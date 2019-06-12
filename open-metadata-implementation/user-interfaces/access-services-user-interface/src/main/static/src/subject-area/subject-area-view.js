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
import {mixinBehaviors} from "../../node_modules/@polymer/polymer/lib/legacy/class.js";
import {AppLocalizeBehavior} from "../../node_modules/@polymer/app-localize-behavior/app-localize-behavior.js";
import "../../node_modules/@vaadin/vaadin-grid/vaadin-grid.js";
import "../../node_modules/@vaadin/vaadin-grid/vaadin-grid-selection-column.js";
import "../../node_modules/@vaadin/vaadin-grid/vaadin-grid-sort-column.js";
import "../../node_modules/@vaadin/vaadin-text-field/vaadin-text-field.js";
import "../../node_modules/@vaadin/vaadin-button/vaadin-button.js";
import { PolymerElement, html } from "../../node_modules/@polymer/polymer/polymer-element.js";
import '../shared-styles.js';
import '../token-ajax.js';
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
         <glossary-selector selected-glossary="{{selectedGlossary}}" ></glossary-selector>
         <p>
         <project-selector selected-project="{{selectedProject}}" ></project-selector>
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
  }
}

window.customElements.define('subject-area-view', SubjectAreaView);