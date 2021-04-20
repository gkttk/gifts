package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/users", produces = "application/json")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable long id) {
        UserDto foundUser = userService.findById(id);
        return ResponseEntity.ok(foundUser);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll(WebRequest request,
                                               @RequestParam(required = false, defaultValue = Integer.MAX_VALUE + "") @Min(value = 0, message = "Limit parameter must be greater or equal 0") Integer limit,
                                               @RequestParam(required = false, defaultValue = "0") @Min(value = 0, message = "Offset parameter must be greater or equal 0") Integer offset) {
        Map<String, String[]> reqParams = request.getParameterMap();
        List<UserDto> tags = userService.findAllForQuery(reqParams, limit, offset);
        return ResponseEntity.ok(tags);
    }



}