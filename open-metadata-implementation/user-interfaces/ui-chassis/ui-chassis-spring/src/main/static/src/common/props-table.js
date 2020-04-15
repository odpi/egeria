/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import { PolymerElement, html } from '@polymer/polymer';
import {mixinBehaviors} from '@polymer/polymer/lib/legacy/class.js';
import { ItemViewBehavior} from '../common/item';
import '@polymer/paper-listbox/paper-listbox.js';
import '@polymer/paper-item/paper-item.js';
import '@polymer/paper-item/paper-item-body';
import '@polymer/paper-styles/paper-styles';
import '@polymer/app-layout/app-grid/app-grid-style';

import '../shared-styles.js';

class PropsTable extends mixinBehaviors([ItemViewBehavior], PolymerElement) {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          display: block;
          padding: 10px 24px;
        }
       
        .rTable {
           display: table;
           width: 100%;
           border: solid 1px var( --egeria-primary-color );
        }
        .rTableRow {
           display: table-row;
        }
        .rTableRowStripe{
            background-color:  var( --egeria-stripes-color );
        }
        .rTableHead {
            background-color:  var( --egeria-stripes-color );
        }
        .rTableCell, .rTableHead {
           display: table-cell;
           padding: 5px ;
        }
        .rTableHeading {
           display: table-header-group;
           font-weight: bold;
           padding: 5px ;
           color: var( --egeria-primary-color );
        }
        .rTableFoot {
           display: table-footer-group;
           font-weight: bold;
        }
        .rTableRowGroup {
           display: table-row-group;
        }
        
        .label {
            font-weight: bold;
            width: 200px;
        }
        
      </style>
      
        <h3>[[title]]</h3>
        <div class="rTable">
            <dom-if if="[[withHeader]]">
                <template>
                    <div class="rTableRow">
                        <div class="rTableHead">Property</div>
                        <div class="rTableHead">Value</div>
                    </div>
                </template>
            </dom-if>
            <dom-repeat items="[[items]]">
                <template>
                    <div class$="rTableRow [[_rowStripeClass(index)]]">
                        <div class="rTableCell label">[[item.key]]</div>
                        <div class="rTableCell">[[item.value]]</div>
                    </div>
                </template>
            </dom-repeat>
        </div>  
       
    `;
    }

    static get properties() {
        return {
            title : String,
            withHeader : {type: Boolean, value: false},
            withRowStripes : {type: Boolean, value: false}
        }
    }

    _rowStripeClass(index){
        if(this.withRowStripes && index % 2 == 1){
            return 'rTableRowStripe';
        }
        return '';
    }

}

window.customElements.define('props-table', PropsTable);
