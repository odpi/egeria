/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import { PolymerElement, html } from '@polymer/polymer';

import '../shared-styles.js';

class GlossaryView extends PolymerElement {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          display: block;
          margin: 10px 24px;
          padding: 5px;
          background-color:  var(--egeria-background-color);
          min-height: calc(100vh - 85px);
        }
        
      </style>
      <app-route route="{{route}}" pattern="/:guid" data="{{routeData}}" tail="{{tail}}"></app-route>
      <token-ajax id="tokenAjaxDetails" last-response="{{glossaries}}" url="/api/glossaries" auto></token-ajax>

      <div class="container">
        <div>[[glossaries]]</div>
      </div>
       <vaadin-grid id="grid" items="[[glossaries]]" theme="row-stripes"
                           column-reordering-allowed multi-sort>
          <vaadin-grid-column width="10em" resizable>
              <template class="header">
                  <vaadin-grid-sorter path="displayName">Name</vaadin-grid-sorter>
              </template>
              <template>
                [[item.typeDefName]]
              </template>
          </vaadin-grid-column>

           <vaadin-grid-column width="6em" resizable>
              <template class="header">
                  <vaadin-grid-sorter path="createdBy">Created By</vaadin-grid-sorter>
              </template>
              <template>[[item.createdBy]]</template>
          </vaadin-grid-column>


          <vaadin-grid-column width="15em" resizable>
              <template class="header">
                  <vaadin-grid-sorter path="status">Status</vaadin-grid-sorter>
              </template>
              <template>[[item.status]]</template>
          </vaadin-grid-column>

          <vaadin-grid-column width="15em" resizable>
              <template class="header">
                  <vaadin-grid-sorter path="qualifiedName">QualifiedName</vaadin-grid-sorter>
              </template>
              <template>[[item.qualifiedName]]</template>
          </vaadin-grid-column>
      </vaadin-grid>
    `;
    }

    _retrieve_all_glossaries() {
        console.debug('searching: '+ this.q);
        var types = [];

        this.$.combo.selectedItems.forEach(function(item){
            types.push( item.name);
        });

        this.$.tokenAjax.url = '/api/glossaries';
        this.$.tokenAjax._go();
    }

    static get observers() {
        return [
            '_routeChanged(route)'
        ];
    }

    _routeChanged(route) {
        if(route.prefix === '/glossary'){
            this.$.tokenAjaxDetails.url='/api/glossaries';
            this.$.tokenAjaxDetails._go();
        }
    }


    connectedCallback() {
        super.connectedCallback();
        console.log('connect glossary-view');

    }

}

window.customElements.define('glossary-view', GlossaryView);
