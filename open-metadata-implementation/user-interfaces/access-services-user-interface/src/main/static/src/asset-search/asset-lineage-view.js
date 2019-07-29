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
          height: calc(100vh - 80px);
          background-color: white;
        }
        
      </style>
        <div class="container">
            <vis-graph id="visgraph" 
            dotcontent="dinetwork {1 -> 1 -> 2; 2 -> 3; 2 -- 4; 2 -> 1;3 ->7; 5; 6 -> 5; 7 -> 6 }" 
            ></vis-graph>
        </div>
    `;
  }

}

window.customElements.define('asset-lineage-view', AssetLineageView);
