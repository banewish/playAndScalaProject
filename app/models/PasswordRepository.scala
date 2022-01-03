package models

import controllers.UserDataForm.UserData

import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.SetParameter.SetBigDecimal.tupled
import slick.jdbc.SetParameter._

import scala.concurrent.{ExecutionContext, Future}


class PasswordRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  import controllers.HomeController
  import dbConfig._
  import profile.api._

  private class PasswordTable(tag: Tag) extends Table[PasswordHash](tag, "hashes") {

    def password_hash = column[String]("password_hash")

    def * = password_hash <> ((PasswordHash.apply _), PasswordHash.unapply _)
  }

  private val passwords = TableQuery[PasswordTable]


  def create(password_hash: String): Future[Int] = db.run {

    val queryForCreate =
    passwords.map(p => (p.password_hash)) += passwordHash(passwords.toString) // already with hash, method of hash below

    queryForCreate
  }



  def createIfNotExists(password_hash : String) : Future[Any] = db.run {
    val queryForCreateIfNotExists =
      passwords.filter(_.password_hash === passwordHash(passwords.toString)).exists.result.flatMap { exists =>
        if (!exists) {
          passwords.map(p => (p.password_hash)) += passwordHash(passwords.toString)
        } else {
          DBIO.successful(None)
        }
      }.transactionally

    queryForCreateIfNotExists
  }




  def list(): Future[Seq[PasswordHash]] = db.run {
    passwords.result

  }
  def delete() : Future[Int] = db.run {
    passwords.filter(p => p.password_hash === "5b2fa449c0c0a5c2e601d9533a24cb4d")
      .delete
  }


// method hash password
  def passwordHash(s: String): String = {
    import java.security.MessageDigest
    import java.math.BigInteger
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(s.getBytes)
    val bigInt = new BigInteger(1, digest)
    val hashedString = bigInt.toString(16)
    hashedString
  }
}