Minimalistic plugin with custom debugger stub
===

[![incubator JetBrains project](http://jb.gg/badges/incubator.svg)](https://github.com/JetBrains#jetbrains-on-github)


Compatibility Disclaimer
---
This is an educational example, compatible with CLion 2020.1. Neither the example author nor JetBrains guarantees
compatibility with upcoming versions.

HOW2USE
---
 - Download and install CLion.
 - Follow the steps described at [Develop plugins for CLion](https://www.jetbrains.com/help/clion/develop-plugins-for-clion.html).
 - Open the project and click *Debug Plugin*. This will start CLion with the plugin installed.
 - In CLion:
   - Create a project of type "C++ Executable".
   - Configure the toolchain as described [here](https://www.jetbrains.com/help/clion/quick-tutorial-on-configuring-clion-on-windows.html).
   - Click the **Debug** button.
   - A fake debugger will be started.

Now all the features of IntelliJ IDEA may be used to develop the plugin functionality.

Fake functionality implemented
---
- Starting the debugger
- Stopping the debugger
- Fake "foo" variable holding "bar" value
- Interrupt/resume execution
- Execution stepping over a *main.cpp* source file
- Fake disassembly

Implementation details
---
There are two approaches to adding new debugger integrations in CLion.

### Using the IntelliJ API
The official way for all IntelliJ-based IDEs is to:
- implement `com.intellij.xdebugger.XDebugProcess` together with the necessary `com.intellij.execution.runners.ProgramRunner`
for the "Debug" action to pick up your debugger,
- register a new `com.intellij.xdebugger.breakpoints.XLineBreakpointType`
so that users can put breakpoints in the desired file types,
- implement `com.intellij.xdebugger.attach.XAttachDebuggerProvider` for attaching to already running processes, and
- make lots of other small customizations along the way.

The benefit is that you control each and every little detail, and use the official and stable platform API, which is also
[open-source](https://github.com/JetBrains/intellij-community/tree/201/platform/xdebugger-api/src/com/intellij/xdebugger).
Of course, the downside is that it's a huge amount of work with little to no relation to the real task - making a new native
debugger work in CLion.
Also, the whole new debugger stack should be integrated with the existing CLion C/C++ file types and run configurations,
which is a whole other task on its own.

### Using the CIDR / CLion API
This is the approach demonstrated in this example plugin. Instead of re-implementing the high-level code responsible 
for integration with the IntelliJ Platform, here we implement a `com.jetbrains.cidr.execution.debugger.backend.DebuggerDriver`
subclass, which is a lower-level API responsible for communication with the actual debugger process.

The pros and cons of this approach are the opposite: you only implement the relevant business logic, but in order to do 
that, you have to use the "unofficial" API, which is subject to change, as already stated before.


Further development steps
---
Major functionality:
  * Implement all the **//todo**s in `com.jetbrains.clion.bugeater.debugger.BugEaterDriver` class

Minor things:
  * Update *resources/META-INF/plugin.xml* with an actual name, description, etc.
  * Update *resources/META-INF/pluginIcon.svg*.
  * Update *resources/icons/bugEaterRunConfig.svg*. Please keep the size unchanged (16x16 px).
