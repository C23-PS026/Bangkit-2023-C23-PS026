FROM node:18

WORKDIR /app

ENV HOST 0.0.0.0

COPY package*.json ./

RUN npm install --only=production

COPY . .

CMD [ "node", "app.js"]
