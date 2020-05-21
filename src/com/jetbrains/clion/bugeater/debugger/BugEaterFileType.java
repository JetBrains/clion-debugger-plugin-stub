package com.jetbrains.clion.bugeater.debugger;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class BugEaterFileType extends LanguageFileType {
  public static final BugEaterFileType INSTANCE = new BugEaterFileType();

  public BugEaterFileType() {
    super(BugEaterLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public String getName() {
    return "BugEaterCommands";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "BugEaterCommands";
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return "BugEaterCommandsFileTypeExtension";
  }

  @Override
  public boolean isReadOnly() {
    return true;
  }

  @Override
  public Icon getIcon() {
    return null;
  }
}