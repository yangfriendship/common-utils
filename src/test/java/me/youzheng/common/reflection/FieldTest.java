package me.youzheng.common.reflection;

import me.youzheng.common.reflection.domain.PersonSub;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {

    @Test
    public void getFieldsTest() {
        Field[] fields = PersonSub.class.getDeclaredFields();
        assertEquals(1, fields.length, "getDeclaredFields() 는 슈퍼클래스의 필드를 제외한 모든 필드 정보를 조회");

        Field[] fields1 = PersonSub.class.getFields();
        assertEquals(2, fields1.length, "getFields() 는 슈퍼클래스의 필드를 포함한 모든 public access 필드 정보를 조회");
    }

    @Test
    public void givenFieldNameTest() throws NoSuchFieldException {
        Field teamName = PersonSub.class.getDeclaredField("teamName");
        assertNotNull(teamName, "getDeclaredField() 는 접근제어자에 상관없이 정의된 필드를 조회");

        assertThrows(NoSuchFieldException.class, () -> {
            Field findByFeildName = PersonSub.class.getField("name");
        }, "존재하지 않는 필드라면 NoSuchFieldException.class 가 발생");

    }

}
