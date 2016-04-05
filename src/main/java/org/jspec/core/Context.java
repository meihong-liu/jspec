package org.jspec.core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

import static org.junit.runner.Description.createTestDescription;

import static org.jspec.core.Block.*;

public class Context implements Executor {

  private List<Block> befores = new ArrayList<>();
  private List<Block> afters = new ArrayList<>();
  private Deque<Executor> executors = new ArrayDeque<>();
  private Description desc;

  public Context(Description desc) {
    this.desc = desc;
  }

  @Override
  public void exec(RunNotifier notifier) {
    for (Executor e : executors) {
      e.exec(notifier);
    }
  }

  public void addChild(Context child) {
    desc.addChild(child.desc);
    executors.add(child);
    addFixture(child);
  }

  private void addFixture(Context child) {
    child.addBefore(collect(befores));
    child.addAfter(collect(afters));
  }

  public void addBefore(Block block) {
    befores.add(block);
  }

  public void addAfter(Block block) {
    afters.add(block);
  }

  public void addSpec(String behavior, Block block) {
    Description spec = createTestDescription(desc.getClassName(), behavior);
    desc.addChild(spec);
    addExecutor(spec, block);
  }

  private void addExecutor(Description desc, Block block) {
    Spec spec = new Spec(desc, blocksInContext(block));
    executors.add(spec);
  }

  private Block blocksInContext(Block block) {
    return collect(collect(befores), block, collect(afters));
  }
}
