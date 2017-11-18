package org.orh.pattern.ch06;

public class Clone {

    public static void main(String[] args) throws CloneNotSupportedException {
        Person p1 = new Person(25, "Wang");
        Person p2 = p1.clone();

        System.out.println(p1.getName() == p2.getName() ? "clone是浅拷贝的" : "clone是深拷贝的");
    }

    static class Person implements Cloneable {
        public Person(int age, String name) {
            this.age = age;
            this.name = name;
        }

        private int age;
        private String name;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        protected Person clone() throws CloneNotSupportedException {
            return (Person) super.clone();
        }
    }
}
