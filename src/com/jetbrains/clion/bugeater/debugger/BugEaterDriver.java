package com.jetbrains.clion.bugeater.debugger;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.BaseProcessHandler;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.jetbrains.cidr.execution.Installer;
import com.jetbrains.cidr.execution.debugger.backend.*;
import com.jetbrains.cidr.execution.debugger.memory.Address;
import com.jetbrains.cidr.execution.debugger.memory.AddressRange;
import com.jetbrains.cidr.system.HostMachine;
import com.jetbrains.cidr.system.LocalHost;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("RedundantThrows")
public class BugEaterDriver extends DebuggerDriver {

    private final AtomicInteger lastId = new AtomicInteger(1);
    private final List<LLThread> fakeThreads = Collections.singletonList(new LLThread(1));
    private String fakeSrcFileName = "main.cpp";
    private int fakeSrcFileLine = 4;
    private final BaseProcessHandler<?> myProcessHandler;

    public BugEaterDriver(@NotNull Handler handler) {
        super(handler);
        /* This fake process handler supposed to be real debugger OS process handler
         * Use com.jetbrains.cidr.execution.debugger.backend.DebuggerDriverConfiguration.createDebugProcessHandler
         * to create real handler
         */
        myProcessHandler = new FakeProcessHandler();
    }

    @Override
    public boolean supportsWatchpointLifetime() {
        return false;
    }

    @Override
    public @NotNull Language getConsoleLanguage() {
        return BugEaterLanguage.INSTANCE;
    }

    @Override
    public @NotNull BaseProcessHandler<?> getProcessHandler() {
        return myProcessHandler;
    }

    @Override
    public boolean isInPromptMode() {
        return false;
    }

    @Override
    public @NotNull HostMachine getHostMachine() {
        return LocalHost.INSTANCE;
    }

    @Override
    public void setValuesFilteringEnabled(boolean enabled) throws ExecutionException {

    }

    /**
     *
     * @param installer information about binary file to be loaded
     * @param architecture target architecture, probably to be ignored
     * @return Inferior to load binary to the target platform
     * @throws ExecutionException something goes wrong
     */
    @Override
    public @NotNull Inferior loadForLaunch(@NotNull Installer installer, @Nullable String architecture) throws ExecutionException {
        //todo
        Project project = ProjectUtil.guessProjectForFile(VfsUtil.findFileByIoFile(installer.getExecutableFile(), true));
        return new FakeInferior(project);
    }

    @Override
    public @NotNull Inferior loadForAttach(int pid) throws ExecutionException {
        throw new ExecutionException("Not supported");
    }

    @Override
    public @NotNull Inferior loadForAttach(@NotNull String name, boolean wait) throws ExecutionException {
        throw new ExecutionException("Attaching by name is not supported");
    }

    /** User presses "Pause Program" button.
     * {@link #handleInterrupted} supposed to be called asynchronously when actual pause happened
     */
    @Override
    public boolean interrupt() throws ExecutionException {
        handleInterrupted(new StopPlace(new LLThread(1),
                new LLFrame(1, "main", null, 0, 0xFF00FFL, null, false)
        ));
        //todo
        return true;
    }

    /** User presses "Resume Program" button.
     * {@link #handleRunning} supposed to be called asynchronously when actual resume happened
     */
    @Override
    public boolean resume() throws ExecutionException {
        handleRunning();
        handleTargetOutput("Say Something?\n", ProcessOutputTypes.STDOUT);
        handleTargetOutput("Something!\n", ProcessOutputTypes.STDERR);
        //todo
        return true;
    }

    /** User presses "Step Over" button.
     * {@link #handleRunning} supposed to be called asynchronously when actual resume happened
     * {@link #handleInterrupted} supposed to be called asynchronously when pause after the step happened
     */
    @Override
    public void stepOver(@Nullable Boolean stepByInstruction) throws ExecutionException {
        handleRunning();
        fakeSrcFileLine = (fakeSrcFileLine + 1) % 6;
        handleInterrupted(new StopPlace(new LLThread(1),
                new LLFrame(1, "main", fakeSrcFileName, fakeSrcFileLine, 0xFF00FFL, null, false))
        );
        //todo
    }

