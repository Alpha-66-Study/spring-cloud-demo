package com.dabinzi.jdk;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class Jdk {

  public static void main(String[] args) throws Exception {
    line("JDK 9");
    jdk9_collections_stream_optional();

    line("JDK 10");
    jdk10_var();

    line("JDK 11");
    jdk11_string_and_http();

    line("JDK 12–14");
    jdk12_14_switch_and_helpfulNpe();

    line("JDK 15");
    jdk15_text_blocks();

    line("JDK 16");
    jdk16_instanceof_and_toList();
    jdk16_record();

    line("JDK 17");
    jdk17_sealed_and_switchOnSealed();

    line("JDK 18");
    jdk18_utf8_default();

    line("JDK 21");
    jdk21_virtual_threads_and_sequenced_and_switchPattern();
  }

  // ───────────────────────────────── JDK 9 ─────────────────────────────────

  static void jdk9_collections_stream_optional() {
    System.out.println("[集合工厂方法 List.of / Set.of / Map.of]");

    // JDK8 旧写法：需要中间集合 + 包装为不可变
    // List<String> a = Collections.unmodifiableList(new ArrayList<>(Arrays.asList("A","B","C")));
    // 容易被误改源集合，写法冗长

    // ✅ JDK9 新写法：一次性创建不可变集合（内容固定、读多写少的配置/常量场景）
    List<String> list = List.of("A", "B", "C");
    Set<String> set = Set.of("X", "Y", "Z");
    Map<String, Integer> map = Map.of("k1", 1, "k2", 2);
    System.out.println(list);
    System.out.println(set);
    System.out.println(map);

    System.out.println("\n[Stream API: takeWhile / dropWhile / ofNullable]");

    var nums = List.of(1, 2, 3, 4, 5, 6, 7);
    // JDK8：想“取到条件不满足为止”往往得加标志/短路逻辑，笨重
    // ✅ JDK9：直接表达“前缀满足/丢弃”
    System.out.println("takeWhile < 4 → " + nums.stream().takeWhile(n -> n < 4).toList()); // [1,2,3]
    System.out.println("dropWhile < 4 → " + nums.stream().dropWhile(n -> n < 4).toList()); // [4,5,6,7]

    // ✅ JDK9：优雅处理可能为 null 的源（避免手写 null 判定流）
    String maybeNull = null;
    System.out.println("ofNullable(null) → " + Stream.ofNullable(maybeNull).toList()); // []
    System.out.println("ofNullable(\"hi\") → " + Stream.ofNullable("hi").toList());    // ["hi"]

    System.out.println("\n[Optional 增强: ifPresentOrElse / stream / or]");
    Optional<String> present = Optional.of("ok");
    Optional<String> empty = Optional.empty();
    // JDK8：if (opt.isPresent()) {...} else {...}
    // ✅ JDK9：更语义化的分支
    present.ifPresentOrElse(
        v -> System.out.println("存在 → " + v),
        () -> System.out.println("不存在")
    );
    // ✅ JDK9：Optional → Stream（配合管道操作更顺滑）
    System.out.println("present.stream() → " + present.stream().toList());
    // ✅ JDK9：提供“备选”Optional
    System.out.println("empty.or(...) → " + empty.or(() -> Optional.of("fallback")).get());
  }

  // ───────────────────────────────── JDK 10 ────────────────────────────────

  static void jdk10_var() {
    System.out.println("[var：局部变量类型推断]");

    // JDK8：必须写出显式类型
    // String s = "hello";

    // ✅ JDK10：编译器根据右值推断（仅限局部变量/for增强变量；不要滥用影响可读性）
    var s = "hello var";
    var map = Map.of("k", 1);
    System.out.println(s + " / " + map);
  }

  // ───────────────────────────────── JDK 11 ────────────────────────────────

  static void jdk11_string_and_http() throws Exception {
    System.out.println("[String API: isBlank / strip / lines / repeat]");

    String raw = "  Hello \u3000\nWorld  "; // 包含全角空白
    // JDK8：trim() 仅处理 ASCII 空白，判断空白常用 trim().isEmpty()
    // ✅ JDK11：isBlank/strip 支持 Unicode 空白；lines/ repeat 更便捷
    System.out.println("isBlank(\"   \") → " + "   ".isBlank());
    System.out.println("strip → [" + raw.strip() + "]");
    System.out.println("lines → " + raw.lines().map(String::strip).toList());
    System.out.println("repeat → " + "ha".repeat(3));

    System.out.println("\n[HTTP Client：替代 HttpURLConnection]");

    // JDK8：HttpURLConnection 或三方库
    // ✅ JDK11：标准库现代化 HTTP，异步/同步都可
    HttpResponse<String> rsp;
    try (HttpClient client = HttpClient.newHttpClient()) {
      HttpRequest req = HttpRequest.newBuilder(URI.create("https://httpbin.org/get")).build();
      rsp = client.send(req, HttpResponse.BodyHandlers.ofString());
    }
    System.out.println("HTTP status = " + rsp.statusCode());
    System.out.println("Content-Type = " + rsp.headers().firstValue("content-type").orElse("?"));
  }

  // ──────────────────────────────── JDK 12–14 ─────────────────────────────

  static void jdk12_14_switch_and_helpfulNpe() {
    System.out.println("[switch 表达式（JDK14 正式）]");
    int day = 6;
    // JDK8：语句式 switch + break；不能直接返回值
    // ✅ JDK14：表达式式 switch，可直接产生值，箭头语法避免穿透
    String type = switch (day) {
      case 1, 2, 3, 4, 5 -> "工作日";
      case 6, 7 -> "周末";
      default -> "未知";
    };
    System.out.println("结果 → " + type);

    System.out.println("\n[更有用的 NPE 信息（JDK14）]");
    try {
      String s = null;
      // JDK14 之前：仅告诉你这一行 NPE
      // ✅ JDK14：会显示 "Cannot invoke String.length() because 's' is null"
      int len = s.length();
      System.out.println(len);
    } catch (NullPointerException e) {
      System.out.println("NPE 提示 → " + e.getMessage());
    }
  }

  // ───────────────────────────────── JDK 15 ────────────────────────────────

  static void jdk15_text_blocks() {
    System.out.println("[文本块 Text Blocks（JDK15 正式）]");

    // JDK8：多行字符串需要 \n 拼接，或手动缩进
    // ✅ JDK15：""" 三引号，所见即所得，配合 .formatted() 更优雅
    String json = """
        {
          "name": "%s",
          "age": %d
        }
        """.formatted("Alice", 20);
    System.out.println(json);
  }

  // ───────────────────────────────── JDK 16 ────────────────────────────────

  static void jdk16_instanceof_and_toList() {
    System.out.println("[instanceof 模式匹配（JDK16 正式）]");

    Object obj = "hello";
    // JDK8：先判断 instanceof，再强转
    // ✅ JDK16：一条语句完成判断+绑定
    if (obj instanceof String s) {
      System.out.println("大写 → " + s.toUpperCase());
    }

    System.out.println("\n[Stream.toList()（JDK16）]");
    // JDK8：collect(Collectors.toList()) → 可变、具体类型实现不保证
    // ✅ JDK16：stream().toList() → 返回不可变 List（更安全，适合作为返回值）
    var doubled = Stream.of(1, 2, 3).map(n -> n * 2).toList();
    System.out.println(doubled);
    try {
      doubled.add(8); // 不可变，抛异常
    } catch (UnsupportedOperationException e) {
      System.out.println("toList() 返回不可变集合");
    }
  }

  static void jdk16_record() {
    System.out.println("[record 数据类（JDK16 正式）]");
    // JDK8：需要写字段、构造器、getter、equals/hashCode/toString
    // ✅ JDK16：一行定义不可变数据载体，自动生成元方法；可自定义校验/方法
    record User(String name, int age) {
      User {
        if (age < 0) throw new IllegalArgumentException("age < 0");
      }

      boolean adult() {
        return age >= 18;
      }
    }
    User u = new User("Bob", 22);
    System.out.println(u + ", adult=" + u.adult());
  }

  // ───────────────────────────────── JDK 17 ────────────────────────────────

  static void jdk17_sealed_and_switchOnSealed() {
    System.out.println("[sealed（密封类/接口，JDK17 正式） + switch 联动]");

    // 场景价值：限定可继承/实现的子类型集合，让模式匹配更“完备”（更安全）。
    Shape s1 = new Circle(2.0);
    Shape s2 = new Rectangle(3.0, 4.0);

    System.out.println("area(s1) = " + area(s1));
    System.out.println("area(s2) = " + area(s2));
  }

  // sealed 家族（作为内部类型更直观）
  sealed interface Shape permits Circle, Rectangle {
  }

  static final class Circle implements Shape {
    final double r;

    Circle(double r) {
      this.r = r;
    }
  }

  static final class Rectangle implements Shape {
    final double w, h;

    Rectangle(double w, double h) {
      this.w = w;
      this.h = h;
    }
  }

  static double area(Shape s) {
    // 对密封层级做 switch：编译器知道穷尽的子类型集合 → 可省 default
    return switch (s) {
      case Circle c -> Math.PI * c.r * c.r;
      case Rectangle r -> r.w * r.h;
    };
  }

  // ───────────────────────────────── JDK 18 ────────────────────────────────

  static void jdk18_utf8_default() {
    System.out.println("[UTF-8 成为默认字符集（JDK18）]");
    // 多平台一致性更好，避免“本机默认编码”带来的坑
    System.out.println("default charset = " + java.nio.charset.Charset.defaultCharset());
  }

  // ───────────────────────────────── JDK 21 ────────────────────────────────

  static void jdk21_virtual_threads_and_sequenced_and_switchPattern() throws Exception {
    System.out.println("[虚拟线程 Virtual Threads（JDK21 正式）]");

    // 价值：I/O 密集型场景可创建海量轻量线程，极大简化“异步回调地狱”，用同步风格写并发。
    // 写法 1：快速启动一个虚拟线程
    Thread vt = Thread.ofVirtual().name("vt-1").start(() ->
        System.out.println("运行在线程: " + Thread.currentThread()));
    vt.join();

    // 写法 2：每任务一个虚拟线程的执行器（自动关闭）
    try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
      var futures = new ArrayList<java.util.concurrent.Future<?>>();
      for (int i = 0; i < 5; i++) {
        int id = i;
        futures.add(exec.submit(() -> "task#" + id + " → " + Thread.currentThread().isVirtual()));
      }
      for (var f : futures) System.out.println(f.get());
    }

    System.out.println("\n[SequencedCollection（JDK21）：有序集合统一 API]");
    // 价值：List/Deque/LinkedHashMap 等获得一致的“首尾操作 + 反转视图”
    var list = new ArrayList<>(List.of("a", "b", "c"));
    list.addFirst("FIRST");
    list.addLast("LAST");
    System.out.println("getFirst=" + list.getFirst() + ", getLast=" + list.getLast());
    System.out.println("reversed view → " + list.reversed());

    System.out.println("\n[switch 模式匹配（JDK21 正式）：类型匹配 + 守卫条件]");
    Object any = 42;
    String desc = switch (any) {
      case String s -> "String len=" + s.length();
      case Integer i when i > 0 -> "Positive int " + i;
      case Integer i -> "Non-positive int " + i;
      case List<?> l when l.isEmpty() -> "Empty list";
      case List<?> l -> "List size=" + l.size();
      default -> "Other: " + any;
    };
    System.out.println(desc);
  }

  // ──────────────────────────────── 小工具 ────────────────────────────────

  static void line(String title) {
    System.out.println("\n" + "=".repeat(16) + " " + title + " " + "=".repeat(16));
  }
}
