FROM bellsoft/liberica-openjdk-alpine:19.0.2
WORKDIR '/app'
#putting jar to workdir
COPY ./build/libs/AskExchange.jar .
#working from workdir
#jar won't work since I am not sure how to make it executable yet
CMD "java -jar AskExchange.jar"

#commands to check out container's bash
#docker run -it --entrypoint /bin/bash great_lewin
#docker exec -t -i container_name /bin/bash
#https://www.digitalocean.com/community/tutorials/how-to-remove-docker-images-containers-and-volumes
#docker build -t dude/man:v2 . # Will be named dude/man:v2
#https://hub.docker.com/r/bellsoft/liberica-openjdk-alpine