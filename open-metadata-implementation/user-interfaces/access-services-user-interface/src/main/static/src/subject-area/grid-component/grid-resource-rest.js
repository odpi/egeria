/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from "@polymer/polymer/polymer-element.js";
import '@polymer/iron-ajax/iron-ajax.js';
import '../../spinner.js';
import '../../shared-styles.js';

class GridResourceRest extends PolymerElement {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          display: block;
          padding: 10px 20px;
        }
      </style>
      <iron-ajax id="getResourcesAjax" url="{{url}}"
                              handle-as="json"
                              last-response="{{propertyDefs}}"
                              on-error="_handleErrorResponse"
                              loading="{{loading}}"
                              method="get"

       ></iron-ajax>
       <spinner-overlay id="resourceBackdrop" with-backdrop scroll-action="lock"
                   always-on-top
                   no-cancel-on-outside-click
                   no-cancel-on-esc-key>
       </spinner-overlay>
    `;
    }

    static get properties() {
      return {
        propertyDefs: {
        type: Array,
        notify: true,
        observer: '_propertyDefsChanged'
      },
      loading: {
        type: Boolean,
        notify: true,
        observer: '_onLoadingChanged'
      },
      component:{
        type: String,
        notify: true,
        value:'subject-area'
      },
      name:{
        type: String,
        notify: true
      },
      url: {
        type: String,
        computed: 'computeUrl(component,name)',
        notify: true,
        observer: '_handleUrlChange'
      }
      };
    }
    ready(){
        super.ready();
    }
    _handleUrlChange(newValue) {
      if (newValue) {
          // a new name changes the url, that then drives this observer to issue the rest call
          this.$.getResourcesAjax.url=newValue;
          this.$.getResourcesAjax.generateRequest();
      }
    }
    _propertyDefsChanged(newValue) {
         // event name contains the name of the definitions that are being processed.
         var eventName = "rest_" + this.name.toLowerCase() + "_defs";
         this.dispatchEvent(new CustomEvent(eventName, {
                             bubbles: true,
                             composed: true,
                             detail: newValue }));


    }
    computeUrl(compName,artifactName) {
         var urlStr ="/properties/" +compName + "/" + artifactName +".json";
         console.log("computing url  " + urlStr);
         return urlStr;
    }

    /**
     * Duplicate of the logic in ajax-token. Used with the non secure rest which gets the resources.
     *
     */
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
                  this.dispatchEvent(new CustomEvent('rest-error-event', {
                                   bubbles: true,
                                   composed: true,
                                   detail: evt.detail
                                }));
             }
    }
    _onLoadingChanged(loading){
        console.debug('loading changed... ' + this.loading);
        if(this.loading){
            this.$.resourceBackdrop.open();
        }else{
            this.$.resourceBackdrop.close();
        }
    }

}

window.customElements.define('grid-resource-rest', GridResourceRest);