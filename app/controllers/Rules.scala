package controllers

import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection

import scala.concurrent.Future
import reactivemongo.api.Cursor
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import org.slf4j.{Logger, LoggerFactory}
import javax.inject.Singleton

import play.api.mvc._
import play.api.libs.json._

@Singleton
class Rules extends Controller with MongoController {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Rules])

  /*
   * Get a JSONCollection (a Collection implementation that is designed to work
   * with JsObject, Reads and Writes.)
   * Note that the `collection` is not a `val`, but a `def`. We do _not_ store
   * the collection reference to avoid potential problems in development with
   * Play hot-reloading.
   */
  def collection: JSONCollection = db.collection[JSONCollection]("user_rules")

  // ------------------------------------------ //
  // Using case classes + Json Writes and Reads //
  // ------------------------------------------ //
  import models.Rule
  import models.NewRule
  import models.RuleFormats._


  def createRule = Action.async(parse.json) {
    request =>
      /*
       * request.body is a JsValue.
       * There is an implicit Writes that turns this JsValue as a JsObject,
       * so you can call insert() with this JsValue.
       * (insert() takes a JsObject as parameter, or anything that can be
       * turned into a JsObject using a Writes.)
       */
      logger.info(request.body.toString())
      request.body.validate[NewRule].map {
        rule =>
          // `rule` is an instance of the case class `models.Rule`
          collection.insert(rule).map {
            lastError =>
              logger.debug(s"Successfully inserted with LastError: $lastError")
              Created(s"Rule Created")
            //Results.Ok(Json.toJson(lastError)).as(ContentTypes.JSON)
          }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }



  def findRules = Action.async {
    // let's do our query
    val cursor: Cursor[Rule] = collection.
      // find all
      find(Json.obj()).
      // sort them by creation date
      //sort(Json.obj("created" -> -1)).
      // perform the query and get a cursor of JsObject
      cursor[Rule]

    // gather all the JsObjects in a list
    val futureTopicsList: Future[List[Rule]] = cursor.collect[List]()

    // transform the list into a JsArray
    val futureTopicsJsonArray: Future[JsArray] = futureTopicsList.map { rules =>
      Json.arr(rules)
    }
    // everything's ok! Let's reply with the array
    futureTopicsJsonArray.map {
      rules =>
        Ok(rules(0))
    }
  }

}
