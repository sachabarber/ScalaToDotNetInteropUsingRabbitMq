import play.api.libs.json.{JsPath, Json, Writes, Reads}
import play.api.libs.functional.syntax._



trait RabbitJsonMessage
case class Person(name: String, age: Int) extends  RabbitJsonMessage
{
  override def toString : String = {
    s"Name : $name, Age : $age"
  }
}

object JsonImplicits {

  implicit val personWrites = new Writes[Person] {
    def writes(person: Person) = Json.obj(
      "name" -> person.name,
      "age" -> person.age
    )
  }

  implicit val personReads : Reads[Person] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "age").read[Int]
    )(Person.apply _)
}