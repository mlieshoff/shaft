package com.mlieshoff.shaft.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.mlieshoff.shaft.ShaftApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.builder.SpringApplicationBuilder;

@ExtendWith(MockitoExtension.class)
class ServletInitializerTest {

    @InjectMocks private ServletInitializer unitUnderTest;

    @Mock private SpringApplicationBuilder springApplicationBuilder;

    @Test
    void configure_whenWithValidParameters_thenConfigureCorrectSources() {
        when(springApplicationBuilder.sources(ShaftApplication.class))
                .thenReturn(springApplicationBuilder);

        SpringApplicationBuilder actual = unitUnderTest.configure(springApplicationBuilder);

        assertThat(actual).isEqualTo(springApplicationBuilder);
    }
}
