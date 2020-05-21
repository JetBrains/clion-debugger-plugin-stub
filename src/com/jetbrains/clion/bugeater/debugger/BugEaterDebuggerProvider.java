package com.jetbrains.clion.bugeater.debugger;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.jetbrains.cidr.execution.debugger.CidrCustomDebuggerProvider;
import com.jetbrains.cidr.execution.debugger.backend.DebuggerDriverConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class BugEaterDebuggerProvider implements CidrCustomDebuggerProvider {
    @Override
    public boolean isAvailable(@NotNull ExecutionEnvironment executionEnvironment) {
        RunProfile runProfile = executionEnvironment.getRunProfile();
        if( runProfile instanceof RunConfiguration) {
            ConfigurationFactory factory = ((RunConfiguration) runProfile).getFactory();
            return factory != null && BugEaterConfigurationType.FACTORY_ID.equals(factory.getId());
        }
        return false;
    }

    @Override
    public @NotNull List<DebuggerDriverConfiguration> getDebuggerConfigurations() {
        return Collections.singletonList(new BugEaterDebuggerDriverConfiguration());
    }
}
