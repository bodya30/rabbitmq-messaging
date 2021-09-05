#!/bin/bash

echo "### Removing rabbitmq-cluster if exists ###"
docker stop rabbit1 || true && docker rm rabbit1 || true
docker stop rabbit2 || true && docker rm rabbit2 || true
docker stop rabbit3 || true && docker rm rabbit3 || true
echo "### Removed succesfully ###"

echo "### Creating new rabbitmq-cluster ###"
docker run -d --hostname rabbit1 --name rabbit1 -e RABBITMQ_ERLANG_COOKIE='rabbitcluster' -p 30000:5672 -p 30001:15672 rabbitmq:management
docker run -d --hostname rabbit2 --name rabbit2 --link rabbit1:rabbit1 -e RABBITMQ_ERLANG_COOKIE='rabbitcluster' -p 30002:5672 -p 30003:15672 rabbitmq:management
docker run -d --hostname rabbit3 --name rabbit3 --link rabbit1:rabbit1 --link rabbit2:rabbit2 -e RABBITMQ_ERLANG_COOKIE='rabbitcluster' -p 30004:5672 -p 30005:15672 rabbitmq:management
docker ps -a
echo "### Created succesfully ###"


: ' NEXT STEPS:
Details here https://dzone.com/articles/rabbitmq-in-cluster

docker exec -i -t rabbit2 \bash
rabbitmqctl stop_app && rabbitmqctl join_cluster rabbit@rabbit1 && rabbitmqctl start_app && exit

docker exec -i -t rabbit3 \bash
rabbitmqctl stop_app && rabbitmqctl join_cluster rabbit@rabbit1 && rabbitmqctl start_app && exit

For jconsole on Mac
cd /Library/Java/JavaVirtualMachines/jdk-16.0.2.jdk/Contents/Home/bin && ./jconsole
'