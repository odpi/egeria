/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */
import { PolymerElement, html } from "../../node_modules/@polymer/polymer/polymer-element.js";
import '../shared-styles.js';
import {} from '@polymer/polymer/lib/elements/dom-repeat.js';

class BreadCrumb extends PolymerElement{
  static get template() {
    return html`
        <style>
    .breadcrumbs {
            background-color: #eeeeee;
    }    
    
    .crumb,
    .crumb-separator {
            padding: 4px;
            cursor: default;
            color: var(--app-secondary-color);
    }
    .crumb {
            border: 1px solid transparent;
            border-radius: 4px;
            text-decoration: underline;
            
            color: var(--app-secondary-color);
       
        }
    .crumb:hover,
    .crumb:focus {
            background-color: #f2f2f2;
            border: 1px solid #d4d4d4;
        }
    .crumb:active {
            background-color: #e9e9e9;
            border: 1px solid #d4d4d4;
        }
    .crumb:nth-last-child(2) {
            background-color: #d4d4d4;
            border: 1px solid #d4d4d4;
        }
    .crumb-separator:last-child {
            display: none;
        }   
    </style>
        <div class="breadcrumbs"  on-keypress="{{_onKeypress}}">
            <dom-repeat items="{{crumbs}}">
            <template id="crumbs" >
            <span class="crumb"
                    idx="{{item.idx}}" 
                    href="{{item.href}}"
                    on-click="_onActivateCrumb"
                    tabIndex="0">{{item.value}} </span>
                    <span class="crumb-separator">&gt;</span>
            </template>
            </dom-repeat>
        </div>`;
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

    attached() {

    }


    activateCrumb(self, crumb) {
        window.location.href = crumb.href;
        window.location.reload();
    }

    renderItems(self, items) {
        var crumbs = [];

        items.forEach(function(item, idx) {
            var href = item.href;
            if(idx > 0){
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

   _onActivateCrumb (event) {
        this.activateCrumb(this, event.target);
    };
    _onKeypress (event) {
        //'Enter' is pressed
        if (event.keyCode == 13) {
            activateCrumb(this, event.target);
        }
    };

}

window.customElements.define('bread-crumb', BreadCrumb);