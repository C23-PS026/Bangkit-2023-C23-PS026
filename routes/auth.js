import {createRequire} from 'module'
const require = createRequire(import.meta.url)

import express from "express";
const router = express.Router();

const admin = require('firebase-admin')
import { getAuth, GoogleAuthProvider, signInWithPopup } from "firebase/auth";
const serviceAccount = require('../serviceAccount.json')

// const provider = new GoogleAuthProvider();

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

router.post('/', (req, res) => {
  const uidFromApp = req.query.uid

  async function checkFirebaseUID(uid) {
    try {
      const userRecord = (await admin.auth().getUser(uid)).toJSON();
      
      return res.json({
        message: "User exist",
        userExist: true
      });
    } catch (err) {
      return res.json({
        error: err,
        userExist: false
      });
    }
  }
  checkFirebaseUID(uidFromApp)
})

// router.post("/signin", (req, res) => {
//   signInWithPopup(auth, provider)
//     .then((result) => {
//       // This gives you a Google Access Token. You can use it to access the Google API.
//       const credential = GoogleAuthProvider.credentialFromResult(result);
//       const token = credential.accessToken;
//       // The signed-in user info.
//       const user = result.user;
//       // IdP data available using getAdditionalUserInfo(result)
//       // ...
//     })
//     .catch((error) => {
//       // Handle Errors here.
//       const errorCode = error.code;
//       const errorMessage = error.message;
//       // The email of the user's account used.
//       const email = error.customData.email;
//       // The AuthCredential type that was used.
//       const credential = GoogleAuthProvider.credentialFromError(error);
//       // ...
//     });
// });

export default router;