    /** Step over
     * @see #stepOver
     *
     */
    @Override
    public void stepInto(boolean forceStepIntoFramesWithNoDebugInfo, @Nullable Boolean stepByInstruction) throws ExecutionException {
        stepOver(stepByInstruction);
        //todo
    }

    /** Step out
     * @see #stepOver
     *
     */
    @Override
    public void stepOut() throws ExecutionException {
        stepOver(false);
        //todo
    }

    /** Run to source file line
     * @see #stepOver
     *
     */
    @Override
    public void runTo(@NotNull String path, int line) throws ExecutionException {
        handleRunning();
        //todo
    }

    /** Run to PC address
     * @see #stepOver
     *
     */
    @Override
    public void runTo(@NotNull Address address) throws ExecutionException {
        handleRunning();
        //todo
    }

    /** Perform debugger exit
     * @see #stepOver
     *
     */
    @Override
    protected boolean doExit() throws ExecutionException {
        /* This handleXXX methods supposed to be called asynchronously, when actual debugger is stopped */
        handleExited(0);
        //todo
        return true;
    }

    /** Autocomplete support for debugger console
     *
     */
    @Override
    public @NotNull ResultList<String> completeConsoleCommand(@NotNull String command, int pos) throws ExecutionException {
        return new ResultList<>(Collections.emptyList(), false);
    }

    /**
     * Watchpoint handling
     */
    @Override
    public @NotNull LLWatchpoint addWatchpoint(long threadId,
                                               int frameIndex,
                                               LLValue value,
                                               String expr,
                                               LLWatchpoint.Lifetime lifetime,
                                               LLWatchpoint.AccessType accessType) throws ExecutionException, DebuggerCommandException {
        //todo
        return new LLWatchpoint(lastId.incrementAndGet(), expr);
    }

    /**
     * Watchpoint handling
     */
    @Override
    public void removeWatchpoint(List<Integer> ids) throws ExecutionException, DebuggerCommandException {

    }

    /** User adds a breakpoint
     * {@link #handleBreakpointAdded} supposed to be called asynchronously when done
     */
    @Override
    public @NotNull LLBreakpoint addBreakpoint(String path, int line, @Nullable String condition)
            throws ExecutionException, DebuggerCommandException {
        //todo
        return new LLBreakpoint(lastId.incrementAndGet(), path, line, condition, null);
    }

    /** User adds a symbolic breakpoint
     */
    @Override
    public @Nullable LLSymbolicBreakpoint addSymbolicBreakpoint(@NotNull SymbolicBreakpoint symBreakpoint)
            throws ExecutionException, DebuggerCommandException {
        //todo
        return new LLSymbolicBreakpoint(lastId.incrementAndGet());
    }

    /** User removes symbolic or line breakpoint
     * {@link #handleBreakpointRemoved(LLBreakpoint)} supposed to be called asynchronously when done
     */
    @Override
    public void removeCodepoints(@NotNull Collection<Integer> ids) throws ExecutionException, DebuggerCommandException {
        //todo
    }

    /** List of threads. For instance, RTOS tasks
     */
    @Override
    public @NotNull List<LLThread> getThreads() throws ExecutionException, DebuggerCommandException {
        //todo
        return fakeThreads;
    }

    /** Stack trace for a thread
     */
    @Override
    public @NotNull ResultList<LLFrame> getFrames(long threadId, int from, int count, boolean untilFirstLineWithCode)
            throws ExecutionException, DebuggerCommandException {
        //todo
        return new ResultList<>(Collections.singletonList(
                new LLFrame(1, "main", fakeSrcFileName, fakeSrcFileLine, 0xFF00FFL, null, false)
        ), false);
    }

    /** List of available variables
     */
    @Override
    public @NotNull List<LLValue> getVariables(long threadId, int frameIndex) throws ExecutionException, DebuggerCommandException {
        return Collections.singletonList(new LLValue("foo", "char*", null, "foo"));
    }

    /** Read value of a variable
     */
    @Override
    public @NotNull LLValueData getData(@NotNull LLValue value) throws ExecutionException, DebuggerCommandException {
        //todo
        return new LLValueData("foo".equals(value.getName()) ? "\"bar\"" : "<->", null, false, false, false);
    }

    /** Read description of a variable
     */
    @Override
    public @Nullable String getDescription(@NotNull LLValue value, int maxLength) throws ExecutionException, DebuggerCommandException {
        //todo
        return null;
    }

