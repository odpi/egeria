/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import { PolymerElement, html } from '@polymer/polymer';
import '@vaadin/vaadin-tabs/vaadin-tabs.js';
import '../shared-styles.js';
import './asset-search-view'

class AssetCatalogView extends PolymerElement {
    static get template() {
        return html`
      <style include="shared-styles">
       :host {
          display: block;
          margin:var(--egeria-view-margin);
          min-height: var(--egeria-view-min-height);
        }
         #useCases {
            color: var(--egeria-primary-color);
            width: fit-content;
            margin: auto;
        }
      </style>
      
      <app-route route="{{route}}" pattern="/:usecase" data="{{routeData}}" tail="{{tail}}"></app-route>
      
      <dom-if if="[[ _tabsVisible(routeData) ]]" > 
        <app-route route="{{tail}}" pattern="/:guid" data="{{routeTailData}}" "></app-route>
        <template> 
            <vaadin-tabs id="useCases" selected="[[ _getUseCase(routeData.usecase) ]]">
              <vaadin-tab value="[[ usecases.0 ]]">
                <a href="[[rootPath]]#/asset-catalog/view/[[routeTailData.guid]]" tabindex="-1" rel="noopener"> 
                    Details
                </a>
              </vaadin-tab>
              <vaadin-tab value="[[ usecases.1 ]]">
                <a href="[[rootPath]]#/asset-catalog/context/[[routeTailData.guid]]" tabindex="-1"  rel="noopener"> 
                    Context
                </a>
              </vaadin-tab>
              
            </vaadin-tabs>
        </template>
      </dom-if>
      
      <iron-pages selected="[[routeData.usecase]]" attr-for-selected="name" role="main" fallback-selection="search">
         <asset-search-view language="[[language]]" name="search" route="{{tail}}"></asset-search-view>
         <asset-details-view language="[[language]]" name="view" route="{{tail}}"></asset-details-view>
         <asset-context-view language="[[language]]" name="context" route="{{tail}}"></asset-context-view>
      </iron-pages>
    `;
    }

    static get properties() {
        return {
            usecases: {
                type: Array,
                value: ['view', 'context']
            }
        }
    }

    static get observers() {
        return [
            '_routeChanged(route)'
        ];
    }
    _getUseCase(usecase){
        return this.usecases.indexOf(usecase);
    }

    _tabsVisible(routeData){
        return routeData !== undefined && routeData.usecase !== undefined && routeData.usecase !== 'search' ;
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
                case 'context' :
                    import('./asset-context-view');
                    break;

            }
        }
    }

}

window.customElements.define('asset-view', AssetCatalogView);
