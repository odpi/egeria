/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import { PolymerElement, html } from '@polymer/polymer';

import '../shared-styles.js';
import '../asset-catalog/asset-tools.js';
import '@vaadin/vaadin-split-layout/vaadin-split-layout';
import '@vaadin/vaadin-button/vaadin-button.js';

class GlossaryView extends PolymerElement {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          display: block;
          margin:var(--egeria-view-margin);
          min-height: var(--egeria-view-min-height);
          --iron-icon-width:16px;
          --iron-icon-height:16px;
        }
        .grid-container{
            display: flex;
        }
        vaadin-grid{
            height: auto;
        }
        
        asset-tools{
            margin: 0;
            padding: 0;
            --asset-tools-li-padding: 2px;
        }
        .right{
            float: right;
        }
      </style>
      <app-route route="{{route}}" data="{{routeData}}" tail="{{tail}}"></app-route>
      <token-ajax id="tokenAjax" last-response="{{glossaries}}" url="/api/glossaries" auto></token-ajax>
      <token-ajax id="tokenAjaxCategories" last-response="{{categories}}"></token-ajax>
      <token-ajax id="tokenAjaxTerms" last-response="{{terms}}"></token-ajax>
      <vaadin-split-layout orientation="vertical" style="min-height: inherit">
            <vaadin-split-layout>
                <div class="grid-container"> 
                  <vaadin-grid id="glossaries" items="[[glossaries]]" theme="row-stripes" 
                                   on-active-item-changed="_activeItemChanged"  
                                   column-reordering-allowed multi-sort >
                      <vaadin-grid-column width="10em" resizable>
                          <template class="header">
                              <vaadin-grid-sorter path="displayName">Glossary</vaadin-grid-sorter>
                          </template>
                          <template>
                            [[item.displayName]]
                          </template>
                      </vaadin-grid-column>
                      
                      <vaadin-grid-column width="5em" resizable>
                          <template class="header">
                              <vaadin-grid-sorter path="status">Status</vaadin-grid-sorter>
                          </template>
                          <template>[[item.status]]</template>
                      </vaadin-grid-column>
                      
                      <vaadin-grid-column width="10em" resizable>
                          <template class="header">
                          </template>
                          <template>
                            <div class="right"> 
                                <a href="#/asset-catalog/view/[[item.guid]]" title="view details"><iron-icon icon="vaadin:eye"></iron-icon></a>
                            </div>
                          </template>
                      </vaadin-grid-column>
                        
                      
                  </vaadin-grid>
                </div>
                <div class="grid-container"> 
                    <vaadin-grid id="categories" items="[[categories]]" theme="row-stripes" 
                                    on-active-item-changed="_activeItemChanged"
                                    column-reordering-allowed multi-sort aria-label="Glosssary categories">
                  <vaadin-grid-column width="10em" resizable>
                      <template class="header">
                          <vaadin-grid-sorter path="displayName">Category</vaadin-grid-sorter>
                      </template>
                      <template>[[item.displayName]]</template>
                  </vaadin-grid-column>
                
                  <vaadin-grid-column width="5em" resizable>
                      <template class="header">
                          <vaadin-grid-sorter path="status">Status</vaadin-grid-sorter>
                      </template>
                      <template>[[item.status]]</template>
                  </vaadin-grid-column>
                 
                  <vaadin-grid-column width="10em" resizable>
                      <template class="header">
                      </template>
                      <template> 
                      <div class="right"> 
                        <a href="#/asset-catalog/view/[[item.guid]]" title="view details"><iron-icon icon="vaadin:eye"></iron-icon></a>
                      </div>
                      </template>
                  </vaadin-grid-column>
                </vaadin-grid>
                </div>
            </vaadin-split-layout>
            <div class="grid-container"> 
                <vaadin-grid id="terms" items="[[terms]]" theme="row-stripes" 
                                on-active-item-changed="_activeItemChanged"
                                column-reordering-allowed multi-sort>
                      <vaadin-grid-column width="10em" resizable>
                          <template class="header">
                              <vaadin-grid-sorter path="displayName">Glossary term</vaadin-grid-sorter>
                          </template>
                          <template>
                            <a href="#/asset-catalog/view/[[item.guid]]">[[item.displayName]]</a>
                          </template>
                      </vaadin-grid-column>
                
                      <vaadin-grid-column width="5em" resizable>
                          <template class="header">
                              <vaadin-grid-sorter path="status">Status</vaadin-grid-sorter>
                          </template>
                          <template>[[item.status]]</template>
                      </vaadin-grid-column>
                      
                      <vaadin-grid-column width="5em" resizable>
                          <template class="header">
                             
                          </template>
                          <template><asset-tools class="right" guid="[[item.guid]]"></asset-tools></template>
                      </vaadin-grid-column>
                </vaadin-grid>
            </div>
      </vaadin-split-layout>
      
    `;
    }


    ready() {
        super.ready();
    }

    static get observers() {
        return [
            '_routeChanged(route)'
        ];
    }

    _activeItemChanged(event) {
        const item = event.detail.value;
        if (item) {
            this.$[event.target.id].selectedItems = item ? [item] : [];
            switch (event.target.id) {
                case 'glossaries':
                    this._loadCategories(item.guid);
                    break;
                case 'categories':
                    this._loadTermsByCategory(item.guid);
            }
        }
    }

    _routeChanged(route) {
        if(route.prefix === '/glossary'){
            this.$.tokenAjax.url='/api/glossaries';
            this.$.tokenAjax._go();
            this._loadAllTerms();
            this._loadAllCategories();
        }
    }

    _loadAllTerms() {
        this.$.tokenAjaxTerms.url='/api/glossaries/terms';
        this.$.tokenAjaxTerms._go();
    }

    _loadAllCategories() {
        this.$.tokenAjaxCategories.url='/api/glossaries/categories';
        this.$.tokenAjaxCategories._go();
    }

    _loadTermsByGlossary(guid) {
        this.$.tokenAjaxTerms.url='/api/glossaries/' + guid + "/terms";
        this.$.tokenAjaxTerms._go();
    }

    _loadTermsByCategory(guid) {
        this.$.tokenAjaxTerms.url='/api/glossaries/categories/' + guid + "/terms";
        this.$.tokenAjaxTerms._go();
    }

    _loadCategories(guid){
        this.$.tokenAjaxCategories.url = '/api/glossaries/' + guid + "/categories";
        this.$.tokenAjaxCategories._go();
    }

}

window.customElements.define('glossary-view', GlossaryView);
