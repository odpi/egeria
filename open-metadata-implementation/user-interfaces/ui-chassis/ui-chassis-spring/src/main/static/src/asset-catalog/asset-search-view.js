/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import '@polymer/paper-input/paper-input.js';
import '@polymer/iron-form/iron-form.js';
import '@polymer/iron-a11y-keys/iron-a11y-keys.js';
import '@polymer/paper-button/paper-button.js';
import '@polymer/paper-styles/paper-styles.js';
import '@polymer/paper-input/paper-input-behavior.js';
import '@vaadin/vaadin-grid/vaadin-grid.js';
import '@vaadin/vaadin-grid/vaadin-grid-selection-column.js';
import '@vaadin/vaadin-grid/vaadin-grid-sort-column.js';
import '@vaadin/vaadin-button/vaadin-button.js';
import 'multiselect-combo-box/multiselect-combo-box.js';
import '@polymer/paper-dialog/paper-dialog.js';
import '@polymer/paper-dialog-behavior/paper-dialog-behavior.js';

import {AppLocalizeBehavior} from "@polymer/app-localize-behavior/app-localize-behavior.js";
import {PolymerElement, html} from '@polymer/polymer/polymer-element.js';
import '../shared-styles.js';
import {mixinBehaviors} from "@polymer/polymer/lib/legacy/class";


class AssetSearchView extends mixinBehaviors([AppLocalizeBehavior], PolymerElement) {
    static get template() {
        return html`
      <style include="shared-styles">
        vaadin-grid {
          height: calc(100vh - 160px);
        }
        a {
            color : var(--egeria-primary-color);
            text-decoration: none;
            cursor: hand;
        }
        
        .level0{ color: inherit;}
        .level1{ color: inherit;}
        .level2{ color: orange;}
        .level3{ color: orange;}
        .level4{ color: red;}
        
      </style>

      <token-ajax id="tokenAjax" last-response="{{searchResp}}"></token-ajax>
      <token-ajax id="tokenAjaxTypes" last-response="{{items}}"></token-ajax>
      
      
      <iron-form id="searchForm">
        <form method="get">
            <iron-a11y-keys keys="enter" on-keys-pressed="_search"></iron-a11y-keys>
           <div>
                <div style="width: 200pt; display: inline-block">
                    <paper-input label="Search" value="{{q}}" no-label-float required autofocus>
                        <iron-icon icon="search" slot="prefix" class="icon"></iron-icon>
                    </paper-input>
                </div>
         
                <vaadin-button id="searchSubmit" theme="primary" on-tap="_search">
                    <iron-icon icon="search"></iron-icon>
                </vaadin-button>
             
                <multiselect-combo-box id="combo" items="[[items]]" item-label-path="name" ordered="false">
                </multiselect-combo-box>
           </div>
        </form>
       </iron-form>
       
      <vaadin-grid id="grid" items="[[searchResp]]" theme="row-stripes"
                     column-reordering-allowed multi-sort>
            <vaadin-grid-column width="10em" resizable>
                <template class="header">
                    <vaadin-grid-sorter path="properties.displayName">Name</vaadin-grid-sorter>
                </template>
                <template>
                   <a href="#/asset-catalog/view/[[item.guid]]">
                        [[_itemName(item)]]
                   </a>
                </template>
            </vaadin-grid-column>
               
             <vaadin-grid-column width="6em" resizable>
                <template class="header">
                    <vaadin-grid-sorter path="type.name">Type</vaadin-grid-sorter>
                </template>
                <template>[[item.type.name]]</template>
            </vaadin-grid-column>
            
             <vaadin-grid-column width="10em" resizable>
                <template class="header">
                    <vaadin-grid-sorter path="classifications.0.properties.level">Classifications</vaadin-grid-sorter>
                </template>
                <template>
                <dom-repeat items="[[item.classifications]]">
                  <template>
                    <div class$="[[_itemClass(item)]]">
                        <span>[[item.name]] : [[item.properties.level]] [[item.properties.zoneMembership]] [[item.properties.dataType]]</span>
                    </div>
                  </template>
                </dom-repeat>
                </template>
             </vaadin-grid-column>
             
             
            <vaadin-grid-column width="15em" resizable>
                <template class="header">
                    <vaadin-grid-sorter path="properties.summary">Description</vaadin-grid-sorter>
                </template>
                <template>[[item.properties.summary]]</template>
            </vaadin-grid-column>
            
            <vaadin-grid-column width="15em" resizable>
                <template class="header">
                    <vaadin-grid-sorter path="properties.qualifiedName">QualifiedName</vaadin-grid-sorter>
                </template>
                <template>[[item.properties.qualifiedName]]</template>
            </vaadin-grid-column>
        </vaadin-grid>
               
    `;
    }

    static get properties() {
        return {
            q: {
                type: Object,
                notify: true,
            },
            searchResp: {
                type: Array,
                notify: true
            },
            item: Object,
            items:{
                type: Object,
                notify: true

            }
        };
    }


    ready() {
        super.ready();
        this.$.tokenAjaxTypes.url = '/api/assets/types';
        this.$.tokenAjaxTypes._go();
    }

    _guidChanged(){
       console.log('guid changed') ;
    }

    _useCaseChanged(){
        console.log('usecase changed');
    }

    _search() {
        console.debug('searching: '+ this.q);
        var types = [];

        this.$.combo.selectedItems.forEach(function(item){
            types.push( item.name);
        });

        this.$.tokenAjax.url = '/api/assets/search?q='+this.q + '&types=' + types;
        this.$.tokenAjax._go();
    }

    _itemClass(item){
        if( item && item.properties.level){
            return 'level'+ item.properties.level;
        } else {
            return '';
        }
    }

    _itemName(item){
        if(item.properties.displayName && item.properties.displayName!=null)
            return item.properties.displayName;
        else if(item.properties.name && item.properties.name!=null)
            return item.properties.name;
        else
            return 'N/A';
    }

    attached() {
        this.loadResources(
            // The specified file only contains the flattened translations for that language:
            'locales/'+this.language+'.json',  //e.g. for es {"hi": "hola"}
            this.language,               // unflatten -> {"es": {"hi": "hola"}}
            true                // merge so existing resources won't be clobbered
        );
    }


}



window.customElements.define('asset-search-view', AssetSearchView);