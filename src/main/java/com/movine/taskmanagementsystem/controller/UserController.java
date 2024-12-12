package com.movine.taskmanagementsystem.controller;

import com.movine.taskmanagementsystem.exceptions.ResourceNotFoundException;
import com.movine.taskmanagementsystem.model.Task;
import com.movine.taskmanagementsystem.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final TaskService taskService;
    private final PagedResourcesAssembler<Task> pagedResourcesAssembler;

    @GetMapping("/users/{userId}/tasks")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get tasks for a user with priority sorting, pagination, and filtering",
            description = "Returns tasks for a user sorted by priority, with pagination support and optional filtering.")
    @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    public ResponseEntity<EntityModel<PagedModel<EntityModel<Task>>>> getUserTasks(
            @PathVariable @Min(1) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") String direction) {

        try {
            Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy != null ? sortBy : "priority");
            Page<Task> taskPage = taskService.getTasksByUserWithPriority(userId, PageRequest.of(page, size, sort));

            if (taskPage.isEmpty()) {
                throw new ResourceNotFoundException("No tasks found for user with ID: " + userId);
            }

            PagedModel<EntityModel<Task>> pagedModel = pagedResourcesAssembler.toModel(taskPage, task -> EntityModel.of(task,
                    linkTo(methodOn(TaskController.class).getTask(task.getId())).withSelfRel(),
                    linkTo(methodOn(UserController.class).getUserTasks(userId, page, size, sortBy, direction)).withRel("userTasks")));
            EntityModel<PagedModel<EntityModel<Task>>> entityModel = EntityModel.of(pagedModel);

            entityModel.add(linkTo(methodOn(UserController.class).getUserTasks(userId, page, size, sortBy, direction)).withSelfRel());
            if (!taskPage.isEmpty()) {
                entityModel.add(linkTo(methodOn(TaskController.class).getTask(taskPage.getContent().get(0).getId())).withRel("firstTask"));
            }

            return ResponseEntity.ok(entityModel);
        }  catch (ResourceNotFoundException e) {
            log.error("Resource not found: {}", e.getMessage());
            // Return an EntityModel of a String for the message
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(EntityModel.of(PagedModel.of(Collections.emptyList(), new PagedModel.PageMetadata(0, 0, 0, 0))));
        } catch (IllegalArgumentException e) {
            log.error("Invalid input parameters: {}", e.getMessage());
            // Return an EntityModel of a String for the message
            return ResponseEntity.badRequest()
                    .body(EntityModel.of(PagedModel.of(Collections.emptyList(), new PagedModel.PageMetadata(0, 0, 0,0))));
        } catch (Exception e) {
            log.error("An error occurred while fetching tasks for user {}: {}", userId, e.getMessage(), e);
            // Return an EntityModel of a String for the message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(EntityModel.of(PagedModel.of(Collections.emptyList(), new PagedModel.PageMetadata(0, 0, 0, 0))));
        }
    }


}