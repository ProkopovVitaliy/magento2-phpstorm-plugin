/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 *
 */

package com.magento.idea.magento2plugin.actions.generation.util.eav.customer;

import com.magento.idea.magento2plugin.magento.packages.eav.AttributeInput;
import java.util.ArrayList;
import java.util.List;

final public class GetAdvancedFormInputsUtil {

    private GetAdvancedFormInputsUtil(){}

    public static List<AttributeInput> execute() {
        final List<AttributeInput> inputsWithArguments = new ArrayList<>();
        inputsWithArguments.add(AttributeInput.MULTISELECT);
        inputsWithArguments.add(AttributeInput.SELECT);
        inputsWithArguments.add(AttributeInput.BOOLEAN);

        return inputsWithArguments;
    }
}
