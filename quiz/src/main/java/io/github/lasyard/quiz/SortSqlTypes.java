package io.github.lasyard.quiz;

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.Arrays;

public final class SortSqlTypes {
    private SortSqlTypes() {
    }

    public static void main(String[] args) throws IllegalAccessException {
        Field[] fields = Types.class.getFields();
        for (Field field : fields) {
            System.out.println(field.getName() + " = " + field.getInt(null));
        }
        Arrays.sort(fields, (f1, f2) -> {
            try {
                int i1 = f1.getInt(null);
                int i2 = f2.getInt(null);
                return Integer.compare(i1, i2);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println("Sorted by value:");
        for (Field field : fields) {
            System.out.println(field.getName() + " = " + field.getInt(null));
        }
    }
}
