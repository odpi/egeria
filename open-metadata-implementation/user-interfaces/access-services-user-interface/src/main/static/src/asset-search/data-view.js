/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import { PolymerElement, html } from '@polymer/polymer';
import '@polymer/paper-button/paper-button';
import '@polymer/paper-menu-button';
import '@polymer/paper-dropdown-menu/paper-dropdown-menu';
import '@polymer/paper-listbox';
import '@polymer/paper-item';
import '@polymer/neon-animation/animations/ripple-animation';

import '../shared-styles.js';

class DataView extends PolymerElement {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          display: block;
          padding: 10px 20px;
        }
        
      </style>
      <token-ajax id="tokenAjax" last-response="{{searchResp}}"></token-ajax>
      
      <vaadin-grid id="grid" items="{{data.records}}" theme="row-stripes"
                     on-active-item-changed="_onActiveItemChanged"
                     column-reordering-allowed multi-sort
                     page-size="15" height-by-rows>

            <template is="dom-repeat" items="[[data.columns]]" as="column">

                <vaadin-grid-column>
                    <template class="header">
                        <span  class$="[[_getStyleClass(column)]]">[[_getColumnHeader(column)]]</span>
                    </template>
                    <template>
                        <span class$="[[_getStyleClass(column)]]">
                            [[get(column, item)]]
                        </span>
                    </template>
                </vaadin-grid-column>
            </template>

      </vaadin-grid>
       
    `;
    }

    static get properties() {
        return {
            data: {
                type: Array,
                notify: true
            },
        };
    }

    _search() {
        this.$.searchForm.validate();
        console.log('searching: '+ this.q);
        this.$.tokenAjax.url = '/api/assets/search?q='+this.q;
        this.$.tokenAjax._go();
    }

}

window.customElements.define('data-view', DataView);
