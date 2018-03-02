package mt.streams;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class StreamUtils {
    @FunctionalInterface
    public interface FunctionE<T, R, E extends Exception>{
        R apply(T t) throws E;
    }
    @FunctionalInterface
    public interface ConsumerE<T, E extends Exception> {
        void accept(T t) throws E;
    }
    @FunctionalInterface
    public interface PredicateE<T, E extends Exception>{
        boolean test(T t) throws E;
    }

    @FunctionalInterface
    public interface SupplierE<T, E extends Exception>{
        T get() throws E;
    }

    public static<T, E extends Exception> Supplier<T> rethrowSupplier(SupplierE<T,E> supplierE) throws E{
        return sneakyThrowSupplier(supplierE);
    }

    public static<T, E extends Exception> Supplier<T> unthrowSupplier(SupplierE<T,E> supplierE){
        return sneakyThrowSupplier(supplierE);
    }

    private static<T, E extends Exception> Supplier<T> sneakyThrowSupplier(SupplierE<T,E> supplierE){
        return ()->{
            try{
                return supplierE.get();
            }catch (Exception exception){
                sneakyThrows(exception);
                return null;
            }
        };
    }

    public static <T, E extends Exception> Consumer<T> rethrowConsumer(ConsumerE<T, E> consumer) throws E {
        return sneakyThrowConsumer(consumer);
    }

    public static <T, E extends Exception> Consumer<T> unthrowConsumer(ConsumerE<T, E> consumer) {
        return sneakyThrowConsumer(consumer);
    }

    private static <T, E extends Exception> Consumer<T> sneakyThrowConsumer(ConsumerE<T, E> consumer){
        return t -> {
            try {
                consumer.accept(t);
            } catch (Exception exception) {
                sneakyThrows(exception);
            }
        };
    }

    public static <T, R, E extends Exception> Function<T, R> rethrowFunction(FunctionE<T, R, E> inputFunction) throws E{
        return sneakyThrowFunction(inputFunction);
    }

    public static <T, R, E extends Exception> Function<T, R> unthrowFunction(FunctionE<T, R, E> inputFunction){
        return sneakyThrowFunction(inputFunction);
    }
    private static <T, R, E extends Exception> Function<T, R> sneakyThrowFunction(FunctionE<T, R, E> inputFunction){
        return r -> {
            try {
                return inputFunction.apply(r);
            } catch (Exception exception) {
                sneakyThrows(exception);
                return null;
            }
        };
    }
    public static <T, E extends Exception> Predicate<T> rethrowPredicate(PredicateE<T, E> predicate) throws E{
        return sneakyThrowPredicate(predicate);
    }
    public static <T, E extends Exception> Predicate<T> unthrowPredicate(PredicateE<T, E> predicate){
        return sneakyThrowPredicate(predicate);
    }
    private static <T, E extends Exception> Predicate<T> sneakyThrowPredicate(PredicateE<T, E> predicate){
        return t->{
            try{
                return predicate.test(t);
            } catch (Exception e) {
                sneakyThrows(e);
                return false;
            }
        };
    }


    /**
     * Checked exceptions are part of Java, not the JVM. In the bytecode, we can throw any exception from anywhere, without restrictions.
     * Java 8 brought a new type inference rule that states that a throws T is inferred as RuntimeException whenever allowed.
     * This gives the ability to implement sneaky throws without the helper method.
     * A problem with sneaky throws is that you probably want to catch the exceptions eventually,
     * but the Java compiler doesn’t allow you to catch sneakily thrown checked exceptions using exception handler for their particular exception type.
     * @param exception
     * @throws E
     *
     * The COOL part: sneakyThrows is a void it won't return anything;
     * The input is an Exception, it takes it and it rethrows to fullfil the signature
     * BUT
     * The input Exception is CASTED to a GENERIC and this way IS TREATED LIKE A RUNTIME EXCEPTION
     */
    @SuppressWarnings("unchecked")
    public static <E extends Exception> void sneakyThrows(Exception exception) throws E{
        if (exception == null)
            throw new NullPointerException("sneakyThrows: Input throwable cannot be null");
        throw (E)exception;
    }
}
