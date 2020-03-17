/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */


import { mixinBehaviors } from "../../node_modules/@polymer/polymer/lib/legacy/class.js";
import { AppLocalizeBehavior } from "../../node_modules/@polymer/app-localize-behavior/app-localize-behavior.js";
import { PolymerElement, html } from "../../node_modules/@polymer/polymer/polymer-element.js";
import '../shared-styles.js';
import '../token-ajax.js';


class TraversalHistory extends PolymerElement {

    static get template() {
        return html`

            <style include="shared-styles">

                 /* Don't want unordered list for instance summaries to be vertically padded */
                 ul { margin-top:0 ; margin-bottom:0 }

                 .gencolumn {
                     float: left;
                     width: 10%;
                 }

                 .qrycolumn {
                     float: left;
                     width: 30%;
                 }

                 .inscolumn {
                     float: left;
                     width: 60%;
                 }

                 /* Clear floats after the columns */
                 .row:after {
                   content: "";
                   display: table;
                   clear: both;
                 }

            </style>

            <div style="height:325px; width:800px; overflow: auto">
                <dom-repeat items="{{historyList}}">
                    <template>
                        <div class="row">
                            <div class="gencolumn">
                                {{item.gen}}
                            </div>

                            <div class="qrycolumn">
                                {{item.query}}
                            </div>

                            <div class="inscolumn">
                                <ul>
                                    <dom-repeat items={{item.instances}}>
                                        <template><li>{{item.category}} {{item.label}} {{item.guid}}</li>
                                        </template>
                                    </dom-repeat>
                                </ul>
                             </div>
                        </div>
                    </template>
                </dom-repeat>
            </div>
        `; }



    static get properties() {

        return {

            // historyList consists of an array of maps - each map has { gen, query, instanceList } and
            // an instanceList is an array of { cat, label, guid }
            historyList : {
                type : Array,
                value : []
            }



        };
    }


    /*
     * Element is ready
     */
    ready() {
        // Ensure you call super.ready() first to initialise node hash...
        super.ready();

    }
}

window.customElements.define('traversal-history', TraversalHistory);