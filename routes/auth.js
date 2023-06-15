import { createRequire } from "module";
const require = createRequire(import.meta.url);
import express from "express";
const router = express.Router();
import { initializeApp } from "firebase/app";
import { getAuth, signInWithPopup, GoogleAuthProvider } from "firebase/auth";
const provider = new GoogleAuthProvider();
// const admin = require("firebase-admin");
import firebaseConfig from "../config/firebase.config.js";

// const key =
//   "-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCYQnocW/PHRuld\n0OZ5XyG8tOSN3MtFKiuftB6HFzVTNpLCoqR+1Sg9q3H2Lf1CksQJtW/KSqAyWn8B\nSZQ9FA3nUuk43A+B37EKN4yYBu4zj6CEOeWoRnR+0jPDLRkN2ae10rTRyVDN9jBN\n595sS2SzFod/y25KA4oYJPLn5F/oLD3tfwUq0UZB7UIRnaSbo71H8I5sJytVJX5J\nyaNEjre+djOQ8o/BHBXVftO557OOXrqZb5r74v2R2aGqCGV7Qt8bI3eTOLWDjGnX\nBlb08XsMDCcVdiQA5Umlr90KL4F5KtgHG6sjWufj4fq7zSXJgBo5Tt2FDiKdiK63\nQElE7wOhAgMBAAECggEAPFUzL+Ob4J4PwPYTLYrtwmnnmvMftON1782evycKIVQh\nqXBrQDrUvEMgYE1u6p9fgP1dM8qIWguUAf2W+PDmZYBsG/TPTEZIT+UdF+y/HL1P\n/LU4dIqehNZkXnWTVPKnSxwYPHzKQSLJl0VG1KC3YWOPA6unozpt0Q1rF0peWo/4\n7lpfdj9f3g2KuNdky7il2nrnpghySaCDQaFE7c4TNgfzHtEY49Ihb4z8BS4st+ni\nIH0hqaLXpH+rXeNrogPOJB45DQomz6g3OAtT3TGWJZEsb8OzaML+p+WO8v/zONzh\nAiXAuCiEB+YZDDwUUt8v/PAe+gOY8VxHhiDqMgzOVQKBgQDGiFqkPWCEAMZFtVN2\nSUyJfUsQXxjvm3VzzSkmPybQhtxSBsb40gueBe65ZmBc1uROv+J24FeDP5l9+BKP\neBHWuBICllBM/5rJ0czVDGgHpCI7JVVc+8HpmsQvb+F5qKyIxloKXCcmoGNw+fHt\nX+TvPRQx1t5k7JsNYk+c2L650wKBgQDEVTVvQ7+ArOy81RZhl2ukVora5LTMmK9z\nXRsii2T+tSFOtWAV+Te/zmVBNs+HeRnug0CufsUBfVOXJT2ixtFAdaML+BNSOoMU\naQBA7gNy695ZMZzd9vfoVEz/NftcJAxLVs+UCudp1vKV/1vKoqYXg8Euv/RjDtUd\nXqzoNg8QOwKBgAzp+7NpRqjvf/Yy2E3+cJsU6lgTR4T2gMpakw2o1/aCfzxasoCm\n8RiDXEb1ENmZJNq2gp2m9CpWbb6S72ojrk1gRdOjQW/wxHnSStek6fNtSsi8IfRd\nb/ypE4/eYDVQsoBnHp5D5kwN7MoBiCofrb2eJH0lc+vutBtHxNP4CVf/AoGAbGU6\n/jYiSdGvVsYy+CiYln/tY9L3eyolXAUgkR7y4KlyPTloVlllDTHPt4SiaYJT7dpr\nYKu+6wadmwpFkFRCfVs1bMtHJ5QEgMVEDZUbNFzDQu1gVTAgSRYZ+KF+KK4CV6oT\nKwImJ8oz3trAfAz2tG7avoveSr6KrQ9zWTVXdbcCgYEAqaE89JW/UlP9Bh2Y4E/o\nD4bEO67zAQKu8F4LnhK4D2Ay3ynrVuvn+GovGrVLyvKusn8fXAowkwjMjsHrrw+V\nxm+k8Lcfxv61KmLj8s37ppLJyQ0oyRjm7ShYRSae64y+2nfnlAtGBopvgcw2Znb3\njVxzIS9zCMoxvD8tEJuHvA4=\n-----END PRIVATE KEY-----\n";

// admin.initializeApp({
//   credential: admin.credential.cert({
//     private_key: key.replace(/\\n/g, "\n"),
//     client_email: "YOUR CLIENT EMAIL HERE",
//     project_id: "capstone-project-c23-ps026",
//   }),
// });

const app = initializeApp(firebaseConfig)
const auth = getAuth(app)

// router.post("/", (req, res) => {
//   const uidFromApp = req.query.uid;

//   async function checkFirebaseUID(uid) {
//     try {
//       const userRecord = (await admin.auth().getUser(uid)).toJSON();

//       return res.json({
//         message: "User exist",
//         userExist: true,
//       });
//     } catch (err) {
//       return res.json({
//         error: err,
//         userExist: false,
//       });
//     }
//   }
//   checkFirebaseUID(uidFromApp);
// });

router.post("/signin", (req, res) => {
  console.log("Signin requested")
  signInWithPopup(auth, provider)
    .then((result) => {
      // This gives you a Google Access Token. You can use it to access the Google API.
      const credential = GoogleAuthProvider.credentialFromResult(result);
      const token = credential.accessToken;
      // The signed-in user info.
      const user = result.user;
      // IdP data available using getAdditionalUserInfo(result)
      // ...
      console.log("Sign in ongoing")
      console.log(credential, token, user)
    })
    .catch((error) => {
      console.log("error occured")
      // Handle Errors here.
      const errorCode = error.code;
      const errorMessage = error.message;
      // The email of the user's account used.
      const email = error.customData.email;
      // The AuthCredential type that was used.
      const credential = GoogleAuthProvider.credentialFromError(error);
      // ...
    });
});

export default router;
