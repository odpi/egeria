/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import {PolymerElement, html} from '@polymer/polymer';
import {mixinBehaviors} from '@polymer/polymer/lib/legacy/class.js';
import {IronFitBehavior} from '@polymer/iron-fit-behavior/iron-fit-behavior.js';
import '@polymer/app-layout/app-grid/app-grid-style.js';
import '@vaadin/vaadin-icons/vaadin-icons.js';
import '@polymer/paper-styles/paper-styles.js';
import '../shared-styles.js';


import '../shared-styles.js';

class Legend extends mixinBehaviors([IronFitBehavior], PolymerElement) {

    static get template() {
        return html`
      <style include="shared-styles">  
          :host { 
            background: rgba(var(--egeria-primary-color-rgb),0.1);
            padding: 20px 10px;
          }
          
      </style>
      <slot></slot>
        <div id="legend-container">
           <dom-repeat items="[[legendNodes]]"> 
            <template> 
                <p> 
                    <iron-icon style = "fill: {{item.color}};" icon="{{item.shape}}"></iron-icon> {{item.group}} {{item.appearances}}
                </p>
            </template>
           </dom-repeat>
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
    ready() {
        super.ready();
        this.addEventListener('dom-change', this.domChanged);
    }

    domChanged(event) {
        this.refit();
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
        const egeriaColor = getComputedStyle(this).getPropertyValue('--egeria-primary-color');
        for (var i = 0; i < data.length; i++) {

            if (uniqueObjects[data[i].group] === undefined) {
                let groupColor = this.groups[data[i].group].color;
                if(groupColor === undefined) groupColor = egeriaColor;
                uniqueObjects[data[i].group] = {
                    group: data[i].group,
                    appearances: 1,
                    color: groupColor,
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
