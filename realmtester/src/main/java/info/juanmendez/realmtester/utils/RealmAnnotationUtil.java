package info.juanmendez.realmtester.utils;

import org.powermock.reflect.Whitebox;

import java.lang.reflect.Field;

import info.juanmendez.realmtester.dependencies.RealmStorage;
import info.juanmendez.realmtester.demo.models.RealmAnnotation;
import io.realm.RealmModel;

public class RealmAnnotationUtil {

    public static Object findPrimaryKey(RealmModel realmModel) {
        Class clazz = RealmModelUtil.getClass(realmModel);
        String primaryField = getPrimaryFieldName(clazz);

        if (primaryField != null) {
            return Whitebox.getInternalState(realmModel, primaryField);
        }

        return null;
    }

    public static String getPrimaryFieldName(Class clazz) {
        RealmAnnotation annotation = RealmStorage.getAnnotationMap().get(clazz);

        if (annotation == null)
            return null;

        return annotation.getPrimaryField();
    }

    public static Boolean isIndexed(Class clazz, String field) {
        RealmAnnotation annotation = RealmStorage.getAnnotationMap().get(clazz);

        if (annotation == null)
            return false;

        return annotation.geIndexedFields().contains(field);
    }

    public static Boolean isIgnored(Class clazz, String field) {
        RealmAnnotation annotation = RealmStorage.getAnnotationMap().get(clazz);

        if (annotation == null)
            return false;

        return annotation.getIgnoredFields().contains(field);
    }

    public static Boolean isIgnored(Field field) {
        return isIgnored(field.getDeclaringClass(), field.getName());
    }
}
