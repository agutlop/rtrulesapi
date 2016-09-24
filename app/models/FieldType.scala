package models


case class FieldType( //_id: String,
                      tipo: String,
                      oper: List[Oper])

case class Oper(cod: String,
                desc: String)

object FieldTypeFormats {
  import play.api.libs.json.Json
  implicit val operFormat = Json.format[Oper]
  implicit val fieldTypeFormat = Json.format[FieldType]
}