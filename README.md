# handwrite-rpc
Hand write RPC framework in Java

>Zookeeper(imported, but it needs to connect with other moduels)
Zookeeper is imported via docker container   ./Dockerfile contains the docker-compose.yml file.

>Docker(basically used)

>Netty(to be done)

>protoBuffer(to be done)

>Annotation(done)

>Java Proxy(done)

Between the Client and the Server, the data handled by Netty, which is a NIO framework.
Protobuffer used to encode the data into binary, as a mean to enhance the performance



### Zookeeper

Zookeeper plays a role of registry center.

At the beginning, we have to mark some of the services on the server end as rpc service. And client has to be aware of these services, in that way, zookeeper was brought in.

The zookeeper is just a blur concept for me.  The only thing I know about it is that it can be a register center, nothing more. So in the next, I have to learn about zookeeper, from its architecture to its application



### Annotation

As for annotation here. I went for some other examples, learned that I can use `@RpcService` to mark a specific class, in that way, this rpc service can be discovered by the server and expose it to the client, it`s pretty handy when it comes to the Spring.

But achieveing it without Spring, putting the service into the `Collection` explicitly is the only choice. It`s pretty easy at the begnning, but as the services grow. Puttting every single one of the service we want to expose is exhausting. But it is a pretty good way for understanding the rpc.



### Annotation Achievement

At first, I wonder how annotation works. I assume I just put the annotation to the place i want, then it will somehow work magically.

But the data will not just appear out of the thin air. The way the annotation works is because of the java reflection.

The principle of annotation. In the Spring frame work. Once the annotation is added, when the method or class we call. it will not longer be the original, it is a combination of the original itself and the annotation. annotation is done in the `Proxy` class.

Without the Spring frame work. Calling the `proxy` is the only way to make annotation works.

It took me a detour to relize it. Finally here we are. Grasped the idea of why Spring is imported. we can also do rpc without Spring and annotation.



### Spring (to be done)

Loading the xml file from the resource is the way initializing the Spring Context. Therefore it can scan the whole project and find the bean with @RpcService



#### Bean

I understood bean as a basic class loading into the Spring Context, if the class is marked with annotation. then the bean is a brand new class with annotation feature on it. it is enhanced. ???



### Netty

Netty was brought in because it is a effective NIO provider. the server will handle a number of clients. It must have the capacity to handle all the requests from the clients. Only in that way, can it be a effective rpc frame work(i know this one here is just for studying).

`channelRead0`, `channelRead` in the server and `channelActive`, `channelRead0` in client are the critical part of this little project. These four functions basically handle the in/out of all the requests and responses.

Netty is worth learning more in the near future



### Multiple Threads

#### Future

In order to get the result from the server. As for my understanding, the client request is running by one thread, and the response from the server is running by a another thread.(I assumed the response comes in the client end, it is running inside the Netty on client-end. Therefore request and resposne are on the same processor). When it comes to getting the result in asynchronously, the request thread will cause blocking, and it will not go forward without the result. Therefore, future is brought in, as a mean to solve this kind of problem.

