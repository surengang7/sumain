package com.sumain.common.utils;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public class BeanUtils {

    public static <T> T instantiateAndSetDefaults(Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            setDefaultValues(instance, clazz);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate and set default values", e);
        }
    }

    private static void setDefaultValues(Object instance, Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.getType().equals(String.class)) {
                    field.set(instance, "default value");
                }else if(field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)){
                    field.set(instance,false);
                } else if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
                    field.set(instance, 0L);
                } else if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
                    field.set(instance, 0);
                } else if (field.getType().isEnum()) {
                    Object enumValue = field.getType().getEnumConstants()[0];
                    field.set(instance, enumValue);
                } else if (field.getType().equals(List.class)) {
                    List<?> list = createAndInitializeList(field);
                    field.set(instance, list);
                } else if (isCustomClass(field.getType())) {
                    Object subInstance = instantiateAndSetDefaults(field.getType());
                    field.set(instance, subInstance);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to set default value for field: " + field.getName(), e);
            }
        }
    }

    private static List<?> createAndInitializeList(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType parameterizedType) {
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length > 0) {
                Class<?> listElementClass = (Class<?>) actualTypeArguments[0];
                List<Object> list = new ArrayList<>();
                Object element = instantiateAndSetDefaults(listElementClass);
                list.add(element);
                return list;
            }
        }
        return new ArrayList<>();
    }

    private static boolean isCustomClass(Class<?> clazz) {
        return !clazz.isPrimitive()
                && !clazz.isEnum()
                && !clazz.equals(String.class)
                && !clazz.equals(Long.class)
                && !clazz.equals(Integer.class)
                && !clazz.equals(Boolean.class);
    }
}