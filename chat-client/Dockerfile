FROM node:16-alpine
MAINTAINER Jonathan Hahn
ARG REACT_APP_SERVER_PORT

RUN mkdir /app
WORKDIR /app

# build
RUN mkdir /tmp-build
COPY . /tmp-build
COPY ./src/env/.env-$REACT_APP_SERVER_PORT /tmp-build/.env
RUN (cd /tmp-build; npm install; npm run build)
RUN mv /tmp-build/build /app/build; rm -r /tmp-build

# install serve
RUN npm i -g serve

ENTRYPOINT ["serve", "-s", "build"]