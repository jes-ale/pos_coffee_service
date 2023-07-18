#!/bin/sh
# TODO: setup github actions to deploy and update deploy script to kill the process before starting again
git fetch
git add .
git commit -m "$1"
git pull origin master
git push https://$GITU:$GITP@github.com/jes-ale/pos_coffee_service.git
