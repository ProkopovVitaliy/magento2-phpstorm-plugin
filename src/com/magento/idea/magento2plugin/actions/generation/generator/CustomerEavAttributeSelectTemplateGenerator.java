/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 *
 */

package com.magento.idea.magento2plugin.actions.generation.generator;

import com.intellij.openapi.project.Project;
import com.magento.idea.magento2plugin.actions.generation.data.CustomerEavAttributeSelectTemplateData;
import com.magento.idea.magento2plugin.actions.generation.data.CustomerEavAttributeTemplateData;
import com.magento.idea.magento2plugin.actions.generation.generator.util.PhpClassGeneratorUtil;
import com.magento.idea.magento2plugin.magento.files.ModuleFileInterface;
import java.util.Properties;
import org.jetbrains.annotations.NotNull;

public class CustomerEavAttributeSelectTemplateGenerator extends CustomerEavAttributeTemplateGenerator {

    public CustomerEavAttributeSelectTemplateGenerator(
            @NotNull CustomerEavAttributeTemplateData data,
            @NotNull ModuleFileInterface file,
            @NotNull Project project
    ) {
        super(data, file, project);
    }

    @Override
    protected void fillAttributes(Properties attributes) {
        super.fillAttributes(attributes);
        CustomerEavAttributeSelectTemplateData selectTemplateData =
                (CustomerEavAttributeSelectTemplateData) data;

        phpClassTypesBuilder
                .append(
                        "VIEW_MODEL_CLASS",
                        selectTemplateData.getViewModelClass()
                )
                .appendProperty(
                        "VIEW_MODEL_NAME",
                        selectTemplateData.getViewModelName()
                );

        if (selectTemplateData.isMultiselect()) {
            phpClassTypesBuilder.appendProperty(
                    "IS_MULTISELECT",
                    Boolean.toString(selectTemplateData.isMultiselect())
            );
        }

        phpClassTypesBuilder.mergeProperties(attributes);

        attributes.setProperty(
                "USES",
                PhpClassGeneratorUtil.formatUses(phpClassTypesBuilder.getUses())
        );
    }
}
