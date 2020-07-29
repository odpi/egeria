/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import {PolymerElement, html} from '@polymer/polymer';
import {mixinBehaviors} from '@polymer/polymer/lib/legacy/class.js';
import {ItemViewBehavior} from '../common/item';
import '@polymer/app-layout/app-grid/app-grid-style.js';
import '@vaadin/vaadin-icons/vaadin-icons.js';
import '@polymer/paper-styles/paper-styles.js';
import '../shared-styles.js';


import '../shared-styles.js';

class Legend extends mixinBehaviors([ItemViewBehavior], PolymerElement) {

    static get template() {
        return html`
      <style include="shared-styles">   

      </style>
        <div>
           <table>
           <template is="dom-repeat" items="[[legendNodes]]"> 
              <tr>
                 <td><iron-icon style = "fill: {{item.color}};" icon="{{item.shape}}"></iron-icon></td>
                 <td>{{item.group}}</td>
                 <td>{{item.appearances}}</td>   
              </tr>
           </template>
           </table>
        </div>
    `;
    }

    static get properties() {
        return {
            legendNodes: {
                type: Object
            },
            data: {
                type: Object,
                observer: 'dataObserver'
            },
            title: String,
            groups: {
                type: Object
            }
        }
    }

    dataObserver(data, newData) {
        if (data == null && newData != null) {
            data = newData;
        }
        this.legendNodes = [];
        var uniqueObjects = {}
        if (this.groups == null) {
            return;
        }
        for (var i = 0; i < data.length; i++) {
            if (uniqueObjects[data[i].group] === undefined) {
                uniqueObjects[data[i].group] = {
                    group: data[i].group,
                    appearances: 1,
                    color: this.groups[data[i].group].color,
                    shape: this.groups[data[i].group].icon
                }
            } else {
                uniqueObjects[data[i].group].appearances = uniqueObjects[data[i].group].appearances + 1;
            }
        }

        this.legendNodes = Object.values(uniqueObjects);
    }
}

window.customElements.define('legend-div', Legend);
