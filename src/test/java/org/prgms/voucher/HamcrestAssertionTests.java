package org.prgms.voucher;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class HamcrestAssertionTests {

    @Test
    @DisplayName("여러 hancrest matcher 테스트")
    void hamcrestTest() {
        //junit
        assertEquals(2,1+1);
        //hamcrest
        assertThat(1+1,equalTo(2));
        assertThat(1+1,equalTo(2));
        assertThat(1+1,anyOf(is(1), is(2)));
        
        //junit
        assertNotEquals(1,1+1);
        //hamcrest
        assertThat(1+1,not(equalTo(1)));
    }

    @Test
    @DisplayName("hamcrest는 컬렉션에 대해 테스트하기에 용이")
    void hamcrestListMatcherTest() {
        List<Integer> prices = List.of(1, 2, 3);
        assertThat(prices, hasSize(3));
        assertThat(prices, everyItem(greaterThan(1)));
        assertThat(prices, containsInAnyOrder(3,4,2));
        assertThat(prices, hasItem(2));
        assertThat(prices, hasItem(greaterThan(2)));
    }
}
