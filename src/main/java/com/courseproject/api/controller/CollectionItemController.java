package com.courseproject.api.controller;

import com.courseproject.api.dto.item.ItemDTO;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.ItemService;
import com.courseproject.api.util.DefaultRequestParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/collections/{collectionId}/items")
public class CollectionItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RestResponse getByCollectionId(
            @RequestParam(value = "page", defaultValue = DefaultRequestParams.PAGE) int page,
            @RequestParam(value = "size", defaultValue = DefaultRequestParams.SIZE) int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortType", defaultValue = "ASC") Sort.Direction sortType,
            @PathVariable Long collectionId
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortType, sortBy));
        Page<ItemDTO> items = itemService.getByCollectionId(collectionId, pageRequest);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(items.getContent());
        response.setLast(items.isLast());
        return response;
    }

}
