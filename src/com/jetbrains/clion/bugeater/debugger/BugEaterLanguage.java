package com.jetbrains.clion.bugeater.debugger;

import com.intellij.lang.Language;

public class BugEaterLanguage extends Language {
    public static final BugEaterLanguage INSTANCE = new BugEaterLanguage();

    private BugEaterLanguage() {
        super("BUG-EATER");
    }
}
