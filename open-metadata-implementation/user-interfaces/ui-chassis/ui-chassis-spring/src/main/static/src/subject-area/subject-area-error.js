/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */
import { PolymerElement, html } from "@polymer/polymer/polymer-element.js";
import '../shared-styles.js';

/**
*
* Subject Area Header is the header piece that is common across subject area content
*/

class SubjectAreaError extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles">
        :host {
          padding: 10px 20px;
          display: inline-block;
        }
      </style>
          <div>
          <dom-repeat items="{{errorMessages}}" mutable-data>
            <template>
                item <br>
            </template>
          </dom-repeat>
       </div>
  `;
   }
   static get properties() {
      return {

        currentError: {
                type: Object,
                notify: true,
                observer: '_errorIssued'
        },
        errorMessages: {
                type: Array,
                notify: true
        }
      }
   }
  ready(){
     super.ready();
  }
  _errorIssued() {
    this.errorMessages.push(this.currentError);
  }

}

window.customElements.define('subject-area-error', SubjectAreaError);