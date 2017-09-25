package ticket;

public class Main {
    private static final Object EXAMPLE_DATA = C.readString.invoke(
            "{\"users\" [{\"name\" \"John\" \"password\" \"abc123\"} " +
                        "{\"name\" \"Suzy\" \"password\" \"correcthorsebatterystaple\"}]}");
    static final Object state = C.atom.invoke(new Data(EXAMPLE_DATA));

    public interface Swapper {
        Data swap(Data oldState);
    }

    // DEMO: how to use the data objects
    public static void main(String[] args) {
        System.out.println(C.deref.invoke(state));

        runInTransaction((data) -> {
            User john = data.getUser(0);
            System.out.println(john);
            System.out.println(john.getName());
            System.out.println(john.getPassword());

            john = john.setPassword("mynewpassword")
                       .setName(john.getName() + " Smith");

            System.out.println(john);
            return data.commit(john);
        });

        runInTransaction((data) ->  data.createUser("Fred", "ilikecheese", 2));

        System.out.println(C.deref.invoke(state));
    }

    public static void runInTransaction(Swapper swapper) {
        C.swap.invoke(Main.state, C.swapperToFn, swapper);
    }
}
