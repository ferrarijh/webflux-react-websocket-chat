FROM node:16-alpine
MAINTAINER Jonathan Hahn

RUN mkdir /app
WORKDIR /app

COPY ./build /app/build
RUN npm i -g serve

CMD ["serve", "-s", "build"]