package org.jspec.core;

import java.util.ArrayDeque;
import java.util.Deque;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

import static org.junit.runner.Description.createSuiteDescription;
import static org.jspec.core.Block.*;

public final class JSpec extends Runner {
  private static Deque<Context> ctxts = new ArrayDeque<Context>();

  static {
    ctxts.push(new Context(createSuiteDescription("JSpec: All Tests")));
  }

  private Description desc;
  private Context root;

  public JSpec(Class<?> suite) {
    desc = createSuiteDescription(suite);
    root = new Context(desc);
    enterCtxt(root, reflect(suite));
  }

  @Override
  public Description getDescription() {
    return desc;
  }

  @Override
  public void run(RunNotifier notifier) {
    root.exec(notifier);
  }

  public static void describe(String desc, Block block) {
    Context ctxt = new Context(createSuiteDescription(desc));
    enterCtxt(ctxt, block);
  }

  public static void it(String behavior, Block block) {
    currentCtxt().addSpec(behavior, block);
  }

  public static void beforeEach(Block block) {
    currentCtxt().addBefore(block);
  }

  public static void afterEach(final Block block) {
    currentCtxt().addAfter(block);
  }

  private static void enterCtxt(Context ctxt, Block block) {
    currentCtxt().addChild(ctxt);
    applyBlock(ctxt, block);
  }

  private static void applyBlock(Context ctxt, Block block) {
    ctxts.push(ctxt);
    doApplyBlock(block);
    ctxts.pop();
  }

  private static void doApplyBlock(Block block) {
    try {
      block.apply();
    } catch (Throwable e) {
      it("happen to an error", failing(e));
    }
  }

  private static Context currentCtxt() {
    return ctxts.peek();
  }
}
