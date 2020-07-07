/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from '@polymer/polymer/polymer-element.js';
import  '@polymer/paper-progress/paper-progress';
import '@polymer/paper-dialog/paper-dialog';
import '@polymer/paper-drawer-panel/paper-drawer-panel';
import './props-table';
import '../asset-catalog/asset-tools';
import '../shared-styles.js';
import {mixinBehaviors} from "@polymer/polymer/lib/legacy/class";
import {ItemViewBehavior} from "./item";

class VisGraph  extends mixinBehaviors([ItemViewBehavior], PolymerElement) {
  static get template() {
    return html`
      <style include="shared-styles">
          :host {
            display: flex;
            flex-direction: column;
            flex-grow: 1;
            --iron-icon-width:16px;
            --iron-icon-height:16px;
          }
          
          #vis_container {
            display: flex;
            flex-direction: column;
            flex-grow: 1;
            /*align-items: stretch;*/
          }
          
          .vis-network {
            display: flex;
            flex-direction: column;
            flex-grow: 1;
          }
          
          div.vis-network canvas {
             flex-grow: 1;
          }
          
          .vis-tooltip {
            position: absolute;
            visibility: hidden;
            padding: 5px;
            white-space: nowrap;
            font-family: verdana;
            font-size: 14px;
            color: #000000;
            background-color: #f5f4ed;
            -moz-border-radius: 3px;
            -webkit-border-radius: 3px;
            border-radius: 3px;
            border: 1px solid #808074;
            box-shadow: 3px 3px 10px rgba(0, 0, 0, 0.2);
            pointer-events: none;
            z-index: 5;
          }
          paper-dialog.vis-dialog {
            position: fixed;
            top: 116px;
            right: 16px;
            width: 600px;
            height: 400px;
            overflow: auto;
          }
          
        </style>
        <div id="vis_container"></div>
        <paper-dialog id="visDialog" class="vis-dialog">
          <div>
            <asset-tools guid="[[node.id]]" style="display: inline-flex"></asset-tools>
            <paper-button dialog-confirm style="float: right">Close</paper-button>
          </div>
          <props-table items="[[_attributes(node.properties)]]"  title="[[node.type]]: [[node.displayName]]" with-row-stripes ></props-table>
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

    this.data.nodes = data.nodes;
    this.data.edges = data.edges;


    var container = this.$.vis_container;
    this.network = new vis.Network(container, data, this.options);
    var thisElement = this;
    this.network.on('click', function(params) {
       thisElement.handleSelectNode(this.getNodeAt(params.pointer.DOM));
    });
    this.network.on('stabilizationProgress', function(params) {
      console.debug('graph stabilization in progress');
    });

    this.network.on('stabilizationIterationsDone', function(params) {
      console.debug('graph stabilization is done');

    });
    this.network.on('stabilized', function(params) {
      console.debug('stabilized!');

    });
    this.network.stabilize();
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
        if(this.data.nodes[i].id === nodeId){
          this.node = this.data.nodes[i];
          break;
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

}

window.customElements.define('vis-graph', VisGraph);
