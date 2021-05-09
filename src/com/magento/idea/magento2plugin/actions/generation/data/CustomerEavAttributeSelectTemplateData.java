/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 *
 */

package com.magento.idea.magento2plugin.actions.generation.data;

import org.jetbrains.annotations.NotNull;

public class CustomerEavAttributeSelectTemplateData implements CustomerEavAttributeTemplateData {

    private final String name;
    private final String title;
    private final boolean required;
    private final String viewModelClass;
    private final String viewModelName;
    private final String moduleName;
    private final boolean multiselect;

    public CustomerEavAttributeSelectTemplateData(
            @NotNull final String inputName,
            @NotNull final String inputTitle,
            @NotNull final boolean required,
            @NotNull final String viewModelClass,
            @NotNull final String viewModelName,
            @NotNull final String moduleName,
            @NotNull final boolean multiselect
    ) {
        this.name = inputName;
        this.title = inputTitle;
        this.required = required;
        this.viewModelClass = viewModelClass;
        this.viewModelName = viewModelName;
        this.moduleName = moduleName;
        this.multiselect = multiselect;
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

    public String getViewModelClass() {
        return viewModelClass;
    }

    public String getViewModelName() {
        return viewModelName;
    }

    public boolean isMultiselect() {
        return multiselect;
    }
}
