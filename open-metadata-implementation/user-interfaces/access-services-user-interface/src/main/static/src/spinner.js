/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import {PolymerElement, html} from '@polymer/polymer/polymer-element.js';
import {mixinBehaviors} from '@polymer/polymer/lib/legacy/class.js';
import {IronOverlayBehavior} from '@polymer/iron-overlay-behavior/iron-overlay-behavior.js';
import '@polymer/paper-spinner/paper-spinner-lite.js';
import '@polymer/paper-spinner/paper-spinner-lite.js';

class SpinnerOverlay extends mixinBehaviors(IronOverlayBehavior, PolymerElement)  {
    static get template() {
        return html`
          <style>
            :host {
              background: white;
            }
            
            paper-spinner-lite {
                --paper-spinner-color: #71ccdc;
                --paper-spinner-stroke-width: 5px;
                width: 80pt;
                height: 80pt;              
            }
          </style>
          <paper-spinner-lite active></paper-spinner-lite> 
    `;
    }
}

window.customElements.define('spinner-overlay', SpinnerOverlay);
