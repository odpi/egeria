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

      <app-localstorage-document key="token" data="{{token}}"></app-localstorage-document>
        <iron-ajax id="ajax" url="[[url]]"
                   headers="[[headers]]"
                   handle-as="json"
                   last-response="{{lastResponse}}"
                   on-error="_handleErrorResponse"
                   loading="{{loading}}"
                   on-loading-changed="_onLoadingChanged"
                   >
        </iron-ajax>
        <spinner-overlay id="backdrop" with-backdrop scroll-action="lock" 
        always-on-top 
        no-cancel-on-outside-click
        no-cancel-on-esc-key>       
        </spinner-overlay>
    `;
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
            headers: {
                type: Object,
                computed: '_getHeader(token)'
            },
            lastResponse: {
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
          '_loadingChanged(loading)'

      ]
    }
    ready() {
        super.ready();
        this.$.ajax.addEventListener('on-error', this._onError);
    }

    _go(){
        this.$.ajax.generateRequest();
    }

    _onError(evt){
        alert('auth token error');
    }

    _tokenUpdated(){
        console.info('token is:'+this.token)
    }

    _getHeader(token) {
        return {'x-auth-token': token};
    }

    _requestOptionsChanged(auto){
        console.log('request token-ajax');
        if (this.auto) {
            this.$.ajax.generateRequest();
        }
    }

    _handleErrorResponse(evt){
         var status = evt.detail.request.xhr.status;
         var resp   = evt.detail.request.xhr.response;
         console.log('Error response with status code: '+ status);
         // Token is not valid, log out.
          if (status === 401 || status === 403 || resp.exception == 'io.jsonwebtoken.ExpiredJwtException') {
              console.debug('Token invalid, logging out.');
              this.token = null;
          }else{
              console.debug('Unknown error!');
          }
    }

    _onLoadingChanged(){
        console.debug('cn-loading changed... ' + this.loading);
    }
    _loadingChanged(loading){
        console.debug('loading changed... ' + this.loading);
        if(this.loading){
            this.$.backdrop.open();
        }else{
            this.$.backdrop.close();
        }
    }

}

window.customElements.define('token-ajax', TokenAjax);
