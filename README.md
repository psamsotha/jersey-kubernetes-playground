# Jersey Kubernetes Playground

This project is for learning and playing around with Kubernetes

## Change

You will need to change the name of the image to use your Docker Hub account.

* `build.sh` - change `psamsotha/jersey-kubernetes` to use your Docker Hub account (`psamsotha`).
* `kubernetes/controllers/jersey-deployment.yml` - change `spec.containers.image` to use your Docker Hub account (`psamsotha`).

The following commands below that use `psamsotha`, should be replaced with your Docker Hub account name.

## Build

Build the Docker image with the build script

```bash
$ cat build.sh
#!/bin/bash
./mvnw clean && ./mvnw package && docker build --tag psamsotha/jersey-kubernetes .

$ ./build.sh
```

## Test

Test the built image to make sure it runs correctly

```bash
$ docker run -d -p 8080:8080 psamsotha/jersey-kubernetes
$ curl -i localhost:8000/hello
HTTP/1.1 200 OK
Date: Wed, 02 Jun 2021 21:57:00 GMT
Content-Type: text/plain
Transfer-Encoding: chunked
Server: Jetty(9.4.28.v20200408)

Hello, World!

$ docker ps | grep jersey
793eb4eccd06   psamsotha/jersey-kubernetes  ...

$ docker stop 793eb4eccd06
$ docker rm 793eb4eccd06

$ docker push psamsotha/jersey-kubernetes
```

You will need to first login to your Docker hub account with `docker login` to push the image to your Docker Hub account. Kubernetes will try to pull this image from your Repository.

## Run

If you have Docker for Mac, kubernetes should be installed. You should also [install Minikube](https://minikube.sigs.k8s.io/docs/start/) for local development.

First start `Minikube`

```bash
$ minikube start
```

Deploy the Jersey app

```bash
$ kubectl apply -f kubernetes/controllers/jersey-deployment.yml
deployment.apps/jersey-deployment created

$ kubectl get deployments
NAME                READY   UP-TO-DATE   AVAILABLE   AGE
jersey-deployment   3/3     3            3           18s

$ curl -i localhost:8000/hello
curl: (7) Failed to connect to localhost port 8000: Connection refused
```

Add the [Service](https://kubernetes.io/docs/concepts/services-networking/service/) to make the app accessible.

> An abstract way to expose an application running on a set of Pods as a network service.
With Kubernetes you don't need to modify your application to use an unfamiliar service discovery mechanism. Kubernetes gives Pods their own IP addresses and a single DNS name for a set of Pods, and can load-balance across them.

```bash
$ kubectl apply -f kubernetes/services/jersey-service.yml
service/jersey-service created

$ minikube service jersey-service
|-----------|----------------|-------------|---------------------------|
| NAMESPACE |      NAME      | TARGET PORT |            URL            |
|-----------|----------------|-------------|---------------------------|
| default   | jersey-service |          80 | http://192.168.49.2:32700 |
|-----------|----------------|-------------|---------------------------|
🏃  Starting tunnel for service jersey-service.
|-----------|----------------|-------------|------------------------|
| NAMESPACE |      NAME      | TARGET PORT |          URL           |
|-----------|----------------|-------------|------------------------|
| default   | jersey-service |             | http://127.0.0.1:50708 |
|-----------|----------------|-------------|------------------------|
🎉  Opening service default/jersey-service in default browser...
❗  Because you are using a Docker driver on darwin, the terminal needs to be open to run it.
```

Now you access the app through the tunnel

```bash
$ curl -i http://127.0.0.1:50708/hello
HTTP/1.1 200 OK
Date: Wed, 02 Jun 2021 22:19:59 GMT
Content-Type: text/plain
Transfer-Encoding: chunked
Server: Jetty(9.4.28.v20200408)

Hello, World!
```

## Cleanup

```bash
$ kubectl get services
NAME             TYPE           CLUSTER-IP       EXTERNAL-IP   PORT(S)        AGE
jersey-service   LoadBalancer   10.110.254.137   <pending>     80:32700/TCP   6m50s
kubernetes       ClusterIP      10.96.0.1        <none>        443/TCP        15h

$ kubectl delete service jersey-service
service "jersey-service" deleted

$ kubectl get deployments
NAME                READY   UP-TO-DATE   AVAILABLE   AGE
jersey-deployment   3/3     3            3           12m

$ kubectl delete deployment jersey-deployment
deployment.apps "jersey-deployment" deleted

$ minikube stop
```
