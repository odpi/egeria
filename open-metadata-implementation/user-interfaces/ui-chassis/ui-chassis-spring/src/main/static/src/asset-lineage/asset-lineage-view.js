/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from '@polymer/polymer/polymer-element.js';
import '../shared-styles.js';
import '../common/vis-graph.js';
import '@vaadin/vaadin-radio-button/vaadin-radio-button.js';
import '@vaadin/vaadin-radio-button/vaadin-radio-group.js';
import '@vaadin/vaadin-tabs/vaadin-tabs.js';
import '@vaadin/vaadin-item/vaadin-item.js';
import '@vaadin/vaadin-list-box/vaadin-list-box.js';
import '@polymer/paper-toggle-button/paper-toggle-button.js';
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
          display: flex;
          flex-direction: column;
          margin:var(--egeria-view-margin);
          min-height: var(--egeria-view-min-height);
          max-height: var(--egeria-view-min-height);
        }
        
        #container {
          background-color: var( --egeria-background-color );
          display: flex;
          flex-direction: column;
          flex-grow: 1;
        }
        #useCases {
            color: var(--egeria-primary-color);
            width: fit-content;
            margin: auto;
        }
        ul#menu, ul#menu li {
            padding-left: 0;
            margin-right: 16px;
        }
    </style>
    <app-route route="{{route}}" pattern="/:usecase/:guid" data="{{routeData}}" tail="{{tail}}"></app-route>
    <token-ajax id="tokenAjax" last-response="{{graphData}}"></token-ajax>
    <token-ajax id="tokenAjaxDetails" last-response="{{item}}" ></token-ajax>

        <div>
        <vaadin-tabs id ="useCases"  selected="[[ _getUseCase(routeData.usecase) ]]" >
          <vaadin-tab value="ultimateSource" >
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
        <ul id="menu"> 
            <li> 
                <paper-toggle-button id="processToggle" checked>
                    ETL Jobs
                </paper-toggle-button>
            </li>
            <li> 
                <paper-toggle-button id="glossaryTermToggle" disabled>
                    Glossary Terms
                </paper-toggle-button>
            </li>
         </ul>
    </div>       
   
    <dom-if if="[[_noGuid(routeData)]]" restamp="true">
    <template > 
        <div class="warning" style="display: block; margin: auto">
            <p>Please use  
                <a href="[[rootPath]]#/asset-catalog/search" > 
                        [ Asset Catalog ]
                </a>
                to select an asset to view lineage.
            </p>
        </div>
    </template>
    </dom-if>
    
    <div id="container" >
        <vis-graph id="visgraph" groups=[[groups]] data=[[graphData]] ></vis-graph>
    </div> 
       
    `;
  }

    ready() {
        super.ready();
            var thisElement = this;
            this.$.tokenAjax.addEventListener('error', () =>
                thisElement.$.visgraph.importNodesAndEdges([], []));

            this.$.processToggle.addEventListener('change', () =>
                this._reload(this.$.useCases.items[this.$.useCases.selected].value, this.$.processToggle.checked));
            this.$.glossaryTermToggle.addEventListener('changed', () =>
                this._reload(this.$.useCases.items[this.$.useCases.selected].value, this.$.processToggle.value));
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
            graphInteraction: {
                type: Object,
                value: {
                    hideEdgesOnDrag: true
                }
            },
            graphLayout: {
                type: Object,
                value: {
                    hierarchical: {
                        enabled: true,
                        levelSeparation: 250,
                        direction: 'LR'
                    }
                }
            },
            groups : {
                type: Object,
                value: {
                    GlossaryTerm: {
                        icon: 'vaadin:records'
                    },
                    Column: {
                        icon: 'vaadin:grid-h'
                    },
                    RelationalColumn: {
                        icon: 'vaadin:road-branches'
                    },
                    TabularColumn: {
                        icon: 'vaadin:tab'
                    },
                    RelationalTable: {
                        icon: 'vaadin:table'
                    },
                    Process: {
                        icon: 'vaadin:file-process'
                    },
                    subProcess: {
                        icon: 'vaadin:cogs'
                    },
                    condensedNode: {
                        icon: 'vaadin:cogs'
                    },
                    GlossaryCategory : {
                        icon: 'vaadin:ticket'
                    },
                    DataFile : {
                        icon: 'vaadin:file'
                    },
                    AssetZoneMembership : {
                        icon: 'vaadin:handshake'
                    }
                }
            }
        }
    }

    _noGuid(routeData){
        return routeData === undefined
            || routeData.guid === undefined
            || routeData.guid === "";
    }

    _noLineage(routeData){
        return !this._noGuid(routeData)
            && this.graphData
            && this.graphData.nodes
            && this.graphData.nodes.length == 0;
    }

    connectedCallback() {
        super.connectedCallback();
        this.$.visgraph.options.groups = this.groups;
        this.$.visgraph.options.interaction = this.graphInteraction;
        this.$.visgraph.options.layout = this.graphLayout;
        this.$.visgraph.options.physics = false;
        }

    static get observers() {
        return [
            '_routeChanged(route)'
        ];
    }

    _routeChanged(route){
        if ( this.route.prefix === '/asset-lineage' ){
            if( this.routeData && this.routeData.guid ) {
                this.$.tokenAjaxDetails.url = '/api/assets/' + this.routeData.guid;
                this.$.tokenAjaxDetails._go();
            }
            this._reload(this.routeData.usecase, this.$.processToggle.checked);
        }
    }

    _parseProperties(props){
        var obj = {};
        Object.keys(props).forEach(
            (key)=> {
                var newKey = key.split("vertex--").join("");
                obj[newKey] = ""+props[key];
            }
        );
        return obj;
    }

    _graphDataChanged(data, newData) {
        if (data === null || data === undefined) {
            if (newData && newData != null) {
                data = newData;
            } else {
                data = {
                    nodes: [],
                    edges: []
                };
            }
        }
        if(data.nodes.length == 0 ){
            this.dispatchEvent(new CustomEvent('show-modal', {
                bubbles: true,
                composed: true,
                detail: { message: "No lineage information available", level: 'info'}}));
        }
        const egeriaColor = getComputedStyle(this).getPropertyValue('--egeria-primary-color');
        for (var i = 0; i < data.nodes.length; i++) {
            let displayName;
            if(data.nodes[i].properties && data.nodes[i].properties!==null && data.nodes[i].properties!==undefined ){
                data.nodes[i].properties = this._parseProperties(data.nodes[i].properties);

                if (data.nodes[i].properties['tableDisplayName'] != null
                    && data.nodes[i].properties['tableDisplayName'] != undefined) {
                    displayName = data.nodes[i].properties['tableDisplayName']
                }
            }
            data.nodes[i].displayName = data.nodes[i].label;
            data.nodes[i].type = data.nodes[i].group;
            data.nodes[i].label = '<b>'+data.nodes[i].label+'</b>';
            data.nodes[i].label += ' \n\n' + this._camelCaseToSentence(data.nodes[i].group);
            if (displayName != null) {
                data.nodes[i].label += ' \n\n From : ' + displayName;
            }
            if (data.nodes[i].id === this.routeData.guid){
                data.nodes[i].group ='QueryNode';
                data.nodes[i].color = {
                    background:'white',
                    border:egeriaColor,
                    highlight:{background:egeriaColor,border:'#a7a7a7'},
                    hover:{background:'white',border:'#a7a7a7'}
                };
            }else{
                data.nodes[i].color = {
                    background:'white',
                    border:'#a7a6a6',
                    highlight:{background:egeriaColor,border:'#a7a7a7'},
                    hover:{background:'white',border:'#a7a7a7'}
                };
            }
        }
        if (!this._hideIncludeGlossaryTerms(this.routeData.usecase) && this.$.glossaryTermMenu.value === "false" ) {
            var filteredNodes = [];
            var nodesToRemove = [];
            var filteredEdges = [];
            for (var i = 0; i < data.nodes.length; i++) {
                if (data.nodes[i].group !== "GlossaryTerm") {
                    filteredNodes.push(data.nodes[i]);
                } else {
                    nodesToRemove.push(data.nodes[i])
                }
            }
            for (var j = 0; j < data.edges.length; j++) {
                var edgeRemoved = false;
                for (var i = 0; i < nodesToRemove.length; i++) {
                    if (data.edges[j].from === nodesToRemove[i].id) {
                        for (var k = 0; k < data.edges.length; k++) {
                            if (data.edges[k].to === nodesToRemove[i].id) {
                                edgeRemoved = true;
                                filteredEdges.push({
                                    to : data.edges[j].to,
                                    from : data.edges[k].from,
                                    label : data.edges[k].label
                                })
                            }
                        }
                    }
                }
                if (edgeRemoved === false) {
                    filteredEdges.push(data.edges[j])
                }
            }
            data.nodes = filteredNodes;
            data.edges = filteredEdges;
        }
        this.$.visgraph.importNodesAndEdges(data.nodes, data.edges);
    }

    _ultimateSource(guid, includeProcesses) {
        if (includeProcesses === null || includeProcesses === undefined) {
         includeProcesses  = "true";
        }
        this.$.visgraph.options.groups = this.groups;
        this.$.tokenAjax.url = '/api/lineage/entities/' + guid + '/ultimate-source?includeProcesses=' + includeProcesses;
        this.$.tokenAjax._go();
    }

    _endToEndLineage(guid, includeProcesses){
      if (includeProcesses === null || includeProcesses === undefined) {
          includeProcesses  = "true";
      }
      this.$.visgraph.options.groups = this.groups;
      this.$.tokenAjax.url = '/api/lineage/entities/' + guid + '/end2end?includeProcesses=' + includeProcesses;
      this.$.tokenAjax._go();
    }

    _ultimateDestination(guid, includeProcesses){
      if (includeProcesses === null || includeProcesses === undefined) {
          includeProcesses  = "true";
      }
      this.$.visgraph.options.groups = this.groups;
      this.$.tokenAjax.url = '/api/lineage/entities/' + guid + '/ultimate-destination?includeProcesses=' + includeProcesses;
      this.$.tokenAjax._go();
    }

    _glossaryLineage(guid, includeProcesses){
      if (includeProcesses === null || includeProcesses === undefined) {
          includeProcesses  = "true";
      }
      this.$.visgraph.options.groups = this.groups;
      this.$.tokenAjax.url = '/api/lineage/entities/' + guid + '/glossary-lineage?includeProcesses=' + includeProcesses;
      this.$.tokenAjax._go();
    }

    _sourceAndDestination(guid, includeProcesses){
      if (includeProcesses === null || includeProcesses === undefined) {
          includeProcesses  = "true";
      }
      this.$.visgraph.options.groups = this.groups;
      this.$.tokenAjax.url = '/api/lineage/entities/' + guid + '/source-and-destination?includeProcesses=' + includeProcesses;
      this.$.tokenAjax._go();
    }

    _reload(usecase, includeProcesses) {
        if (this.routeData.guid !== undefined && this.routeData.guid !== "")
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

    _hideIncludeGlossaryTerms(usecase) {
        // return !("ultimateDestination" === usecase || "ultimateSource" === usecase) ;
        return true;
    }
}

window.customElements.define('asset-lineage-view', AssetLineageView);
