package com.jetbrains.clion.bugeater.debugger;


import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.util.Expirable;
import com.intellij.openapi.util.Pair;
import com.jetbrains.cidr.ArchitectureType;
import com.jetbrains.cidr.execution.debugger.CidrStackFrame;
import com.jetbrains.cidr.execution.debugger.backend.*;
import com.jetbrains.cidr.execution.debugger.evaluation.EvaluationContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BugEaterDebuggerDriverConfiguration  extends DebuggerDriverConfiguration {
    public BugEaterDebuggerDriverConfiguration() {
    }

    @Override
    public @NotNull String getDriverName() {
        return "BUG-EATER";
    }

    @Override
    public @NotNull DebuggerDriver createDriver(@NotNull DebuggerDriver.Handler handler,
                                                @NotNull ArchitectureType architectureType) throws ExecutionException {
        return new BugEaterDriver(handler);
    }

    @Override
    public @NotNull GeneralCommandLine createDriverCommandLine(@NotNull DebuggerDriver debuggerDriver, @NotNull ArchitectureType architectureType) throws ExecutionException {
        return new GeneralCommandLine("bug-eater");
    }

    @Override
    public @NotNull EvaluationContext createEvaluationContext(@NotNull DebuggerDriver debuggerDriver, @Nullable Expirable expirable, @NotNull CidrStackFrame cidrStackFrame) {
        return new EvaluationContext(debuggerDriver,expirable,cidrStackFrame) {
            @Override
            public @NotNull String convertToRValue(@NotNull LLValueData llValueData, @NotNull Pair<LLValue, String> pair) throws DebuggerCommandException, ExecutionException {
                return cast(pair.getSecond(), pair.getFirst().getType());
            }
        } ;
    }
}
