using System;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using System.Text;
using System.Collections.Generic;
using Common;
using Newtonsoft.Json;

namespace Subscriber
{
    class Program
    {
        public static void Main()
        {
            Dictionary<int, Type> typelookup = new Dictionary<int, Type>();
            typelookup.Add(1, typeof(Person));

            var factory = new ConnectionFactory() { HostName = "localhost" };
            using (var connection = factory.CreateConnection())
            using (var channel = connection.CreateModel())
            {
                channel.ExchangeDeclare(exchange: "logs", type: "fanout");

                var queueName = channel.QueueDeclare().QueueName;
                channel.QueueBind(queue: queueName,
                                  exchange: "logs",
                                  routingKey: "");

                Console.WriteLine(" [*] Waiting for logs.");

                var consumer = new EventingBasicConsumer(channel);
                consumer.Received += (model, ea) =>
                {
                    var body = ea.Body;
                    var typeToLookupBytes = (Byte[])ea.BasicProperties.Headers["type"];
                    var typeToLookup = int.Parse(Encoding.UTF8.GetString(typeToLookupBytes));
                    var messageType = typelookup[typeToLookup];
                    var message = Encoding.UTF8.GetString(body);
                    var person = JsonConvert.DeserializeObject(message, messageType);

                    Console.WriteLine("[Recieved] message : {0}", person);
                };
                channel.BasicConsume(queue: queueName,
                                     noAck: true,
                                     consumer: consumer);

                Console.WriteLine(" Press [enter] to exit.");
                Console.ReadLine();
            }
        }
    }
}
