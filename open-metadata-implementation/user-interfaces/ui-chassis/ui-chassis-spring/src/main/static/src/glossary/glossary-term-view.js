/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import {PolymerElement, html} from '@polymer/polymer';

import '../shared-styles.js';
import '../common/props-table';

class GlossaryTermView extends PolymerElement {
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
      <token-ajax id="tokenAjaxItems" last-response="{{terms}}"></token-ajax>
      
     <div class="container">
           <vaadin-grid id="termsGrid" items="[[terms]]" theme="row-stripes"
                               column-reordering-allowed multi-sort>
              <vaadin-grid-column width="10em" resizable>
                  <template class="header">
                      <vaadin-grid-sorter path="displayName">Name</vaadin-grid-sorter>
                  </template>
                  <template>[[item.displayName]]</template>
              </vaadin-grid-column>
    
              <vaadin-grid-column width="10em" resizable>
                  <template class="header">
                      <vaadin-grid-sorter path="qualifiedName">Qualified Name</vaadin-grid-sorter>
                  </template>
                  <template>[[item.qualifiedName]]</template>
              </vaadin-grid-column>
    
              <vaadin-grid-column width="5em" resizable>
                  <template class="header">
                      <vaadin-grid-sorter path="status">Status</vaadin-grid-sorter>
                  </template>
                  <template>[[item.status]]</template>
              </vaadin-grid-column>
    
             <vaadin-grid-column width="6em" resizable>
                  <template class="header">
                      <vaadin-grid-sorter path="createdBy">Created By</vaadin-grid-sorter>
                  </template>
                  <template>[[item.createdBy]]</template>
              </vaadin-grid-column>
    
             <vaadin-grid-column width="6em" resizable>
                  <template class="header">
                      <vaadin-grid-sorter path="createTime">Create Time</vaadin-grid-sorter>
                  </template>
                  <template>[[item.createTime]]</template>
              </vaadin-grid-column>
    
             <vaadin-grid-column width="6em" resizable>
                  <template class="header">
                      <vaadin-grid-sorter path="updatedBy">Updated By</vaadin-grid-sorter>
                  </template>
                  <template>[[item.updatedBy]]</template>
              </vaadin-grid-column>
    
             <vaadin-grid-column width="6em" resizable>
                  <template class="header">
                      <vaadin-grid-sorter path="updateTime">Updated Time</vaadin-grid-sorter>
                  </template>
                  <template>[[item.updateTime]]</template>
              </vaadin-grid-column>
          </vaadin-grid>
      </div>
       
    `;
    }

    static get observers() {
        return [
            '_routeChanged(routeData.guid)'
        ];
    }

    _routeChanged(guid) {
        console.log(' called with guid ' + guid)
        this.$.tokenAjaxItems.url = '/api/glossaries/' + guid + "/terms";
        this.$.tokenAjaxItems._go();
    }

    connectedCallback() {
        super.connectedCallback();
        console.log('connect glossary-view');

    }
}

window.customElements.define('glossary-terms', GlossaryTermView);