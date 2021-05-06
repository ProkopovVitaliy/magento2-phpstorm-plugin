/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 *
 */

package com.magento.idea.magento2plugin.actions.generation.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomerAccountCreateLayoutData {
    private final String blockName;
    private final String viewModelName;
    private final String moduleName;
    private final String viewModelClassPatch;
    private final String input;

    public CustomerAccountCreateLayoutData(
            @NotNull final String blockName,
            @NotNull final String input,
            @NotNull final String viewModelName,
            @Nullable final String viewModelClassPatch,
            @NotNull final String moduleName
    ) {
        this.blockName = blockName;
        this.input = input;
        this.viewModelName = viewModelName;
        this.viewModelClassPatch = viewModelClassPatch;
        this.moduleName = moduleName;
    }

    public String getBlockName() {
        return blockName;
    }

    public String getInput() {
        return input;
    }

    public String getViewModelName() {
        return viewModelName;
    }

    public String getViewModelClassPatch() {
        return viewModelClassPatch;
    }

    //TODO move from DTO
    public String getTemplate() {
        return moduleName + "::" + blockName + ".phtml";
    }
}
