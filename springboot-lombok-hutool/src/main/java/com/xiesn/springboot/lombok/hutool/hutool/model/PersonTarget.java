package com.xiesn.springboot.lombok.hutool.hutool.model;

/**
 * Bean 拷贝目标对象。
 */
public class PersonTarget {

    private String name;
    private Integer age;
    private String city;

    /**
     * @return 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return 年龄
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @param age 年龄
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * @return 城市
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city 城市
     */
    public void setCity(String city) {
        this.city = city;
    }
}
