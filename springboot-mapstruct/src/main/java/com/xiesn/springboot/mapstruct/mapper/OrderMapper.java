package com.xiesn.springboot.mapstruct.mapper;

import com.xiesn.springboot.common.exception.BusinessException;
import com.xiesn.springboot.common.result.CommonErrorCode;
import com.xiesn.springboot.mapstruct.domain.Address;
import com.xiesn.springboot.mapstruct.domain.Order;
import com.xiesn.springboot.mapstruct.domain.OrderItem;
import com.xiesn.springboot.mapstruct.domain.OrderStatus;
import com.xiesn.springboot.mapstruct.dto.AddressRequest;
import com.xiesn.springboot.mapstruct.dto.OrderCreateRequest;
import com.xiesn.springboot.mapstruct.dto.OrderItemRequest;
import com.xiesn.springboot.mapstruct.dto.OrderPatchRequest;
import com.xiesn.springboot.mapstruct.vo.AddressVo;
import com.xiesn.springboot.mapstruct.vo.OrderDetailVo;
import com.xiesn.springboot.mapstruct.vo.OrderItemVo;
import com.xiesn.springboot.mapstruct.vo.OrderSummaryVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单 DTO / VO / Entity 映射。
 * <p>目标侧用 {@link ReportingPolicy#ERROR}：漏映射一个字段就编译失败。
 * 源侧用 IGNORE：摘要 VO 本来就比实体少字段。</p>
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = MoneyMapper.class,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface OrderMapper {

    /**
     * 创建请求转实体。客户端 id、单号、创建时间都不采信；金额等明细映射完再汇总。
     *
     * @param request 创建请求
     * @return 尚未赋主键的实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderNo", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "amountCent", ignore = true)
    @Mapping(target = "status", source = "statusCode", qualifiedByName = "toStatus")
    Order toEntity(OrderCreateRequest request);

    /**
     * 把请求里的非 null 字段写进已有订单。null 表示不修改。
     * <p>{@code items} 不在这里映射，见 {@link #refillAmount}。</p>
     *
     * @param request 局部更新
     * @param order   已存在的实体
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderNo", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "amountCent", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "status", source = "statusCode", qualifiedByName = "toStatus")
    void update(OrderPatchRequest request, @MappingTarget Order order);

    /**
     * 深拷贝，给总览里的局部更新预览用，避免改到内存里的原单。
     * <p>同类型 {@code List} 默认只复制列表外壳，元素仍是原引用，所以明细走 {@code copyItems}。</p>
     *
     * @param source 原单
     * @return 新实例
     */
    @Mapping(target = "items", source = "items", qualifiedByName = "copyItems")
    Order copy(Order source);

    /**
     * @param source 原地址
     * @return 新地址
     */
    Address copy(Address source);

    /**
     * @param source 原明细
     * @return 新明细
     */
    OrderItem copy(OrderItem source);

    /**
     * 逐条复制明细。MapStruct 对同类型列表会 {@code new ArrayList<>(source)}，元素对象仍然共用。
     *
     * @param items 原明细
     * @return 新列表；源为 null 时返回 null
     */
    @Named("copyItems")
    default List<OrderItem> copyItems(List<OrderItem> items) {
        if (items == null) {
            return null;
        }
        List<OrderItem> copied = new ArrayList<>(items.size());
        for (OrderItem item : items) {
            copied.add(copy(item));
        }
        return copied;
    }

    /**
     * 实体转详情。
     *
     * @param order 实体
     * @return 详情
     */
    @Mapping(target = "amountYuan", source = "amountCent", qualifiedByName = "centToYuan")
    @Mapping(target = "statusCode", source = "status.code")
    @Mapping(target = "statusLabel", source = "status.label")
    @Mapping(target = "createdAtText", source = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "itemCount", source = "items", qualifiedByName = "itemCount")
    OrderDetailVo toDetail(Order order);

    /**
     * 实体转摘要。
     *
     * @param order 实体
     * @return 摘要
     */
    @Mapping(target = "amountYuan", source = "amountCent", qualifiedByName = "centToYuan")
    @Mapping(target = "statusCode", source = "status.code")
    OrderSummaryVo toSummary(Order order);

    /**
     * 列表映射。源列表为 null 时返回空列表，避免接口出现 {@code "data": null}。
     *
     * @param orders 实体列表
     * @return 摘要列表
     */
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<OrderSummaryVo> toSummaries(List<Order> orders);

    /**
     * 创建时把没传的明细收成空列表。更新方法不走这里，免得 null 被收成空列表后清掉旧明细。
     *
     * @param items 请求明细
     * @return 实体明细
     */
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<OrderItem> toItems(List<OrderItemRequest> items);

    /**
     * 详情里的明细同样不返回 null。
     *
     * @param items 实体明细
     * @return 出参明细
     */
    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<OrderItemVo> toItemVos(List<OrderItem> items);

    /**
     * @param request 明细入参
     * @return 明细实体
     */
    @Mapping(target = "priceCent", source = "priceYuan", qualifiedByName = "yuanToCent")
    OrderItem toItem(OrderItemRequest request);

    /**
     * @param item 明细实体
     * @return 明细出参
     */
    @Mapping(target = "priceYuan", source = "priceCent", qualifiedByName = "centToYuan")
    OrderItemVo toItemVo(OrderItem item);

    /**
     * @param request 地址入参
     * @return 新地址
     */
    Address toAddress(AddressRequest request);

    /**
     * 地址字段级更新。目标地址已存在时，MapStruct 会选这个方法，而不是整段换成新对象。
     *
     * @param request 地址入参
     * @param target  已有地址
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAddress(AddressRequest request, @MappingTarget Address target);

    /**
     * @param address 地址实体
     * @return 地址出参
     */
    @Mapping(target = "fullAddress", source = ".", qualifiedByName = "fullAddress")
    AddressVo toAddressVo(Address address);

    /**
     * 数字码转枚举。更新请求里的 null 由 IGNORE 挡住，不会进这个方法。
     *
     * @param code 状态码
     * @return 枚举
     */
    @Named("toStatus")
    default OrderStatus toStatus(Integer code) {
        return OrderStatus.fromCode(code);
    }

    /**
     * @param items 明细
     * @return 条数；null 当 0
     */
    @Named("itemCount")
    default int itemCount(List<OrderItem> items) {
        return items == null ? 0 : items.size();
    }

    /**
     * 拼接完整地址。空字段当空串，避免出现「null杭州市」。
     *
     * @param address 地址
     * @return 拼接结果；地址本身为 null 时返回 null
     */
    @Named("fullAddress")
    default String fullAddress(Address address) {
        if (address == null) {
            return null;
        }
        return text(address.getProvince()) + text(address.getCity()) + text(address.getStreet());
    }

    /**
     * 明细映射完成后按单价乘数量写回订单金额。
     *
     * @param request 创建请求（用来把本方法限定在创建映射上）
     * @param order   刚映射出的实体
     */
    @AfterMapping
    default void fillAmount(OrderCreateRequest request, @MappingTarget Order order) {
        if (request == null) {
            return;
        }
        order.setAmountCent(sumItems(order.getItems()));
    }

    /**
     * 只有请求真的带了明细时才整表替换并重算金额。
     * <p>不能交给 update 的集合映射：它对已有 List 是 {@code clear()} 再 {@code addAll()}，
     * 源为 null 时仍会执行，而 {@code toItems} 又把 null 收成空列表。</p>
     *
     * @param request 局部更新
     * @param order   被更新的实体
     */
    @AfterMapping
    default void refillAmount(OrderPatchRequest request, @MappingTarget Order order) {
        if (request.items() != null) {
            order.setItems(toItems(request.items()));
            order.setAmountCent(sumItems(order.getItems()));
        }
    }

    /**
     * @param items 已经换成「分」的明细
     * @return 总金额（分）
     */
    private long sumItems(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            return 0L;
        }
        long total = 0L;
        for (OrderItem item : items) {
            if (item.getPriceCent() == null || item.getQuantity() == null) {
                throw new BusinessException(CommonErrorCode.BAD_REQUEST, "明细需要价格和数量");
            }
            total = Math.addExact(total, Math.multiplyExact(item.getPriceCent(), item.getQuantity().longValue()));
        }
        return total;
    }

    /**
     * @param value 可能为 null 的片段
     * @return 非 null 文本
     */
    private String text(String value) {
        return value == null ? "" : value;
    }
}
