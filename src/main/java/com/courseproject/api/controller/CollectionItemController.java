package com.courseproject.api.controller;

import com.courseproject.api.dto.ItemDTO;
import com.courseproject.api.response.RestResponse;
import com.courseproject.api.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collections/{collectionId}/items")
public class CollectionItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResponse getByCollectionId(@PathVariable Long collectionId) {
        List<ItemDTO> items = itemService.getByCollectionId(collectionId);
        RestResponse response = new RestResponse();
        response.setMessage("OK");
        response.setData(items);
        return response;
    }

}
