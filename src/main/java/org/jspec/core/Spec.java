package org.jspec.core;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

public class Spec implements Executor {

  public Spec(Description desc, Block block) {
    this.desc = desc;
    this.block = block;
  }

  @Override
  public void exec(RunNotifier notifier) {
    notifier.fireTestStarted(desc);
    runSpec(notifier);
    notifier.fireTestFinished(desc);
  }

  private void runSpec(RunNotifier notifier) {
    try {
      block.apply();
    } catch (Throwable t) {
      notifier.fireTestFailure(new Failure(desc, t));
    }
  }

  private Description desc;
  private Block block;
}
