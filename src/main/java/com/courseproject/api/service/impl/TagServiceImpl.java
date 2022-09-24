package com.courseproject.api.service.impl;

import com.courseproject.api.entity.Tag;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.repository.TagRepository;
import com.courseproject.api.request.TagRequest;
import com.courseproject.api.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class TagServiceImpl implements TagService {

    private final Locale locale = LocaleContextHolder.getLocale();

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public Page<Tag> getAll(PageRequest pageRequest) {
        return tagRepository.findAll(pageRequest);
    }

    @Override
    public List<Tag> getAllById(Iterable<Long> ids) {
        return tagRepository.findAllById(ids);
    }

    @Override
    public Tag getById(Long id) {
        Object[] arguments = new Object[] { id };
        String message = messageSource.getMessage("tag.notFound", arguments, locale);
        return tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(message));
    }

    @Override
    public Tag save(TagRequest request) {
        Tag tag = new Tag();
        if (request.getName() != null) {
            tag.setName(request.getName());
        }
        return tagRepository.save(tag);
    }

    @Override
    public Tag update(TagRequest request, Long id) {
        Tag tag = getById(id);
        if (request.getName() != null) {
            tag.setName(request.getName());
        }
        return tagRepository.save(tag);
    }

    @Override
    public void destroy(Long id) {
        tagRepository.deleteById(id);
    }

}
