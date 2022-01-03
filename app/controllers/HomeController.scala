package controllers




import javax.inject._
import play.api.mvc._
import play.api._
import play.api.libs.json.Json
import services.PasswordService

import scala.concurrent._
import ExecutionContext.Implicits.global

class HomeController @Inject()(passService: PasswordService, cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {

  import forms.UserDataForm._


  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def validate() = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.registration(userFormConstraints))
  }

  def formPost() = Action.async { implicit request =>
    userFormConstraints.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(Ok(views.html.registration(formWithErrors)))
      },
      formData => {
        passService.createIfNotExists(formData.toString).map { _ =>
          Redirect(routes.HomeController.index)

        }
      }
    )
  }
 //

  def getPassword = Action.async { implicit request =>
    passService.list().map { passwords =>
      Ok(Json.toJson(passwords.toString()))
    }
  }

  def deletePassword = Action.async { implicit request =>
    passService.delete().map { passwords =>
      Ok(Json.toJson(passwords.toString()))
    }
  }

}

// получить, хешировать, отправить на сервис и проверить наличие в базе
// если есть - ошибка, если нет - добавить в базу


