FROM node:18

WORKDIR /

ENV HOST=0.0.0.0

COPY package*.json ./

RUN npm install --only=production

COPY . .

EXPOSE 8080

CMD [ "node", "app.js"]
