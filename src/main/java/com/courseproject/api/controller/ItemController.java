package com.courseproject.api.controller;

import com.courseproject.api.dto.item.ItemDTO;
import com.courseproject.api.request.ItemRequest;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.ItemService;
import com.courseproject.api.util.DefaultRequestParams;
import com.courseproject.api.validator.IsAllowedItemID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/items")
@Validated
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/search/{key}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse search(@PathVariable String key) {
        List<ItemDTO> items = itemService.search(key);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(items);
        return response;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RestResponse index(
            @RequestParam(value = "page", defaultValue = DefaultRequestParams.PAGE) int page,
            @RequestParam(value = "size", defaultValue = Integer.MAX_VALUE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortType", defaultValue = "ASC") Sort.Direction sortType
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortType, sortBy));
        Page<ItemDTO> items = itemService.getAll(pageRequest);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(items.getContent());
        response.setLast(items.isLast());
        return response;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse show(@PathVariable Long id) {
        ItemDTO item = itemService.getById(id);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(item);
        return response;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestResponse store(@Valid @ModelAttribute ItemRequest request, Locale locale) throws IOException {
        ItemDTO item = itemService.store(request);
        String message = messageSource.getMessage("item.created", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        response.setData(item);
        return response;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse update(
            @Valid @ModelAttribute ItemRequest request,
            @PathVariable @IsAllowedItemID Long id, Locale locale) throws IOException {
        ItemDTO item = itemService.update(request, id);
        String message = messageSource.getMessage("item.updated", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        response.setData(item);
        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse destroy(@PathVariable @IsAllowedItemID Long id, Locale locale) throws IOException {
        itemService.destroy(id);
        String message = messageSource.getMessage("item.deleted", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        return response;
    }

    @DeleteMapping("/{id}/images")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse destroyImage(@PathVariable @IsAllowedItemID Long id, Locale locale) throws IOException {
        itemService.destroyImage(id);
        String message = messageSource.getMessage("image.deleted", null, locale);
        RestResponse response = new RestResponse();
        response.setMessage(message);
        return response;
    }

    @PutMapping("/{itemId}/likes/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse likes(@PathVariable Long itemId, @PathVariable Long userId) {
        ItemDTO item = itemService.likes(itemId, userId);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(item);
        return response;
    }

}
