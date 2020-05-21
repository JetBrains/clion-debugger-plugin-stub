package com.jetbrains.clion.bugeater.debugger;

import com.intellij.execution.process.BaseOSProcessHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class FakeProcessHandler extends BaseOSProcessHandler {
    public FakeProcessHandler() {
        super(new Process() {
            private final CompletableFuture<Integer> processLock = new CompletableFuture<>();
            @Override
            public OutputStream getOutputStream() {
                return new ByteArrayOutputStream();
            }

            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream("Say something".getBytes());
            }

            @Override
            public InputStream getErrorStream() {
                return new ByteArrayInputStream(new byte[0]);
            }

            @Override
            public int waitFor() throws InterruptedException {
                try {
                    return processLock.get();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public int exitValue() {
                return processLock.getNow(-1);
            }

            @Override
            public void destroy() {
                processLock.complete(0);
            }
        }, "bug-eater", StandardCharsets.UTF_8);
    }

}
