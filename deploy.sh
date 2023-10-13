#!/bin/bash

# Define environment variables for container and image names
CONTAINER="pos_coffee_container"
IMAGE="pos_coffee_image"

# Stop and remove the existing container (if it exists)
if sudo docker ps -a | grep -q $CONTAINER; then
    echo "Stopping and removing existing container: $CONTAINER"
    sudo docker stop $CONTAINER
    sudo docker rm $CONTAINER
fi

# Build the Docker image
echo "Building Docker image: $IMAGE"
sudo docker build -t $IMAGE .

# Run the Docker container
echo "Running Docker container: $CONTAINER"
sudo docker run -d --env-file .env --net=host --name $CONTAINER $IMAGE
