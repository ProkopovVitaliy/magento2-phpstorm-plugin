/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 *
 */

package com.magento.idea.magento2plugin.actions.generation.data;

import org.jetbrains.annotations.NotNull;

public class CustomerEavAttributeBaseTemplateData implements CustomerEavAttributeTemplateData {

    private final String name;
    private final String title;
    private final boolean required;
    private final String moduleName;

    public CustomerEavAttributeBaseTemplateData(
            @NotNull final String inputName,
            @NotNull final String inputTitle,
            @NotNull final boolean required,
            @NotNull final String moduleName
    ) {
        this.name = inputName;
        this.title = inputTitle;
        this.required = required;
        this.moduleName = moduleName;
    }

    @Override
    public String getInputName() {
        return name;
    }

    @Override
    public String getInputTitle() {
        return title;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }
}
