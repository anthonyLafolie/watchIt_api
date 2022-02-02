package com.watchit.api.controller.impl;

import java.util.List;

import com.watchit.api.common.exception.CurrentUserAuthorizationException;
import com.watchit.api.controller.FilterController;
import com.watchit.api.dto.filter.FilterDto;
import com.watchit.api.services.FilterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilterControllerImpl implements FilterController{

    @Autowired
    FilterService filterService;

    @Override
    public ResponseEntity<List<FilterDto>> updateCurrentUserFilters(List<FilterDto> filtersDto) {
        try {
            List<FilterDto> filters_dto_updated = filterService.updateFilters(filtersDto);
            return new ResponseEntity<List<FilterDto>>(filters_dto_updated, HttpStatus.OK);
        } catch (CurrentUserAuthorizationException unfe) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
