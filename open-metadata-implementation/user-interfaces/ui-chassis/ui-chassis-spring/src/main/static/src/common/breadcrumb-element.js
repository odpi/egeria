/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */
import { PolymerElement, html } from "../../node_modules/@polymer/polymer/polymer-element.js";
import { ThemableMixin } from '../../node_modules/@vaadin/vaadin-themable-mixin';
import { ElementMixin } from '../../node_modules/@vaadin/vaadin-element-mixin';
import "../../node_modules/@vaadin/vaadin-lumo-styles/typography.js";
import '../shared-styles.js';
import {} from '@polymer/polymer/lib/elements/dom-repeat.js';
import "../../node_modules/@vaadin/vaadin-themable-mixin/vaadin-themable-mixin.js";
import "../../node_modules/@vaadin/vaadin-element-mixin/vaadin-element-mixin.js";

class BreadCrumbElement extends ElementMixin(ThemableMixin(PolymerElement)){
    static get template() {
        return html`
             <style include="lumo-typography">
               .hidden {
                  display: none;
                }
        
                :host(:last-of-type) [part='separator']
                {
                   display: none;
                }
                  
                [part='separator']::after {
                    content: '>';
                    speak: none;
                    color: var(--app-secondary-color);
                }
                a, a:hover, a:active, a:hover{
                    text-decoration: underline;
                    color: var(--egeria-secondary-color);

                }
                [part='crumb'] {
                    padding: 4px;
                    color: var(--egeria-secondary-color);
                    --lumo-text-field-size: var(--lumo-size-m);
                }
                .host(:last-of-type) [part='separator']
                {
                   display: none;
                }
               
            </style>
              <span part="link">
                  <dom-if if="[[href]]" restamp >
                    <template> 
                        <slot id="pageLabel" part="crumb"><a href="[[ href ]]" >[[label]]</a></slot>
                    </template>
                  </dom-if>
                  <dom-if if="[[ !href ]]" restamp >
                    <template> 
                        <strong>[[label]]</strong>
                    </template>
                  </dom-if>
              </span>
              <span class="crumb-separator" part="separator"></span>
`;
    }
}

window.customElements.define('breadcrumb-element', BreadCrumbElement);