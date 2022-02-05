package com.watchit.api.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.watchit.api.common.component.IAuthenticationFacade;
import com.watchit.api.common.constant.ExceptionMessage;
import com.watchit.api.common.exception.CurrentUserAuthorizationException;
import com.watchit.api.dto.filter.FilterDto;
import com.watchit.api.entity.Filter;
import com.watchit.api.entity.User;
import com.watchit.api.repository.FilterRepository;
import com.watchit.api.services.FilterService;
import com.watchit.api.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

@ExtendWith(MockitoExtension.class)
public class FilterServiceTest {

    @InjectMocks
    FilterService filterService;

    @Mock
    FilterRepository filterRepository;

    @Mock
    ModelMapper modelMapper;

    @Mock
    IAuthenticationFacade authenticationFacade;

    @Mock
    UserService userService;

    private static final String CREDENTIALS = "test";
    private static final String EMAIL = "test@test.fr";
    private static final Long USER_ID = 1L;

    List<Filter> filters;
    List<FilterDto> filtersDto;
    User user;

    public User buildUser() {
        User user = new User();
        user.setUsername(CREDENTIALS);
        user.setPassword(CREDENTIALS);
        user.setId(USER_ID);
        user.setEmail(EMAIL);
        user.setFilters(filters);
        return user;
    }

    public List<Filter> buildFilters() {
        List<Filter> filters = new ArrayList<>();
        filters.add(buildFilter(0, "action", true));
        filters.add(buildFilter(1, "sci-fi", false));
        return filters;
    }

    public Filter buildFilter(int id, String name, boolean checked) {
        Filter filter = new Filter();
        filter.setName(name);
        filter.setChecked(checked);
        return filter;
    }

    public List<FilterDto> buildFiltersDto() {
        List<FilterDto> filtersDto = new ArrayList<>();
        filtersDto.add(buildFilterDto("action", true));
        filtersDto.add(buildFilterDto("sci-fi", false));
        return filtersDto;
    }

    public FilterDto buildFilterDto(String name, boolean checked) {
        FilterDto filter = new FilterDto();
        filter.setName(name);
        filter.setChecked(checked);
        return filter;
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        filtersDto = buildFiltersDto();
        filters = buildFilters();
        user = buildUser();
    }

    @Test
    public void updateFiltersThrowsCurrentUserAuthorizationExceptionWhenNotAuthentificated()
            throws CurrentUserAuthorizationException {
        when(authenticationFacade.getCurrentUser()).thenThrow(
                new CurrentUserAuthorizationException(ExceptionMessage.CURRENT_USER_CANNOT_BE_AUTHENTICATED));
        assertThatExceptionOfType(CurrentUserAuthorizationException.class)
                .isThrownBy(() -> filterService.updateFilters(filtersDto))
                .withMessage(ExceptionMessage.CURRENT_USER_CANNOT_BE_AUTHENTICATED);
        verify(authenticationFacade).getCurrentUser();
    }

    @Test
    public void updateFilters() throws CurrentUserAuthorizationException {
        when(authenticationFacade.getCurrentUser()).thenReturn(user);
        when(modelMapper.map(filtersDto,  new TypeToken<List<Filter>>() {}.getType())).thenReturn(filters);
        when(filterService.convertFilterDtoToFilter(filtersDto, user)).thenReturn(filters);
        when(userService.getFilters()).thenReturn(filtersDto);
        List<FilterDto> filterDtoResponse = filterService.updateFilters(filtersDto);
        verify(authenticationFacade).getCurrentUser();
        assertEquals(filterDtoResponse, filtersDto);
    }

}