/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import '@polymer/iron-form/iron-form.js';
import '@polymer/paper-input/paper-input.js';
import '@polymer/paper-material/paper-material.js';
import '@polymer/iron-a11y-keys/iron-a11y-keys.js';
import '@polymer/paper-button/paper-button.js';
import '@polymer/paper-styles/paper-styles.js';
import '@polymer/paper-input/paper-input-behavior.js';
import '@polymer/iron-localstorage/iron-localstorage.js';

import {PolymerElement, html} from '@polymer/polymer/polymer-element.js';
import './shared-styles.js';
import './form-feedback.js';


class LoginView extends PolymerElement {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          display: block;
          padding: 10px 20px;
        }
        div.container6 {
          display: flex;
          align-items: center;
          justify-content: center 
        }
        img.logo {
          display: block;
          margin-left: auto;
          margin-right: auto;
          margin-top: 1em;
        }
      </style>
      <iron-localstorage name="my-app-storage" value="{{token}}"></iron-localstorage>
      <p>
          <img class="logo" src="images/Logo_transparent.png" width="600"/>
      </p>
       <div class="container6">
              <paper-material  class="card">
                 <iron-form id="form">
                    <form method="post" action="/auth/login">
                        <paper-input value={{username}} label="Username" name="username" required
                                     error-message="Username is required"                                
                                     autofocus></paper-input>
                        <paper-input value="{{password}}" label="Password" name="password" required
                                     error-message="Password is required"
                                     type="password" ></paper-input>
                                     
                        <form-feedback message="{{feedback}}" level="{{feedbackLevel}}"></form-feedback>
                        <element style="float: right">
                            <paper-button id="login-button" on-tap="_logIn" raised>Login</paper-button>
                        </element>
                        <iron-a11y-keys keys="enter" on-keys-pressed="_logIn"></iron-a11y-keys>
                    </form>
                </iron-form>   
               </paper-material>
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