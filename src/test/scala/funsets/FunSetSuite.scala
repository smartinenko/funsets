package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  // test("string take") {
  //   val message = "hello, world"
  //   assert(message.take(5) == "hello")
  // }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  // test("adding ints") {
  //   assert(1 + 2 === 3)
  // }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements of each set") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersect contains all elements that are in both sets") {
    new TestSets {
      val s = intersect(union(s1, s2), union(s2, s3))
      assert(!contains(s, 1), "Intersect 1")
      assert(contains(s, 2), "Intersect 2")
      assert(!contains(s, 3), "Intersect 3")
    }
  }

  test("diff contains all elements that are in first set and not in second sets") {
    new TestSets {
      val s = diff(union(s1, s2), union(s2, s3))
      assert(contains(s, 1), "diff 1")
      assert(!contains(s, 2), "diff 2")
      assert(!contains(s, 3), "diff 3")
    }
  }

  test("filter contains all elements that match filter") {
    new TestSets {
      val s = filter(union(union(s1, s2), s3), x => x == 1)
      assert(contains(s, 1), "filter 1")
      assert(!contains(s, 2), "filter 2")
      assert(!contains(s, 3), "filter 3")
    }
  }

  test("forall values in [-1000:1000] x >= -1000 - true") {
    assert(forall(x => x >= -1000, x => x >= -1000), "forall values in [-1000:1000] x >= -1000 - true")
  }

  test("forall values in [-1000:1000] x == 0 - false") {
    assert(!forall(x => x >= -1000 && x <=1000, x => x == 0), "forall values in [-1000:1000] x == 0 - false")
  }

  test("forall values positive multipliers of 3 true") {
    assert(forall(x => x > 0 && x % 3 == 0, x => x % 3 == 0), "forall values positive multipliers of 3 true")
  }

  test("exists values in [-1000:1000] == 100") {
    assert(exists(x => x >= -1000 && x <=1000, x => x == 100), "exists values in [-1000:1000] == 100")
  }

  test("exists multipliers of 10 contain 100") {
    assert(exists(x => x % 10 == 0, x => x == 100), "exists multipliers of 10 contain 100")
  }

  test("exists: given {1,3,4,5,7,1000}") {
    val s: Set = x => List(1,3,4,5,7,1000) contains x
    assert(!exists(s, x => x == 2), "2 shouldn't exist in the given set")
  }

  test("map multipliers of 10") {
    assert(forall(map(x => x > 0 && x <= 10, x => x * 10), p => p % 10 == 0), "exists all x % 10 == 0 in range 10 - 100")
  }

  test("map {1,3,4,5,7,1000}") {
    val s: Set = x => List(1,3,4,5,7,1000) contains x
    val f: Int => Int = x => x - 1
    assert(forall(map(s, f), x => List(0,2,3,4,6,999) contains x), "map {1,3,4,5,7,1000}")
    assert(contains(map(s, f), 999), "map {1,3,4,5,7,1000} contains 999")

    printSet(map(s, f))
  }
}
