import express from "express";
import { initializeApp } from "firebase/app";
import {
  getStorage,
  ref,
  getDownloadURL,
  uploadBytesResumable,
} from "firebase/storage";
import multer from "multer";
import firebaseConfig from "../config/firebase.config.js";

const router = express.Router();

initializeApp(firebaseConfig);

const storage = getStorage();

const upload = multer({ storage: multer.memoryStorage() });

router.post("/", upload.single("image"), async (req, res) => {
  try {
    const storageRef = ref(storage, `images/${req.file.originalname}`);

    const metadata = {
      contentType: req.file.mimetype,
    };

    const snapshot = await uploadBytesResumable(
      storageRef,
      req.file.buffer,
      metadata
    );

    const url = await getDownloadURL(snapshot.ref);

    return res.status(200).json({
      message: "File succesfully uploaded",
      name: req.file.originalname,
      type: req.file.mimetype,
      url: url,
    });
  } catch (err) {
    return res.status(500).json({
      message: "Upload failed",
      error: err.message,
    });
  }
});

export default router;
