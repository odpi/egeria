/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */

import '@polymer/polymer/polymer-element.js';

import '../shared-styles.js';


/**
*
* This module imports shared-styles and adds styles specific to Rex.
*
*/



const $_documentContainer = document.createElement('template');
$_documentContainer.innerHTML = `<dom-module id="rex-styles">
   <template>

       <style is="custom-style" include="shared-styles">

           * { font-size: 12px ; font-family: sans-serif; }

           .inline-element { display: inline-block;  }

           :host {
               display: inline-block;
               padding: 10px 20px;
           }

           .user-input {
               width: 200px;
               --paper-input-container-input: { font-size: 12px; };
           }

           .vl { border-left: 1px solid black; }

           paper-dropdown-menu {
               width     : 300px;
               height    : 50px;
               display   : block;
               font-size : 12px;
           }

           paper-item {
               font-size: 12px;
               --paper-item-min-height: 15px;
           }

           paper-dropdown-menu.custom {
               --paper-input-container-label : {font-size: 12px; color: #000000; };
               --paper-input-container-input : {font-size: 12px; };
               --paper-input-container       : {font-size: 12px; };
           }

           paper-input {
               --paper-input-container-label : {font-size: 12px;};
               --paper-input-container-input : {font-size: 12px;};
               --paper-input-container       : {font-size: 12px;};
           }

           paper-radio-button {
               --paper-radio-button-checked-color              : var(--egeria-primary-color);
               --paper-radio-button-unchecked-color            : var(--egeria-primary-color);
               --paper-radio-button-unchecked-background-color : #FFFFFF;
           }

           paper-checkbox {
               --paper-checkbox-checked-color: var(--egeria-primary-color);
           }



           /* The --egeria-primary-color may be egeria aqua #71ccdc, orange #ff6200, or anything else actually  */
           /* This style setting is to create a focus background that shows clearly against all these colors,   */
           /* being either darker or lighter but of a similar hue so it is aesthetically OK next to them. This  */
           /* is challenging, which is why a middle gray is chosen below. The code for dynamically creating a   */
           /* a darker (relative to aqua) or lighter (relative to orange) shade of the primary color is         */
           /* included and commented out.                                                                       */
           paper-button:focus {
               outline: none;
               /* The following shadow definition uses a hard-coded darkening - neutral (gray) */
               box-shadow: 0 0 0 4px #BBBBBB;
               /* The following shadow definition also works and is dynamic - but some colors need lightening, others darkening */
               /* which is why this code is not being used currently. It is kept in case you want to use it.                    */
               /* box-shadow: 0 0 0 4px darken(--egeria-primary-color, 50%);                                                    */
           }


       </style>
   </template>
 </dom-module>`;

 document.head.appendChild($_documentContainer.content);
