/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from '@polymer/polymer/polymer-element.js';
import '@polymer/paper-progress/paper-progress';
import '@polymer/paper-dialog/paper-dialog';
import './props-table';
import './legend';
import '../asset-catalog/asset-tools';
import '../shared-styles.js';
import '@vaadin/vaadin-icons/vaadin-icons.js';
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
            shape: 'image',
            size : 50
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

    for (var i = 0; i < data.nodes.length; i++) {
      this.enhanceNodeWithIcon(data, i);
    }
    var container = this.$.vis_container;
    this.network = new vis.Network(container, data, this.options);
    var thisElement = this;
    this.network.on('click', function(params) {
       thisElement.handleSelectNode(this.getNodeAt(params.pointer.DOM));
    });
    this.network.stabilize();
  }

  enhanceNodeWithIcon(data, i) {
    var egeriaColor;
    if (!data.nodes[i].isQueridNode) {
      egeriaColor = "#a7a6a6";
    } else {
      egeriaColor = getComputedStyle(document.documentElement).getPropertyValue('--egeria-primary-color');
    }

    const nodeComponent =
    `<svg xmlns="http://www.w3.org/2000/svg" width="220px" height="136px">
    <foreignObject x="0" y="0" width="100%" height="100%">
      <div xmlns="http://www.w3.org/1999/xhtml" style="height: 100%; width: 100%;">
      <div style="width: 84%; height: 74%; font-size: 20px;border: solid 5px ` + egeriaColor + `; padding: 12px;
        border-radius: 25px; background: white">` +
        this.getIconAsSVG(data.nodes[i].group) + `  ` + data.nodes[i].label +
        `<div>` + data.nodes[i].groupInfo + ` </div>
        <div>` + (data.nodes[i].dispName === undefined ? '' : data.nodes[i].dispName) + ` </div>` +
        `</div>
      </div>
      </foreignObject>
      </svg>`

    const nodeComponentUrl = "data:image/svg+xml;charset=utf-8," + encodeURIComponent(nodeComponent);

    data.nodes[i].shape = 'image';
    data.nodes[i].image = nodeComponentUrl;
    data.nodes[i].label = ''
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
      this.$.tokenAjaxDetails.url = '/api/assets/' + nodeId;
      this.$.tokenAjaxDetails._go();
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

  getIconAsSVG(group){

      const group_svg = {
          'vaadin:cogs': `<svg xmlns="http://www.w3.org/2000/svg" aria-hidden="true" focusable="false" width="1.5em" height="1.5em" style="-ms-transform: rotate(360deg); -webkit-transform: rotate(360deg); transform: rotate(360deg);" preserveAspectRatio="xMidYMid meet" viewBox="0 0 16 16"><path fill="#626262" d="M12 7V5l-1.2-.4c-.1-.3-.2-.7-.4-1l.6-1.2l-1.5-1.3l-1.1.5c-.3-.2-.6-.3-1-.4L7 0H5l-.4 1.2c-.3.1-.7.2-1 .4l-1.1-.5l-1.4 1.4l.6 1.2c-.2.3-.3.6-.4 1L0 5v2l1.2.4c.1.3.2.7.4 1l-.5 1.1l1.4 1.4l1.2-.6c.3.2.6.3 1 .4L5 12h2l.4-1.2c.3-.1.7-.2 1-.4l1.2.6L11 9.6l-.6-1.2c.2-.3.3-.6.4-1L12 7zM3 6c0-1.7 1.3-3 3-3s3 1.3 3 3s-1.3 3-3 3s-3-1.3-3-3z"/><path fill="#626262" d="M7.5 6a1.5 1.5 0 1 1-3.001-.001A1.5 1.5 0 0 1 7.5 6z"/><path fill="#626262" d="M16 3V2h-.6c0-.2-.1-.4-.2-.5l.4-.4l-.7-.7l-.4.4c-.2-.1-.3-.2-.5-.2V0h-1v.6c-.2 0-.4.1-.5.2l-.4-.4l-.7.7l.4.4c-.1.2-.2.3-.2.5H11v1h.6c0 .2.1.4.2.5l-.4.4l.7.7l.4-.4c.2.1.3.2.5.2V5h1v-.6c.2 0 .4-.1.5-.2l.4.4l.7-.7l-.4-.4c.1-.2.2-.3.2-.5h.6zm-2.5.5c-.6 0-1-.4-1-1s.4-1 1-1s1 .4 1 1s-.4 1-1 1z"/><path fill="#626262" d="M15.4 11.8c-.1-.3-.2-.6-.4-.9l.3-.6l-.7-.7l-.5.4c-.3-.2-.6-.3-.9-.4L13 9h-1l-.2.6c-.3.1-.6.2-.9.4l-.6-.3l-.7.7l.3.6c-.2.3-.3.6-.4.9L9 12v1l.6.2c.1.3.2.6.4.9l-.3.6l.7.7l.6-.3c.3.2.6.3.9.4l.1.5h1l.2-.6c.3-.1.6-.2.9-.4l.6.3l.7-.7l-.4-.5c.2-.3.3-.6.4-.9l.6-.2v-1l-.6-.2zM12.5 14c-.8 0-1.5-.7-1.5-1.5s.7-1.5 1.5-1.5s1.5.7 1.5 1.5s-.7 1.5-1.5 1.5z"/></svg>`,
          'vaadin:road-branches': `<svg xmlns="http://www.w3.org/2000/svg" aria-hidden="true" focusable="false" width="1.5em" height="1.5em" style="-ms-transform: rotate(360deg); -webkit-transform: rotate(360deg); transform: rotate(360deg);" preserveAspectRatio="xMidYMid meet" viewBox="0 0 16 16"><path fill="#626262" d="M12 7V5l-1.2-.4c-.1-.3-.2-.7-.4-1l.6-1.2l-1.5-1.3l-1.1.5c-.3-.2-.6-.3-1-.4L7 0H5l-.4 1.2c-.3.1-.7.2-1 .4l-1.1-.5l-1.4 1.4l.6 1.2c-.2.3-.3.6-.4 1L0 5v2l1.2.4c.1.3.2.7.4 1l-.5 1.1l1.4 1.4l1.2-.6c.3.2.6.3 1 .4L5 12h2l.4-1.2c.3-.1.7-.2 1-.4l1.2.6L11 9.6l-.6-1.2c.2-.3.3-.6.4-1L12 7zM3 6c0-1.7 1.3-3 3-3s3 1.3 3 3s-1.3 3-3 3s-3-1.3-3-3z"/><path fill="#626262" d="M7.5 6a1.5 1.5 0 1 1-3.001-.001A1.5 1.5 0 0 1 7.5 6z"/><path fill="#626262" d="M16 3V2h-.6c0-.2-.1-.4-.2-.5l.4-.4l-.7-.7l-.4.4c-.2-.1-.3-.2-.5-.2V0h-1v.6c-.2 0-.4.1-.5.2l-.4-.4l-.7.7l.4.4c-.1.2-.2.3-.2.5H11v1h.6c0 .2.1.4.2.5l-.4.4l.7.7l.4-.4c.2.1.3.2.5.2V5h1v-.6c.2 0 .4-.1.5-.2l.4.4l.7-.7l-.4-.4c.1-.2.2-.3.2-.5h.6zm-2.5.5c-.6 0-1-.4-1-1s.4-1 1-1s1 .4 1 1s-.4 1-1 1z"/><path fill="#626262" d="M15.4 11.8c-.1-.3-.2-.6-.4-.9l.3-.6l-.7-.7l-.5.4c-.3-.2-.6-.3-.9-.4L13 9h-1l-.2.6c-.3.1-.6.2-.9.4l-.6-.3l-.7.7l.3.6c-.2.3-.3.6-.4.9L9 12v1l.6.2c.1.3.2.6.4.9l-.3.6l.7.7l.6-.3c.3.2.6.3.9.4l.1.5h1l.2-.6c.3-.1.6-.2.9-.4l.6.3l.7-.7l-.4-.5c.2-.3.3-.6.4-.9l.6-.2v-1l-.6-.2zM12.5 14c-.8 0-1.5-.7-1.5-1.5s.7-1.5 1.5-1.5s1.5.7 1.5 1.5s-.7 1.5-1.5 1.5z"/></svg>`,
          'vaadin:tab': `<svg xmlns="http://www.w3.org/2000/svg" aria-hidden="true" focusable="false" width="1.5em" height="1.5em" style="-ms-transform: rotate(360deg); -webkit-transform: rotate(360deg); transform: rotate(360deg);" preserveAspectRatio="xMidYMid meet" viewBox="0 0 16 16"><path fill="#626262" d="M0 2v12h16V2H0zm13 9h-1V8l-3 3V9H3V7h6V5l3 3V5h1v6z"/></svg>`,
          'vaadin:file': `<svg xmlns="http://www.w3.org/2000/svg" aria-hidden="true" focusable="false" width="1.5em" height="1.5em" style="-ms-transform: rotate(360deg); -webkit-transform: rotate(360deg); transform: rotate(360deg);" preserveAspectRatio="xMidYMid meet" viewBox="0 0 16 16"><path fill="#626262" d="M9 5h5v11H2V0h7v5zm1-1V0l4 4h-4z"/></svg>`,
          'vaadin:ticket': `<svg xmlns="http://www.w3.org/2000/svg"  aria-hidden="true" focusable="false" width="1.5em" height="1.5em" style="-ms-transform: rotate(360deg); -webkit-transform: rotate(360deg); transform: rotate(360deg);" preserveAspectRatio="xMidYMid meet" viewBox="0 0 16 16"><path fill="#626262" d="M14 3H2c0 1.1-.9 2-2 2v6c1.1 0 2 .9 2 2h12c0-1.1.9-2 2-2V5c-1.1 0-2-.9-2-2zm-1 9H3V4h10v8z"/><path fill="#626262" d="M4 5h8v6H4V5z"/></svg>`,
          'vaadin:file-process': `<svg xmlns="http://www.w3.org/2000/svg" aria-hidden="true" focusable="false" width="1.5em" height="1.5em" style="-ms-transform: rotate(360deg); -webkit-transform: rotate(360deg); transform: rotate(360deg);" preserveAspectRatio="xMidYMid meet" viewBox="0 0 16 16"><path fill="#626262" d="M12 0H5v6h.7l.2.7l.1.1V1h5v4h4v9H9l.3.5l-.5.5H16V4l-4-4zm0 4V1l3 3h-3z"/><path fill="#626262" d="M5.5 11.5a1 1 0 1 1-2 0a1 1 0 0 1 2 0z"/><path fill="#626262" d="M7.9 12.4L9 12v-1l-1.1-.4c-.1-.3-.2-.6-.4-.9l.5-1l-.7-.7l-1 .5c-.3-.2-.6-.3-.9-.4L5 7H4l-.4 1.1c-.3.1-.6.2-.9.4l-1-.5l-.7.7l.5 1.1c-.2.3-.3.6-.4.9L0 11v1l1.1.4c.1.3.2.6.4.9l-.5 1l.7.7l1.1-.5c.3.2.6.3.9.4L4 16h1l.4-1.1c.3-.1.6-.2.9-.4l1 .5l.7-.7l-.5-1.1c.2-.2.3-.5.4-.8zm-3.4 1.1c-1.1 0-2-.9-2-2s.9-2 2-2s2 .9 2 2s-.9 2-2 2z"/></svg>`,
          'vaadin:table': `<svg xmlns="http://www.w3.org/2000/svg" aria-hidden="true" focusable="false" width="1.5em" height="1.5em" style="-ms-transform: rotate(360deg); -webkit-transform: rotate(360deg); transform: rotate(360deg);" preserveAspectRatio="xMidYMid meet" viewBox="0 0 16 16"><path fill="#626262" d="M0 1v15h16V1H0zm5 14H1v-2h4v2zm0-3H1v-2h4v2zm0-3H1V7h4v2zm0-3H1V4h4v2zm5 9H6v-2h4v2zm0-3H6v-2h4v2zm0-3H6V7h4v2zm0-3H6V4h4v2zm5 9h-4v-2h4v2zm0-3h-4v-2h4v2zm0-3h-4V7h4v2zm0-3h-4V4h4v2z"/></svg>`,
          'vaadin:grid-h': `<svg xmlns="http://www.w3.org/2000/svg" aria-hidden="true" focusable="false" width="1.5em" height="1.5em" style="-ms-transform: rotate(360deg); -webkit-transform: rotate(360deg); transform: rotate(360deg);" preserveAspectRatio="xMidYMid meet" viewBox="0 0 16 16"><path fill="#626262" d="M0 0v16h16V0H0zm5 15H1V1h4v14zm5 0H6V1h4v14zm5 0h-4V1h4v14z"/></svg>`,
          'vaadin:records': `<svg xmlns="http://www.w3.org/2000/svg" aria-hidden="true" focusable="false" width="1.5em" height="1.5em" style="-ms-transform: rotate(360deg); -webkit-transform: rotate(360deg); transform: rotate(360deg);" preserveAspectRatio="xMidYMid meet" viewBox="0 0 16 16"><path fill="#626262" d="M4 9h4v2H4V9z"/><path fill="#626262" d="M16 2h-1V0H5v2H3v1.25L2.4 4H1v1.75L0 7v9h12l4-5V2zM2 5h8v2H2V5zm9 10H1V8h10v7zm1-8h-1V4H4V3h8v4zm2-2.5l-1 1.25V2H6V1h8v3.5z"/></svg>`
      };

    if (this.groups[group] === undefined || this.groups[group].icon === undefined) {
      return '';
    }
    return group_svg[this.groups[group].icon];

  }
}

window.customElements.define('vis-graph', VisGraph);
