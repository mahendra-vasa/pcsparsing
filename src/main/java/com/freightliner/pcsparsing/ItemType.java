package com.freightliner.pcsparsing;

import java.io.Serializable;
import java.util.*;

/**
 * This class defines all the types of Item and stored them in a map..
 *
 * @author   jftl8v
 * 
 */
public class ItemType implements Serializable {
  
 /**
  * A map for storing all the types.
  */
  private static final Map map = new HashMap();
 
  //  All the Item types
  public static final ItemType ASSEMBLY = new ItemType("A", "Assembly");
  public static final ItemType BOM = new ItemType("B", "BOM");
  public static final ItemType CWO = new ItemType("C", "CWO");
  public static final ItemType DRAWING = new ItemType("D", "Drawing");
  public static final ItemType E = new ItemType("E", "E");
  public static final ItemType F = new ItemType("F", "F");
  public static final ItemType H = new ItemType("H", "H");
  public static final ItemType K = new ItemType("K", "K");
  public static final ItemType L = new ItemType("L", "L");
  public static final ItemType M = new ItemType("M", "M");
  public static final ItemType N = new ItemType("N", "N");
  public static final ItemType PART = new ItemType("P", "Part");
  public static final ItemType R = new ItemType("R", "R");
  
 /**
  * The type code
  */
  private final String code;
  
 /**
  * The type name
  */
  private final String name;
  
  // @TODO: Remove? Not allowed?
  public static final ItemType EMPTY = new ItemType(" ", " ");
  public static final ItemType NULL = new ItemType(null, "");

 /**
  * Get item type according to the type code.
  * @param String typeCode: the type code
  * @return ItemType	the item type
  */
  public static ItemType get(String typeCode) {
    ItemType type = (ItemType) map.get(typeCode);
    if (type == null) {
      throw new RuntimeException("Could not find item type '" + typeCode + "'");
    }
    return type;
  }

 /**
  * Constructor.
  * @param String code: a string for type code
  * @param String name: a type name
  */
  private ItemType(String code, String name) {
    this.code = code;
    this.name = name;
    map.put(code, this);
  }

 /**
  * Get a string of the object
  * @return String	the name of the item type
  */
  public String toString() {
    return name;
  }
 
 /**
  * Get the type code.
  * @return String	the type code
  */
  public String getCode() {
    return code;
  }

 /**
  * Compare the the item code between this item type and the other object.
  * @param Object o: an object (Module)
  * @return boolean	true/false
  */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ItemType)) return false;

    final ItemType itemType = (ItemType) o;

    if (code != null ? !code.equals(itemType.code) : itemType.code != null) return false;

    return true;
  }

 /**
  * Generate a hash code based on the item code.
  * @return int	a hash code
  */
  public int hashCode() {
    return (code != null ? code.hashCode() : 0);
  }
}
