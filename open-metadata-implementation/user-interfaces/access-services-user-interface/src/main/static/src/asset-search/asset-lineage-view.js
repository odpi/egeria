/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from '@polymer/polymer/polymer-element.js';
import '../shared-styles.js';
import '../common/vis-graph.js';

class AssetLineageView extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles">
        :host {
          display: block;
          padding: 10px;
        }
        
        .container {
          margin: auto; 
          height: calc(100vh - 130px);
          background-color: white;
        }
        
      </style>
      
    
      
        <token-ajax id="tokenAjax"  last-response="{{graphData}}" token="[[token]]" ></token-ajax>

        <paper-button on-click="_loadData"> Go </paper-button>
      
        <div class="container" id="container">
        
            <!--mock-up graph-->
            <vis-graph id="visgraph" 
            data=[[graphData]]>
            </vis-graph>
        </div>
    `;
  }

    static get properties() {
        return {
            token: Object,
            graphData: {
                type: Object,
                observer: '_graphDataChanged'
            }
        }
    }


    _graphDataChanged(data){
       this.$.visgraph.importNodesAndEdges(data.nodes, data.edges);
    }

      _loadData(){
          var groups = {
              'Glossary Term': {
                  shape: 'diamond',
                  color: 'yellowgreen'
              },
              Column: {
                  color: 'mediumpurple'
              },
              Table: {
                  shape: 'box',
              },
              Process: {
                  shape: 'parallelogram'
              }
          };
          this.$.visgraph.options.groups = groups;
          this.$.tokenAjax.url = '/api/lineage/export?graph=MOCK';
          this.$.tokenAjax._go();

      }

}

window.customElements.define('asset-lineage-view', AssetLineageView);
