package com.courseproject.api.controller;

import com.courseproject.api.dto.ItemDTO;
import com.courseproject.api.request.ItemRequest;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public RestResponse store(@Valid @ModelAttribute ItemRequest request) throws IOException {
        ItemDTO item = itemService.store(request);
        RestResponse response = new RestResponse();
        response.setMessage("Item created successfully!");
        response.setContent(item);
        return response;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse update(@Valid @ModelAttribute ItemRequest request, @PathVariable Long id) throws IOException {
        ItemDTO item = itemService.update(request, id);
        RestResponse response = new RestResponse();
        response.setMessage("Item updated successfully!");
        response.setContent(item);
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

}
