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
          
        </style>
        
        <div id="vis_container">
          <div class="vis-network" tabindex="900" style="position: relative; overflow: hidden; touch-action: pan-y; 
                  user-select: none; -webkit-user-drag: none; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); 
              width: 100%; height: 100%;">
              <canvas style="position: relative; touch-action: none; user-select: none; -webkit-user-drag: none; 
                      -webkit-tap-highlight-color: rgba(0, 0, 0, 0); 
                       width="600" height="400"></canvas>
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
          // autoResize: true,
          // height: '100%',
          // width: '100%',
          nodes: {
            fixed: false
          },
          edges: {
            width: 0.15,
            smooth: {
              type: 'continuous'
            }
          },
          layout: {    improvedLayout: false  },
          interaction: {
            tooltipDelay: 200,
            hideEdgesOnDrag: true
          },
          physics: {
            stabilization: false
            // barnesHut: {
            //   gravitationalConstant: -10000,
            //   springConstant: 0.002,
            //   springLength: 150
            // }
          },
          groups: {

          }
        }
      },
      dotcontent: {
        type: String,
        observer: '_dotcontentChanged'
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
      }
    };
  }

  attached() {
    this.$.vis_container.style.height = this.height;
    this.$.vis_container.style.width = this.width;
  }

  setData(data) {
    console.log('data: ' + data);
    console.log('this: ' + this);
    this.data.nodes = data.nodes;
    this.data.edges = data.edges;


    var container = this.$.vis_container;
    this.network = new vis.Network(container, data, this.options);
    var thisElement = this;
    this.network.on('selectNode', function(params) {
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

  importDotString(dotString) {
    var parsedData = vis.network.convertDot(dotString);
    this.options = parsedData.options;
    var data = {
      nodes: parsedData.nodes,
      edges: parsedData.edges
    }
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
    eventDetail.selectedNode = params.nodes[0];
    eventDetail.pointer = params.pointer;
    this.fire('node-selected', eventDetail);
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

  _dotcontentChanged(value) {
    if (value === undefined || value === null) {
      console.log('dotcontent is undefined or null');
      return false;
    }
    console.log('importDotString:'+value)
    this.importDotString(value);
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
