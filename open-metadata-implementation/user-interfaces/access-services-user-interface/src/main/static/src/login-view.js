/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import '@polymer/iron-form/iron-form.js';
import '@polymer/paper-input/paper-input.js';
import '@polymer/paper-material/paper-material.js';
import '@polymer/iron-a11y-keys-behavior/iron-a11y-keys-behavior.js';
import '@polymer/paper-button/paper-button.js';
import '@polymer/paper-styles/paper-styles.js';
import '@polymer/paper-input/paper-input-behavior.js';

import {PolymerElement, html} from '@polymer/polymer/polymer-element.js';
import './shared-styles.js';


class LoginView extends PolymerElement {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          display: block;
          padding: 10px 20px;
        }
      </style>
       
      <paper-material  class="card">
         <iron-form id="form">
            <iron-a11y-keys keys="enter" on-keys-pressed="_logIn"></iron-a11y-keys>
            <form method="post" action="/auth/login">
                <paper-input value={{username}} label="Username" name="username" required
                             error-message="Username is required"                                
                             autofocus></paper-input>
                <paper-input value="{{password}}" label="Password" name="password" required
                             error-message="Password is required"
                             type="password" ></paper-input>
                <span class="error-message" hidden="[[!errorMessage]]">[[errorMessage]]</span>
                <paper-button id="login-button" on-tap="_logIn" raised>Login</paper-button>
                <button hidden="true"></button>
            </form>
        </iron-form>     
       </paper-material>
    `;
    }
    _logIn() {
        this.$.form.submit();
    }
}

window.customElements.define('login-view', LoginView);