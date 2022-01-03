package controllers

import com.google.inject.Inject
import models.{PasswordHash, PasswordRepository}

import scala.concurrent.Future

case class PasswordService @Inject()(repo: PasswordRepository) {

  def list(): Future[Seq[PasswordHash]] = repo.list()

  def create(password_hash: String): Future[Int] = repo.create(password_hash: String)

  def delete(): Future[Int] = repo.delete()

  def createIfNotExists(password_hash: String) : Future[Any] = repo.createIfNotExists(password_hash: String)

}
