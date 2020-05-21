package com.jetbrains.clion.bugeater.debugger;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.runners.RunConfigurationWithSuppressedDefaultRunAction;
import com.intellij.openapi.project.Project;
import com.jetbrains.cidr.cpp.execution.CMakeAppRunConfiguration;

public class BugEaterConfiguration extends CMakeAppRunConfiguration  implements RunConfigurationWithSuppressedDefaultRunAction {
    public BugEaterConfiguration(Project project, ConfigurationFactory factory, String name) {
        super(project, factory, name);
    }

}
