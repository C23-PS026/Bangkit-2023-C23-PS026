import express from 'express';
import cors from 'cors';
const app = express();
const PORT = process.env.PORT || 8080;
import auth from './routes/auth.js';
import uploadImage from './routes/uploadImage.js';
import { initializeApp } from "firebase/app";
import firebaseConfig from "./config/firebase.config.js";

initializeApp(firebaseConfig);
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

app.get("/", (req, res) => {
  res.json({
    message: "MeatFYI API is now live",
  });
});

app.use('/auth', auth);
app.use('/uploadImage', uploadImage);

app.listen(PORT, () => {
  console.log(`App is running on port ${PORT}`);
});

