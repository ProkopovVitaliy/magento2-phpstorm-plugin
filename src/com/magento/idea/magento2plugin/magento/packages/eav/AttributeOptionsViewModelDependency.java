/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 *
 */

package com.magento.idea.magento2plugin.magento.packages.eav;

import org.jetbrains.annotations.NotNull;

public enum AttributeOptionsViewModelDependency {
    CUSTOMER_MODEL("Magento\\Customer\\Model\\Customer"),
    ATTRIBUTE_REPOSITORY_INTERFACE("Magento\\Eav\\Api\\AttributeRepositoryInterface"),
    ATTRIBUTE_OPTION("Magento\\Eav\\Model\\Entity\\Attribute\\Option"),
    NO_SUCH_ENTITY_EXCEPTION("Magento\\Framework\\Exception\\NoSuchEntityException"),
    ARGUMENT_INTERFACE("Magento\\Framework\\View\\Element\\Block\\ArgumentInterface");

    private final String classPatch;

    AttributeOptionsViewModelDependency(
            @NotNull final String classPatch
    ) {
        this.classPatch = classPatch;
    }

    public String getClassPatch() {
        return classPatch;
    }
}
