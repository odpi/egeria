/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import { PolymerElement, html } from '@polymer/polymer';
import {mixinBehaviors} from '@polymer/polymer/lib/legacy/class.js';
import { ItemViewBehavior} from '../common/item';

import '../shared-styles.js';
import '../common/props-table';

class AssetContextView extends mixinBehaviors([ItemViewBehavior], PolymerElement) {
    static get template() {
        return html`
      <style include="app-grid-style"></style>
      <style include="shared-styles">
       
      </style>

      <app-route route="{{route}}" pattern="/:guid" data="{{routeData}}" tail="{{tail}}"></app-route>
      <token-ajax id="tokenAjaxDetails" last-response="{{item}}" ></token-ajax>
      
      <dom-if if="[[item]]" restamp> 
        <template> 
        </template>
      </dom-if>
    `;
    }

    static get observers() {
        return [
            '_routeChanged(routeData.guid)'
        ];
    }

    _routeChanged(guid) {
        if (this.route.prefix === '/asset-catalog/view') {
            this.$.tokenAjaxDetails.url = '/api/assets/' + guid;
            this.$.tokenAjaxDetails._go();
        }
    }

}

window.customElements.define('asset-context-view', AssetContextView);