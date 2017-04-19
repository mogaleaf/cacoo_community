package com.mogaleaf.community.api;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestsApiControllerTest {

    RequestsAppliController instance = new RequestsAppliController();


    @Test
      public void requestPopularBadArgTest() {
        boolean catchEx = false;
        try {
            instance.popular("A");
        } catch (IllegalArgumentException ex) {
            catchEx = true;
        }
        assertThat(catchEx).isTrue();
    }

    @Test
    public void requestRecentBadArgTest() {
        boolean catchEx = false;
        try {
            instance.recent("A");
        } catch (IllegalArgumentException ex) {
            catchEx = true;
        }
        assertThat(catchEx).isTrue();
    }
}
