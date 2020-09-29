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
      <style include="shared-styles">
       :host {
          display: flex;
          flex-flow: column;
          margin:var(--egeria-view-margin);
          min-height: var(--egeria-view-min-height);
        }
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
            '_routeChanged(route)'
        ];
    }

    _routeChanged(guid) {
        if (this.route && this.route.prefix === '/asset-catalog/context') {
            this.$.tokenAjaxDetails.url = '/api/assets/' + this.routeData.guid;
            this.$.tokenAjaxDetails._go();
        }
    }

}

window.customElements.define('asset-context-view', AssetContextView);