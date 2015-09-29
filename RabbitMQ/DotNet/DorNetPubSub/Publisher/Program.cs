using System;
using RabbitMQ.Client;
using System.Text;
using Common;
using Newtonsoft.Json;
using RabbitMQ.Client.Framing;
using System.Collections.Generic;
using System.Threading;

namespace Publisher
{
    class Program
    {
        public static void Main(string[] args)
        {
            var factory = new ConnectionFactory() { HostName = "localhost" };
            using (var connection = factory.CreateConnection())
            using (var channel = connection.CreateModel())
            {
                channel.ExchangeDeclare(exchange: "logs", type: "fanout");

                Random rand = new Random();


                while (true)
                {
                    var message = GetMessage(rand);
                    var body = Encoding.UTF8.GetBytes(message);

                    var properties = new BasicProperties();
                    properties.Headers = new Dictionary<string, object>();
                    properties.Headers.Add("type", "1");

                    channel.BasicPublish(exchange: "logs",
                                         routingKey: "",
                                         basicProperties: properties,
                                         body: body);
                    Console.WriteLine(" [x] Sent {0}", message);
                    Thread.Sleep(1000);
                }
            }

           
            Console.ReadLine();
        }

        private static string GetMessage(Random rand)
        {
            Person p = new Person();
            p.Age = rand.Next(100);
            p.Name = string.Format("Name_{0}", p.Age);
            return JsonConvert.SerializeObject(p);
        }
    }
}
