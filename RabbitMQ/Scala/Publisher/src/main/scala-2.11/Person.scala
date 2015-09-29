import play.api.libs.json.{Json, Writes}
import scala.runtime.RichInt

case class Person(name: String, age: RichInt)

object MyImplicits {
  implicit val personWrites = new Writes[Person] {
    def writes(person: Person) = Json.obj(
      "name" -> person.name,
      "age" -> person.age.toInt
    )
  }
}
