#! /usr/bin/env sh

aws ec2 run-instances --min-count 1 --max-count 1 --image-id ami-a73264ce --user-data userdata.mime --instance-type m1.xlarge --security-groups jenkins-ssh-http-8080 --key-name newkey
