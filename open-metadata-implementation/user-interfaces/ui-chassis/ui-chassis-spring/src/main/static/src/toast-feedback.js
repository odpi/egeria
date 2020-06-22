/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from "@polymer/polymer/polymer-element.js";
import '@polymer/paper-button/paper-button';
import '@polymer/paper-toast/paper-toast';
import './shared-styles.js';

class ToastFeedback extends PolymerElement {
    static get template() {
        return html`
            <style>
                .yellow-button {
                text-transform: none;
                color: #eeff41;
              }
            </style>

            <paper-toast id="toast" duration="[[duration]]" text="[[message]]">
                <paper-button on-tap="_onClose" class="yellow-button">Close!</paper-button>
            </paper-toast>
    `;
    }


    constructor() {
        super();
        this._boundListener = this._onFeedback.bind(this);
    }

    connectedCallback() {
        super.connectedCallback();
        window.addEventListener('show-feedback', this._boundListener);
    }

    disconnectedCallback() {
        super.disconnectedCallback();
        window.removeEventListener('show-feedback', this._boundListener);
    }

    ready(){
        super.ready();
    }

    static get properties() {
        return {
            duration: {
                type: Number,
                value: 5
            },
            message: String,
            level: {
                type: String,
                enum: ['error', 'warning', 'info']
            }
        };
    }



    _onFeedback(e) {
        this.message = e.detail.message;
        this.level = e.detail.level;
        this.$.toast.open();
    }

    _onClose(){
        this.$.toast.toggle();
    }


}

window.customElements.define('toast-feedback', ToastFeedback);