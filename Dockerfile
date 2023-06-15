FROM --platform=linux/amd64 node:18

WORKDIR /usr/src/app

ENV HOST=0.0.0.0
ENV PORT=8080

COPY package*.json ./

RUN npm install --only=production

COPY . .

EXPOSE 8080

CMD [ "node", "app.js"]
