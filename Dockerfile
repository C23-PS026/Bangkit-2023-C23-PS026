FROM --platform=linux/amd64 node:18

WORKDIR /usr/src/app

ENV HOST=0.0.0.0

COPY package*.json ./

RUN npm install --only=production

COPY . .

EXPOSE $PORT

CMD [ "node", "app.js"]
