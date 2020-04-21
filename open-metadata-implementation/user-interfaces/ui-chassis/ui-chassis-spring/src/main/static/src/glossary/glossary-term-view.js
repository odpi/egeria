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

class GlossaryTermView extends mixinBehaviors([ItemViewBehavior], PolymerElement) {
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
      <token-ajax id="tokenAjaxDetails" last-response="{{items}}" ></token-ajax>
      
     <div class="container">
       <vaadin-grid id="grid" items="[[items]]" theme="row-stripes"
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

          <vaadin-grid-column width="15em" resizable>
              <template class="header">
                  <vaadin-grid-sorter path="status">Status</vaadin-grid-sorter>
              </template>
              <template>[[item.status]]</template>
          </vaadin-grid-column>

          <vaadin-grid-column width="15em" resizable>
              <template class="header">
                  <vaadin-grid-sorter path="version">Version</vaadin-grid-sorter>
              </template>
              <template>[[item.version]]</template>
          </vaadin-grid-column>

          <vaadin-grid-column width="15em" resizable>
              <template class="header">
                  <vaadin-grid-sorter path="guid">GUID</vaadin-grid-sorter>
              </template>
              <template>[[item.guid]]</template>
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
      
      <dom-if if="[[ !items ]]" restamp>
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
        this.$.tokenAjaxDetails.url='/api/glossaries/' + guid + "/terms";
        this.$.tokenAjaxDetails._go();
    }

}

window.customElements.define('glossary-term-view', AssetDetailsView);