package com.watchit.api.controller;

import java.util.List;

import com.watchit.api.dto.filter.FilterDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@CrossOrigin
@RequestMapping("/filter")
public interface FilterController {
    /***
     * update Filters of current user
     *
     * @return ResponseEntity<Filters[]> list of filters
     */
    @PostMapping()
    ResponseEntity<List<FilterDto>> updateCurrentUserFilters(@RequestBody List<FilterDto> filtersDto);

}
