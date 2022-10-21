package ca.bc.gov.educ.api.institute.util;

import java.lang.reflect.Field;

public class BeanComparatorUtil {

  private BeanComparatorUtil(){

  }

  public static <T> boolean compare(T t, T t2) {
    try {
      Field[] fields = t.getClass().getDeclaredFields();
      if (fields != null) {
        for (Field field : fields) {
          if (field.isAnnotationPresent(ComparableField.class)) {
            var firstField = field.get(t);
            var secondField = field.get(t2);
            if(firstField == null && secondField == null) {
              //Do Nothing
            } else if ((firstField == null && secondField != null) || (firstField != null && secondField == null))  {
              return false;
            } else if (!(field.get(t)).equals(field.get(t2))) {
              return false;
            }
          }
        }
      }
    }catch(Exception e){
      return false;
    }
    return true;
  }
}
