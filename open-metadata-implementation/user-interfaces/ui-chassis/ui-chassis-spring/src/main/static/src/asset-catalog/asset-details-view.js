/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import { PolymerElement, html } from '@polymer/polymer';
import {mixinBehaviors} from '@polymer/polymer/lib/legacy/class.js';
import { ItemViewBehavior} from '../common/item';
import '@polymer/paper-listbox/paper-listbox.js';
import '@polymer/paper-styles/paper-styles.js';
import '@polymer/app-layout/app-grid/app-grid-style.js';

import '../shared-styles.js';
import '../common/props-table';
import './asset-tools';

class AssetDetailsView extends mixinBehaviors([ItemViewBehavior], PolymerElement) {
    static get template() {
        return html`
      <style include="app-grid-style"></style>
      <style include="shared-styles">
        :host {
          display: block;
          --app-grid-columns: 2;
          --app-grid-gutter: 1px;
          --app-grid-expandible-item-columns: 2;
          background-color:  var(--egeria-background-color);
          margin-top: 5px;
          min-height: calc(100vh - 115px);
          overflow-scrolling: auto;
          overflow: visible;
        }
        
        .gridItem{
            list-style: none;
        }
        
        h3{
            font-weight: normal;
        }
        @media (max-width: 640px) {
          :host {
            --app-grid-columns: 1;
          }
        }
        
      </style>
      <app-route route="{{route}}" pattern="/:guid" data="{{routeData}}" tail="{{tail}}"></app-route>
      <token-ajax id="tokenAjaxDetails" last-response="{{item}}" ></token-ajax>
      
      <dom-if if="[[item]]" restamp> 
        <template> 
          <asset-tools guid="[[item.guid]]"></asset-tools>
          <props-table items="[[_attributes(item.properties)]]" title="Properties" with-row-stripes ></props-table>
          <props-table items="[[_attributes(item.type)]]"  title="Type" with-row-stripes ></props-table>
          <props-table items="[[_attributes(item)]]"  title="Attributes" with-row-stripes ></props-table>
          
          <dom-if if="[[_hasKeys(item.additionalProperties)]]"> 
            <template> 
                <props-table items="[[_attributes(item.additionalProperties)]]" title="Additional Properties" with-row-stripes ></props-table>
            </template>
          </dom-if>
          
          <dom-if if="[[ _hasKey(item,'classifications')]]"> 
           <template> 
              <h3 style="text-align: center;">Classifications</h3>
              <ul class="app-grid" style="margin: 0; padding: 0">
                  <dom-repeat items="[[item.classifications]]">
                    <template>
                    <li class="gridItem"> <props-table items="[[_attributes(item)]]"  title="" with-row-stripes></props-table> </li>
                    <li class="gridItem"> <props-table items="[[_attributes(item.properties)]]"  title="" with-row-stripes></props-table> </li>
                    </template>
                  </dom-repeat>
              </ul>
           </template>
          </dom-if> 
        </template>
      </dom-if>
      
      <dom-if if="[[ !item ]]" restamp > 
        <template> Item not found</template>
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

window.customElements.define('asset-details-view', AssetDetailsView);