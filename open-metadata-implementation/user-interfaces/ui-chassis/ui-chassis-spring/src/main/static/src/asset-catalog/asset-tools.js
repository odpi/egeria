/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import { PolymerElement, html } from '@polymer/polymer';

import '../shared-styles.js';
import '@polymer/paper-item/paper-item.js';
import '@polymer/paper-item/paper-item-body.js';
import '@polymer/paper-styles/paper-styles.js';
import '@vaadin/vaadin-icons/vaadin-icons.js';

class AssetTools extends PolymerElement {
    static get template() {
        return html`
  
      <style include="shared-styles">
        :host {
          display: block;
          padding: 10px 24px;
        }
        
        iron-icon {
         --iron-icon-fill-color: var(--egeria-button-ink-color);
        }
        paper-button {
          --paper-button-ink-color: var(--egeria-button-ink-color);
        }
        paper-button:hover {
          filter: brightness(110%);
        }
       ul#menu {
            margin: 0;
            padding: 0;
        }
      </style>
    <token-ajax id="tokenAjaxSettings" last-response="{{omas}}" url="/api/omas/settings" auto></token-ajax>
        <ul id="menu"> 
            <li> 
                <a href="#/asset-lineage/ultimateSource/[[guid]]" title="Ultimate Source Lineage">
                    <paper-button raised>
                    <iron-icon icon="vaadin:connect-o" style="transform: rotate(180deg)"></iron-icon>
                    <div>&nbsp;Source</div>
                    </paper-button>
                </a>
            </li>
            <li> 
                <a href="#/asset-lineage/endToEnd/[[guid]]" title="End2End Lineage">
                    <paper-button raised>
                    <iron-icon icon="vaadin:cluster"></iron-icon>
                    <div>&nbsp;End2End</div>
                    </paper-button>
                </a>
            </li>
            <li> 
                <a href="#/asset-lineage/ultimateDestination/[[guid]]" title="Ultimate Destination Lineage"> 
                    <paper-button raised>
                    <iron-icon icon="vaadin:connect-o"></iron-icon>
                    <div>&nbsp;Dest</div>
                    </paper-button>
                </a>
            </li>
            <li> 
                <a href="#/asset-lineage/glossaryLineage/[[guid]]" title="Glossary Lineage">
                    <paper-button raised>
                    <iron-icon  icon="vaadin:file-tree"></iron-icon>
                    <div>&nbsp;Glossary</div>
                    </paper-button>
                </a>
            </li>
            <li> 
                <a href="#/asset-lineage/sourceAndDestination/[[guid]]" title="Source and Destination Lineage">
                <paper-button raised>
                    <iron-icon  icon="vaadin:exchange"></iron-icon>
                    <div>&nbsp;Source & Dest</div>
                    </paper-button>
                </a>
            </li>
            <li> 
                <a href="#/repository-explorer/[[omas.serverName]]/[[ _encode(omas.baseUrl) ]]/[[guid]]" title="Repository explorer">
                    <paper-button raised>
                    <iron-icon icon="vaadin:cogs"></iron-icon>
                    <div>&nbsp;REX</div>
                    </paper-button>
                </a>
            </li>
        </ul>
    `;
    }

    _encode(val){
        return btoa(val);
    }

}

window.customElements.define('asset-tools', AssetTools);