package com.courseproject.api.controller;

import com.courseproject.api.dto.ItemDTO;
import com.courseproject.api.request.ItemRequest;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.ItemService;
import com.courseproject.api.util.DefaultRequestParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse index(
            @RequestParam(value = "page", defaultValue = DefaultRequestParams.PAGE) int page,
            @RequestParam(value = "size", defaultValue = DefaultRequestParams.SIZE) int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortType", defaultValue = "ASC") Sort.Direction sortType
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortType, sortBy));
        Page<ItemDTO> items = itemService.getAll(pageRequest);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(items);
        return response;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse show(@PathVariable Long id) {
        ItemDTO item = itemService.getById(id);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(item);
        return response;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public RestResponse store(@Valid @ModelAttribute ItemRequest request) throws IOException {
        ItemDTO item = itemService.store(request);
        RestResponse response = new RestResponse();
        response.setMessage("Item created successfully!");
        response.setData(item);
        return response;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse update(@Valid @ModelAttribute ItemRequest request, @PathVariable Long id) throws IOException {
        ItemDTO item = itemService.update(request, id);
        RestResponse response = new RestResponse();
        response.setMessage("Item updated successfully!");
        response.setData(item);
        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse destroy(@PathVariable Long id) throws IOException {
        itemService.destroy(id);
        RestResponse response = new RestResponse();
        response.setMessage("Item deleted successfully!");
        return response;
    }

    @DeleteMapping("/{itemId}/images")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse destroyImage(@PathVariable Long itemId) throws IOException {
        itemService.destroyImage(itemId);
        RestResponse response = new RestResponse();
        response.setMessage("Image deleted successfully!");
        return response;
    }

    @PutMapping("/{itemId}/likes/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse likes(@PathVariable Long itemId, @PathVariable Long userId) {
        ItemDTO item = itemService.likes(itemId, userId);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(item);
        return response;
    }

}
