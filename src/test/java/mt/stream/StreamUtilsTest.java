package mt.stream;

import mt.streams.StreamUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamUtilsTest {
    private static class CheckedException extends Exception{}
    private void doNothing(){}
    @Test
    public void testFunctionThatThrowException(){
        Assertions.assertThrows(CheckedException.class, () ->
                Stream.of(1,2,3)
                        .map(StreamUtils.rethrowFunction(this::functionThatThrowsChecked))
                        .forEach(n-> doNothing()));
    }

    @Test
    public void testPredicateThatThrowException(){
        Assertions.assertThrows(CheckedException.class, () ->
                Stream.of(1,2,3)
                        .filter(StreamUtils.rethrowPredicate(this::predicateThatThrowsChecked))
                        .forEach(n-> doNothing()));
    }

    @Test
    public void testConsumerThatThrowException(){
        Assertions.assertThrows(CheckedException.class, () -> Stream.of(1,2,3).forEach(StreamUtils.rethrowConsumer(this::consumerThatThrowsChecked)));
    }

    @Test
    public void testSupplierThatThrowException(){
        Assertions.assertThrows(CheckedException.class, () -> StreamUtils.rethrowSupplier(this::supplierThatThrowsChecked).get());
    }

    @Test
    public void testFunctionThatThrowExceptionWithoutChecking(){
        Assertions.assertThrows(CheckedException.class, () ->
                Stream.of(1,2,3)
                        .map(StreamUtils.unthrowFunction(this::functionThatThrowsChecked))
                        .forEach(n-> doNothing()));
    }

    @Test
    public void testPredicateThatThrowExceptionWithoutChecking(){
        Assertions.assertThrows(CheckedException.class, () ->
                Stream.of(1,2,3)
                        .filter(StreamUtils.unthrowPredicate(this::predicateThatThrowsChecked))
                        .forEach(n-> doNothing()));
    }

    @Test
    public void testConsumerThatThrowExceptionWithoutChecking(){
        Assertions.assertThrows(CheckedException.class, () -> Stream.of(1,2,3).forEach(StreamUtils.unthrowConsumer(this::consumerThatThrowsChecked)));
    }

    @Test
    public void testSupplierThatThrowExceptionWithoutCheckingInAOptionalOrElseGet(){
        Assertions.assertThrows(CheckedException.class, () -> StreamUtils.unthrowSupplier(this::supplierThatThrowsChecked).get());
    }

    private Integer functionThatThrowsChecked(Integer o) throws CheckedException{
        throw new CheckedException();
    }
    private boolean predicateThatThrowsChecked(Integer o) throws CheckedException{
        throw new CheckedException();
    }
    private void consumerThatThrowsChecked(Integer o) throws CheckedException{
        throw new CheckedException();
    }
    private Integer supplierThatThrowsChecked() throws CheckedException{
        throw new CheckedException();
    }

    @Test
    public void testFunctionNoException(){
        List<Integer> result = Stream.of(1,2,3).map(StreamUtils.rethrowFunction(e->e)).collect(Collectors.toList());
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void testPredicateNoException(){
        List<Integer> result = Stream.of(1,2,3).filter(StreamUtils.rethrowPredicate(e->true)).collect(Collectors.toList());
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void testConsumerNoException(){
        List<Integer> anotherList = new ArrayList<>();
        Stream.of(1,2,3).forEach(StreamUtils.rethrowConsumer(anotherList::add));
        Assertions.assertEquals(3, anotherList.size());
    }

    @Test
    public void testSupplierNoException(){
        Assertions.assertEquals(10, (long)StreamUtils.rethrowSupplier(()->10).get());
    }
}
