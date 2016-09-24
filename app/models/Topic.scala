package models

case class TopicField(name: String,
                      typ: String)

case class Stage(stage: String,
                 desc: String,
                 fields: List[TopicField])

case class Topic( //_id:String,
                  topic: String,
                  desc: String,
                  stages: List[Stage])

object TopicFormats {
  import play.api.libs.json.Json
  implicit val topicFieldFormat = Json.format[TopicField]
  implicit val stageFormat = Json.format[Stage]
  implicit val topicFormat = Json.format[Topic]
}