/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from "@polymer/polymer/polymer-element.js";
import './property-cell.js';
import '../../shared-styles.js';

class PropertyRow extends PolymerElement {

 static get template() {
        return html`
      <style include="shared-styles">
         :host {
              padding: 10px 20px;
              display: table-row;
         }
         .table_cell {
            display:table-cell;
            border-style: solid;
            border-color: LightGrey;}
      </style>

      <dom-repeat items="{{row}}" mutable-data>
            <template>
          <!--  <div class='table_cell'>{{item.value}}</div>  -->
            <div class='table_cell'>
              <property-cell type="{{item.type}}"
                                 name ="{{item.name}}"
                                 value ="{{item.value}}"
                                 guid = "{{item.guid}}"
                                 readonly = "{{item.readonly}}"
                                 on-change="_onPropertyWidgetChange">
             </property-cell>
            </div>
            </template>
      </dom-repeat>
    `;
    }

    static get properties() {
        return {
          
            updatedpropresponse: {
              type: Object,
              notify: true,
              observer: '_handlePropertyUpdated'
            },
            row: {
                type: Array,
                notify: true,
                value: function() { return []; }
            },
            component: {
              type: String,
              notify: true
            },
            name: {
              type: String,
              notify: true
            },
            pluralName: {
              type: String,
              notify: true,
              computed: 'computePluralName(name)',
            },
            url: {
                 type: String,
                 computed: 'computeUrl(component,name)',
                 notify: true
            },
            updateurl: {
                 type: String,
                 notify: true
            },
            updatebody: {
                 type: Object,
                 notify: true
            }
        };
    }
    ready(){
        super.ready();
    }
    _onPropertyWidgetChange(e) {
         var item = e.model.item;
         console.log("property-row property changed, guid =" + item.guid + ",value="+item.value);
    }
    _handleModelInstanceChanged(newValue) {
         console.log("subject-area row model instance changed " + newValue);
    }
    _onPropertyWidgetChange(e) {
         var item = e.model.item;
         console.log("subject-area row property changed, guid =" + item.guid + ",value="+item.value);

        // create an event. Indicating that the view has been updated
        this.dispatchEvent(new CustomEvent('view-properties-changed', {
                          bubbles: true,
                          composed: true,
                          detail: item  }));


//         //kick off the rest call to do the update.
//         var body = {};
//          // assume the name we are passed is the json file name, the class and nodetype
//         body.class = this.name;
//         body.nodeType =  this.name;
//         body[item.name] = item.value;
//         var sa = {};
//         sa.guid=  item.guid;
//         body.systemAttributes = sa;
//         this.$.updateRowPropertiesAjax.body = body;
//         this.$.updateRowPropertiesAjax.url = "/api/" +this.component + "/" + this.pluralName + "/"+ item.guid;
//         this.$.updateRowPropertiesAjax._go();
    }
    _handlePropertyUpdated(updateResponse) {
         //  update the artifact to force a redraw.
         // assume that the response key is the lower cased version of the name.
         var responseKey = this.name.toLowerCase()

         if (updateResponse[responseKey]) {
             this.artifact = updateResponse[responseKey];
         }
    }

    computeUrl(compName,artifactName) {
            var urlStr ="/properties/" +compName + "/" + artifactName +".json";
            return urlStr;
    }
    /**
     * create a lower case plural version of the name for use in rest calls. Rest calls are assumed to use plurals to represent the resource in the url.
     */
    computePluralName(singular) {
        var plural;
        if (singularName.endsWith('y')) {
            // remove y and add ie
            plural = singular.substring(0,singular.length-1) +"ie";
        } else {
            plural = singular;
        }
        plural = plural.toLowerCase();
        plural = plural + 's';
        return plural;
    }

}

window.customElements.define('property-row', PropertyRow);