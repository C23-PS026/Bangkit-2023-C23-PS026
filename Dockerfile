# Use the official Node.js 14 image as the base
FROM --platform=linux/amd64 node:14

# Set the working directory in the container
WORKDIR /app

ENV HOST=0.0.0.0
ENV PORT=8080

# Copy the package.json and package-lock.json files
COPY package*.json ./

# Install the dependencies
RUN npm install

# Copy the server.js file
COPY server.js .

# Copy the model.tflite file
COPY model.tflite .

# Expose the port
EXPOSE 8080

# Set the entry point command
CMD ["npm", "start"]
