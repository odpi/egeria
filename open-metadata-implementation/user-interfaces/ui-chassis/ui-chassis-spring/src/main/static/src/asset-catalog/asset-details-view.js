/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import { PolymerElement, html } from '@polymer/polymer';
import {mixinBehaviors} from '@polymer/polymer/lib/legacy/class.js';
import { ItemViewBehavior} from '../common/item';
import '@polymer/paper-listbox/paper-listbox.js';
import '@polymer/paper-item/paper-item.js';
import '@polymer/paper-item/paper-item-body';
import '@polymer/paper-styles/paper-styles';
import '@vaadin/vaadin-icons/vaadin-icons';
import '@polymer/app-layout/app-grid/app-grid-style';

import '../shared-styles.js';
import '../common/props-table';

class AssetDetailsView extends mixinBehaviors([ItemViewBehavior], PolymerElement) {
    static get template() {
        return html`
      <style include="app-grid-style"></style>
      <style include="shared-styles">
        :host {
          --app-grid-columns: 2;
          --app-grid-gutter: 1px;
          --app-grid-expandible-item-columns: 2;
          --iron-icon-fill-color: var(--egeria-primary-color);
          display: block;
          margin: 10px 24px;
          padding: 5px;
          background-color:  var(--egeria-background-color);
          min-height: calc(100vh - 115px);
          overflow-scrolling: auto;
          overflow: visible;
        }
        
        .gridItem{
            list-style: none;
        }
        
        ul#menu, ul#menu li {
          display:inline;
          padding: 0;
          margin: 0 10pt;
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
        
            <div style="border: solid 1px var(--egeria-primary-color); padding: 5pt; margin: 10pt;"> 
                <ul id="menu"> 
                    <li> 
                        <a href="#/asset-lineage/ultimateSource/[[item.guid]]" title="Ultimate Source Lineage"><iron-icon icon="vaadin:connect-o" style="transform: rotate(180deg)"></iron-icon></a>
                    </li>
                    <li> 
                        <a href="#/asset-lineage/endToEnd/[[item.guid]]" title="End2End Lineage"><iron-icon icon="vaadin:cluster"></iron-icon></a>
                    </li>
                    <li> 
                        <a href="#/asset-lineage/ultimateDestination/[[item.guid]]" title="Ultimate Destination Lineage"><iron-icon icon="vaadin:connect-o"></iron-icon></a>
                    </li>
                </ul>
            </div>
    
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
              <h3 style="margin-left: 20pt; text-align: center;">Classifications</h3>
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
      
      <dom-if if="[[ !item ]]" restamp> 
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
        this.$.tokenAjaxDetails.url='/api/assets/' + guid;
        this.$.tokenAjaxDetails._go();
    }

}

window.customElements.define('asset-details-view', AssetDetailsView);