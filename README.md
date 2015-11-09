# ScalaToDotNetInteropUsingRabbitMq
<p>This repository shows how to get .NET code to communicate with Scala code using <a href="https://www.rabbitmq.com/" target="_blank">RabbitMQ</a></p>
<p>On both the .NET and Scala sides messages are sent as JSON.</p>

+ .NET Side : Uses JSON.Net as the JSON library
+ Scala Side : Uses Play JSON library

<p>It also uses the <a href="https://www.rabbitmq.com/" target="_blank">RabbitMQ</a> standard headers collection to send a header which tells the subscriber what type of message is being sent, such that it may deserialize it correctly.

<p>You can read more about this over at codeproject where I wrote this up <a href="http://www.codeproject.com/Articles/1037532/NET-Scala-Interop-Using-RabbitMQ" target="_blank">http://www.codeproject.com/Articles/1037532/NET-Scala-Interop-Using-RabbitMQ</a>



