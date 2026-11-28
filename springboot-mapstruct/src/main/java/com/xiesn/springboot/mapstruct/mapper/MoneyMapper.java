package com.xiesn.springboot.mapstruct.mapper;

import com.xiesn.springboot.common.exception.BusinessException;
import com.xiesn.springboot.common.result.CommonErrorCode;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.math.BigDecimal;

/**
 * 金额换算。单独一个 Mapper，由 {@link OrderMapper} 通过 {@code uses} 组合进来。
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MoneyMapper {

    /**
     * 元转分。超过两位小数直接拒绝，而不是四舍五入把 1.999 收成 2.00。
     *
     * @param yuan 元
     * @return 分；入参为 null 时返回 null
     */
    @Named("yuanToCent")
    default Long yuanToCent(BigDecimal yuan) {
        if (yuan == null) {
            return null;
        }
        if (yuan.stripTrailingZeros().scale() > 2) {
            throw new BusinessException(CommonErrorCode.BAD_REQUEST, "金额最多两位小数");
        }
        return yuan.movePointRight(2).longValueExact();
    }

    /**
     * 分转元，固定两位小数。
     *
     * @param cent 分
     * @return 元；入参为 null 时返回 null
     */
    @Named("centToYuan")
    default BigDecimal centToYuan(Long cent) {
        if (cent == null) {
            return null;
        }
        return BigDecimal.valueOf(cent, 2);
    }
}
