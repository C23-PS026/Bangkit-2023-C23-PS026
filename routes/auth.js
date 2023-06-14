import { createRequire } from 'module';
const require = createRequire(import.meta.url);

import express from 'express';
const router = express.Router();

const admin = require('firebase-admin');
import { getAuth, GoogleAuthProvider, signInWithPopup } from 'firebase/auth';

const serviceAccount = require('D:\\PENTING_Perkuliahan\\Semester 8_Bangkit\\capstone\\Bangkit-2023-C23-PS026\\google-services.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

router.post('/', (req, res) => {
  const uidFromApp = req.query.uid;

  async function checkFirebaseUID(uid) {
    try {
      const userRecord = (await admin.auth().getUser(uid)).toJSON();

      return res.json({
        message: 'User exists',
        userExist: true,
      });
    } catch (err) {
      return res.json({
        error: err,
        userExist: false,
      });
    }
  }
  checkFirebaseUID(uidFromApp);
});

export default router;
