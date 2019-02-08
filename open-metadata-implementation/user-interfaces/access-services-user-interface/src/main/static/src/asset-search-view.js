/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import '@polymer/paper-input/paper-input.js';
import '@polymer/paper-material/paper-material.js';
import '@polymer/iron-form/iron-form.js';
import '@polymer/iron-a11y-keys/iron-a11y-keys.js';
import '@polymer/paper-button/paper-button.js';
import '@polymer/paper-styles/paper-styles.js';
import '@polymer/paper-input/paper-input-behavior.js';
import '@polymer/iron-localstorage/iron-localstorage.js';
import '@vaadin/vaadin-grid/vaadin-grid.js';
import '@vaadin/vaadin-grid/vaadin-grid-selection-column.js';
import '@vaadin/vaadin-grid/vaadin-grid-sort-column.js';

import {PolymerElement, html} from '@polymer/polymer/polymer-element.js';
import './shared-styles.js';


class AssetSearchView extends PolymerElement {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          display: block;
          padding: 10px 20px;
        }
      </style>
      <token-ajax id="tokenAjax" last-response="{{searchResp}}"></token-ajax>
      <iron-form id="searchForm">
        <form method="get">
            <iron-a11y-keys keys="enter" on-keys-pressed="_search"></iron-a11y-keys>
            <paper-input label="Search" value="{{q}}" no-label-float required autofocus>
                <iron-icon icon="search" slot="prefix"></iron-icon>
            </paper-input>
            
             <!--<paper-icon-button on-tap="_test" icon="icons:visibility" title="View"></paper-icon-button>-->
        </form>
       </iron-form>
        <vaadin-grid id="grid" items="{{searchResp}}" theme="row-stripes"
                     column-reordering-allowed multi-sort>
            <vaadin-grid-selection-column auto-select frozen></vaadin-grid-selection-column>
        
            <vaadin-grid-column width="5em" resizable>
                <template class="header">
                    <vaadin-grid-sorter path="displayName">Name</vaadin-grid-sorter>
                </template>
                <template>[[item.displayName]]</template>
            </vaadin-grid-column>
            
             <vaadin-grid-column width="5em" resizable>
                <template class="header">
                    <vaadin-grid-sorter path="typeDefName">Type</vaadin-grid-sorter>
                </template>
                <template>[[item.typeDefName]]</template>
            </vaadin-grid-column>
            
            <vaadin-grid-column width="5em" resizable>
                <template class="header">
                    <vaadin-grid-sorter path="typeDefDescription">Description</vaadin-grid-sorter>
                </template>
                <template>[[item.typeDefDescription]]</template>
            </vaadin-grid-column>
            
            <vaadin-grid-column width="5em" resizable>
                <template class="header">
                    <vaadin-grid-sorter path="item.contexts[0].database.name">Database</vaadin-grid-sorter>
                </template>
                <template></template>
            </vaadin-grid-column>
            
             <vaadin-grid-column width="5em" resizable>
                <template class="header">
                    <vaadin-grid-sorter path="item.contexts[0].table.name">Table</vaadin-grid-sorter>
                </template>
                <template></template>
            </vaadin-grid-column>
            
            <vaadin-grid-column width="5em" >
                <template class="header">
                    <vaadin-grid-sorter >Options</vaadin-grid-sorter>
                </template>
                <template>
                    <paper-icon-button on-tap="_itemClick" icon="icons:visibility" title="View"></paper-icon-button>
                </template>
            </vaadin-grid-column>
  
        </vaadin-grid>
       
    `;
    }

    static get properties() {
        return {
            q: {
                type: Object,
                notify: true
            },
            searchResp: {
                type: Array,
                notify: true
            }
        };
    }

    _search() {
        this.$.searchForm.validate();
        console.log('searching: '+ this.q);
        this.$.tokenAjax.url = '/api/assets/search?q='+this.q;
        this.$.tokenAjax._go();
    }

    _test(){
        console.log("clicked on page changes");
        var customEvent = new CustomEvent('open-page', {
            page: "view1"
        });
        this.dispatchEvent(customEvent);
    }

    _itemClick(e){
        console.log("clicked on:" + e.model.item);
        var customEvent = new CustomEvent('open-page', {
            page: "view1"
        });
        this.dispatchEvent(customEvent);
    }

}

window.customElements.define('asset-search-view', AssetSearchView);