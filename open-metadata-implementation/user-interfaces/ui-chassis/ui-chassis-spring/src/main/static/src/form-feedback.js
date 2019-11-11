/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from "../node_modules/@polymer/polymer/polymer-element.js";
import './shared-styles.js';

class FormFeedback extends PolymerElement {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
            display: block;
        } 
        
      </style>

       <div class$="feedback [[level]]">
         <p>[[message]]</p>
       </div>
    `;
    }

    static get properties() {
        return {
            message: String,
            level: {
                type: String,
                enum: ['error', 'warning', 'info']
            }
        };
    }

}

window.customElements.define('form-feedback', FormFeedback);