/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from '@polymer/polymer/polymer-element.js';
import '@polymer/paper-listbox/paper-listbox.js';
import '@polymer/paper-item/paper-item.js';
import '@polymer/paper-item/paper-item-body';
import '@polymer/paper-styles/paper-styles';
import '@polymer/paper-styles/color';
import './shared-styles.js';

class AboutView extends PolymerElement {
  static get template() {
    return html`
       <style include="shared-styles">
        :host {
          display: block;
          padding: 10px 20px;
          background-color:  var(--egeria-background-color);
        }
        div.container {
          min-height: 10em;
          vertical-align: middle;
          margin-left: auto;
          margin-right: auto;
        }
        
      </style>
    
      <token-ajax id="userAjax" last-response="{{a}}" url="/api/about" auto></token-ajax>
        
        <div class="container">
        
            <h2>About</h2>

            <paper-listbox>
            
            <div role="listbox">
              <paper-item>
                <paper-item-body two-line>
                  <div>Application name</div>
                  <div secondary>[[a.name]]</div>
                </paper-item-body>
              </paper-item>
              
              <paper-item>
                <paper-item-body two-line>
                  <div>Maven group</div>
                  <div secondary>[[a.group]]</div>
                </paper-item-body>
              </paper-item>
              
               <paper-item>
                <paper-item-body two-line>
                  <div>Maven artifact id</div>
                  <div secondary>[[a.artifact]]</div>
                </paper-item-body>
              </paper-item>
              
              <paper-item>
                <paper-item-body two-line>
                  <div>Version</div>
                  <div secondary>[[a.version]]</div>
                </paper-item-body>
              </paper-item>
              
               <paper-item>
                <paper-item-body two-line>
                  <div>Build time</div>
                  <div secondary>[[a.time]]</div>
                </paper-item-body>
              </paper-item>
              
            </paper-listbox>
            </div>
            
        </div>
    `;
  }
}

window.customElements.define('about-view', AboutView);
