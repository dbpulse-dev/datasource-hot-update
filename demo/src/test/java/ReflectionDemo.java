import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionDemo {
    public static void main(String[] args) throws Exception {
        MyClass obj = new MyClass();
        System.out.println("Before: " + obj.getValue()); // 输出 "Before: 10"
        Field field2 = MyClass.class.getDeclaredField("user");
        Field field = MyClass.class.getDeclaredField("value");
        field.setAccessible(true);

        // 修改 final 标志位
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        // 设置新值
        field.set(obj, 20);

        System.out.println("After: " + obj.getValue()); // 输出 "After: 20"
    }
}

class MyClass {
    private final int value = 10;
    private User user;


    public int getValue() {
        return value;
    }
}


class User {
    private String name;
}
