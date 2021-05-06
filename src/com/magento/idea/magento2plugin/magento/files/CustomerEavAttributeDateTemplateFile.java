/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 *
 */

package com.magento.idea.magento2plugin.magento.files;

import com.intellij.lang.Language;
import com.intellij.lang.xml.XMLLanguage;
import org.jetbrains.annotations.NotNull;

public class CustomerEavAttributeDateTemplateFile implements ModuleFileInterface {

    public static final String TEMPLATE = "Magento Customer Eav Attribute Date Templates PHTML";
    public static final String DIRECTORY = "view/frontend/templates";
    public static final String FILE_EXTENSION = "phtml";

    private final String fileName;

    public CustomerEavAttributeDateTemplateFile(
            @NotNull final String templateFileName
    ) {
        this.fileName = templateFileName;
    }

    @Override
    public String getFileName() {
        return fileName.concat(".").concat(FILE_EXTENSION);
    }

    @Override
    public String getTemplate() {
        return TEMPLATE;
    }

    @Override
    public Language getLanguage() {
        return XMLLanguage.INSTANCE;
    }

    public String getDirectory() {
        return DIRECTORY;
    }
}
