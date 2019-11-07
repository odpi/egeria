/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */
import "@polymer/paper-input/paper-input.js";
import "@polymer/paper-material/paper-material.js";
import "@polymer/iron-form/iron-form.js";
import "@polymer/iron-a11y-keys/iron-a11y-keys.js";
import "@polymer/paper-button/paper-button.js";
import "@polymer/paper-styles/paper-styles.js";
import "@polymer/paper-dropdown-menu/paper-dropdown-menu.js";
import "@polymer/paper-listbox/paper-listbox.js";
import "@polymer/paper-item/paper-item.js";
import "@polymer/paper-menu-button/paper-menu-button.js";
import "@polymer/paper-input/paper-input-behavior.js";
import "@polymer/paper-dialog/paper-dialog.js";
import "@polymer/paper-dialog-behavior/paper-dialog-behavior.js";
import "@polymer/iron-ajax/iron-ajax.js";
import "@vaadin/vaadin-grid/vaadin-grid.js";
import "@vaadin/vaadin-grid/vaadin-grid-selection-column.js";
import "@vaadin/vaadin-grid/vaadin-grid-sort-column.js";
import "@vaadin/vaadin-text-field/vaadin-text-field.js";
import "@vaadin/vaadin-button/vaadin-button.js";
import { PolymerElement, html } from "@polymer/polymer/polymer-element.js";
import './subject-area-header.js';
import './subject-area-home.js';
import './grid-component/grid-controller.js';

/**
*
* SubjectAreaComponent is the top level web component for the subject area expert. It controls all
* interactions between the bigger components.
* It instantiates components and passed them the appropriate properties.
* It listens for events so that it can set the appropriate properties and drive the next action.
* It collects error events so it can call the error component to display the error - not implemented.
* It does not issue rest calls itself.
*
* The idea is push down as much processing as possible into the children components to simplify the top level component.
*/

