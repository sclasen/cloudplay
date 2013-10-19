#!/bin/sh
apt-get update
apt-get install -q -y linux-image-extra-`uname -r`
sh -c "wget -qO- https://get.docker.io/gpg | apt-key add -"
sh -c "echo deb http://get.docker.io/ubuntu docker main > /etc/apt/sources.list.d/docker.list"
apt-get update
apt-get install -q -y lxc-docker
docker pull sclasen/jenkins-heroku
docker run -d -p 80:8080 sclasen/jenkins-heroku
