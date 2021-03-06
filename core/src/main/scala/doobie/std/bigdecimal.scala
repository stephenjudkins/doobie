package doobie.std

import doobie.free.preparedstatement.setBigDecimal
import doobie.free.resultset.{ getBigDecimal, updateBigDecimal }
import doobie.util.atom.Atom
import doobie.enum.jdbctype

import scalaz.syntax.functor._

object bigdecimal {

  implicit val JavaBigDecimalAtom: Atom[java.math.BigDecimal] =
    Atom.atom(jdbctype.Numeric, setBigDecimal, updateBigDecimal, getBigDecimal)

  implicit val BigDecimalAtom: Atom[BigDecimal] = 
    JavaBigDecimalAtom.xmap(BigDecimal(_), _.bigDecimal)

}