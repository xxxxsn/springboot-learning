package com.xiesn.springboot.java21.features.model;

/**
 * 坐标点 record。
 * <p>record 自动生成构造、getter、equals/hashCode/toString，适合不可变 DTO。</p>
 *
 * @param x 横坐标
 * @param y 纵坐标
 */
public record Point(int x, int y) {

    /**
     * 计算到原点的曼哈顿距离。
     *
     * @return |x| + |y|
     */
    public int manhattanDistance() {
        return Math.abs(x) + Math.abs(y);
    }
}
