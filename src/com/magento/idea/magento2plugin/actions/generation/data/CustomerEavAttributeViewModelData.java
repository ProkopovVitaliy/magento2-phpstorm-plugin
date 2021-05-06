/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 *
 */

package com.magento.idea.magento2plugin.actions.generation.data;

import org.jetbrains.annotations.NotNull;

public class CustomerEavAttributeViewModelData {
    private final String attributeCode;
    private final String className;
    private final String moduleName;

    public CustomerEavAttributeViewModelData(
            @NotNull final String attributeCode,
            @NotNull final String className,
            @NotNull final String moduleName
            ) {
        this.attributeCode = attributeCode;
        this.className = className;
        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getClassName() {
        return className;
    }

    public String getAttributeCode() {
        return attributeCode;
    }
}
