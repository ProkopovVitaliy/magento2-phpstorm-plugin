/*
 * Copyright © Magento, Inc. All rights reserved.
 * See COPYING.txt for license details.
 */

package com.magento.idea.magento2plugin.actions.generation.generator;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.magento.idea.magento2plugin.actions.generation.data.UiComponentDataProviderData;
import com.magento.idea.magento2plugin.actions.generation.generator.util.DirectoryGenerator;
import com.magento.idea.magento2plugin.actions.generation.generator.util.FileFromTemplateGenerator;
import com.magento.idea.magento2plugin.bundles.CommonBundle;
import com.magento.idea.magento2plugin.bundles.ValidatorBundle;
import com.magento.idea.magento2plugin.indexes.ModuleIndex;
import com.magento.idea.magento2plugin.magento.files.UiComponentDataProviderPhp;
import com.magento.idea.magento2plugin.magento.packages.File;
import com.magento.idea.magento2plugin.magento.packages.Package;
import com.magento.idea.magento2plugin.util.GetFirstClassOfFile;
import com.magento.idea.magento2plugin.util.GetPhpClassByFQN;
import java.util.Properties;
import javax.swing.JOptionPane;

@SuppressWarnings({"PMD.OnlyOneReturn", "PMD.DataflowAnomalyAnalysis"})
public class UiComponentDataProviderGenerator extends FileGenerator {
    private final UiComponentDataProviderData uiComponentGridDataProviderData;
    private final Project project;
    private final DirectoryGenerator directoryGenerator;
    private final FileFromTemplateGenerator fileFromTemplateGenerator;
    private final ValidatorBundle validatorBundle;
    private final CommonBundle commonBundle;
    private final String moduleName;
    private final GetFirstClassOfFile getFirstClassOfFile;

    /**
     * Ui component grid data provider constructor.
     *
     * @param uiComponentGridDataProviderData UiComponentGridDataProviderData
     * @param moduleName String
     * @param project Project
     */
    public UiComponentDataProviderGenerator(
            final UiComponentDataProviderData uiComponentGridDataProviderData,
            final String moduleName,
            final Project project
    ) {
        super(project);

        this.uiComponentGridDataProviderData = uiComponentGridDataProviderData;
        this.directoryGenerator = DirectoryGenerator.getInstance();
        this.fileFromTemplateGenerator = new FileFromTemplateGenerator(project);
        this.validatorBundle = new ValidatorBundle();
        this.commonBundle = new CommonBundle();
        this.getFirstClassOfFile = GetFirstClassOfFile.getInstance();
        this.project = project;
        this.moduleName = moduleName;
    }

    @Override
    public PsiFile generate(final String actionName) {
        final PsiFile[] dataProviderFiles = new PsiFile[1];

        WriteCommandAction.runWriteCommandAction(project, () -> {
            PhpClass dataProvider = GetPhpClassByFQN.getInstance(project).execute(
                    getDataProviderFqn()
            );

            if (dataProvider != null) {
                final String errorMessage = this.validatorBundle.message(
                        "validator.file.alreadyExists",
                        "DataProvider Class"
                );
                JOptionPane.showMessageDialog(
                        null,
                        errorMessage,
                        commonBundle.message("common.error"),
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            dataProvider = createDataProviderClass(actionName);

            if (dataProvider == null) {
                final String errorMessage = this.validatorBundle.message(
                        "validator.file.cantBeCreated",
                        "DataProvider Class"
                );
                JOptionPane.showMessageDialog(
                        null,
                        errorMessage,
                        commonBundle.message("common.error"),
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            dataProviderFiles[0] = dataProvider.getContainingFile();
        });

        return dataProviderFiles[0];
    }

    @Override
    protected void fillAttributes(final Properties attributes) {
        attributes.setProperty("NAMESPACE", uiComponentGridDataProviderData.getNamespace());
        attributes.setProperty("CLASS_NAME", uiComponentGridDataProviderData.getName());
    }

    private PhpClass createDataProviderClass(final String actionName) {
        PsiDirectory parentDirectory = new ModuleIndex(project)
                .getModuleDirectoryByModuleName(this.moduleName);
        final PsiFile dataProviderFile;
        final String[] dataProviderDirectories = uiComponentGridDataProviderData.getPath().split(
                File.separator
        );
        for (final String dataProviderDirectory: dataProviderDirectories) {
            parentDirectory = directoryGenerator.findOrCreateSubdirectory(
                    parentDirectory, dataProviderDirectory
            );
        }

        final Properties attributes = getAttributes();

        dataProviderFile = fileFromTemplateGenerator.generate(
                UiComponentDataProviderPhp.getInstance(
                        uiComponentGridDataProviderData.getName()
                ),
                attributes,
                parentDirectory,
                actionName
        );

        if (dataProviderFile == null) {
            return null;
        }

        return getFirstClassOfFile.execute((PhpFile) dataProviderFile);
    }

    private String getDataProviderFqn() {
        return String.format(
                "%s%s%s",
                uiComponentGridDataProviderData.getNamespace(),
                Package.fqnSeparator,
                uiComponentGridDataProviderData.getName()
        );
    }
}
