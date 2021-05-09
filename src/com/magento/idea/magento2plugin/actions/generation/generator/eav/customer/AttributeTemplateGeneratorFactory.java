/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 *
 */

package com.magento.idea.magento2plugin.actions.generation.generator.eav.customer;

import com.intellij.openapi.project.Project;
import com.magento.idea.magento2plugin.actions.generation.data.CustomerEavAttributeSelectTemplateData;
import com.magento.idea.magento2plugin.actions.generation.data.CustomerEavAttributeTemplateData;
import com.magento.idea.magento2plugin.actions.generation.data.CustomerEavAttributeBaseTemplateData;
import com.magento.idea.magento2plugin.actions.generation.generator.CustomerEavAttributeBaseTemplateGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.CustomerEavAttributeSelectTemplateGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.CustomerEavAttributeTemplateGenerator;
import com.magento.idea.magento2plugin.magento.files.*;
import com.magento.idea.magento2plugin.magento.packages.eav.AttributeInput;
import org.jetbrains.annotations.NotNull;

public class AttributeTemplateGeneratorFactory {

    private final AttributeInput input;
    private final String inputName;
    private final String inputTitle;
    private final boolean required;
    private final String templateName;
    private final Project project;
    private final String moduleName;
    private String viewModelName;
    private String viewModelClass;
    private boolean multiselect;

    public AttributeTemplateGeneratorFactory(
            @NotNull final AttributeInput input,
            @NotNull final String inputName,
            @NotNull final String inputTitle,
            @NotNull final boolean required,
            @NotNull final String templateName,
            @NotNull final String moduleName,
            @NotNull final Project project
    ) {
        this.input = input;
        this.inputName = inputName;
        this.inputTitle = inputTitle;
        this.required = required;
        this.templateName = templateName;
        this.moduleName = moduleName;
        this.project = project;
    }

    public AttributeTemplateGeneratorFactory(
            @NotNull final AttributeInput input,
            @NotNull final String inputName,
            @NotNull final String inputTitle,
            @NotNull final boolean required,
            @NotNull final String templateName,
            @NotNull final String viewModelName,
            @NotNull final String viewModelClass,
            @NotNull final boolean multiselect,
            @NotNull final String moduleName,
            @NotNull final Project project
            ) {
        this(
                input,
                inputName,
                inputTitle,
                required,
                templateName,
                moduleName, project
        );

        this.viewModelName = viewModelName;
        this.viewModelClass = viewModelClass;
        this.multiselect = multiselect;
    }

    public CustomerEavAttributeTemplateGenerator create() {
        CustomerEavAttributeTemplateData customerEavAttributeTemplateData = null;
        ModuleFileInterface moduleFile = null;

        switch (this.input) {
            case TEXT:
                customerEavAttributeTemplateData = new CustomerEavAttributeBaseTemplateData(
                        inputName,
                        inputTitle,
                        required,
                        moduleName
                );
                moduleFile = new CustomerEavAttributeTextTemplateFile(templateName);

                return new CustomerEavAttributeBaseTemplateGenerator(
                        customerEavAttributeTemplateData,
                        moduleFile,
                        project
                );
            case DATE:
                customerEavAttributeTemplateData = new CustomerEavAttributeBaseTemplateData(
                        inputName,
                        inputTitle,
                        required,
                        moduleName
                );
                moduleFile = new CustomerEavAttributeDateTemplateFile(templateName);

                return new CustomerEavAttributeBaseTemplateGenerator(
                        customerEavAttributeTemplateData,
                        moduleFile,
                        project
                );
            case TEXTAREA:
                customerEavAttributeTemplateData = new CustomerEavAttributeBaseTemplateData(
                        inputName,
                        inputTitle,
                        required,
                        moduleName
                );
                moduleFile = new CustomerEavAttributeTextareaTemplateFile(templateName);

                return new CustomerEavAttributeBaseTemplateGenerator(
                        customerEavAttributeTemplateData,
                        moduleFile,
                        project
                );
            case SELECT:
            case BOOLEAN:
            case MULTISELECT:
                customerEavAttributeTemplateData = new CustomerEavAttributeSelectTemplateData(
                        inputName,
                        inputTitle,
                        required,
                        viewModelClass,
                        viewModelName,
                        moduleName,
                        multiselect
                );
                moduleFile = new CustomerEavAttributeSelectTemplateFile(templateName);
                return new CustomerEavAttributeSelectTemplateGenerator(
                        customerEavAttributeTemplateData,
                        moduleFile,
                        project
                );

        }

        return null;
    }
}
