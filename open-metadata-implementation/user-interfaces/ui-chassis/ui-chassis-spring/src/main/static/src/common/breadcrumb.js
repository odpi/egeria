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
import './breadcrumb-element.js';

class BreadCrumb extends ElementMixin(ThemableMixin(PolymerElement)){
  static get template() {
    return html`
     <style include="lumo-typography">
       .hidden {
          display: none;
        }
       .breadcrumbs {
          padding: 2px 24px;
        }
      </style>
        <div class="breadcrumbs">
            <dom-repeat items="{{crumbs}}">
            <template id="crumbs" >
                <breadcrumb-element href="[[item.href]]" label="[[item.value]]"></breadcrumb-element>
            </template>
            </dom-repeat>
        </div>
`;
    }

    static get properties() {
        return {
            items: {
                type: Array,
                observer: 'setItems'
            },
            crumbs: {
                type: Array
            }
        };
    }

    ready() {
        super.ready();
        this.setAttribute('aria-label', 'breadcrumb');
        this.setAttribute('role', 'navigation');
    }

    renderItems(self, items) {
        var crumbs = [];

        items.forEach(function(item, idx) {
            var href = item.href;
            if(idx > 0 && item.href){
                href = crumbs[idx -1].href + item.href;
            }
            crumbs.push({
                idx: idx,
                value: item.label,
                href: href
            });
        });
        return crumbs;
    }

    setItems(items) {
        this.items = items;
        this.crumbs = this.renderItems(this, items);
    };

}

window.customElements.define('bread-crumb', BreadCrumb);