/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */
import { PolymerElement, html } from "@polymer/polymer/polymer-element.js";
import '@polymer/paper-input/paper-input.js';
import '@polymer/paper-material/paper-material.js';
import '@polymer/iron-form/iron-form.js';
import '@polymer/iron-a11y-keys/iron-a11y-keys.js';
import '@polymer/paper-button/paper-button.js';
import '@polymer/paper-styles/paper-styles.js';
import '@polymer/paper-input/paper-input-behavior.js';
import {mixinBehaviors} from "@polymer/polymer/lib/legacy/class.js";
import {AppLocalizeBehavior} from "@polymer/app-localize-behavior/app-localize-behavior.js";
import './glossary-selector.js';
import './project-selector.js';
import '../shared-styles.js';
/**
*
* Subject Area Header is the header piece that is common across subject area content
*/

class SubjectAreaHome extends mixinBehaviors([AppLocalizeBehavior], PolymerElement) {
  static get template() {
    return html`
      <style include="shared-styles">
        :host {
          padding: 10px 20px;
          display: inline-block;
        }
      </style>
          <div>
          <h1>[[localize('subject-area_home_title')]]</h1>
          [[localize('subject-area_home_prototypeLabel')]]
          <p>
          [[localize('subject-area_home_usage')]]
          <p>
          [[localize('subject-area_home_project_select')]]
          <p>
          <project-selector id='projectSelector' language='[[language]]' selectedProject='[[selectedProject]]'></project-selector>
          <p>
          [[localize('subject-area_home_glossary_select')]]
          <p>
          <glossary-selector  id='glossarySelector' language='[[language]]' selectedGlossary='[[selectedGlossary]]' enabled='[[enabledGlossary]]'></glossary-selector>

          </div>
  `;
   }
   static get properties() {
      return {
        language: {
             type: String
        },
        //  selected project
        selectedProject: {
                type: Object,
                notify: true,
                observer:"_handleSelectedProject"
        },
        //  selected glossary
        selectedGlossary: {
                 type: Object,
                 notify: true
        }
      }
   }
  ready(){
     super.ready();
  }
  _handleEnabledGlossary(newValue) {
   console.log('_handleEnabledGlossary home');
  }
  attached() {
          this.loadResources(
                 // The specified file only contains the flattened translations for that language:
                 "locales/subject-area/home_" + this.language + ".json",  //e.g. for es {"hi": "hola"}
                 this.language,               // unflatten -> {"es": {"hi": "hola"}}
                 true                // merge so existing resources won't be clobbered
          );
  }
  _handleSelectedProject(newValue) {
       this.$.glossarySelector.enabled= newValue;

  }
}

window.customElements.define('subject-area-home', SubjectAreaHome);