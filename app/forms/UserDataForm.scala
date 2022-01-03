package forms

object UserDataForm {

  import play.api.data.Forms._
  import play.api.data._

  case class UserData(password: String)

  val userFormConstraints = Form(
    mapping(
      "password" -> nonEmptyText
    )(UserData.apply)(UserData.unapply)
  )
}
