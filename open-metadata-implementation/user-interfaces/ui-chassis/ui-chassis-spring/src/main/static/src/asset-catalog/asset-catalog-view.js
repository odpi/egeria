/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import { PolymerElement, html } from '@polymer/polymer';

import '../shared-styles.js';
import './asset-search-view'

class AssetCatalogView extends PolymerElement {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          display: block;
          padding: 0;
        }
        
      </style>
      <app-route route="{{route}}" pattern="/:usecase" data="{{routeData}}" tail="{{tail}}"></app-route>
      
      <iron-pages selected="[[routeData.usecase]]" attr-for-selected="name" role="main" fallback-selection="search">
         <asset-search-view language="[[language]]" name="search" route="{{tail}}"></asset-search-view>
         <asset-details-view language="[[language]]" name="view" route="{{tail}}"></asset-details-view>
      </iron-pages>
    `;
    }

    static get observers() {
        return [
            '_routeChanged(route)'
        ];
    }

    _routeChanged(route) {
        if (route.prefix === '/asset-catalog') {
            /**
             * keeping the switch for later add cases
             */
            switch (this.routeData.usecase) {
                case 'view' :
                    import('./asset-details-view');
                    break;


            }
        }
    }

}

window.customElements.define('asset-view', AssetCatalogView);
