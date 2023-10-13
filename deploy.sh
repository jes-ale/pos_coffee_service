#!/bin/sh
# [Author: Jesus Alejos <https://github.com/jes-ale>]
CONTAINER="pos_coffee_container"
IMAGE="pos_coffee_image"
sudo docker stop $CONTAINER
sudo docker rm $CONTAINER
sudo docker build -t $IMAGE .
sudo docker run -d --env-file .env --net=host --name $CONTAINER $IMAGE
