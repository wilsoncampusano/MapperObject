package mapper;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Created by Gaperez on 8/5/2015.
 */
public abstract class Mapper {
    public static <TDestination> TDestination map(Object source, Class<TDestination> clazz) {
        try {
            TDestination destination = clazz.newInstance();
            for (Field toField : destination.getClass().getDeclaredFields()) {
                if (toField.isAnnotationPresent(Mapping.class)) {

                    Mapping mapping = toField.getAnnotation(Mapping.class);
                    String fieldName = !mapping.name().equals("") ? mapping.name() : toField.getName();
                    boolean isOtherType = mapping.otherType();

                    Field fromField = source.getClass().getDeclaredField(fieldName);
                    boolean isEqualType = fromField.getType().equals(toField.getType());

                    toField.setAccessible(true);
                    fromField.setAccessible(true);

                    if (isEqualType) {
                        toField.set(destination, fromField.get(source));
                    } else if (isOtherType) {
                        Object value = map(fromField.get(source), toField.getType());
                        toField.set(destination, value);
                    }
                }
            }
            return destination;
        } catch (IllegalAccessException e) {
            //TODO: implement exception.
        } catch (NoSuchFieldException e) {
            //TODO: implement exception.
        } catch (InstantiationException e) {
            //TODO: implement exception.
        }

        return null;
    }
}