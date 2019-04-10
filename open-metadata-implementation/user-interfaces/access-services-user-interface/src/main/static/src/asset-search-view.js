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
        vaadin-grid {
          height: calc(100vh - 130px);
        }
      </style>
      <token-ajax id="tokenAjax" last-response="{{searchResp}}"></token-ajax>
      <iron-form id="searchForm">
        <form method="get">
            <iron-a11y-keys keys="enter" on-keys-pressed="_search"></iron-a11y-keys>
            <paper-input label="Search" value="{{q}}" no-label-float required autofocus>
                <iron-icon icon="search" slot="prefix" class="icon"></iron-icon>
            </paper-input>
        </form>
       </iron-form>
        <vaadin-grid id="grid" items="{{searchResp}}" theme="row-stripes"
                     column-reordering-allowed multi-sort>
            <vaadin-grid-selection-column auto-select frozen></vaadin-grid-selection-column>
        
            <vaadin-grid-column width="10em" resizable>
                <template class="header">
                    <vaadin-grid-sorter path="displayName">Name</vaadin-grid-sorter>
                </template>
                <template>[[item.properties.displayName]][[item.properties.name]]</template>
            </vaadin-grid-column>
            
             <vaadin-grid-column width="6em" resizable>
                <template class="header">
                    <vaadin-grid-sorter path="type">Type</vaadin-grid-sorter>
                </template>
                <template>[[item.type]]</template>
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


}

window.customElements.define('asset-search-view', AssetSearchView);