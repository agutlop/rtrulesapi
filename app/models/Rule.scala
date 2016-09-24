package models

case class Id($oid: String)

case class Leaf(operador: String,
                field: String,
                value: String,
                tipo: String)

case class Condition(op: String,
                     conns: List[Condition],
                     leaf: Option[Leaf])

case class Rule( _id: Id,
                  name: String,
                  desc: String,
                  topic: String,
                  stage: String,
                  flag: Boolean,
                  user: String,
                  cond: Condition,
                  action: String,
                  actionFields: List[String])

case class NewRule( //_id: Id,
                 name: String,
                 desc: String,
                 topic: String,
                 stage: String,
                 flag: Boolean,
                 user: String,
                 cond: Condition,
                 action: String,
                 actionFields: List[String])

object RuleFormats {
  import play.api.libs.json.Json
  implicit val idFormat = Json.format[Id]
  implicit val leafFormat = Json.format[Leaf]
  implicit val conditionFormat = Json.format[Condition]
  implicit val ruleFormat = Json.format[Rule]
  implicit val newRuleFormat = Json.format[NewRule]
}
