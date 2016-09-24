package models

case class Connector( //_id: String,
                      tipo: String,
                      desc: String,
                      conds: Int,
                      leaf: Int
                    )


object ConnectorFormats {
  import play.api.libs.json.Json
  implicit val connectorFormat = Json.format[Connector]
}