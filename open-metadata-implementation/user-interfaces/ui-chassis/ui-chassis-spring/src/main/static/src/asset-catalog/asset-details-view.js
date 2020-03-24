/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import { PolymerElement, html } from '@polymer/polymer';
import '@polymer/paper-button/paper-button';
import '@polymer/paper-menu-button';
import '@polymer/paper-dropdown-menu/paper-dropdown-menu';
import '@polymer/paper-listbox';
import '@polymer/paper-item';
import '@polymer/neon-animation/animations/ripple-animation';

import '../shared-styles.js';

class AssetDetailsView extends PolymerElement {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          display: block;
          padding: 10px 20px;
        }
        
      </style>
      <app-route route="{{route}}" pattern="/:guid" data="{{routeData}}" tail="{{tail}}"></app-route>


      <token-ajax id="tokenAjaxDetails" last-response="{{items}}" ></token-ajax>
         
      <div>display name: [[items.0.properties.displayName]]</div>
      
      <div>type: [[item.0.type.name]]</div>
      <div>classification : [[items.0.classifications.name]]</div>
<!--      "createdBy": "Administrator IIS",-->
<!--      "createTime": "2020-01-17T13:33:05.000+0000",-->
<!--      "updatedBy": "Administrator IIS",-->
<!--      "updateTime": "2020-01-17T13:33:21.000+0000",-->
<!--      "version": 1579268001000,-->
<!--      "status": "Active",-->
       
    `;
    }

    static get properties() {
        return {
            items: {type: Array, notify: true}
        };
    }

    static get observers() {
        return [
            '_routeChanged(routeData.guid)',
            '_itemChanged(items)'
        ];
    }

    _routeChanged(guid) {
        this.$.tokenAjaxDetails.url='/api/assets/' + guid;
        this.$.tokenAjaxDetails._go();
    }

    _itemChanged(items){
        console.debug('asset details items changed');
        if(items){
            this.dispatchEvent(new CustomEvent('push-crumb', {
                bubbles: true,
                composed: true,
                detail: {label: items[0].properties.displayName, href: '/view/'+ items[0].guid }}));
        }
    }

}

window.customElements.define('asset-details-view', AssetDetailsView);
