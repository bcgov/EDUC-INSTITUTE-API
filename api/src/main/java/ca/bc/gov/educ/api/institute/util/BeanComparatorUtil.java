package ca.bc.gov.educ.api.institute.util;

import java.lang.reflect.Field;

public class BeanComparatorUtil {

  private BeanComparatorUtil(){

  }

  @java.lang.SuppressWarnings("squid:S3011")
  public static <T> boolean compare(T t, T t2) {
    try {
      Field[] fields = t.getClass().getDeclaredFields();
      if (fields != null) {
        for (Field field : fields) {
          if (field.isAnnotationPresent(ComparableField.class)) {
            field.setAccessible(true);
            var firstField = field.get(t);
            var secondField = field.get(t2);
            if(!checkForNulls(firstField, secondField) && firstField != null && secondField != null && !(firstField.equals(secondField))) {
              return false;
            }
            field.setAccessible(false);
          }
        }
      }
    }catch(Exception e){
      return false;
    }
    return true;
  }

  private static boolean checkForNulls(Object firstField, Object secondField){
   return ((firstField == null && secondField != null) || (firstField != null && secondField == null));
  }
}
