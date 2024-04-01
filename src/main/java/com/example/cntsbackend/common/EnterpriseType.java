package com.example.cntsbackend.common;

import lombok.Getter;

import java.lang.reflect.InvocationTargetException;

@Getter
public enum EnterpriseType {

    POWER_GENERATION_ENTERPRISE(0, "发电企业"),
    POWER_GRID_ENTERPRISE(1, "电网企业"),
    STEEL_PRODUCTION_ENTERPRISE(2, "钢铁生产企业"),
    CHEMICAL_PRODUCTION_ENTERPRISE(3, "化工生产企业"),
    ALUMINUM_ELECTROLYSIS_PRODUCTION_ENTERPRISE(4, "电解铝生产企业企业"),
    MAGNESIUM_SMELTING_ENTERPRISE(5, "镁冶炼企业"),
    FLAT_GLASS_PRODUCTION_ENTERPRISE(6, "平板玻璃生产企业"),
    CEMENT_PRODUCTION_ENTERPRISE(7, "水泥生产企业"),
    CERAMIC_PRODUCTION_ENTERPRISE(8, "陶瓷生产企业"),
    CIVIL_AVIATION_ENTERPRISE(9, "民航企业"),
    OTHER_ENTERPRISE(10, "其它企业"),
    ;


    private final int code;
    private final String desc;

    private static final String fieldName = "enterprise_type";

    EnterpriseType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    /**
     * 通过DTO对象设置企业类型
     *
     * @param dto   DTO对象
     * @param clazz DTO对象的类
     * @param type  企业类型
     */
    public static void setDto(Object dto, Class<?> clazz, int type){
        // 获取DTO对象的类信息
        Class<?> dtoClass = dto.getClass();
        // 拼接setter方法名称
        String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        // 获取setter方法
        // 这里setter方法都是public的，如果不是，可以使用 getDeclaredMethod() 方法
        // 并在调用 invoke() 之前通过 setAccessible(true) 设置为可访问
        try {
            java.lang.reflect.Method setter = dtoClass.getMethod(setterName, String.class);
            for (EnterpriseType value : EnterpriseType.values()) {
                 if (value.code == type) {
                     setter.invoke(dto, value.desc);
                     return;
                 }
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

}
