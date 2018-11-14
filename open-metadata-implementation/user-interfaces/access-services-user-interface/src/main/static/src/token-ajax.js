/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import '@polymer/iron-ajax/iron-ajax.js';
import {PolymerElement, html} from '@polymer/polymer/polymer-element.js';

class TokenAjax extends PolymerElement {
    static get template() {
        return html`
      <style>
        :host {
            display: none;
        }        
      </style>

      <app-localstorage-document key="token" data="{{token}}"></app-localstorage-document>
        <iron-ajax id="ajax" url="{{url}}"
                   headers="[[headers]]"
                   handle-as="json"
                   sync="true"
                   last-response="{{lastResponse}}"></iron-ajax>
    `;
    }

    static get properties() {
        return {
            token: {
                type: String,
                notify: true,
                observer: '_tokenUpdated'
            },
            url: String,
            headers: {
                type: Object,
                computed: '_getHeader(token)'
            },
            lastResponse: {
                type: Object,
                notify: true
            }
        };
    }

    ready() {
        super.ready();
        this.$.ajax.addEventListener('on-error', this._onError);
    }

    _onError(evt){
        alert('token error');
    }

    _tokenUpdated(){
        console.info(this.token)
        if(this.token){
            this.$.ajax.generateRequest();
        }else{
            window.location = "/login";
        }
    }
}

window.customElements.define('token-ajax', TokenAjax);
