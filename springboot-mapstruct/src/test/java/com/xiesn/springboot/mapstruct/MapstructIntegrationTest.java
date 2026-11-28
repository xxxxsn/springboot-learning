package com.xiesn.springboot.mapstruct;

import com.jayway.jsonpath.JsonPath;
import com.xiesn.springboot.mapstruct.domain.Address;
import com.xiesn.springboot.mapstruct.domain.Order;
import com.xiesn.springboot.mapstruct.domain.OrderItem;
import com.xiesn.springboot.mapstruct.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 创建、详情、摘要、局部更新和金额换算。
 */
@SpringBootTest
@AutoConfigureMockMvc
class MapstructIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 总览同时给出详情、摘要，以及一次不落库的局部更新。
     */
    @Test
    void overview_shouldShowMappingAndIgnoredNulls() throws Exception {
        mockMvc.perform(get("/api/mapstruct/overview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.port").value(8030))
                .andExpect(jsonPath("$.data.mapper", containsString("OrderMapperImpl")))
                .andExpect(jsonPath("$.data.sampleId").value("9007199254740993"))
                .andExpect(jsonPath("$.data.sampleDetail.id").value("9007199254740993"))
                .andExpect(jsonPath("$.data.sampleDetail.amountYuan").value(39.80))
                .andExpect(jsonPath("$.data.sampleDetail.statusCode").value(20))
                .andExpect(jsonPath("$.data.sampleDetail.statusLabel").value("已支付"))
                .andExpect(jsonPath("$.data.sampleDetail.createdAt").value("2026-11-24T08:30:00"))
                .andExpect(jsonPath("$.data.sampleDetail.createdAtText").value("2026-11-24 08:30:00"))
                .andExpect(jsonPath("$.data.sampleDetail.remark").value(nullValue()))
                .andExpect(jsonPath("$.data.sampleDetail.address.fullAddress").value("浙江省杭州市文三路 1 号"))
                .andExpect(jsonPath("$.data.sampleDetail.itemCount").value(1))
                .andExpect(jsonPath("$.data.sampleDetail.items[0].priceYuan").value(19.90))
                .andExpect(jsonPath("$.data.sampleSummary.statusCode").value(20))
                .andExpect(jsonPath("$.data.sampleSummary.amountYuan").value(39.80))
                .andExpect(jsonPath("$.data.afterPatchPreview.buyerName").value("张三"))
                .andExpect(jsonPath("$.data.afterPatchPreview.statusCode").value(30))
                .andExpect(jsonPath("$.data.afterPatchPreview.statusLabel").value("已关闭"))
                .andExpect(jsonPath("$.data.afterPatchPreview.remark").value("只改备注"))
                .andExpect(jsonPath("$.data.afterPatchPreview.address.street").value("文三路 1 号"))
                .andExpect(jsonPath("$.data.afterPatchPreview.itemCount").value(1))
                .andExpect(jsonPath("$.data.afterPatchPreview.amountYuan").value(39.80));
    }

    /**
     * 总览里的预览不能改掉内存中的样例。
     */
    @Test
    void overview_shouldNotMutateStoredSample() throws Exception {
        mockMvc.perform(get("/api/mapstruct/overview")).andExpect(status().isOk());
        mockMvc.perform(get("/api/mapstruct/orders/sample"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.statusCode").value(20))
                .andExpect(jsonPath("$.data.remark").value(nullValue()))
                .andExpect(jsonPath("$.data.address.street").value("文三路 1 号"))
                .andExpect(jsonPath("$.data.itemCount").value(1));
    }

    /**
     * 摘要列表能找到样例，且主键是字符串。
     */
    @Test
    void summaries_shouldContainSample() throws Exception {
        mockMvc.perform(get("/api/mapstruct/orders/summaries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].id", hasItem("9007199254740993")))
                .andExpect(jsonPath("$.data[?(@.id=='9007199254740993')].amountYuan").value(hasItem(39.80)));
    }

    /**
     * 拷贝不能和原对象共用地址或明细列表。
     */
    @Test
    void copy_shouldNotShareNestedObjects() {
        Address address = new Address();
        address.setProvince("浙江省");
        address.setCity("杭州市");
        address.setStreet("文三路 1 号");
        OrderItem item = new OrderItem();
        item.setSkuCode("SKU");
        item.setSkuName("龙井");
        item.setQuantity(1);
        item.setPriceCent(100L);
        List<OrderItem> items = new ArrayList<>();
        items.add(item);
        Order source = new Order();
        source.setId(1L);
        source.setOrderNo("ORD-1");
        source.setBuyerName("张三");
        source.setAmountCent(100L);
        source.setAddress(address);
        source.setItems(items);

        Order copied = orderMapper.copy(source);
        copied.getAddress().setStreet("改了");
        copied.getItems().get(0).setSkuCode("OTHER");
        copied.getItems().add(new OrderItem());

        assertEquals("文三路 1 号", source.getAddress().getStreet());
        assertEquals("SKU", source.getItems().get(0).getSkuCode());
        assertEquals(1, source.getItems().size());
        assertEquals("张三", copied.getBuyerName());
    }

    /**
     * 创建：忽略客户端 id，嵌套和金额按明细汇总，主键以字符串返回。
     */
    @Test
    void create_shouldIgnoreClientIdAndConvertTypes() throws Exception {
        String body = mockMvc.perform(post("/api/mapstruct/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 1,
                                  "buyerName": "李四",
                                  "statusCode": 10,
                                  "remark": "加急",
                                  "address": {"province": "浙江省", "city": "宁波市", "street": "天一广场"},
                                  "items": [
                                    {"skuCode": "SKU-1", "skuName": "杯子", "quantity": 2, "priceYuan": 19.90},
                                    {"skuCode": "SKU-2", "skuName": "盖子", "quantity": 1, "priceYuan": 1.00}
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.id", matchesPattern("9007\\d+")))
                .andExpect(jsonPath("$.data.id", not("1")))
                .andExpect(jsonPath("$.data.buyerName").value("李四"))
                .andExpect(jsonPath("$.data.statusCode").value(10))
                .andExpect(jsonPath("$.data.statusLabel").value("待支付"))
                .andExpect(jsonPath("$.data.amountYuan").value(40.80))
                .andExpect(jsonPath("$.data.itemCount").value(2))
                .andExpect(jsonPath("$.data.items[0].priceYuan").value(19.90))
                .andExpect(jsonPath("$.data.address.fullAddress").value("浙江省宁波市天一广场"))
                .andExpect(jsonPath("$.data.createdAtText", matchesPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")))
                .andExpect(jsonPath("$.data.createdAt", containsString("T")))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        String id = JsonPath.read(body, "$.data.id");
        mockMvc.perform(get("/api/mapstruct/orders/sample"))
                .andExpect(jsonPath("$.data.id", not(id)));
    }

    /**
     * 不传明细时得到空列表和 0 元，而不是 null。
     */
    @Test
    void create_shouldTurnMissingItemsIntoEmptyList() throws Exception {
        mockMvc.perform(post("/api/mapstruct/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"buyerName": "无明细", "statusCode": 10}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.items").isEmpty())
                .andExpect(jsonPath("$.data.itemCount").value(0))
                .andExpect(jsonPath("$.data.amountYuan").value(0.00))
                .andExpect(jsonPath("$.data.address").value(nullValue()));
    }

    /**
     * 未知状态码在映射阶段失败。
     */
    @Test
    void create_shouldRejectUnknownStatus() throws Exception {
        mockMvc.perform(post("/api/mapstruct/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"buyerName": "状态", "statusCode": 99}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message", containsString("未知订单状态")));
    }

    /**
     * 创建必须带状态码。
     */
    @Test
    void create_shouldRejectMissingStatus() throws Exception {
        mockMvc.perform(post("/api/mapstruct/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"buyerName": "缺状态"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message", containsString("statusCode 不能为空")));
    }

    /**
     * 超过两位小数的金额不四舍五入。
     */
    @Test
    void create_shouldRejectTooManyFractionDigits() throws Exception {
        mockMvc.perform(post("/api/mapstruct/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "buyerName": "小数",
                                  "statusCode": 10,
                                  "items": [{"skuCode": "A", "skuName": "a", "quantity": 1, "priceYuan": 1.999}]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message", containsString("金额最多两位小数")));
    }

    /**
     * 明细缺单价时给出业务错误，而不是 500。
     */
    @Test
    void create_shouldRejectItemWithoutPrice() throws Exception {
        mockMvc.perform(post("/api/mapstruct/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "buyerName": "缺价格",
                                  "statusCode": 10,
                                  "items": [{"skuCode": "A", "skuName": "a", "quantity": 1}]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message", containsString("明细需要价格和数量")));
    }

    /**
     * 只改备注和状态时，买家、地址、明细、金额都保持原值。
     */
    @Test
    void patch_shouldIgnoreNullFields() throws Exception {
        String id = createOrder("""
                {
                  "buyerName": "李四",
                  "statusCode": 10,
                  "remark": "原备注",
                  "address": {"province": "浙江省", "city": "宁波市", "street": "天一广场"},
                  "items": [{"skuCode": "SKU-1", "skuName": "杯子", "quantity": 1, "priceYuan": 10.00}]
                }
                """);
        mockMvc.perform(patch("/api/mapstruct/orders/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"remark": "已改", "statusCode": 20}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.buyerName").value("李四"))
                .andExpect(jsonPath("$.data.remark").value("已改"))
                .andExpect(jsonPath("$.data.statusCode").value(20))
                .andExpect(jsonPath("$.data.statusLabel").value("已支付"))
                .andExpect(jsonPath("$.data.address.fullAddress").value("浙江省宁波市天一广场"))
                .andExpect(jsonPath("$.data.itemCount").value(1))
                .andExpect(jsonPath("$.data.amountYuan").value(10.00));
    }

    /**
     * 地址对象里没传的字段保留原值。
     */
    @Test
    void patch_shouldUpdateAddressByField() throws Exception {
        String id = createOrder("""
                {
                  "buyerName": "王五",
                  "statusCode": 10,
                  "address": {"province": "浙江省", "city": "杭州市", "street": "文三路 1 号"},
                  "items": [{"skuCode": "SKU-1", "skuName": "杯子", "quantity": 1, "priceYuan": 10.00}]
                }
                """);
        mockMvc.perform(patch("/api/mapstruct/orders/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"address": {"street": "西湖"}}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.address.province").value("浙江省"))
                .andExpect(jsonPath("$.data.address.city").value("杭州市"))
                .andExpect(jsonPath("$.data.address.street").value("西湖"))
                .andExpect(jsonPath("$.data.address.fullAddress").value("浙江省杭州市西湖"))
                .andExpect(jsonPath("$.data.buyerName").value("王五"));
    }

    /**
     * 原来没有地址时，补上一段新地址。
     */
    @Test
    void patch_shouldCreateAddressWhenMissing() throws Exception {
        String id = createOrder("""
                {"buyerName": "无地址", "statusCode": 10}
                """);
        mockMvc.perform(patch("/api/mapstruct/orders/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"address": {"province": "上海市", "city": "上海市", "street": "南京路"}}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.address.fullAddress").value("上海市上海市南京路"));
    }

    /**
     * 明细整表替换，并重算金额。
     */
    @Test
    void patch_shouldReplaceItemsAndRecomputeAmount() throws Exception {
        String id = createOrder("""
                {
                  "buyerName": "赵六",
                  "statusCode": 10,
                  "items": [
                    {"skuCode": "SKU-1", "skuName": "杯子", "quantity": 2, "priceYuan": 19.90}
                  ]
                }
                """);
        mockMvc.perform(patch("/api/mapstruct/orders/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"items": [{"skuCode": "SKU-9", "skuName": "壶", "quantity": 3, "priceYuan": 3.00}]}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.buyerName").value("赵六"))
                .andExpect(jsonPath("$.data.itemCount").value(1))
                .andExpect(jsonPath("$.data.items[0].skuCode").value("SKU-9"))
                .andExpect(jsonPath("$.data.items[0].priceYuan").value(3.00))
                .andExpect(jsonPath("$.data.amountYuan").value(9.00));
    }

    /**
     * 不存在的订单。
     */
    @Test
    void patch_shouldReturnNotFound() throws Exception {
        mockMvc.perform(patch("/api/mapstruct/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"remark": "不存在"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("订单不存在"));
    }

    /**
     * @param json 创建请求
     * @return 服务端主键字符串
     */
    private String createOrder(String json) throws Exception {
        String body = mockMvc.perform(post("/api/mapstruct/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        return JsonPath.read(body, "$.data.id");
    }
}
