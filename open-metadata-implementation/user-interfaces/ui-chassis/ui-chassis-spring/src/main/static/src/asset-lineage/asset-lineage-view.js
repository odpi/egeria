/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from '@polymer/polymer/polymer-element.js';
import '../shared-styles.js';
import '../common/vis-graph.js';
import '@vaadin/vaadin-radio-button/vaadin-radio-button.js';
import '@vaadin/vaadin-radio-button/vaadin-radio-group.js';
import '@vaadin/vaadin-tabs/vaadin-tabs.js';
import '@vaadin/vaadin-select/vaadin-select.js';
import '@vaadin/vaadin-dropdown-menu/vaadin-dropdown-menu.js';
import '@vaadin/vaadin-item/vaadin-item.js';
import '@vaadin/vaadin-list-box/vaadin-list-box.js';

class AssetLineageView extends PolymerElement {
  static get template() {
    return html`
    <style include="shared-styles">
        :host {
          display: block;
          padding: 2px 20px;
        }
        
        .container {
          margin: auto; 
          height: calc(100vh - 130px);
          background-color: white;
        }
    </style>
      
    <token-ajax id="tokenAjax" last-response="{{graphData}}"></token-ajax>
    <vaadin-tabs id ="useCases" selected="[[_getUseCase(usecase)]]" style="left: -20px; color: var(--egeria-primary-color);" >
      <vaadin-tab value="ultimateSource">Ultimate Source</vaadin-tab>
      <vaadin-tab value="endToEnd">End to End Lineage</vaadin-tab>
      <vaadin-tab value="ultimateDestination">Ultimate Destination</vaadin-tab>
      <vaadin-tab value="glossaryLineage">Glossary Lineage</vaadin-tab>
      <vaadin-tab value="sourceAndDestination">Source and Destination</vaadin-tab>
    </vaadin-tabs>
    
    <div>
        <vaadin-select id="viewsMenu" value="column-view" >
          <template>
            <vaadin-list-box>
              <vaadin-item value="column-view" selected="true">Column View</vaadin-item>
              <vaadin-item value="table-view">Table View</vaadin-item>
            </vaadin-list-box>
            </template>
        </vaadin-select>
    </div>
    
    <div class="container" id="container">
        <vis-graph id="visgraph" data=[[graphData]]></vis-graph>
    </div>
    `;
  }

    ready() {
        super.ready();
        this.$.useCases.addEventListener('selected-changed', () => this.usecase=this.$.useCases.items[this.$.useCases.selected].value);
        this.$.viewsMenu.addEventListener('value-changed', () => this._reload(this.$.useCases.items[this.$.useCases.selected].value, this.$.viewsMenu.value));
    }

    static get properties() {
        return {
            guid: {
                type: String,
                observer: '_guidChanged'
            },
            usecase: {
                type: String,
                observer: '_useCaseChanged'
            },
            usecases:{
                type: Array,
                value:['ultimateSource','endToEnd','ultimateDestination','glossaryLineage','sourceAndDestination' ]
            },
            graphData: {
                type: Object,
                observer: '_graphDataChanged'
            },
            groups : {
                type: Object,
                value: {
                    GlossaryTerm: {
                        shape: 'diamond',
                        color: '#FCF68E'
                    },
                    Column: {
                        color: '#99E17E'
                    },
                    RelationalColumn: {
                        color: '#99E17E'
                    },
                    TabularColumn: {
                        color: '#99E17E'
                    },
                    RelationalTable: {
                        shape: 'box',
                    },
                    Process: {
                        shape: 'parallelogram'
                    },
                    condensedNode: {
                        color: '#71ccdc'
                    }
                }
            }
        }
    }


        _graphDataChanged(data) {
            console.log(data);
            if (data === null || data === undefined) {
                data = { nodes : [],
                         edges : []};
            } else {
                for (var i = 0; i < data.nodes.length; i++) {
                    data.nodes[i].title = JSON.stringify(data.nodes[i].properties, "test", '<br>');
                }
            }
            this.$.visgraph.importNodesAndEdges(data.nodes, data.edges);
        }


      _ultimateSource(guid, view) {
          if (view === null || view === undefined) {
             view  = "column-view";
          }
          this.$.visgraph.options.groups = this.groups;
          this.$.tokenAjax.url = '/api/lineage/entities/' + guid + '/ultimate-source?view=' + view;
          this.$.tokenAjax._go();
      }


      _endToEndLineage(guid, view){
          if (view === null || view === undefined) {
              view  = "column-view";
          }
          this.$.visgraph.options.groups = this.groups;
          this.$.tokenAjax.url = '/servers/<<servername>>/open-metadata/view-services/open-lineage/entities/' + guid + '/end2end?view=' + view;
          this.$.tokenAjax._go();
      }

      _ultimateDestination(guid, view){
          if (view === null || view === undefined) {
              view  = "column-view";
          }
          this.$.visgraph.options.groups = this.groups;
          this.$.tokenAjax.url = '/servers/<<servername>>/open-metadata/view-services/open-lineage/' + guid + '/ultimate-destination?view=' + view;
          this.$.tokenAjax._go();
      }

      _glossaryLineage(guid, view){
          if (view === null || view === undefined) {
              view  = "column-view";
          }
          this.$.visgraph.options.groups = this.groups;
          this.$.tokenAjax.url = '/servers/<<servername>>/open-metadata/view-services/open-lineage/entities/' + guid + '/glossary-lineage?view=' + view;
          this.$.tokenAjax._go();
      }

      _sourceAndDestination(guid, view){
          if (view === null || view === undefined) {
              view  = "column-view";
          }
          this.$.visgraph.options.groups = this.groups;
          this.$.tokenAjax.url = '/servers/<<servername>>/open-metadata/view-services/open-lineage/entities/' + guid + '/source-and-destination?view=' + view;
          this.$.tokenAjax._go();
      }

    _reload(usecase, view) {

        switch (usecase) {
            case 'ultimateSource':
                this._ultimateSource(this.guid, view);
                break;
            case 'endToEnd':
                this._endToEndLineage(this.guid, view);
                break;
            case 'ultimateDestination':
                this._ultimateDestination(this.guid, view);
                break;
            case 'glossaryLineage':
                this._glossaryLineage(this.guid, view);
                break;
            case 'sourceAndDestination':
                this._sourceAndDestination(this.guid, view);
                break;
        }
    }


    _guidChanged() {
        this._reload(this.usecase, this.$.viewsMenu.value);
    }

    _useCaseChanged() {
         var view = this.getAttribute("name");
         var index =  window.location.href.lastIndexOf(view);
         var rootPath = window.location.href.substring(0, view.length + index);
         var newLocation = rootPath + "/" + this.usecase ;
         if( this.guid ){
             newLocation = newLocation + "/" + this.guid;
         }
         window.location.href = newLocation;
         window.dispatchEvent(new CustomEvent('location-changed'));
         this._reload(this.usecase, this.$.viewsMenu.value);
    }

    _getUseCase(usecase){
      return this.usecases.indexOf(usecase);
    }
}

window.customElements.define('asset-lineage-view', AssetLineageView);
