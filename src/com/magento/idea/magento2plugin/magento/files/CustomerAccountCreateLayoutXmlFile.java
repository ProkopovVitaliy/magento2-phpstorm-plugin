/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 *
 */

package com.magento.idea.magento2plugin.magento.files;

import com.intellij.lang.Language;
import com.intellij.lang.xml.XMLLanguage;

public class CustomerAccountCreateLayoutXmlFile implements ModuleFileInterface {
    public static final String FILE_NAME = "customer_account_create.xml";
    public static final String ROUTE_ID = "customer";
    public static final String CONTROLLER_NAME = "account";
    public static final String ACTION_NAME = "create";
    public static final String TEMPLATE = "Magento Customer Account Create From XML";
    public static final String DIRECTORY = "view/frontend/layout";


    //attributes
    public static final String XML_ATTR_BLOCK_NAME = "name";
    public static final String XML_ATTR_BLOCK_TEMPLATE = "template";
    public static final String XML_ATTR_REFERENCE_CONTAINER_NAME = "name";
    public static final String XML_ATTR_ARGUMENT_NAME = "name";
    public static final String XML_ATTR_ARGUMENT_XSI_TYPE = "xsi:type";

    //tags
    public static final String XML_TAG_BODY = "body";
    public static final String XML_TAG_REFERENCE_CONTAINER = "referenceContainer";
    public static final String XML_TAG_BLOCK = "block";
    public static final String XML_TAG_ARGUMENTS = "arguments";
    public static final String XML_TAG_ARGUMENT = "argument";

    //attributes values
    public static final String XML_ATTR_VALUE_ARGUMENT_XSI_TYPE = "object";
    public static final String XML_ATTR_VALUE_REFERENCE_CONTAINER_NAME = "form.additional.info";

    @Override
    public String getFileName() {
        return FILE_NAME;
    }

    @Override
    public String getTemplate() {
        return TEMPLATE;
    }

    @Override
    public Language getLanguage() {
        return XMLLanguage.INSTANCE;
    }
}
