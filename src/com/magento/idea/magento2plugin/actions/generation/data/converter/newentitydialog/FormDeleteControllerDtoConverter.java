/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 */

package com.magento.idea.magento2plugin.actions.generation.data.converter.newentitydialog;

import com.magento.idea.magento2plugin.actions.generation.data.DeleteEntityControllerFileData;
import com.magento.idea.magento2plugin.actions.generation.data.converter.DataObjectConverter;
import com.magento.idea.magento2plugin.actions.generation.data.dialog.EntityManagerContextData;
import com.magento.idea.magento2plugin.actions.generation.data.dialog.NewEntityDialogData;
import org.jetbrains.annotations.NotNull;

public class FormDeleteControllerDtoConverter extends DeleteEntityControllerFileData
        implements DataObjectConverter {

    /**
     * Form delete controller converter.
     *
     * @param generationContextData EntityManagerContextData
     * @param newEntityDialogData NewEntityDialogData
     */
    public FormDeleteControllerDtoConverter(
            final @NotNull EntityManagerContextData generationContextData,
            final @NotNull NewEntityDialogData newEntityDialogData
    ) {
        super(
                newEntityDialogData.getEntityName(),
                generationContextData.getModuleName(),
                generationContextData.getDeleteControllerNamespaceBuilder().getNamespace(),
                generationContextData.getDeleteEntityByIdCommandNamespaceBuilder().getClassFqn(),
                newEntityDialogData.getAclId(),
                newEntityDialogData.getIdFieldName()
        );
    }
}