    /** Unions, structures, or classes are hierarchical. This method help to obtain the hierarchy
     */
    @Override
    public @Nullable Integer getChildrenCount(@NotNull LLValue value) throws ExecutionException, DebuggerCommandException {
        //todo
        return null;
    }

    /** Unions, structures, or classes are hierarchical. This method help to obtain the hierarchy
     */
    @Override
    public @NotNull ResultList<LLValue> getVariableChildren(LLValue value, int from, int count)
            throws ExecutionException, DebuggerCommandException {
        //todo
        throw new DebuggerCommandException("Not implemented");
    }

    /** Expression evaluation
     */
    @Override
    public @NotNull LLValue evaluate(long threadId, int frameIndex, @NotNull String expression, @Nullable DebuggerLanguage language)
            throws ExecutionException, DebuggerCommandException {
        //todo
        if (expression.equals(new LLValue("foo", "char*", null, "foo").getName())) {
            return new LLValue("foo", "char*", null, "foo");
        } else {
            return new LLValue("<unknown>", "<unknown>", null, "<unknown>");
        }
    }

    @Override
    public @NotNull List<LLInstruction> disassembleFunction(@NotNull Address address, @NotNull AddressRange fallbackRange)
            throws ExecutionException, DebuggerCommandException {
        //todo
        return
                Arrays.asList(
                        LLInstruction.create(address, "55", "FAKE.NOP", "No-op", null),
                        LLInstruction.create(address.plus(1), "AA", "FAKE.RET", "Return", null)
                );

    }

    @Override
    public @NotNull List<LLInstruction> disassemble(@NotNull AddressRange range) throws ExecutionException, DebuggerCommandException {
        //todo
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<LLMemoryHunk> dumpMemory(@NotNull AddressRange range) throws ExecutionException, DebuggerCommandException {
        //todo
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<LLModule> getLoadedModules() throws ExecutionException, DebuggerCommandException {
        //todo
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<LLSection> getModuleSections(@NotNull LLModule module) throws ExecutionException, DebuggerCommandException {
        //todo
        return Collections.emptyList();
    }

    @Override
    public @NotNull ShellCommandResult executeShellCommand(@NotNull String executable,
                                                           @Nullable List<String> params,
                                                           @Nullable String workingDir,
                                                           int timeoutSecs) throws ExecutionException {
        throw new ExecutionException("Shell is not Supported");
    }

    @Override
    public void executeConsoleCommand(String command) throws ExecutionException, DebuggerCommandException {
        executeConsoleCommand(-1, -1, command);
    }

    @Override
    public void executeConsoleCommand(long threadId, int frameIndex, String text) throws ExecutionException, DebuggerCommandException {
        // not supported?
    }

    @Override
    public void handleSignal(String signalName, boolean stop, boolean pass, boolean notify)
            throws ExecutionException, DebuggerCommandException {
        //todo
    }

    @Override
    protected String getPromptText() {
        return "bug-eater";
    }

    /** Verify if driver is in OK state
     *
     * @throws ExecutionException if something is wrong
     */
    @Override
    public void checkErrors() throws ExecutionException {
        //todo
    }

    /** Load compiled binary with debug information into debugger engine(but not into target platform)
     *
     */
    @Override
    public void addSymbolsFile(File file, File module) throws ExecutionException {
        //todo
    }

    private class FakeInferior extends Inferior {

        private FakeInferior(@Nullable Project project) {
            super(com.jetbrains.clion.bugeater.debugger.BugEaterDriver.this.lastId.incrementAndGet());
            if (project!=null && project.getBasePath() != null) {
                fakeSrcFileName = Paths.get(project.getBasePath(), "main.cpp").toString();
            }
        }

        @Override
        protected long startImpl() throws ExecutionException {
            //todo These methods are supposed to be called asynchronously when actual debugger is started
            handleAttached(0);
            handleRunning();
            return 0;
        }

        @Override
        protected void detachImpl() throws ExecutionException {
            //todo These methods are supposed to be called asynchronously when actual debugger is stopped
            handleDetached();
        }

        @Override
        protected boolean destroyImpl() throws ExecutionException {
            //todo These methods are supposed to be called asynchronously when actual debugger is disconnected
            handleDisconnected();
            return true;
        }
    }
}

