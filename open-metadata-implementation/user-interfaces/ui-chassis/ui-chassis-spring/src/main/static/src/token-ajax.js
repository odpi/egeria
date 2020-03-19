/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import {PolymerElement, html} from '@polymer/polymer/polymer-element.js';
import '@polymer/iron-ajax/iron-ajax.js';

import './spinner.js';


class TokenAjax extends PolymerElement {
    static get template() {
        return html`
        <style>
        :host {
            /*display: none;*/
        }
        
        </style>
        <iron-localstorage id="storage" name="my-app-storage" value="{{token}}"></iron-localstorage>
        <iron-ajax id="ajax" url="[[url]]"
                   handle-as="json"
                   last-response="{{lastResponse}}"
                   on-error="_handleErrorResponse"
                   body="[[body]]"
                   loading="{{loading}}"
                   method="[[method]]"
                   on-loading-changed="_onLoadingChanged"
                   headers="{{headers}}"
                   >
        </iron-ajax>
        <spinner-overlay id="backdrop" with-backdrop scroll-action="lock" 
            always-on-top 
            no-cancel-on-outside-click
            no-cancel-on-esc-key>       
        </spinner-overlay>
    `;
    }

    connectedCallback(){
        super.connectedCallback();
        this.$.storage.reload();
    }

    static get properties() {
        return {
            token: {
                type: String,
                notify: true,
                observer: '_tokenUpdated'
            },
            loading: {
                type: Boolean,
                notify: true
            },
            url: String,
            lastResponse: {
                notify: true
            },
            method: {
                type: String,
                value: 'GET'
            },
            body: {
                type: Object
            },
            headers: {
                type: Object,
                value: {'content-type' : 'application/json'},
                notify: true
            },

            /**
             * If true, automatically performs an Ajax request when either `url` or
             * `params` changes.
             */
            auto: {type: Boolean, value: false}

        };
    }

    static get observers() {
        return [
            // Observer method name, followed by a list of dependencies, in parenthesis
            '_requestOptionsChanged(auto)',
            '_loadingChanged(loading)']
    }

    _go(){
        this.$.storage.reload();
        //console.debug('_go with token:'+this.token);
        if( typeof this.token !== 'undefined'){
            this.$.ajax.headers['content-type'] = 'application/json';
            this.$.ajax.headers['x-auth-token'] = this.token;
            this.$.ajax.generateRequest();
        }else{
            console.debug('No token set to token-ajax: no _go');
        }
    }

    _tokenUpdated(){
        console.debug(this.id + ' -- token updated with:'+this.token)
        this.$.ajax.headers['x-auth-token'] = this.token;
    }

    _requestOptionsChanged(auto){
        if (this.auto) {
            this._go();
        }
    }

    _handleErrorResponse(evt){
        var status = evt.detail.request.xhr.status;
        var resp   = evt.detail.request.xhr.response;
        // Token is not valid, log out.
        if (status === 401 || status === 403 || resp.exception == 'io.jsonwebtoken.ExpiredJwtException') {
            console.log('Token invalid:'+ this.token);
            this.dispatchEvent(new CustomEvent('logout', {
                bubbles: true,
                composed: true,
                detail: {greeted: "Bye!", status: status}}));
        }else{
            console.log('Error Response:'+ resp);
            window.dispatchEvent(new CustomEvent('show-feedback', {
                bubbles: true,
                composed: true,
                detail: {message: "We are experiencing an unexpected error. Please try again later!", level: 'error'}}));
            }
            this.$.backdrop.close();
    }

    _onLoadingChanged(){
        //console.debug('cn-loading changed... ' + this.loading);
    }

    _loadingChanged(loading){
        //console.debug('loading changed... ' + this.loading);
        if(this.loading){
            this.$.backdrop.open();
        }else{
            this.$.backdrop.close();
        }
    }
}

window.customElements.define('token-ajax', TokenAjax);
