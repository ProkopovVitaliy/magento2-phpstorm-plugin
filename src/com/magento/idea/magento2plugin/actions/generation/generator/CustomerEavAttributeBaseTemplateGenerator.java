/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 *
 */

package com.magento.idea.magento2plugin.actions.generation.generator;

import com.intellij.openapi.project.Project;
import com.magento.idea.magento2plugin.actions.generation.data.CustomerEavAttributeTemplateData;
import com.magento.idea.magento2plugin.magento.files.ModuleFileInterface;
import org.jetbrains.annotations.NotNull;

public class CustomerEavAttributeBaseTemplateGenerator extends CustomerEavAttributeTemplateGenerator {

    public CustomerEavAttributeBaseTemplateGenerator(
            @NotNull CustomerEavAttributeTemplateData data,
            @NotNull ModuleFileInterface file,
            @NotNull Project project
    ) {
        super(data, file, project);
    }
}
