package de.mle.stackoverflow.mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SpyTest {
    @Spy
    private MyTestClass myTestClass;

    @Test
    public void spyInsteadOfPowermock() {
        when(myTestClass.getObject()).thenReturn(Integer.valueOf(3));

        assertThat(myTestClass.doSomethingMethod()).isEqualTo("3");
    }

    class MyTestClass{
        public String doSomethingMethod(){
            return getObject().toString();
        }

        Object getObject() {
            return new Object();
        }
    }
}
