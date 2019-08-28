/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from '@polymer/polymer/polymer-element.js';
import '../shared-styles.js';
import '../common/vis-graph.js';
import '@vaadin/vaadin-radio-button/vaadin-radio-button.js';
import '@vaadin/vaadin-radio-button/vaadin-radio-group.js';

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
      
    <token-ajax id="tokenAjax" last-response="{{graphData}}"></token-ajax>
    <vaadin-radio-group id ="radioUsecases" class="select-option-group" name="radio-group" value="ultimateSource"  role="radiogroup" >
      <vaadin-radio-button value="ultimateSource" class="select-option" role="radio" type="radio" >Ultimate Source</vaadin-radio-button>
      <vaadin-radio-button value="endToEnd" class="select-option" role="radio" type="radio">End to End Lineage</vaadin-radio-button>
    </vaadin-radio-group>
          
    <div class="container" id="container">
        <vis-graph id="visgraph" data=[[graphData]]></vis-graph>
    </div>
    `;
  }

    ready() {
        super.ready();
        this.$.radioUsecases.addEventListener('value-changed', () => this._usecaseChanged(this.$.radioUsecases.value) );
    }

    static get properties() {
        return {
            guid: {
                type: String,
                observer: '_ultimateSource'
            },
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
            console.log(data);
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


      _ultimateSource(guid){
          this.$.visgraph.options.groups = this.groups;
          this.$.tokenAjax.url = '/api/lineage/entities/' + guid + '/ultimate-source?scope=COLUMNVIEW';
          this.$.tokenAjax._go();
      }


      _endToEndLineage(guid){
          this.$.visgraph.options.groups = this.groups;
          this.$.tokenAjax.url = '/api/lineage/entities/' + guid+ '/end2end?scope=COLUMNVIEW';
          this.$.tokenAjax._go();
      }

    _usecaseChanged(value) {
        switch (value) {
            case 'ultimateSource':
                this._ultimateSource(this.guid);
                break;
            case 'endToEnd':
                this._endToEndLineage(this.guid);
                break;
        }
    }
}

window.customElements.define('asset-lineage-view', AssetLineageView);
