/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 *
 */

package com.magento.idea.magento2plugin.actions.generation.generator.eav.customer;

import com.intellij.openapi.project.Project;
import com.magento.idea.magento2plugin.actions.generation.data.CustomerEavAttributeTemplateData;
import com.magento.idea.magento2plugin.actions.generation.data.CustomerEavAttributeBaseTemplateData;
import com.magento.idea.magento2plugin.actions.generation.generator.CustomerEavAttributeBaseTemplateGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.CustomerEavAttributeTemplateGenerator;
import com.magento.idea.magento2plugin.magento.files.CustomerEavAttributeDateTemplateFile;
import com.magento.idea.magento2plugin.magento.files.CustomerEavAttributeTextTemplateFile;
import com.magento.idea.magento2plugin.magento.files.ModuleFileInterface;
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

    public AttributeTemplateGeneratorFactory(
            @NotNull final AttributeInput input,
            @NotNull final String inputName,
            @NotNull final String inputTitle,
            @NotNull final boolean required,
            @NotNull final String templateName,
            @NotNull final Project project,
            @NotNull final String moduleName
    ) {
        this.input = input;
        this.inputName = inputName;
        this.inputTitle = inputTitle;
        this.required = required;
        this.templateName = templateName;
        this.project = project;
        this.moduleName = moduleName;
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
        }

        return null;
    }
}
