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
      
    
      
        <token-ajax id="tokenAjax" last-response="{{graphData}}" token="[[token]]" ></token-ajax>

      
        <iron-form id="ultimateSourceForm">
        <form method="get">
            <iron-a11y-keys keys="enter" on-keys-pressed="_ultimateSource"></iron-a11y-keys>
            <paper-input label="Ultimate Source" value="{{guid1}}" no-label-float required autofocus>
                <iron-icon icon="search" slot="prefix" class="icon"></iron-icon>
            </paper-input>
        </form>
       </iron-form>
      
      
        <iron-form id="end2EndLineageForm">
        <form method="get">
            <iron-a11y-keys keys="enter" on-keys-pressed="_endToEndLineage"></iron-a11y-keys>
            <paper-input label="End to End Lineage" value="{{guid2}}" no-label-float required autofocus>
                <iron-icon icon="search" slot="prefix" class="icon"></iron-icon>
            </paper-input>
        </form>
       </iron-form>
      
      
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
            },
            groups : {
                type: Object,
                value:{
                        glossaryTerm: {
                            shape: 'diamond',
                            color: 'yellowgreen'
                        },
                        Column: {
                            color: 'mediumpurple'
                        },
                        RelationalColumn: {
                            color: 'mediumpurple'
                        },
                        TabularColumn: {
                            color: 'mediumpurple'
                        },
                        RelationalTable: {
                            shape: 'box',
                        },
                        Process: {
                            shape: 'parallelogram'
                        },
                        condensedNode: {
                            color: 'turquoise'
                        }
            }
            }
        }
    }


    _graphDataChanged(data) {
        if (data === null || data === undefined) {
            data ={ nodes : {},
                    edges  : {}
                    };
        } else {
            for (var i = 0; i < data.nodes.length; i++) {
                data.nodes[i].title = JSON.stringify(data.nodes[i].properties, "test", '<br>');
            }
        }
        this.$.visgraph.importNodesAndEdges(data.nodes, data.edges);
    }

      _loadData(){
          this.$.visgraph.options.groups = this.groups;
          this.$.tokenAjax.url = '/api/lineage/export?graph=MOCK';
          this.$.tokenAjax._go();

      }


      _ultimateSource(){

          this.$.visgraph.options.groups = this.groups;
          this.$.tokenAjax.url = '/api/lineage/entities/' + this.guid1 + '/ultimate-source?scope=COLUMNVIEW';
          this.$.tokenAjax._go();
      }


      _endToEndLineage(){

          this.$.visgraph.options.groups = this.groups;
          this.$.tokenAjax.url = '/api/lineage/entities/' + this.guid2+ '/end2end?scope=COLUMNVIEW';
          this.$.tokenAjax._go();
      }

}

window.customElements.define('asset-lineage-view', AssetLineageView);
