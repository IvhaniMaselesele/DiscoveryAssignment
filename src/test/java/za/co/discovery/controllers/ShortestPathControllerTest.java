package za.co.discovery.controllers;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import za.co.discovery.dataAccess.PlanetDAO;
import za.co.discovery.services.FileReadingService;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class ShortestPathControllerTest {
    MockMvc mockMvc;

    @Test
    public void rootReturnsIndexPage() throws Exception {
        setupFixture();
        mockMvc.perform(get("/"))
                .andExpect(view().name("index"));
    }

    public void setupFixture() {
        mockMvc = standaloneSetup(
                new ShortestPathController(new FileReadingService(mock(PlanetDAO.class))))
                .setViewResolvers(getInternalResourceViewResolver())
                .build();
    }

    private InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
        return viewResolver;
    }
}