/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
const LocalStrategy = require("passport-local").Strategy;
const db = require("../db");


const passportConfiguration = (passport) => {

  /**
   * Middleware to configure Passport to use Local strategy 
   */
  passport.use(
    new LocalStrategy(function (username, password, cb) {
      console.log("username: " + username);
      console.log("password: " + password);
      db.users.findByUsername(username, function (err, user) {
        if (err) {
          return cb(err);
        }
        if (!user) {
          return cb(null, false);
        }
        if (user.password != password) {
          return cb(null, false);
        }
        return cb(null, user);
      });
    })
  );
  // Configure Passport authenticated session persistence.
  //
  // In order to restore authentication state across HTTP requests, Passport needs
  // to serialize users into and deserialize users out of the session.  The
  // typical implementation of this is as simple as supplying the user ID when
  // serializing, and querying the user record by ID from the database when
  // deserializing.
  passport.serializeUser(function (user, cb) {
    console.log("serializeUser called with user " + user);
    cb(null, user.id);
  });
  /**
   * Deserialise the user. This means look up the id in the database (db).  
   */
  passport.deserializeUser(function (id, cb) {
    console.log("deserializeUser called with id " + id);
    db.users.findById(id, function (err, user) {
      console.log("passport.deserializeUser user is " + user + ",err is" + err);
      if (err) {
        return cb(err);
      }
      cb(null, user);
    });
  });

  return passport;

}

module.exports = passportConfiguration;