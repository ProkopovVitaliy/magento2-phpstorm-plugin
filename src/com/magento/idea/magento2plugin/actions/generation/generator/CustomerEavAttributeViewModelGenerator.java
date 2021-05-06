/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 *
 */

package com.magento.idea.magento2plugin.actions.generation.generator;

import com.intellij.openapi.project.Project;
import com.magento.idea.magento2plugin.actions.generation.data.CustomerEavAttributeViewModelData;
import com.magento.idea.magento2plugin.actions.generation.generator.util.PhpClassGeneratorUtil;
import com.magento.idea.magento2plugin.actions.generation.generator.util.PhpClassTypesBuilder;
import com.magento.idea.magento2plugin.magento.files.AbstractPhpFile;
import com.magento.idea.magento2plugin.magento.files.CustomerEavAttributeViewModelFile;
import com.magento.idea.magento2plugin.magento.packages.eav.AttributeOptionsViewModelDependency;
import java.util.Properties;
import org.jetbrains.annotations.NotNull;

public class CustomerEavAttributeViewModelGenerator extends PhpFileGenerator {

    private final CustomerEavAttributeViewModelData data;

    public CustomerEavAttributeViewModelGenerator(
            final @NotNull CustomerEavAttributeViewModelData data,
            final Project project
    ) {
        this(data, project, true);
    }

    /**
     * Php file generator constructor.
     *
     * @param project                Project
     * @param checkFileAlreadyExists boolean
     */
    public CustomerEavAttributeViewModelGenerator(
            @NotNull final CustomerEavAttributeViewModelData data,
            @NotNull final Project project,
            final boolean checkFileAlreadyExists
    ) {
        super(project, checkFileAlreadyExists);
        this.data = data;
    }

    @Override
    protected void fillAttributes(Properties attributes) {
        final PhpClassTypesBuilder phpClassTypesBuilder = new PhpClassTypesBuilder();

        phpClassTypesBuilder
                .appendProperty(
                        "CLASS_NAME",
                        data.getClassName()
                )
                .appendProperty(
                        "ATTRIBUTE_CODE",
                        data.getAttributeCode()
                )
                .appendProperty(
                        "NAMESPACE",
                        getFile().getNamespace()
                )
                .append(
                        "CUSTOMER_MODEL",
                        AttributeOptionsViewModelDependency.CUSTOMER_MODEL.getClassPatch()
                )
                .append(
                        "ARGUMENT_INTERFACE",
                        AttributeOptionsViewModelDependency.ARGUMENT_INTERFACE.getClassPatch()
                )
                .append(
                        "ATTRIBUTE_REPOSITORY_INTERFACE",
                        AttributeOptionsViewModelDependency.ATTRIBUTE_REPOSITORY_INTERFACE.getClassPatch()
                )
                .append(
                        "ATTRIBUTE_OPTION",
                        AttributeOptionsViewModelDependency.ATTRIBUTE_OPTION.getClassPatch()
                )
                .append(
                        "NO_SUCH_ENTITY_EXCEPTION",
                        AttributeOptionsViewModelDependency.NO_SUCH_ENTITY_EXCEPTION.getClassPatch()
                )
                .mergeProperties(attributes);

        attributes.setProperty(
                "USES",
                PhpClassGeneratorUtil.formatUses(phpClassTypesBuilder.getUses())
        );
    }

    @Override
    protected AbstractPhpFile initFile() {
        return new CustomerEavAttributeViewModelFile(data.getModuleName(), data.getClassName());
    }
}
