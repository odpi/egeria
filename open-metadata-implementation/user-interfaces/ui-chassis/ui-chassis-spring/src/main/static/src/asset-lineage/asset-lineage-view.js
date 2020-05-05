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
import {mixinBehaviors} from "@polymer/polymer/lib/legacy/class";
import {ItemViewBehavior} from "../common/item";

class AssetLineageView extends mixinBehaviors([ItemViewBehavior], PolymerElement) {
    constructor() {
        super();
    }
  static get template() {
    return html`
    <style include="shared-styles">
        :host {
          display: block;
          margin: 0 24px;
        }
        
        .container {
          margin: auto; 
          height: calc(100vh - 130px);
          background-color: white;
        }
        #useCases {
            color: var(--egeria-primary-color);
            width: fit-content;
            margin: auto;
        }
    </style>
    
    <app-route route="{{route}}" pattern="/:usecase/:guid" data="{{routeData}}" tail="{{tail}}"></app-route>
     
    <token-ajax id="tokenAjax" last-response="{{graphData}}"></token-ajax>
    <token-ajax id="tokenAjaxDetails" last-response="{{items}}" ></token-ajax>
    
    <vaadin-tabs id ="useCases" selected="[[_getUseCase(routeData.usecase)]]" >
      <vaadin-tab value="ultimateSource">
        <a href="[[rootPath]]#/asset-lineage/ultimateSource/[[routeData.guid]]" tabindex="-1" rel="noopener"> 
            Ultimate Source
        </a>
      </vaadin-tab>
      <vaadin-tab value="endToEnd">
        <a href="[[rootPath]]#/asset-lineage/endToEnd/[[routeData.guid]]" tabindex="-1"  rel="noopener"> 
            End to End Lineage
        </a>
      </vaadin-tab>
      <vaadin-tab value="ultimateDestination">
        <a href="[[rootPath]]#/asset-lineage/ultimateDestination/[[routeData.guid]]" tabindex="-1"  rel="noopener"> 
            Ultimate Destination
        </a>
      </vaadin-tab>
      <vaadin-tab value="glossaryLineage">
        <a href="[[rootPath]]#/asset-lineage/glossaryLineage/[[routeData.guid]]" tabindex="-1" rel="noopener"> 
            Glossary Lineage
        </a>
      </vaadin-tab>
      <vaadin-tab value="sourceAndDestination">
        <a href="[[rootPath]]#/asset-lineage/sourceAndDestination/[[routeData.guid]]" tabindex="-1" rel="noopener"> 
            Source and Destination
        </a>
      </vaadin-tab>
    </vaadin-tabs>
    
    <div>
        <vaadin-select id="processMenu" value="false" >
          <template>
            <vaadin-list-box>
              <vaadin-item value="true" selected>Include ETL Jobs</vaadin-item>
              <vaadin-item value="false">Exclude ETL Jobs</vaadin-item>
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
        this.$.processMenu.addEventListener('value-changed', () => this._reload(this.$.useCases.items[this.$.useCases.selected].value, this.$.processMenu.value));
    }

    static get properties() {
        return {
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

    static get observers() {
        return [
            '_routeChanged(routeData)'
        ];
    }

    _routeChanged(routeData){
        this._reload(this.routeData.usecase, this.$.processMenu.value);
    }

    _graphDataChanged(data) {
        console.debug(data);
        if (data === null || data === undefined) {
            data = { nodes : [],
                     edges : []};
        } else {
            for (var i = 0; i < data.nodes.length; i++) {
                data.nodes[i].title = JSON.stringify(data.nodes[i].properties, "", '<br>');
                if(data.nodes[i].properties['tableDisplayName'])
                      data.nodes[i].label += ' \n Table : ' + data.nodes[i].properties['tableDisplayName'];
            }
        }
        this.$.visgraph.importNodesAndEdges(data.nodes, data.edges);
    }

  _ultimateSource(guid, includeProcesses) {
      if (includeProcesses === null || includeProcesses === undefined) {
         includeProcesses  = "false";
      }
      this.$.visgraph.options.groups = this.groups;
      this.$.tokenAjax.url = '/api/lineage/entities/' + guid + '/ultimate-source?includeProcesses=' + includeProcesses;
      this.$.tokenAjax._go();
  }


      _endToEndLineage(guid, includeProcesses){
          if (includeProcesses === null || includeProcesses === undefined) {
              includeProcesses  = "false";
          }
          this.$.visgraph.options.groups = this.groups;
          this.$.tokenAjax.url = '/api/lineage/entities/' + guid + '/end2end?includeProcesses=' + includeProcesses;
          this.$.tokenAjax._go();
      }

      _ultimateDestination(guid, includeProcesses){
          if (includeProcesses === null || includeProcesses === undefined) {
              includeProcesses  = "false";
          }
          this.$.visgraph.options.groups = this.groups;
          this.$.tokenAjax.url = '/api/lineage/entities/' + guid + '/ultimate-destination?includeProcesses=' + includeProcesses;
          this.$.tokenAjax._go();
      }

      _glossaryLineage(guid, includeProcesses){
          if (includeProcesses === null || includeProcesses === undefined) {
              includeProcesses  = "false";
          }
          this.$.visgraph.options.groups = this.groups;
          this.$.tokenAjax.url = '/api/lineage/entities/' + guid + '/glossary-lineage?includeProcesses=' + includeProcesses;
          this.$.tokenAjax._go();
      }

      _sourceAndDestination(guid, includeProcesses){
          if (includeProcesses === null || includeProcesses === undefined) {
              includeProcesses  = "false";
          }
          this.$.visgraph.options.groups = this.groups;
          this.$.tokenAjax.url = '/api/lineage/entities/' + guid + '/source-and-destination?includeProcesses=' + includeProcesses;
          this.$.tokenAjax._go();
      }

    _reload(usecase, includeProcesses) {

        switch (usecase) {
            case 'ultimateSource':
                this._ultimateSource(this.routeData.guid, includeProcesses);
                break;
            case 'endToEnd':
                this._endToEndLineage(this.routeData.guid, includeProcesses);
                break;
            case 'ultimateDestination':
                this._ultimateDestination(this.routeData.guid, includeProcesses);
                break;
            case 'glossaryLineage':
                this._glossaryLineage(this.routeData.guid, includeProcesses);
                break;
            case 'sourceAndDestination':
                this._sourceAndDestination(this.routeData.guid, includeProcesses);
                break;
        }
    }
    _getUseCase(usecase){
      return this.usecases.indexOf(usecase);
    }
}

window.customElements.define('asset-lineage-view', AssetLineageView);
