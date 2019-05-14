/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import {PolymerElement, html} from '@polymer/polymer/polymer-element.js';
import '@polymer/iron-ajax/iron-ajax.js';
import '@polymer/iron-localstorage/iron-localstorage.js';
import './spinner.js';


class TokenAjax extends PolymerElement {
    static get template() {
        return html`
        <style>
        :host {
            /*display: none;*/
        }
        
        </style>
        <iron-localstorage name="my-app-storage" value="{{token}}"></iron-localstorage>

        <iron-ajax id="ajax" url="[[url]]"
                   headers="[[headers]]"
                   handle-as="json"
                   last-response="{{lastResponse}}"
                   on-error="_handleErrorResponse"
                   body="{{body}}"
                   loading="{{loading}}"
                   method="{{method}}"
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
                computed: 'computeHeaders(token,method)',
                notify: true
            },
            lastResponse: {
                notify: true
            },
            method: {
                type: String
            },
            body: {
                type: Object
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
    ready() {
        super.ready();
        this.$.ajax.addEventListener('on-error', this._onError);
    }

    _go(){
        this.$.ajax.generateRequest();
    }

    _tokenUpdated(){
        console.debug('token updated with:'+this.token)
    }
   /*
    * Compute the headers for the rest call. The authentication token (tok) is used as the value of the authentication header
    * The method name (the operation) is passed through so that the content type header id only sent when there could be content
    * associated with the request (i.e. post and put).
    */
    computeHeaders(tok,meth) {
        var computedHeaders = {};
        computedHeaders['x-auth-token'] = tok;
        // only need to the content type in the header if there is content
        if (meth == 'post' || meth == 'put') {
            computedHeaders['content-type'] = 'application/json';
        }

        console.log("computing header with token: " + tok);
        return computedHeaders;
    }

    _requestOptionsChanged(auto){
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
              console.log('Token invalid, logging out.');
              this.dispatchEvent(new CustomEvent('logout', {
                  bubbles: true,
                  composed: true,
                  detail: {greeted: "Bye!", status: status}}));
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
