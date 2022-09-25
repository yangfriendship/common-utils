package me.youzheng.common.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class JsonSerializerUtil {

    public static String writeToJsonString(Object target) {
        return writeToJsonString(new StringBuilder(), target);
    }

    private static String writeToJsonString(StringBuilder stringBuilder, Object target) {
        if (target == null) {
            return "null";
        }
        if (target.getClass().isArray()) {
            stringBuilder.append("[");
            writeArrays(stringBuilder, target);
        } else  {
            stringBuilder.append("{");
        }
        Field[] fields = target.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            if (field.isSynthetic()) {
                continue;
            }
            writeKey(stringBuilder,field);
            writeValue(stringBuilder, field, target);
            if (i < fields.length -1) {
                stringBuilder.append(',');
            }
        }

        if (target.getClass().isArray()) {
            stringBuilder.append("]");
        } else  {
            stringBuilder.append("}");
        }
        return stringBuilder.toString();
    }

    private static void writeValue(StringBuilder stringBuilder, Field field, Object target) {
        if (isPrimitiveType(field)) {
            writePrimitiveType(stringBuilder,field, target);
        }
        else if (isWrapperClass(field)){
            writeWrapperType(stringBuilder, field, target);
        } else {
            try {
                stringBuilder.append(writeToJsonString(field.get(target)));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void writeWrapperType(StringBuilder stringBuilder, Field field, Object target) {
        try {
            if (field.getType().equals(Float.class) || field.getType().equals(Double.class)) {
                stringBuilder.append(String.format("%.02f", field.get(target)));
            } else {
                Object value = field.get(target);
                if (value == null) {
                    stringBuilder.append("null");
                } else {
                    stringBuilder.append(field.get(target));
                }

            }
        } catch (IllegalAccessException e) {
            throw new JsonUtilException();
        }
    }

    private static void writeKey(StringBuilder stringBuilder, Field field) {
        stringBuilder.append('\"')
                .append(field.getName())
                .append('\"')
                .append(':');
    }

    private static void writePrimitiveType(StringBuilder stringBuilder,Field field, Object target) {
        try {
             if (field.getType().equals(float.class) || field.getType().equals(double.class)) {
                stringBuilder.append(String.format("%.02f", field.get(target)));
            } else {
                 stringBuilder.append(field.get(target).toString());
             }
        } catch (IllegalAccessException e) {
            throw new JsonUtilException();
        }
    }

    private static boolean isPrimitiveType(Field field) {
        return isPrimitiveType(field.getType());
    }

    private static boolean isPrimitiveType(Class<?> clazz) {
        return clazz.equals(int.class)
                || clazz.equals(long.class)
                || clazz.equals(short.class)
                || clazz.equals(byte.class)
                || clazz.equals(boolean.class)
                || clazz.equals(double.class)
                || clazz.equals(float.class)
                || clazz.equals(char.class);
    }

    private static boolean isWrapperClass(Field field) {
        return isWrapperClass(field.getType());
    }

    private static boolean isWrapperClass(Class<?> clazz) {
        return clazz.equals(Integer.class)
                || clazz.equals(Long.class)
                || clazz.equals(Short.class)
                || clazz.equals(Byte.class)
                || clazz.equals(Boolean.class )
                || clazz.equals(Double.class)
                || clazz.equals(String.class)
                || clazz.equals(Float.class)
                || clazz.equals(Character.class);
    }


    private static void writeArrays(StringBuilder stringBuilder, Object array) {
        int length = Array.getLength(array);
        Class<?> componentType = array.getClass().getComponentType();
        for (int i = 0; i < length; i++) {
            Object target = Array.get(array, i);
            if (isPrimitiveType(componentType) || isWrapperClass(componentType)) {
                stringBuilder.append('\"').append(target.toString()).append('\"');
            } else {
                writeToJsonString(stringBuilder, target);
            }
            if (i < length -1 && stringBuilder.charAt(stringBuilder.length() -1) != '[') {
                stringBuilder.append(',');
            }
        }
    }

    public static class JsonUtilException extends RuntimeException {

        public JsonUtilException() {
        }

        public JsonUtilException(String message) {
            super(message);
        }
    }

}
