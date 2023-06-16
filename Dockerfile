FROM python:3

RUN pip install --no-cache-dir --upgrade pip && \
    pip install --no-cache-dir nibabel pydicom matplotlib pillow med2image
    # Note: we had to merge the two "pip install" package lists here, otherwise
    # the last "pip install" command in the OP may break dependency resolution…

CMD ["cat", "/etc/os-release"]

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

RUN npm install @tensorflow/tfjs-node

COPY . .

# Expose the port
EXPOSE 8080

# Set the entry point command
CMD ["npm", "start"]