class SubjectAreaComponent extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles">
        :host {
          display: block;
          padding: 10px 20px;
        }
        .container {
          margin: auto;
          height: calc(100vh - 130px);
          background-color: white;
        }
      </style>
      <dom-module id='subject-area-component'>
          <!--header contains the dropdown to choose the view  -->
          <div>
              <subject-area-header id='header' selectedProject='[[selectedProject]]' selectedGlossary='[[selectedGlossary]]' language='[[language]]'></subject-area-header>
          </div>
          <div class='container'>
            <template is="dom-if" if="[[homeView]]" restamp="true">
                  <subject-area-home id='subjectAreaHome' enabledGlossary='[[enabledGlossary]]' refresh='[[refreshHome]]' language='[[language]]' selectedProject='[[selectedProject]]' selectedGlossary='[[selectedGlossary]]'></subject-area-home>
            </template>
            <template is="dom-if" if="[[glossaryGridView]]" restamp="true">
                  <grid-controller name='Glossary' refresh='[[refreshGlossaryGrid]]' selectedProject='[[selectedProject]]' language='[[language]]'></grid-controller>
            </template>
            <template is="dom-if" if="[[projectGridView]]" restamp="true">
                  <grid-controller name='Project' refresh='[[refreshProjectGrid]] language='[[language]]''></grid-controller>
            </template>
            <template is="dom-if" if="[[termGridView]]" restamp="true">
               <!--   <grid-controller name='Term' refresh='[[refreshTermGrid]] language='[[language]]''></grid-controller> -->
               TODO - work in progress
            </template>
              <template is="dom-if" if="[[NoGridView]]" restamp="true">

              </template>
          </div>
           <div>
             <subject-area-errors id='errorMessages' currentError=[[currentError]] ></subject-area-errors>
           </div>
      </dom-module>
  `;
   }

  static get properties() {
    return {
    // language
      language: {
       type: String,
       value: 'en'
      },
      //  selected project
      selectedProject: {
        type: Object,
        notify: true,
        observer: '_selectedProjectChanged'
      },
      //  selected glossary
      selectedGlossary: {
        type: Object,
        notify: true,
        observer: '_selectedGlossaryChanged'
      },
      //  selected view
      selectedView: {
        type: String,
        notify: true
      },

      // views
      homeView: {
          type: String,
          computed: 'computeHomeView(selectedView)',
          notify: true
      },

      refreshHome: {
        type: Boolean,
        notify: true
      },
      refreshGlossaryGrid: {
        type: Boolean,
        notify: true
      },
      refreshProjectGrid: {
        type: Boolean,
        notify: true
      },
      refreshTermGrid: {
        type: Boolean,
        notify: true
      },
      projectGridView: {
        type: String,
        computed: 'computeProjectGridView(selectedView)',
        notify: true
     //   observer: '_projectGridViewChanged'
      },
      glossaryGridView: {
        type: String,
        computed: 'computeGlossaryGridView(selectedView)',
        notify: true
      //  observer: '_glossaryGridViewChanged'
      },
      termGridView: {
        type: String,
        computed: 'computeTermGridView(selectedView)',
        notify: true
      },
      noGridView: {
        type: String,
        computed: 'computeNoGridView(selectedView)',
        notify: true
      },
      enabledGlossary : {
                    type: Boolean,
                    notify: true,
                    value: false
      },
      currentError: {
        type: Object,
        notify: true,
      }
    };
  }
  ready(){
     super.ready();
     this.$.header.addEventListener('viewSelectionEvent', e => {this._handleViewSelected(e)});
     this.addEventListener('rest-error-event', e => {this._handleRestError(e)});
     this.addEventListener('projectSelectionEvent', e => {this._handleProjectSelected(e)});
     this.addEventListener('glossarySelectionEvent', e => {this._handleGlossarySelected(e)});

  }
  _selectedProjectChanged(newValue) {
     console.log('_selectedProjectChanged - component');
     this.shadowRoot.getElementById('subjectAreaHome').selectedProject=newValue;
  }
  _selectedGlossaryChanged(newValue) {
     console.log('_selectedGlossaryChanged - component');
     this.shadowRoot.getElementById('subjectAreaHome').selectedGlossary=newValue;
  }

  _handleGlossarySelected(event) {
        console.log("subject-area-component _handleGlossarySelected... " + event);
        this.selectedGlossary = event.detail;
        // refresh the view
        this.refreshGlossaryGrid = true;
        // refresh the header
        this.$.header.selectedGlossary = event.detail;
  }
  _handleProjectSelected(event) {
         console.log("subject-area-component _handleProjectSelected... " + event);
         this.selectedProject = event.detail;
          // refresh the view
         this.refreshProjectGrid = true;
         // refresh the header
         this.$.header.selectedProject = event.detail;
         this.shadowRoot.getElementById('subjectAreaHome').enabledGlossary = true;
  }
  _handleViewSelected(event) {
     console.log("subject-area-component _handleViewSelected... " + event.detail);
     this.selectedView = event.detail;

  }
  computeHomeView(view) {
      return this.computeActualView(view,"home");
  }
  computeGlossaryGridView(view) {
      return this.computeActualView(view,"ggrid");
  }
  computeProjectGridView(view) {
      return this.computeActualView(view,"pgrid");
  }
  computeTermGridView(view) {
      return this.computeActualView(view,"tgrid");
  }
  computeNoGridView(view) {
      return this.computeActualView(view,"ngrid");
  }
  _handleRestError(event) {
      console.log("subject-area-component  _handleGlossaryGridRestError... " + event.detail);
      this.error = event.detail;
  }
//  _handleModelInstancesChanged(e) {
//       console.log("subject-area-component  _handleModelInstancesChanged");
//  }

  computeActualView(view,viewName) {
        var actualView;
        if (viewName == view) {
            actualView = true;
        }
        return actualView;
    }
}

window.customElements.define('subject-area-component', SubjectAreaComponent);