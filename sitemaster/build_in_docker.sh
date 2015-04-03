#!/bin/sh

#######
#
# Uses the given docker image to start a container and run the given command
#
# Usage: ./build_in_docker.sh <image> <bash command>
# Example: ./build_in_docker.sh sfr:engage_dev "(mongod --smallfiles > mongodb.log &) ; (sleep 40) ; ( gradle integrationTest )"
#
#######

docker_image=$1
bash_command=$2
timestamp=`date +%s`
here=`pwd`
username=`whoami`

echo "Building in docker image $docker_image"
echo "Running build command $bash_command"
echo "Timestamp is $timestamp"
echo "I am in $here"
echo "and my username is $username"

# build the image if it does not exist in cache
sudo docker build -t sfr:sitemaster_build -f Dockerfile.Build .

# run build command in new container
sudo docker run -t -a stdout -a stderr --rm --volume ${here}:/workspace:rw sfr:sitemaster_build bash -c "${bash_command}"

# commented out for my local vagrant box running docker server
# sudo docker run -t -a stdout -a stderr --rm --volume /vagrant:/workspace:rw sfr:sitemaster_build bash -c "${bash_command}"


# change owner back to jenkins so it can do stuff with the files after docker exits
sudo chown -R jenkins .