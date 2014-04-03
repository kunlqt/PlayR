package twentysix.playr

import play.api.mvc.EssentialAction
import play.api.mvc.Controller

object ResourceCaps extends Enumeration {
  type ResourceCaps = Value
  val Read, Write, Create, Delete, Update, Parent, Child, Action = Value
}

/**
 * Define the conversion from an url id to a real object
 */
trait BaseResource extends Controller {
  def name: String
  type IdentifierType

  def toNumber[N](id: String, f: String => N): Option[N] = {
    import scala.util.control.Exception._
    catching(classOf[NumberFormatException]) opt f(id)
  }

  def toInt(id: String) = toNumber(id, _.toInt)
  def toLong(id: String) = toNumber(id, _.toLong)

  def parseId(id: String): Option[IdentifierType]

  def requestWrapper(block: => Option[EssentialAction]): Option[EssentialAction] = block
}

trait Resource[R] extends BaseResource {
  type IdentifierType = R
}


/**
 * Respond to HTTP GET method
 */
trait ResourceRead {
  this: BaseResource =>

  def read(id: IdentifierType): EssentialAction
  def list: EssentialAction

  def readRequestWrapper(block: => Option[EssentialAction]): Option[EssentialAction] = requestWrapper(block)
}

/**
 * Respond to HTTP PUT method
 */
trait ResourceWrite {
  this: BaseResource =>

  def write(id: IdentifierType): EssentialAction

  def writeRequestWrapper(block: => Option[EssentialAction]): Option[EssentialAction] = requestWrapper(block)
}

/**
 * Respond to HTTP POST method
 */
trait ResourceCreate {
  this: BaseResource =>

  def create: EssentialAction
}

/**
 * Respond to HTTP DELETE method
 */
trait ResourceDelete {
  this: BaseResource =>

  def delete(id: IdentifierType): EssentialAction

  def deleteRequestWrapper(block: => Option[EssentialAction]): Option[EssentialAction] = requestWrapper(block)
}


/**
 * Respond to HTTP PATCH method
 */
trait ResourceUpdate {
  this: BaseResource =>

  def update(id: IdentifierType): EssentialAction

  def updateRequestWrapper(block: => Option[EssentialAction]): Option[EssentialAction] = requestWrapper(block)
}



//-------------------------
//---- Shortcut traits ----
//-------------------------

trait RestReadController[R] extends Resource[R]
                               with ResourceRead

/**
 * Read and write controller: implements GET, POST and PATCH for partial updates
 */
trait RestRwController[R] extends Resource[R]
                             with ResourceCreate
                             with ResourceRead
                             with ResourceUpdate

/**
 * Same as RestRWController plus DELETE method
 */
trait RestRwdController[R] extends RestRwController[R]
                              with ResourceDelete

/**
 * Classic rest controller: handle GET, POST, PUT and DELETE http methods
 */
trait RestCrudController[R] extends Resource[R]
                               with ResourceCreate
                               with ResourceRead
                               with ResourceDelete
                               with ResourceWrite
