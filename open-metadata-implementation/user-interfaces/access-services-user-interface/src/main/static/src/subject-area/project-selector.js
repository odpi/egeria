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
import '../shared-styles.js';
import '../token-ajax.js';
class ProjectSelector extends PolymerElement {
    static get template() {
        return html`
        <style include="shared-styles">
          :host {
            display: block;
            padding: 10px 20px;
          }
      </style>
       <token-ajax id="addProjectAjaxId" last-response="{{lastAddProjectResp}}" ></token-ajax>
       <token-ajax id="getProjectsAjaxId" last-response="{{lastGetProjectsResp}}"></token-ajax>
       <paper-dropdown-menu label="Projects" id="project-selector" selected="[[selectedProject]]" attr-for-selected="name">
                  <paper-listbox slot="dropdown-content" selected="1">
                        <template is="dom-repeat" items="[[projects]]">
                          <paper-item>[[item.name]]</paper-item>
                        </template>
                  </paper-listbox>
       </paper-dropdown-menu>
       <vaadin-button on-click="onProjectDialogOpen">+</vaadin-button>
       <paper-dialog id="createProjectDialog">

               <form is="iron-form" id="createProjectForm">
                  <label for="projectName">Name</label>
                  <input is="paper-input" id="projectName" type="text" name="name"> <br>

                  <label for="projectQualifiedName">Qualified Name</label>
                  <input is="paper-input" id="projectQualifiedName" type="text" name="qname"><br>

                  <label for="projectDescription">Description</label>
                  <input is="paper-input" id="projectDescription" type="text" name="description"><br>

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
  //  add project response
      lastAddProjectResp: {
        type: Object,
         // Observer called  when this property changes
        observer: '_addProjectRespChanged'
      },
      //  get projects response
      lastGetProjectsResp: {
        type: Object,
        notify: true
      },
       //  selected project
      selectedProject: {
        type: Object,
        notify: true
      },
      // all projects from the server rest call response
      projects: {
        type: Array,
        computed: 'computeProjects(lastGetProjectsResp)',
        notify: true
      }
        };
    }
    ready(){
        super.ready();
        // do the initial load of the projects
        this.getProjects();
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
         alert('Project name required');
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
        this.$.addProjectAjaxId.url = "/api/subject-area/projects";
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
               alert('Error occurred resp :' +  newValue);
          }
      }
  }
 /**
  * Issue get projects Ajax rest call to the server
  */
  getProjects() {
      this.$.getProjectsAjaxId.method ="get";
      this.$.getProjectsAjaxId.url = "/api/subject-area/projects";
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

}

window.customElements.define('project-selector', ProjectSelector);