/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 *
 */

package com.magento.idea.magento2plugin.actions.generation.generator;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.magento.idea.magento2plugin.actions.generation.data.CustomerEavAttributeTemplateData;
import com.magento.idea.magento2plugin.actions.generation.generator.util.DirectoryGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.util.FileFromTemplateGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.util.PhpClassGeneratorUtil;
import com.magento.idea.magento2plugin.actions.generation.generator.util.PhpClassTypesBuilder;
import com.magento.idea.magento2plugin.indexes.ModuleIndex;
import com.magento.idea.magento2plugin.magento.files.ModuleFileInterface;
import com.magento.idea.magento2plugin.magento.packages.Areas;
import com.magento.idea.magento2plugin.magento.packages.eav.CustomerAttributeTemplateDependency;
import com.magento.idea.magento2plugin.util.magento.FileBasedIndexUtil;
import java.util.Properties;
import org.jetbrains.annotations.NotNull;

public abstract class CustomerEavAttributeTemplateGenerator extends FileGenerator {

    protected final CustomerEavAttributeTemplateData data;
    protected final ModuleFileInterface file;
    protected final FileFromTemplateGenerator fileFromTemplateGenerator;
    protected final ModuleIndex moduleIndex;
    protected final PhpClassTypesBuilder phpClassTypesBuilder;

    public CustomerEavAttributeTemplateGenerator(
            @NotNull final CustomerEavAttributeTemplateData data,
            @NotNull ModuleFileInterface file,
            @NotNull final Project project
    ) {
        super(project);
        this.data = data;
        this.file = file;
        this.fileFromTemplateGenerator = new FileFromTemplateGenerator(project);
        this.moduleIndex = new ModuleIndex(project);
        this.phpClassTypesBuilder = new PhpClassTypesBuilder();

    }

    @Override
    public PsiFile generate(String actionName) {
        PsiFile templateFile = findTemplateFile(file, data.getModuleName());

        if (templateFile == null) {
            templateFile = createTemplateFile(file, actionName);
        }

        return reformatFile(templateFile);
    }

    @Override
    protected void fillAttributes(Properties attributes) {
        phpClassTypesBuilder
                .append(
                        "BLOCK_CLASS",
                        CustomerAttributeTemplateDependency.BLOCK_CLASS.getClassPath()
                )
                .appendProperty(
                        "INPUT_NAME",
                        data.getInputName()
                )
                .appendProperty(
                        "INPUT_LABEL",
                        data.getInputTitle()
                );

        if (data.isRequired()) {
            phpClassTypesBuilder.appendProperty(
                    "IS_REQUIRED",
                    Boolean.toString(data.isRequired())
            );
        }

        phpClassTypesBuilder.mergeProperties(attributes);

        attributes.setProperty(
                "USES",
                PhpClassGeneratorUtil.formatUses(phpClassTypesBuilder.getUses())
        );
    }

    private PsiFile findTemplateFile(final ModuleFileInterface moduleFile, final String moduleName) {
        return FileBasedIndexUtil.findModuleViewFile(
                moduleFile.getFileName(),
                Areas.frontend,
                moduleName,
                project,
                "templates"
        );
    }

    private PsiFile createTemplateFile(final ModuleFileInterface moduleFile, final String actionName) {
        final DirectoryGenerator directoryGenerator = DirectoryGenerator.getInstance();

        final PsiDirectory fileBaseDir = directoryGenerator.findOrCreateSubdirectories(
                moduleIndex.getModuleDirectoryByModuleName(data.getModuleName()),
                "view/frontend/templates"
        );

        return fileFromTemplateGenerator.generate(
                moduleFile,
                getAttributes(),
                fileBaseDir,
                actionName
        );
    }

    private PsiFile reformatFile(final PsiFile templateFile) {
        WriteCommandAction.runWriteCommandAction(project, () -> {
            CodeStyleManager.getInstance(project).reformat(templateFile);
        });

        return templateFile;
    }
}
