package com.jetbrains.clion.bugeater.debugger;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.ui.IconManager;
import com.jetbrains.cidr.cpp.execution.CMakeAppRunConfiguration;
import com.jetbrains.cidr.cpp.execution.CMakeAppRunConfigurationSettingsEditor;
import com.jetbrains.cidr.cpp.execution.CMakeRunConfigurationType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class BugEaterConfigurationType extends CMakeRunConfigurationType implements DumbAware {

    public static final String TYPE_ID = "com.intellij.bugeater.type";
    public static final String FACTORY_ID = "com.intellij.bugeater.factory";

    public BugEaterConfigurationType() {
        super(TYPE_ID, FACTORY_ID, "Debugger-Stub", "Bug-Eater Debugger Stub",
                new NotNullLazyValue<Icon>() {
                    @NotNull
                    @Override
                    protected Icon compute() {
                        return IconManager.getInstance().getIcon("/icons/bugEaterRunConfig.svg", BugEaterConfigurationType.class);
                    }
                }

        );
    }

    @Override
    protected @NotNull CMakeAppRunConfiguration createRunConfiguration(@NotNull Project project, @NotNull ConfigurationFactory configurationFactory) {
        return new BugEaterConfiguration(project, getConfigurationFactories()[0], "BUG-EATER");
    }

    @Override
    public SettingsEditor<? extends CMakeAppRunConfiguration> createEditor(@NotNull Project project) {
        return new CMakeAppRunConfigurationSettingsEditor(project, getHelper(project));
    }
}
