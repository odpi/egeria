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

class LoginView extends PolymerElement {
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
        
        @media (max-width: 640px){
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
       
       <div class="container6">
              <div   class="login">
                 <iron-form id="form">
                    <form method="post" action="/auth/login">
                        <paper-input value={{username}} label="Username" name="username" required
                                     error-message="Username is required"                                
                                     autofocus></paper-input>
                        <paper-input value="{{password}}" label="Password" name="password" required
                                     error-message="Password is required"
                                     type="password" ></paper-input>
                                     
                        <form-feedback message="{{feedback}}" level="{{feedbackLevel}}"></form-feedback>
                        <div style="float: right">
                            <paper-button id="login-button" on-tap="_logIn" raised>Login</paper-button>
                        </div>
                        <iron-a11y-keys keys="enter" on-keys-pressed="_logIn"></iron-a11y-keys>
                    </form>
                </iron-form>   
               </div>
       </div>

    `;
    }

    static get properties() {
        return {
            token: {
                type: Object,
                notify: true
            },
            feedback: String,
            feedbackLevel: String
        };
    }

    ready() {
        super.ready();
        this.addEventListener('iron-form-response', this._handleLoginSuccess);
        this.addEventListener('iron-form-error', this._handleLoginError);
    }

    _handleLoginSuccess(evt){
        this.token = evt.detail.xhr.getResponseHeader('x-auth-token');
        this.feedback = 'Authentication successful!';
        this.feedbackLevel = 'info';
    }

    _handleLoginError(evt){
        this.feedback = 'Authentication failed!';
        this.feedbackLevel='error';
    }

    _logIn() {
        this.$.form.submit();
    }


}

window.customElements.define('login-view', LoginView);