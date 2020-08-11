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
            margin-bottom: 15px;
            padding: 20px 10px;
            max-height: none;
            display: flex;
            flex-grow: 1;
            flex-flow: column;
            flex-direction: column;
            height: 100%;
            ;
          }
          
          #legend-container {
            padding: 20px;
          }
          
      </style>
      
      <slot></slot>
      <dom-if if="[[visible]]"> 
        <template> 
         <div>
          <iron-icon icon="vaadin:angle-right" on-tap="_toggle"></iron-icon>
         </div>
         <div id="legend-container">
           <dom-repeat items="[[data]]"> 
        <div id="legend-container">
           <dom-repeat items="[[data]]"> 
            <template> 
                <p> 
                    <iron-icon style = "fill: {{item.color}};" icon="{{item.shape}}"></iron-icon> {{item.group}} {{item.appearances}}
                </p>
            </template>
           </dom-repeat>
        </div>
        </template>
      </dom-if>
      <dom-if if="[[!visible]]"> 
        <template> 
         <div>
          <iron-icon icon="vaadin:angle-left" on-tap="_toggle"></iron-icon>
         </div>
        </template>
      </dom-if>
       
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
            },
            visible: {
                type: Boolean,
                value: false
            }
        }
    }

    ready() {
        super.ready();
        this.addEventListener('dom-change', () => {this.refit()});
        window.addEventListener('resize', () => {this.refit()});
        window.addEventListener('scroll', () => {this.refit()});
    }

    _toggle(){
        this.visible = !this.visible;
    }
    dataObserver (data, newData) {
        console.log(data)
        console.log(newData)
    }

}

window.customElements.define('legend-div', Legend);
