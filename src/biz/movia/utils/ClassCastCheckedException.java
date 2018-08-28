package biz.movia.utils;

/* Represents an error while trying to cast an instance.
 * For example, instead of executing the following code,
 *  String s="abc;
 *  int i = (int) s;
 * ClassCastCheckedException can be thrown.
 * Note, Java has a ClassCastException, but it is not a checked exception.
 */
public class ClassCastCheckedException extends Exception {
}
