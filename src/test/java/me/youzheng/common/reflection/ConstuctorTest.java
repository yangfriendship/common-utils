package me.youzheng.common.reflection;

import me.youzheng.common.reflection.domain.Person;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public class ConstuctorTest {

    @Test
    public void 생성자를_조회하는_메서드() {
        Constructor<?>[] constructors = Person.class.getDeclaredConstructors();
        assertEquals(3, constructors.length, "public, non-public 모든 생성자를 조회한다..");

        Constructor<?>[] constructors1 = Person.class.getConstructors();
        assertEquals(2, constructors1.length, "public 생성자만 조회한다.");
    }

    @Test
    public void 생성자_파라미터를_통해_조회할수_있다() throws NoSuchMethodException {
        Constructor<Person> nameStringConstructor = Person.class.getDeclaredConstructor(String.class);
        assertNotNull(nameStringConstructor);

        assertThrows(NoSuchMethodException.class, () -> {
            Constructor<Person> notExistConstructor = Person.class.getDeclaredConstructor(Person.class);
        }, "존재하지 않는 생성자를 조회하면 NoSuchMethodException.class 가 발생한다.");

    }

    @Test
    public void 생성자클래스를_이용한_객체생성() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<Person> allConstructor = Person.class.getDeclaredConstructor(String.class, Integer.class);
        Person person2 = allConstructor.newInstance("HI", 1);
        assertNotNull(person2);
        assertEquals("HI", person2.getName());
        assertEquals(1, person2.getAge());
    }

    @Test
    public void 리플랙션을_이용한_객체생성() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<Person> declaredConstructor = Person.class.getDeclaredConstructor(String.class);
        declaredConstructor.setAccessible(true); // 생성자가 private 이지만 강제로 접근할 수 있다.
        Person person = declaredConstructor.newInstance("HI");
        assertNotNull(person);
        assertEquals("HI", person.getName());
        assertNull(person.getAge());
        Constructor<Person> allConstructor = Person.class.getDeclaredConstructor(String.class, Integer.class);
        Person person2 = allConstructor.newInstance("HI", 1);
        assertNotNull(person2);
        assertEquals("HI", person2.getName());
        assertEquals(1, person2.getAge());
    }

}