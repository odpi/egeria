/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import '@polymer/paper-input/paper-input.js';
import '@polymer/paper-material/paper-material.js';
import '@polymer/iron-form/iron-form.js';
import '@polymer/iron-a11y-keys/iron-a11y-keys.js';
import '@polymer/paper-button/paper-button.js';
import '@polymer/paper-styles/paper-styles.js';
import '@polymer/paper-input/paper-input-behavior.js';
import '@vaadin/vaadin-grid/vaadin-grid.js';
import '@vaadin/vaadin-grid/vaadin-grid-selection-column.js';
import '@vaadin/vaadin-grid/vaadin-grid-sort-column.js';
import { PolymerElement, html } from "@polymer/polymer/polymer-element.js";
import {mixinBehaviors} from "@polymer/polymer/lib/legacy/class.js";
import {AppLocalizeBehavior} from "@polymer/app-localize-behavior/app-localize-behavior.js";
import '../shared-styles.js';
import '../token-ajax.js';

class ProjectSelector extends mixinBehaviors([AppLocalizeBehavior], PolymerElement) {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          display: inline-block;
          padding: 10px 20px;
        }
         form  { display: table;      }
                p     { display: table-row;  }
                label { display: table-cell; }
                input { display: table-cell; }
                a     { display: table-cell; }
      </style>
       <token-ajax id="addProjectAjaxId" last-response="{{lastAddProjectResp}}" ></token-ajax>
       <token-ajax id="getProjectsAjaxId" last-response="{{lastGetProjectsResp}}" ></token-ajax>
       <paper-dropdown-menu label="Projects"
                            id="project-selector"
                            selected="[[selectedProject]]"
                            attr-for-selected="name"
                          on-iron-select="_itemSelected">
                      <paper-listbox slot="dropdown-content" selected="1">
                             <template is="dom-repeat" items="[[projects]]">
                                 <paper-item guid=[[item.systemAttributes.guid]]>[[item.name]]</paper-item>
                             </template>
                      </paper-listbox>
       </paper-dropdown-menu>
       <paper-button on-tap="onProjectDialogOpen">+</paper-button>
       <paper-dialog id="createProjectDialog">

               <form is="iron-form" id="createProjectForm">
                  <p>
                  <label for="projectName">Name</label>
                  <input is="paper-input" id="projectName" type="text" name="name"> <br>
                  </p>
                   <p>
                  <label for="projectQualifiedName">Qualified Name</label>
                  <input is="paper-input" id="projectQualifiedName" type="text" name="qname"><br>
                  </p>
                   <p>
                  <label for="projectDescription">Description</label>
                  <input is="paper-input" id="projectDescription" type="text" name="description"><br>
                  </p>
                  <div class="buttons">
                      <paper-button  dialog-dismiss>Cancel</paper-button>
                      <paper-button on-tap="_onProjectDialogCreate">Create</paper-button>
                  </div>
               </form>
        </paper-dialog>
       
    `;
    }

    static get properties() {
        return {
      language: {
        type: String
      },
      //  add project response
      lastAddProjectResp: {
        type: Object,
         // Observer called  when this property changes
        observer: '_addProjectRespChanged'
      },
      //  get project response
      lastGetProjectResp: {
        type: Object,
         // Observer called  when this property changes
        observer: '_getProjectRespChanged'
      },
      //  get projects response
      lastGetProjectsResp: {
        type: Object,
        notify: true
      },
      // all projects from the server rest call response
      projects: {
        type: Array,
        computed: 'computeProjects(lastGetProjectsResp)',
        notify: true
      },
      projectMap: {
              type: Array,
              computed: 'computeProjectMap(projects)',
              notify: true
            }
        };
    }
    ready(){
        super.ready();
        this.getProjects();
    }
    attached() {
            this.loadResources(
                   // The specified file only contains the flattened translations for that language:
                   "locales/subject-area/projectsel_" + this.language + ".json",  //e.g. for es {"hi": "hola"}
                   this.language,               // unflatten -> {"es": {"hi": "hola"}}
                   true                // merge so existing resources won't be clobbered
            );
    }
    onProjectDialogOpen() {
        this.$.createProjectDialog.open();
    }
  /**
   *  Create project dialog form - by issuing the rest call. Note that this dos not submit the form which would close the dialog.
   * It keeps the dialog open so that ut can display any error that occurs
   */
  _onProjectDialogCreate() {
     if (this.$.projectName.value) {
         this.createProjectAJAX();
     } else {
         alert(this.localize("subject-area_projectsel_no_name"));
     }
  }
  /**
   * driven when an item is selected. Issue a custom event to pass up the selected item.
   */
  _itemSelected(e) {
      var selectedItem = e.target.selectedItem;
      if (selectedItem) {
        console.log("selected: " + selectedItem.innerText + ",guid is " + selectedItem.guid);
        var selectedProject = this.projectMap[selectedItem.guid];
        this.dispatchEvent(new CustomEvent('projectSelectionEvent', {
                         bubbles: true,  // bubble up
                         composed: true, // allow the event to go through shadow dom boundaries
                         detail: selectedProject}));


      }
  }

 /**
   * Issue the create rest Ajax call to add a project to the server
   */
  createProjectAJAX() {
        var project = {};
        project.nodeType = "Project";
        project.class = "Project";
        project.name =  this.$.projectName.value;
        if (this.$.projectDescription.value) {
            project.description= this.$.projectDescription.value;
        }
        if (this.$.projectQualifiedName) {
            project.qualifiedName =this.$.projectQualifiedName.value;
        }

        this.$.addProjectAjaxId.method ="post";
        this.$.addProjectAjaxId.body = project;
        this.$.addProjectAjaxId.url = "/servers/<<servername>>/open-metadata/view-services/subject-area/projects";
        this.$.addProjectAjaxId._go();
  }

  /*
   * After an add project - get the projects again so the drop down will be up tp date.
   */
  _addProjectRespChanged(newValue,oldValue) {
      if (newValue.relatedHTTPCode == 200) {
           this.getProjects();
           // close the dialog - a project was successfully created
           this.$.createProjectDialog.close();
      } else {
          if (newValue.exceptionErrorMessage) {
               // this is an error that the omas code generated with message and user action.
               alert('Error occurred: ' +newValue.exceptionErrorMessage + ',user action: ' + newValue.exceptionUserAction);
           } else {
               alert('Good resp :' +  newValue);
          }
      }
  }

 /**
  * Issue get projects Ajax rest call to the server
  */
  getProjects() {
      this.$.getProjectsAjaxId.method ="get";
      this.$.getProjectsAjaxId.url = "/servers/<<servername>>/open-metadata/view-services/subject-area/projects";
      this.$.getProjectsAjaxId._go();
  }
 /**
   * Get the projects from the response
   */
  computeProjects(resp) {
        if (resp) {
           return resp.projects;
        } else {
           return null;
        }
  }
    computeProjectMap(projects) {
          var map = null;
          if (projects) {
             map = {};
             for (var i = 0; i < projects.length; i++) {
               map[projects[i].systemAttributes.guid] = projects[i];
             }
             return map;
          }
          return map;
    }

}

window.customElements.define('project-selector', ProjectSelector);