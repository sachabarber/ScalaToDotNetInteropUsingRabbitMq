import java.util.HashMap
import com.rabbitmq.client._
import scala.reflect.ClassTag
import scala.runtime.RichInt
import scala.reflect.runtime.universe._
import play.api.libs.json.{JsValue, Json, Writes}
import JsonImplicits ._


object SubscriberDemo {

  def main (args:Array[String]): Unit = {
    val r = new SubscriberDemo()
    r.Receive()
  }
}


class SubscriberDemo {
  val EXCHANGE_NAME = "logs"

  def Receive() = {

      val factory = new ConnectionFactory()
      factory.setHost("localhost")
      val connection = factory.newConnection()
      val channel = connection.createChannel()

      channel.exchangeDeclare(EXCHANGE_NAME, "fanout")
      val queueName = channel.queueDeclare().getQueue()
      channel.queueBind(queueName, EXCHANGE_NAME, "")

      val typelookup = new HashMap[Int, JsValue => RabbitJsonMessage]()
      typelookup.putIfAbsent(1,value =>
      {
          val person = Json.fromJson[Person](value).get
          person.asInstanceOf[RabbitJsonMessage]
      })

      System.out.println(" [*] Waiting for messages. To exit press CTRL+C")
      val consumer = new DefaultConsumer(channel) {


      override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: scala.Array[scala.Byte] ) =
      {
          val typeToLookup = properties.getHeaders().get("type").toString().toInt
          val jsonConverter = typelookup.get(typeToLookup)
          val messageBody = new String(body, "UTF-8")
          val jsonObject = Json.parse(messageBody)
          val person = jsonConverter(jsonObject).asInstanceOf[Person]
          System.out.println(" [x] Received '" + person + "'");
      }

    }
    channel.basicConsume(queueName, true, consumer)

  }
}
