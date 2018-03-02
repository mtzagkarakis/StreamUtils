package mt.stream;

import mt.streams.StreamUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamUtilsTest {
    private class CheckedException extends Exception{private static final long serialVersionUID = 5432972796197909724L;}
    private void doNothing(){}
    @Test(expected=CheckedException.class)
    public void testFunctionThatThrowException() throws CheckedException{
        Stream.of(1,2,3)
            .map(StreamUtils.rethrowFunction(this::functionThatThrowsChecked))
            .forEach(n-> doNothing());
    }

    @Test(expected=CheckedException.class)
    public void testPredicateThatThrowException() throws CheckedException{
        Stream.of(1,2,3)
            .filter(StreamUtils.rethrowPredicate(this::predicateThatThrowsChecked))
            .forEach(n-> doNothing());
    }

    @Test(expected=CheckedException.class)
    public void testConsumerThatThrowException() throws CheckedException{
        Stream.of(1,2,3).forEach(StreamUtils.rethrowConsumer(this::consumerThatThrowsChecked));
    }

    @Test(expected = CheckedException.class)
    public void testSupplierThatThrowException() throws CheckedException{
        StreamUtils.rethrowSupplier(this::supplierThatThrowsChecked).get();
    }

    @Test(expected=CheckedException.class)
    public void testFunctionThatThrowExceptionWithoutChecking(){
        Stream.of(1,2,3)
                .map(StreamUtils.unthrowFunction(this::functionThatThrowsChecked))
                .forEach(n-> doNothing());
    }

    @Test(expected=CheckedException.class)
    public void testPredicateThatThrowExceptionWithoutChecking(){
        Stream.of(1,2,3)
                .filter(StreamUtils.unthrowPredicate(this::predicateThatThrowsChecked))
                .forEach(n-> doNothing());
    }

    @Test(expected=CheckedException.class)
    public void testConsumerThatThrowExceptionWithoutChecking(){
        Stream.of(1,2,3).forEach(StreamUtils.unthrowConsumer(this::consumerThatThrowsChecked));
    }

    @Test(expected=CheckedException.class)
    public void testSupplierThatThrowExceptionWithoutCheckingInAOptionalOrElseGet(){
        StreamUtils.unthrowSupplier(this::supplierThatThrowsChecked).get();
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
        Assert.assertEquals(3, result.size());
    }
    @Test
    public void testPredicateNoException(){
        List<Integer> result = Stream.of(1,2,3).filter(StreamUtils.rethrowPredicate(e->true)).collect(Collectors.toList());
        Assert.assertEquals(3, result.size());
    }
    @Test
    public void testConsumerNoException(){
        List<Integer> anotherList = new ArrayList<>();
        Stream.of(1,2,3).forEach(StreamUtils.rethrowConsumer(anotherList::add));
        Assert.assertEquals(3, anotherList.size());
    }

    @Test
    public void testSupplierNoException(){
        Assert.assertEquals(10, (long)StreamUtils.rethrowSupplier(()->10).get());
    }
}
