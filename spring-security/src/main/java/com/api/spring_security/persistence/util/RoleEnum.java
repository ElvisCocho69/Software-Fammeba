package com.api.spring_security.persistence.util;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public enum RoleEnum {

    ADMINISTRATOR(Arrays.asList(
        RolePermissionEnum.READ_ALL_PRODUCTS,
        RolePermissionEnum.READ_ONE_PRODUCT,
        RolePermissionEnum.CREATE_ONE_PRODUCT,
        RolePermissionEnum.UPDATE_ONE_PRODUCT,
        RolePermissionEnum.DISABLE_ONE_PRODUCT,

        RolePermissionEnum.READ_ALL_CATEGORIES,
        RolePermissionEnum.READ_ONE_CATEGORY,
        RolePermissionEnum.CREATE_ONE_CATEGORY,
        RolePermissionEnum.UPDATE_ONE_CATEGORY,
        RolePermissionEnum.DISABLE_ONE_CATEGORY,

        RolePermissionEnum.READ_MY_PROFILE
    )),

    RECEPTIONIST(Arrays.asList(
        RolePermissionEnum.READ_ALL_PRODUCTS,
        RolePermissionEnum.READ_ONE_PRODUCT,

        RolePermissionEnum.READ_ALL_CATEGORIES,
        RolePermissionEnum.READ_ONE_CATEGORY,

        RolePermissionEnum.READ_MY_PROFILE
    )),

    CONCIERGE(Arrays.asList(
        RolePermissionEnum.READ_MY_PROFILE
    )),

    HOUSEKEEPER(Arrays.asList(
        RolePermissionEnum.READ_MY_PROFILE
    )),
    
    CUSTOMER(Arrays.asList(
        RolePermissionEnum.READ_MY_PROFILE
    ));

    private List<RolePermissionEnum> permissions;
    
    RoleEnum(List<RolePermissionEnum> permissions) {
        this.permissions = permissions;
    }

}
