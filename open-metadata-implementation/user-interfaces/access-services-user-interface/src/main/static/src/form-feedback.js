/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from "../node_modules/@polymer/polymer/polymer-element.js";

class FormFeedback extends PolymerElement {
    static get template() {
        return html`
      <style>
        :host {
            display: block;
        } 
        .feedback {
            min-height: 1em;
            padding: 3px 6px;
            margin: 5px 0;
        }       
        .error {
            color: red;
            border: solid 1px red;
            background-color: #ffcfd3;
        }
        .info {
            color : green;
            border: solid 1px green;
            background-color: #bdeebd;
        }
        
        .warning {
            color: orange;
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