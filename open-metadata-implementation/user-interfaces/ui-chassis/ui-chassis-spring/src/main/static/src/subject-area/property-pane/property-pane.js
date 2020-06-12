/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import '@polymer/paper-input/paper-input.js';
import '@polymer/paper-material/paper-material.js';
import '@polymer/iron-form/iron-form.js';
import '@polymer/iron-ajax/iron-ajax.js';
import '../../spinner.js';
import '../../token-ajax.js';
import './property-widget.js';

import { PolymerElement, html } from "@polymer/polymer/polymer-element.js";

class PropertyPane extends PolymerElement {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          display: block;
          padding: 10px 20px;
        }
        form  { display: table;      }
        p     { display: table-row;  }
        label { display: table-cell; }
        input { display: table-cell; }
        a     { display: table-cell; }
      </style>
      <div>Properties:</div>
      <form is="iron-form">
      <dom-repeat items="{{valuedprops}}" mutable-data>
            <template>
             <p>
                 <label for="{{item.name}}">{{item.label}}</label>
                 <a href="{{item.url}}" target="_blank">{{item.url}}</a>
                 <property-widget type="{{item.type}}"
                     name ="{{item.name}}"
                     value ="{{item.value}}"
                     component="{{component}}"
                     guid = "{{item.guid}}"
                     readonly = "{{item.readonly}}"
                     on-change="_onPropertyWidgetChange"
                 >
                 </property-widget>
                 <br>
             </p>
            </template>
      </dom-repeat>
      </form>
      <iron-ajax id="getPropertyDefsAjax" url="{{url}}"
                         handle-as="json"
                         last-response="{{props}}"
                         on-error="_handleErrorResponse"
                         loading="{{loading}}"
                         method="get"
                         on-loading-changed="_onLoadingChanged"
       ></iron-ajax>
       <token-ajax id="updatePropertiesAjax" url="{{updateurl}}"
                         body="{{updatebody}}"
                         last-response="{{updatedpropresponse}}"
                         method="put"

       ></token-ajax>
       <spinner-overlay id="defbackdrop" with-backdrop scroll-action="lock"
                   always-on-top
                   no-cancel-on-outside-click
                   no-cancel-on-esc-key>
       </spinner-overlay>

    `;
    }

    static get properties() {
        return {
            props: {
              type: Array,
              notify: true
            },
            updatedpropresponse: {
              type: Object,
              notify: true,
              observer: '_handlePropertyUpdated'
            },
            valuedprops: {
              type: Array,
              notify: true,
              computed: 'computePropertyValues(props,artifact)',
            },
            loading: {
              type: Boolean,
              notify: true
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
            artifact: {
              type: Object,
              notify: true,
              // populate the properties when we have a new artifact.
              observer: 'populateProperties'
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
        this.populateProperties();
    }
    _onPropertyWidgetChange(e) {
         var item = e.model.item;
         console.log("subject-area-view property changed, guid =" + item.guid + ",value="+item.value);
         //kick off the rest call to do the update.
         var body = {};
          // assume the name we are passed is the json file name, the class and nodetype
         body.class = this.name;
         body.nodeType =  this.name;
         body[item.name] = item.value;
         var sa = {};
         sa.guid=  item.guid;
         body.systemAttributes = sa;
         this.$.updatePropertiesAjax.body = body;
         this.$.updatePropertiesAjax.url = "/api/" +this.component + "/" + this.pluralName + "/"+ item.guid;
         this.$.updatePropertiesAjax._go();
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
     * At the values to the field information.
     * @param passedPropertyDefinitions an array of fields, each filed has metadata information like its label, name and type
     * @param passedArtifact the artifact
     * @return return an array that is the passed properties embellished with values from the matching artifact field.
     */
    computePropertyValues(passedPropertyDefinitions,passedArtifact) {
        var valuedProps =[];
        if (passedPropertyDefinitions && passedArtifact) {
             var guid;
             if (passedArtifact.guid) {
                // if this is a system Attributes in which case we have the guid as a property
                guid = passedArtifact.guid;
            } else if (passedArtifact.systemAttributes) {
                // if this is a top level artifact then the guid will be in the system attributes
                guid = passedArtifact.systemAttributes.guid;
            }
            var i;
            for (i=0; i<passedPropertyDefinitions.length;i++) {
               // copy over existing properties
               var valuedProp = passedPropertyDefinitions[i];
               var key = valuedProp.name;

                // find the artifact property that matches the property.
               if (passedArtifact[key]) {
                  // add in the value from the artifact if there is one
                   valuedProp.value = passedArtifact[key];
               }
               // storing the guid in the item when there is not a value allows us to still have the guid when we update on this property
               if (guid) {
                   valuedProp.guid = guid;
               }
               // push into the return array
               valuedProps.push (valuedProp);
            }
        }
        return valuedProps;
    }
    /**
     * create a lower case plural version of the name for use in rest calls. Rest calls are assumed to use plurals to represent the resource in the url.
     */
    computePluralName(singular) {
        var plural;
        if (singular.endsWith('y')) {
            // remove y and add ie
            plural = singular.substring(0,singular.length-1) +"ie";
            plural = plural.toLowerCase();
        }
        plural = plural + 's';
        return plural;
    }

 /**
   * Issue the create rest Ajax call to get properties from the server
   */
  populateProperties() {
        this.$.getPropertyDefsAjax.generateRequest();
  }
  _onLoadingChanged(){
        console.debug('on-loading changed... ' + this.loading);
  }
  _loadingChanged(loading){
        console.debug('loading changed... ' + this.loading);
        if(this.loading){
            this.$.defbackdrop.open();
        }else{
            this.$.defbackdrop.close();
        }
    }
  /**
   * Duplicate of the logic in ajax-token. Used with the non secure rest which gets the resources.
   *
   */
  _handleErrorResponse(evt){
          var status = evt.detail.request.xhr.status;
          var resp   = evt.detail.request.xhr.response;
          console.log('Error response with status code: '+ status);
          // Token is not valid, log out.
           if (status === 401 || status === 403 || resp.exception == 'io.jsonwebtoken.ExpiredJwtException') {
               console.log('Token invalid, logging out.');
               this.dispatchEvent(new CustomEvent('logout', {
                   bubbles: true,
                   composed: true,
                   detail: {greeted: "Bye!", status: status}}));
           }else{
                this.dispatchEvent(new CustomEvent('rest-error-event', {
                                 bubbles: true,
                                 composed: true,
                                 detail: evt.detail
                              }));
           }
  }

}

window.customElements.define('property-pane', PropertyPane);