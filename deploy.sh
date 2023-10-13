#!/bin/sh

# Author: Jesus Alejos <https://github.com/jes-ale>
# Script to stop, remove, build, and run a Docker container only if the image has changed.

# Define environment variables for container and image names
CONTAINER="pos_coffee_container"
IMAGE="pos_coffee_image"

# Calculate the hash of the Dockerfile and source code
DOCKERFILE_HASH=$(md5sum Dockerfile | awk '{print $1}')
SOURCE_CODE_HASH=$(md5sum -c .source_code_hash 2>/dev/null | awk '{print $1}')

# Calculate the image tag as a combination of Dockerfile and source code hashes
IMAGE_TAG="$DOCKERFILE_HASH-$SOURCE_CODE_HASH"

# Check if an image with the same tag exists
if sudo docker image inspect $IMAGE:$IMAGE_TAG &>/dev/null; then
    echo "Image $IMAGE:$IMAGE_TAG already exists. No changes detected."
else
    # Stop and remove the existing container (if it exists)
    if sudo docker ps -a | grep -q $CONTAINER; then
        echo "Stopping and removing existing container: $CONTAINER"
        sudo docker stop $CONTAINER
        sudo docker rm $CONTAINER
    fi

    # Build the Docker image with the specified tag
    echo "Building Docker image: $IMAGE:$IMAGE_TAG"
    sudo docker build -t $IMAGE:$IMAGE_TAG .

    # Run the Docker container
    echo "Running Docker container: $CONTAINER"
    sudo docker run -d --env-file .env --net=host --name $CONTAINER $IMAGE:$IMAGE_TAG
fi
