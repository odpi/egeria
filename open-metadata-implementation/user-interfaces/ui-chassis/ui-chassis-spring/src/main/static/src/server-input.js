/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import '@polymer/iron-form/iron-form.js';
import '@polymer/paper-input/paper-input.js';
import '@polymer/iron-a11y-keys/iron-a11y-keys.js';
import '@polymer/paper-button/paper-button.js';
import '@polymer/paper-styles/paper-styles.js';
import '@polymer/paper-input/paper-input-behavior.js';


import {PolymerElement, html} from '@polymer/polymer/polymer-element.js';
import './shared-styles.js';
import './form-feedback.js';

class ServerInput extends PolymerElement {
    static get template() {
        return html`
        <custom-style>
          <style is="custom-style" include="paper-material-styles">
            .paper-material {
              padding: 16px;
              margin: 0 16px;
              display: inline-block;
            }
          </style>
        </custom-style>
      <style include="shared-styles">
        :host {
          display: block;
          padding: 10px 20px;
        }
        div.container6 {
          display: flex;
          align-items: center;
          justify-content: center;
        }
        
        .login {
            padding: 16px;
            color: #757575;
            border-radius: 5px;
            background-color: #fff;
            box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.14), 0 1px 5px 0 rgba(0, 0, 0, 0.12), 0 3px 1px -2px rgba(0, 0, 0, 0.2);
            max-width: 600px;
            width: 100%;
        }
        
        img.logo {
          display: block;
          margin-left: auto;
          margin-right: auto;
          margin-top: 1em;
          width: 600px;
        }
        
        @media (max-width: 600px){
          :host {
            justify-content: flex-end;
          }
          
          img.logo {
              width: 70vw;
          }
    
        }
      </style>
      <iron-localstorage name="my-app-storage" value="{{token}}"></iron-localstorage>

      <p>
          <img class="logo" src="images/Logo_transparent.png"/>
      </p>

               <form is="iron-form" id="createServerNameForm">
                  <p>
                  <label for="serverName">Please enter your server name.</label>
                  <input is="paper-input" id="serverName" type="text" name="name"> <br>
                  </p>
                  <div class="buttons">
                      <paper-button on-tap="_onServerNameInputted">+</paper-button>
                  </div>
               </form>
       </template>

    `;
    }
    /*
     * Check whether there is a serverName in the query parameter - in this case set the session storage to that value and reload the page.
     */
    ready() {
        super.ready();
          var currentLocation = window.location.href;
          var currentPath = window.location.pathname;
          var indexOfQuery = currentLocation.indexOf('?');
          if (currentPath == '/' && indexOfQuery != -1 && currentLocation.length > indexOfQuery) {
              var queryContent = currentLocation.substring(indexOfQuery+1);

              if (queryContent.length > 10 && (queryContent.slice(0,10) == "serverName=")) {
                   var serverName = queryContent.substring(10);
                   sessionStorage.setItem('egeria-ui-servername',serverName);
                   window.location.reload();
              }
          }
    }
    /*
     * This method is driven when a server name has been input. If the current url path is '/' then we load a page with the serverName as a query parm, to allow
     * a tenanted url to be bookmarked.
     * If the url is no '/' then the user expects that url to be honoured, so we do not change the url.
     *
     */
    _onServerNameInputted() {
     var inputtedServerName = this.$.serverName.value;
     if (inputtedServerName) {
         sessionStorage.setItem('egeria-ui-servername',inputtedServerName);
         var currentLocation = window.location.href;
         var currentPath = window.location.pathname;
         var indexOfQuery = currentLocation.indexOf('?');
         var newlocation= window.location.href;
         var basePath = window.location.protocol + '//' + window.location.hostname + ':' +  window.location.port + '/';
         if (currentPath == '/' && (currentLocation == basePath)) {
              newlocation =  window.location.protocol + '//' + window.location.hostname + ':' +  window.location.port + '/' +  "?serverName="+inputtedServerName;
              console.log('tenanted url is ' + newlocation);
              // load new page
              window.location.assign(newlocation);
         } else {
              window.location.reload();
         }
     } else {
         // TODO localise
         alert("Please input your serverName");
     }
  }



}

window.customElements.define('server-input', ServerInput);