/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from '@polymer/polymer/polymer-element.js';
import '@polymer/paper-progress/paper-progress';
import '@polymer/paper-dialog/paper-dialog';
import './props-table';
import './legend';
import '../asset-catalog/asset-tools';
import '../shared-styles.js';
import {mixinBehaviors} from "@polymer/polymer/lib/legacy/class";
import '@polymer/iron-flex-layout/iron-flex-layout-classes.js'

import {ItemViewBehavior} from "./item";

class VisGraph extends mixinBehaviors([ItemViewBehavior], PolymerElement) {
  static get template() {
    return html`
      <style include="shared-styles iron-flex iron-flex-alignment">
          :host {
            display: flex;
            flex-direction: column;
            flex-grow: 1;
            --iron-icon-width:16px;
            --iron-icon-height:16px;
          }
          
          vaadin-app-layout, #vis_container, .vis-network  {
            display: flex;
            flex-direction: column;
            flex-grow: 1;
          }
          
          div.vis-network canvas {
             flex-grow: 1;
             height: fit-content;
          }
          
          paper-dialog.vis-dialog {
            position: fixed;
            top: 116px;
            right: 16px;
            overflow: auto;
          }
          #legend{
            z-index: 10;
            overflow: auto;
          }
        </style>
        
      <token-ajax id="tokenAjaxDetails" last-response="{{typeDetails}}" _handleErrorResponse = "{{handleError()}}"></token-ajax>

        <div id="visLayout" class = "layout horizontal displayLength" style="flex-grow: 1">
            <div id="vis_container"></div>            
            <legend-div id="legend" 
                groups = "[[groups]]"
                visible = "[[!hideLegend]]" 
                data = "[[legendNodes]]"
                vertical-align="top"
                horizontal-align="right"
                auto-fit-on-attach
            ></legend-div>
        </div>
        
        <paper-dialog id="visDialog" class = "vis-dialog">
          <div>
            <a dialog-confirm style="float: right" title="close">
             <iron-icon icon="icons:close" style="width: 24px;height: 24px;"></iron-icon>
            </a>
          </div>
          <asset-tools guid="[[node.id]]" style="display: inline-flex"></asset-tools>
          <props-table  items="[[_attributes(node.properties)]]"   
                        title="[[node.type]]: [[node.displayName]]" 
                        with-row-stripes>
          </props-table>

         <template is ="dom-if" if = "[[typeDetails.type]]">
            <props-table items="[[_attributes(typeDetails.type)]]" title="Type" with-row-stripes ></props-table>
         </template>
         <div></div>
         
        </paper-dialog>
    `;
  }

  static get properties() {
    return {
      network: {
        type: Object,
        observer: 'networkChanged'
      },
      options: {
        type: Object,
        observer: '_optionsChanged',
        value: {
          nodes: {
            shape: 'box',
            margin: 10,
            font: { multi: 'html' }
          },
          edges: {
            smooth: {
              type: 'cubicBezier',
              forceDirection: 'horizontal',
              roundness: 0.8
            },
            arrows:'to'
          },
          interaction: { type : Object },
          layout: { type : Object },
          physics: false,
          groups: { type : Object }
        }
      },

      data: {
        nodes: {},
        edges: {}
      },
      interaction: {
        // tooltipDelay: 200,
        selectable: true,
        hover: false
    },
      groups: {
          type: Object
      },
      legendNodes : {
        type: Object
      }
    };
  }


  setData(data) {
    if(this.data === null || this.data === undefined ) {
      this.data = {
        nodes: {},
        edges: {}
      };
    }

    this.determineLegendData(data.nodes)

    this.data.nodes = data.nodes;
    this.data.edges = data.edges;


    var container = this.$.vis_container;
    this.network = new vis.Network(container, data, this.options);
    var thisElement = this;
    this.network.on('click', function(params) {
       thisElement.handleSelectNode(this.getNodeAt(params.pointer.DOM));
    });
    this.network.stabilize();
  }

toggleLegend() {
    this.hideLegend = !this.hideLegend;
}

networkChanged(newNetwork) {
    if (!newNetwork) {
      return;
    }
    newNetwork.on("stabilizationProgress", function(params) {
      console.debug('graph stabilization in progress');
    }.bind(this));
    newNetwork.once("stabilizationIterationsDone", function() {
      console.debug('graph stabilization is done');
    }.bind(this));
  }

  importNodesAndEdges(nodes, edges) {
    var data = {
      nodes: nodes,
      edges: edges
    };
    this.setData(data);
  }

  setOptions(value) {
    if (this.network === undefined) {
      console.log('network is undefined');
      return false;
    }
    this.options = value;
    this.network.setOptions = this.options;
  }

  handleSelectNode(nodeId) {
    if(nodeId){
      for (var i = 0; i < this.data.nodes.length; i++) {
        if(this.data.nodes[i].id === nodeId) {
          this.node = this.data.nodes[i];
          break;
        }
      }
      if (this.node.group !== "subProcess") {
        this.$.tokenAjaxDetails.url = '/api/assets/' + nodeId;
        this.$.tokenAjaxDetails._go();
      } else {
        this.typeDetails = {
          type : undefined
        }
      }
      this.$.visDialog.open();
    }
  }

  _graphChanged(value) {
    if (value === undefined || value === null) {
      console.log('graph is undefined or null');
      return false;
    }
    this.setData(value);
  }

  _optionsChanged(value) {
    if (value === undefined || value === null) {
      console.log('options are undefined or null');
      return false;
    }
    this.setOptions(value);
  }
  determineLegendData(data) {

    this.legendNodes = [];
    var uniqueObjects = {}
    if (this.groups == null) {
      return;
    }
    const egeriaColor = getComputedStyle(this).getPropertyValue('--egeria-primary-color');
    for (var i = 0; i < data.length; i++) {

      if (uniqueObjects[data[i].group] === undefined) {
        let currentNode = data[i];
        let {icon, groupColor} = this.getIconAndColor(currentNode, egeriaColor);


        uniqueObjects[currentNode.group] = {
          group: currentNode.group,
          appearances: 1,
          color: groupColor,
          shape: icon
        }
      } else {
        uniqueObjects[data[i].group].appearances = uniqueObjects[data[i].group].appearances + 1;
      }
    }

    this.legendNodes = Object.values(uniqueObjects);
  }

  getIconAndColor(currentNode, egeriaColor) {
    let icon;
    let groupColor;
    if (this.groups[currentNode.group] === undefined) {
      icon = undefined;
      groupColor = egeriaColor
    } else {
      icon = this.groups[currentNode.group].icon;
      groupColor = this.groups[currentNode.group].color
      if (groupColor === undefined) {
        groupColor = egeriaColor;
      }
    }
    return {icon, groupColor};
  }
}

window.customElements.define('vis-graph', VisGraph);
