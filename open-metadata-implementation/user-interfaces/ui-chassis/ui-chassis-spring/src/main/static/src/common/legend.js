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
           <dom-repeat items="[[data]]"> 
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
    dataObserver (data, newData) {
        console.log(data)
        console.log(newData)
    }

}

window.customElements.define('legend-div', Legend);
