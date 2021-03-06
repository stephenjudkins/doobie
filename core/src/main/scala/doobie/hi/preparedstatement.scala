package doobie.hi

import doobie.enum.holdability.Holdability
import doobie.enum.transactionisolation.TransactionIsolation
import doobie.enum.fetchdirection.FetchDirection
import doobie.enum.resultsetconcurrency.ResultSetConcurrency
import doobie.enum.resultsettype.ResultSetType

import doobie.syntax.catchable._

import doobie.free.{ connection => C }
import doobie.free.{ preparedstatement => PS }
import doobie.free.{ callablestatement => CS }
import doobie.free.{ resultset => RS }
import doobie.free.{ statement => S }
import doobie.free.{ databasemetadata => DMD }

import doobie.util.composite._
import doobie.util.process.resource

import java.net.URL
import java.util.{ Date, Calendar }
import java.sql.{ ParameterMetaData, ResultSetMetaData, SQLWarning, Time, Timestamp, Ref, RowId }

import scala.collection.immutable.Map
import scala.collection.JavaConverters._
import scala.Predef.intArrayOps

import scalaz.stream.Process
import scalaz.syntax.id._

/**
 * Module of high-level constructors for `PreparedStatementIO` actions. Batching operations are not
 * provided; see the `statement` module for this functionality.
 * @group Modules
 */
object preparedstatement {

  /** @group Typeclass Instances */
  implicit val MonadPreparedStatementIO = PS.MonadPreparedStatementIO

  /** @group Typeclass Instances */
  implicit val CatchablePreparedStatementIO = PS.CatchablePreparedStatementIO

  /** @group Execution */
  def process[A: Composite]: Process[PreparedStatementIO, A] =
    resource(PS.executeQuery)(rs =>
             PS.liftResultSet(rs, RS.close))(rs => 
             PS.liftResultSet(rs, resultset.getNext[A]))

  /**
   * Non-strict unit for capturing effects.
   * @group Constructors (Lifting)
   */
  def delay[A](a: => A): PreparedStatementIO[A] =
    PS.delay(a)

  /** @group Execution */
  def executeQuery[A](k: ResultSetIO[A]): PreparedStatementIO[A] =
    PS.executeQuery.flatMap(s => PS.liftResultSet(s, k ensuring RS.close))

  /** @group Execution */
  val executeUpdate: PreparedStatementIO[Int] =
    PS.executeUpdate

  /** @group Properties */
  val getFetchDirection: PreparedStatementIO[FetchDirection] =
    PS.getFetchDirection.map(FetchDirection.unsafeFromInt)

  /** @group Properties */
  val getFetchSize: PreparedStatementIO[Int] =
    PS.getFetchSize

  /** @group Results */
  def getGeneratedKeys[A](k: ResultSetIO[A]): PreparedStatementIO[A] =
    PS.getGeneratedKeys.flatMap(s => PS.liftResultSet(s, k ensuring RS.close))

  /** @group Properties */
  val getMaxFieldSize: PreparedStatementIO[Int] =
    PS.getMaxFieldSize

  /** @group Properties */
  val getMaxRows: PreparedStatementIO[Int] =
    PS.getMaxRows
     
  /** @group MetaData */
  val getMetaData: PreparedStatementIO[ResultSetMetaData] =
    PS.getMetaData

  /** @group MetaData */
  val getParameterMetaData: PreparedStatementIO[ParameterMetaData] =
    PS.getParameterMetaData

  /** @group Properties */
  val getQueryTimeout: PreparedStatementIO[Int] =
    PS.getQueryTimeout

  /** @group Properties */
  val getResultSetConcurrency: PreparedStatementIO[ResultSetConcurrency] =
    PS.getResultSetConcurrency.map(ResultSetConcurrency.unsafeFromInt)

  /** @group Properties */
  val getResultSetHoldability: PreparedStatementIO[Holdability] =
    PS.getResultSetHoldability.map(Holdability.unsafeFromInt)

  /** @group Properties */
  val getResultSetType: PreparedStatementIO[ResultSetType] =
    PS.getResultSetType.map(ResultSetType.unsafeFromInt)

  /** @group Results */
  val getWarnings: PreparedStatementIO[SQLWarning] =
    PS.getWarnings

  /** 
   * Set the given composite value, starting at column `n`.
   * @group Parameters 
   */
  def set[A](n: Int, a: A)(implicit A: Composite[A]): PreparedStatementIO[Unit] =
    A.set(n, a)

  /** 
   * Set the given composite value, starting at column `1`.
   * @group Parameters 
   */
  def set[A](a: A)(implicit A: Composite[A]): PreparedStatementIO[Unit] =
    A.set(1, a)

  /** @group Properties */
  def setCursorName(name: String): PreparedStatementIO[Unit] =
    PS.setCursorName(name)

  /** @group Properties */
  def setEscapeProcessing(a: Boolean): PreparedStatementIO[Unit] =
    PS.setEscapeProcessing(a)

  /** @group Properties */
  def setFetchDirection(fd: FetchDirection): PreparedStatementIO[Unit] =
    PS.setFetchDirection(fd.toInt)

  /** @group Properties */
  def setFetchSize(n: Int): PreparedStatementIO[Unit] =
    PS.setFetchSize(n)

  /** @group Properties */
  def setMaxFieldSize(n: Int): PreparedStatementIO[Unit] =
    PS.setMaxFieldSize(n)

  /** @group Properties */
  def setMaxRows(n: Int): PreparedStatementIO[Unit] =
    PS.setMaxRows(n)

  /** @group Properties */
  def setQueryTimeout(a: Int): PreparedStatementIO[Unit] =
    PS.setQueryTimeout(a)

}