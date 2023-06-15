# Backend API

As Cloud Computing Team, we build RESTful API to store captured image by user from Android App to Firebase Storage. The images collected can be used as additional dataset for ML model training to increase accuracy further. User needs to be authenticated (via Android App) to be able to call API method.

## Requirements

These are the frameworks and dependencies used in the application:

- NodeJS (`Node v18.15.0 & npm v9.6.3`)
- Express
- Firebase (Storage)
- Docker
- Google Cloud Run

..and some of the others you can check in [package.json](package.json)

## Documentation

API application runs on Google Cloud Run with the following description:

**Base URL:** <https://bangkit-2023-c23-ps026-f5nput3awq-et.a.run.app/>

**Method:**

- **POST** `/uploadImage` | Upload captured image to Firebase Storage
  - **Request:**
    >File:Image

  - **Response:**

    <details markdown=span>
  
    <summary markdown=span><b>200</b> Successful</summary>
  
    ```JSON
    {
      "message": "File succesfully uploaded",
      "name": "822E8AA99C97C34885FFAA65E9B576A3A589AD7E.jpg",
      "type": "image/jpeg",
      "url": "https://firebasestorage.googleapis.com/v0/b/capstone-project-c23-ps026.appspot.com/o/images%2F822E8AA99C97C34885FFAA65E9B576A3A589AD7E.jpg?alt=media&token=7caa2e5b-13e4-40ba-a2e1-fc50cde2f878"
    }
    ```
  
    </details>
    
    <details markdown=span>
  
    <summary markdown=span><b>500</b> Failed to process request due to server or application error</summary>
  
    ```JSON
    {
      "message": "Upload failed",
      "error": "Cannot read properties of undefined (reading 'originalname')"
    }
    ```
  
    </details>
