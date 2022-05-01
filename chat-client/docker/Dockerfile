FROM node:16-alpine
MAINTAINER Jonathan Hahn

RUN mkdir /app
WORKDIR /app

# copy build
COPY ./build ./build

# install serve
RUN npm i -g serve

ENTRYPOINT ["serve", "-l", "3000", "-s", "build"]