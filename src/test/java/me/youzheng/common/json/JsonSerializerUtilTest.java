package me.youzheng.common.json;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JsonSerializerUtilTest {

    private static class Parent {

        private String parentName;
        private int parentAge;
        private Child child;

        public String getParentName() {
            return parentName;
        }

        public void setParentName(String parentName) {
            this.parentName = parentName;
        }

        public int getParentAge() {
            return parentAge;
        }

        public void setParentAge(int parentAge) {
            this.parentAge = parentAge;
        }

        public Child getChild() {
            return child;
        }

        public void setChild(Child child) {
            this.child = child;
        }
    }

    private static class Child {
        private String childName;
        private int childAge;

        public String getChildName() {
            return childName;
        }

        public void setChildName(String childName) {
            this.childName = childName;
        }

        public int getChildAge() {
            return childAge;
        }

        public void setChildAge(int childAge) {
            this.childAge = childAge;
        }
    }

    @Test
    public void 객체_Json_변환_테스트() {
        Parent parent = new Parent();
        parent.setParentAge(1);
        parent.setParentName("parent");

        String result = JsonSerializerUtil.writeToJsonString(parent);
        assertEquals("{\"parentName\":parent,\"parentAge\":1,\"child\":null}", result);
    }

    @Test
    public void null_일경우_null을_출력() {
        String result = JsonSerializerUtil.writeToJsonString(null);

        assertNotNull(result);
        assertEquals("null", result);
    }

    @Test
    public void object_타입도_변환한다() {
        Parent parent = new Parent();
        parent.setParentAge(1);
        parent.setParentName("parent");

        Child child = new Child();
        child.setChildAge(2);
        child.setChildName("child");
        parent.setChild(child);
        String result = JsonSerializerUtil.writeToJsonString(parent);
        assertEquals("{\"parentName\":parent,\"parentAge\":1,\"child\":{\"childName\":child,\"childAge\":2}}", result);
    }

    @Test
    public void 배열_직열화_테스트() {
        Child[] children = new Child[2];

        Child child1 = new Child();
        child1.setChildAge(1);
        child1.setChildName("child");
        children[0] = child1;
        children[1] = child1;

        String result = JsonSerializerUtil.writeToJsonString(children);
        assertEquals("[{\"childName\":child,\"childAge\":1},{\"childName\":child,\"childAge\":1}]", result);
    }

    @Test
    public void 비어있는_배열_직열화_테스트() {
        Child[] children = new Child[2];
        String result = JsonSerializerUtil.writeToJsonString(children);
        assertEquals("[]", result);
    }

    @Test
    public void 배열직열화() {
        String[] strings = {"string1", "string2"};
        String result = JsonSerializerUtil.writeToJsonString(strings);
        System.out.println("result = " + result);

        assertEquals("[\"string1\",\"string2\"]", result);
    }

}