/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 *
 */

package com.magento.idea.magento2plugin.actions.generation.generator;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.magento.idea.magento2plugin.actions.generation.data.CustomerAccountCreateLayoutData;
import com.magento.idea.magento2plugin.actions.generation.generator.util.*;
import com.magento.idea.magento2plugin.actions.generation.util.eav.customer.GetAdvancedFormInputsUtil;
import com.magento.idea.magento2plugin.bundles.CommonBundle;
import com.magento.idea.magento2plugin.bundles.ValidatorBundle;
import com.magento.idea.magento2plugin.magento.files.CustomerAccountCreateLayoutXmlFile;
import com.magento.idea.magento2plugin.magento.packages.Areas;
import com.magento.idea.magento2plugin.magento.packages.eav.AttributeInput;
import java.util.*;
import javax.swing.JOptionPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomerAccountCreateLayoutGenerator extends FileGenerator {

    private final CustomerAccountCreateLayoutData data;
    private final String moduleName;
    private final ValidatorBundle validatorBundle;
    private final CommonBundle commonBundle;

    private final List<XmlTag> newTagsQueue;
    private final Map<XmlTag, XmlTag> newTagsChildParentRelationMap;

    public CustomerAccountCreateLayoutGenerator(
            @NotNull final CustomerAccountCreateLayoutData data,
            @NotNull final Project project,
            @NotNull final String moduleName
    ) {
        super(project);

        this.data = data;
        this.moduleName = moduleName;
        this.commonBundle = new CommonBundle();
        this.validatorBundle = new ValidatorBundle();
        newTagsQueue = new LinkedList<>();
        newTagsChildParentRelationMap = new HashMap<>();
    }

    @Override
    public PsiFile generate(String actionName) {
        PsiFile layoutXmlFile = findLayoutFile(
                new FindOrCreateLayoutXml(project),
                actionName
        );

        final XmlTag rootTag = ((XmlFile) layoutXmlFile).getRootTag();

        if (rootTag == null) {
            showDeclarationCannotBeCreatedDialog();
            return null;
        }

        final PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
        final Document document = psiDocumentManager.getDocument(layoutXmlFile);

        if (document == null) {
            showDeclarationCannotBeCreatedDialog();
            return null;
        }
        
        //Find or create body tag
        XmlTag bodyTag = findBodyTag(rootTag.getSubTags());

        if (bodyTag == null) {
            bodyTag = createBodyTag(rootTag);

            newTagsQueue.add(bodyTag);
            newTagsChildParentRelationMap.put(bodyTag, rootTag);
        }

        //Find or create referenceContainer tag
        XmlTag referenceContainerTag = findReferenceContainerTag(bodyTag.getSubTags());

        if (referenceContainerTag == null) {
            referenceContainerTag = createReferenceContainerTag(rootTag);

            newTagsQueue.add(referenceContainerTag);
            newTagsChildParentRelationMap.put(referenceContainerTag, bodyTag);
        }

        //Find or create block tag
        XmlTag blockTag = findBlockTagByName(referenceContainerTag.getSubTags(), data.getBlockName());
        XmlTag argumentsTag = null;
        XmlTag argumentTag = null;

        if (blockTag == null) {
            final String selectedAttributeInput = data.getInput();
            final List<AttributeInput> advancedFormInputs = GetAdvancedFormInputsUtil.execute();

            if (advancedFormInputs.contains(AttributeInput.getAttributeInputByCode(selectedAttributeInput))) {
                blockTag = createSimpleBlockTag(referenceContainerTag);
                argumentsTag = createArgumentsTag(blockTag);
                argumentTag = createArgumentBlock(argumentsTag);
            } else {
                blockTag = createBlockWithArgumentsTag(rootTag);
            }

            newTagsQueue.add(blockTag);
            newTagsChildParentRelationMap.put(blockTag, referenceContainerTag);

            if (argumentsTag != null) {
                newTagsQueue.add(argumentsTag);
                newTagsChildParentRelationMap.put(argumentsTag, blockTag);

                newTagsQueue.add(argumentTag);
                newTagsChildParentRelationMap.put(argumentTag, argumentsTag);
            }
        }

        CommitXmlFileUtil.execute(
                (XmlFile) layoutXmlFile,
                newTagsQueue,
                newTagsChildParentRelationMap
        );

        return reformatFile(layoutXmlFile);
    }

    private PsiFile reformatFile(final PsiFile layoutXmlFile) {
        WriteCommandAction.runWriteCommandAction(project, () -> {
            CodeStyleManager.getInstance(project).reformat(layoutXmlFile);
        });

        return layoutXmlFile;
    }

    @NotNull
    private XmlTag createArgumentBlock(XmlTag argumentsTag) {
        XmlTag argumentTag;
        argumentTag = argumentsTag.createChildTag(
                CustomerAccountCreateLayoutXmlFile.XML_TAG_ARGUMENT,
                null,
                data.getViewModelClassPatch(),
                false
        );
        argumentTag.setAttribute(
                CustomerAccountCreateLayoutXmlFile.XML_ATTR_ARGUMENT_NAME,
                data.getViewModelName()
        );
        argumentTag.setAttribute(
                CustomerAccountCreateLayoutXmlFile.XML_ATTR_ARGUMENT_XSI_TYPE,
                CustomerAccountCreateLayoutXmlFile.XML_ATTR_VALUE_ARGUMENT_XSI_TYPE
        );

        return argumentTag;
    }

    private XmlTag createArgumentsTag(XmlTag blockTag) {
        XmlTag argumentsTag;
        argumentsTag = blockTag.createChildTag(
                CustomerAccountCreateLayoutXmlFile.XML_TAG_ARGUMENTS,
                null,
                "",
                false
        );
        return argumentsTag;
    }

    private XmlTag createBlockWithArgumentsTag(XmlTag rootTag) {
        XmlTag blockTag;
        blockTag = rootTag.createChildTag(
                CustomerAccountCreateLayoutXmlFile.XML_TAG_BLOCK,
                null,
                null,
                false
        );
        blockTag.setAttribute(CustomerAccountCreateLayoutXmlFile.XML_ATTR_BLOCK_NAME, data.getBlockName());
        blockTag.setAttribute(CustomerAccountCreateLayoutXmlFile.XML_ATTR_BLOCK_TEMPLATE, data.getTemplate());

        return blockTag;
    }

    private XmlTag createSimpleBlockTag(XmlTag referenceContainerTag) {
        XmlTag blockTag;
        blockTag = referenceContainerTag.createChildTag(
                CustomerAccountCreateLayoutXmlFile.XML_TAG_BLOCK,
                null,
                "",
                false
        );
        blockTag.setAttribute(CustomerAccountCreateLayoutXmlFile.XML_ATTR_BLOCK_NAME, data.getBlockName());
        blockTag.setAttribute(CustomerAccountCreateLayoutXmlFile.XML_ATTR_BLOCK_TEMPLATE, data.getTemplate());

        return blockTag;
    }

    @NotNull
    private XmlTag createReferenceContainerTag(XmlTag rootTag) {
        XmlTag referenceContainerTag;
        referenceContainerTag = rootTag.createChildTag(
                CustomerAccountCreateLayoutXmlFile.XML_TAG_REFERENCE_CONTAINER,
                null,
                "",
                false
        );
        referenceContainerTag.setAttribute(
                CustomerAccountCreateLayoutXmlFile.XML_ATTR_BLOCK_NAME,
                CustomerAccountCreateLayoutXmlFile.XML_ATTR_VALUE_REFERENCE_CONTAINER_NAME
        );

        return referenceContainerTag;
    }

    private XmlTag createBodyTag(XmlTag rootTag) {
        return rootTag.createChildTag(
                CustomerAccountCreateLayoutXmlFile.XML_TAG_BODY,
                null,
                "",
                false
        );
    }

    private XmlTag findBlockTagByName(XmlTag[] subTags , String name) {
        for (XmlTag subTag : subTags) {
            if (subTag.getName().equals(CustomerAccountCreateLayoutXmlFile.XML_TAG_BLOCK)
                    && subTag.getAttributeValue(CustomerAccountCreateLayoutXmlFile.XML_ATTR_BLOCK_NAME).equals(name)) {
                return subTag;
            }
        }

        return null;
    }

    private XmlTag findReferenceContainerTag(XmlTag[] subTags ) {
        for (XmlTag subTag : subTags) {
            final String subTagName = subTag.getName();
            final String subTagAttributeValue = subTag.getAttributeValue(
                    CustomerAccountCreateLayoutXmlFile.XML_ATTR_REFERENCE_CONTAINER_NAME
            );
            if (subTagAttributeValue == null) {
                continue;
            }

            if (subTagName.equals(CustomerAccountCreateLayoutXmlFile.XML_TAG_REFERENCE_CONTAINER)
                    && subTagAttributeValue.equals(CustomerAccountCreateLayoutXmlFile.XML_ATTR_VALUE_REFERENCE_CONTAINER_NAME)) {
                return subTag;
            }
        }

        return null;
    }

    private XmlTag findBodyTag(XmlTag[] subTags ) {
        for (XmlTag subTag : subTags) {
            if (subTag.getName().equals(CustomerAccountCreateLayoutXmlFile.XML_TAG_BODY)) {
                return subTag;
            }
        }

        return null;
    }

    @Nullable
    private PsiFile findLayoutFile(FindOrCreateLayoutXml findOrCreateLayoutXml, String actionName) {
        return findOrCreateLayoutXml.execute(
                actionName,
                CustomerAccountCreateLayoutXmlFile.ROUTE_ID,
                CustomerAccountCreateLayoutXmlFile.CONTROLLER_NAME,
                CustomerAccountCreateLayoutXmlFile.ACTION_NAME,
                moduleName,
                Areas.frontend.name()
        );
    }

    private void showDeclarationCannotBeCreatedDialog() {
        JOptionPane.showMessageDialog(
                null,
                validatorBundle.message(
                        "validator.file.cantBeCreated",
                        "Customer Create Layout XML file"
                ),
                commonBundle.message("common.error"),
                JOptionPane.ERROR_MESSAGE
        );
    }

    @Override
    protected void fillAttributes(Properties attributes) {
    }
}
