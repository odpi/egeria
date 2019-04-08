/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import '@polymer/polymer/polymer-element.js';

const $_documentContainer = document.createElement('template');
$_documentContainer.innerHTML = `<dom-module id="shared-styles">
  <template>
    
    <style>
        :root{
        
            --egeria-primary-color: 	#71ccdc;
            --egeria-secondary-color: #24272a;          
            --egeria-icon-color: var(--egeria-primary-color);          
          
            --paper-input-container-focus-color: var(--egeria-primary-color);
            --lumo-primary-color: var(--egeria-primary-color);
            
            --paper-spinner-color: var(--egeria-primary-color);
            --paper-spinner-stroke-width: 5px;
        }
        
        .icon {
            color: var(--egeria-icon-color);
        }
      .card {
        margin: 24px;
        padding: 16px;
        color: #757575;
        border-radius: 5px;
        background-color: #fff;
        box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.14), 0 1px 5px 0 rgba(0, 0, 0, 0.12), 0 3px 1px -2px rgba(0, 0, 0, 0.2);
        max-width: 600px;
        width: 100%;
      }

      .circle {
        display: inline-block;
        width: 64px;
        height: 64px;
        text-align: center;
        color: #555;
        border-radius: 50%;
        background: #ddd;
        font-size: 30px;
        line-height: 64px;
      }
      
      

      h1 {
        margin: 16px 0;
        color: #24272a;
        font-size: 22px;
      }
      
      paper-button:not([disabled]) {
        background: var(--egeria-primary-color);
        color: white;
      }
      
      vaadin-grid {
          height: calc(100vh - 130px);
       }

    </style>
  </template>
</dom-module>`;

document.head.appendChild($_documentContainer.content);
