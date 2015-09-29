import com.rabbitmq.client._
import java.io.IOException

import java.util.HashMap
import java.nio.charset.StandardCharsets

import play.api.libs.json._

import MyImplicits._



object SubscriberDemo {

  def main (args:Array[String]):Unit = {
    val r = new SubscriberDemo ()
    r.Receive
  }
}


/*
JSON

http://stackoverflow.com/questions/19436069/adding-play-json-library-to-sbt/20475410#20475410
https://www.playframework.com/documentation/2.2.x/ScalaJson


FAILED

Spray.Json : Added extra "type" part into JSON
Lift.Json :  SBT was not happy

 */

class SubscriberDemo {
  val EXCHANGE_NAME = "logs"

  def Receive () = {

    //PRODUCER CODE TO REMOVE
    val factory = new ConnectionFactory()
    factory.setHost("localhost")
    val connection = factory.newConnection()
    val channel = connection.createChannel()

    channel.exchangeDeclare(EXCHANGE_NAME, "fanout")

    for (i <- (0 to 100)) {

      val person = new Person("named from scala " + i.toString(), i)
      val message = Json.toJson(person)
      val bytes =  message.toString().getBytes(StandardCharsets.UTF_8)
      val headers = new HashMap[String,AnyRef]()
      headers.putIfAbsent("type","1")


      val props = new AMQP.BasicProperties.Builder().headers(headers)
      channel.basicPublish(EXCHANGE_NAME,"",props.build() , bytes)
      System.out.println(" [x] Sent '" + message + "'")
      Thread.sleep(1000)
    }
    channel.close()
    connection.close()


    /*
    JAVA BASED CONSUMER CODE TO WRITE ABOVE


    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
    String queueName = channel.queueDeclare().getQueue();
    channel.queueBind(queueName, EXCHANGE_NAME, "");

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope,
                                 AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(" [x] Received '" + message + "'");
      }
    };
    channel.basicConsume(queueName, true, consumer);
  }

     */

  }
}
