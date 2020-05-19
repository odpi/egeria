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
        ul#menu {
            margin: 0;
            padding: 0;
        }
        ul#menu li {
          display:inline-block;
          padding: 10px;
        }
        ul#menu li:hover {
            background-color: var(--app-background-color);
        }
        .container {
            border: solid 1px var(--egeria-primary-color); 
            padding: 5pt;
        }
      </style>
    <token-ajax id="tokenAjaxSettings" last-response="{{omas}}" url="/api/omas/settings" auto></token-ajax>
    
    <div class="container"> 
        <ul id="menu"> 
            <li> 
                <a href="#/asset-lineage/ultimateSource/[[guid]]" title="Ultimate Source Lineage"><iron-icon icon="vaadin:connect-o" style="transform: rotate(180deg)"></iron-icon></a>
            </li>
            <li> 
                <a href="#/asset-lineage/endToEnd/[[guid]]" title="End2End Lineage"><iron-icon icon="vaadin:cluster"></iron-icon></a>
            </li>
            <li> 
                <a href="#/asset-lineage/ultimateDestination/[[guid]]" title="Ultimate Destination Lineage"><iron-icon icon="vaadin:connect-o"></iron-icon></a>
            </li>
            <li> 
                <a href="#/asset-lineage/glossaryLineage/[[guid]]" title="Glossary Lineage"><iron-icon icon="vaadin:file-tree"></iron-icon></a>
            </li>
            <li> 
                <a href="#/asset-lineage/sourceAndDestination/[[guid]]" title="Source and Destination Lineage"><iron-icon icon="vaadin:exchange"></iron-icon></a>
            </li>
            <li> 
                <a href="#/repository-explorer/[[omas.serverName]]/[[ _encode(omas.baseUrl) ]]/[[guid]]" title="Repository explorer"><iron-icon icon="vaadin:cogs"></iron-icon></a>
            </li>
        </ul>
    </div>
    `;
    }

    _encode(val){
        return btoa(val);
    }

}

window.customElements.define('asset-tools', AssetTools);