package me.youzheng.common.typeutil;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static me.youzheng.common.typeutil.ConvertType.*;

@UtilityClass
public class TypeConvertUtil {

    /**
     * 변환할 데이터를 Supplier 을 통해서 전달받고 파라미터로 넘어온 ConvertType 의 제네릭 타입에 맞춰 형변환
     * @param sourceSupplier
     * @param convertType
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T convertTo(final Supplier<?> sourceSupplier, final ConvertType<T> convertType) {
        if (sourceSupplier == null) {
            throw new NullPointerException("sourceSupplier 는 null 이 될 수 없습니다.");
        }
        if (convertType == null) {
            throw new NullPointerException("ConvertType 은 null 이 될 수 없습니다.");
        }
        return (T)sourceSupplier.get();
    }

    /**
     * 변환할 데이터를 Supplier 을 통해서 전달받고 파라미터로 넘어온 ConvertType 의 제네릭 타입에 맞춰 형변환
     * @param sourceSupplier
     * @param convertType
     * @param <T>
     * @return
     */
    public static <T> T convertTo(final Supplier<?> sourceSupplier, final Class<T> convertType) {
        return convertTo(sourceSupplier, new ConvertType<T>() {});
    }

    /**
     * 변환할 데이터를 Supplier 을 통해서 전달받고 Map<String, Object> 타입으로 변환
     * @param sourceSupplier
     * @return
     */
    public static Map<String, Object> convertToDefaultMap(final Supplier<?> sourceSupplier) {
        return convertTo(sourceSupplier, DEFAULT_MAP_TYPE);
    }

    /**
     * 변환할 데이터를 Supplier 을 통해서 전달받고 List<Map<String, Object>> 타입으로 변환
     * @param sourceSupplier
     * @return
     */
    public static List<Map<String, Object>> convertToDefaultMapList(final Supplier<?> sourceSupplier) {
        return convertTo(sourceSupplier, DEFAULT_MAP_LIST_TYPE);
    }

    /**
     * Map 의 null 과 empty 를 체크, map != null && map.isEmpty()
     * @param target 검사할 대상
     * @return
     */
    public static boolean isNullOrEmpty(final Map<?, ?> target) {
        return target != null && !target.isEmpty();
    }

    /**
     * Collection 의 null 과 empty 를 체크, collection != null && collection.isEmpty()
     * @param target 검사할 대상
     * @return
     */
    public static boolean isNullOrEmpty(final Collection<?> target) {
        return target != null && !target.isEmpty();
    }

    /**
     * Map 에서 Key 로 Value 값을 찾는다. 존재하지 않는다면 지정한 예외를 던진다.
     * @param target
     * @param k
     * @param exceptionSupplier
     * @param <K> Key 타입
     * @param <V> Value 타입
     * @param <X> 존재하지 않았을 경우 발생시킬 예외
     * @return Key 에 해당하는 Value
     * @throws X
     */
    public static <K, V, X extends Throwable> V getOrThrow(final Map<K, V> target, final K k, final Supplier<X> exceptionSupplier) throws X {
        if (isNullOrEmpty(target)) {
            throw exceptionSupplier.get();
        }
        return target.get(k);
    }

}