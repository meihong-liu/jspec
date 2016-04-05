# JSpec: A Simple BDD Framework in Java8

## Quick Start

```java
@RunWith(JSpec.class)
public class JSpecs {{
  describe("A spec", () -> {
    List<String> items = new ArrayList<>();

    before(() -> {
      items.add("foo");
      items.add("bar");
    });

    after(() -> {
      items.clear();
    });

    it("runs the before() blocks", () -> {
      assertThat(items, contains("foo", "bar"));
    });

    describe("when nested", () -> {
      before(() -> {
        items.add("baz");
      });

      it("runs before and after from inner and outer scopes", () -> {
        assertThat(items, contains("foo", "bar", "baz"));
      });
    });
  });
}}
```
