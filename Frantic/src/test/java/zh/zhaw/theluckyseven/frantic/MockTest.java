package zh.zhaw.theluckyseven.frantic;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class MockTest {

    // mock creation
    List mockedList = mock(List.class);

    @Test
    public void simpleTest(){
        // using mock object - it does not throw any "unexpected interaction" exception
        mockedList.add("one");
        mockedList.clear();

        // selective, explicit, highly readable verification
        verify(mockedList).add("one");
        verify(mockedList).clear();
    }
}
