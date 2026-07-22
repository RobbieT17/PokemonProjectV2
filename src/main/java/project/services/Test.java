package project.services;

public class Test {

    public static int foo(int value) {
        return (int) Math.floor((double) value / 2);
    }
    
    public static void main(String[] args) {

        for (int i = -6; i <= 6; i++) {
            if (i == 0) {
                continue;
            }

            System.out.println(String.format("%d -> %d", i, foo(i)));
        }
  
    }
}
