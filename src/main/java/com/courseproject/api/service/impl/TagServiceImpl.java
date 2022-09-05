package com.courseproject.api.service.impl;

import com.courseproject.api.dto.TagDTO;
import com.courseproject.api.entity.Tag;
import com.courseproject.api.exception.ResourceNotFoundException;
import com.courseproject.api.repository.TagRepository;
import com.courseproject.api.request.TagRequest;
import com.courseproject.api.service.TagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class TagServiceImpl implements TagService {

    private final Locale locale = LocaleContextHolder.getLocale();

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ModelMapper modelMapper;

    private TagDTO convertToDTO(Tag tag) {
        return modelMapper.map(tag, TagDTO.class);
    }

    @Override
    public Page<TagDTO> getAll(PageRequest pageRequest) {
        return tagRepository.findAll(pageRequest).map(this::convertToDTO);
    }

    @Override
    public TagDTO findById(Long id) {
        Object[] arguments = new Object[] { id };
        String message = messageSource.getMessage("tag.notFound", arguments, locale);
        Tag tag = tagRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(message)
        );
        return convertToDTO(tag);
    }

    @Override
    public TagDTO store(TagRequest request) {
        Tag tag = new Tag();
        if (request.getName() != null) {
            tag.setName(request.getName());
        }
        Tag newTag = tagRepository.save(tag);
        return convertToDTO(newTag);
    }

    @Override
    public TagDTO update(TagRequest request, Long id) {
        Object[] arguments = new Object[] { id };
        String message = messageSource.getMessage("tag.notFound", arguments, locale);
        Tag tag = tagRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(message)
        );
        if (request.getName() != null) {
            tag.setName(request.getName());
        }
        Tag newTag = tagRepository.save(tag);
        return convertToDTO(newTag);
    }

    @Override
    public void destroy(Long id) {
        tagRepository.deleteById(id);
    }

}
