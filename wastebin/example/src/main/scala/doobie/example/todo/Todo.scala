package doobie.example.todo

import doobie._
import doobie.hi._

import scalaz._, Scalaz._
import scalaz.effect._, stateTEffect._, IO._

object Todo extends SafeApp {

  val ta = DriverManagerTransactor[org.h2.Driver]("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "")

  ////// ENTRY POINT

  override def runc: IO[Unit] =
    for {
      _ <- greet
      _ <- ta.exec(DAO.init)
      _ <- Repl.run(ta)
      _ <- ungreet
    } yield ()

  def greet: IO[Unit] =
    putStrLn(s"""
      |Welcome to TODO
      |Type help for instructions.""".stripMargin)

  def ungreet: IO[Unit] =
    putStrLn(s"Bye.\n")

}
