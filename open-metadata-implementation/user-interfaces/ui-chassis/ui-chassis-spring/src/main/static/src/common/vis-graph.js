/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from '@polymer/polymer/polymer-element.js';
import  '@polymer/paper-progress/paper-progress';
import '../shared-styles.js';

class VisGraph extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles">
        <style>
          :host {
            display: block;
            box-sizing: border-box;
          }
          
          #vis_container {
            width: 100%;
            height: 100%;
          }
          
          .flex-center-align {
            @apply(--layout-horizontal);
            @apply(--layout-center-center);
          }
          
          .nodeContent {
            position: relative;
            border: 1px solid lightgray;
            /*width: 30%;*/
            height: 100%;
            padding: 10px;
          }
          
          div.vis-tooltip {
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
          
          
        </style>
        
        <div id="vis_container">
          <div class="vis-network" tabindex="900" style="position: relative; overflow: hidden; touch-action: pan-y; 
                  user-select: none; -webkit-user-drag: none; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); 
              width: 100%; height: 100%;">
              <canvas style="position: relative; touch-action: none; user-select: none; -webkit-user-drag: none; 
                      -webkit-tap-highlight-color: rgba(0, 0, 0, 0); 
                       width="600" height="400"></canvas>
                       
               <div class="vis-tooltip" style="left: 239px; top: 119px; visibility: hidden; box-sizing: border-box;"><div style="text-align:center;">test</div></div>
          </div>
         
        </div>
     
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
            margin: 10
          },
          edges: {
            width: 1,
            smooth: {
              type: 'continuous'
            },
            arrows:'to'
          },
          interaction: {
            tooltipDelay: 200,
            hideEdgesOnDrag: true
          },
          layout: {
            hierarchical: {
              enabled: true,
              levelSeparation: 300,
              direction: 'RL'
            }
          },
          physics: false,
          groups: {

          }
        }
      },

      width: {
        type: String,
        value: '100%',
        observer: '_widthChanged'
      },
      height: {
        type: String,
        value: '100%',
        observer: '_heightChanged'
      },
      data: {
        nodes: {type: vis.DataSet},
        edges: {type: vis.DataSet}
      },
      interaction: {
        tooltipDelay: 200,
        selectable: true,
        hover: true
      }
    };
  }

  attached() {
    this.$.vis_container.style.height = this.height;
    this.$.vis_container.style.width = this.width;
  }

  setData(data) {
    console.log('data: ' + data);
    if(this.data === null || this.data === undefined )
       this.data = {
          nodes: {
            type: vis.DataSet
          },
          edges: {
            type: vis.DataSet
          }
        };

    this.data.nodes = data.nodes;
    this.data.edges = data.edges;


    var container = this.$.vis_container;
    this.network = new vis.Network(container, data, this.options);
    var thisElement = this;
    this.network.on('click', function(params) {
      thisElement.handleSelectNode(params);
    });
    this.network.fit();
    //this.network.stabilize();
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
      nodes: new vis.DataSet(nodes),
      edges: new vis.DataSet(edges)
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

  handleSelectNode(params) {
    var eventDetail = {};
    // if (params.nodes.length > 0) {
    //   eventDetail.selectedNode = params.nodes[0];
    //   eventDetail.pointer = params.pointer;
    //   var nodeContent = this.data.nodes.get(params.nodes[0]);
    //   this.$.node_content.innerHTML = JSON.stringify(nodeContent, undefined, 3);
    //   // this.fire('node-selected', eventDetail);
    //   // this.network.fit();
    // }
  }

  _widthChanged(value) {
    if (this.$.vis_container === null) {
      console.log('vis container is null');
      return false;
    }
    this.$.vis_container.style.width = value;
  }

  _heightChanged(value) {
    if (this.$.vis_container === null) {
      console.log('vis container is null');
      return false;
    }
    this.$.vis_container.style.height = value;
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
