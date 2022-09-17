package me.youzheng.common.typeutil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TypeConvertUtilTest {

    Map<String, Object> testMap;
    List<Map<String, Object>> maps;

    @BeforeEach
    public void setup() {
        this.testMap = new HashMap<>();
        this.testMap.put("stringKey", "value1");
        this.testMap.put("integerKey", 1);
        this.testMap.put("longKey", 2L);
        this.testMap.put("localDateTimeKey", LocalDateTime.of(2022, 9, 13, 17, 23));
        this.maps = Arrays.asList(this.testMap, this.testMap, this.testMap);
    }

    @Test
    public void 컨버터타입을_이용한_캐스팅() {
        // given

        // when
        String value1 = TypeConvertUtil.convertTo(() -> this.testMap.get("stringKey"), new ConvertType<String>() {
        });
        Integer value2 = TypeConvertUtil.convertTo(() -> this.testMap.get("integerKey"), new ConvertType<Integer>() {
        });

        // then
        assertEquals(this.testMap.get("stringKey"), value1);
        assertEquals(this.testMap.get("integerKey"), value2);
    }

    @Test
    public void 서플라이와_컨버터타입은_널일_수_없다() {

        // supplier == null
        assertThrows(NullPointerException.class, () -> {
            String result = TypeConvertUtil.convertTo(null, new ConvertType<String>() {
            });
        });

        assertThrows(NullPointerException.class, () -> {
            ConvertType<String> convertType = null;
            String result = TypeConvertUtil.convertTo(() -> null, convertType);
        });
    }

    @Test
    public void 서플라이어의_결과는_널이여도_괜찮다_널반환() {
        // supplier.get() return null
        assertDoesNotThrow(() -> {
            String result = TypeConvertUtil.convertTo(() -> null, new ConvertType<String>() {
            });
            assertNull(result);
        });
    }

    @Test
    public void 캐스팅_실패시_예외_반환() {
        // 컴파일러가 최적화를 진행한다. 메서드를 호출한 caller 에서 callee 의 반환값을 사용하지 않으므로 Supplier.get() 을 생략해준다.
        TypeConvertUtil.convertTo(() -> this.testMap.get("localDateTimeKey"), new ConvertType<String>() {
        });

        assertThrows(ClassCastException.class, () -> {
            String result = TypeConvertUtil.convertTo(() -> this.testMap.get("localDateTimeKey"), new ConvertType<String>() {
            });
        }, "타입이 일치하지 않아 ClassCastException 발생");
    }

    @Test
    public void 클래스를_이용한_변환_성공() {
        // given

        // when
        String stringValue = TypeConvertUtil.convertTo(() -> this.testMap.get("stringKey"), String.class);
        Integer integerValue = TypeConvertUtil.convertTo(() -> this.testMap.get("integerKey"), Integer.class);
        Long longValue = TypeConvertUtil.convertTo(() -> this.testMap.get("longKey"), Long.class);
        LocalDateTime localDateTimeValue = TypeConvertUtil.convertTo(() -> this.testMap.get("localDateTimeKey"), LocalDateTime.class);

        // then
        assertEquals(this.testMap.get("stringKey"), stringValue);
        assertEquals(this.testMap.get("integerKey"), integerValue);
        assertEquals(this.testMap.get("longKey"), longValue);
        assertEquals(this.testMap.get("localDateTimeKey"), localDateTimeValue);
    }

    @Test
    public void 컬렉션_타입_변환_성공() {
        // given

        // when
        List<Map<String, Object>> result = TypeConvertUtil.convertTo(() -> this.maps, new ConvertType<List<Map<String,Object>>>() {});

        // then
        assertEquals(this.maps.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertSame(this.maps.get(i), result.get(i));
        }
    }

    @Test
    public void convertToDefaultMapTest_기본맵타입_변환성공() {
        assertDoesNotThrow(() -> {
            Map<String, Object> result = TypeConvertUtil.convertToDefaultMap(() -> this.testMap);
            assertNotNull(result);
        }, "DefaultMap 의 타입은 Map<String,Object>, 형변환에 예외가 발생하면 안된다.");
    }

    @Test
    public void convertToDefaultMapList_기본맵리스트타입_변환성공() {
        assertDoesNotThrow(() -> {
            List<Map<String, Object>> result = TypeConvertUtil.convertToDefaultMapList(() -> this.maps);
            assertNotNull(result);
            assertEquals(this.maps.size(), result.size());
        }, "DefaultMapList 의 타입은 List<Map<String,Object>>, 형변환에 예외가 발생하면 안된다.");
    }

    @Test
    public void isNullOrEmptyTest_map() {
        Map<String, Object> map = null;
        assertTrue(TypeConvertUtil.isNullOrEmpty(map), "null 인 맵은 false 를 반환");
        map = new HashMap<>();
        assertTrue(TypeConvertUtil.isNullOrEmpty(map), "비어있는 맵은 false 를 반환");
        map.put("key", "value");
        assertFalse(TypeConvertUtil.isNullOrEmpty(map), "null/empty 가 아닌 맵은 true 를 반환");
    }

    @Test
    public void isNullOrEmptyTest_collection() {
        List<Object> objects = null;
        assertTrue(TypeConvertUtil.isNullOrEmpty(objects), "null 인 컬렉션은 false 를 반환");
        objects = new ArrayList<>();
        assertTrue(TypeConvertUtil.isNullOrEmpty(objects), "비어있는 컬렉션은 false 를 반환");
        objects.add("item");
        assertFalse(TypeConvertUtil.isNullOrEmpty(objects), "null/empty 가 아닌 컬렉션은 true 를 반환");
    }

    @Test
    public void getOrThrowTest() {
        Class<RuntimeException> expectedException = RuntimeException.class;

        assertThrows(expectedException, () -> {
            TypeConvertUtil.getOrThrow(this.testMap, "undefineKey", RuntimeException::new);
        }, "key 에 해당하는 value 가 존재하지 않는다면 지정된 exception 을 발생시킨다.");

        assertDoesNotThrow(() -> {
            for (Map.Entry<String, Object> entry : this.testMap.entrySet()) {
                assertNotNull(TypeConvertUtil.getOrThrow(this.testMap, entry.getKey(), RuntimeException::new));
            }
        }, "key 에 해당하는 value 가 존재한다면 반환, 예외가 발생할 수 없다.");

    }

}