/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 *
 */

package com.magento.idea.magento2plugin.magento.packages.eav;

public enum CustomerAttributeTemplateDependency {
    BLOCK_CLASS("Magento\\Framework\\View\\Element\\Template");

    private final String classPath;

    CustomerAttributeTemplateDependency(String classPath) {
        this.classPath = classPath;
    }

    public String getClassPath() {
        return this.classPath;
    }
}
