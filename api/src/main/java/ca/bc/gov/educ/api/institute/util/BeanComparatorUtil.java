package ca.bc.gov.educ.api.institute.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BeanComparatorUtil {

  private BeanComparatorUtil(){

  }

  @java.lang.SuppressWarnings("squid:S3011")
  public static <T> boolean compare(T t, T t2) {
    try {
      List<Field> fields = getAllModelFields(t.getClass());
      for (Field field : fields) {
        if (field.isAnnotationPresent(ComparableField.class)) {
          field.setAccessible(true);
          var firstField = field.get(t);
          var secondField = field.get(t2);
          if(checkForNulls(firstField, secondField) || (firstField != null && secondField != null && !(firstField.equals(secondField)))) {
            return false;
          }
          field.setAccessible(false);
        }
      }
    }catch(Exception e){
      return false;
    }
    return true;
  }

  @java.lang.SuppressWarnings("squid:S3740")
  public static List<Field> getAllModelFields(Class aClass) {
    List<Field> fields = new ArrayList<>();
    do {
      Collections.addAll(fields, aClass.getDeclaredFields());
      aClass = aClass.getSuperclass();
    } while (aClass != null);
    return fields;
  }

  private static boolean checkForNulls(Object firstField, Object secondField){
   return ((firstField == null && secondField != null) || (firstField != null && secondField == null));
  }
}
