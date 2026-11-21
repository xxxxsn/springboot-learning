package com.xiesn.springboot.java21.features.service;

import com.xiesn.springboot.java21.features.model.Point;
import com.xiesn.springboot.java21.features.model.Shape;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SequencedCollection;

/**
 * Java 21 特性演示服务。
 * <p>把语言特性收敛到可调用方法，方便 HTTP 接口与单测复用。</p>
 */
@Service
public class Java21DemoService {

    /**
     * 演示 record：构造不可变坐标并计算距离。
     *
     * @param x 横坐标
     * @param y 纵坐标
     * @return 结果说明
     */
    public Map<String, Object> demoRecord(int x, int y) {
        Point point = new Point(x, y);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("point", point.toString());
        result.put("x", point.x());
        result.put("y", point.y());
        result.put("manhattanDistance", point.manhattanDistance());
        return result;
    }

    /**
     * 演示密封类 + 模式匹配 switch：按形状计算面积。
     *
     * @param type   形状类型：circle / rectangle / square
     * @param values 尺寸参数
     * @return 面积与匹配分支说明
     */
    public Map<String, Object> demoSealedAndPattern(String type, double... values) {
        Shape shape = switch (type == null ? "" : type.toLowerCase()) {
            case "circle" -> new Shape.Circle(values.length > 0 ? values[0] : 1);
            case "rectangle" -> new Shape.Rectangle(
                    values.length > 0 ? values[0] : 2,
                    values.length > 1 ? values[1] : 3);
            case "square" -> new Shape.Square(values.length > 0 ? values[0] : 4);
            default -> new Shape.Circle(1);
        };

        double area = switch (shape) {
            case Shape.Circle(double radius) -> Math.PI * radius * radius;
            case Shape.Rectangle(double width, double height) -> width * height;
            case Shape.Square(double side) -> side * side;
        };

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("shape", shape.toString());
        result.put("area", area);
        result.put("matchedBy", "pattern-matching switch + sealed permits");
        return result;
    }

    /**
     * 演示文本块：多行 SQL/JSON 字面量不再靠字符串拼接。
     *
     * @param table 表名占位
     * @return 文本块内容
     */
    public Map<String, Object> demoTextBlock(String table) {
        String safeTable = table == null || table.isBlank() ? "t_user" : table;
        String sql = """
                SELECT id, name, create_time
                FROM %s
                WHERE deleted = 0
                ORDER BY id DESC
                """.formatted(safeTable);

        String json = """
                {
                  "module": "springboot-java21-features",
                  "feature": "text-block"
                }
                """;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("sql", sql.strip());
        result.put("json", json.strip());
        return result;
    }

    /**
     * 演示 SequencedCollection：有序集合可直接取首尾元素。
     *
     * @return 首尾元素与列表快照
     */
    public Map<String, Object> demoSequencedCollection() {
        SequencedCollection<String> names = new ArrayList<>();
        names.add("alpha");
        names.add("beta");
        names.add("gamma");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("first", names.getFirst());
        result.put("last", names.getLast());
        result.put("reversed", List.copyOf(names.reversed()));
        result.put("original", List.copyOf(names));
        return result;
    }

    /**
     * 汇总一次「特性套餐」演示结果。
     *
     * @return 各特性样例
     */
    public Map<String, Object> demoAll() {
        Map<String, Object> all = new LinkedHashMap<>();
        all.put("record", demoRecord(3, 4));
        all.put("sealedAndPattern", demoSealedAndPattern("rectangle", 2, 5));
        all.put("textBlock", demoTextBlock("t_order"));
        all.put("sequencedCollection", demoSequencedCollection());
        return all;
    }
}
