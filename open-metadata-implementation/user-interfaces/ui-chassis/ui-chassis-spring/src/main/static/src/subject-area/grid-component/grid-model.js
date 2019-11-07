/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import './grid-model-instance.js';

import { PolymerElement, html } from "@polymer/polymer/polymer-element.js";
import { MutableData } from '@polymer/polymer/lib/mixins/mutable-data.js';
import '../../shared-styles.js';

class GridModel extends MutableData(PolymerElement) {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          display: inline-block;
          padding: 10px 20px;
        }
      </style>
       <dom-repeat items="{{modelInstances}}" mutable-data>
             <template>
                <grid-model-instance instance="{{item}}" on-change="_onChange">
                </grid-model-instance>
             </template>
       </dom-repeat>
    `;
    }

    static get properties() {
        return {
      artifacts: {
              type: Array,
              notify: true,
              observer: '_artifactsChanged'
      },
      // all artifacts from the server rest call response
      modelInstances: {
        type: Array,
        notify: true,
        observer: '_modelInstancesChanged'
      }
      };
    }
    ready(){
        super.ready();
    }
   _modelInstancesChanged(newValue) {
        console.log('_modelInstancesChanged '+ newValue);
        // create an event. This is an event for the complete array
        this.dispatchEvent(new CustomEvent('model-instances-changed', {
                          bubbles: true,
                          composed: true,
                          detail: newValue  }));

   }
   _artifactsChanged(newValue) {
        console.log('_artifactsChanged '+ newValue);
        this.set('modelInstances', newValue);
   }
   _onChange(newValue) {
        console.log('_onChange '+ newValue);
        // create an event. This is an event for an item change in the array
        this.dispatchEvent(new CustomEvent('model-instance-changed', {
                          bubbles: true,
                          composed: true,
                          detail: newValue  }));
   }
}

window.customElements.define('grid-model', GridModel);