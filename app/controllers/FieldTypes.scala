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
class FieldTypes extends Controller with MongoController {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[FieldTypes])

  /*
   * Get a JSONCollection (a Collection implementation that is designed to work
   * with JsObject, Reads and Writes.)
   * Note that the `collection` is not a `val`, but a `def`. We do _not_ store
   * the collection reference to avoid potential problems in development with
   * Play hot-reloading.
   */
  def collection: JSONCollection = db.collection[JSONCollection]("fieldtypes")

  // ------------------------------------------ //
  // Using case classes + Json Writes and Reads //
  // ------------------------------------------ //
  import models.FieldType
  import models.FieldTypeFormats._

  def createFieldType = Action.async(parse.json) {
    request =>
      /*
       * request.body is a JsValue.
       * There is an implicit Writes that turns this JsValue as a JsObject,
       * so you can call insert() with this JsValue.
       * (insert() takes a JsObject as parameter, or anything that can be
       * turned into a JsObject using a Writes.)
       */
      request.body.validate[FieldType].map {
        fieldtype =>
          // `fieldtype` is an instance of the case class `models.FieldType`
          collection.insert(fieldtype).map {
            lastError =>
              logger.debug(s"Successfully inserted with LastError: $lastError")
              Created(s"Fieldtype Created")
              //Results.Ok(Json.toJson(lastError)).as(ContentTypes.JSON)
          }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def findFieldTypes = Action.async {
    // let's do our query
    val cursor: Cursor[FieldType] = collection.
      // find all
      find(Json.obj()).
      // sort them by creation date
      //sort(Json.obj("created" -> -1)).
      // perform the query and get a cursor of JsObject
      cursor[FieldType]

    // gather all the JsObjects in a list
    val futureUsersList: Future[List[FieldType]] = cursor.collect[List]()

    // transform the list into a JsArray
    val futurePersonsJsonArray: Future[JsArray] = futureUsersList.map { fieldtypes =>
      Json.arr(fieldtypes)
    }
    // everything's ok! Let's reply with the array
    futurePersonsJsonArray.map {
      fieldtypes =>
        Ok(fieldtypes(0))
    }
  }

}
