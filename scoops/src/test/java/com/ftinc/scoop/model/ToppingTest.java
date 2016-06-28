package com.ftinc.scoop.model;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Project: ThemeEngineTest
 * Package: com.ftinc.scoop.model
 * Created by drew.heavner on 6/24/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class ToppingTest {

    private static final int MOCK_ID = 1000;
    private static final String MOCK_NAME = "Primary";
    private static final int MOCK_DEFAULT_COLOR = -1;

    Topping mToppingMock;

    @Before
    public void setup() throws Exception{
        mToppingMock = new Topping(MOCK_ID, MOCK_NAME, MOCK_DEFAULT_COLOR);
    }

    @Test
    public void getId() throws Exception {
        int id = mToppingMock.getId();
        assertThat(id, is(MOCK_ID));
    }

    @Test
    public void getName() throws Exception {
        String name = mToppingMock.getName();
        assertThat(name, is(MOCK_NAME));
    }

    @Test
    public void getColor() throws Exception {
        int color = mToppingMock.getColor();
        assertThat(color, is(MOCK_DEFAULT_COLOR));
    }

    @Test
    public void getPreviousColor() throws Exception {
        int color = mToppingMock.getColor();
        assertThat(color, is(MOCK_DEFAULT_COLOR));
    }

    @SuppressWarnings("ResourceAsColor")
    @Test
    public void updateColor() throws Exception {
        int newColor = -123456;
        mToppingMock.updateColor(newColor);

        int color = mToppingMock.getColor();
        int prevColor = mToppingMock.getPreviousColor();

        assertThat(color, is(newColor));
        assertThat(prevColor, is(MOCK_DEFAULT_COLOR));
    }

    @Test
    public void equals() throws Exception {
        Topping t1 = new Topping(MOCK_ID, MOCK_NAME, MOCK_DEFAULT_COLOR);
        assertEquals(mToppingMock, t1);

        // Test first constructor
        Topping t2 = new Topping(MOCK_ID, MOCK_NAME);
        Topping t3 = new Topping(MOCK_ID, MOCK_NAME);
        assertEquals(t2, t3);
    }

}