/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from "@polymer/polymer/polymer-element.js";
import '../../shared-styles.js';

class ArtifactGridRest extends PolymerElement {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          display: block;
          padding: 10px 20px;
        }
      </style>
      <token-ajax id="getGridArtifactsAjaxId" last-response="{{lastGetArtifactsResp}}"></token-ajax>
      <token-ajax id="addGridArtifactAjaxId" last-response="{{lastAddArtifactResp}}"></token-ajax>
      <token-ajax id="updateGridArtifactAjaxId" last-response="{{lastUpdateArtifactResp}}"></token-ajax>
      <token-ajax id="deleteGridArtifactAjaxId" last-response="{{lastDeleteArtifactResp}}"></token-ajax>
    `;
    }

    static get properties() {
        return {
            // name of the artifact - first letter is a capital
            name: {
              type: String,
              notify: true
            },
            // lower case plural of the artifact name
            pluralName: {
              type: String,
              notify: true,
              computed: 'computePluralName(name)',
            },
            // lower case artifact name
            artifactName: {
              type: String,
              notify: true,
              computed: 'computeArtifactName(name)',
            },
            // The root of the url used to make rest calls
            urlRoot: {
              type: String,
              notify: true,
              computed: 'computeUrlRoot(pluralName)',
            }, 
                    
      //  rest operation
      restOperation: {
        type: String,
        notify: true,
        observer: '_handleRestOp'
      },
      getArtifactsOperation: {
        type: String,
        notify: true,
        computed: '_computeGetArtifacts(restOperation)',
        observer: 'issueGetArtifacts'
      },
      addArtifactOperation: {
        type: String,
        notify: true,
        computed: '_computeAddArtifact(restOperation)'
      },
      updateArtifactOperation: {
        type: String,
        notify: true,
        computed: '_computeUpdateArtifact(restOperation)'

      },
      deleteOperation: {
        type: String,
        notify: true,
        computed: '_computeDeleteArtifact(restOperation)'
      },
      // supplied artifact passed in on the request.
      suppliedArtifact: {
        type: Object,
        notify: true
      },
      suppliedGuid: {
        type: String,
        notify: true
      },
      //  add artifact response
      lastAddArtifactResp: {
        type: Object,
         // Observer called  when this property changes
        observer: '_lastAddArtifactResp'
      },
      //  update artifact response
      lastUpdateArtifactResp: {
         type: Object,
          // Observer called  when this property changes
         observer: '_lastUpdateArtifactResp'
      },
      //  delete artifact response
      lastDeleteArtifactResp: {
        type: Object,
         // Observer called  when this property changes
        observer: '_lastDeleteArtifactResp'
      },
      //  get artifacts response
      lastGetArtifactsResp: {
        type: Object,
        notify: true,
        observer: '_lastGetArtifactsResp'
      }
      };
    }
    static get observers() {
       return [
            'issueUpdateArtifact(suppliedArtifact,updateArtifactOperation)',
            'issueAddArtifact(suppliedArtifact,addArtifactOperation)',
            'issueDeleteArtifact(suppliedGuid,deleteArtifactOperation)',
       ];
    }
    ready(){
        super.ready();
    }

    _handleRestOp(newValue) {
      console.log("Rest op " +newValue);
    }
    /**
     * create a lower case plural version of the name for use in rest calls. Rest calls are assumed to use plurals to represent the resource in the url.
     */
    computePluralName(singular) {
        var plural;
        if (singular.endsWith('y')) {
            // remove y and add ie
            plural = singular.substring(0,singular.length-1) +"ie";
        } else {
            plural = singular;
        }
        plural = plural.toLowerCase();
        plural = plural + 's';
        return plural;
    }
    computeArtifactName(capitalizedName) {
        return capitalizedName.toLowerCase();
    }
    computeUrlRoot(plural) {
            return "/api/subject-area/"+plural;
    }

 /**
  * Issue get artifacts Ajax rest call to the server
  */
  issueGetArtifacts(newValue) {
      if (newValue) {
           this.$.getGridArtifactsAjaxId.method="get";
           this.$.getGridArtifactsAjaxId.url = this.urlRoot;
           this.$.getGridArtifactsAjaxId._go();

      }
  }
 /**
   * Issue the create rest Ajax call to add a artifact to the server
   */
  issueAddArtifact(artifact,op) {
      if (artifact && op) {
        var body = this.suppliedArtifact;
        body.nodeType = this.name;
        body.class = this.name;
        // copy in values from this.suppliedArtifact

        this.$.addGridArtifactAjaxId.method ="post";
        this.$.addGridArtifactAjaxId.body = body;
        this.$.addGridArtifactAjaxId.url = this.urlRoot;
        this.$.addGridArtifactAjaxId._go();
      }
  }
 /**
   * Issue the create rest Ajax call to add a artifact to the server
   */
  issueUpdateArtifact(artifact,op) {
        if (artifact && op) {
            var body = artifact;
            body.nodeType = this.name;
            body.class = this.name;
            // copy in values from this.suppliedArtifact
            let guid = "";
            if (artifact.systemAttributes) {
                guid = artifact.systemAttributes.guid;
            }

            this.$.updateGridArtifactAjaxId.method ="put";
            this.$.updateGridArtifactAjaxId.body = body;
            this.$.updateGridArtifactAjaxId.url = this.urlRoot+"/"+guid;
            this.$.updateGridArtifactAjaxId._go();
        }
  }
 /**
   * Issue the create rest Ajax call to add a body to the server
   */
  issueDeleteArtifact(guid,op) {
     if (guid && op) {
        this.$.deleteGridArtifactAjaxId.method ="delete";
        // copy in guid
        this.$.deleteGridArtifactAjaxId.url = this.urlRoot+"/"+guid;
        this.$.deleteGridArtifactAjaxId._go();
     }
  }

//  _guidArtifactMapMap(artifacts) {
//        // do we still need this ?
//        var map = null;
//        if (artifacts) {
//            map = {};
//            for (var i = 0; i < artifacts.length; i++) {
//                map[artifacts[i].systemAttributes.guid] = artifacts[i];
//            }
//         }
//        return map;
//  }
  _computeAddArtifact(op) {
        return this._computeActualOperation(op,"addArtifactOperation");
  }
  _computeUpdateArtifact(op) {
        return this._computeActualOperation(op,"updateArtifactOperation");
  }
  _computeDeleteArtifact(op) {
        return this._computeActualOperation(op,"deleteArtifactOperation");
  }
  _computeGetArtifacts(op) {
        return this._computeActualOperation(op,"getArtifactsOperation");
  }
  _computeActualOperation(op,opName) {
        var actualOp;
        if (opName == op) {
           actualOp = true;
        }
        return actualOp;
  }
  _lastGetArtifactsResp() {
      if(this.lastGetArtifactsResp && this.pluralName) {
          if (this.lastGetArtifactsResp[this.pluralName]) {
               this.dispatchEvent(new CustomEvent('rest-get-all', {
                         bubbles: true,
                         composed: true,
                         detail: this.lastGetArtifactsResp[this.pluralName]
               }));
          }
      }
  }
  _lastAddArtifactResp() {
       if (this.lastAddArtifactResp && this.artifactName) {
            var artifact = this.lastAddArtifactResp[this.artifactName];
            if (artifact) {
                this.dispatchEvent(new CustomEvent('rest-add', {
                    bubbles: true,
                    composed: true,
                    detail: artifact
                }));
            }
       }
  }
  _lastUpdateArtifactResp() {
        if(this.lastUpdateArtifactResp && this.artifactName) {
            var artifact = this.lastUpdateArtifactResp[this.artifactName];
            if (this.lastAddUpdateArtifactResp[artifactName]) {
                 this.dispatchEvent(new CustomEvent('rest-update', {
                           bubbles: true,
                           composed: true,
                           detail: artifact
                 }));
            }
        }
  }
  _lastDeleteArtifactResp() {
        if(this.lastDeleteArtifactResp && this.artifactName) {
            var artifact = this.lastDeleteArtifactResp[this.artifactName];
            if (artifact) {
                 this.dispatchEvent(new CustomEvent('rest-delete', {
                           bubbles: true,
                           composed: true,
                           detail: artifact
                 }));
            }
        }
  }
}

window.customElements.define('grid-rest', ArtifactGridRest);