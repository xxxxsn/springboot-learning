package com.xiesn.springboot.java21.features.model;

/**
 * 密封形状类型。
 * <p>只允许列出的子类型继承，配合模式匹配 switch 可做穷尽检查。</p>
 */
public sealed interface Shape permits Shape.Circle, Shape.Rectangle, Shape.Square {

    /**
     * 圆形。
     *
     * @param radius 半径
     */
    record Circle(double radius) implements Shape {
    }

    /**
     * 矩形。
     *
     * @param width  宽
     * @param height 高
     */
    record Rectangle(double width, double height) implements Shape {
    }

    /**
     * 正方形。
     *
     * @param side 边长
     */
    record Square(double side) implements Shape {
    }
}
