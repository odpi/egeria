/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import { PolymerElement, html } from "@polymer/polymer/polymer-element.js";
import '../property-pane/property-pane.js';
import './grid-model.js';
import './grid-rest.js';
import './grid-resource-rest.js';
import './grid-view.js';
import '../../shared-styles.js';
/**
 * This is the controller (from the model view controller pattern) for the grid component.
 * Changes to the model result in events that this class then listens for and takes the appropriate next action to update the view
 * Changes to the view result in events that this class listens for and takes the appropriate next action.
 * The actions that this class takes when it receives an event is to update one or more property values.
 * The properties in this class, are specified as input parameters for other web components. Polymer property binding means that
 * property changes in the child components cause the child component to do processing.
 *
 */
class GridController extends PolymerElement {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          padding: 10px 20px;
       }
       .container { float: left; width: 100%; }
       .container__left { float: left; width: calc(100%-80px); resize }
       .container__right { float: right; width: 80px;  resize}

      </style>
      <!--  the model from model-view-controller -->
      <grid-model id='GridModel' model-instances='[[inputModelInstances]]'></grid-model>
      <!-- there is a json file held on server that contains metadata about the artifact that this grid is displaying -->
      <grid-resource-rest id='DefsForGrid' name='[[name]]'></grid-resource-rest>
      <!-- there is a json file held on server that contains metadata system attributes, which include its guid -->
      <grid-resource-rest id='systemAttributeDefsForGrid' name='systemAttributes'></grid-resource-rest>

      <div>
           <!-- when there are propertyDefs including the column names instandient the grid rest component-->
           <template is="dom-if" if="[[propertyDefs]]" restamp="true">
               <!-- used to issue the omas rest call  -->
              <grid-rest id='GridRest'
                     name='[[name]]'
                     rest-operation='[[restOperation]]'
                     supplied-artifact='[[suppliedArtifact]]'
                     supplied-guid='[[suppliedGuid]]'
               ></grid-rest>

              <div>
                 <div>
                      <!-- the view from model-view-controller -->
                      <grid-view id='GridView' name='[[name]]' defs='[[propertyDefs]]' model-instances='[[modelInstancesForView]]' class='grid' {width=100%}></grid-view>
                 </div>
                 <div>
         <!--   <property-pane id="selected-artifact" name="" artifact="{{selected}}" component="subject-area"></property-pane> -->
                </div>

              </div>
          </template>
      </div>
    `;
    }
    static get properties() {
            return {
            // name of the artifact - singular name starting with a capital letter
            name: {
              type: String,
              notify: true
            },
            // selected
            selected: {
              type: Object,
              notify: true
            },
            //  selected project - needed for non project artifacts as the project to create artifact in
            selectedProject: {
              type: Object,
              notify: true
            },
            //  selected glossary - needed for authoring terms and categories
            selectedGlossary: {
              type: Object,
              notify: true
            },
            inputModelInstances: {
              type: Array,
              notify: true,
              observer: '_handleInputModelInstancesChanged'
            },
            modelInstancesForView: {
              type: Array,
              notify: true,
              observer: '_handleModelInstancesForViewChanged'
            },
            // the definition of the artifact
            artifactDefs: {
              type: Array,
              notify: true
            },
            // the definition of the system attributes
            systemAttributesDefs: {
              type: Array,
              notify: true
            },
            // a combination of the artifact definitions and the system attributes. This is in a form that can easily be
            // combined with the actual values in the grid.
            propertyDefs: {
                      type: Array,
                      notify: true,
                      computed: '_computePropertyDefs(artifactDefs,systemAttributesDefs)',
                      observer: '_handlePropertyDefsChanged'
            },
            // this is a string representing the rest operation
            restOperation: {
                     type: String,
                     notify: true
            },
            // some rest calls e.g. adding and updating need the artifact as input.
            suppliedArtifact: {
                     type: String,
                     notify: true
            },
            // some rest calls need the guid as input.
            suppliedGuid: {
                     type: String,
                     notify: true
            },
            // a boolean indicating that the grid should be refreshed
            refresh: {
                     type: Boolean,
                     notify: true,
                     observer: '_handleRefreshChanged'
            }

            };
    }
    ready(){
        super.ready();
        // add handlers to catch successful rest calls
        this.addEventListener('rest-get-all'  , e => {this._handleGetArtifacts(e)});
        this.addEventListener('rest-add'    , e => {this._handleAdd(e)});
        this.addEventListener('rest-update' , e => {this._handleUpdate(e)});
        this.addEventListener('rest-delete' , e => {this._handleDelete(e)});
        var artifactDefEventName = "rest_" + this.name.toLowerCase() + "_defs";
        this.addEventListener(artifactDefEventName, e => {this._handleDefsChanged(e)});
        this.addEventListener('rest_systemattributes_defs'   , e => {this._handleSystemAttributeDefsChanged(e)});
        this.addEventListener('model-instances-changed',e => {this._handleModelInstancesChanged(e)});
        this.addEventListener('model-instance-changed',e => {this._handleModelInstanceChanged(e)});
        this.addEventListener('view-properties-changed',e => {this._handleViewPropertiesChanged(e)});

        var newNameEvent = 'view-new-' + this.name.toLowerCase() + '-glossary-name';
        this.addEventListener(newNameEvent,e => {this._handleNewName(e)});
    }
     /**
       * The  defs are in one file. It contains an attribute with type object that represents the
       * system attributes. The system attributes are defined in a separate file. The propertyDefs are the
       *  definitions with the system attributes folded into them.
       *
      */
    _computePropertyDefs(aDefs,saDefs) {
        var propertyDefs= [];
        if (aDefs && saDefs)  {

           for (let i=0;i<aDefs.length;i++) {
               if (aDefs[i].name == 'systemAttributes') {
                   propertyDefs.push(...saDefs);
               } else {
                   propertyDefs.push(aDefs[i]);
               }
           }
        }
        return propertyDefs;
    }
    _handleSystemAttributeDefsChanged(e) {
        if (e.detail) {
           this.set('systemAttributesDefs', e.detail);
           this.notifyPath('systemAttributesDefs');
        }
    }
    _handleDefsChanged(e) {
        if (e.detail) {
           this.set('artifactDefs', e.detail);
           this.notifyPath('artifactDefs');
        }
    }
    /**
     * we have received artifacts from the server - so update the model with them.
     */
     _handlePropertyDefsChanged(newValue) {
         console.log('controller _handlePropertyDefsChanged')
         // issue get artifacts rest call
         this.set('restOperation', 'getArtifactsOperation');
         this.notifyPath('restOperation');
     }
    _handleInputModelInstancesChanged(newValue) {
         console.log('_handleInputModelInstancesChanged');

    }
    _handleRefreshChanged(newValue) {
          if (newValue) {
              this.restOperation = 'getArtifactsOperation';
          }
    }
   _handleModelInstancesForViewChanged(newValue) {
         console.log('_handleModelInstancesForViewChanged');
    }
    /**
     * view properties have changed - issue the rest call to update
     */
    _handleViewPropertiesChanged(e) {
        if (e.detail) {
           this.suppliedArtifact = this._calculateFromItem(e.detail);
           this.restOperation = "updateArtifactOperation";
           this.notifyPath('restOperation');
        }
    }
    _handleNewName(e) {
        if (e.detail) {
           let  artifact= {};
           artifact.name = e.detail;
           this.suppliedArtifact= artifact;;
           this.restOperation = "addArtifactOperation";
           this.notifyPath('restOperation');
        }
    }

    _calculateFromItem(item) {
         let artifact= {};
         artifact[item.name] = item.value;
         if (item.guid) {
            var sa = {};
            sa.guid=  item.guid;
            artifact.systemAttributes = sa;
         }
         return ;
    }
    _refresh() {
            //TODO be more granular in the refresh
            this.restOperation =  'getArtifactsOperation';
            this.notifyPath('restOperation');
    }
    _handleGetArtifacts(e){
        if (e.detail) {
            console.log('_handleGetArtifacts ');
            this.set('inputModelInstances',e.detail);
            this.notifyPath('inputModelInstances');
            this.refresh = false;
        }
    }
    _handleAdd(e) {
        this._refresh();
    }
    _handleUpdate(e) {
        this._refresh();
    }
    _handleDelete(e) {
        this._refresh();
    }
    /**
     * populate view with modelInstances
     */
    _handleModelInstancesChanged(e) {
        console.log("controller _handleModelInstancesChanged" + e.detail);
        // update view
        if (e.detail) {
           this.set('modelInstancesForView',e.detail);
           this.notifyPath('modelInstancesForView');
        }
    }
    _handleModelInstanceChanged(e) {
        console.log("_handleModelInstanceChanged" + e.detail);
    }
}

window.customElements.define('grid-controller', GridController);