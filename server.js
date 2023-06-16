const express = require('express');
const tf = require('@tensorflow/tfjs-node');
const mime = require('mime');
const fs = require('fs');

const app = express();
const port = process.env.PORT || 8080;
const modelPath = 'https://storage.googleapis.com/meat-classification-model-001/model.tflite';

const model = fs.readFileSync(modelPath);
const interpreter = tf.createInterpreter(model);

app.use(express.json());

app.post('/predict', (req, res) => {
  try {
    const inputData = req.body.input;
    const inputTensor = tf.tensor(inputData);

    interpreter.setInputTensor(inputTensor);
    interpreter.invoke();

    const outputTensor = interpreter.getOutputTensor(0);
    const outputData = outputTensor.dataSync();

    res.json({ output: Array.from(outputData) });
  } catch (error) {
    console.error('Prediction error:', error);
    res.status(500).json({ error: 'Prediction error' });
  }
});

app.get('/model', (req, res) => {
  res.setHeader('Content-Type', mime.getType(modelPath));
  res.sendFile(modelPath);
});

app.listen(port, () => {
  console.log(`Server listening on port ${port}`);
});
