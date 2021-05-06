/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 *
 */

package com.magento.idea.magento2plugin.actions.generation.data;

public interface CustomerEavAttributeTemplateData {
    String getInputName();
    String getInputTitle();
    boolean isRequired();
    String getModuleName();
}
