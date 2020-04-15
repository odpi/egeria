/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import { PolymerElement, html } from '@polymer/polymer';

import '../shared-styles.js';

class GlossaryView extends PolymerElement {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          display: block;
          margin: 10px 24px;
          padding: 5px;
          background-color:  var(--egeria-background-color);
          min-height: calc(100vh - 85px);
        }
        
      </style>
      
      <app-route route="{{route}}" pattern="/:guid" data="{{routeData}}" tail="{{tail}}"></app-route>
      <token-ajax id="tokenAjaxDetails" last-response="{{item}}" ></token-ajax>
      
    `;
    }

    static get observers() {
        return [
            '_routeChanged(route)'
        ];
    }

    _routeChanged(route) {
        if(route.prefix === '/glossary'){
            this.$.tokenAjaxDetails.url='/api/glossaries';
            this.$.tokenAjaxDetails._go();
        }
    }


    connectedCallback() {
        super.connectedCallback();
        console.log('connect glossary-view');

    }

}

window.customElements.define('glossary-view', GlossaryView);
