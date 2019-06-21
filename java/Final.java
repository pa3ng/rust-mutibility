import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Final {
  public static final void main(final String[] args) {
    final var list = new ArrayList<Integer>();
    System.out.println("list as originally assigned: "+list);
    try {
      list.add(0);
    } catch (Exception e) {
      System.err.println("caught exception: "+e.getClass().getName());
    } finally {
      System.out.println("list after list.add(0): "+list);
    }
    System.out.println();

    final var asList = Arrays.asList(0, 1, 2);
    System.out.println("asList as originally assigned: "+asList);
    try {
      asList.set(0, 6);
    } catch (Exception e) {
      System.err.println("caught exception: "+e.getClass().getName());
    } finally {
      System.out.println("asList after asList.set(0, 6): "+asList);
    }
    try {
      asList.add(3);
    } catch (Exception e) {
      System.err.println("caught exception: "+e.getClass().getName());
    } finally {
      System.out.println("asList after asList.add(3): "+asList);
    }
    System.out.println();

    final var unmodifiableList = Collections.unmodifiableList(Arrays.asList(0, 1, 2));
    System.out.println("unmodifiableList as originally assigned: "+unmodifiableList);
    try {
      unmodifiableList.set(0, 6);
    } catch (Exception e) {
      System.err.println("caught exception: "+e.getClass().getName());
    } finally {
      System.out.println("unmodifiableList after asList.set(0, 6): "+unmodifiableList);
    }
    try {
      unmodifiableList.add(3);
    } catch (Exception e) {
      System.err.println("caught exception: "+e.getClass().getName());
    } finally {
      System.out.println("unmodifiableList after asList.add(3): "+unmodifiableList);
    }
  }
}
