FROM node:12.18.0
EXPOSE 8080
WORKDIR /usr/src/app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build
ENTRYPOINT ["npm", "run", "serve"]